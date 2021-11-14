package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.inject.Injector;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceInjectorBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                .overrides((bind(RedditApi.class).to(RedditImplementationMock.class)))
                .build();
        redditService = testApp.instanceOf(RedditService.class);

    }

    @Test
    public void getReddits() {
        List<Reddit> result = redditService.getReddits("test")
                .toCompletableFuture()
                .join();

        assertEquals(25, result.size());
    }

    @Test
    public void getsubRedditsts() {
        List<Reddit> result = redditService.getSubreddits("test")
                .toCompletableFuture()
                .join();
        assertEquals("test",result.get(0).getSubReddit());

        assertEquals(25, result.size());
    }

    @Test
    public void getSubRedditsByAuthor() {
        List<Reddit> result = redditService.getSubredditsByAuthor("testAuthor")
                .toCompletableFuture()
                .join();

        assertEquals(25, result.size());
    }

    @Test
    public void parseReddits() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = readFileAsString("test/resources/searchReddits.json");
        JsonNode submissions = mapper.readTree(jsonStr);
        List<Reddit> result = redditService.parseReddits(submissions);

        assertEquals(25, result.size());
        assertEquals("qtbyzc", result.get(0).getRedditID());
        assertEquals("testAuthor", result.get(0).getAuthor());
        assertEquals("test subreddit", result.get(0).getSubReddit());
        assertEquals("[ISO][US] Malezia urea moisturizer", result.get(0).getTitle());
    }

    @Test
    public void getAuthorProfile() {
        User result = redditService.getAuthorProfile("testAuthor")
                .toCompletableFuture()
                .join();

        assertNotNull(result);
        assertEquals(25, result.getPostedReddits().size());
    }

    @Test
    public void parseUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setUserID("test userID");
        user.setName("testAuthor");
        user.setAwardeeKarma(6);
        user.setAwarderKarma(4);
        user.setLinkKarma(20);
        user.setCommentKarma(120);
        user.setTotalKarma(150);
        user.setCreateDate(1636675200);
        user.setSnoovatarImgUrl("test img Url");

        String jsonStr = readFileAsString("test/resources/userProfile.json");
        JsonNode profile = mapper.readTree(jsonStr);
        User result = redditService.parseUser(profile);

        assertNotNull(result);
        assertEquals(user.getUserID(), result.getUserID());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getAwardeeKarma(), result.getAwardeeKarma());
        assertEquals(user.getAwarderKarma(), result.getAwarderKarma());
        assertEquals(user.getLinkKarma(), result.getLinkKarma());
        assertEquals(user.getCommentKarma(), result.getCommentKarma());
        assertEquals(user.getTotalKarma(), result.getTotalKarma());
        assertEquals(user.getCreateDate(), result.getCreateDate());
        assertEquals(user.getSnoovatarImgUrl(), result.getSnoovatarImgUrl());
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

        User result = redditService.addPostedReddit(new User(), reddits);

        assertEquals(user.getPostedReddits().size(), result.getPostedReddits().size());
    }

    private static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}