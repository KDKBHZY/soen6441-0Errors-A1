package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class RedditImplemention implements RedditApi {
    private final WSClient ws;
    private String baseUrl = "https://api.pushshift.io/reddit/search/submission";

    /**
     * Constructor
     *
     * @param ws WSClient provided by Guice
     */
    @Inject
    public RedditImplemention(WSClient ws) {
        this.ws = ws;
    }

    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return ws.url(baseUrl)
                .addQueryParameter("q", keyword)
                .addQueryParameter("size", "250")
                .addQueryParameter("sort", "desc")
                .get();
    }

    @Override
    public CompletionStage<WSResponse> searchSubreddit(String keyword) {
        return ws.url(baseUrl)
                .addQueryParameter("subreddit", keyword)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc")
                .get();
    }
}
