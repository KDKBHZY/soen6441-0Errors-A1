package controllers;

import play.libs.Json;
import play.mvc.*;
import services.RedditService;
import views.html.*;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 * @author: ZeYu Huang
 */
public class RedditLyticsController extends Controller {
    private RedditService redditService;
    private List<String> searchtermHistory;
    @Inject
    public RedditLyticsController(RedditService redditService) {
        this.redditService = redditService;
        this.searchtermHistory = new ArrayList<>();
    }

    /**
     * @des: display search page
     * @author: ZeYu Huang
     */
    public CompletionStage<Result> rlyticsIndex() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    /**
     * @des: use term to search 10 latest submission
     * @param term
     * @author: ZeYu Huang
     */
    public CompletionStage<Result> search(String term) {
         if (searchtermHistory.contains(term)){
             return CompletableFuture.completedFuture(ok(term));
         }
         else {
             searchtermHistory.add(term);
             return redditService.getRedditsts(term)
                     .thenApplyAsync(res -> ok(Json.toJson(res)));
         }

    }


}
