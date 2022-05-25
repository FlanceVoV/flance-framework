package com.flance.components.oss.domain.oss.entity;

import com.flance.jdbc.mybatis.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OssServer extends BaseEntity<String> {

    /**
     * oss服务提供商名称
     */
    private String ossName;

    /**
     * oss服务提供商编码
     */
    private String ossCode;

    /**
     * oss服务bean
     **/
    private String ossServiceBean;

    /**
     * oss临时文件存储路径
     */
    private String ossTmpDisk;

    private String bucketName;

    private String accessKeyId;

    private String accessKeySecret;

    private String baseUrl;

    private String durationSeconds;


}
