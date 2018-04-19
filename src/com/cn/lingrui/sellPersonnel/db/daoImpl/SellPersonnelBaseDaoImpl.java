package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SellPersonnelBaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

public class SellPersonnelBaseDaoImpl extends BaseDaoImpl implements SellPersonnelBaseDao{


	private static Logger log = LogManager.getLogger();

	@Override
	public CurrentPerson receiveCurrentPerson(String loginId, Connection conn) throws SQLException {
		
		// userId获取的是辅助系统的登录ID,与人员信息LOGINID一致,如果REGION_UID为空,则是地总,不为空则是大区总,REGION_UID为负责大区的UID
		
		String sql ="    SELECT A.*, " + 
					"    B.NBPT_SP_REGION_ONAME AS NBPT_SP_REGION_NEED," + // 所在部门配额(22或26,获取的是地区,21获取的大区)
					"    B.NBPT_SP_REGION_RESPONSIBLER AS NBPT_SP_REGION_RESPONSIBLER, " + // 所在部门负责人
					"    CASE A.NBPT_SP_PERSON_JOB " + 
					"        WHEN '22'" + 
					"            THEN (SELECT NBPT_SP_REGION_UID FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1' AND NBPT_SP_REGION_ID = SUBSTRING(B.NBPT_SP_REGION_ID,1,2))" + 
					"        WHEN '21'" + 
					"            THEN B.NBPT_SP_REGION_UID" + 
					"        WHEN '26'" + 
					"            THEN (SELECT NBPT_SP_REGION_UID FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1' AND NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID)" + 
					"        WHEN '27'" + 
					"            THEN A.NBPT_SP_PERSON_DEPT_ID" + 
					"        END AS REGION_UID," + // 所在大区UID
					"    CASE A.NBPT_SP_PERSON_JOB " + 
					"        WHEN '22'" + 
					"            THEN (SELECT NBPT_SP_REGION_NAME FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1' AND NBPT_SP_REGION_ID = SUBSTRING(B.NBPT_SP_REGION_ID,1,2))" + 
					"        WHEN '21'" + 
					"            THEN B.NBPT_SP_REGION_NAME" + 
					"        WHEN '26'" + 
					"            THEN (SELECT NBPT_SP_REGION_NAME FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1' AND NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID)" + 
					"        WHEN '27'" + 
					"            THEN '信息专员没有所属大区'" +  
					"        END AS REGION_NAME" + // 所在大区NAME
					"    FROM NBPT_SP_PERSON A " + 
					"    LEFT JOIN NBPT_SP_REGION B " + 
					"    ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_UID " + 
					"    WHERE NBPT_SP_PERSON_LOGINID = '" + loginId + "'";
		
		try {
			
			CurrentPerson currentPerson = this.oneQueryForClasz(sql, conn, CurrentPerson.class);
			
			return currentPerson;
			
		} catch (SQLException e) {
			
			log.error("查询当前登录用户出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
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

	@Override
	public List<NBPT_VIEW_REGION> receiveRegion(String parentUid, String provinceId,Connection conn, Integer... type) throws SQLException {
		
		StringBuffer sql = new StringBuffer("SELECT * "
										  + "FROM NBPT_VIEW_REGION A "
										  + "WHERE 1=1 ");
		if(null != parentUid) {
			
			sql.append("AND A.NBPT_SP_REGION_PARENT_UID = '" + parentUid + "' ");
		}
		
		if(null != provinceId) {

			sql.append("AND A.NBPT_SP_REGION_PROVINCE_ID = '" + provinceId + "' ");
		}
		
		if(type.length != 0 && 1 == type[0]) {
			
		} else {

			sql.append("AND A.NBPT_SP_REGION_PARENT_UID <> '' AND A.NBPT_SP_REGION_PARENT_UID IS NOT NULL ");
		}
		
		sql.append("ORDER BY A.NBPT_SP_REGION_PARENT_ID ASC, A.NBPT_SP_REGION_ID ASC, A.NBPT_SP_REGION_PROVINCE_ID");
		
		try {
			
			List<NBPT_VIEW_REGION> regions = this.queryForClaszs(sql.toString(), conn, NBPT_VIEW_REGION.class);
			
			return regions;
			
		} catch (SQLException e) {
			
			log.error("查询部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
	@Override
	public NBPT_VIEW_REGION receiveRegion(String regionUid, Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer("  SELECT A.* "
										  + "  FROM NBPT_VIEW_REGION A "
										  + "  WHERE 1=1 ");
		
		 // 如果指定UID为空,查询所有
		if(null != regionUid) {

			sql.append("AND A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
		}
		
		sql.append("ORDER BY A.NBPT_SP_REGION_PARENT_ID ASC, A.NBPT_SP_REGION_ID ASC, A.NBPT_SP_REGION_PROVINCE_ID");
		
		try {
			
			NBPT_VIEW_REGION region = this.oneQueryForClasz(sql.toString(), conn, NBPT_VIEW_REGION.class);
			
			return region;
			
		} catch (SQLException e) {
			
			log.error("查询指定部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receiveTerminal(String regionUid, String provinceId, String AreaId, String terminalPid, Connection connection) throws SQLException {
		
		try {
			
			// 初始化返回结果
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<NBPT_VIEW_CURRENTPERSON>();

			// 初始化查询sql
			StringBuffer sql = new StringBuffer();
			// 查大区
			if(null != regionUid) {
				
				sql.append("SELECT A.* ");
				sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
				sql.append("WHERE  A.NBPT_SP_PERSON_REGION_UID = '" + regionUid + "' ");
				sql.append("AND A.NBPT_SP_PERSON_TYPE = '2' ");
			} 
			// 查省区
			else if (null != provinceId) {

				sql.append("SELECT A.* ");
				sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
				sql.append("WHERE A.NBPT_SP_PERSON_PROVINCE_ID = '" + provinceId + "' ");
				sql.append("AND A.NBPT_SP_PERSON_TYPE = '2' ");
			}
			// 查地区
			else if(null != AreaId) {

				sql.append("SELECT A.* ");
				sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
				sql.append("WHERE A.NBPT_SP_PERSON_AREA_UID = '" + AreaId + "' ");
				sql.append("AND A.NBPT_SP_PERSON_TYPE = '2' ");
			}
			// 查人
			else if(null != terminalPid) {

				sql.append("SELECT A.* ");
				sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
				sql.append("WHERE A.NBPT_SP_PERSON_PID = '" + terminalPid + "' ");
				sql.append("AND A.NBPT_SP_PERSON_TYPE = '2' ");
			}
			
			sql.append("AND A.NBPT_SP_PERSON_JOB IN (23,24,25)");
			persons = this.queryForClaszs(sql.toString(), connection, NBPT_VIEW_CURRENTPERSON.class);
			return persons;
			
		} catch (SQLException e) {
			
			log.error("查询终端出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_REGION> receiveRegion(List<String> regionUids, Connection conn) throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.* ");
		sql.append("FROM NBPT_VIEW_REGION A ");
		sql.append("WHERE A.NBPT_SP_REGION_UID = '' ");

		if(0 != regionUids.size()) {
			
			// 如果指定UID为空,查询所有
			for(String regionUid : regionUids) {
				
				sql.append("OR A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
			}
		}
		
		sql.append("ORDER BY A.NBPT_SP_REGION_PARENT_ID ASC, A.NBPT_SP_REGION_ID ASC, A.NBPT_SP_REGION_PROVINCE_ID");
		
		try {
		
			List<NBPT_VIEW_REGION> regions = this.queryForClaszs(sql.toString(), conn, NBPT_VIEW_REGION.class);
		
			return regions;
		
		} catch (SQLException e) {
		
			log.error("查询指定多部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_REGION> receiveRegion(String[] regionUids, Connection conn) throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.* ");
		sql.append("FROM NBPT_VIEW_REGION A ");
		sql.append("WHERE A.NBPT_SP_REGION_UID = '' ");

		if(0 != regionUids.length) {
			
			// 如果指定UID为空,查询所有
			for(String regionUid : regionUids) {
				
				sql.append("OR A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
			}
			
		}
		
		sql.append("ORDER BY A.NBPT_SP_REGION_PARENT_ID ASC, A.NBPT_SP_REGION_ID ASC, A.NBPT_SP_REGION_PROVINCE_ID");
		
		try {
		
			List<NBPT_VIEW_REGION> regions = this.queryForClaszs(sql.toString(), conn, NBPT_VIEW_REGION.class);
		
			return regions;
		
		} catch (SQLException e) {
		
			log.error("查询指定多部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
}

























