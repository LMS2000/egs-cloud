package com.lms.sqlfather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatProperties {
    /**
     * 设置微信小程序的appid
     */
    private String appid;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     *前后端约定的字符串，防止别人爆破
     */
    private String state;
}
