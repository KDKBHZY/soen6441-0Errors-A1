package controllers;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import services.RedditService;
import services.SubredditService;
import views.html.*;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
    private RedditService redditService;
    private SubredditService subredditService;

    @Inject
    public RedditLyticsController(RedditService redditService, SubredditService subredditService) {
        this.redditService = redditService;
        this.subredditService = subredditService;
    }

    public CompletionStage<Result> rlyticsIndex() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    public CompletionStage<Result> search(String term) {
        return redditService.getRedditsts(term)
                .thenApplyAsync(res -> ok(Json.toJson(res)));
    }

    public CompletionStage<Result> searchSubreddit(String term) {
        return subredditService.getsubRedditsts(term)
                //.thenApplyAsync(Json::toJson)
                .thenApplyAsync(res -> ok(views.html.result.render(res, term)));
    }
}
