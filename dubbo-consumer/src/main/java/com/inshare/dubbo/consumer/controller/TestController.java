package com.inshare.dubbo.consumer.controller;

import com.inshare.dubbo.common.entity.ValidationParameter;
import com.inshare.dubbo.consumer.service.TestService;
import com.inshare.dubbo.consumer.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		validationService.save(parameter);
		return "success";
	}

	@GetMapping("/update")
	public String update(String name, int age) {
		ValidationParameter parameter = new ValidationParameter();
		parameter.setName(name);
		parameter.setAge(age);
		validationService.update(parameter);
		return "success";
	}
}
