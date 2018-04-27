package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SellPersonnelBaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;

public class SellPersonnelBaseDaoImpl extends BaseDaoImpl implements SellPersonnelBaseDao{


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

		sql.append("AND A.NBPT_SP_REGION_FLAG = '1' ");
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

		sql.append("AND A.NBPT_SP_REGION_FLAG = '1' ");
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
			// 所有参数为空,查询所有
			else {

				sql.append("SELECT A.* ");
				sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
				sql.append("WHERE A.NBPT_SP_PERSON_TYPE = '2' ");
			}
			
			sql.append("AND A.NBPT_SP_PERSON_JOB IN (23,24,25)");
			sql.append("ORDER BY A.NBPT_SP_PERSON_AREA_ID ASC,A.NBPT_SP_PERSON_FLAG ASC,A.NBPT_SP_PERSON_ID ASC ");
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
		sql.append("WHERE (A.NBPT_SP_REGION_UID = '' ");

		if(0 != regionUids.size()) {
			
			// 如果指定UID为空,查询所有
			for(String regionUid : regionUids) {
				
				sql.append("OR A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
			}
		}
		sql.append(") ");

		sql.append("AND A.NBPT_SP_REGION_FLAG = '1' ");
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
		sql.append("WHERE (A.NBPT_SP_REGION_UID = '' ");

		if(0 != regionUids.length) {
			
			// 如果指定UID为空,查询所有
			for(String regionUid : regionUids) {
				
				sql.append("OR A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
			}
			
		}
		sql.append(") ");
		sql.append("AND A.NBPT_SP_REGION_FLAG = '1' ");
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
	public List<NBPT_COMMON_XZQXHF> receiveTerminalResponsAreas(String terminalPid, Connection connection) throws SQLException {

		String sql =    "  SELECT C.*" + 
						"  FROM NBPT_SP_PERSON_XZQX A" + 
						"  LEFT JOIN NBPT_SP_PERSON B" + 
						"  ON A.NBPT_SP_PERSON_XZQX_PID = B.NBPT_SP_PERSON_ID" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF C" + 
						"  ON A.NBPT_SP_PERSON_XZQX_XID = C.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE B.NBPT_SP_PERSON_PID = '" + terminalPid + "'";
		try {
			
			
			List<NBPT_COMMON_XZQXHF> respons = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
			
			return respons;
		} catch (SQLException e) {
			
			log.error("查询终端人员负责区域" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}
	
	@Override
	public NBPT_VIEW_REGION receiveRegionByResper(String nbpt_SP_PERSON_PID, String type, Connection connection) throws SQLException {
		
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT * ");
			sql.append("FROM NBPT_VIEW_REGION A ");
			sql.append("WHERE A.NBPT_SP_REGION_RESPONSIBLER = '" + nbpt_SP_PERSON_PID + "' ");
			sql.append("AND A.NBPT_SP_REGION_LEVEL = '" + type + "' ");
			sql.append("AND A.NBPT_SP_REGION_FLAG = '1' ");
			NBPT_VIEW_REGION regionInfo = this.oneQueryForClasz(sql.toString(), connection, NBPT_VIEW_REGION.class);
			return regionInfo;
		} catch (SQLException e) {
			log.error("根据负责人获取地区信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public NBPT_VIEW_CURRENTPERSON receivePerson(String personPid, Connection connection) throws SQLException {
		
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT * ");
			sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
			sql.append("WHERE A.NBPT_SP_PERSON_PID = '" + personPid + "' ");
			NBPT_VIEW_CURRENTPERSON regionInfo = this.oneQueryForClasz(sql.toString(), connection, NBPT_VIEW_CURRENTPERSON.class);
			
			return regionInfo;
		} catch (SQLException e) {
			log.error("根据PID查询指定人员出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}
	@Override
	public NBPT_VIEW_XZQX receiveXzqx(String xzqxId, Connection connection) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * ");
		sql.append("FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE A.NBPT_COMMON_XZQXHF_ID = '" + xzqxId + "' ");

		try {
			
			return this.oneQueryForClasz(sql.toString(), connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			
			log.error("查询废弃的部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_XZQX> receiveRegionXzqx(String regionUid, Connection connection) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * ");
		sql.append("FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
		
		try {
			
			return this.queryForClaszs(sql.toString(), connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			
			log.error("查询废弃的部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
}

























