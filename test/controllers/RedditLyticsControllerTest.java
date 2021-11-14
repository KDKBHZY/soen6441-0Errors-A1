package controllers;

import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.mvc.Result;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;

public class RedditLyticsControllerTest {
    private static RedditService redditService;
    private static RedditLyticsController redditLyticsController;
    @BeforeClass
    public static void initTestApp() {
        Injector testApp = new GuiceInjectorBuilder()
                .overrides((bind(RedditApi.class).to(RedditImplementationMock.class)))
                .build();
        redditService = testApp.instanceOf(RedditService.class);
        redditLyticsController = new RedditLyticsController(redditService);
    }

    @Test
    public void index() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.index()
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Welcome to 0Errors!"));
    }

    @Test
    public void rlyticsIndex() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.rlyticsIndex()
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Welcome to RedditLytics"));
    }

    @Test
    public void search() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.search("test")
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("<ol>\n" +
                "        <li>Author: testAuthor, test subreddit, \"[ISO][US] Malezia urea moisturizer\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Hire Me: Gen Chem, Org Chem, Biochemistry, Biology Tutor with 100+ (Vouches Included) Disc : dheeraj#1893\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"test 2\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"MRW someone in an egg donation group asks if they can buy “boy eggs”. No, she doesn’t want embryos. Just eggs. Eggs that will make boys. Can she test the eggs to make sure they have just a Y chromosome and no X? Her last 2 cycles “only gave me girl embryos, so now I want to start with boy eggs”.\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Nowhere else really to post, but I'm pretty damn stoked after my 2nd to last trial yesterday.\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Do you need a covid test for domestic flights in Egypt?\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Test\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"UK DAS test for A2\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Combine sklearn RepeatedStratifiedKFold with PredefinedSplit?\"</li>\n" +
                "        <li>Author: testAuthor, test subreddit, \"Tp roll test\"</li>\n" +
                "    </ol>"));

    }

    @Test
    public void getAuthorProfile() {
    }

    @Test
    public void searchSubreddit() {
    }

    @Test
    public void statistics() {
    }
}