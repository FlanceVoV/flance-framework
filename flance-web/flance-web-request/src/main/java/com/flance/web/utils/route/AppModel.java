package com.flance.web.utils.route;

import java.util.List;

/**
 * 外部对接实例
 * @author jhf
 */
public interface AppModel {

    /**
     * 获取app rsa公钥 用于传输加密、加签 (本地系统 -> 第三方app)， 需要app方提供
     * @return  rsa公钥
     */
    String getAppRsaPubKey();

    /**
     * 获取本服务 私钥 用于传输解密、验签（第三方app -> 本地系统）
     * @return  rsa私钥
     */
    String getSysRsaPriKey();

    /**
     * 获取本服务 公钥 需要提供给app
     * @return  rsa公钥
     */
    String getSysRsaPubKey();

    /**
     * 第三方应用名称
     * @return  名称
     */
    String getAppName();

    /**
     * 获取应用唯一编号 非主键
     * @return  app编号
     */
    String getAppId();

    /**
     * 是否启用
     * @return  1.正常 0.失效
     */
    Integer getEnabled();

    /**
     * 获取服务权限
     * @return   返回服务id集合
     */
    List<String> getServerResources();

    /**
     * 获取api接口权限
     * @return   返回接口id集合
     */
    List<String> getApiResources();

}
