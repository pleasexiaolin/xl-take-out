package com.xiaolin.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
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
     * 默认过期时长，单位：秒
     */
    @Value("${redis.defaultExpire:86400}")
    public long defaultExpire;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 存入数据
     *
     * @param key   键
     * @param value 值
     */
    public void setValue(String key, Object value) {
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString());
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 存入数据并设置默认过期时间
     *
     * @param key   键
     * @param value 值
     */
    public void setValueAndExpire(String key, Object value) {
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString(), defaultExpire, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, defaultExpire, TimeUnit.SECONDS);
        }
    }

    /**
     * 存入数据并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间(秒)
     */
    public void setValueAndExpire(String key, Object value, long time) {
        if (value instanceof String) {
            redisTemplate.opsForValue().set(key, value.toString(), time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key获取数据
     *
     * @param key 键
     * @return 值
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key获取数据并转换为指定类型
     *
     * @param key   键
     * @param clazz 类型Class
     * @param <T>   泛型
     * @return 指定类型的值
     */
    @SuppressWarnings("unchecked")
    public <T> T getValueAs(String key, Class<T> clazz) {
        Object value = getValue(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        throw new ClassCastException("Cannot cast " + value.getClass() + " to " + clazz);
    }

    /**
     * 批量获取值
     *
     * @param keys 键集合
     * @return 值列表
     */
    public List<Object> getValues(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean existsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 判断key存储的值类型
     *
     * @param key 键
     * @return 数据类型
     */
    public DataType typeKey(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 重命名key
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     * @return 修改成功返回true
     */
    public boolean renameKeyNx(String oldKey, String newKey) {
        return Boolean.TRUE.equals(redisTemplate.renameIfAbsent(oldKey, newKey));
    }

    /**
     * 删除key
     *
     * @param key 键
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 清理缓存数据
     *
     * @param pattern
     */
    public void cleanCache(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除多个key
     *
     * @param keys 可变参数键列表
     */
    public void deleteKey(String... keys) {
        redisTemplate.delete(Stream.of(keys).collect(Collectors.toSet()));
    }

    /**
     * 删除key集合
     *
     * @param keys 键集合
     */
    public void deleteKey(Collection<String> keys) {
        redisTemplate.delete(new HashSet<>(keys));
    }

    /**
     * 设置key在指定的日期过期
     *
     * @param key  键
     * @param date 指定日期
     */
    public void expireKeyAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 设置key的过期时间
     *
     * @param key  键
     * @param time 时间
     * @param unit 时间单位
     * @return 设置成功返回true
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, unit));
    }

    /**
     * 获取key的生命周期(秒)
     *
     * @param key 键
     * @return 过期时间(秒) -2表示key不存在或已过期
     */
    public long getKeyExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? -2L : expire;
    }

    /**
     * 查询key的生命周期
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 指定时间单位的时间数 -2表示key不存在或已过期
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        Long expire = redisTemplate.getExpire(key, timeUnit);
        return expire == null ? -2L : expire;
    }

    /**
     * key是否已过期
     *
     * @param key 键
     * @return 是否过期
     */
    public boolean isExpire(String key) {
        return getKeyExpire(key) <= 0;
    }

    /**
     * 根据前缀获取一组keys
     *
     * @param prefix 前缀
     * @return 键集合
     */
    public Set<String> getKeysByPrefix(String prefix) {
        return redisTemplate.keys(prefix + "*");
    }

    /**
     * 根据前缀删除一组keys
     *
     * @param prefix 前缀
     * @return 键集合
     */
    public void deleteKeysByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    // ======================== Hash操作 ========================

    /**
     * HashPut
     *
     * @param key     键
     * @param hashKey Hash键
     * @param value   值
     */
    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * HashGet
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 值
     */
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取整个Hash
     *
     * @param key 键
     * @return Hash映射
     */
    public Map<Object, Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除Hash中的属性
     *
     * @param key      键
     * @param hashKeys Hash键数组
     * @return 删除个数
     */
    public Long hashDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断Hash中是否有该属性
     *
     * @param key     键
     * @param hashKey Hash键
     * @return 是否存在
     */
    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    // ======================== List操作 ========================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0到-1代表所有值
     * @return 值列表
     */
    public List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key 键
     * @return 值列表
     */
    public List<Object> listGetAll(String key) {
        return listGet(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long listGetSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 往list中放入数据
     *
     * @param key   键
     * @param value 值
     * @return 插入后的长度
     */
    public Long listPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 往list中放入多个数据
     *
     * @param key   键
     * @param value 值集合
     * @return 插入后的长度
     */
    public Long listPushAll(String key, Collection<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public void listSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value的元素
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long listRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    // ======================== Set操作 ========================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return 值集合
     */
    public Set<Object> setGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return 是否存在
     */
    public boolean setHasKey(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long setAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的元素
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    // ======================== ZSet操作 ========================

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    public Boolean zSetAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取zset中某个元素的分数
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    public Double zSetScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取zset中某个元素的排名（从小到大）
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    public Long zSetRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取zset中某个元素的排名（从大到小）
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    public Long zSetReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取zset指定范围的值（按score从小到大）
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 值集合
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取zset指定范围的值（按score从大到小）
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 值集合
     */
    public Set<Object> zSetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取zset指定分数范围的值
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 值集合
     */
    public Set<Object> zSetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取zset大小
     *
     * @param key 键
     * @return 大小
     */
    public Long zSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 移除zset中的值
     *
     * @param key    键
     * @param values 值
     * @return 移除个数
     */
    public Long zSetRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值
     *
     * @param key   键
     * @param value 值
     * @param delta 增加的分数
     * @return 增加后的分数
     */
    public Double zSetIncrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }
}
