package controllers;

import org.junit.jupiter.api.Test;

import play.mvc.Result;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static play.mvc.Http.Status.OK;

/** 
* HomeController Tester. 
* 
* @author Zeyu Huang
* @since <pre>11月 8, 2021</pre> 
* @version 1.0 
*/ 
public class HomeControllerTest { 


/** 
* 
* Method: index() 
* 
*/ 
@Test
public void testIndex() throws Exception {
    Result result = new HomeController().index().toCompletableFuture().get();
    assertEquals(OK, result.status());
    assertEquals("text/html", result.contentType().get());
    assertEquals("utf-8", result.charset().get());
}


} 
