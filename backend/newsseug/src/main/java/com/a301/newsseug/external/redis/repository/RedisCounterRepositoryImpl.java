package com.a301.newsseug.external.redis.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCounterRepositoryImpl implements RedisCounterRepository {

    private static final String script = """
            local result = redis.call('HGETALL', KEYS[1])
            if #result > 0 then
                local keys = {}
                for i = 1, #result, 2 do
                    table.insert(keys, result[i])
                end
                redis.call('HDEL', KEYS[1], unpack(keys))
            end
            return result
        """;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String hash, String HashKey, Long value) {
        redisTemplate.opsForHash().put(hash, HashKey, value);
    }

    @Override
    public Map<String, Long> findAndDeleteByHash(String hash) {

        /**
         * List<Object> results = redisTemplate.execute(
         *                 new DefaultRedisScript<>(script, List.class),
         *                 Collections.singletonList(hash)
         *         );
         */

        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>(script, List.class);
        List results = redisTemplate.execute(redisScript, Collections.singletonList(hash));

        Map<String, Long> map = new HashMap<>();
        if (Objects.nonNull(results)) {
            for (int offset = 0; offset < results.size(); offset += 2) {
                String field = String.valueOf(results.get(offset));
                String valueStr = String.valueOf(results.get(offset + 1));
                try {
                    Long value = Long.parseLong(valueStr);
                    map.put(field, value);
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse value for field: {} with value: {}", field, valueStr);
                }
            }
        }

        return map;

    }

    @Override
    public Optional<Long> findByKey(String hash, String HashKey) {

        Number value = (Number) redisTemplate.opsForHash().get(hash, HashKey);

        if (Objects.nonNull(value)) {
            return Optional.of(value.longValue());
        }

        return Optional.empty();

    }

    @Override
    public Long increment(String hash, String HashKey, Long value) {
        return redisTemplate.opsForHash().increment(hash, HashKey, value);
    }

    @Override
    public void deleteByKey(String hash, String hashKey) {
        redisTemplate.opsForHash().delete(hash, hashKey);
    }

}
