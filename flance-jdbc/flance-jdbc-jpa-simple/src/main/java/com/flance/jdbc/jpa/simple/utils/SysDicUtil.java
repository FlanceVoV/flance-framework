package com.flance.jdbc.jpa.simple.utils;

import com.alibaba.fastjson.JSONArray;
import com.flance.jdbc.jpa.simple.components.sys.entity.SysDictionary;
import com.flance.jdbc.jpa.simple.components.sys.service.SysDictionaryService;
import com.flance.web.utils.RedisUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统自动工具类
 * @author jhf
 */
@Component
public class SysDicUtil {

    @Resource
    RedisUtils redisUtils;

    @Resource
    SysDictionaryService sysDictionaryService;

    public SysDictionary getDic(String code) {
        String dic = redisUtils.get("sys:dic");
        if (StringUtils.isEmpty(dic)) {
            throw new RuntimeException("字典加载失败！");
        }
        List<SysDictionary> root = JSONArray.parseArray(dic, SysDictionary.class);
        return getDicLoop(root, code);
    }

    public void refreshDic() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("NULL_parent.id", "null");
        List<SysDictionary> list = sysDictionaryService.findAll(params);
        String dic = JSONArray.toJSONString(list);
        redisUtils.add("sys:dic", dic);
    }

    private SysDictionary getDicLoop(List<SysDictionary> nodes, String code) {
        SysDictionary sysDictionary = null;
        for (SysDictionary item : nodes) {
            if (item.getDicCode().equals(code)) {
                return item;
            }
            if (null == sysDictionary) {
                sysDictionary = getDicLoop(item.getChildren(), code);
            }
        }
        return sysDictionary;
    }

}
