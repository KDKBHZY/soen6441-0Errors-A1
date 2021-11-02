package services;

import models.Reddit;

import java.util.List;

public interface RedditService {

    List<Reddit> getRedditsByKey(String keyword);
}
