package actors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Reddit> reddits;
    private ActorRef redditactor;


    public RedditResultActor(){
        this.redditactor = null;
        this.query = null;
}


    @Override
    public void preStart() {
        getTimers().startPeriodicTimer("Timer", new Filter(), Duration.create(2, TimeUnit.SECONDS));
    }
    @Override
    public Receive createReceive() {
        return  receiveBuilder()
                .match(Messages.RegisterActor.class, message -> {
            logger.info("Registering actor {}", message);
            redditactor = sender();
            getSender().tell("Redditactor registered", getSelf());
        })
                .match(Filter.class, message -> {
                    System.out.println("Received message Filter {}"+message);
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

    public static final class Filter {
    }

    /**
     * watchSearchResult message handling
     * @param message message to handle
     * @return CompletionStage of Void
     */
    public CompletionStage<Void> watchSearchResult(Messages.WatchSearchResults message) {
        // Set the query
        query = message.query;

        return redditService.getReddits(query)
                .thenAcceptAsync(searchResults -> {
            // This is the first time we want to watch a (new) query: reset the list
            this.reddits = new ArrayList<>(10);
            this.reddits.addAll(searchResults.subList(0,10));

            // Add all the statuses to the list
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
        return redditService.getReddits(query).thenAcceptAsync(searchResults -> {

            List<Reddit> oldReddits = new ArrayList<>(reddits);
            //update  local reddits
            reddits.clear();
            reddits.addAll(searchResults.subList(0,10));
            System.out.println("old:  "+query+"  id: "+oldReddits.get(0).getRedditID());

            List<Reddit> newReddits = new ArrayList<>(10);
            newReddits.addAll(searchResults.subList(0,10));
            System.out.println("new:  "+query+"  id: "+newReddits.get(0).getRedditID());
            if (!newReddits.get(0).getRedditID().equals(oldReddits.get(0).getRedditID())){
                newReddits.removeAll(oldReddits);
                System.out.println("!!!!change: "+query+ " number:  "+newReddits.size());
                Messages.RedditsMessage redditsMessage =
                        new Messages.RedditsMessage(newReddits, query);

                redditactor.tell(redditsMessage, self());
            }else {
                Messages.UnwatchSearchResults nonewdata =
                        new Messages.UnwatchSearchResults(query);

                redditactor.tell(nonewdata, self());
            }


        });
    }
    /**
     * Set Reddit Service
     * @param redditService twitterService
     */
    public void setRedditService(RedditService redditService) {
        this.redditService = redditService;
    }
}