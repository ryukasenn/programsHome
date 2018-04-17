package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.LoginDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.login.CurrentUser;
import com.cn.lingrui.common.pojos.login.LoginPojoIn;

@Repository("loginDao")
public class LoginDaoImpl extends BaseDaoImpl implements LoginDao{

	private static Logger log = LogManager.getLogger();
	
	@Override
	public List<NBPT_RSFZ_USER> checkUser(LoginPojoIn in, Connection conn) throws SQLException {

		List<NBPT_RSFZ_USER> resultList = this.queryForClaszs("SELECT * FROM NBPT_RSFZ_USER WHERE NBPT_RSFZ_USER_ID = '" 
												+ in.getUserId() 
												+ "' AND NBPT_RSFZ_USER_PASSWORD = '" 
												+ in.getPassword() 
												+ "'", conn, NBPT_RSFZ_USER.class);
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_U_R> getRole(LoginPojoIn in, Connection conn) throws SQLException {

		List<NBPT_RSFZ_U_R> resultList = this.queryForClaszs("SELECT * FROM NBPT_RSFZ_U_R WHERE NBPT_RSFZ_U_R_UID = '" + in.getUserId() + "'", conn, NBPT_RSFZ_U_R.class);
		
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_USER> otherLogin(String username, Connection conn) throws SQLException {
		
		List<NBPT_RSFZ_USER> resultList = this.queryForClaszs("SELECT * FROM NBPT_RSFZ_USER WHERE NBPT_RSFZ_USER_ID = '" 
				+ username + "'", conn, NBPT_RSFZ_USER.class);
		return resultList;
	}


	@Override
	public List<NBPT_RSFZ_U_R> getRole(String userId, Connection conn) throws SQLException {

		List<NBPT_RSFZ_U_R> resultList = this.queryForClaszs("SELECT * FROM NBPT_RSFZ_U_R WHERE NBPT_RSFZ_U_R_UID = '" + userId + "'", conn, NBPT_RSFZ_U_R.class);
		
		return resultList;
	}

	@Override
	public CurrentUser getUserInfo(LoginPojoIn in, Connection connection) throws SQLException {

		try {
			CurrentUser userinfo = null;
			
			String sql =  "SELECT * FROM "
						+ "NBPT_RSFZ_USER A "
						+ "LEFT JOIN NBPT_RSFZ_U_R B "
						+ "ON A.NBPT_RSFZ_USER_ID = B.NBPT_RSFZ_U_R_UID "
						+ "WHERE A.NBPT_RSFZ_USER_ID = '" + in.getUserId() + "' ";
			// 如果登录模式为1
			if("1".equals(in.getLoginModel())) {

				userinfo = this.oneQueryForClasz(sql, connection, CurrentUser.class);
			} 
			
			// 正常登录
			else {
				
				sql += "AND A.NBPT_RSFZ_USER_PASSWORD = '" + in.getPassword() + "'";
				userinfo = this.oneQueryForClasz(sql, connection, CurrentUser.class);
			}
			
			return userinfo;
		} catch (SQLException e) {
			
			log.error("查询系统用户出错" + e.getMessage());
			throw new SQLException();
		}
	}
	
}

























