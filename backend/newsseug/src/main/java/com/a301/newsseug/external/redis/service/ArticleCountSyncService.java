package com.a301.newsseug.external.redis.service;

import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.external.caffeine.manager.ArticleCacheManager;
import com.a301.newsseug.external.redis.repository.RedisHashRepository;
import com.a301.newsseug.external.redisson.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCountSyncService {

    private final RedisHashRepository redisHashRepository;
    private final ArticleCacheManager articleCacheManager;
    private final ArticleRepository articleRepository;

    @Scheduled(cron = "0 0/5 * * * ?")
    @DistributedLock(key = "'VIEW_COUNT_SYNC'", waitTime = 5L, leaseTime = 280L)
    public void scheduledSyncViewCounting() {
        syncCounting("article:viewCount:", "viewCount");
    }

    @Scheduled(cron = "0 0/7 * * * ?")
    @DistributedLock(key = "'LIKE_COUNT_SYNC'", waitTime = 5L, leaseTime = 390L)
    public void scheduledSyncLikeCounting() {
        syncCounting("article:likeCount:", "likeCount");
    }

    @Scheduled(cron = "0 0/7 * * * ?")
    @DistributedLock(key = "'HATE_COUNT_SYNC'", waitTime = 5L, leaseTime = 390L)
    public void scheduledSyncHateCounting() {
        syncCounting("article:hateCount:", "hateCount");
    }

    private void syncCounting(String hash, String field) {
        Map<String, Long> countingLog = redisHashRepository.findAndDeleteByHash(hash);
        if (Objects.nonNull(countingLog) && !countingLog.isEmpty()) {
            articleRepository.updateCount(field, countingLog);
            articleCacheManager.evictBatch(countingLog.keySet());
        }
    }

}
