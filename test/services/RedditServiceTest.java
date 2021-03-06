package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Reddit;
import models.User;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.inject.Bindings.bind;

/**
 * Test the {@link RedditService} class
 *
 * @author Yongshi Liang
 */
public class RedditServiceTest {

    private static RedditService redditService;

    /**
     * Initialize the test application and bind {@link RedditApi} interface to
     * its mock implementation {@link RedditImplementationMock}
     */
    @BeforeClass
    public static void initTestApp() {
        Injector testApp = new GuiceInjectorBuilder()
                .overrides(bind(RedditApi.class).to(RedditImplementationMock.class))
                .build();
        redditService = testApp.instanceOf(RedditService.class);
    }

    @After
    public void resetObjectMapper() {
        redditService.setObjectMapper(new ObjectMapper());
    }

    /**
     * Test for getReddits by comparing the sample result and
     * the result returned by the mock implementation
     */
    @Test
    public void getReddits() {
        List<Reddit> result = redditService.getReddits("test")
                .toCompletableFuture()
                .join();

        assertEquals(25, result.size());
    }

    /**
     * Test for fail to getReddits
     */
    @Test
    public void getRedditsFail() {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(Reddit[].class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redditService.setObjectMapper(om);
        List<Reddit> badResult = redditService.getReddits("test")
                .toCompletableFuture()
                .join();
        assertNull(badResult);
    }

    /**
     * Test for getSubreddits by comparing the sample result and
     * the result returned by the mock implementation
     */
    @Test
    public void getSubreddits() {
        List<Reddit> result = redditService.getSubreddits("test subreddit")
                .toCompletableFuture()
                .join();

        assertEquals(25, result.size());
    }

    /**
     * Test for fail to getSubreddits
     */
    @Test
    public void getSubredditsFail() {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(Reddit[].class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redditService.setObjectMapper(om);
        List<Reddit> badResult = redditService.getSubreddits("test")
                .toCompletableFuture()
                .join();
        assertNull(badResult);
    }

    /**
     * Test for getSubredditsByAuthor by comparing the sample result and
     * the result returned by the mock implementation
     */
    @Test
    public void getSubredditsByAuthor() {
        List<Reddit> result = redditService.getSubredditsByAuthor("testAuthor")
                .toCompletableFuture()
                .join();

        assertEquals(25, result.size());
    }

    /**
     * Test for fail to getSubredditsByAuthor
     */
    @Test
    public void getSubredditsByAuthorFail() {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(Reddit[].class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redditService.setObjectMapper(om);
        List<Reddit> badResult = redditService.getSubredditsByAuthor("test")
                .toCompletableFuture()
                .join();
        assertNull(badResult);
    }

    /**
     * Test for parseReddits by using the given test sample Json file
     *
     * @throws Exception when given test sample file is not found
     */
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

    /**
     * Test for fail to parseReddits
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void parseRedditsFail() throws Exception {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(Reddit[].class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String jsonStr = readFileAsString("test/resources/searchReddits.json");
        JsonNode submissions = new ObjectMapper().readTree(jsonStr);

        redditService.setObjectMapper(om);
        List<Reddit> badResult = redditService.parseReddits(submissions);

        assertNull(badResult);
    }

    /**
     * Test for getAuthorProfile by comparing the sample result and
     * the result returned by the mock implementation
     */
    @Test
    public void getAuthorProfile() {
        User result = redditService.getAuthorProfile("testAuthor")
                .toCompletableFuture()
                .join();

        assertNotNull(result);
        assertEquals(25, result.getPostedReddits().size());
    }

    /**
     * Test for fail to getAuthorProfile
     */
    @Test
    public void getAuthorProfileFail() {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(User.class))).thenThrow(new JsonProcessingException("") {
            });
            when(om.treeToValue(any(TreeNode.class), eq(Reddit[].class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        redditService.setObjectMapper(om);
        User badResult = redditService.getAuthorProfile("testAuthor")
                .toCompletableFuture()
                .join();
        assertNull(badResult);
    }

    /**
     * Test for parseUser by using the given test sample Json file
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void parseUser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setName("testAuthor");
        user.setUserID("test userID");
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

    /**
     * Test for fail to parseUser
     *
     * @throws Exception when given test sample file is not found
     */
    @Test
    public void parseUserFail() throws Exception {
        ObjectMapper om = mock(ObjectMapper.class);
        try {
            when(om.treeToValue(any(TreeNode.class), eq(User.class))).thenThrow(new JsonProcessingException("") {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String jsonStr = readFileAsString("test/resources/userProfile.json");
        JsonNode profile = new ObjectMapper().readTree(jsonStr);

        redditService.setObjectMapper(om);
        User badResult = redditService.parseUser(profile);

        assertNull(badResult);
    }

    /**
     * Test for addPostedReddit
     */
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

        assertNull(redditService.addPostedReddit(null, reddits));
        User user2 = new User();
        assertEquals(user2, redditService.addPostedReddit(user2, null));
    }

    /**
     * Converts the input Json file with the given path into string
     *
     * @param file Json file path
     * @return File string
     * @throws Exception when given file in not found
     */
    private static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}