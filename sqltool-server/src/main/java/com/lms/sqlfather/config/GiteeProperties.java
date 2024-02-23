package com.lms.sqlfather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitee")
@Data
public class GiteeProperties {

    private String clientId;
    private String clientSecret;

    private String callBack;

    private String userPrefix;

    private String tokenUrl;

    private String userUrl;

    private String giteeState;
}
