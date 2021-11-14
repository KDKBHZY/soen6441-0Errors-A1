import org.junit.jupiter.api.AfterAll;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.routing.RoutingDsl;
import play.server.Server;
import services.RedditApi;
import services.RedditImplemention;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class RedditTestImplementation  {
    private WSClient ws;

    private Server server;

    private RedditImplemention redditImplemention;

    /**
     * Constructor
     * First, we setup a server that will return our static files for a search or a profile
     * Then, we get a test instance of the WSClient, existing in Play
     * Then, we inject this instance in the real implementation: this way, the mock server
     * will respond instead of Reddit, giving us the static files
     * Finally, we override the base URL to query the local server which responds on /search and /statuses
     * without any domain name in front of it
     */
    public RedditTestImplementation() {
        // Mock the Twitter's API response
//        server = Server.forRouter((components) -> RoutingDsl.fromComponents(components)
//                .GET("/search/tweets.json").routeTo(() ->
//                        ok().sendResource("search.json")
//                )
//                .GET("/statuses/user_timeline.json").routeTo(() ->
//                        ok().sendResource("userProfile.json")
//                )
//                .build()
//        );
//
//        // Get test instance of WSClient
//        ws = play.test.WSTestClient.newClient(server.httpPort());
//
//        // Here we will use the "real" implementation but with the mock server created above
//        // Therefore, we will achieve code coverage but not call the live Twitter API!
//        redditImplemention = new RedditImplemention(ws);
//        redditImplemention.setBaseUrl("");
    }


//    @Override
//    public CompletionStage<WSResponse> search(String keyword) {
//        return redditImplemention.search(keyword);
//    }
//
//    @Override
//    public CompletionStage<WSResponse> searchSubreddit(String keyword) {
//        return null;
//    }
//
//    @Override
//    public CompletionStage<WSResponse> searchByAuthor(String author) {
//        return null;
//    }
//
//    @Override
//    public CompletionStage<WSResponse> getAuthorProfile(String author) {
//        return null;
//    }


//    /**
//     * Close the WSClient, stop the server
//     * @throws IOException exception
//     */
//    @AfterAll
//    public void tearDown() throws IOException {
//        try {
//            ws.close();
//        }
//        finally {
//            server.stop();
//        }
//    }
}
