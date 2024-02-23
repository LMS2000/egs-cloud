package com.lms.sqlfather.config;

import com.lms.lmscommon.common.AppLogLevel;
import io.netty.handler.logging.LogLevel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lms2000
 */
@ConfigurationProperties("app.log")
@Data
public class LogProperties {
    private Boolean enabled = true;
    private Boolean errorLog = true;
    private AppLogLevel level;
}
