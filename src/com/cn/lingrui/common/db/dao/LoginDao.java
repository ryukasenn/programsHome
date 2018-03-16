package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.login.LoginPojoIn;

public interface LoginDao extends BaseDao{

	public List<NBPT_RSFZ_USER> checkUser(LoginPojoIn in, Connection conn) throws SQLException;

	public List<NBPT_RSFZ_U_R> getRole(LoginPojoIn in, Connection connection) throws SQLException;
	
	public List<NBPT_RSFZ_USER> otherLogin(String username, Connection connection) throws SQLException;

	public List<NBPT_RSFZ_U_R> getRole(String userId, Connection conn) throws SQLException;

}
