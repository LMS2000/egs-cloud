package com.lms.sqlfather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * @author lms2000
 */
@Data
@Primary
@ConfigurationProperties(prefix = "app.amqp")
public class MyMQProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
