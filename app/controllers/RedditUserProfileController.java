package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import services.RedditService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's userProfile page.
 */
public class RedditUserProfileController extends Controller {
    private RedditService redditService;

    @Inject
    public RedditUserProfileController(RedditService redditService) {
        this.redditService = redditService;
    }

//    public CompletionStage<Result> searchSubmission(String author) {
//        return redditService.getSubRedditsByAuthor(author)
//                .thenApplyAsync(res -> ok(views.html.profile.render(res, author)));
//    }

    public CompletionStage<Result> getAuthorProfile(String author) {
        return redditService.getAuthorProfile(author)
                .thenApplyAsync(res -> ok(views.html.profile.render(res)));
    }
}
