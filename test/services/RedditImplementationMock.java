package services;

import org.junit.After;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.routing.RoutingDsl;
import play.server.Server;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class RedditImplementationMock implements RedditApi {

    private WSClient ws;
    private Server server;
    private RedditImplemention redditImplemention;

    public RedditImplementationMock() {
        server = Server.forRouter((components) ->
                RoutingDsl.fromComponents(components)
                        .GET("/search/submission")
                        .routingTo(request -> ok().sendResource("resources/searchReddits.json"))
                        .GET("/testAuthor/about.json")
                        .routingTo(request -> ok().sendResource("resources/userProfile.json"))
                        .build());

        ws = play.test.WSTestClient.newClient(server.httpPort());
        redditImplemention = new RedditImplemention(ws);
        redditImplemention.setBaseUrl("");
        redditImplemention.setUserInfoBaseUrl("");
    }

    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return redditImplemention.search(keyword);
    }

    @Override
    public CompletionStage<WSResponse> searchSubreddit(String keyword) {
        return redditImplemention.searchSubreddit(keyword);
    }

    @Override
    public CompletionStage<WSResponse> searchByAuthor(String author) {
        return redditImplemention.searchByAuthor(author);
    }

    @Override
    public CompletionStage<WSResponse> getAuthorProfile(String author) {
        return redditImplemention.getAuthorProfile(author);
    }

    @After
    public void tearDown() throws IOException {
        try {
            ws.close();
        }
        finally {
            server.stop();
        }
    }
}
