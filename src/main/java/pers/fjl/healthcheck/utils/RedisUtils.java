package pers.fjl.healthcheck.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * Get cache
     *
     * @param key Key
     * @return Value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * Set cache
     *
     * @param key   Key
     * @param value Value
     * @return true if successful, false if failed
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("Can not set cache into redis, key {}, value {}", key, value, e);
            return false;
        }
    }

    /**
     * Set cache and specify expiration time
     *
     * @param key   Key
     * @param value Value
     * @param time  Expiration time (in seconds). A time less than or equal to 0 means no limit.
     * @return true if successful, false if failed
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("Can not set cache into redis, key {}, value {}", key, value, e);
            return false;
        }
    }

    /**
     * Delete cache
     *
     * @param key 1 or many keys
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

}
