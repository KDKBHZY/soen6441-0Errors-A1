package models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userID;
    private List<String> postedSubmissions;

    public User (String id) {
        this.userID = id;
        this.postedSubmissions = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public List<String> getPostedSubmissions() {
        return postedSubmissions;
    }
}
