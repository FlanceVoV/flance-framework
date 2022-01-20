package com.flance.web.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis队列工具类
 * @author jhf
 */
@Component
public class RedisUtils {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 左进
     * @param key       队列key
     * @param value     队列值
     * @return          返回值
     */
    public Long putLeft(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 左出
     * @param key       队列key
     * @return          返回值
     */
    public String popLeft(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 左进
     * @param key       队列key
     * @param value     队列值
     * @return          返回值
     */
    public Long putRight(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 左出
     * @param key       队列key
     * @return          返回值
     */
    public String popRight(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取长度
     * @param key       队列key
     * @return          队列长度
     */
    public Long getLengthList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取长度
     * @param key       队列key
     * @return          队列长度
     */
    public Long getLengthSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * set保存
     * @param key key
     * @param value   值
     */
    public void putSet(String key,String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 取出对象
     * @param key       key
     * @return          值
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 存放对象
     * @param key
     * @param value
     * @return
     */
    public void add(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 存放对象
     * @param key
     * @param value
     * @return
     */
    public void add(String key, String value, Long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * table put
     * @param mkey
     * @param ckey
     * @param value
     */
    public void putTable(String mkey, String ckey, String value) {
        redisTemplate.opsForHash().put(mkey, ckey, value);
    }

    /**
     * table get
     * @param mkey
     * @param ckey
     * @return
     */
    public String getTable(String mkey, String ckey) {
        return (String) redisTemplate.opsForHash().get(mkey, ckey);
    }

    /**
     * table del
     * @param mkey
     * @param ckey
     */
    public Long delTable(String mkey, String ckey) {
        return redisTemplate.opsForHash().delete(mkey, ckey);
    }

    /**
     * table length
     * @param mkey
     * @return
     */
    public Long getLengthTable(String mkey) {
        return redisTemplate.opsForHash().size(mkey);
    }

    /**
     * 获取对象集合长度
     * @param key   key
     * @return      长处
     */
    public Long getLengthObject(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 删除key下所有数据
     * @param key       key
     * @return          删除成功与否
     */
    public Boolean clear(String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (null == keys) {
            return redisTemplate.delete(key);
        }
        return redisTemplate.delete(keys) > 0;
    }

}
