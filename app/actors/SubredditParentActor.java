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
/**
 * get message form controller and creat subredditactor
 * @author: Zeyu Huang
 */

public class SubredditParentActor extends AbstractActor implements InjectedActorSupport {
    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);
    private final String query;

    private final SubredditActor.Factory childFactory;

    /**
     * Create the default UserParentActor
     * Called by the WebSocketController
     * Runs a default search on the keyword "test"
     * @param childFactory factory to create a UserActor
     */
    @Inject
    public SubredditParentActor(SubredditActor.Factory childFactory) {
        this.query = "test";
        this.childFactory = childFactory;
    }



    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Messages.SubredditActorCreate.class, create -> {
                    System.out.println("!!!!subreddit: "+query);
                        ActorRef child = injectedChild(() -> childFactory.create(create.id), "subRedditActor-" + create.id);
                        CompletionStage<Object> future = ask(child, new Messages.WatchsubRedditResults(query), timeout);
                        pipe(future, context().dispatcher()).to(sender());

                })
                .build();
    }
}
