package services;

import models.Reddit;

import play.libs.ws.WSResponse;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * @des: an interface for Reddit
 * @author: Yongshi Liang && ZeYu Huang
 */
public interface RedditApi {
    CompletionStage<WSResponse> search(String keyword);

    CompletionStage<WSResponse> searchSubreddit(String keyword);

    CompletionStage<WSResponse> searchByAuthor(String author);

    CompletionStage<WSResponse> getAuthorProfile(String author);
}
