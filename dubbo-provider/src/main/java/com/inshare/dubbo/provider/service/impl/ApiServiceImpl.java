package com.inshare.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.inshare.dubbo.common.ApiService;
import org.springframework.stereotype.Component;

/**
 * 重点2 使用 @com.alibaba.dubbo.config.annotation.Service 注解， interfaceClass 指定接口
 *
 * @Component 指定这是一个 spring bean
 */
@Service(interfaceClass = ApiService.class, timeout = 5000)
@Component
public class ApiServiceImpl implements ApiService {

    @Override
    public String show(long id) {
        return String.format("TestHandler service %d", id);
    }
}
