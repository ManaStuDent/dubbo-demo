package com.inshare.dubbo.consumer.controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.inshare.dubbo.common.entity.ValidationParameter;
import com.inshare.dubbo.consumer.service.AsyncService;
import com.inshare.dubbo.consumer.service.TestService;
import com.inshare.dubbo.consumer.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 测试 Controller
 *
 * @author Chengloong
 */
@RequestMapping("/dubbo")
@RestController
public class TestController {

	@Autowired
	private TestService testService;
	@Autowired
	private ValidationService validationService;
	@Autowired
	private AsyncService asyncService;

	@GetMapping("/show")
	public String show(@RequestParam("id") long id) {
		return testService.show(id);
	}

	@GetMapping("/save")
	public String save(String name, int age) {
		ValidationParameter parameter = new ValidationParameter();
		parameter.setName(name);
		parameter.setAge(age);

		try {
			validationService.save(parameter);
		} catch (RpcException e) {
			// 重点 6 参数验证
			// 里面嵌了一个ConstraintViolationException
			ConstraintViolationException ve = (ConstraintViolationException) e.getCause();
			// 可以拿到一个验证错误详细信息的集合
			Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
			System.out.println(violations);

			return "参数不正确";
		}

		return "success";
	}

	@GetMapping("/update")
	public String update(String name, int age) {
		ValidationParameter parameter = new ValidationParameter();
		parameter.setName(name);
		parameter.setAge(age);

		try {
			validationService.update(parameter);
		} catch (RpcException e) {
			e.printStackTrace();
			return "参数不正确";
		}

		return "success";
	}

	@GetMapping("/async")
	public String async() throws ExecutionException, InterruptedException {

		// 重点 9 异步调用
		// 正常调用，不使用 dubbo async
//		asyncService.findFoo();
//		int bar = asyncService.findBar();
//		System.out.println("bar = " + bar);

		// 异步调用，使用 dubbo async get()的时候直接拿到返回值，否则线程wait住，等待 bar 返回后，线程会被notify唤醒
//		asyncService.findFoo();
//		asyncService.findBar();
//		Future<Integer> barFuture = RpcContext.getContext().getFuture();
//		System.out.println("async bar = " + barFuture.get());

		// 使用 JDK 8 CompletableFuture 的方式异步
		CompletableFuture.runAsync(asyncService::findFoo);
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(asyncService::findBar);
		System.out.println("async bar = " + future.get());

		return "success";
	}
}
