package com.flance.web.utils.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "flance.response.advice")
public class GlobalResponseBodyAdviceConfig {

    private List<String> ignoreUrls;

    private boolean enable;

}
