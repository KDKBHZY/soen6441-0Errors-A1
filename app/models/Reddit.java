package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO of Reddit
 *
 * @author Yongshi Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reddit {

    @JsonProperty("id")
    private String redditID;
    private String author;
    private String subreddit;
    private String title;

    /**
     * Parse the POJO of Reddit to a String
     *
     * @return a string with the author name, subreddit and the title of the submission
     */
    @Override
    public String toString() {
        return String.format("Author: %s, %s, \"%s\"", this.author, this.subreddit, this.title);
    }

    /**
     * Gets redditID of the Reddit
     *
     * @return redditID
     */
    public String getRedditID() {
        return redditID;
    }

    /**
     * Sets redditID of the Reddit by given id
     *
     * @param id given string of id
     */
    public void setRedditID(String id) {
        this.redditID = id;
    }

    /**
     * Gets author of the Reddit
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author of the Reddit by given name of the author
     *
     * @param author given string of author's name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets title of the Reddit
     *
     * @return title
     */
    public String getSubReddit() {
        return subreddit;
    }

    /**
     * Sets subreddit of the Reddit by given subreddit
     *
     * @param subreddit given string of subreddit
     */
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    /**
     * Gets title of the Reddit
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title of the Reddit by given title
     *
     * @param title given string of title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
