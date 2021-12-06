package actors;

import akka.Done;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.inject.Bindings.bind;

public class StreamTest {

    private static ActorSystem system;
    private static Injector testApp;
    private static RedditActor redditActor;
    private static Materializer mat;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testSink() throws ExecutionException, InterruptedException {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);
            mat = ActorMaterializer.create(system);

            final Props props = Props.create(RedditActor.class);
            final TestActorRef<RedditActor> subject = TestActorRef.create(system, props, "testA");
            redditActor = subject.underlyingActor();
            redditActor.setMat(mat);

            final Props propSra = Props.create(RedditResultActor.class);
            final TestActorRef<RedditResultActor> subjectSra = TestActorRef.create(system, propSra, "testB");
            final RedditResultActor redditResultActor = subjectSra.underlyingActor();
            redditResultActor.setRedditService(redditService);

            Map<String, ActorRef> redditResultActorsMap = new HashMap<>();
            redditResultActorsMap.put("concordia", subjectSra);
            redditActor.setSearchResultsActors(redditResultActorsMap);
            redditActor.createSink();

            final ObjectMapper mapper = new ObjectMapper();
            final ObjectNode root = mapper.createObjectNode();
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("query", "concordia");
            root.set("obj1", childNode);

            final CompletionStage<Done> future = Source.from(root).runWith(redditActor.getJsonSink(), mat);
            future.toCompletableFuture().get();
            Assert.assertEquals("concordia", redditActor.getSearchResultsActors().entrySet().iterator().next().getKey());
        }};
    }
}


