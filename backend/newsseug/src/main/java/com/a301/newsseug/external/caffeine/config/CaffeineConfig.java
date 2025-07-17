package com.a301.newsseug.external.caffeine.config;

import com.a301.newsseug.external.caffeine.type.CacheType;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches =
                Arrays.stream(CacheType.values())
                        .map(cache -> {
                            Caffeine<Object, Object> builder = Caffeine.newBuilder()
                                    .maximumSize(cache.getMaximumSize())
                                    .recordStats();

                            if (cache.getExpireAfterWrite() > 0) {
                                builder.expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS);
                            }

                            return new CaffeineCache(cache.getName(), builder.build());
                        }).toList();

        cacheManager.setCaches(caches);

        return cacheManager;
    }
}
