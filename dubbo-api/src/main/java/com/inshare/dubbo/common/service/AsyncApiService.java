package com.inshare.dubbo.common.service;

/**
 * 测试异步调用 Service
 *
 * @author Chengloong
 */
public interface AsyncApiService {

	/**
	 * 测试没有返回值
	 */
	void findFoo();

	/**
	 * 测试有返回值
	 */
	int findBar();
}