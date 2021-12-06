package actors;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;

public class AuthorProfileResultActorTest {

    private static ActorSystem system;
    private static Injector testApp;

    @BeforeClass
    public static void setUp() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testAuthorProfileResultActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);

            final Props props = Props.create(AuthorProfileResultActor.class);
            final TestActorRef<AuthorProfileResultActor> subject = TestActorRef.create(system, props, "testB");
            final AuthorProfileResultActor authorProfileResultActorSync = subject.underlyingActor();
            authorProfileResultActorSync.setRedditService(redditService);

            subject.tell(new Messages.RegisterActor(), getRef());
            expectMsg(duration("1 seconds"), "AuthorProfileActor registered");

            within(duration("3 seconds"), () -> {
                subject.tell(new Messages.WatchAuthorProfileResults("testUser"), getRef());
                return null;
            });

            within(duration("3 seconds"), () -> {
                subject.tell(new AuthorProfileResultActor.Filter(), getRef());
                return null;
            });
        }};
    }
}