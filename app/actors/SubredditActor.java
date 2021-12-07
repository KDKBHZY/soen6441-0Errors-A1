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
import com.google.inject.Injector;
import models.Reddit;
import play.libs.Json;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
/**
 * get message from subredditparentactor
 * @author: Zeyu Huang
 */

public class SubredditActor extends AbstractActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private Map<String, UniqueKillSwitch> searchResultsMap = new HashMap<>();

    private Map<String, ActorRef> searchsubredditActors;

    private Materializer mat;

    private Sink<JsonNode, NotUsed> hubSink;

    private Sink<JsonNode, CompletionStage<Done>> jsonSink;

    private Flow<JsonNode, JsonNode, NotUsed> websocketFlow;

    private Injector injector;

    /**
     * Default empty constructor for the tests
     */
    public SubredditActor() {
        searchsubredditActors = null;
        mat = null;
        hubSink = null;
        websocketFlow = null;
        injector = null;
    }

    /**
     * Regular constructor
     * @param injector Guice Injector, used later to create the SearchResultsActor with GuiceInjectedActor
     * @param mat Materializer for the Akka streams
     */
    @Inject
    public SubredditActor(Injector injector, Materializer mat) {
        this.searchsubredditActors = new HashMap<>();
        this.mat = mat;
        this.injector = injector;
        createSink();
    }

    /**
     * Create the Akka Sink
     */
    public void createSink() {
        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                        .toMat(BroadcastHub.of(JsonNode.class, 256), Keep.both())
                        .run(mat);

        hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        jsonSink = Sink.foreach((JsonNode json) -> {
            // When the user types in a stock in the upper right corner, this is triggered,
            String queryRequest = json.findPath("query").asText();
            askForStatuses(queryRequest);
        });

        // Put the source and sink together to make a flow of hub source as output (aggregating all
        // searchResults as JSON to the browser) and the actor as the sink (receiving any JSON messages
        // from the browser), using a coupled sink and source.
        this.websocketFlow = Flow.fromSinkAndSourceCoupled(jsonSink, hubSource)
                .watchTermination((n, stage) -> {
                    // Stop the searchResultsActors
                    searchsubredditActors.forEach((query, actor) -> stage.thenAccept(f -> context().stop(actor)));

                    // When the flow shuts down, make sure this actor also stops.
                    stage.thenAccept(f -> context().stop(self()));

                    return NotUsed.getInstance();
                });
    }

    /**
     * If there already exists a SearchResultsActor for the keyword we want, ask it for updates
     * Otherwise, create a new one, register the UserActor and wait the results
     * @param query
     */
    private void askForStatuses(String query) {
        ActorRef actorForQuery = searchsubredditActors.get(query);
        System.out.println("!!!subreddit:"+query);
        if (actorForQuery != null) {
            actorForQuery.tell(new Messages.WatchsubRedditResults(query), self());
        } else {
            actorForQuery = getContext().actorOf(Props.create(GuiceInjectedActor.class, injector,
                    SubredditResultActor.class));
            searchsubredditActors.put(query, actorForQuery);
            actorForQuery.tell(new Messages.RegisterActor(), self());
            actorForQuery.tell(new Messages.WatchsubRedditResults(query), self());
        }
    }

    /**
     * The receive block, useful if other actors want to manipulate the flow.
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.WatchsubRedditResults.class, watchsubRedditResults -> {
                    logger.info("!!!!Received message WatchsubRedditResults {}", watchsubRedditResults);
                    if (watchsubRedditResults != null) {
                        // Ask the searchResultsActors for a stream containing these searchResults
                        askForStatuses(watchsubRedditResults.query);
                        sender().tell(websocketFlow, self());
                    }
                })
                .match(Messages.UnwatchSearchResults.class, unwatchSearchResults -> {
                    logger.info("Received message UnwatchSearchResults {}", unwatchSearchResults);
                    if (unwatchSearchResults != null) {
//                        searchResultsMap.get(unwatchSearchResults.query).shutdown();
//                        searchResultsMap.remove(unwatchSearchResults.query);
                    }
                })
                .match(Messages.RedditsMessage.class, message -> {
                    System.out.println("Received subreddit StatusesMessage:  "+message);
                    if (message != null) {
                        addStatuses(message);
                        sender().tell(message, self());
                    }
                })
                .build();
    }

    /**
     * Adds a statuses to the hub.
     * @param message StatusesMessage message contaning the query and the statuses
     */
    public void addStatuses(Messages.RedditsMessage message) {
        List<Reddit> reddits = message.reddits;
        String query = message.query;
        Source<JsonNode, NotUsed> getSource = Source.from(reddits)
                .map(Json::toJson);

        // Set up a flow that will let us pull out a killswitch for this specific stock,
        // and automatic cleanup for very slow subscribers (where the browser has crashed, etc).
        final Flow<JsonNode, JsonNode, UniqueKillSwitch> killswitchFlow = Flow.of(JsonNode.class)
                .joinMat(KillSwitches.singleBidi(), Keep.right());
        // Set up a complete runnable graph from the stock source to the hub's sink
        String name = "searchsubreddit-" + query;
        final RunnableGraph<UniqueKillSwitch> graph = getSource
                .viaMat(killswitchFlow, Keep.right())
                .to(hubSink)
                .named(name);

        // Start it up!
        UniqueKillSwitch killSwitch = graph.run(mat);

        // Pull out the kill switch so we can stop it when we want to unwatch a stock.
        searchResultsMap.put(query, killSwitch);
    }

    /**
     * Factory interface to create a SubredditActor from theS ubredditParentActor
     */
    public interface Factory {
        Actor create(String id);
    }

    /**
     * Setter for Materializer
     * @param mat Materializer
     */
    public void setMat(Materializer mat) {
        this.mat = mat;
    }

    /**
     * Getter for the SearchResultsMap
     * @return a Map containing the kill switches for a query
     */
    public Map<String, UniqueKillSwitch> getSearchResultsMap() {
        return searchResultsMap;
    }

    /**
     * Setter for the SearchResultsMap
     * @param searchResultsMap SearchResultsMap
     */
    public void setSearchResultsMap(Map<String, UniqueKillSwitch> searchResultsMap) {
        this.searchResultsMap = searchResultsMap;
    }

    /**
     * Getter for the Materializer
     * @return Materializer
     */
    public Materializer getMat() {
        return mat;
    }

    /**
     * Getter for the json sink
     * @return jsonSink a Sink of JsonNodes and CompletionStage of Done
     */
    public Sink<JsonNode, CompletionStage<Done>> getJsonSink() {
        return jsonSink;
    }

    /**
     * Setter for the json sink
     * @param jsonSink Sink of JsonNode and CompletionStage of Done
     */
    public void setJsonSink(Sink<JsonNode, CompletionStage<Done>> jsonSink) {
        this.jsonSink = jsonSink;
    }

    /**
     * Getter for the SearchResultsActor map
     * @return searchResultsActors Map of String and ActorRef a map of actor references for a given query
     */
    public Map<String, ActorRef> getSearchResultsActors() {
        return searchsubredditActors;
    }

    /**
     * Setter for the SearchResultsActor map
     * @param searchResultsActors Map of String and ActorRef a map of actor references for a given query
     */
    public void setSearchResultsActors(Map<String, ActorRef> searchResultsActors) {
        this.searchsubredditActors = searchResultsActors;
    }
}
