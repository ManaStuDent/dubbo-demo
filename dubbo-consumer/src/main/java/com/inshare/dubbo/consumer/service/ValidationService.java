package com.inshare.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.inshare.dubbo.common.entity.ValidationParameter;
import com.inshare.dubbo.common.service.ValidationApiService;
import org.springframework.stereotype.Service;

/**
 * 校验
 *
 * @author Chengloong
 */
@Service
public class ValidationService {

	/**
	 * 重点 6 参数验证
	 * 需要将 validation 开启方可验证该 Service 下方法
	 */
	@Reference(validation = "true")
	private ValidationApiService validationApiService;

	/**
	 * 保存方法
	 *
	 * @param parameter
	 */
	public void save(ValidationParameter parameter){
		validationApiService.save(parameter);
	}

	/**
	 * 更新方法
	 * @param parameter
	 */
	public void update(ValidationParameter parameter){
		validationApiService.update(parameter);
	}
}
