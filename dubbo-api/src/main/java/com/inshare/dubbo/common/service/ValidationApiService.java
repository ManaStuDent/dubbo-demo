package com.inshare.dubbo.common.service;

import com.inshare.dubbo.common.entity.ValidationParameter;

import javax.validation.GroupSequence;

/**
 * 参数验证 Service
 *
 * @author Chengloong
 */
public interface ValidationApiService {// 缺省可按服务接口区分验证场景，如：@NotNull(groups = ValidationApiService.class)

	// 重点 6 参数验证
	// 与方法同名接口，首字母大写，用于区分验证场景，如：@NotNull(groups = ValidationApiService.Save.class)，可选
	@GroupSequence(Update.class) // 同时验证Update组规则
	@interface Save{}
	void save(ValidationParameter parameter);

	@interface Update{}
	void update(ValidationParameter parameter);
}
