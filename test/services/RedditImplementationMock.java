package services;

import org.junit.After;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

/**
 * Mock implementation of the {@link RedditApi} interface
 *
 * @author Yongshi Liang
 */
public class RedditImplementationMock implements RedditApi {

    private WSClient ws;
    private Server server;
    private RedditImplemention redditImplemention;

    /**
     * Constructor of the mock
     * Set up a local server that returns the sample test results for every request's
     * Get a test instance of the WSClient that existing in Play
     * Inject this instance into the live implementation
     * Override the base URl for every request, so that the local server will respond to the request
     */
    public RedditImplementationMock() {
        server = Server.forRouter((components) ->
                RoutingDsl.fromComponents(components)
                        .GET("/search/submission")
                        .routingTo(request -> ok().sendResource(
                                (request.queryString("q").isEmpty()?"":(request.queryString("q").get().equals("")?"":"searchReddits"))
                                        + (request.queryString("subreddit").isEmpty()?"":(request.queryString("subreddit").get().equals("")?"":"searchReddits"))
                                        + (request.queryString("author").isEmpty()?"":(request.queryString("author").get().equals("")?"":"searchReddits"))
                                        + ".json"))
//                        .routingTo(request -> ok().sendResource("searchReddits.json"))
                        .GET("/testAuthor/about.json")
                        .routingTo(request -> ok().sendResource("userProfile.json"))
                        .GET("/testFalseAuthor/about.json")
                        .routingTo(request -> ok().sendResource("json"))
                        .build());

        ws = play.test.WSTestClient.newClient(server.httpPort());
        redditImplemention = new RedditImplemention(ws);
        redditImplemention.setBaseUrl("");
        redditImplemention.setUserInfoBaseUrl("");
    }

    /**
     * Mock the search with the live implementation
     *
     * @param keyword search term
     * @return CompletionStage of a WSResponse from the local server
     */
    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return redditImplemention.search(keyword);
    }

    /**
     * Mock the searchSubreddit with the live implementation
     *
     * @param keyword search term
     * @return CompletionStage of a WSResponse from the local server
     */
    @Override
    public CompletionStage<WSResponse> searchSubreddit(String keyword) {
        return redditImplemention.searchSubreddit(keyword);
    }

    /**
     * Mock the searchByAuthor with the live implementation
     *
     * @param author name of the author to search
     * @return CompletionStage of a WSResponse from the local server
     */
    @Override
    public CompletionStage<WSResponse> searchByAuthor(String author) {
        return redditImplemention.searchByAuthor(author);
    }

    /**
     * Mock the getAuthorProfile with the live implementation
     *
     * @param author name of the author to search
     * @return CompletionStage of a WSResponse from the local server
     */
    @Override
    public CompletionStage<WSResponse> getAuthorProfile(String author) {
        return redditImplemention.getAuthorProfile(author);
    }

    /**
     * Close the WSClient and stop the local server
     *
     * @throws IOException when WSClient cannot be close
     */
    @After
    public void tearDown() throws IOException {
        try {
            ws.close();
        } finally {
            server.stop();
        }
    }
}
