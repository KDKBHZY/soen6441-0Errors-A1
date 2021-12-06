package actors;

import models.Reddit;

import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class Messages {
    /**
     * Create UserParentActor Message
     */
    public static final class UserParentActorCreate {
        public final String id;

        public UserParentActorCreate(String id) {
            this.id = requireNonNull(id);
        }

        @Override
        public String toString() {
            return "UserParentActorCreate(" + id + ")";
        }
    }

    /**
     * Create SubredditActor Message
     */
    public static final class SubredditActorCreate {
        public final String id;

        public SubredditActorCreate(String id) {
            this.id = requireNonNull(id);
        }

        @Override
        public String toString() {
            return "UserParentActorCreate(" + id + ")";
        }
    }
    /**
     * WatchSearchResults Message
     */
    public static final class WatchSearchResults {
        public final String query;

        public WatchSearchResults(String query) {
            this.query = requireNonNull(query);
        }
        @Override
        public String toString() {
            return "WatchSearchResults(" + query + ")";
        }
    }

    /**
     * WatchsubRedditResults Message
     */
    public static final class WatchsubRedditResults {
        public final String query;

        public WatchsubRedditResults(String query) {
            this.query = requireNonNull(query);
        }
        @Override
        public String toString() {
            return "WatchsubRedditResults(" + query + ")";
        }
    }

    /**
     * UnwatchSearchResults Message
     */
    public static final class UnwatchSearchResults {
        public final String query;

        public UnwatchSearchResults(String query) {
            this.query = requireNonNull(query);
        }

        @Override
        public String toString() {
            return "UnwatchSearchResults(" + query + ")";
        }
    }

    /**
     * RedditsMessage Message
     */
    public static final class RedditsMessage {
        public final List<Reddit> reddits;
        public final String query;

        public RedditsMessage(List<Reddit> reddits, String query) {

            this.reddits = requireNonNull(reddits);
            for (Reddit reddit : this.reddits) {
                reddit.setTerm(query);
            }
            this.query = requireNonNull(query);
        }

        @Override
        public String toString() {
            return "RedditMessage(" + query + ")";
        }
    }

    /**
     * RegisterActor Message
     */
    public static final class RegisterActor {
        @Override
        public String toString() {
            return "RegisterActor";
        }
    }

}
