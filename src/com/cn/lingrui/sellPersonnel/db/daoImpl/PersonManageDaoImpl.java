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
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

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
	public void addTerminalDeptId(String pid, String regionId, Connection connection) throws SQLException {

		try {
			String sql =  "INSERT NBPT_SP_PERSON_REGION VALUES ("
						+ "'" + CommonUtil.getUUID_32() + "',"
						+ "'" + pid + "',"
						+ "'" + regionId + "',"
						+ "'2')";
			
			this.excuteUpdate(sql, connection);
		
		} catch (SQLException e) {
			
			log.error("添加终端人员的部门信息出错" + CommonUtil.getTrace(e));
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

	@Override
	public List<CurrentPerson> receiveCurrentProvincePersonInfos(String region_UID, Connection connection) throws SQLException {


		try {
			String sql ="  SELECT A.*," + 
						"  B.NBPT_SP_REGION_ONAME," +
						"  B.NBPT_SP_REGION_NAME," +
						"  B.NBPT_SP_REGION_UID," +
						"  C.NBPT_SP_REGION_UID AS REGION_UID," +
						"  D.NBPT_COMMON_XZQXHF_ID," + 
						"  D.NBPT_COMMON_XZQXHF_NAME" +
						"  FROM NBPT_SP_PERSON A" + 
						"  LEFT JOIN NBPT_SP_REGION B" + 
						"  ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_UID" + 
						"  LEFT JOIN NBPT_SP_REGION C" + 
						"  ON B.NBPT_SP_REGION_ID LIKE C.NBPT_SP_REGION_ID + '%'" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF D" + 
						"  ON SUBSTRING(B.NBPT_SP_REGION_ID,3,2)+'0000' = D.NBPT_COMMON_XZQXHF_ID " + 
						"  WHERE C.NBPT_SP_REGION_UID = '" + region_UID + "'" + 
						"  AND B.NBPT_SP_REGION_UID <> '" + region_UID + "'" + 
						"  AND A.NBPT_SP_PERSON_JOB <> '27'" +
						"  ORDER BY B.NBPT_SP_REGION_ID";
			List<CurrentPerson> personinfos = this.queryForClaszs(sql, connection, CurrentPerson.class);
			return personinfos;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("获取大区下人员信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentPerson> receiveCurrentPersonInfos(String areaUid, Connection connection) throws SQLException {

		try {
			String sql ="  SELECT " + 
						"  A.NBPT_SP_PERSON_NAME,A.NBPT_SP_PERSON_TYPE," +
						"  A.NBPT_SP_PERSON_MOB1,A.NBPT_SP_PERSON_MOB2,A.NBPT_SP_PERSON_JOB," +
						"  B.NBPT_SP_REGION_ONAME,  B.NBPT_SP_REGION_NAME," + 
						"  CASE A.NBPT_SP_PERSON_MALE" +
						"    WHEN '0' THEN '女'" +
						"    WHEN '1' THEN '男'" +
						"  END AS NBPT_SP_PERSON_MALE," +
						"  A.NBPT_SP_PERSON_ENTRYDATA," +
						"  A.NBPT_SP_PERSON_PLACE," +
						"  A.NBPT_SP_PERSON_DEGREE," +
						"  A.NBPT_SP_PERSON_PID," + 
						"  A.NBPT_SP_PERSON_FLAG," + 
						"  CASE ISDATE(SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8))" + 
						"    WHEN 1 THEN DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE())" + 
						"    WHEN 0 THEN 888" + 
						"  END AS NBPT_SP_PERSON_AGE," + 
						"  CASE ISDATE(A.NBPT_SP_PERSON_ENTRYDATA)" + 
						"    WHEN 1 THEN DATEDIFF(yy,A.NBPT_SP_PERSON_ENTRYDATA,GETDATE())" + 
						"    WHEN 0 THEN 888" + 
						"  END AS NBPT_SP_PERSON_WORKAGE " + 
						"  FROM NBPT_SP_PERSON A  LEFT JOIN NBPT_SP_REGION B  ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_UID  " + 
						"  WHERE B.NBPT_SP_REGION_UID = '" + areaUid + "'  ORDER BY B.NBPT_SP_REGION_ID";
			List<CurrentPerson> personinfos = this.queryForClaszs(sql, connection, CurrentPerson.class);
			return personinfos;
		} catch (SQLException e) {
			log.error("获取地区下人员信息出错" + CommonUtil.getTrace(e));
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
			NBPT_VIEW_REGION regionInfo = this.oneQueryForClasz(sql.toString(), connection, NBPT_VIEW_REGION.class);
			return regionInfo;
		} catch (SQLException e) {
			log.error("根据负责人获取地区信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}
	
	
}





















