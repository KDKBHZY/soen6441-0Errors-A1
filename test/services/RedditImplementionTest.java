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

/**
 * Test the {@link RedditImplemention} class
 *
 * @author Yongshi Liang
 */
public class RedditImplementionTest {

    private static RedditApi redditImplementationMock;

    /**
     * Initialize the test application and bind {@link RedditApi} interface to
     * its mock implementation {@link RedditImplementationMock}
     */
    @BeforeClass
    public static void initTestApp() {
        Injector testApp = new GuiceInjectorBuilder()
                .overrides((bind(RedditApi.class).to(RedditImplementationMock.class)))
                .build();
        redditImplementationMock = testApp.instanceOf(RedditApi.class);
    }

    /**
     * Test for search by comparing the sample result and
     * the result returned by the mock implementation
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void search() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.search("test").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    /**
     * Test for searchSubreddit by comparing the sample result and
     * the result returned by the mock implementation
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void searchSubreddit() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.searchSubreddit("test subreddit").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    /**
     * Test for searchByAuthor by comparing the sample result and
     * the result returned by the mock implementation
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void searchByAuthor() throws Exception {
        String jsonStr = readFileAsString("test/resources/searchReddits.json");

        WSResponse response = redditImplementationMock.searchByAuthor("testAuthor").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    /**
     * Test for getAuthorProfile by comparing the sample result and
     * the result returned by the mock implementation
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void getAuthorProfile() throws Exception {
        String jsonStr = readFileAsString("test/resources/userProfile.json");

        WSResponse response = redditImplementationMock.getAuthorProfile("testAuthor").toCompletableFuture().join();
        String responseStr = response.getBody();

        assertEquals(jsonStr, responseStr);
    }

    /**
     * Converts the input Json file with the given path into string
     *
     * @param file Json file path
     * @return File string
     * @throws Exception when given file in not found
     */
    private static String readFileAsString(String file) throws Exception {
        return Files.readString(Paths.get(file));    }
}