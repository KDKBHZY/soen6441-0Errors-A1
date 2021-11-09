package controllers;

import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.RedditService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class SubRedditController extends Controller {
    private RedditService redditService;

    @Inject
    public SubRedditController(RedditService redditService, HttpExecutionContext ec) {
        this.redditService = redditService;
    }
    public  CompletionStage<Result> searchSubreddit(String term) {
        return redditService.getsubRedditsts(term)
                //.thenApplyAsync(Json::toJson)
                .thenApplyAsync(res -> ok(views.html.result.render(res, term)));
    }
}
