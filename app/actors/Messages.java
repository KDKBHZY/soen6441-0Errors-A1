package actors;

import models.Reddit;
import models.User;

import java.util.List;

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

    public static final class AuthorProfileActorCreate {
        public final String id;

        public AuthorProfileActorCreate(String id) {
            this.id = requireNonNull(id);
        }

        @Override
        public String toString() {
            return "AuthorProfileActorCreate(" + id + ")";
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

    public static final class WatchAuthorProfileResults {
        public final String query;

        public WatchAuthorProfileResults(String query) {
            this.query = requireNonNull(query);
        }

        @Override
        public String toString() {
            return "WatchAuthorProfileResults(" + query + ")";
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
     * UnwatchSearchResults Message
     */
    public static final class UnwatchAuthorProfile {
        public final String query;

        public UnwatchAuthorProfile(String query) {
            this.query = requireNonNull(query);
        }

        @Override
        public String toString() {
            return "UnwatchAuthorProfile(" + query + ")";
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
     * AuthorProfile Message
     */
    public static final class AuthorProfileMessage {
        public final User author;

        public AuthorProfileMessage(User author) {
            this.author = requireNonNull(author);
        }

        @Override
        public String toString() {
            return "AuthorProfileMessage(" + author.getName() + ")";
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

    /**
     * Override toString for the Messages class
     * @return string "Messages"
     */
    @Override
    public String toString() {
        return "Messages";
    }

}
