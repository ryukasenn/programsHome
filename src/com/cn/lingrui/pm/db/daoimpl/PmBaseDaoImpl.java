package com.cn.lingrui.pm.db.daoimpl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.pm.db.dao.PmBaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public class PmBaseDaoImpl extends BaseDaoImpl implements PmBaseDao{
	
	private static Logger log = LogManager.getLogger();
	@Override
	public NBPT_VIEW_CURRENTPERSON receiveLoginPerson(String loginId, Connection conn) throws SQLException {
		String sql ="SELECT * "
				  + "FROM NBPT_VIEW_CURRENTPERSON A "
				  + "WHERE "
				  + "A.NBPT_SP_PERSON_LOGINID = '" + loginId + "'";
	
		try {
			
			NBPT_VIEW_CURRENTPERSON lgoinPerson = this.oneQueryForClasz(sql, conn, NBPT_VIEW_CURRENTPERSON.class);
			
			return lgoinPerson;
			
		} catch (SQLException e) {
			
			log.error("查询当前登录用户出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

}
