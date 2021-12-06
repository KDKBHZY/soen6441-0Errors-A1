package models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * test Reddit model
 *
 * @author Zeyu Huang
 */
public class RedditTest {

    @Test
    public void testToString() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        r.setSubreddit("nba");
        r.setTitle("nba");
        assertEquals("Author: hzy, nba, \"nba\"", r.toString());
    }
    @Test
    public void getTerm() {
        Reddit r = new Reddit();
        r.setTerm("nba");
        assertEquals("nba", r.getTerm());
    }

    @Test
    public void setTerm() {
        Reddit r = new Reddit();
        r.setTerm("nba");
        assertEquals("nba", r.getTerm());
    }


    @Test
    public void getRedditID() {
        Reddit r = new Reddit();
        r.setRedditID("100");
        assertEquals("100", r.getRedditID());

    }

    @Test
    public void testEquals() {
        Reddit r1 = new Reddit();
        r1.setRedditID("1");
        r1.setAuthor("test");
        r1.setSubreddit("test");
        r1.setTitle("test");
        Reddit r2 = new Reddit();
        r2.setRedditID("1");
        r2.setAuthor("test");
        r2.setSubreddit("test");
        r2.setTitle("test");

        assertTrue(r1.equals(r2));

        r2.setTerm("test");
        assertTrue(r1.equals(r2));

        r2.setTitle("title");
        assertFalse(r1.equals(r2));
    }
    @Test
    public void testHashCode() {
        Reddit r1 = new Reddit();
        r1.setRedditID("1");
        r1.setAuthor("test");
        r1.setSubreddit("test");
        r1.setTitle("test");
        Reddit r2 = new Reddit();
        r2.setRedditID("1");
        r2.setAuthor("test");
        r2.setSubreddit("test");
        r2.setTitle("test");

        assertEquals(r1.hashCode(), r2.hashCode());
    }


        @Test
    public void setRedditID() {
        Reddit r = new Reddit();
        r.setRedditID("100");
        assertEquals("100", r.getRedditID());
    }

    @Test
    public void getAuthor() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        assertEquals("hzy", r.getAuthor());
    }

    @Test
    public void setAuthor() {
        Reddit r = new Reddit();
        r.setAuthor("hzy");
        assertEquals("hzy", r.getAuthor());
    }

    @Test
    public void getSubReddit() {
        Reddit r = new Reddit();
        r.setSubreddit("nba");
        assertEquals("nba", r.getSubReddit());
    }

    @Test
    public void setSubreddit() {
        Reddit r = new Reddit();
        r.setSubreddit("nba");
        assertEquals("nba", r.getSubReddit());
    }

    @Test
    public void getTitle() {
        Reddit r = new Reddit();
        r.setTitle("nba");
        assertEquals("nba", r.getTitle());
    }

    @Test
    public void setTitle() {
        Reddit r = new Reddit();
        r.setTitle("nba");
        assertEquals("nba", r.getTitle());
    }
}