package com.a301.newsseug.external.caffeine.manager;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.external.caffeine.type.CacheType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleCacheManager {

    private final CacheManager cacheManager;
    private final ArticleRepository articleRepository;

    @Cacheable(value = "article", key = "#p0")
    public Article getCached(Long id) {
        log.info("Cache MISS for articleId={}", id);
        return articleRepository.getOrThrow(id);
    }

    public void evict(Long id) {
        Objects.requireNonNull(cacheManager.getCache(getCacheName())).evict(id);
    }

    public void evictBatch(Set<String> ids) {
        Cache cache = Objects.requireNonNull(cacheManager.getCache(getCacheName()));
        ids.forEach(cache::evict);
    }

    protected String getCacheName() {
        return CacheType.ARTICLE.getName();
    }

}