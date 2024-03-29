package com.flance.web.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis队列工具类
 *
 * @author jhf
 */
@Component
public class RedisUtils {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 左进
     *
     * @param key   队列key
     * @param value 队列值
     * @return 返回值
     */
    public Long putLeft(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 左出
     *
     * @param key 队列key
     * @return 返回值
     */
    public String popLeft(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 左进
     *
     * @param key   队列key
     * @param value 队列值
     * @return 返回值
     */
    public Long putRight(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 左出
     *
     * @param key 队列key
     * @return 返回值
     */
    public String popRight(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取长度
     *
     * @param key 队列key
     * @return 队列长度
     */
    public Long getLengthList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取长度
     *
     * @param key 队列key
     * @return 队列长度
     */
    public Long getLengthSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * set保存
     *
     * @param key   key
     * @param value 值
     */
    public void putSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 取出对象
     *
     * @param key key
     * @return 值
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 存放对象
     *
     * @param key
     * @param value
     * @return
     */
    public void add(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 存放对象
     *
     * @param key
     * @param value
     * @return
     */
    public void add(String key, String value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * table put
     *
     * @param mkey
     * @param ckey
     * @param value
     */
    public void putTable(String mkey, String ckey, String value) {
        redisTemplate.opsForHash().put(mkey, ckey, value);
    }

    /**
     * table get
     *
     * @param mkey
     * @param ckey
     * @return
     */
    public String getTable(String mkey, String ckey) {
        return (String) redisTemplate.opsForHash().get(mkey, ckey);
    }

    /**
     * table del
     *
     * @param mkey
     * @param ckey
     */
    public Long delTable(String mkey, String ckey) {
        return redisTemplate.opsForHash().delete(mkey, ckey);
    }

    /**
     * table length
     *
     * @param mkey
     * @return
     */
    public Long getLengthTable(String mkey) {
        return redisTemplate.opsForHash().size(mkey);
    }

    /**
     * 获取对象集合长度
     *
     * @param key key
     * @return 长处
     */
    public Long getLengthObject(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 删除key下所有数据
     *
     * @param key key
     * @return 删除成功与否
     */
    public Boolean clear(String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (null == keys) {
            return redisTemplate.delete(key);
        }
        return redisTemplate.delete(keys) > 0;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取所有子key
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        Set<String> keys = redisTemplate.keys(key);
        return null == keys ? new HashSet<>() : keys;
    }

    /**
     * 重置过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public Boolean setExp(String key, Long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 分布式锁
     *
     */
    public boolean lock(String key, long timeout, TimeUnit unit, Boolean wait) {
        long time = System.nanoTime();
        long end_time = time + unit.toNanos(timeout);
        try {
            while (System.nanoTime() < end_time) {
                if (redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(end_time), timeout, unit)) {
                    return true;
                } else {
                    String lock_expired_time = redisTemplate.opsForValue().get(key);
                    if (null != lock_expired_time && !"".equals(lock_expired_time) && time > Long.parseLong(lock_expired_time)) {
                        redisTemplate.expire(key, timeout, unit);
                        return true;
                    }

                    if (!wait) {  //不等待直接返回
                        return false;
                    }
                }

                //加随机时间防止活锁
                Thread.sleep(100 + new Random().nextInt(20));
            }
        } catch (Exception e) {
            unlock(key);
        }
        return false;
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }

}
