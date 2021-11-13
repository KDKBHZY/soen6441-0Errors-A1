package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class RedditImplemention implements RedditApi {
    private final WSClient ws;
    private String baseUrl = "https://api.pushshift.io/reddit/search/submission";
    private final String userInfoBaseUrl ="https://www.reddit.com/user/";

    /**
     * Constructor
     *
     * @param ws WSClient provided by Guice
     */
    @Inject
    public RedditImplemention(WSClient ws) {
        this.ws = ws;
    }

    /**
     * send a request to submission API to get 10 latest submission of this keyword
     * @author ZeYu Huang
     * @param keyword keyword
     */

    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return (ws.url(baseUrl)
                .addQueryParameter("q", keyword)
                .addQueryParameter("size", "250")
                .addQueryParameter("sort", "desc"))
                .get();
    }

    /**
     * send a request to submission API to get 10 latest submission in this subreddit
     * @author ZeYu Huang
     * @param subreddit subreddit
     */

    @Override
    public CompletionStage<WSResponse> searchSubreddit(String subreddit) {
        return ws.url(baseUrl)
                .addQueryParameter("subreddit", subreddit)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc")
                .get();
    }

    @Override
    public CompletionStage<WSResponse> searchByAuthor(String author) {
        return ws.url(baseUrl)
                .addQueryParameter("author", author)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc")
                .get();
    }

    @Override
    public CompletionStage<WSResponse> getAuthorProfile(String author) {
        String userInfoUrl = userInfoBaseUrl + author + "/about.json";
        return ws.url(userInfoUrl).get();
    }

    public void setBaseUrl(String baseurl) {
        this.baseUrl = baseUrl;
    }
}
