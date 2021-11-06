package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reddit {

    @JsonProperty("id")
    private String subreddit_id;
    private String author;
    private String subreddit;
    private String title;

    public Reddit() {

    }

//    public Reddit(String subreddit_id, String author, String subReddit, String title) {
//        this.subreddit_id = subreddit_id;
//        this.author = author;
//        this.subreddit = subReddit;
//        this.title = title;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Reddit reddit = (Reddit) o;
//        return Objects.equals(subreddit_id, reddit.subreddit_id)
//                && Objects.equals(author, reddit.author)
//                && Objects.equals(subreddit, reddit.subreddit)
//                && Objects.equals(title, reddit.title);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash()
//    }

    @Override
    public String toString() {
        return String.format("Author: %s, %s, \"%s\"", this.author, this.subreddit, this.title);
    }

    public String getRedditID() {
        return subreddit_id;
    }

    public void setSubreddit_id(String id) {
        this.subreddit_id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubReddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getSubmission() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
