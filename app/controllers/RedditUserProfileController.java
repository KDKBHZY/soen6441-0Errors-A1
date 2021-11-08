package controllers;

import models.Reddit;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.RedditService;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's userProfile page.
 */
public class RedditUserProfileController extends Controller {
    private final RedditService redditService;

    @Inject
    public RedditUserProfileController(RedditService redditService) {
        this.redditService = redditService;
    }

    public CompletionStage<Result> getAuthorProfile(String author) {
        List<Reddit> authorSubmissions = redditService.getSubRedditsByAuthor(author).toCompletableFuture().join();
        return redditService.getAuthorProfile(author)
                .thenApplyAsync(res -> ok(views.html.profile.render(res, authorSubmissions)));
    }
}
