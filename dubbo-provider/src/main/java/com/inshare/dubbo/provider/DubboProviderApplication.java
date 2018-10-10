package com.inshare.dubbo.provider;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者启动类
 * 重点 11 使用 java config 的方式配置 dubbo
 * 在使用 java config 的方式配置时 不能使用 EnableDubboConfiguration 注解
 * 使用 DubboComponentScan 指定 serverImpl 包路对 @Service 进行扫描
 * @author Chengloong
 */
@SpringBootApplication
//@EnableDubboConfiguration
@DubboComponentScan(basePackages = "com.inshare.dubbo.provider.service.impl")
public class DubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }
}
