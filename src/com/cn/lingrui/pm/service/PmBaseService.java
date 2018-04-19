package com.cn.lingrui.pm.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.DBConnect;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.BaseService;
import com.cn.lingrui.common.utils.CommonUtil;

public abstract class PmBaseService extends BServiceLogic{
	private static Logger log = LogManager.getLogger();
	
	@Override
	protected void before() {
		try {

			this.dbc = new DBConnect(CommonUtil.getBasePropertieValue("PM_USERNAME"),
								 CommonUtil.getBasePropertieValue("PM_PASSWORD"),
								 CommonUtil.getBasePropertieValue("PM_IP"),
								 CommonUtil.getBasePropertieValue("PM_DBNAME"));
			this.connection = dbc.getConnection();
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			
		}
	}

	
}
