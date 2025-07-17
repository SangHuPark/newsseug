package com.a301.newsseug.external.caffeine.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    ARTICLE("article", 50, 180);

    private final String name;
    private final int maximumSize;
    private final int expireAfterWrite;

}
