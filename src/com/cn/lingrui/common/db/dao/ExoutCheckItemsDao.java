package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.PMBASE;

public interface ExoutCheckItemsDao extends BaseDao{

	public List<PMBASE> getCheckItems(String procName, Connection conn) throws SQLException;
	
}
