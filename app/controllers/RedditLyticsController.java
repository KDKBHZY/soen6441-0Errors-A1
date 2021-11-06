package controllers;

import models.Reddit;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSResponse;
import play.mvc.*;
import services.RedditImplemention;
import services.RedditService;
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

    @Inject
public RedditLyticsController(RedditService redditService ,HttpExecutionContext httpExecutionContext){
        this.httpExecutionContext = httpExecutionContext;
        this.redditService = redditService;
}
//
    public Result rlyticsIndex() {
        return ok(views.html.rlytics.render());
    }

    public CompletionStage<Result> search(String term) {
        //CompletionStage<WSResponse> responsePromise = re.search("trump");
        //System.out.println("json data:::::::"+redditService.getRedditsts(term));

        return redditService.getRedditsts(term)
                .thenApplyAsync(res->ok(views.html.result.render(res)));
                       // httpExecutionContext.current());

        //return CompletableFuture.completedFuture(ok(rlytics.render()));
    }
//    public Result res() {
//        Reddit p1=new Reddit("vsdcv","cvsvd","vs","vd");
//        Reddit p2=new Reddit("vs","cv","vs","vd");
//        List<Reddit> l= new ArrayList<>();
//        l.add(p1);
//        l.add(p2);//这里是将对象加入到list中
//        redditService.getRedditsts("trump")
//        .thenRunAsync(System.out::println);
//        return ok(views.html.result.render(l));
//    }

}
