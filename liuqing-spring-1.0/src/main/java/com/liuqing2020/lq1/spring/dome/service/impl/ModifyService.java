package com.liuqing2020.lq1.spring.dome.service.impl;

import com.liuqing2020.lq1.spring.dome.service.IModifyService;
import com.liuqing2020.lq1.spring.formework.annotation.LQService;

/**
 * 增删改业务
 *
 */
@LQService
public class ModifyService implements IModifyService {

	/**
	 * 增加
	 */
	public String add(String name,String addr) throws Exception{
		throw new RuntimeException("这是故意抛出异常");
		// return "modifyService add,name=" + name + ",addr=" + addr;
	}

	/**
	 * 修改
	 */
	public String edit(Integer id,String name) {
		return "modifyService edit,id=" + id + ",name=" + name;
	}

	/**
	 * 删除
	 */
	public String remove(Integer id) {
		return "modifyService id=" + id;
	}
	
}
