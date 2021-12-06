package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.testkit.javadsl.TestKit;
import models.Reddit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.inject.Injector;
import akka.testkit.TestActorRef;
import play.inject.guice.GuiceInjectorBuilder;
import scala.Equals;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditImplementionTest;
import services.RedditService;

import java.util.*;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;

/**
 * Test for RedditActor inspired by  https://github.com/simranx/Semantic_Tweet_Analysis_Soen6441
 * @author: Zeyu Huang
 */

public class RedditActorTest {
    private static ActorSystem system;

    private static Injector testApp;
    private static RedditActor redditActor;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
        final Props props = Props.create(RedditActor.class);
        final TestActorRef<RedditActor> subject = TestActorRef.create(system, props, "testA");
        redditActor = subject.underlyingActor();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void RedditActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);
            Materializer mat = ActorMaterializer.create(system);

            final Props props = Props.create(RedditActor.class);
            final TestActorRef<RedditActor> subject = TestActorRef.create(system, props, "testC");
            final RedditActor userActor = subject.underlyingActor();
            userActor.setMat(mat);

            final Props propsSra = Props.create(RedditResultActor.class);
            final TestActorRef<RedditResultActor> subjectSra = TestActorRef.create(system, propsSra, "testB");
            final RedditResultActor searchResultsActorSync = subjectSra.underlyingActor();
            searchResultsActorSync.setRedditService(redditService);

            Map<String, ActorRef> searchResultsActorsMap = new HashMap<>();
            searchResultsActorsMap.put("concordia", subjectSra);
            userActor.setSearchResultsActors(searchResultsActorsMap);
            userActor.createSink();

            subject.tell(new Messages.WatchSearchResults("concordia"), getRef()); // test registration
            // await the correct response
            expectMsgClass(duration("3 seconds"), Flow.class);

            subject.tell(new Messages.RedditsMessage(new ArrayList<>() {
            }, "concordia"), getRef()); // test registration
            // await the correct response
            assertEquals("RedditMessage(concordia)", "RedditMessage(concordia)");

            subject.tell(new Messages.UnwatchSearchResults("concordia"), getRef());
        }};

    }

    @Test
    public void setSearchResultsMap() {
        redditActor.setSearchResultsMap(null);
        Assert.assertNull(redditActor.getSearchResultsMap());
    }

    @Test
    public void setJsonSink() {
        redditActor.setJsonSink(null);
        Assert.assertNull(redditActor.getJsonSink());
    }

    @Test
    public void setSearchResultsActors() {
        redditActor.setSearchResultsActors(null);
        Assert.assertNull(redditActor.getSearchResultsActors());
    }

    /**
     * Setter test for Materializer
     */
    @Test
    public void testSetMaterializer() {
        redditActor.setMat(null);
        Assert.assertNull(redditActor.getMat());
    }

}