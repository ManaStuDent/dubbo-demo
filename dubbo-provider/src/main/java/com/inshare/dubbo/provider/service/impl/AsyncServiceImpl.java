package com.inshare.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.inshare.dubbo.common.service.AsyncApiService;
import org.springframework.stereotype.Component;

/**
 * Async Service 实现
 *
 * @author Chengloong
 */
@Service(timeout = 5000)
@Component
public class AsyncServiceImpl implements AsyncApiService{

	@Override
	public void findFoo() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("findFoo 被调用");
	}

	@Override
	public int findBar() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("findBar 被调用");
		return 2;
	}
}
