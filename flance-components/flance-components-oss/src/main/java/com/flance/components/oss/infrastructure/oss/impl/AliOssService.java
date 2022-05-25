package com.flance.components.oss.infrastructure.oss.impl;

import com.flance.components.oss.infrastructure.oss.BaseOssService;
import com.flance.components.oss.infrastructure.oss.response.AliOssResponse;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("aliOssService")
public class AliOssService implements BaseOssService<AliOssResponse> {

    @Override
    public AliOssResponse upload(File[] files) {
        return null;
    }
}
