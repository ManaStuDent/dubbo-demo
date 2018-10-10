package com.inshare.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.inshare.dubbo.common.service.ApiService;
import org.springframework.stereotype.Component;

/**
 * 生产者 Service
 * 重点 2 使用 @com.alibaba.dubbo.config.annotation.Service 注解， interfaceClass 指定接口
 *
 * @Component 指定这是一个 spring bean
 * @author Chengloong
 */
@Service(interfaceClass = ApiService.class, timeout = 5000, owner = "Chengloong")
@Component
public class ApiServiceImpl implements ApiService {

    @Override
    public String show(long id) {
        System.out.println("provider show 被调用");

        // 重点 8 隐式传参
	    // 获取客户端隐式传入的参数，用于框架集成，不建议常规业务使用
	    String index = RpcContext.getContext().getAttachment("index");
	    System.out.println("隐式传参 index = " + index);
        return String.format("TestHandler service %d", id);
    }
}
