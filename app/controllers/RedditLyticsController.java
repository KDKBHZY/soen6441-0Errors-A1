package controllers;

import play.mvc.*;
//import scala.collection.parallel.immutable.ParRange;
import services.RedditService;
import views.html.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
private RedditService redditService;
public RedditLyticsController(RedditService redditService){
    this.redditService = redditService;
}
//
    public Result rlyticsIndex() {
        return ok(views.html.rlytics.render());
    }
    public CompletionStage<Result> search() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }
}
