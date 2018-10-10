package com.inshare.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.inshare.dubbo.common.service.AsyncApiService;
import org.springframework.stereotype.Service;

/**
 * Async Service
 *
 * @author Chengloong
 */
@Service
public class AsyncService {

	// 重点 9 异步调用
	// 使用注解的方式没有办法定义到 method 级别的异步
	// @Reference(async = true)
	@Reference(owner = "Chengloong")
	private AsyncApiService asyncApiService;

	public void findFoo() {
		asyncApiService.findFoo();
	}

	public int findBar() {
		return asyncApiService.findBar();
	}
}
