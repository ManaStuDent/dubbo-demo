package com.inshare.dubbo.common.entity;

import com.inshare.dubbo.common.service.ValidationApiService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 参数验证 实体
 *
 * @author Chengloong
 */
public class ValidationParameter implements Serializable {

	private static final long serialVersionUID = 7158911668568000392L;

	@NotNull(groups = ValidationApiService.Update.class) // 不允许为空
	@Size(min = 1, max = 20, groups = ValidationApiService.Update.class) // 长度或大小范围
	private String name;

	@Min(value = 0, groups = ValidationApiService.Save.class)
	@Max(value = 100, groups = ValidationApiService.Save.class)
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "ValidationParameter{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
