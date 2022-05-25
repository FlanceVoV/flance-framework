package com.flance.components.oss.infrastructure.oss.impl;

import com.flance.components.oss.infrastructure.oss.BaseOssService;
import com.flance.components.oss.infrastructure.oss.response.TencentOssResponse;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("tencentOssService")
public class TencentOssService implements BaseOssService<TencentOssResponse> {

    @Override
    public TencentOssResponse upload(File[] files) {
        return null;
    }

}
