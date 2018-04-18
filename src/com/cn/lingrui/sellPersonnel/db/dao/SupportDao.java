package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public interface SupportDao extends SellPersonnelBaseDao{

	/**
	 * 获取所有已登记人员信息,除信息专员外
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveAllPersons(Connection conn) throws SQLException;
}
