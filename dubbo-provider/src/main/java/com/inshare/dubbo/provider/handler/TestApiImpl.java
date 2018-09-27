package com.inshare.dubbo.provider.handler;

import com.alibaba.dubbo.config.annotation.Service;
import com.inshare.dubbo.common.TestApi;
import org.springframework.stereotype.Component;

/**
 * 重点2 使用 @com.alibaba.dubbo.config.annotation.Service 注解， interfaceClass 指定接口
 *
 * @Component 指定这是一个 spring bean
 */
@Service(interfaceClass = TestApi.class)
@Component
public class TestApiImpl implements TestApi {

    @Override
    public String show(long id) {
        return String.format("TestHandler service %d", id);
    }
}
