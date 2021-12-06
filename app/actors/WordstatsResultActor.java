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

public class WordstatsResultActor  extends AbstractActorWithTimers {
    @Inject
    private RedditService redditService;
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private String query;
    private List<Reddit> reddits;
    private ActorRef wordstatsactor;


    public WordstatsResultActor(){
        this.wordstatsactor = null;
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
                    wordstatsactor = sender();
                    getSender().tell("WordstatsActor registered", getSelf());
                })
                .match(Filter.class, message -> {
                    System.out.println("Received wordstats message Filter {}"+message);
                    if (query != null) {
                        filterMessage();
                    }
                })
                .match(Messages.WatchwordstatsResults.class, message -> {
                    System.out.println("Received wordstats message WatchSearchResults: "+message);
                    if (message != null && message.query != null) {
                        watchwordstatsResult(message);
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
    public CompletionStage<Void> watchwordstatsResult(Messages.WatchwordstatsResults message) {
        // Set the query
        query = message.query;

        return redditService.getReddits(query)
                .thenAcceptAsync(searchResults -> {
                    // This is the first time we want to watch a (new) query: reset the list
                    this.reddits = new ArrayList<>(searchResults);;

                    // Add all the statuses to the list
                    System.out.println("----Wordstats Loaded");

                    Messages.WordstatasMessage wordstatsMessage =
                            new Messages.WordstatasMessage(reddits, query);

                    wordstatsactor.tell(wordstatsMessage, self());
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
            reddits.addAll(searchResults);
            System.out.println("old:  "+query+"  id: "+oldReddits.get(0).getRedditID());

            List<Reddit> newReddits = new ArrayList<>(searchResults);
            System.out.println("new:  "+query+"  id: "+newReddits.get(0).getRedditID());
            if (!newReddits.get(0).getRedditID().equals(oldReddits.get(0).getRedditID())){
                newReddits.removeAll(oldReddits);
                System.out.println("!!!!change: "+query+ " number:  "+newReddits.size());
                Messages.WordstatasMessage wordstatasMessage =
                        new Messages.WordstatasMessage(newReddits, query);

                wordstatsactor.tell(wordstatasMessage, self());
            }else {
                Messages.UnwatchWordstatsResults nonewdata =
                        new Messages.UnwatchWordstatsResults(query);

                wordstatsactor.tell(nonewdata, self());
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
