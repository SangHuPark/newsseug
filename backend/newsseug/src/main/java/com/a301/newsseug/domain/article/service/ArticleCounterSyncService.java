package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.external.caffeine.manager.ArticleCacheManager;
import com.a301.newsseug.external.redis.repository.RedisCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCounterSyncService {

    private final RedisCounterRepository redisCounterService;
    private final ArticleRepository articleRepository;
    private final ArticleCacheManager articleCacheManager;

    @Scheduled(cron = "0 0/7 * * * ?")
    public void syncCounts() {
        syncCount("article:likeCount:", "likeCount");
//        syncCount("article:hateCount:", "hateCount");
//        syncCount("article:viewCount:", "viewCount");
    }

    private void syncCount(String hash, String column) {
        Map<String, Long> countLogs = redisCounterService.findAndDeleteByHash(hash);

        if (Objects.nonNull(countLogs) && !countLogs.isEmpty()) {
//            log.info("Updating articleId: {}, New {}: {}", articleId, column, count);
//            redisCounterService.deleteByKey(hash, articleId);
            articleRepository.updateCount(column, countLogs);
            articleCacheManager.evictBatch(countLogs.keySet());
        }

    }
}
