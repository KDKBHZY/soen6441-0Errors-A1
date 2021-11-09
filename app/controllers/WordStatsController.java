package controllers;

import javafx.util.Pair;
import play.libs.Json;
import play.mvc.*;
import models.Reddit;
import services.RedditService;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.stream.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's wordStats page.
 * @author Shuo Gao
 */
public class WordStatsController extends Controller {
    private RedditService redditService;

    /**
     * Constructor function
     * @author Shuo Gao
     * @param  redditService Service for getting reddits.
     */
    @Inject
    public WordStatsController(RedditService redditService) {
        this.redditService = redditService;
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
    private List<Pair<String, Long>> statistics(List<Reddit> reddits) {
        Map<String, Long> words = reddits.stream()
                .map(Reddit::getSubmission)
                .flatMap(w -> {return Stream.of(w.split("[\\W]"));})
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        List<Pair<String, Long>> res = words.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(w -> new Pair<>(w.getKey(), w.getValue()))
                .filter(w -> w.getKey().length()>0)
                .collect(Collectors.toList());
        System.out.println("List of "+ res.size());
        return res;
    }

}
