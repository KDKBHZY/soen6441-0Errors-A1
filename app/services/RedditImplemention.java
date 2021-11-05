package services;

import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class RedditImplemention implements RedditApi {

    private String baseUrl = "https://api.pushshift.io/reddit/search/submission";


    private final WSClient ws;

    /**
     * Constructor
     * @param ws WSClient provided by Guice
     */
    @Inject
    public RedditImplemention(WSClient ws) {
        this.ws = ws;
    }



    @Override
    public CompletionStage<WSResponse> search(String keyword) {
        return ws.url(baseUrl)
                //.addHeader("Authorization", bearer)
                .addQueryParameter("q", keyword)
                .addQueryParameter("size", "10")
                .addQueryParameter("sort", "asc")
                .get(); // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
          }
}
