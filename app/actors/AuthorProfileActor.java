package actors;

import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akka.stream.KillSwitches;
import akka.stream.Materializer;
import akka.stream.UniqueKillSwitch;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Injector;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class AuthorProfileActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private Map<String, UniqueKillSwitch> authorProfileMap = new HashMap<>();

    private Map<String, ActorRef> authorProfileActors;

    private Materializer mat;

    private Sink<JsonNode, NotUsed> hubSink;

    private Sink<JsonNode, CompletionStage<Done>> jsonSink;

    private Flow<JsonNode, JsonNode, NotUsed> websocketFlow;

    private Injector injector;

    /**
     * Default empty constructor for the tests
     */
    public AuthorProfileActor() {
        this.authorProfileActors = null;
        this.mat = null;
        this.hubSink = null;
        this.websocketFlow = null;
        this.injector = null;
    }

    /**
     * Regular constructor of AuthorProfileActor
     * @param injector Guice Injector for creating AuthorProfileActor with GuiceInjectedActor
     * @param mat Materialize for Akka streams
     */
    @Inject
    public AuthorProfileActor(Injector injector, Materializer mat) {
        this.authorProfileActors = new HashMap<>();
        this.mat = mat;
        this.injector = injector;
        createSink();
    }

    /**
     * Create Akka Sink
     */
    public void createSink() {
        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                        .toMat(BroadcastHub.of(JsonNode.class, 256), Keep.both())
                        .run(mat);

        this.hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        jsonSink = Sink.foreach((JsonNode json) -> {
            String queryRequest = json.findPath("query").asText();
            askForAuthorProfile(queryRequest);
        });

        this.websocketFlow = Flow.fromSinkAndSourceCoupled(jsonSink, hubSource)
                .watchTermination((n, stage) -> {
                    authorProfileActors.forEach((query, actor) ->
                            stage.thenAccept(f -> context().stop(actor)));
                    stage.thenAccept(f -> context().stop(self()));

                    return NotUsed.getInstance();
                });
    }

    /**
     * Updates the existing AuthorProfileActor.
     * Otherwise, create and register a new AuthorProfileActor
     * @param queryRequest author name
     */
    private void askForAuthorProfile(String queryRequest) {
        ActorRef actorRef = authorProfileActors.get(queryRequest);
        logger.info("update author: " + queryRequest);

        if (actorRef != null) {
            actorRef.tell(new Messages.WatchAuthorProfileResults(queryRequest), self());
        } else {
            actorRef = getContext().actorOf(Props.create(GuiceInjectedActor.class, injector, AuthorProfileResultActor.class));
            authorProfileActors.put(queryRequest, actorRef);
            actorRef.tell(new Messages.RegisterActor(), self());
            actorRef.tell(new Messages.WatchAuthorProfileResults(queryRequest), self());
        }
    }

    /**
     * Receive block
     * @return receiveBuilder
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.WatchAuthorProfileResults.class, watchAuthorProfileResults -> {
                    logger.info("!!!!Received message WatchAuthorProfileResults {}", watchAuthorProfileResults);
                    if (watchAuthorProfileResults != null) {
                        askForAuthorProfile(watchAuthorProfileResults.query);
                        sender().tell(websocketFlow, self());
                    }
                })
                .match(Messages.UnwatchSearchResults.class, unwatchSearchResults -> {
                    logger.info("Received message UnwatchSearchResults {}", unwatchSearchResults);
                    if (unwatchSearchResults != null) {
                        authorProfileMap.get(unwatchSearchResults.query).shutdown();
                        authorProfileMap.remove(unwatchSearchResults.query);
                    }
                })
                .match(Messages.AuthorProfileMessage.class, authorProfileMessage -> {
                    logger.info("Received AuthorProfile Message:  " + authorProfileMessage);
                    if (authorProfileMessage != null) {
                        addAuthorProfileMessages(authorProfileMessage);
                        sender().tell(authorProfileMessage, self());
                    }
                }).build();
    }

    /**
     * Add a authorProfileMessage to the hub
     * @param authorProfileMessage authorProfileMessage that contains the author profile and author name
     */
    private void addAuthorProfileMessages(Messages.AuthorProfileMessage authorProfileMessage) {
        Source<JsonNode, NotUsed> getSource = Source.single(authorProfileMessage.author).map(Json::toJson);
        final Flow<JsonNode, JsonNode, UniqueKillSwitch> killSwitchFlow = Flow.of(JsonNode.class)
                .joinMat(KillSwitches.singleBidi(), Keep.right());

        String authorName = authorProfileMessage.authorName;
        String runnableName = "authorProfile-" + authorName;
        final RunnableGraph<UniqueKillSwitch> graph = getSource
                .viaMat(killSwitchFlow, Keep.right())
                .to(hubSink)
                .named(runnableName);

        authorProfileMap.put(authorName, graph.run(mat));
    }

    /**
     * Factory interface to create an Actor from the ParentActor
     */
    public interface Factory {
        Actor create(String id);
    }

    /**
     * Getter of authorProfileMap
     * @return authorProfileMap
     */
    public Map<String, UniqueKillSwitch> getAuthorProfileMap() {
        return authorProfileMap;
    }

    /**
     * Setter of authorProfileMap
     * @param authorProfileMap authorProfileMap
     */
    public void setAuthorProfileMap(Map<String, UniqueKillSwitch> authorProfileMap) {
        this.authorProfileMap = authorProfileMap;
    }

    /**
     * Getter of authorProfileActors
     * @return authorProfileActors
     */
    public Map<String, ActorRef> getAuthorProfileActors() {
        return authorProfileActors;
    }

    /**
     * Setter of authorProfileActors
     * @param authorProfileActors authorProfileActors
     */
    public void setAuthorProfileActors(Map<String, ActorRef> authorProfileActors) {
        this.authorProfileActors = authorProfileActors;
    }

    /**
     * Getter of Materializer
     * @return Materializer
     */
    public Materializer getMat() {
        return mat;
    }

    /**
     * Setter of Materializer
     * @param mat Materializer
     */
    public void setMat(Materializer mat) {
        this.mat = mat;
    }

    /**
     * Getter of hubSink
     * @return hubSink
     */
    public Sink<JsonNode, NotUsed> getHubSink() {
        return hubSink;
    }

    /**
     * Setter of hubSink
     * @param hubSink hubSink
     */
    public void setHubSink(Sink<JsonNode, NotUsed> hubSink) {
        this.hubSink = hubSink;
    }

    /**
     * Getter of jsonSink
     * @return jsonSink
     */
    public Sink<JsonNode, CompletionStage<Done>> getJsonSink() {
        return jsonSink;
    }

    /**
     * Setter of jsonSink
     * @param jsonSink jsonSink
     */
    public void setJsonSink(Sink<JsonNode, CompletionStage<Done>> jsonSink) {
        this.jsonSink = jsonSink;
    }
}
