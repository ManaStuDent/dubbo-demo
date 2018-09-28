package com.inshare.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.inshare.dubbo.common.service.ApiService;
import org.springframework.stereotype.Service;

/**
 * 消费者 Service
 *
 * @author Chengloong
 */
@Service
public class TestService {

    /**
     * 重点 1 使用 @com.alibaba.dubbo.config.annotation.Reference 注入分布式的远程服务对象
     *
     * 重点 3 Reference(url = "localhost:20880") 这样配置可以让该方法直连提供者，而不使用注册中心
     * 配合提供者 spring.dubbo.registry.register=false  提供者不注册到注册中心
     *
     * 重点 7 结果缓存 用于加速热门数据的访问速度
     */
    @Reference(cache = "lru")
    private ApiService apiService;

//    重点 10 如果使用 xml 的方式配置 dubbo 这就只需要注入就可以了
//    @Autowired
//    private ApiService apiService;

    public String show(long id) {
    	// 重点 8 隐式传参
	    // 后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie，用于框架集成，不建议常规业务使用
	    RpcContext.getContext().setAttachment("index", "1");
        return  apiService.show(id);
    }
}
