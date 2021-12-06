package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import models.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.Assert;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import java.util.HashMap;
import java.util.Map;

import static play.inject.Bindings.bind;

public class AuthorProfileActorTest {

    private static ActorSystem system;
    private static Injector testApp;
    private static AuthorProfileActor authorProfileActor;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
        final Props props = Props.create(AuthorProfileActor.class);
        final TestActorRef<AuthorProfileActor> subject = TestActorRef.create(system, props, "testA");
        authorProfileActor = subject.underlyingActor();
    }

    @AfterClass
    public static void tearDown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void AuthorProfileActor() {
        new TestKit(system) {{
            testApp = new GuiceInjectorBuilder()
                    .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                    .build();
            RedditService redditService = testApp.instanceOf(RedditService.class);
            Materializer mat = ActorMaterializer.create(system);

            final Props props = Props.create(AuthorProfileActor.class);
            final TestActorRef<AuthorProfileActor> subject = TestActorRef.create(system, props, "testC");
            final AuthorProfileActor authorProfileActor = subject.underlyingActor();
            authorProfileActor.setMat(mat);

            final Props propsSra = Props.create(AuthorProfileResultActor.class);
            final TestActorRef<AuthorProfileResultActor> subjectSra = TestActorRef.create(system, propsSra, "testB");
            final AuthorProfileResultActor authorProfileActorSync = subjectSra.underlyingActor();
            authorProfileActorSync.setRedditService(redditService);

            Map<String, ActorRef> authorProfileActorsMap = new HashMap<>();
            authorProfileActorsMap.put("testUser", subjectSra);
            authorProfileActor.setAuthorProfileActors(authorProfileActorsMap);
            authorProfileActor.createSink();

            subject.tell(new Messages.WatchAuthorProfileResults("testUser"), getRef());
            expectMsgClass(duration("3 seconds"), Flow.class);

            User testAuthor = new User();
            testAuthor.setName("testUser");
            Messages.AuthorProfileMessage message = new Messages.AuthorProfileMessage(testAuthor);
            subject.tell(message, getRef());

            subject.tell(new Messages.UnwatchAuthorProfile("testUser"), getRef());
        }};
    }

    @Test
    public void getAuthorProfileMap() {
        authorProfileActor.setAuthorProfileMap(null);
        Assert.assertNull(authorProfileActor.getAuthorProfileMap());
    }

    @Test
    public void setAuthorProfileMap() {
        authorProfileActor.setAuthorProfileMap(null);
        Assert.assertNull(authorProfileActor.getAuthorProfileMap());
    }

    @Test
    public void getAuthorProfileActors() {
        authorProfileActor.setAuthorProfileActors(null);
        Assert.assertNull(authorProfileActor.getAuthorProfileActors());
    }

    @Test
    public void setAuthorProfileActors() {
        authorProfileActor.setAuthorProfileActors(null);
        Assert.assertNull(authorProfileActor.getAuthorProfileActors());
    }

    @Test
    public void getMat() {
        authorProfileActor.setMat(null);
        Assert.assertNull(authorProfileActor.getMat());
    }

    @Test
    public void setMat() {
        authorProfileActor.setMat(null);
        Assert.assertNull(authorProfileActor.getMat());
    }

    @Test
    public void getHubSink() {
        authorProfileActor.setHubSink(null);
        Assert.assertNull(authorProfileActor.getHubSink());
    }

    @Test
    public void setHubSink() {
        authorProfileActor.setHubSink(null);
        Assert.assertNull(authorProfileActor.getHubSink());
    }

    @Test
    public void getJsonSink() {
        authorProfileActor.setJsonSink(null);
        Assert.assertNull(authorProfileActor.getJsonSink());
    }

    @Test
    public void setJsonSink() {
        authorProfileActor.setJsonSink(null);
        Assert.assertNull(authorProfileActor.getJsonSink());
    }
}
