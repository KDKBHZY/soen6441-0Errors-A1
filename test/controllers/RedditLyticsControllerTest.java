package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.libs.Json;
import play.mvc.Result;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;

/**
 * Test the {@link RedditLyticsController} class
 *
 * @author Zeyu Huang
 */
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
                .join();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Welcome to RedditLytics"));
    }

//    @Test
//    public void search() throws ExecutionException, InterruptedException, JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        Result result = redditLyticsController.search("test")
//                .toCompletableFuture()
//                .join();
//          assertEquals(OK, result.status());
//          assertEquals("application/json", result.contentType().get());
//        JsonNode result1 = mapper.readTree(contentAsString(result));
//        assertEquals("\"testAuthor\"",result1.get(0).get("author").toString());
//        assertEquals("\"test subreddit\"",result1.get(0).get("subReddit").toString());
//        assertEquals("\"[ISO][US] Malezia urea moisturizer\"",result1.get(0).get("title").toString());
//    }

    @Test
    public void getAuthorProfile() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.getAuthorProfile("testAuthor")
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertTrue(contentAsString(result).contains("Author: testAuthor"));

    }

    @Test
    public void searchSubreddit() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.searchSubreddit("test subreddit")
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertTrue(contentAsString(result).contains("SubReddit: test subreddit"));
    }

    @Test
    public void statistics() throws ExecutionException, InterruptedException {
        Result result = redditLyticsController.statistics("test")
                .toCompletableFuture()
                .get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertTrue(contentAsString(result).contains("Word stats of latest submissions with term &quottest&quot"));

       // System.out.println(contentAsString(result));
    }
}