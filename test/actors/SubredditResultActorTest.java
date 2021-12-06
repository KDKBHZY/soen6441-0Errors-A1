package actors;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;

/**
 * Test for SubredditResultActor inspired by  https://github.com/simranx/Semantic_Tweet_Analysis_Soen6441
 * @author: Zeyu Huang
 */
public class SubredditResultActorTest {

    static ActorSystem system;

    private static Injector testApp;
    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testRedditResultsActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);

            final Props props = Props.create(SubredditResultActor.class);
            final TestActorRef<SubredditResultActor> subject = TestActorRef.create(system, props, "testB");
            final SubredditResultActor searchResultsActorSync = subject.underlyingActor();
            searchResultsActorSync.setRedditService(redditService);

            subject.tell(new Messages.RegisterActor(), getRef()); // test registration
            // await the correct response
            expectMsg(duration("1 seconds"), "SubredditActor registered");

            // the run() method needs to finish within 3 seconds
            within(duration("3 seconds"), () -> {
                subject.tell(new Messages.WatchsubRedditResults("nba"), getRef());

                // response must have been enqueued to us before probe
                expectMsgClass(duration("3 seconds"), Messages.RedditsMessage.class);
                return null;
            });

            // the run() method needs to finish within 3 seconds
            within(duration("3 seconds"), () -> {
                subject.tell(new SubredditResultActor.Filter(), getRef());

                // response must have been enqueued to us before probe
                expectMsgClass(duration("3 seconds"), Messages.UnwatchSearchResults.class);
                return null;
            });
        }};
    }

}