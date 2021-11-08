package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
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

    public CompletionStage<List<Reddit>> getSubRedditsByAuthor(final String author) {
        try {
            return redditImplementation.searchByAuthor(author)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseReddits);
        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    /**
     * Convert the reddits from a JsonNode to a List of Reddit using jackson
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

    public CompletionStage<User> getAuthorProfile(final String author) {
        try {
            return redditImplementation.getAuthorProfile(author)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseUser);
        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    public User parseUser(JsonNode result) {
        try {
            return mapper.treeToValue(result.get("data"), User.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot parse json data");
            return null;
        }
    }
}
