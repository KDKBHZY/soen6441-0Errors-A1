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
public class WordstatsActorTest {

    private static ActorSystem system;

    private static Injector testApp;
    private static WordstatsActor wordstatsActor;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create();
        final Props props = Props.create(SubredditActor.class);
        final TestActorRef<WordstatsActor> subject = TestActorRef.create(system, props, "testA");
        wordstatsActor = subject.underlyingActor();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testWordstatsActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);
            Materializer mat = ActorMaterializer.create(system);

            final Props props = Props.create(WordstatsActor.class);
            final TestActorRef<WordstatsActor> subject = TestActorRef.create(system, props, "testC");
            final WordstatsActor wordstatsActor = subject.underlyingActor();
            wordstatsActor.setMat(mat);

            final Props propsSra = Props.create(WordstatsActor.class);
            final TestActorRef<WordstatsActor> subjectSra = TestActorRef.create(system, propsSra, "testB");
            final WordstatsActor wordstatsActorSync = subjectSra.underlyingActor();
            wordstatsActorSync.setRedditService(redditService);

            Map<String, ActorRef> searchResultsActorsMap = new HashMap<>();
            searchResultsActorsMap.put("nba", subjectSra);
            wordstatsActor.setSearchResultsActors(searchResultsActorsMap);
            wordstatsActor.createSink();

            subject.tell(new Messages.WatchwordstatsResults("nba"), getRef()); // test registration
            // await the correct response
            expectMsgClass(duration("3 seconds"), Flow.class);

            subject.tell(new Messages.WordstatsMessage(new ArrayList<>() {
            }, "nba"), getRef()); // test registration
            // await the correct response
            assertEquals("RedditMessage(nba)", "RedditMessage(nba)");

            subject.tell(new Messages.UnwatchWordstatsResults("concordia"), getRef());
        }};

    }

    @Test
    public void setSearchResultsMap() {
        wordstatsActor.setSearchResultsMap(null);
        Assert.assertNull(wordstatsActor.getSearchResultsMap());
    }

    @Test
    public void setJsonSink() {
        wordstatsActor.setJsonSink(null);
        Assert.assertNull(wordstatsActor.getJsonSink());
    }

    @Test
    public void setSearchResultsActors() {
        wordstatsActor.setSearchResultsActors(null);
        Assert.assertNull(wordstatsActor.getSearchResultsActors());
    }

    /**
     * Setter test for Materializer
     */
    @Test
    public void testSetMaterializer() {
        wordstatsActor.setMat(null);
        Assert.assertNull(wordstatsActor.getMat());
    }

}