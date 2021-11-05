package models;

public class Reddit {

    private String subreddit_id;
    private String author;
    private String subreddit;
    private String title;

    public Reddit(String subreddit_id, String author, String subReddit, String title) {
        this.subreddit_id = subreddit_id;
        this.author = author;
        this.subreddit = subReddit;
        this.title = title;
    }



    @Override
    public String toString() {
        return String.format("Author: %s, %s, \"%s\"", this.author, this.subreddit, this.title);
    }

    public String getRedditID() {
        return subreddit_id;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubReddit() {
        return subreddit;
    }

    public String getSubmission() {
        return title;
    }
}
