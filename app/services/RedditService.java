package services;

import models.Reddit;

import play.libs.ws.WSResponse;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface RedditService {
    CompletionStage<WSResponse> search(String keyword);
   // List<Reddit> getRedditsByKey(String keyword);
}
