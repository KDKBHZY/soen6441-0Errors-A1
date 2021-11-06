package controllers;

import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSResponse;
import play.mvc.*;
import services.RedditImplemention;
import services.RedditService;
import views.html.*;
import javax.inject.Inject;
import play.api.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
    private RedditService redditService;
    private HttpExecutionContext httpExecutionContext;

    @Inject
public RedditLyticsController(RedditService redditService ,HttpExecutionContext ec){
        this.httpExecutionContext = ec;

        this.redditService = redditService;
}
//
    public Result rlyticsIndex() {
        return ok(views.html.rlytics.render());
    }

    public CompletionStage<Result> search(String term) {
    System.out.println(term);
        //CompletionStage<WSResponse> responsePromise = re.search("trump");
        //System.out.println("json data:::::::"+redditService.getRedditsts(term));

        return redditService.getRedditsts(term)
                .thenApplyAsync(res -> ok(views.html.result.render(res)),
                httpExecutionContext.current());
       // return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

}
