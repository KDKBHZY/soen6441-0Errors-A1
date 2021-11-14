package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
import play.libs.ws.WSResponse;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Parses the response data from the live implementation {@link RedditImplemention}
 * and return POJO instances of {@link Reddit} and {@link User}
 */
public class RedditService {

    @Inject
    private RedditApi redditImplementation;

    /**
     * Parse the reddits for a keyword
     *
     * @param keywords keyword
     * @author ZeYu Huang
     */
    public CompletionStage<List<Reddit>> getReddits(final String keywords) {
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
     * Parse the reddits for a subreddit
     *
     * @param subreddit
     * @author ZeYu Huang
     */
    public CompletionStage<List<Reddit>> getSubreddits(final String subreddit) {
        try {
            return redditImplementation.searchSubreddit(subreddit)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseReddits);
        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    /**
     * Parses the Reddit submissions posted by the given author
     *
     * @param author username of the author
     * @return CompletionStage of a List of {@link Reddit}
     * @author Yongshi Liang
     */
    public CompletionStage<List<Reddit>> getSubredditsByAuthor(final String author) {
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
     * Convert the submissions from a JsonNode to a List of {@link Reddit} using jackson
     *
     * @param result JsonNode extracted from the redditImplementation respond
     * @return List of {@link Reddit}
     */
    public List<Reddit> parseReddits(JsonNode result) {
        try {
            return Arrays.asList(new ObjectMapper().treeToValue(result.get("data"), Reddit[].class));
        } catch (Exception e) {
            System.out.println("Cannot parse json data");
            return null;
        }
    }

    /**
     * Parses the user profile of the given author
     *
     * @param author username of the author
     * @return CompletionStage of an instance of {@link User}
     * @author Yongshi Liang
     */
    public CompletionStage<User> getAuthorProfile(final String author) {
        try {
            CompletionStage<User> user = redditImplementation.getAuthorProfile(author)
                    .thenApplyAsync(WSResponse::asJson)
                    .thenApplyAsync(this::parseUser);
            return user.thenCombineAsync(getSubredditsByAuthor(author), (this::addPostedReddit));

        } catch (Exception e) {
            System.out.println("error!!!");
            return null;
        }
    }

    /**
     * Adds a list of {@link Reddit} to a given {@link User}
     *
     * @param user an existing {@link User}
     * @param reddits a lists of {@link Reddit}
     * @return an instance of {@link User} contains the Reddit submission posted by itself
     */
    public User addPostedReddit(User user, List<Reddit> reddits) {
        reddits.forEach(r -> user.getPostedReddits().add(r));
        return user;
    }

    /**
     * Convert the user profile from a JsonNode to a {@link User} using jackson
     *
     * @param result JsonNode extracted from the redditImplementation respond
     * @return an instance of {@link User}
     */
    public User parseUser(JsonNode result) {
        try {
            return new ObjectMapper().treeToValue(result.get("data"), User.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot parse json data");
            return null;
        }
    }
}
