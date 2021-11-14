package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.inject.Bindings.bind;

/**
 * Test the {@link RedditService} class
 */
public class RedditServiceTest {

    private static RedditService redditService;

    @BeforeClass
    public static void initTestApp() {
        Injector testApp = new GuiceInjectorBuilder()
                .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                .build();
        redditService = testApp.instanceOf(RedditService.class);
    }

    @Test
    public void getReddits() {
        List<Reddit> result = redditService.getReddits("test")
                .toCompletableFuture()
                .join();

        assertEquals(10, result.size());
    }

    @Test
    public void getsubRedditsts() {
        List<Reddit> result = redditService.getSubreddits("subreddit")
                .toCompletableFuture()
                .join();

        assertEquals(10, result.size());
    }

    @Test
    public void getSubRedditsByAuthor() {
        List<Reddit> result = redditService.getSubredditsByAuthor("testAuthor")
                .toCompletableFuture()
                .join();

        assertEquals(10, result.size());
    }

    @Test
    public void parseReddits() {
//        om = mock(ObjectMapper.class);
//        List<Reddit> reddits = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Reddit reddit = new Reddit();
//            reddit.setRedditID("redditId " + i);
//            reddit.setAuthor("author "+ i);
//            reddit.setSubreddit("subreddit");
//            reddit.setTitle("submission_title " + i);
//            reddits.add(reddit);
//        }
//
//        List<Reddit> result = redditService.parseReddits();
//        assertEquals(10, result.size());
//        assertEquals(reddits.get(4).getRedditID(), result.get(4).getRedditID());
//        assertEquals(reddits.get(4).getAuthor(), result.get(4).getAuthor());
//        assertEquals(reddits.get(4).getSubReddit(), result.get(4).getSubReddit());
//        assertEquals(reddits.get(4).getTitle(), result.get(4).getTitle());
    }

    @Test
    public void getAuthorProfile() {
        User result = redditService.getAuthorProfile("testAuthor")
                .toCompletableFuture()
                .join();

        assertNotNull(result);
        assertEquals(10, result.getPostedReddits().size());
    }

    @Test
    public void parseUser() {
//        om = mock(ObjectMapper.class);
//        User user = new User();
//        user.setUserID("test userID");
//        user.setName("testAuthor");
//        user.setAwardeeKarma(6);
//        user.setAwarderKarma(4);
//        user.setLinkKarma(20);
//        user.setCommentKarma(120);
//        user.setTotalKarma(150);
//        user.setCreateDate(1636675200);
//        user.setSnoovatarImgUrl("test img Url");
//
//        User result = redditService.parseUser();
//
//        assertEquals(user.getUserID(), result.getUserID());
//        assertEquals(user.getName(), result.getName());
//        assertEquals(user.getAwardeeKarma(), result.getAwardeeKarma());
//        assertEquals(user.getAwarderKarma(), result.getAwarderKarma());
//        assertEquals(user.getLinkKarma(), result.getLinkKarma());
//        assertEquals(user.getCommentKarma(), result.getCommentKarma());
//        assertEquals(user.getTotalKarma(), result.getCommentKarma());
//        assertEquals(user.getCreateDate(), result.getCreateDate());
//        assertEquals(user.getSnoovatarImgUrl(), result.getSnoovatarImgUrl());
    }

    @Test
    public void addPostedReddit() {
        List<Reddit> reddits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reddit reddit = new Reddit();
            reddit.setRedditID("redditId " + i);
            reddit.setAuthor("testAuthor");
            reddit.setSubreddit("subreddit " + i);
            reddit.setTitle("submissionTitle " + i);
            reddits.add(reddit);
        }
        User user = mock(User.class);
        when(user.getPostedReddits()).thenReturn(reddits);

        User result = redditService.addPostedReddit(user, reddits);

        assertEquals(user.getPostedReddits().size(), result.getPostedReddits().size());
    }
}