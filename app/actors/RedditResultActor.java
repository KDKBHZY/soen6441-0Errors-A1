package actors;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Reddit;
import scala.concurrent.duration.Duration;
import akka.actor.AbstractActorWithTimers;
import services.RedditService;

import javax.inject.Inject;

public class RedditResultActor extends AbstractActorWithTimers {
    @Inject
    private RedditService redditService;
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private String query;
    private Set<Reddit> reddits;
    private ActorRef redditactor;


    public RedditResultActor(){
        this.redditactor = null;
        this.query = null;
}


    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Filter(), Duration.create(5, TimeUnit.SECONDS));
    }
    @Override
    public Receive createReceive() {
        return  receiveBuilder()
                .match(Messages.RegisterActor.class, message -> {
            logger.info("Registering actor {}", message);
            redditactor = sender();
            getSender().tell("UserActor registered", getSelf());
        })
                .match(Filter.class, message -> {
                    logger.info("Received message Filter {}", message);
                    if (query != null) {
                       filterMessage();
                    }
                })
                .match(Messages.WatchSearchResults.class, message -> {
                    System.out.println("Received message WatchSearchResults: "+message);
                    if (message != null && message.query != null) {
                        watchSearchResult(message);
                    }
                })
                .build();
    }

    private static final class Filter {
    }

    /**
     * watchSearchResult message handling
     * @param message message to handle
     * @return CompletionStage of Void
     */
    public CompletionStage<Void> watchSearchResult(Messages.WatchSearchResults message) {
        // Set the query
        query = message.query;

        return redditService.getReddits(query).thenAcceptAsync(searchResults -> {
            // This is the first time we want to watch a (new) query: reset the list
            this.reddits = new HashSet<>();

            // Add all the statuses to the list
            reddits.addAll(searchResults);
          System.out.println("!!!reddits,,,okkkkk");
          //  reddits.forEach(status -> status.setQuery(query));

            Messages.RedditsMessage redditMessage =
                    new Messages.RedditsMessage(reddits, query);

            redditactor.tell(redditMessage, self());
        });
    }

    /**
     * watchSearchResult message handling
     * @return CompletionStage of void
     */
    public CompletionStage<Void> filterMessage() {
        // Every 5 seconds, check for new tweets if we have a query
        return redditService.getReddits(query).thenAcceptAsync(searchResults -> {
            // Copy the current state of statuses in a temporary variable
            Set<Reddit> oldReddits = new HashSet<>(reddits);

            // Add all the statuses to the list, now filtered to only add the new ones
            reddits.addAll(searchResults);

            // Copy the current state of statuses after addition in a temporary variable
            Set<Reddit> newReddits = new HashSet<>(reddits);

            // Get the new statuses only by doing new - old = what we have to display
            newReddits.removeAll(oldReddits);

           // newReddits.forEach(status -> status.setQuery(query));

            Messages.RedditsMessage statusesMessage =
                    new Messages.RedditsMessage(newReddits, query);

            redditactor.tell(statusesMessage, self());
        });
    }
}