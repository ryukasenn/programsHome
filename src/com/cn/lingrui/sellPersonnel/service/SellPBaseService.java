package com.cn.lingrui.sellPersonnel.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.utils.DBUtils;

public abstract class SellPBaseService extends BServiceLogic{

	private static Logger log = LogManager.getLogger();
	
	@Override
	protected void before() {
		
		dbc = DBUtils.getSELLPERSONNELDBC();
		connection = dbc.getConnection();
	}
}
