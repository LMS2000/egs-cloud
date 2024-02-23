package com.lms.lmsclient;



import com.lms.lmsclient.client.SqlToolApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sqltool.client")
@Data
@ComponentScan
public class SqlToolApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public SqlToolApiClient sqlToolApiClient() {
        return new SqlToolApiClient(accessKey, secretKey);
    }

}
