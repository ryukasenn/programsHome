package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;

public interface UserManageDao extends BaseDao{

	public List<NBPT_RSFZ_USER> getUserList(Connection conn);
	
	public void postAddUser(Connection conn, NBPT_RSFZ_USER user);

	public void postAddU_R(Connection connection, NBPT_RSFZ_U_R rsfz_U_R);

	public List<NBPT_RSFZ_USER> getUser(Connection conn, String userId) throws SQLException;
}
