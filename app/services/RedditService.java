package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.Reddit;
import play.libs.ws.WSResponse;
import javax.inject.Inject;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.CompletionStage;


public class RedditService {

    @Inject
    private RedditImplemention redditImplementation;

    /**
     * Default constructor
     */
    public RedditService() {
        //mapper = new ObjectMapper();
    }


    /**
     * Parse the reddits for a keyword
     * @param keywords keyword
     * @return CompletionStage of a SearchResult
     */
    public CompletionStage<List<Reddit>> getRedditsts(final String keywords) {
        try {

            return redditImplementation.search(keywords)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseReddits);
        }catch (Exception e){
            System.out.println("error!!!");
            return null;
        }

    }

    /**
     * Convert the reddits from a JsonNode to a SearchResult using jackson
     * @param result JsonNode jsonNode extracted from the redditImplementation
     * @return SearchResult search results as a POJO
     */
    public List<Reddit> parseReddits(JsonNode result) {
        try {
            List<Reddit> res = new ArrayList<>();
            result = result.get("data");
            int n = result.size();
            System.out.println(n);
            for(int i = 0;i<n;i++){
                Reddit r = new Reddit(Integer.toString(i), result.get(i).get("author").toString(), result.get(i).get("subreddit").toString(), result.get(i).get("title").toString());
                res.add(r);
            }
            System.out.println(res);
            return res;
        } catch (Exception e) {
            System.out.println("Cannot parse jaon data");
            return null;
        }
    }
}
