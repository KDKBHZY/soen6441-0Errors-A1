package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class SubredditService {

    @Inject
    private RedditImplemention redditImplementation;

    private ObjectMapper mapper;

    /**
     * Default constructor
     */
    public SubredditService() {
        this.mapper = new ObjectMapper();
    }

    public CompletionStage<List<Reddit>> getsubRedditsts(final String keywords) {
        try {
            return redditImplementation.searchSubreddit(keywords)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parsesubReddits);
        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    public List<Reddit> parsesubReddits(JsonNode result) {
        try {
            //System.out.println(Arrays.asList(mapper.treeToValue(result.get("data"), Reddit[].class)));
            return Arrays.asList(mapper.treeToValue(result.get("data"), Reddit[].class));
        } catch (Exception e) {
            System.out.println("Cannot parse json data");
            return null;
        }
    }
}
