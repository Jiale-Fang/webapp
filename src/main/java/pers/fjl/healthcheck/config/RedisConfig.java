package pers.fjl.healthcheck.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // Set key as String
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                // Set value as Object automatically converted to Json
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                // Do not cache null values
                .disableCachingNullValues()
                // Cache data saved for 1 hour
                .entryTtl(Duration.ofHours(1));
        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                // Redis connection factory
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                // Cache configuration
                .cacheDefaults(defaultCacheConfiguration)
                // Configure synchronous modification or deletion put/evict
                .transactionAware()
                .build();

        return redisCacheManager;
    }

    /**
     * Redis template config
     *
     * @param factory The Redis connection factory
     * @return The configured Redis template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Configure connection factory
        template.setConnectionFactory(factory);

        // Use Jackson2JsonRedisSerializer to serialize and deserialize the value of redis (default using JDK's serialization method)
        Jackson2JsonRedisSerializer<Object> jacksonSerial = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper om = new ObjectMapper();
        // Specify the domain to be serialized, field, get and set, as well as the scope of modifiers, ANY includes both private and public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // Specify the type of the serialization input, the class must be non-final modified, final modified classes, such as String, Integer, etc. will throw an exception
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSerial.setObjectMapper(om);

        // Values are serialized in json
        template.setValueSerializer(jacksonSerial);
        // Use StringRedisSerializer to serialize and deserialize the key value of redis
        template.setKeySerializer(new StringRedisSerializer());

        // Set hash key and value serialization mode
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSerial);
        template.afterPropertiesSet();

        return template;
    }
}
