package controllers;

import akka.actor.ActorRef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.name.Named;
import models.Reddit;
import models.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceInjectorBuilder;
import static play.test.Helpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import play.mvc.Result;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.test.TestServer;
import services.RedditApi;
import services.RedditImplementationMock;
import services.RedditService;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.netty.ws.NettyWebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.test.TestServer;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
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

        redditLyticsController = new RedditLyticsController(null, null,null,redditService);
    }

    /**
     * Test for the reject WebSocket
     * inspired by https://github.com/playframework/play-java-websocket-example/blob/2.6.x/test/controllers/FunctionalTest.java
     */
    @Test
    public void testRejectWebSocket() {
        TestServer server = testServer(37117);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:37117/ws";
                    String serverURL1 = "ws://localhost:37117//subredditws";
                    String serverURL2 = "ws://localhost:37117/authorprofilews";

                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    WebSocketClient.LoggingListener listener1 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage1 = webSocketClient.call(serverURL1, listener1);
                    WebSocketClient.LoggingListener listener2 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage2 = webSocketClient.call(serverURL2, listener2);
                    await().until(completionStage::isDone);
                    await().until(completionStage1::isDone);
                    await().until(completionStage2::isDone);

                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    /**
     * Test for the accept WebSocket
     * inspired by https://github.com/playframework/play-java-websocket-example/blob/2.6.x/test/controllers/FunctionalTest.java
     */
    @Test
    public void testAcceptWebSocket() {
        TestServer server = testServer(19001);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:19001/ws";
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    String serverURL1 = "ws://localhost:19001/subredditws";
                    WebSocketClient.LoggingListener listener1 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage1 = webSocketClient.call(serverURL1, listener1);
                    String serverURL2 = "ws://localhost:19001/authorprofilews";
                    WebSocketClient.LoggingListener listener2 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage2 = webSocketClient.call(serverURL2, listener2);
                    await().until(completionStage::isDone);
                    assertThat(completionStage)
                            .hasNotFailed();
                    await().until(completionStage1::isDone);
                    assertThat(completionStage1)
                            .hasNotFailed();
                    await().until(completionStage2::isDone);
                    assertThat(completionStage2)
                            .hasNotFailed();
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    /**
     * Test for the accept WebSocket
     * inspired by https://github.com/playframework/play-java-websocket-example/blob/2.6.x/test/controllers/FunctionalTest.java
     */
    @Test
    public void testAcceptWebSocketOtherPort() {
        TestServer server = testServer(9000);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:9000/ws";
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    await().until(completionStage::isDone);
                    assertThat(completionStage)
                            .hasNotFailed();

                    String serverURL1 = "ws://localhost:9000/subredditws";
                    WebSocketClient.LoggingListener listener1 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage1 = webSocketClient.call(serverURL1, listener1);
                    await().until(completionStage1::isDone);
                    assertThat(completionStage1)
                            .hasNotFailed();

                    String serverURL2 = "ws://localhost:9000/authorprofilews";
                    WebSocketClient.LoggingListener listener2 = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<NettyWebSocket> completionStage2 = webSocketClient.call(serverURL2, listener2);
                    await().until(completionStage2::isDone);
                    assertThat(completionStage2)
                            .hasNotFailed();
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
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