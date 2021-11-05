package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import play.libs.ws.WSResponse;


import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
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
     * Parse the reddits for a keyword
     * @param keywords keyword
     * @return CompletionStage of a SearchResult
     */
    public CompletionStage<Reddit> getRedditsts(final String keywords) {
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
    public Reddit parseReddits(JsonNode result) {
        try {
           Iterator<JsonNode> elements = result.elements();
                   result = result.get("data");
                   int n = result.size();
            System.out.println(n);
            for(int i = 0;i<n;i++){
                System.out.println(result.get(i).get("author"));

            }
            return mapper.treeToValue(result,
                    Reddit.class);
        } catch (JsonProcessingException e) {
            System.out.println("Cannot parse jaon data");
            return null;
        }
    }
}
