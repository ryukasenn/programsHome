package com.cn.lingrui.rsfz.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.utils.DBUtils;

public abstract class RSFZBaseService extends BServiceLogic{

	private static Logger log = LogManager.getLogger();
	
	/**
	 * init方法用于初始化数据库连接,如果需要连接数据库,则在方法执行前执行此方法
	 */
	@Override
	protected void before() {
		
		dbc = DBUtils.getRSFZDBC();
		connection = dbc.getConnection();
	}
}
