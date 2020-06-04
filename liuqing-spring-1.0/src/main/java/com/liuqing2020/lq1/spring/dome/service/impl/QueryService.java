package com.liuqing2020.lq1.spring.dome.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.liuqing2020.lq1.spring.dome.service.IQueryService;
import com.liuqing2020.lq1.spring.formework.annotation.LQService;
import lombok.extern.slf4j.Slf4j;

/**
 * 查询业务
 *
 */
@LQService
@Slf4j
public class QueryService implements IQueryService {

	/**
	 * 查询
	 */
	public String query(String name) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
		log.info("这是在业务方法中打印的：" + json);
		return json;
	}

}
