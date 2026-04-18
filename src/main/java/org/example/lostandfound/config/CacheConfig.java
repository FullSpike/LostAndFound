package org.example.lostandfound.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        // 配置序列化方式
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // 设置缓存过期时间
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        RedisSerializer.json() // 使用推荐的 JSON 序列化器
                ));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}