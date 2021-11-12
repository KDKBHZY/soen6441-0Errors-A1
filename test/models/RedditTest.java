package models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test Reddit model
 * @author Zeyu Huang
 */
public class RedditTest {

    @Test
    public void testToString() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        r.setSubreddit("nba");
        r.setTitle("nba");
        assertEquals("Author: hzy, nba, \"nba\"",r.toString());


    }

    @Test
    public void getRedditID() {
        Reddit r = new Reddit();
        r.setRedditID("100");
        assertEquals("100",r.getRedditID());

    }

    @Test
    public void setRedditID() {
        Reddit r = new Reddit();
        r.setRedditID("100");
        assertEquals("100",r.getRedditID());
    }

    @Test
    public void getAuthor() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        assertEquals("hzy",r.getAuthor());
    }

    @Test
    public void setAuthor() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        assertEquals("hzy",r.getAuthor());
    }

    @Test
    public void getSubReddit() {
        Reddit r = new Reddit();
        r.setSubreddit("nba");
        assertEquals("nba",r.getSubReddit());
    }

    @Test
    public void setSubreddit() {
        Reddit r = new Reddit();
        r.setSubreddit("nba");
        assertEquals("nba",r.getSubReddit());
    }

    @Test
    public void getTitle() {
        Reddit r = new Reddit();
        r.setTitle("nba");
        assertEquals("nba",r.getTitle());
    }

    @Test
    public void setTitle() {
        Reddit r = new Reddit();
        r.setTitle("nba");
        assertEquals("nba",r.getTitle());
    }
}