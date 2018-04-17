package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.AuthorityDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;


@Repository("authorityDao")
public class AuthorityDaoImpl extends BaseDaoImpl implements AuthorityDao{

	private static Logger log = LogManager.getLogger();
	
	public List<NBPT_RSFZ_R_P> getAuthorityList(Connection conn){
		
		// 初始化返回结果
		List<NBPT_RSFZ_R_P> resultList = new ArrayList<NBPT_RSFZ_R_P>();
		
		String sql = 
				"SELECT "
				+ "NBPT_RSFZ_R_P_RID,"
				+ "NBPT_RSFZ_R_P_PID,"
				+ "NBPT_RSFZ_PAGE_TYPE as NBPT_RSFZ_R_P_PTYPE "
				+ "FROM NBPT_RSFZ_R_P "
				+ "LEFT JOIN NBPT_RSFZ_PAGE "
				+ "ON NBPT_RSFZ_R_P.NBPT_RSFZ_R_P_PID = NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_ID "
				+ "ORDER BY NBPT_RSFZ_R_P.NBPT_RSFZ_R_P_PID ASC";
		
		try {
			
			resultList = this.queryForClaszs(sql, conn, NBPT_RSFZ_R_P.class);
		} catch (Exception e) {

			log.info("查询错误" + e.getMessage());
		}
		
		return resultList;
	}
}
