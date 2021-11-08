package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String userID;
    private String name;
    private int awardeeKarma;
    private int awarderKarma;
    private int linkKarma;
    private int commentKarma;
    private int totalKarma;
    private Date createDate;
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

    public int getlinkKarma() {
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

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String epochString) {
        this.createDate = new Date(Long.parseLong(epochString) * 1000);
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
