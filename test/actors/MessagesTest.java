package actors;

import org.junit.Test;
import org.testng.Assert;

import static org.junit.Assert.*;

public class MessagesTest {

    @Test
    public void testRedditParentActorCreate() {
        Messages.UserParentActorCreate redditParentActorCreate = new Messages.UserParentActorCreate("1");
        Assert.assertEquals("1", redditParentActorCreate.id);
        Assert.assertEquals("UserParentActorCreate(1)", redditParentActorCreate.toString());
    }

    @Test
    public void testWatchRedditResults() {
        Messages.WatchSearchResults watchSearchResults = new Messages.WatchSearchResults("test");
        Assert.assertEquals("test", watchSearchResults.query);
        Assert.assertEquals("WatchSearchResults(test)", watchSearchResults.toString());
    }

    @Test
    public void testUnWatchRedditResults() {
        Messages.UnwatchSearchResults unwatchSearchResults = new Messages.UnwatchSearchResults("test");
        Assert.assertEquals("test", unwatchSearchResults.query);
        Assert.assertEquals("UnwatchSearchResults(test)", unwatchSearchResults.toString());
    }

    @Test
    public void testSubRedditParentActorCreate() {
        Messages.SubredditActorCreate subredditActorCreate = new Messages.SubredditActorCreate("1");
        Assert.assertEquals("1", subredditActorCreate.id);
        Assert.assertEquals("UserParentActorCreate(1)", subredditActorCreate.toString());
    }

    @Test
    public void testWatchsubRedditResults() {
        Messages.WatchsubRedditResults watchsubRedditResults = new Messages.WatchsubRedditResults("test");
        Assert.assertEquals("test", watchsubRedditResults.query);
        Assert.assertEquals("WatchsubRedditResults(test)", watchsubRedditResults.toString());
    }

    @Test
    public void testAuthorProfileParentActorCreate() {
        Messages.AuthorProfileActorCreate authorProfileActorCreate = new Messages.AuthorProfileActorCreate("1");
        Assert.assertEquals("1", authorProfileActorCreate.id);
        Assert.assertEquals("AuthorProfileActorCreate(1)", authorProfileActorCreate.toString());
    }

    @Test
    public void testWatchAuthorProfileResults() {
        Messages.WatchAuthorProfileResults watchAuthorProfileResults = new Messages.WatchAuthorProfileResults("test");
        Assert.assertEquals("test", watchAuthorProfileResults.query);
        Assert.assertEquals("WatchAuthorProfileResults(test)", watchAuthorProfileResults.toString());
    }

    @Test
    public void testUnWatchAuthorProfileResults() {
        Messages.UnwatchAuthorProfile unwatchAuthorProfile = new Messages.UnwatchAuthorProfile("test");
        Assert.assertEquals("test", unwatchAuthorProfile.query);
        Assert.assertEquals("UnwatchAuthorProfile(test)", unwatchAuthorProfile.toString());
    }

    @Test
    public void testRegisterActor() {
        Messages.RegisterActor registerActor = new Messages.RegisterActor();
        Assert.assertEquals("RegisterActor", registerActor.toString());
    }

//    @Test
//    public void testMessage() {
//        Messages messages = new Messages();
//        Assert.assertEquals("Messages", messages.toString());
//    }
}
