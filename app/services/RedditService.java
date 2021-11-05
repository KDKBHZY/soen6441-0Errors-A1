package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.ws.WSResponse;


import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.naming.directory.SearchResult;
import java.util.concurrent.CompletionStage;

public class RedditService {

    @Inject
    private RedditImplemention redditImplementation;

    private ObjectMapper mapper;

    /**
     * Default constructor
     */
    public RedditService() {
        mapper = new ObjectMapper();
    }

    /**
     * Parse the tweets for a keyword
     * @param keywords keyword
     * @return CompletionStage of a SearchResult
     */
    public CompletionStage<SearchResult> getTweets(final String keywords) {
        return redditImplementation.search(keywords)
                .thenApplyAsync(WSResponse::asJson)
                .thenApplyAsync(this::parseTweets);
    }

    /**
     * Convert the tweets from a JsonNode to a SearchResult using jackson
     * @param result JsonNode jsonNode extracted from the twitterImplementation
     * @return SearchResult search results as a POJO
     */
    public SearchResult parseTweets(JsonNode result) {
        try {
            return mapper.treeToValue(result,
                    SearchResult.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
