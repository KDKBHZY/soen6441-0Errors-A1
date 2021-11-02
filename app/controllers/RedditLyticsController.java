package controllers;

import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's redditLytics page.
 */
public class RedditLyticsController extends Controller {

    /**
     * An action that renders an HTML page with a input for searching reddit submissions
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/redditlytics</code>.
     */
    public Result rlyticsIndex() {
        return ok(views.html.rlytics.render());
    }

}
