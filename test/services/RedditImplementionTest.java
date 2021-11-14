package services;

import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.libs.ws.WSResponse;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;

public class RedditImplementionTest {

    private static RedditApi redditImplementationMock;

    @BeforeClass
    public static void initTestApp() {
        Injector testApp = new GuiceInjectorBuilder()
                .overrides((bind(RedditApi.class).to(RedditImplementationMock.class)))
                .build();
        redditImplementationMock = testApp.instanceOf(RedditApi.class);
    }

    @Test
    public void search() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.search("test").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    @Test
    public void searchSubreddit() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.searchSubreddit("test subreddit").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    @Test
    public void searchByAuthor() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.searchByAuthor("testAuthor").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    @Test
    public void getAuthorProfile() throws Exception {
        String jsonStr = readFileAsString("test/resources/userProfile.json");

        WSResponse response = redditImplementationMock.getAuthorProfile("testAuthor").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    private static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}