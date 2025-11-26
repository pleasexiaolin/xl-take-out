package com.xiaolin.utils;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Redis工具类
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {


    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;

    /**
     * 默认过期时长，单位：秒
     */
    @Value("${redis.defaultExpire:86400}")
    public long defaultExpire;

    private  final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis的根操作路径
     */
    @Value("${redis.root:SJZX}")
    private String category;

    public RedisUtil setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * 获取Key的全路径
     *
     * @param key key
     * @return full key
     */
    public String getFullKey(String key) {
        return this.category + ":" + key;
    }


    /**
     * 存入数据
     *
     * @param key
     * @param value
     */
    public void setValue(String key, Object value) {
        setValue(key, value, true);
    }

    /**
     * 存入数据
     *
     * @param key
     * @param value
     */
    public void setValue(String key, Object value, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString());
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 存入数据并设置默认过期时间（一天）
     *
     * @param key
     * @param value
     */
    public void setValueAndExpire(String key, Object value) {
        setValueAndExpire(key, value, true);
    }

    /**
     * 存入数据并设置默认过期时间（一天）
     *
     * @param key
     * @param value
     * @param hasPrefix 是否包含前缀
     */
    public void setValueAndExpire(String key, Object value, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString(), defaultExpire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, defaultExpire, TimeUnit.SECONDS);
        }
    }

    /**
     * 存入数据并设置过期时间，单位秒
     *
     * @param key
     * @param value
     * @param time
     */
    public void setValueAndExpire(String key, Object value, long time) {
        redisTemplate.opsForValue().set(getFullKey(key), value, time, TimeUnit.SECONDS);
    }

    /**
     * 存入数据并设置过期时间，单位秒
     *
     * @param key
     * @param value
     * @param time
     * @param hasPrefix 是否包含前缀
     */
    public void setValueAndExpire(String key, Object value, long time, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString(), time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key获取数据
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return getValue(key, true);
    }

    /**
     * 根据key获取数据
     *
     * @param key
     * @param hasPrefix
     * @return
     */
    public Object getValue(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key获取数据
     *
     * @param key
     * @return
     */
    public Object increment(String key, long var) {
        return increment(key, var, true);
    }

    /**
     * 根据key获取数据
     *
     * @param key
     * @param hasPrefix
     * @return
     */
    public Object increment(String key, long var, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return redisTemplate.opsForValue().increment(key, var);
    }

    /**
     * 判断key是否存在
     *
     * <p>
     * <i>exists key</i>
     *
     * @param key key
     */
    public boolean existsKey(String key) {
        return existsKey(key, true);
    }

    /**
     * 判断key是否存在
     *
     * <p>
     * <i>exists key</i>
     *
     * @param key key
     */
    public boolean existsKey(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return redisTemplate.hasKey(key);
    }

    /**
     * 判断key存储的值类型
     *
     * <p>
     * <i>type key</i>
     *
     * @param key key
     * @return DataType[string、list、set、zset、hash]
     */
    public DataType typeKey(String key) {
        return typeKey(key, true);
    }

    /**
     * 判断key存储的值类型
     *
     * <p>
     * <i>type key</i>
     *
     * @param key       key
     * @param hasPrefix
     * @return DataType[string、list、set、zset、hash]
     */
    public DataType typeKey(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return redisTemplate.type(key);
    }

    /**
     * 重命名key. 如果newKey已经存在，则newKey的原值被覆盖
     *
     * <p>
     * <i>rename oldKey newKey</i>
     *
     * @param oldKey oldKeys
     * @param newKey newKey
     */
    public void renameKey(String oldKey, String newKey) {
        renameKey(oldKey, newKey, true);
    }

    /**
     * 重命名key. 如果newKey已经存在，则newKey的原值被覆盖
     *
     * <p>
     * <i>rename oldKey newKey</i>
     *
     * @param oldKey    oldKeys
     * @param newKey    newKey
     * @param hasPrefix
     */
    public void renameKey(String oldKey, String newKey, boolean hasPrefix) {
        oldKey = getKey(oldKey, hasPrefix);
        newKey = getKey(newKey, hasPrefix);
        redisTemplate.rename(getFullKey(oldKey), getFullKey(newKey));
    }

    /**
     * newKey不存在时才重命名.
     *
     * <p>
     * <i>renamenx oldKey newKey</i>
     *
     * @param oldKey oldKey
     * @param newKey newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNx(String oldKey, String newKey) {
        return renameKeyNx(oldKey, newKey, true);
    }

    /**
     * newKey不存在时才重命名.
     *
     * <p>
     * <i>renamenx oldKey newKey</i>
     *
     * @param oldKey oldKey
     * @param newKey newKey
     * @return 修改成功返回true
     */
    public boolean renameKeyNx(String oldKey, String newKey, boolean hasPrefix) {
        oldKey = getKey(oldKey, hasPrefix);
        newKey = getKey(newKey, hasPrefix);
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key</i>
     *
     * @param key key
     */
    public void deleteKey(String key) {
        deleteKey(key, true);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key</i>
     *
     * @param key key
     */
    public void deleteKey(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        redisTemplate.delete(key);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys 可传入多个key
     */
    public void deleteKey(String... keys) {
        deleteKey(true, keys);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys 可传入多个key
     */
    public void deleteKey(boolean hasPrefix, String... keys) {
        Set<String> ks;
        if (hasPrefix) {
            ks = Stream.of(keys).map(k -> getFullKey(k)).collect(Collectors.toSet());
        } else {
            ks = Stream.of(keys).collect(Collectors.toSet());
        }
        redisTemplate.delete(ks);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys key集合
     */
    public void deleteKey(Collection<String> keys) {
        deleteKey(keys, true);
    }

    /**
     * 删除key
     *
     * <p>
     * <i>del key1 key2 ...</i>
     *
     * @param keys key集合
     */
    public void deleteKey(Collection<String> keys, boolean hasPrefix) {
        Set<String> ks;
        if (hasPrefix) {
            ks = keys.stream().map(k -> getFullKey(k)).collect(Collectors.toSet());
        } else {
            ks = keys.stream().collect(Collectors.toSet());
        }
        redisTemplate.delete(ks);
    }

    /**
     * 设置key在指定的日期过期
     *
     * <p>
     * <i>expireat key timestamp</i>
     *
     * @param key  key
     * @param date 指定日期
     */
    public void expireKeyAt(String key, Date date) {
        expireKeyAt(key, date, true);
    }

    /**
     * 设置key在指定的日期过期
     *
     * <p>
     * <i>expireat key timestamp</i>
     *
     * @param key  key
     * @param date 指定日期
     */
    public void expireKeyAt(String key, Date date, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        redisTemplate.expireAt(key, date);
    }

    /**
     * 获取key的生命周期
     *
     * @param key
     * @return
     */
    public long getKeyExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 查询key的生命周期
     *
     * <p>
     * <i>ttl key</i>
     *
     * @param key      key
     * @param timeUnit TimeUnit 时间单位
     * @return 指定时间单位的时间数
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        return getExpire(key, timeUnit, true);
    }

    /**
     * 查询key的生命周期
     *
     * <p>
     * <i>ttl key</i>
     *
     * @param key      key
     * @param timeUnit TimeUnit 时间单位
     * @return 指定时间单位的时间数
     */
    public long getExpire(String key, TimeUnit timeUnit, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * key是否已过期
     *
     * @param key
     * @return
     */
    public boolean isExpire(String key) {
        return isExpire(key, true);
    }

    /**
     * key是否已过期
     *
     * @param key
     * @return
     */
    public boolean isExpire(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        return getKeyExpire(key) <= 0;
    }

    /**
     * 根据前缀获取一组keys
     *
     * @param prefix
     * @return
     */
    public Set<String> getKeysByPrefix(String prefix) {
        return getKeysByPrefix(prefix, true);
    }

    /**
     * 根据前缀获取一组keys
     *
     * @param prefix
     * @return
     */
    public Set<String> getKeysByPrefix(String prefix, boolean hasPrefix) {
        String key = getKey(prefix + "*", hasPrefix);
        return redisTemplate.keys(key);
    }

    /**
     * 删除以某些字符串打头的Key
     *
     * @param preKey
     */
    public void deleteByPreKey(String preKey) {
        deleteByPreKey(preKey, true);
    }

    /**
     * key是否已过期
     *
     * @param key
     * @return
     */
    public void deleteByPreKey(String key, boolean hasPrefix) {
        Set<String> keys = getKeysByPrefix(key, hasPrefix);
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 将key设置为永久有效
     *
     * <p>
     * <i>persist key</i>
     *
     * @param key key
     */
    public void persistKey(String key) {
        persistKey(key, true);
    }

    /**
     * 将key设置为永久有效
     *
     * <p>
     * <i>persist key</i>
     *
     * @param key key
     */
    public void persistKey(String key, boolean hasPrefix) {
        key = getKey(key, hasPrefix);
        redisTemplate.persist(key);
    }

    private String getKey(String key, boolean hasPrefix) {
        key = hasPrefix ? getFullKey(key) : key;
        return key;
    }
}
