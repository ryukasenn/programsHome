package com.cn.lingrui.sellPersonnel.service;

import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.utils.DBUtils;

public abstract class SellPBaseService extends BServiceLogic{
	
	@Override
	protected void before() {
		
		dbc = DBUtils.getSELLPERSONNELDBC();
		connection = dbc.getConnection();
	}
	

}
