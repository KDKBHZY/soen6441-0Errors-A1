package test.controllers;


import controllers.SubRedditController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import play.inject.Injector;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceInjectorBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import services.RedditApi;
import services.RedditService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;

/** 
* SubRedditController Tester. 
* 
* @author Zeyu Huang
* @since <pre>11月 8, 2021</pre> 
* @version 1.0 
*/ 
public class SubRedditControllerTest {
    private static RedditService redditService;

    private static SubRedditController subRedditController;

    private static Injector testApp;

    @BeforeAll
    public void before() throws Exception {
//    Http.Context context = mock(Http.Context.class);
//    Http.Context.current.set(context);
//    HttpExecutionContext ec = new GuiceApplicationBuilder().injector().instanceOf(HttpExecutionContext.class);
//
//    testApp = new GuiceInjectorBuilder()
//            .overrides(bind(RedditApi.class).to(RedditTestImplementation.class))
//            .build();
//    redditService = testApp.instanceOf(RedditService.class);
//    subRedditController = new SubRedditController(redditService, ec);
    }


    /**
     * Method: searchSubreddit(String term)
     */
    @Test
    public void testSearchSubreddit() throws Exception {
//    Result result = subRedditController.searchSubreddit("nba").toCompletableFuture().get();
//    assertEquals(OK, result.status());
//    assertEquals("text/html", result.contentType().get());
//    assertEquals("utf-8", result.charset().get());


    }
}
