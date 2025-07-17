package com.a301.newsseug.external.caffeine.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import java.util.Objects;
import java.util.Set;

public abstract class BaseCacheManager<T, ID> {

    private final CacheManager cacheManager;

    // 명시적으로 CacheManager가 있어야만 이 클래스를 인스턴스화
    protected BaseCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected abstract String getCacheName();
    protected abstract T loadFromRepository(ID id);

    @Cacheable(value = "#{this.getCacheName()}", key = "#id")
    public T getCached(ID id) {
        return loadFromRepository(id);
    }

    public void evict(ID id) {
        Objects.requireNonNull(cacheManager.getCache(getCacheName())).evict(id);
    }

    public void evictBatch(Set<ID> ids) {
        Cache cache = Objects.requireNonNull(cacheManager.getCache(getCacheName()));
        ids.forEach(cache::evict);
    }

}