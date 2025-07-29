package com.a301.newsseug.external.jwt.repository;

import com.a301.newsseug.external.redis.repository.RedisKeyValueRepository;

public interface RedisTokenRepository extends RedisKeyValueRepository<String, String> {

}
