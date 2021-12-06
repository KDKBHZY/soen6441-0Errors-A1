package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Reddit;
import models.User;
import scala.concurrent.duration.Duration;
import services.RedditService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class AuthorProfileResultActor extends AbstractActorWithTimers {
    @Inject
    private RedditService redditService;
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private String query;
    private User author;
    private List<Reddit> reddits;
    private ActorRef authorProfileActor;

    /**
     * Constructor of AuthorProfileResultActor
     */
    public AuthorProfileResultActor() {
        this.authorProfileActor = null;
        this.query = null;
    }

    /**
     * Set timer
     */
    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new AuthorProfileResultActor.Filter(), Duration.create(15, TimeUnit.SECONDS));
    }

    private static final class Filter {
    }

    /**
     * Receive block
     *
     * @return receiveBuilder
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RegisterActor.class, message -> {
                    logger.info("Registering actor {}", message);
                    authorProfileActor = sender();
                    getSender().tell("AuthorProfileActor registered", getSelf());
                }).match(Filter.class, message -> {
                    logger.info("Received message Filter {} " + message);
                    if (query != null) {
                        filterMessage();
                    }
                }).match(Messages.WatchAuthorProfileResults.class, message -> {
                    logger.info("Received message WatchAuthorProfileResults: " + message);
                    if (message != null && message.query != null) {
                        watchAuthorProfileResults(message);
                    }
                }).build();
    }

    /**
     * WatchAuthorProfileResults message handling
     * @param message message to handle
     * @return CompletionStage of Void
     */
    private CompletionStage<Void> watchAuthorProfileResults(Messages.WatchAuthorProfileResults message) {
        query = message.query;

        return redditService.getAuthorProfile(query).thenAcceptAsync(authorProfile -> {
            this.author = authorProfile;
            this.reddits = new ArrayList<>();
            this.reddits.addAll(authorProfile.getPostedReddits());
            logger.info("Get author profile");

            Messages.AuthorProfileMessage authorProfileMessage = new Messages.AuthorProfileMessage(author, query);
            authorProfileActor.tell(authorProfileMessage, self());
        });
    }

    /**
     * WatchAuthorProfileResults message handling
     */
    public CompletionStage<Void> filterMessage() {
        return redditService.getAuthorProfile(query).thenAcceptAsync(authorProfileResults -> {
            User oldAuthor = author;
            List<Reddit> oldReddits = new ArrayList<>(reddits);
            logger.info("author: " + query + " old latest post id: " + oldReddits.get(0).getRedditID());
            author = authorProfileResults;
            reddits.clear();
            reddits.addAll(authorProfileResults.getPostedReddits().subList(0, 10));
            logger.info("author: " + query + " new latest post id: " + reddits.get(0).getRedditID());
            if (!oldAuthor.equals(author)
                    || !oldReddits.get(0).getRedditID().equals(reddits.get(0).getRedditID())) {
                logger.info("Author" + query + "updated");
                Messages.AuthorProfileMessage authorProfileMessage = new Messages.AuthorProfileMessage(author, query);
                authorProfileActor.tell(authorProfileMessage, self());
            }
        });
    }
}
