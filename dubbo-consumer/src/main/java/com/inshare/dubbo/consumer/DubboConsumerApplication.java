package com.inshare.dubbo.consumer;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 消费者启动类
 *
 * @author Chengloong
 */
@SpringBootApplication
@EnableDubboConfiguration
// 重点 10 springboot 使用 xml 配置文件配置 dubbo
// 指定配置文件
//@ImportResource({"classpath:dubbo-consume.xml"})
public class DubboConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboConsumerApplication.class, args);
	}
}
