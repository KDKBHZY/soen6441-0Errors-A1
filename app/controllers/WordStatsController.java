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
 * to the application's userProfile page.
 */
public class WordStatsController extends Controller {
    private RedditService redditService;

    @Inject
    public WordStatsController(RedditService redditService) {
        this.redditService = redditService;
    }

    public CompletionStage<Result> statistics(String term) {
        return redditService.getRedditsts(term)
                .thenApplyAsync(res -> ok(views.html.wordstats.render(statistics(res), term)));
    }

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
