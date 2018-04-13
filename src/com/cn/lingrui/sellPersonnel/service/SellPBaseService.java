package com.cn.lingrui.sellPersonnel.service;

import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.utils.DBUtils;

public abstract class SellPBaseService extends BServiceLogic{
	
	@Override
	protected void before() {
		
		dbc = DBUtils.getSELLPERSONNELDBC();
		connection = dbc.getConnection();
	}
	
	/**
	 * 获取登录人员的id
	 * @return
	 */
	protected String getLoginId() {
		
		return this.getRequest().getAttribute("userID").toString();
	}
}
