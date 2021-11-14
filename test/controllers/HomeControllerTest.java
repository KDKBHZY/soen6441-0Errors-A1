package controllers;

import org.junit.jupiter.api.Test;

import play.libs.ws.WSClient;
import play.mvc.Result;
import services.RedditImplemention;
import services.RedditService;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;

/**
 * HomeController Tester.
 *
 * @author Zeyu Huang
 * @version 1.0
 * @since <pre>11月 8, 2021</pre>
 */
public class HomeControllerTest {
    private RedditImplemention redditImplemention;
    /**
     * Method: index()
     */

    @Test
    public void testIndex() throws Exception {
        RedditService redditService = new RedditService();

        Result result = new RedditLyticsController(redditService).index().toCompletableFuture().get();
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Welcome to the project of LabK-Group3."));

    }

} 
