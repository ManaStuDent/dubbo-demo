package com.inshare.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.inshare.dubbo.common.service.ApiService;
import org.springframework.stereotype.Component;

/**
 * 生产者 Service
 * 重点 2 使用 @com.alibaba.dubbo.config.annotation.Service 注解， interfaceClass 指定接口
 *
 * @Component 指定这是一个 spring bean
 * @author Chengloong
 */
@Service(interfaceClass = ApiService.class, timeout = 5000)
@Component
public class ApiServiceImpl implements ApiService {

    @Override
    public String show(long id) {
        System.out.println("provider show 被调用");
        return String.format("TestHandler service %d", id);
    }
}
