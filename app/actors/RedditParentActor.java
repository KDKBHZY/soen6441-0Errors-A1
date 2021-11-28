package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Timeout;
import play.libs.akka.InjectedActorSupport;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;

public class RedditParentActor extends AbstractActor implements InjectedActorSupport {
    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);
    private final String query;

    private final RedditActor.Factory childFactory;

    /**
     * Create the default UserParentActor
     * Called by the WebSocketController
     * Runs a default search on the keyword "test"
     * @param childFactory factory to create a UserActor
     */
    @Inject
    public RedditParentActor(RedditActor.Factory childFactory) {
        this.childFactory = childFactory;
        this.query = "test"; // default keyword
    }



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.UserParentActorCreate.class, create -> {
                    ActorRef child = injectedChild(() -> childFactory.create(create.id), "userActor-" + create.id);
                    CompletionStage<Object> future = ask(child, new Messages.WatchSearchResults(query), timeout);
                    pipe(future, context().dispatcher()).to(sender());
                }).build();
    }
}
