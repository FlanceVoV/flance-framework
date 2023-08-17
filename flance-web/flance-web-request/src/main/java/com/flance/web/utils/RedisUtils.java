package com.flance.web.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis队列工具类
 *
 * @author jhf
 */
@Slf4j
@Component
public class RedisUtils {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    private static final String SETNX_SCRIPT = "return redis.call('setnx',KEYS[1], ARGV[1])";


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
     * 左出
     *
     * @param key 队列key
     * @return 返回值
     */
    public List<String> popLeft(String key, long count) {
        return redisTemplate.opsForList().leftPop(key, count);
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

    public List<String> popRight(String key, long count) {
        return redisTemplate.opsForList().rightPop(key, count);
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

    public Boolean setNx(String key, long time) {
        //自定义脚本
        DefaultRedisScript<List> script = new DefaultRedisScript<>(SETNX_SCRIPT, List.class);
        //执行脚本,传入参数,由于value没啥用,这里随便写死的"1"
        List<Long> rst = redisTemplate.execute(script, Collections.singletonList(key), "1");
        if (null == rst) {
            rst = Lists.newArrayList();
        }
        //返回1,表示设置成功,拿到锁
        if(rst.size() > 0 && rst.get(0) == 1){
            log.info(key+"成功拿到锁");
            //设置过期时间
            setExp(key, time);
            log.info(key+"已成功设置过期时间:"+time +" 秒");
            return true;
        }else{
            Long cacheExp = redisTemplate.getExpire(key);
            long expire = cacheExp == null ? 0 : cacheExp;
            log.info(key+"未拿到到锁,还有"+expire+"释放");
            return false;
        }
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

    /**
     * 达到限流时，则等待，直到新的间隔。
     *
     * @param key
     * @param limitCount
     * @param limitSecond
     */
    public void limitWait(String key, int limitCount, int limitSecond) {
        boolean ok;//放行标志
        do {
            ok = limit(key, limitCount, limitSecond);
            log.info("放行标志={}", ok);
            if (!ok) {
                Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
                if (null != ttl && ttl > 0) {
                    try {
                        Thread.sleep(ttl);
                        log.info("sleeped:{}", ttl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } while (!ok);
    }

    /**
     * 限流方法    true-放行；false-限流
     *
     * @param key
     * @param limitCount
     * @param limitSecond
     * @return
     */
    public boolean limit(String key, int limitCount, int limitSecond) {
        List<String> keys = Collections.singletonList(key);
        String luaScript = buildLuaScript();
        RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
        Number count = redisTemplate.execute(redisScript, keys, limitCount, limitSecond);
        log.info("Access try count is {} for key = {}", count, key);
        if (count != null && count.intValue() <= limitCount) {
            return true;//放行
        } else {
            return false;//限流
        }
    }

    /**
     * 编写 redis Lua 限流脚本
     */
    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 实际调用次数超过阈值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }

}
