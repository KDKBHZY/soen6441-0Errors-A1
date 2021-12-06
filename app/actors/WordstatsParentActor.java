package actors;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.util.Timeout;
import play.libs.akka.InjectedActorSupport;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class WordstatsParentActor extends AbstractActor implements InjectedActorSupport {
    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);
    private final String query;

    private final WordstatsActor.Factory childFactory;

    /**
     * Create the default UserParentActor
     * Called by the WebSocketController
     * Runs a default search on the keyword "test"
     * @param childFactory factory to create a UserActor
     */
    @Inject
    public WordstatsParentActor(SubredditActor.Factory childFactory) {
        this.query = "test";
        this.childFactory = childFactory;
    }



    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.WordstatsActorCreate.class, create -> {
                    ActorRef child = injectedChild(() -> childFactory.create(create.id), "WordstatsActor-" + create.id);
                    CompletionStage<Object> future = ask(child, new Messages.WatchwordstatsResults(query), timeout);
                    pipe(future, context().dispatcher()).to(sender());

                })
                .build();
    }
}
