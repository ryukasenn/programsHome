package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;

@Repository("personManageDao")
public class PersonManageDaoImpl extends SellPersonnelBaseDaoImpl implements PersonManageDao{

	private static Logger log = LogManager.getLogger();	

	@Override
	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException {

		String sql = DBUtils.beanToSql(NBPT_SP_PERSON.class, "INSERT", "NBPT_SP_PERSON", person);

		try {
			
			this.excuteUpdate(sql.toString(), conn);
		} catch (SQLException e) {
			
			log.error("插入终端人员出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}


	@Override
	public void addReposeAreas(List<NBPT_SP_PERSON_XZQX> reponseAreas, Connection conn) throws SQLException {

		List<String> sqls = new ArrayList<String>();
		for(NBPT_SP_PERSON_XZQX reponseArea : reponseAreas) {
			
			String sql = DBUtils.beanToSql(NBPT_SP_PERSON_XZQX.class, "insert", "NBPT_SP_PERSON_XZQX", reponseArea);
			sqls.add(sql);
		}
		
		try {
			
			this.excuteUpdateGroups(sqls, conn);
		} catch (SQLException e) {
			
			log.error("添加管理区域出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}


	@Override
	public List<NBPT_COMMON_XZQXHF> getAreaSelects(String loginId, Connection connection) throws SQLException {

		try {
	
			String sql ="  SELECT D.*" + 
						"  FROM NBPT_SP_REGION_XZQX A" + 
						"  LEFT JOIN NBPT_SP_REGION B" + 
						"  ON A.NBPT_SP_REGION_XZQX_REGIONID = B.NBPT_SP_REGION_ID" + 
						"  LEFT JOIN NBPT_SP_PERSON C" + 
						"  ON B.NBPT_SP_REGION_RESPONSIBLER = C.NBPT_SP_PERSON_PID" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF D" + 
						"  ON A.NBPT_SP_REGION_XZQX_XZQXID = D.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE C.NBPT_SP_PERSON_LOGINID = '" + loginId + "'" + 
						"  AND A.NBPT_SP_REGION_XZQX_TYPE = '22'" +
						"  ORDER BY D.NBPT_COMMON_XZQXHF_ID";
			
			List<NBPT_COMMON_XZQXHF> resultList = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
			return resultList;
		} catch (SQLException e) {
			
			log.error("获取地总下辖行政区县出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> getAreasSelect(String areaId, Connection connection) throws SQLException {

		try {
			String sql =  "SELECT A.* FROM "
						+ "FROM NBPT_COMMON_XZQXHF A "
						+ "LEFT JOIN NBPT_SP_REGION_XZQX B "
						+ "ON A.NBPT_COMMON_XZQXHF_ID = B.NBPT_SP_REGION_XZQX_XZQXID "
						+ "WHERE B.NBPT_SP_REGION_XZQX_REGIONID = '" + areaId + "'";
			
			List<NBPT_COMMON_XZQXHF> resultList = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
		
			return resultList;
		} catch (SQLException e) {
			
			log.error("AJAX获取管理区县出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> getTerminalResponsXzqx(String terminalPId, Connection connection) throws SQLException {


		try {
			String sql =  "  SELECT A.*" + 
							"  FROM NBPT_COMMON_XZQXHF A" + 
							"  LEFT JOIN NBPT_SP_PERSON_XZQX B" + 
							"  ON A.NBPT_COMMON_XZQXHF_ID = B.NBPT_SP_PERSON_XZQX_XID" + 
							"  LEFT JOIN NBPT_SP_PERSON C" + 
							"  ON B.NBPT_SP_PERSON_XZQX_PID = C.NBPT_SP_PERSON_ID" + 
							"  WHERE C.NBPT_SP_PERSON_PID = '"+ terminalPId +"'" +
							"  ORDER BY A.NBPT_COMMON_XZQXHF_ID";
			
			List<NBPT_COMMON_XZQXHF> resultList = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
		
			return resultList;
		} catch (SQLException e) {
			
			log.error("AJAX获取管理区县出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public void updateTerminal(NBPT_SP_PERSON person, Connection connection) throws SQLException {

		try {
			
			
			// 更新人员信息
			String updateSql = DBUtils.beanToSql(NBPT_SP_PERSON.class, "update", "NBPT_SP_PERSON", person);
			this.excuteUpdate(updateSql + " WHERE NBPT_SP_PERSON_PID = '" + person.getNBPT_SP_PERSON_PID() + "'", connection);
			
			// 删除原有负责区域
			String deleteSql = "DELETE FROM NBPT_SP_PERSON_XZQX WHERE NBPT_SP_PERSON_XZQX_PID = '" + person.getNBPT_SP_PERSON_ID() + "'";
			this.excuteUpdate(deleteSql, connection);	
				
			
		} catch (SQLException e) {
			log.error("更新终端人员信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> checkPlace(String string, Connection connection) throws SQLException {

		try {
			String sql ="  SELECT B.*" + 
						"  FROM NBPT_COMMON_XZQXHF A" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF B" + 
						"  ON A.NBPT_COMMON_XZQXHF_PID = B.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE A.NBPT_COMMON_XZQXHF_ID = '" + string + "'" +
						"  ORDER BY B.NBPT_COMMON_XZQXHF_ID";
			List<NBPT_COMMON_XZQXHF> placeInfo = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
			return placeInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新终端人员信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}	
	
}





















