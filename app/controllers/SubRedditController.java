package controllers;

import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.RedditService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class SubRedditController extends Controller {
    private RedditService redditService;

    /**
     * @des:    Constructor function
     * @author: ZeYu Huang
     * @param redditService
     */
    @Inject
    public SubRedditController(RedditService redditService) {
        this.redditService = redditService;
    }

    /**
     * @des: use subreddit from main page to search 10 latest submission in this subreddit
     * @author: ZeYu Huang
     * @param subreddit
     */

    public  CompletionStage<Result> searchSubreddit(String subreddit) {
        return redditService.getsubRedditsts(subreddit)
                //.thenApplyAsync(Json::toJson)
                .thenApplyAsync(res -> ok(views.html.result.render(res, subreddit)));
    }
}
