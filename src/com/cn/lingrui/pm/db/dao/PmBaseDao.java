package com.cn.lingrui.pm.db.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public interface PmBaseDao extends BaseDao{

	/**
	 * 获取登录人员信息
	 * @param loginId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_CURRENTPERSON receiveLoginPerson(String loginId, Connection conn) throws SQLException;
	
	
}
