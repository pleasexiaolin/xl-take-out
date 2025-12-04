package com.xiaolin.utils.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // ============================= common ============================//

    /**
     * 指定缓存失效时间
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("设置Redis键过期时间失败，key: {}", key, e);
        }
    }

    /**
     * 根据 key 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("检查Redis键是否存在失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存 可多个key
     */
    public void del(String... key) {
        if (key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 根据key的前缀 删除缓存 可多为个key  例如 dish:则是删除dish:下全部的value
     */
    public void delByPrefix(String... preKey) {
        for (String prefix : preKey) {
            try {
                // 使用Redis的keys命令查找匹配前缀的所有key
                Set<String> keys = redisTemplate.keys(prefix + "*");
                if (keys != null && !keys.isEmpty()) {
                    // 批量删除找到的key
                    redisTemplate.delete(keys);
                }
            } catch (Exception e) {
                log.error("根据前缀删除缓存失败，prefix: {}", prefix, e);
            }
        }
    }

    // ============================= String ============================

    /**
     * 普通缓存获取
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("普通缓存放入失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
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
            log.error("普普通缓存放入并设置时间失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 递增
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    // ============================= Map ============================

    /**
     * HashGet
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("HashSet存入失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("HashSet 存入并设置时间失败，key: {}", key, e);
            return false;
        }
    }

    // ============================= Set ============================

    /**
     * 根据key获取Set中的所有值
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("根据key获取Set中的所有值失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 将数据放入set缓存
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("将数据放入set缓存失败，key: {}", key, e);
            return 0L;
        }
    }

    // ============================= ZSet ============================

    /**
     * 向有序集合添加元素
     */
    public Boolean zAdd(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error("ZSet 将向有序集合添加元素失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取有序集合指定范围元素
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("ZSet 获取有序集合指定范围元素失败，key: {}", key, e);
            return null;
        }
    }

    // ============================= 分布式锁 ============================

    /**
     * 尝试获取锁
     * @param key 锁的名称，用于唯一标识一个锁资源
     * @param value 锁的值，通常使用UUID等唯一标识，用于后续释放锁时的身份验证
     * @param timeout 锁的超时时间，超过该时间锁会自动释放
     * @param unit 超时时间的时间单位（如秒、毫秒等）
     * @return 当 key 不存在时，设置成功并返回 true
     *         当 key 已存在时，设置失败并返回 false
     */
    public Boolean tryLock(String key, String value, long timeout, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
    }

    /**
     * 释放锁
     * @param key 锁的名称，用于唯一标识一个锁资源
     * @param value 锁的值，通常使用UUID等唯一标识，用于后续释放锁时的身份验证
     */
    public Boolean releaseLock(String key, String value) {
        // 使用Lua脚本保证原子性
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";
        Long result = redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.eval(
                        script.getBytes(),
                        ReturnType.INTEGER,
                        1,
                        key.getBytes(),
                        value.getBytes()
                )
        );
        return result != null && result == 1L;
    }
}
