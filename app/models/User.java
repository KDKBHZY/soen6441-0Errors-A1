package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id")
    private String userID;
    private String name;
    @JsonProperty("awardee_karma")
    private int awardeeKarma;
    @JsonProperty("awarder_karma")
    private int awarderKarma;
    @JsonProperty("link_karma")
    private int linkKarma;
    @JsonProperty("comment_karma")
    private int commentKarma;
    @JsonProperty("total_karma")
    private int totalKarma;
    @JsonProperty("created")
    private String createDate;
    @JsonProperty("snoovatar_img")
    private String snoovatarImgUrl;

    private List<Reddit> postedSubmissions;

    public User() {
        this.postedSubmissions = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAwardeeKarma() {
        return this.awardeeKarma;
    }

    public void setAwardeeKarma(int awardeeKarma) {
        this.awardeeKarma = awardeeKarma;
    }

    public int getAwarderKarma() {
        return this.awarderKarma;
    }

    public void setAwarderKarma(int awarderKarma) {
        this.awarderKarma = awarderKarma;
    }

    public int getLinkKarma() {
        return this.linkKarma;
    }

    public void setLinkKarma(int linkKarma) {
        this.linkKarma = linkKarma;
    }

    public int getCommentKarma() {
        return this.commentKarma;
    }

    public void setCommentKarma(int commentKarma) {
        this.commentKarma = commentKarma;
    }

    public int getTotalKarma() {
        return this.totalKarma;
    }

    public void setTotalKarma(int totalKarma) {
        this.totalKarma = totalKarma;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long epoch) {
        this.createDate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch * 1000));
    }

    public String getSnoovatarImgUrl() {
        return this.snoovatarImgUrl;
    }

    public void setSnoovatarImgUrl(String snoovatarImgUrl) {
        this.snoovatarImgUrl = snoovatarImgUrl;
    }

    public List<Reddit> getPostedSubmissions() {
        return postedSubmissions;
    }
}
