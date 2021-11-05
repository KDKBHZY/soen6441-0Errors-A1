package controllers;

import play.libs.ws.WSResponse;
import play.mvc.*;
import services.RedditImplemention;
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
    private RedditImplemention re;
@Inject
public RedditLyticsController(RedditService redditService ,RedditImplemention re ){
    this.redditService = redditService;
    this.re = re;
}
//
    public Result rlyticsIndex() {
        return ok(views.html.rlytics.render());
    }
    public CompletionStage<Result> search() {
        CompletionStage<WSResponse> responsePromise = re.search("trump");
        System.out.println("json data:::::::"+redditService.getTweets("trump"));
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }
}
