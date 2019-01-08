package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SellPersonnelBaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_RECORD;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
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
	public List<NBPT_VIEW_REGION> receiveRegion(String parentUid, String provinceId,Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer("SELECT * "
										  + "FROM NBPT_VIEW_REGION A "
										  + "WHERE 1=1 ");
		if(null != parentUid) {
			
			sql.append("AND A.NBPT_SP_REGION_PARENT_UID = '" + parentUid + "' ");
		} 
		
		if(null != provinceId) {

			sql.append("AND A.NBPT_SP_REGION_PROVINCE_ID = '" + provinceId + "' ");
		}

		sql.append("ORDER BY A.NBPT_SP_REGION_FLAG DESC ,A.NBPT_SP_REGION_PARENT_ID ASC, A.NBPT_SP_REGION_ID ASC, A.NBPT_SP_REGION_PROVINCE_ID ASC");
		
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
		

		sql.append("AND A.NBPT_SP_REGION_UID = '" + regionUid + "' ");
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
	public NBPT_VIEW_XZQX receiveXzqx(String xzqxId, String type, Connection connection) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * ");
		sql.append("FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE A.NBPT_COMMON_XZQXHF_ID = '" + xzqxId + "' ");

		if(null != type) {

			sql.append("AND A.NBPT_SP_REGION_XZQX_TYPE = '" + type + "' ");
		}

		try {
			
			return this.oneQueryForClasz(sql.toString(), connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			
			log.error("查询省份与地区绑定关系出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_XZQX> receiveXzqxs(String[] xzqxIds, String level, String type, Connection connection) throws SQLException { 
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * ");
		sql.append("FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE 1=1 ");
		
		// 查询所有
		if(null == xzqxIds || 0 == xzqxIds.length) {
			
			
		} else {

			sql.append("AND NBPT_COMMON_XZQXHF_ID IN (" );
			
			sql.append("'" + xzqxIds[0] + "'");
			for(int i = 1; i < xzqxIds.length; i++) {
				
				sql.append(",'" + xzqxIds[i] + "'");
			}
			sql.append(")" );
		}
		

		if(null != level) {

			sql.append("AND A.NBPT_COMMON_XZQXHF_LEVEL = '" + level + "' ");
		}
		if(null != type) {

			sql.append("AND A.NBPT_SP_REGION_XZQX_TYPE = '" + type + "'");
		}
		sql.append("ORDER BY A.NBPT_COMMON_XZQXHF_ID");
		try {
			
			return this.queryForClaszs(sql.toString(), connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			
			log.error("查询国家行政区县划分信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
	@Override
	public List<NBPT_VIEW_XZQX> receiveRegionXzqx(String regionUid, Connection connection) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * ");
		sql.append("FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE A.NBPT_SP_REGION_UID = '" + regionUid + "' ORDER BY A.NBPT_COMMON_XZQXHF_ID");
		
		try {
			
			return this.queryForClaszs(sql.toString(), connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			
			log.error("根据地区ID查询地区行政区县绑定关系出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
	@Override
	public void changeTerminalState(String uncheckPid, String type, Connection conn, String... leaveTime) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE NBPT_SP_PERSON ");
		sql.append("SET NBPT_SP_PERSON_FLAG = '" + type + "' ");
		if("0".equals(type)) {
			sql.append(", NBPT_SP_PERSON_LEAVE_APPLY_DATA = '" + CommonUtil.getYYYYMMDD() + "' ");
			sql.append(", NBPT_SP_PERSON_LEAVE_REAL_DATA = '" + leaveTime[0] + "' ");
		}
		sql.append("WHERE NBPT_SP_PERSON_PID = '" + uncheckPid + "'");
		try {
			this.excuteUpdate(sql, conn);
		} catch (SQLException e) {
			log.error("修改终端人员状态出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
	
	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String[] personPids, Connection connection) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.* ");
		sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
		sql.append("WHERE (A.NBPT_SP_PERSON_PID = '' ");

		if(0 != personPids.length) {
			
			// 如果指定UID为空,查询所有
			for(String regionUid : personPids) {
				
				sql.append("OR A.NBPT_SP_PERSON_PID = '" + regionUid + "' ");
			}
		}
		sql.append(") ");

		sql.append("ORDER BY CHARINDEX(A.NBPT_SP_PERSON_FLAG , '210543')");
		try {
		
			List<NBPT_VIEW_CURRENTPERSON> persons = this.queryForClaszs(sql, connection, NBPT_VIEW_CURRENTPERSON.class);
		
			return persons;
		
		} catch (SQLException e) {
		
			log.error("查询指定人员出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String personJob, String personName, String personType, Connection connection)
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM NBPT_VIEW_CURRENTPERSON A ");
		sql.append("WHERE 1=1 ");

		if(null != personName) {
			sql.append("AND ( 1<>1 ");
			sql.append("OR A.NBPT_SP_PERSON_NAME LIKE '%" + personName + "%' ");
			sql.append("OR A.NBPT_SP_PERSON_LOGINID LIKE '%" + personName + "%' ");
			sql.append(") ");
		}
		
		if(null != personJob) {
			sql.append("AND A.NBPT_SP_PERSON_JOB = '" + personJob + "' ");
		}
		
		if(null != personType) {
			
			sql.append("AND A.NBPT_SP_PERSON_TYPE = '" + personType + "' ");
		}

		sql.append("ORDER BY CHARINDEX(A.NBPT_SP_PERSON_FLAG , '210543')");
		try {
			
			List<NBPT_VIEW_CURRENTPERSON> persons = this.queryForClaszs(sql, connection, NBPT_VIEW_CURRENTPERSON.class);
		
			return persons;
		
		} catch (SQLException e) {
		
			log.error("查询指定人员出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String[] personJob, String personName, String personType, Connection connection)
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM NBPT_VIEW_CURRENTPERSON A ");
		sql.append("WHERE 1=1 ");
		
		if(null != personName) {
			sql.append("AND ( 1<>1 ");
			sql.append("OR A.NBPT_SP_PERSON_NAME LIKE '%" + personName + "%' ");
			sql.append("OR A.NBPT_SP_PERSON_LOGINID LIKE '%" + personName + "%' ");
			sql.append(") ");
		}
		
		if(null != personJob) {
			
			sql.append("AND ( 1<>1 ");
			for(String personjob : personJob) {

				sql.append("OR A.NBPT_SP_PERSON_JOB = '" + personjob + "' ");
			}

			sql.append(") ");
		}
		
		if(null != personType) {
			
			sql.append("AND A.NBPT_SP_PERSON_TYPE = '" + personType + "' ");
		}
		
		sql.append("ORDER BY CHARINDEX(A.NBPT_SP_PERSON_FLAG , '210543')");
		
		try {
			
			List<NBPT_VIEW_CURRENTPERSON> persons = this.queryForClaszs(sql, connection, NBPT_VIEW_CURRENTPERSON.class);
		
			return persons;
		
		} catch (SQLException e) {
		
			log.error("查询指定人员出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(List<String> personJob, String personName, String personType, Connection connection)
			throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public List<NBPT_VIEW_REGION> receiveRegion(String regionName, String level, String provinceId, Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM NBPT_VIEW_REGION A ");
		sql.append("WHERE 1=1 ");
		
		if(null != level) {
			
			sql.append("AND A.NBPT_SP_REGION_LEVEL = '" + level + "'");
		}
		
		if(null != provinceId) {
			
			sql.append("AND A.NBPT_SP_REGION_PROVINCE_ID = '" + provinceId + "'");
		}
		
		if(null != regionName) {

			sql.append("AND A.NBPT_SP_REGION_NAME LIKE '%" + regionName + "%'");
		}
		
		sql.append("ORDER BY A.NBPT_SP_REGION_FLAG DESC, A.NBPT_SP_REGION_ID ");
		
		try {
			
			List<NBPT_VIEW_REGION> regions = this.queryForClaszs(sql, conn, NBPT_VIEW_REGION.class);
		
			return regions;
		
		} catch (SQLException e) {
		
			log.error("查询指定地区出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
	}

	@Override
	public void addRecord(String target, String by, String type, Connection conn, String... note) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT NBPT_SP_RECORD ");
		sql.append("(");
		sql.append("NBPT_SP_RECORD_TARGET");
		sql.append(",NBPT_SP_RECORD_TYPE ");
		sql.append(",NBPT_SP_RECORD_BY ");
		sql.append(",NBPT_SP_RECORD_ID ");
		sql.append(",NBPT_SP_RECORD_TIME ");
		if(0 != note.length) 
			sql.append(",NBPT_SP_RECORD_NOTE ");
		sql.append(")");
		sql.append("VALUES ( ");
		sql.append("'" + target + "' ");
		sql.append(",'" + type + "' ");
		sql.append(",'" + by + "' ");
		sql.append(",'" + CommonUtil.getUUID_32() + "' ");
		sql.append(",'" + new Date().getTime() + "' ");
		
		if(0 != note.length) 
			sql.append(",'" + note[0] + "' ");
		sql.append(")");

		try {
			
			this.excuteUpdate(sql, conn);
		
		} catch (SQLException e) {
		
			log.error("插入操作记录出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
		
	}
	
	@Override
	public NBPT_SP_RECORD getRecord(String target, Connection conn) throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOP 1 * FROM NBPT_SP_RECORD A ");
		sql.append("WHERE A.NBPT_SP_RECORD_TARGET = '" + target + "' ");
		sql.append("AND A.NBPT_SP_RECORD_TYPE = '3' ");
		sql.append("ORDER BY A.NBPT_SP_RECORD_TIME DESC");
		try {
			
			NBPT_SP_RECORD record = this.oneQueryForClasz(sql, conn, NBPT_SP_RECORD.class);
			
			return record;
		} catch (SQLException e) {
		
			log.error("获取操作记录出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
		
	}

	@Override
	public NBPT_SP_PERSON receiveSpPerson(String personPid, Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_SP_PERSON A ");
		sql.append("WHERE A.NBPT_SP_PERSON_PID = '" + personPid + "'");
		try {
			
			NBPT_SP_PERSON person = this.oneQueryForClasz(sql, conn, NBPT_SP_PERSON.class);

			return person;
		} catch (SQLException e) {
		
			log.error("获取人员信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
	}

	@Override
	public NBPT_COMMON_XZQXHF receiveXzqx(String xzqxId, Connection connection) throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_COMMON_XZQXHF A ");
		sql.append("WHERE A.NBPT_COMMON_XZQXHF_ID = '" + xzqxId + "'");
		try {
			
			NBPT_COMMON_XZQXHF xzqx = this.oneQueryForClasz(sql, connection, NBPT_COMMON_XZQXHF.class);

			return xzqx;
		} catch (SQLException e) {
		
			log.error("获取国家行政区县划分信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> receiveCommonXzqxs(List<String> xzqxId, String level, Connection connection) throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_COMMON_XZQXHF A ");

		sql.append("WHERE 1 = 1 ");
		if(null == xzqxId) {
		} else if(0 != xzqxId.size()){
			// TODO
		}
		if(null != level) {
			sql.append("AND A.NBPT_COMMON_XZQXHF_LEVEL = '" + level + "' ");
		} else {
		}
		
		sql.append("ORDER BY A.NBPT_COMMON_XZQXHF_ID");
		try {
			List<NBPT_COMMON_XZQXHF> xzqxs = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
			return xzqxs;
		} catch (SQLException e) {
		
			log.error("获取国家行政区县划分信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
	}

	@Override
	public List<NBPT_VIEW_XZQX> receiveXzqxsByProvince(String provinceId, Connection connection) throws SQLException {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_VIEW_XZQX A ");
		sql.append("WHERE A.NBPT_COMMON_XZQXHF_PID LIKE '" + provinceId.substring(0,2) + "%' ORDER BY A.NBPT_COMMON_XZQXHF_ID");
		try {
			
			List<NBPT_VIEW_XZQX> xzqx = this.queryForClaszs(sql, connection, NBPT_VIEW_XZQX.class);

			return xzqx;
		} catch (SQLException e) {
		
			log.error("按省份获取国家行政区县划分信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receivePersonByDepart(String regionUid, String areaUid, String provinceId, String personType,
			Connection connection) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT B.* FROM NBPT_SP_PERSON_REGION A "
				+ "LEFT JOIN NBPT_VIEW_CURRENTPERSON B "
				+ "ON A.NBPT_SP_PERSON_REGION_PID = B.NBPT_SP_PERSON_PID ");
		sql.append("WHERE A.NBPT_SP_PERSON_REGION_TYPE = '2' ");
		if(null != regionUid) {
			sql.append("AND B.NBPT_SP_PERSON_REGION_UID = '" + regionUid + "' ");
		}
		
		if(null != areaUid) {
			sql.append("AND B.NBPT_SP_PERSON_AREA_UID = '" + areaUid + "' ");
		}
		
		if(null != personType) {
			sql.append("AND B.NBPT_SP_PERSON_TYPE = '" + personType + "' ");
		}

		if(null != provinceId) {
			sql.append("AND B.NBPT_SP_PERSON_PROVINCE_ID = '" + provinceId + "' ");
		}
		
		sql.append("ORDER BY CHARINDEX(B.NBPT_SP_PERSON_FLAG , '210543'), B.NBPT_SP_PERSON_JOB ASC");
		
		try {
			
			List<NBPT_VIEW_CURRENTPERSON> persons = this.queryForClaszs(sql, connection, NBPT_VIEW_CURRENTPERSON.class);

			return persons;
		} catch (SQLException e) {
		
			log.error("按部门获取国家行政区县划分信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON_REGION receivePersonRegion(String personPid, Connection connection) throws SQLException {

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM NBPT_SP_PERSON_REGION A ");
			sql.append("WHERE A.NBPT_SP_PERSON_REGION_PID = '" + personPid + "' ");
			sql.append("AND A.NBPT_SP_PERSON_REGION_TYPE = '2'");
			NBPT_SP_PERSON_REGION result = this.oneQueryForClasz(sql, connection, NBPT_SP_PERSON_REGION.class);
			return result;
		} catch (Exception e) {
			log.error("按人员PID获取人员与部门绑定关系信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_REGION> receiveRegions(String parentUid, String province, String level, String type,
			Connection conn) throws SQLException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM NBPT_VIEW_REGION A ");
			sql.append("WHERE 1=1 ");
			if(null != parentUid) {
				sql.append("AND A.NBPT_SP_REGION_PARENT_UID = '" + parentUid + "' ");
			}
			
			if(null != province) {

				sql.append("AND A.NBPT_SP_REGION_PROVINCE_ID = '" + province + "' ");
			}
			
			if(null != level) {
				sql.append("AND A.NBPT_SP_REGION_LEVEL = '" + level + "' ");				
			}
			
			if(null != type) {
				sql.append("AND A.NBPT_SP_REGION_TYPE = '" + type + "' ");				
			}
			
			List<NBPT_VIEW_REGION> result = this.queryForClaszs(sql, conn, NBPT_VIEW_REGION.class);
			return result;
		} catch (Exception e) {
			
			log.error("获取指定条件所有部门信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_REGION receiveSpRegion(String uid,String responsiblerPid, Connection conn) throws SQLException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM NBPT_SP_REGION A ");
			sql.append("WHERE 1=1 ");
			if(null != uid) {
				sql.append("AND A.NBPT_SP_REGION_UID = '" + uid + "' ");
			}
			if(null != responsiblerPid) {
				sql.append("AND A.NBPT_SP_REGION_RESPONSIBLER = '" + responsiblerPid + "' ");
			}
			NBPT_SP_REGION result = this.oneQueryForClasz(sql, conn, NBPT_SP_REGION.class);
			return result;
		} catch (Exception e) {
			
			log.error("获取指定条件所有部门信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON_REGION receivePersonRegionResper(String regionUid, Connection connection)
			throws SQLException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM NBPT_SP_PERSON_REGION A ");
			sql.append("WHERE A.NBPT_SP_PERSON_REGION_RID = '" + regionUid + "' ");
			sql.append("AND A.NBPT_SP_PERSON_REGION_TYPE = '1'");
			NBPT_SP_PERSON_REGION result = this.oneQueryForClasz(sql, connection, NBPT_SP_PERSON_REGION.class);
			return result;
		} catch (Exception e) {
			log.error("按人员PID获取人员与部门负责关系信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void deleteTerminal(String terminalPid, Connection conn) throws SQLException {
		try {
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("DELETE FROM NBPT_SP_PERSON ");
			sql.append("WHERE NBPT_SP_PERSON_PID = '" + terminalPid + "'");
			
			this.excuteUpdate(sql, conn);
		} catch (SQLException e) {
	
			log.error("删除指定终端人员出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}	
	}

	@Override
	public void deletePersonRegion(String terminalPid, Connection conn) throws SQLException {
		try {
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("DELETE FROM NBPT_SP_PERSON_REGION ");
			sql.append("WHERE NBPT_SP_PERSON_REGION_PID = '" + terminalPid + "' AND NBPT_SP_PERSON_REGION_TYPE = '2'");
			
			this.excuteUpdate(sql, conn);
			
			StringBuffer sql2 = new StringBuffer();
			sql2.append("UPDATE NBPT_SP_PERSON_REGION ");
			sql2.append("SET NBPT_SP_PERSON_REGION_PID = '' ");
			sql2.append("WHERE NBPT_SP_PERSON_REGION_PID = '" + terminalPid + "' AND NBPT_SP_PERSON_REGION_TYPE = '1'");
			this.excuteUpdate(sql2, conn);
		} catch (SQLException e) {
	
			log.error("删除指定人员绑定信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
}

























