package models;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for the {@link User} class
 * @author Yongshi Liang
 */
public class UserTest {

    /**
     * Getter test for UserID
     */
    @Test
    public void getUserID() {
        String ID = "userId";
        User user = new User();
        user.setUserID(ID);
        assertEquals(ID, user.getUserID());
    }

    /**
     * Setter test for UserID
     */
    @Test
    public void setUserID() {
        String ID = "userId";
        User user = new User();
        user.setUserID(ID);
        assertEquals(ID, user.getUserID());
    }

    /**
     * Getter test for Name
     */
    @Test
    public void getName() {
        String name = "username";
        User user = new User();
        user.setName(name);
        assertEquals(name, user.getName());
    }

    /**
     * Setter test for Name
     */
    @Test
    public void setName() {
        String name = "username";
        User user = new User();
        user.setName(name);
        assertEquals(name, user.getName());
    }

    /**
     * Getter test for AwardeeKarma
     */
    @Test
    public void getAwardeeKarma() {
        int awardeeKarma = 384;
        User user = new User();
        user.setAwardeeKarma(awardeeKarma);
        assertEquals(awardeeKarma, user.getAwardeeKarma());
    }

    /**
     * Setter test for AwardeeKarma
     */
    @Test
    public void setAwardeeKarma() {
        int awardeeKarma = 0;
        User user = new User();
        user.setAwardeeKarma(awardeeKarma);
        assertEquals(awardeeKarma, user.getAwardeeKarma());
    }

    /**
     * Getter test for AwarderKarma
     */
    @Test
    public void getAwarderKarma() {
        int awarderKarma = -3;
        User user = new User();
        user.setAwarderKarma(awarderKarma);
        assertEquals(awarderKarma, user.getAwarderKarma());
    }

    /**
     * Setter test for AwarderKarma
     */
    @Test
    public void setAwarderKarma() {
        int awarderKarma = -289;
        User user = new User();
        user.setAwarderKarma(awarderKarma);
        assertEquals(awarderKarma, user.getAwarderKarma());
    }

    /**
     * Getter test for LinkKarma
     */
    @Test
    public void getLinkKarma() {
        int linkKarma = 834;
        User user = new User();
        user.setLinkKarma(linkKarma);
        assertEquals(linkKarma, user.getLinkKarma());
    }

    /**
     * Setter test for LinkKarma
     */
    @Test
    public void setLinkKarma() {
        int linkKarma = -834;
        User user = new User();
        user.setLinkKarma(linkKarma);
        assertEquals(linkKarma, user.getLinkKarma());
    }

    /**
     * Getter test for CommentKarma
     */
    @Test
    public void getCommentKarma() {
        int commentKarma = 299;
        User user = new User();
        user.setCommentKarma(commentKarma);
        assertEquals(commentKarma, user.getCommentKarma());
    }

    /**
     * Setter test for CommentKarma
     */
    @Test
    public void setCommentKarma() {
        int commentKarma = -299;
        User user = new User();
        user.setCommentKarma(commentKarma);
        assertEquals(commentKarma, user.getCommentKarma());
    }

    /**
     * Getter test for TotalKarma
     */
    @Test
    public void getTotalKarma() {
        int totalKarma = 3923;
        User user = new User();
        user.setTotalKarma(totalKarma);
        assertEquals(totalKarma, user.getTotalKarma());
    }

    /**
     * Setter test for TotalKarma
     */
    @Test
    public void setTotalKarma() {
        int totalKarma = -3923;
        User user = new User();
        user.setTotalKarma(totalKarma);
        assertEquals(totalKarma, user.getTotalKarma());
    }

    /**
     * Getter test for CreateDate
     */
    @Test
    public void getCreateDate() {
        String createDate = "11/11/2021 19:00:00";
        long epochTimestamp = 1636675200;
        User user = new User();
        user.setCreateDate(epochTimestamp);
        assertEquals(createDate, user.getCreateDate());
    }

    /**
     * Setter test for CreateDate
     */
    @Test
    public void setCreateDate() {
        long epochTimestamp = 1636675200;
        String formatter = "MM/dd/yyyy HH:mm:ss";
        Date createDate = new Date(2021 - 1900, Calendar.NOVEMBER,11,19, 0, 0);
        User user = new User();
        user.setCreateDate(epochTimestamp);
        assertEquals(new java.text.SimpleDateFormat(formatter).format(createDate), user.getCreateDate());
    }

    /**
     * Getter test for SnoovatarImgUrl
     */
    @Test
    public void getSnoovatarImgUrl() {
        String url = "Http://_jdkasjflkdjsa";
        User user = new User();
        user.setSnoovatarImgUrl(url);
        assertEquals(url, user.getSnoovatarImgUrl());
    }

    /**
     * Setter test for SnoovatarImgUrl
     */
    @Test
    public void setSnoovatarImgUrl() {
        String url = "";
        User user = new User();
        user.setSnoovatarImgUrl(url);
        assertEquals(url, user.getSnoovatarImgUrl());
    }

    /**
     * Getter test for PostedReddits
     */
    @Test
    public void getPostedReddits() {
        List<Reddit> reddits = Arrays.asList(new Reddit(), new Reddit(), new Reddit());
        User user = new User();
        reddits.forEach(r -> user.getPostedReddits().add(r));
        assertEquals(reddits.size(), user.getPostedReddits().size());
    }
}