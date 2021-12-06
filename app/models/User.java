package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A POJO of User
 *
 * @author Yongshi Liang
 */
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

    private List<Reddit> postedReddits;

    /**
     * Constructor of a User
     */
    public User() {
        this.postedReddits = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;

        return (Objects.equals(this.getUserID(), ((User) obj).getUserID())
                && Objects.equals(this.getName(), ((User) obj).getName())
                && Objects.equals(this.getAwardeeKarma(), ((User) obj).getAwardeeKarma())
                && Objects.equals(this.getAwarderKarma(), ((User) obj).getAwarderKarma())
                && Objects.equals(this.getCommentKarma(), ((User) obj).getCommentKarma())
                && Objects.equals(this.getLinkKarma(), ((User) obj).getLinkKarma())
                && Objects.equals(this.getTotalKarma(), ((User) obj).getTotalKarma())
                && Objects.equals(this.getSnoovatarImgUrl(), ((User) obj).getSnoovatarImgUrl()));
    }

    /**
     * Gets userID of the user
     *
     * @return userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets userID of the user with the given id
     *
     * @param id given String of id
     */
    public void setUserID(String id) {
        this.userID = id;
    }

    /**
     * Gets name of the user
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name of the user by the given name
     *
     * @param name given string of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets awardeeKarma of the user
     *
     * @return awardeeKarma
     */
    public int getAwardeeKarma() {
        return this.awardeeKarma;
    }

    /**
     * Sets awardeeKarma of the user by given awardeeKarma
     *
     * @param awardeeKarma given integer of awardeeKarma
     */
    public void setAwardeeKarma(int awardeeKarma) {
        this.awardeeKarma = awardeeKarma;
    }

    /**
     * Gets awarderKarma of the user
     *
     * @return awarderKarma
     */
    public int getAwarderKarma() {
        return this.awarderKarma;
    }

    /**
     * Sets awarderKarma of the user by given awarderKarma
     *
     * @param awarderKarma given integer of awarderKarma
     */
    public void setAwarderKarma(int awarderKarma) {
        this.awarderKarma = awarderKarma;
    }

    /**
     * Gets linkKarma of the user
     *
     * @return linkKarma
     */
    public int getLinkKarma() {
        return this.linkKarma;
    }

    /**
     * Sets linkKarma of the user by given linkKarma
     *
     * @param linkKarma given integer of linkKarma
     */
    public void setLinkKarma(int linkKarma) {
        this.linkKarma = linkKarma;
    }

    /**
     * Gets commentKarma of the user
     *
     * @return commentKarma
     */
    public int getCommentKarma() {
        return this.commentKarma;
    }

    /**
     * Sets commentKarma of the user by given commentKarma
     *
     * @param commentKarma given integer of commentKarma
     */
    public void setCommentKarma(int commentKarma) {
        this.commentKarma = commentKarma;
    }

    /**
     * Gets totalKarma of the user
     *
     * @return totalKarma
     */
    public int getTotalKarma() {
        return this.totalKarma;
    }

    /**
     * Sets totalKarma of the user by given totalKarma
     *
     * @param totalKarma given integer of totalKarma
     */
    public void setTotalKarma(int totalKarma) {
        this.totalKarma = totalKarma;
    }

    /**
     * Gets createDate of the user
     *
     * @return createDate
     */
    public String getCreateDate() {
        return this.createDate;
    }

    /**
     * Sets createDate of the user by given epoch timestamp
     *
     * @param epoch given long of epoch timestamp
     */
    public void setCreateDate(long epoch) {
        this.createDate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(epoch * 1000));
    }

    /**
     * Gets snoovatarImgUrl of the user
     *
     * @return snoovatarImgUrl
     */
    public String getSnoovatarImgUrl() {
        return this.snoovatarImgUrl;
    }

    /**
     * Sets snoovatarImgUrl of the user by given snoovatarImgUrl
     *
     * @param snoovatarImgUrl given string of snoovatarImgUrl
     */
    public void setSnoovatarImgUrl(String snoovatarImgUrl) {
        this.snoovatarImgUrl = snoovatarImgUrl;
    }

    /**
     * Gets a List of posted submissions of the user
     *
     * @return a list of {@link Reddit}
     */
    public List<Reddit> getPostedReddits() {
        return postedReddits;
    }
}
