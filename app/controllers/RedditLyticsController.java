package controllers;

import play.libs.Json;
import play.mvc.*;
import services.RedditService;
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

    @Inject
    public RedditLyticsController(RedditService redditService) {
        this.redditService = redditService;
    }

    public CompletionStage<Result> rlyticsIndex() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    public CompletionStage<Result> search(String term) {
        return redditService.getRedditsts(term)
                .thenApplyAsync(res -> ok(Json.toJson(res)));
    }

    public CompletionStage<Result> searchSubreddit(String term) {
        return redditService.getsubRedditsts(term)
                //.thenApplyAsync(Json::toJson)
                .thenApplyAsync(res -> ok(views.html.result.render(res, term)));
    }
}
