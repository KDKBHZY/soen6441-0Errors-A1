package controllers;

import models.Reddit;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSResponse;
import play.mvc.*;
import services.RedditImplemention;
import services.RedditService;
import services.SubredditService;
import views.html.*;
import javax.inject.Inject;
import play.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
    private RedditService redditService;
    private HttpExecutionContext httpExecutionContext;
    private SubredditService subredditService;

    @Inject
    public RedditLyticsController(RedditService redditService ,HttpExecutionContext httpExecutionContext,SubredditService subredditService){
            this.httpExecutionContext = httpExecutionContext;
            this.redditService = redditService;
            this.subredditService = subredditService;
    }

    public CompletionStage<Result> rlyticsIndex() {

        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    public CompletionStage<Result> search(String term) {
        //CompletionStage<WSResponse> responsePromise = re.search("trump");
        //System.out.println("json data:::::::"+redditService.getRedditsts(term));

        return redditService.getRedditsts(term)
                .thenApplyAsync(res->ok(Json.toJson(res)));
    }

    public CompletionStage<Result> searchSubreddit(String term) {

        return subredditService.getsubRedditsts(term)
                //.thenApplyAsync(Json::toJson)
                .thenApplyAsync(res->ok(views.html.result.render(res,term)));
    }


}
