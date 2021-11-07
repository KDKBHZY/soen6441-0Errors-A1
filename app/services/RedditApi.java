package services;

import models.Reddit;

import play.libs.ws.WSResponse;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface RedditApi {
    CompletionStage<WSResponse> search(String keyword);

    CompletionStage<WSResponse> searchSubreddit(String keyword);
}
