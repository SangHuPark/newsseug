package com.a301.newsseug.external.redis.service;

import com.a301.newsseug.external.redis.buffer.ViewCounterBuffer;
import com.a301.newsseug.external.redis.repository.RedisCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleCounterService {

    private static final String VIEW_COUNT_KEY = "article:viewCount:";
    private static final String LIKE_COUNT_KEY = "article:likeCount:";
    private static final String HATE_COUNT_KEY = "article:hateCount:";

    private final RedisCounterRepository countRepository;
    private final ViewCounterBuffer viewCounterBuffer;

    public long increaseViewCount(Long articleId) {
        return viewCounterBuffer.increment(articleId) + getViewCount(articleId);
    }

    private Long getViewCount(Long articleId) {
        return countRepository.findByKey(VIEW_COUNT_KEY, articleId.toString()).orElse(0L);
    }

    public Long getLikeCount(Long articleId) {
        return countRepository.findByKey(LIKE_COUNT_KEY, articleId.toString()).orElse(0L);
    }

    public Long getHateCount(Long articleId) {
        return countRepository.findByKey(HATE_COUNT_KEY, articleId.toString()).orElse(0L);
    }

}
