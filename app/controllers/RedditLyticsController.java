package controllers;

import actors.Messages;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.name.Named;
import models.Reddit;
import org.slf4j.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import scala.concurrent.duration.Duration;
import services.RedditService;
import views.html.*;

import javax.inject.Inject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static akka.pattern.PatternsCS.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {
    @Inject
    private RedditService redditService;
    private final Timeout t = new Timeout(Duration.create(1, TimeUnit.SECONDS));
    private final Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.RedditLyticsController");
    private final ActorRef redditparentactor;
    private final ActorRef subredditparentactor;

    /**
     * Constructor
     */
    @Inject
    public RedditLyticsController(@Named("reddit-ParentActor") ActorRef redditparentactor, @Named("subreddit-ParentActor") ActorRef subredditparentactor) {
        this.redditparentactor = redditparentactor;
        this.subredditparentactor = subredditparentactor;
    }

    public CompletionStage<Result> index() {
        return CompletableFuture.completedFuture(ok(views.html.index.render()));
    }
    /**
     * searchpage websocket
     * @author: Zeyu Huang
     * @return data to front end
     */
    public WebSocket subRedditws() {
        return WebSocket.Json.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {
                final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = subRedditwsFutureFlow(request);
                return future.thenApply(F.Either::Right);
            } else {
                return forbiddenResult();
            }
        });
    }

    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> subRedditwsFutureFlow(Http.RequestHeader request) {
        long id = request.asScala().id();
        Messages.SubredditActorCreate create = new Messages.SubredditActorCreate(Long.toString(id));

        return ask(subredditparentactor, create, t).thenApply((Object flow) -> {

            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
            return f.named("subredditsocket");
        });
    }

    /**
     * searchpage websocket
     * @author: Zeyu Huang
     * @return data to front end
     */
    public WebSocket ws() {
        return WebSocket.Json.acceptOrResult(request -> {
            if (sameOriginCheck(request)) {
                final CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> future = wsFutureFlow(request);
                return future.thenApply(F.Either::Right);
            } else {
                return forbiddenResult();
            }
        });
    }

    private CompletionStage<Flow<JsonNode, JsonNode, NotUsed>> wsFutureFlow(Http.RequestHeader request) {
        long id = request.asScala().id();
        Messages.UserParentActorCreate create = new Messages.UserParentActorCreate(Long.toString(id));

        return ask(redditparentactor, create, t).thenApply((Object flow) -> {

            final Flow<JsonNode, JsonNode, NotUsed> f = (Flow<JsonNode, JsonNode, NotUsed>) flow;
            return f.named("websocket");
        });
    }

    private CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final F.Either<Result, Flow<JsonNode, JsonNode, ?>> left = F.Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }


    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (origin.isEmpty()) {
            logger.error("originCheck: rejecting request because no Origin header found");
            return false;
        } else if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin: "
                    + String.join(", ", validOrigins));
            return false;
        }
    }

    private List<String> validOrigins = Arrays.asList("localhost:9000", "localhost:19001");
    private boolean originMatches(String actualOrigin) {
        return validOrigins.stream().anyMatch(actualOrigin::contains);
    }


    /**
     * display search page
     *
     * @author ZeYu Huang
     */
    public CompletionStage<Result> rlyticsIndex() {
        return CompletableFuture.completedFuture(ok(rlytics.render()));
    }

    /**
     * use term to search 10 latest submission
     *
     * @param term keyword
     * @author ZeYu Huang
     */
    public CompletionStage<Result> search(String term) {
        return redditService.getReddits(term)
                .thenApplyAsync(res -> ok(Json.toJson(res)));
    }

    /**
     * Displays the user profile and its latest submissions for the given author
     *
     * @param author username of the Reddit user
     * @return CompletionStage of Result
     */
    public CompletionStage<Result> getAuthorProfile(String author) {
        return redditService.getAuthorProfile(author)
                .thenApplyAsync(res -> ok(views.html.profile.render(res)));
    }

    /**
     * use subreddit from main page to search 10 latest submission in this subreddit
     *
     * @param subreddit keyword
     * @author ZeYu Huang
     */
    public CompletionStage<Result> searchSubreddit(String subreddit) {
        return redditService.getSubreddits(subreddit)
                .thenApplyAsync(res -> ok(views.html.result.render(res, subreddit)));
    }

    /**
     * Display statistics result
     *
     * @param term Key word for the searching.
     * @author Shuo Gao
     */
    public CompletionStage<Result> statistics(String term) {
        return redditService.getReddits(term)
                .thenApplyAsync(res -> ok(wordstats.render(statistics(res), term)));
    }

    /**
     * Perform the statistics of words
     *
     * @param reddits Search results to be handled
     * @return A list of Pairs contains words and frequencies
     * @author Shuo Gao
     */
    private List<Map.Entry<String, Long>> statistics(List<Reddit> reddits) {
        Map<String, Long> words = reddits.stream()
                .map(Reddit::getTitle)
                .flatMap(w -> {
                    return Stream.of(w.split("[\\W]"));
                })
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        List<Map.Entry<String, Long>> res = words.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(w -> w.getKey().length() > 0)
                .collect(Collectors.toList());
        System.out.println("List of " + res.size());
        return res;
    }


}
