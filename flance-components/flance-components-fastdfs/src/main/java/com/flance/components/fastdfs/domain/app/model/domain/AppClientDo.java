package com.flance.components.fastdfs.domain.app.model.domain;


import cn.hutool.http.HttpUtil;
import com.flance.web.utils.feign.response.FeignResponse;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * fastdfs组件 服务对象Do
 * @author jhf
 */
@Data
@Slf4j
public class AppClientDo {

    private Long id;

    /** app名称 **/
    private String appName;

    /** app标识，唯一索引 **/
    private String appId;

    /** app服务的文件鉴权url **/
    private String appAuthUrl;

    /** 创建日期 **/
    private Date createDate;

    public boolean hasFilePermission(String token, Long fileId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("token", token);
        params.put("fileId", fileId);
        log.info("[fast_dfs_server]构建文件鉴权请求，appId:[{}]，url:[{}]，token[{}]，fileId[{}]", this.appId, this.appAuthUrl, token, fileId);
        try {
            String resultStr = HttpUtil.post(this.appAuthUrl, params);
            log.info("[fast_dfs_server]响应结果[{}]", resultStr);
            Gson gson = new Gson();
            FeignResponse feignResponse = gson.fromJson(resultStr, FeignResponse.class);
            if (!feignResponse.getSuccess() || null == feignResponse.getData() || !Boolean.parseBoolean(feignResponse.getData().toString())) {
                log.warn("[fast_dfs_server]没有权限，appId:[{}]，token:[{}]，fileId:[{}]", this.appId, token, fileId);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[fast_dfs_server]请求失败，appId:[{}]，url:[{}]，token[{}]，fileId[{}]", this.appId, this.appAuthUrl, token, fileId);
            return false;
        }
        return true;
    }

}
