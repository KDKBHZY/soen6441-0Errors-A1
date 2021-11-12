package controllers;

import models.Reddit;
import play.libs.Json;
import play.mvc.*;
import services.RedditService;
import views.html.*;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
    private RedditService redditService;
    @Inject
    public RedditLyticsController(RedditService redditService) {
        this.redditService = redditService;
    }
    public CompletionStage<Result> index() {
        return CompletableFuture.completedFuture(ok(views.html.index.render()));
    }


    /**
     * display search page
     * @author ZeYu Huang
     */
    public CompletionStage<Result> rlyticsIndex() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    /**
     * use term to search 10 latest submission
     * @param term keyword
     * @author ZeYu Huang
     */
    public CompletionStage<Result> search(String term) {
             return redditService.getRedditsts(term)
                     .thenApplyAsync(res -> ok(Json.toJson(res)));
    }


    public CompletionStage<Result> getAuthorProfile(String author) {
        return redditService.getAuthorProfile(author)
                .thenApplyAsync(res -> ok(views.html.profile.render(res)));
    }

    /**
     * use subreddit from main page to search 10 latest submission in this subreddit
     * @author ZeYu Huang
     * @param subreddit keyword
     */

    public  CompletionStage<Result> searchSubreddit(String subreddit) {
        return redditService.getsubRedditsts(subreddit)
                .thenApplyAsync(res -> ok(views.html.result.render(res, subreddit)));
    }

    /**
     * Display statistics result
     * @author Shuo Gao
     * @param  term Key word for the searching.
     */
    public CompletionStage<Result> statistics(String term) {
        return redditService.getRedditsts(term)
                .thenApplyAsync(res -> ok(views.html.wordstats.render(statistics(res), term)));
    }

    /**
     * Perform the statistics of words
     * @author Shuo Gao
     * @param  reddits Search results to be handled
     * @return A list of Pairs contains words and frequencies
     */
    private List<Map.Entry<String, Long>> statistics(List<Reddit> reddits) {
        Map<String, Long> words = reddits.stream()
                .map(Reddit::getTitle)
                .flatMap(w -> {return Stream.of(w.split("[\\W]"));})
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        List<Map.Entry<String, Long>> res = words.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(w -> w.getKey().length()>0)
                .collect(Collectors.toList());
        System.out.println("List of "+ res.size());
        return res;
    }


}
