package com.inshare.dubbo.consumer.controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.inshare.dubbo.common.entity.ValidationParameter;
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
}
