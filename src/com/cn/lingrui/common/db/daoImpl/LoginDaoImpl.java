package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.LoginDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.login.LoginPojoIn;

@Repository("loginDao")
public class LoginDaoImpl extends BaseDaoImpl implements LoginDao{

	@Override
	public List<NBPT_RSFZ_USER> checkUser(LoginPojoIn in, Connection conn) throws SQLException {

		List<NBPT_RSFZ_USER> resultList = this.query("SELECT * FROM NBPT_RSFZ_USER WHERE NBPT_RSFZ_USER_ID = '" 
												+ in.getUserId() 
												+ "' AND NBPT_RSFZ_USER_PASSWORD = '" 
												+ in.getPassword() 
												+ "'", conn, NBPT_RSFZ_USER.class);
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_U_R> getRole(LoginPojoIn in, Connection conn) throws SQLException {

		List<NBPT_RSFZ_U_R> resultList = this.query("SELECT * FROM NBPT_RSFZ_U_R WHERE NBPT_RSFZ_U_R_UID = '" + in.getUserId() + "'", conn, NBPT_RSFZ_U_R.class);
		
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_USER> otherLogin(String username, Connection conn) throws SQLException {
		
		List<NBPT_RSFZ_USER> resultList = this.query("SELECT * FROM NBPT_RSFZ_USER WHERE NBPT_RSFZ_USER_ID = '" 
				+ username + "'", conn, NBPT_RSFZ_USER.class);
		return resultList;
	}


	@Override
	public List<NBPT_RSFZ_U_R> getRole(String userId, Connection conn) throws SQLException {

		List<NBPT_RSFZ_U_R> resultList = this.query("SELECT * FROM NBPT_RSFZ_U_R WHERE NBPT_RSFZ_U_R_UID = '" + userId + "'", conn, NBPT_RSFZ_U_R.class);
		
		return resultList;
	}
	
}
