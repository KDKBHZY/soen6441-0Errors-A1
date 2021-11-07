package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reddit {

    @JsonProperty("id")
    private String subredditID;
    private String author;
    private String subreddit;
    private String title;

    @Override
    public String toString() {
        return String.format("Author: %s, %s, \"%s\"", this.author, this.subreddit, this.title);
    }

    public String getRedditID() {
        return subredditID;
    }

    public void setSubredditID(String id) {
        this.subredditID = id;
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
