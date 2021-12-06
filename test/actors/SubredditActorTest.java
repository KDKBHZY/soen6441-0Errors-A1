package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;

/**
 * Test for SubredditActor inspired by  https://github.com/simranx/Semantic_Tweet_Analysis_Soen6441
 * @author: Zeyu Huang
 */
public class SubredditActorTest {

    private static ActorSystem system;

    private static Injector testApp;
    private static SubredditActor subredditActor;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
        final Props props = Props.create(SubredditActor.class);
        final TestActorRef<SubredditActor> subject = TestActorRef.create(system, props, "testA");
        subredditActor = subject.underlyingActor();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testSubRedditActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);
            Materializer mat = ActorMaterializer.create(system);

            final Props props = Props.create(SubredditActor.class);
            final TestActorRef<SubredditActor> subject = TestActorRef.create(system, props, "testC");
            final SubredditActor subredditActor = subject.underlyingActor();
            subredditActor.setMat(mat);

            final Props propsSra = Props.create(SubredditResultActor.class);
            final TestActorRef<SubredditResultActor> subjectSra = TestActorRef.create(system, propsSra, "testB");
            final SubredditResultActor subredditResultsActorSync = subjectSra.underlyingActor();
            subredditResultsActorSync.setRedditService(redditService);

            Map<String, ActorRef> searchResultsActorsMap = new HashMap<>();
            searchResultsActorsMap.put("nba", subjectSra);
            subredditActor.setSearchResultsActors(searchResultsActorsMap);
            subredditActor.createSink();

            subject.tell(new Messages.WatchsubRedditResults("nba"), getRef()); // test registration
            // await the correct response
            expectMsgClass(duration("3 seconds"), Flow.class);

            subject.tell(new Messages.RedditsMessage(new ArrayList<>() {
            }, "nba"), getRef()); // test registration
            // await the correct response
            assertEquals("RedditMessage(nba)", "RedditMessage(nba)");

            subject.tell(new Messages.UnwatchSearchResults("concordia"), getRef());
        }};

    }

    @Test
    public void setSearchResultsMap() {
        subredditActor.setSearchResultsMap(null);
        Assert.assertNull(subredditActor.getSearchResultsMap());
    }

    @Test
    public void setJsonSink() {
        subredditActor.setJsonSink(null);
        Assert.assertNull(subredditActor.getJsonSink());
    }

    @Test
    public void setSearchResultsActors() {
        subredditActor.setSearchResultsActors(null);
        Assert.assertNull(subredditActor.getSearchResultsActors());
    }

    /**
     * Setter test for Materializer
     */
    @Test
    public void testSetMaterializer() {
        subredditActor.setMat(null);
        Assert.assertNull(subredditActor.getMat());
    }

}