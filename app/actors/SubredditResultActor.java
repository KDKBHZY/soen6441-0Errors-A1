package actors;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.Reddit;
import scala.concurrent.duration.Duration;
import services.RedditService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class SubredditResultActor  extends AbstractActorWithTimers {
    @Inject
    private RedditService redditService;
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private String query;
    private List<Reddit> reddits;
    private ActorRef subredditactor;


    public SubredditResultActor(){
        this.subredditactor = null;
        this.query = null;
    }


    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new SubredditResultActor.Filter(), Duration.create(15, TimeUnit.SECONDS));
    }
    @Override
    public Receive createReceive() {
        return  receiveBuilder()
                .match(Messages.RegisterActor.class, message -> {
                    logger.info("Registering actor {}", message);
                    subredditactor = sender();
                    getSender().tell("UserActor registered", getSelf());
                })
                .match(SubredditResultActor.Filter.class, message -> {
                    System.out.println("Received subreddit message Filter {}"+message);
                    if (query != null) {
                        filterMessage();
                    }
                })
                .match(Messages.WatchsubRedditResults.class, message -> {
                    System.out.println("Received subreddit message WatchSearchResults: "+message);
                    if (message != null && message.query != null) {
                        watchsubredditResult(message);
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
    public CompletionStage<Void> watchsubredditResult(Messages.WatchsubRedditResults message) {
        // Set the query
        query = message.query;

        return redditService.getSubreddits(query)
                .thenAcceptAsync(searchResults -> {
                    // This is the first time we want to watch a (new) query: reset the list
                    this.reddits = new ArrayList<>(10);
                    this.reddits.addAll(searchResults.subList(0,10));

                    // Add all the statuses to the list
                    System.out.println("----subreddits,,,okkkkk");
                    //  reddits.forEach(status -> status.setQuery(query));

                    Messages.RedditsMessage redditMessage =
                            new Messages.RedditsMessage(reddits, query);

                    subredditactor.tell(redditMessage, self());
                });
    }

    /**
     * watchSearchResult message handling
     * @return CompletionStage of void
     */
    public CompletionStage<Void> filterMessage() {
        return redditService.getSubreddits(query).thenAcceptAsync(searchResults -> {

            List<Reddit> oldReddits = new ArrayList<>(reddits);
            //update  local reddits
            reddits.clear();
            reddits.addAll(searchResults.subList(0,10));
            System.out.println("old subreddit:  "+query+"  id: "+oldReddits.get(0).getRedditID());

            List<Reddit> newReddits = new ArrayList<>(10);
            newReddits.addAll(searchResults.subList(0,10));
            System.out.println("new subreddit:  "+query+"  id: "+newReddits.get(0).getRedditID());
            if (!newReddits.get(0).getRedditID().equals(oldReddits.get(0).getRedditID())){
                newReddits.removeAll(oldReddits);
                System.out.println("!!!!change subreddit: "+query+ " number:  "+newReddits.size());
                Messages.RedditsMessage redditsMessage =
                        new Messages.RedditsMessage(newReddits, query);

                subredditactor.tell(redditsMessage, self());
            }

        });
    }
}
