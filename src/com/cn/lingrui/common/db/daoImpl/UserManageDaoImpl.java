package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.UserManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.utils.DBUtils;

@Repository("userManageDao")
public class UserManageDaoImpl extends BaseDaoImpl implements UserManageDao{

	private static Logger log = LogManager.getLogger();
	
	@Override
	public List<NBPT_RSFZ_USER> getUserList(Connection conn) {
		
		// 初始化返回结果
		List<NBPT_RSFZ_USER> resultList = new ArrayList<NBPT_RSFZ_USER>();
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_USER.class, "SELECT", "NBPT_RSFZ_USER");
		if(!"".equals(sql)) {

			try {
				resultList = this.query(sql + " where NBPT_RSFZ_USER_ID > '10000' order by NBPT_RSFZ_USER.NBPT_RSFZ_USER_ID ASC", conn, NBPT_RSFZ_USER.class);
			} catch (Exception e) {

				log.error("查询所有用户出错" + e.getMessage());
			}
		}
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_USER> getUser(Connection conn, String userId) throws SQLException {

		// 初始化返回结果
		List<NBPT_RSFZ_USER> resultList = new ArrayList<NBPT_RSFZ_USER>();
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_USER.class, "select", "NBPT_RSFZ_USER");

		try {
			resultList = this.query(sql + " where NBPT_RSFZ_USER_ID = '" + userId + "'", conn, NBPT_RSFZ_USER.class);
			return resultList;
		} catch (Exception e) {

			log.error("查询用户出错" + e.getMessage());
			throw new SQLException();
		}
		
	}

	@Override
	public void postAddUser(Connection conn, NBPT_RSFZ_USER user) {


		String sql = DBUtils.beanToSql(NBPT_RSFZ_USER.class, "insert", "NBPT_RSFZ_USER", user);
		
		if(!"".equals(sql)) {

			try {
				
				this.excuteUpdate(sql , conn);
			} catch (Exception e) {

				log.error("插入新用户出错" + e.getMessage());
			}
		}
	}

	@Override
	public void postAddU_R(Connection conn, NBPT_RSFZ_U_R rsfz_U_R) {
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_U_R.class, "insert", "NBPT_RSFZ_U_R", rsfz_U_R);

		if(!"".equals(sql)) {

			try {
				
				this.excuteUpdate(sql , conn);
			} catch (Exception e) {

				log.error("插入新用户角色出错" + e.getMessage());
			}
		}
	}

}
