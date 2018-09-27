package com.inshare.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.inshare.dubbo.common.entity.ValidationParameter;
import com.inshare.dubbo.common.service.ValidationApiService;
import org.springframework.stereotype.Component;

/**
 * ValidationService 消费者
 *
 * 重点 6 参数验证 validation 也可在此处声明
 * @author Chengloong
 */

@Service(interfaceClass = ValidationApiService.class, timeout = 5000, validation = "true")
@Component
public class ValidationServiceImpl implements ValidationApiService {

	@Override
	public void save(ValidationParameter parameter) {
		System.out.println("save success" + parameter.toString());
	}

	@Override
	public void update(ValidationParameter parameter) {
		System.out.println("update success" + parameter.toString());
	}
}
