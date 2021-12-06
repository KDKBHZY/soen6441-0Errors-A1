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
public class WordstatsResultActorTest {

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
    public void testWordstatsResultActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);

            final Props props = Props.create(SubredditResultActor.class);
            final TestActorRef<WordstatsResultActor> subject = TestActorRef.create(system, props, "testB");
            final WordstatsResultActor wordstatsResultActorSync = subject.underlyingActor();
            wordstatsResultActorSync.setRedditService(redditService);

            subject.tell(new Messages.RegisterActor(), getRef()); // test registration
            // await the correct response
            expectMsg(duration("1 seconds"), "WordstatsActor registered");

            // the run() method needs to finish within 3 seconds
            within(duration("3 seconds"), () -> {
                subject.tell(new Messages.WatchsubRedditResults("nba"), getRef());

                // response must have been enqueued to us before probe
                expectMsgClass(duration("3 seconds"), Messages.WordstatsMessag.class);
                return null;
            });

            // the run() method needs to finish within 3 seconds
            within(duration("3 seconds"), () -> {
                subject.tell(new WordstatsResultActor.Filter(), getRef());

                // response must have been enqueued to us before probe
                expectMsgClass(duration("3 seconds"), Messages.UnwatchWordstatsResults.class);
                return null;
            });
        }};
    }

}