package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Live implementation of RedditApi
 */
public class RedditImplemention implements RedditApi {

    private WSClient ws;
    private String baseUrl = "https://api.pushshift.io/reddit";
    private static final String path = "/search/submission";
    private String userInfoBaseUrl = "https://www.reddit.com/user";

    /**
     * Constructor of the live implementation
     *
     * @param ws WSClient provided by Guice
     */
    @Inject
    public RedditImplemention(WSClient ws) {
        this.ws = ws;
    }

    /**
     * Send a request to submission API to get 10 latest submission of this keyword
     *
     * @param keyword
     * @author ZeYu Huang
     */
    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return (ws.url(baseUrl + path)
                .addQueryParameter("q", keyword)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc"))
                .get();
    }

    /**
     * Send a request to submission API to get 10 latest submission in this subreddit
     *
     * @param subreddit
     * @author ZeYu Huang
     */
    @Override
    public CompletionStage<WSResponse> searchSubreddit(String subreddit) {
        return ws.url(baseUrl + path)
                .addQueryParameter("subreddit", subreddit)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc")
                .get();
    }

    /**
     * Search for reddit submissions that posted by a given name of the author
     *
     * @param author username of the author
     * @return CompletionStage of WSResponse from the live server
     * @author YongshiLiang
     */
    @Override
    public CompletionStage<WSResponse> searchByAuthor(String author) {
        return ws.url(baseUrl + path)
                .addQueryParameter("author", author)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "desc")
                .get();
    }

    /**
     * Search for the user profile by a given name of the author
     *
     * @param author username of the author
     * @return CompletionStage of WSResponse from the live server
     * @author YongshiLiang
     */
    @Override
    public CompletionStage<WSResponse> getAuthorProfile(String author) {
        String userInfoUrl = userInfoBaseUrl + "/" + author + "/about.json";
        return ws.url(userInfoUrl).get();
    }

    /**
     * Setter for the baseUrl,
     * so that the baseUrl can be override while testing
     *
     * @param baseUrl given API
     * @author Yongshi Liang
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Setter for the userInfoBaseUrl,
     * so that the userInfoBaseUrl can be override while testing
     *
     * @param userInfoBaseUrl
     * @author Yongshi Liang
     */
    public void setUserInfoBaseUrl(String userInfoBaseUrl) {
        this.userInfoBaseUrl = userInfoBaseUrl;
    }
}
