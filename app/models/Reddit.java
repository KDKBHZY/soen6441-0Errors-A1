package models;

public class Reddit {

    private String redditID;
    private User author;
    private String subReddit;
    private String submission;

    public Reddit(String id, User author, String subReddit, String submission) {
        this.redditID = id;
        this.author = author;
        this.subReddit = subReddit;
        this.submission = submission;
    }

    @Override
    public String toString() {
        return String.format("Author: %s, %s, \"%s\"", this.author, this.subReddit, this.submission);
    }

    public String getRedditID() {
        return redditID;
    }

    public User getAuthor() {
        return author;
    }

    public String getSubReddit() {
        return subReddit;
    }

    public String getSubmission() {
        return submission;
    }
}
