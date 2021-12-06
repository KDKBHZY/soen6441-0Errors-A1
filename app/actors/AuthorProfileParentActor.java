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

public class AuthorProfileParentActor extends AbstractActor implements InjectedActorSupport {

    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);
    private final String query;
    private final AuthorProfileActor.Factory childFactory;

    /**
     * Create AuthorProfileActor
     * @param childFactory factory to create an AuthorProfileActor
     */
    @Inject
    public AuthorProfileParentActor(AuthorProfileActor.Factory childFactory) {
        this.childFactory = childFactory;
        this.query = "test";
    }

    /**
     * Receive block
     * @return receiveBuilder
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.AuthorProfileActorCreate.class, create -> {
                    ActorRef child = injectedChild(() -> childFactory.create(create.id), "authorProfileActor-" + create.id);
                    CompletionStage<Object> future = ask(child, new Messages.WatchAuthorProfileResults(query), timeout);
                    pipe(future, context().dispatcher()).to(sender());
                }).build();
    }
}
