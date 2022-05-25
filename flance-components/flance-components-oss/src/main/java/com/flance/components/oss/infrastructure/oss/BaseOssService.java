package com.flance.components.oss.infrastructure.oss;


import java.io.File;

/**
 * oss服务处理接口
 * @author jhf
 */
public interface BaseOssService<T extends BaseOssResponse> {

    T upload(File[] files);

}
