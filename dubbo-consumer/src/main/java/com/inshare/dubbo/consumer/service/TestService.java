package com.inshare.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.inshare.dubbo.common.ApiService;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    /**
     * 重点1 使用 @com.alibaba.dubbo.config.annotation.Reference 注入分布式的远程服务对象
     *
     * 重点3 Reference(url = "localhost:20880") 这样配置可以让该方法直连提供者，而不使用注册中心
     * 配合提供者 spring.dubbo.registry.register=false  提供者不注册到注册中心
     */
    @Reference
    private ApiService apiService;

    public String show(long id) {
        return  apiService.show(id);
    }
}
