package com.a301.newsseug.external.redis.repository;

import java.util.Map;
import java.util.Optional;

public interface RedisHashRepository<H, HK, HV> {

    void save(H hash, HK key, HV value);
    Map<H, HV> findAndDeleteByHash(H hash);
    Optional<HV> findByKey(H hash, HK key);
    Long increment(H hash, HK key, HV value);
    void deleteByKey(H hash, HK key);

}
