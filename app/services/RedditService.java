package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import play.libs.ws.WSResponse;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;


public class RedditService {

    @Inject
    private RedditImplemention redditImplementation;

    private ObjectMapper mapper;

    /**
     * Default constructor
     */
    public RedditService() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Parse the reddits for a keyword
     *
     * @param keywords keyword
     * @return CompletionStage of a SearchResult
     */
    public CompletionStage<List<Reddit>> getRedditsts(final String keywords) {
        try {
            return redditImplementation.search(keywords)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseReddits);
        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    /**
     * Convert the reddits from a JsonNode to a SearchResult using jackson
     *
     * @param result JsonNode jsonNode extracted from the redditImplementation
     * @return SearchResult search results as a List of Reddit
     */
    public List<Reddit> parseReddits(JsonNode result) {
        try {
            return Arrays.asList(mapper.treeToValue(result.get("data"), Reddit[].class));
        } catch (Exception e) {
            System.out.println("Cannot parse json data");
            return null;
        }
    }

}
