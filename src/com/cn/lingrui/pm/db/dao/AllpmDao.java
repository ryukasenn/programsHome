package com.cn.lingrui.pm.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.pm.pojos.PmInfo;

public interface AllpmDao extends PmBaseDao {
	public List<PmInfo> getAllPm(Connection connection) throws SQLException;
}
