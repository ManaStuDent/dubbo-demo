package com.inshare.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.inshare.dubbo.common.TestApi;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    /**
     * 重点1 使用 @com.alibaba.dubbo.config.annotation.Reference 注入分布式的远程服务对象
     */
    @Reference
    private TestApi testApi;

    public String show(long id) {
        return  testApi.show(id);
    }
}
