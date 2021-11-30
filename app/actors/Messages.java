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
            Reddit r = new Reddit();
            r.setAuthor(query);
            this.reddits = requireNonNull(reddits);
            this.reddits.add(r);
            this.query = requireNonNull(query);
        }

        @Override
        public String toString() {
            return "StatusesMessage(" + query + ")";
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
