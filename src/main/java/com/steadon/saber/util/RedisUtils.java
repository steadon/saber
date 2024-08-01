package com.steadon.saber.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Component
public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    public static void setKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void setKey(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public static <T> Optional<T> getKey(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key)) {
            return Optional.empty();
        }
        Object value = redisTemplate.opsForValue().get(key);
        return clazz.isInstance(value) ? Optional.of(clazz.cast(value)) : Optional.empty();
    }

    public static void deleteKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            redisTemplate.delete(key);
        }
    }

    public static void deletedAllKey() {
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("*")));
    }

    @Autowired
    public void ReactiveStringRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }
}
