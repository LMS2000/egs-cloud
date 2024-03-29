package com.lms.sqlfather;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @author lms2000
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@MapperScan("com.lms.sqlfather.mapper")
@EnableScheduling
@EnableDiscoveryClient
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}
