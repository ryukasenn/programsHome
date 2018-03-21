package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

@Repository("personManageDao")
public class PersonManageDaoImpl extends BaseDaoImpl implements PersonManageDao{

	private static Logger log = LogManager.getLogger();	

	@Override
	public CurrentPerson receiveCurrentPerson(String userId, Connection conn) throws SQLException {
		
		String sql = "SELECT A.*,B.NBPT_SP_REGION_ONAME AS NBPT_SP_REGION_NEED, B.NBPT_SP_REGION_RESPONSIBLER AS NBPT_SP_REGION_RESPONSIBLER "
				+ "FROM NBPT_SP_PERSON A "
				+ "LEFT JOIN NBPT_SP_REGION B "
				+ "ON A.NBPT_SP_PERSON_PID = B.NBPT_SP_REGION_RESPONSIBLER "
				+ "WHERE NBPT_SP_PERSON_LOGINID = '" + userId + "'";
		
		try {
			
			CurrentPerson resultList = this.oneQuery(sql, conn, CurrentPerson.class);
			
			return resultList;
			
		} catch (SQLException e) {
			
			log.info("查询当前登录用户出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentPerson> receiveCurrentTerminals(NBPT_SP_PERSON person, Connection conn)  throws SQLException {
		
		try {
			
			// 如果person为null,后勤查询
			if(null == person) {

				// 查询所有大区总
				String sql =    "SELECT " + 
								"CASE A.NBPT_SP_PERSON_TYPE " + 
									"WHEN '1' THEN '商务' " + 
									"WHEN '2' THEN 'OTC ' " + 
									"WHEN '3' THEN '临床'" + 
									"WHEN '4' THEN '诊所' " + 
									"END AS TYPE," + 
								"A.NBPT_SP_PERSON_NAME AS NAME," + 
								"B.NBPT_SP_REGION_NAME AS AREA," + 
								"B.NBPT_SP_REGION_ONAME AS NEED," + 
								"(SELECT COUNT(*) FROM NBPT_SP_PERSON C " + 
								" WHERE EXISTS( " + 
								"	 SELECT * FROM NBPT_SP_REGION_XZQX D " + 
								"	 WHERE D.NBPT_SP_REGION_XZQX_REGIONID <> A.NBPT_SP_PERSON_DEPT_ID AND " + 
								"		 EXISTS(" + 
								"			 SELECT * FROM NBPT_SP_REGION_XZQX E " + 
								"			 WHERE E.NBPT_SP_REGION_XZQX_REGIONID = A.NBPT_SP_PERSON_DEPT_ID AND " + 
								"				 D.NBPT_SP_REGION_XZQX_XZQXID = E.NBPT_SP_REGION_XZQX_XZQXID " + 
								"		) AND " + 
								"	      C.NBPT_SP_PERSON_DEPT_ID = D.NBPT_SP_REGION_XZQX_REGIONID AND " + 
								"	      C.NBPT_SP_PERSON_FLAG = '0')) AS INFACT," + 
								"(SELECT COUNT(*) FROM NBPT_SP_PERSON F " + 
								" WHERE EXISTS( " + 
								"	 SELECT * FROM NBPT_SP_REGION_XZQX G " + 
								"	 WHERE G.NBPT_SP_REGION_XZQX_REGIONID <> A.NBPT_SP_PERSON_DEPT_ID AND " + 
								"		 EXISTS(" + 
								"			 SELECT * FROM NBPT_SP_REGION_XZQX H " + 
								"			 WHERE H.NBPT_SP_REGION_XZQX_REGIONID = A.NBPT_SP_PERSON_DEPT_ID AND " + 
								"				 G.NBPT_SP_REGION_XZQX_XZQXID = H.NBPT_SP_REGION_XZQX_XZQXID " + 
								"		) AND " + 
								"	      F.NBPT_SP_PERSON_DEPT_ID = G.NBPT_SP_REGION_XZQX_REGIONID )) AS HISTORY " + 
								"FROM NBPT_SP_PERSON A " + 
								"LEFT JOIN NBPT_SP_REGION B " + 
								"ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_ID " + 
								"WHERE A.NBPT_SP_PERSON_JOB  = '1' OR A.NBPT_SP_PERSON_JOB = '6' ORDER BY NBPT_SP_PERSON_ID ASC ";
				
				List<CurrentPerson> resultList = this.query(sql, conn, CurrentPerson.class);
				
				return resultList;
			} 
			
			// 如果是大区总
			else if("1".equals(person.getNBPT_SP_PERSON_JOB()) || "6".equals(person.getNBPT_SP_PERSON_JOB())) {

				// 查询所有非离职地总
				String sql =    "SELECT " + 
								"A.NBPT_SP_PERSON_NAME AS NAME ," + 
								"B.NBPT_SP_REGION_NAME AS AREA ," + 
								"B.NBPT_SP_REGION_ONAME AS NEED," + 
								"(SELECT COUNT(*) - 1 FROM NBPT_SP_PERSON E WHERE E.NBPT_SP_PERSON_DEPT_ID = A.NBPT_SP_PERSON_DEPT_ID AND E.NBPT_SP_PERSON_FLAG = '0') AS INFACT," + 
								"(SELECT COUNT(*) - 1 FROM NBPT_SP_PERSON F WHERE F.NBPT_SP_PERSON_DEPT_ID = A.NBPT_SP_PERSON_DEPT_ID) AS HISTORY " + 
								"FROM NBPT_SP_PERSON A " + 
								"LEFT JOIN NBPT_SP_REGION B " + 
								"ON A.NBPT_SP_PERSON_ID = B.NBPT_SP_REGION_RESPONSIBLER " + 
								"WHERE A.NBPT_SP_PERSON_JOB  = '2' AND " + 
								"	  EXISTS(" + 
								"		SELECT * FROM NBPT_SP_REGION_XZQX C " + 
								"		WHERE EXISTS(" + 
								"				  SELECT * FROM NBPT_SP_REGION_XZQX D " + 
								"					  WHERE D.NBPT_SP_REGION_XZQX_REGIONID = '" + person.getNBPT_SP_PERSON_DEPT_ID() + "' AND " + 
								"							C.NBPT_SP_REGION_XZQX_XZQXID = D.NBPT_SP_REGION_XZQX_XZQXID " + 
								"		      ) AND" + 
								"			  C.NBPT_SP_REGION_XZQX_REGIONID <> '" + person.getNBPT_SP_PERSON_DEPT_ID() + "'AND " + 
								"			  A.NBPT_SP_PERSON_DEPT_ID = C.NBPT_SP_REGION_XZQX_REGIONID 			 " + 
								"	  ) " + 
								"ORDER BY A.NBPT_SP_PERSON_ID ASC";
				
				List<CurrentPerson> resultList = this.query(sql, conn, CurrentPerson.class);
				
				return resultList;
			}
			
			// 如果是地区总
			else if("2".equals(person.getNBPT_SP_PERSON_JOB())) {

				// 查询该部门下,非离职人员
				String sql =    "SELECT " +
								"A.NBPT_SP_PERSON_NAME AS NAME , " +// 姓名
								"CASE A.NBPT_SP_PERSON_MALE " +
									"WHEN '0' THEN '女' " +
									"WHEN '1' THEN '男' " +
									"END AS SEX, " + // 性别
								"DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE()) AS AGE, "+ // 年龄
								"CASE A.NBPT_SP_PERSON_JOB " +
									"WHEN '3' THEN '区县总' " +
									"WHEN '4' THEN '预备区县总' " +
									"WHEN '5' THEN '推广经理' " +
									"END AS JOB, " + // 职务
								"A.NBPT_SP_PERSON_ENTRYDATA AS ENTRYDATA, " + // 入职时间
								"A.NBPT_SP_PERSON_MOB1 AS MOB1, " + // 联系电话
								"A.NBPT_SP_PERSON_MOB2 AS MOB2 " + // 紧急联络人
								"FROM NBPT_SP_PERSON A " +
								"LEFT JOIN NBPT_SP_REGION B " +
								"ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_ID " +
								"WHERE A.NBPT_SP_PERSON_JOB BETWEEN '3' AND '5' ORDER BY NBPT_SP_PERSON_ID ASC , NBPT_SP_PERSON_LOGINID DESC ";
				
				List<CurrentPerson> resultList = this.query(sql, conn, CurrentPerson.class);
				
				return resultList;
			} else {
				return new ArrayList<CurrentPerson>();
			}
			
		} catch (SQLException e) {
			
			log.info("查询当前登录用户下所有终端出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException {

		String sql = DBUtils.beanToSql(NBPT_SP_PERSON.class, "INSERT", "NBPT_SP_PERSON", person);

		try {
			
			this.excuteUpdate(sql.toString(), conn);
		} catch (SQLException e) {
			
			log.info("插入终端人员出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public List<NBPT_SP_REGION> getRegions(String level, Connection conn) throws SQLException {

		StringBuffer sql = new StringBuffer();
		
		if("3".equals(level)) {
			
			sql.append("SELECT * FROM NBPT_SP_REGION");
		} else {

			sql.append("SELECT * FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '" + level + "'");
		}
		try {
			
			List<NBPT_SP_REGION> resultList = this.query(sql.toString(), conn, NBPT_SP_REGION.class);
			return resultList;
		} catch (SQLException e) {
			log.info("获取部门列表出错" + CommonUtil.getTrace(e));
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
			
			log.info("添加管理区域出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentPerson> receiveCurrentPersonInfos(CurrentPerson nbpt_SP_PERSON, Connection connection) throws SQLException {
		
		List<CurrentPerson> persons = new ArrayList<>();
		

		try {
			
			// 如果登录人员信息为空,为后勤人员,查询所有人员以供统计
			if(null == nbpt_SP_PERSON) {
				
				
				String sql =    "	SELECT A.NBPT_SP_PERSON_JOB," + 
								"	A.NBPT_SP_PERSON_TYPE," + 
								"	B.NBPT_SP_REGION_NAME," + 
								"	B.NBPT_SP_REGION_ID,B.NBPT_SP_REGION_ONAME," + 
								"	B.NBPT_SP_REGION_LEVEL," + 
								"	C.NBPT_SP_REGION_NAME AS REGION_NAME," + 
								"	C.NBPT_SP_REGION_ONAME AS REGION_ONAME" + 
								"	FROM NBPT_SP_PERSON A " + 
								"	LEFT JOIN NBPT_SP_REGION B " + 
								"	ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_UID " + 
								"	LEFT JOIN NBPT_SP_REGION C " + 
								"	ON SUBSTRING(B.NBPT_SP_REGION_ID,1 ,2) = C.NBPT_SP_REGION_ID " + 
								"	WHERE A.NBPT_SP_PERSON_DEPT_ID IS NOT NULL AND A.NBPT_SP_PERSON_DEPT_ID <> '' " + 
								"	AND (A.NBPT_SP_PERSON_FLAG = '3' OR A.NBPT_SP_PERSON_FLAG = '2')";
				persons = this.query(sql, connection, CurrentPerson.class);
			} 
			
			// 如果登录人员的JOB是22,则是地总,查询该地总下的所有人员
			else if("22".equals(nbpt_SP_PERSON.getNBPT_SP_PERSON_JOB()) || "26".equals(nbpt_SP_PERSON.getNBPT_SP_PERSON_JOB())){
				
				String sql ="  SELECT A.NBPT_SP_PERSON_NAME,A.NBPT_SP_PERSON_TYPE," + 
							"  A.NBPT_SP_PERSON_MOB1,A.NBPT_SP_PERSON_MOB2,A.NBPT_SP_PERSON_JOB," + 
							"  CASE A.NBPT_SP_PERSON_MALE     " + 
							"	WHEN '0' THEN '女'     " + 
							"	WHEN '1' THEN '男'     " + 
							"	END AS NBPT_SP_PERSON_MALE," + 
							"  A.NBPT_SP_PERSON_ENTRYDATA," + 
							"  A.NBPT_SP_PERSON_PLACE," + 
							"  A.NBPT_SP_PERSON_DEGREE," + 
							"  A.NBPT_SP_PERSON_PID," + 
							"  DATEDIFF(yy,A.NBPT_SP_PERSON_ENTRYDATA,GETDATE()) AS NBPT_SP_PERSON_WORKAGE," + 
							"  DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE()) AS NBPT_SP_PERSON_AGE " + 
							"  FROM NBPT_SP_PERSON A " + 
							"  WHERE A.NBPT_SP_PERSON_DEPT_ID = '" + nbpt_SP_PERSON.getNBPT_SP_PERSON_DEPT_ID() + "' " + 
							"  AND A.NBPT_SP_PERSON_PID <> '" + nbpt_SP_PERSON.getNBPT_SP_PERSON_PID() + "' " + 
							"  AND A.NBPT_SP_PERSON_FLAG <> '3'" +
							
							"  ORDER BY A.NBPT_SP_PERSON_ID ";
	
				persons = this.query(sql, connection, CurrentPerson.class);
			}
			
			// 最后大区总登录时,查询所有的地总信息
			else if("21".equals(nbpt_SP_PERSON.getNBPT_SP_PERSON_JOB()) || "26".equals(nbpt_SP_PERSON.getNBPT_SP_PERSON_JOB())){
				String sql = "SELECT A.*,DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE()) AS NBPT_SP_PERSON_AGE "
							+ "FROM NBPT_SP_PERSON A "
							+ "LEFT JOIN NBPT_SP_REGION B "
							+ "ON A.NBPT_SP_PERSON_PID = B.NBPT_SP_REGION_RESPONSIBLER "
							+ "WHERE NBPT_SP_REGION_ID LIKE "
							+ "  ("
							+ "    SELECT NBPT_SP_REGION_ID FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_RESPONSIBLER = '" + nbpt_SP_PERSON.getNBPT_SP_PERSON_PID() + "') + '%'"
							+ "  )";
				persons = this.query(sql, connection, CurrentPerson.class);
			}
			return persons;
		} catch (SQLException e) {
			
			log.info("添加管理区域出错" + CommonUtil.getTrace(e));
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
						"  AND A.NBPT_SP_REGION_XZQX_TYPE = '22'";
			
			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
			return resultList;
		} catch (SQLException e) {
			
			log.info("添加管理区域出错" + CommonUtil.getTrace(e));
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
			
			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
		
			return resultList;
		} catch (SQLException e) {
			
			log.info("AJAX获取管理区县出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> getTerminalResponsXzqx(String terminalId, Connection connection) throws SQLException {


		try {
			String sql =  "  SELECT A.*" + 
							"  FROM NBPT_COMMON_XZQXHF A" + 
							"  LEFT JOIN NBPT_SP_PERSON_XZQX B" + 
							"  ON A.NBPT_COMMON_XZQXHF_ID = B.NBPT_SP_PERSON_XZQX_XID" + 
							"  LEFT JOIN NBPT_SP_PERSON C" + 
							"  ON B.NBPT_SP_PERSON_XZQX_PID = C.NBPT_SP_PERSON_ID" + 
							"  WHERE C.NBPT_SP_PERSON_PID = '"+ terminalId +"'";
			
			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
		
			return resultList;
		} catch (SQLException e) {
			
			log.info("AJAX获取管理区县出错" + CommonUtil.getTrace(e));
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
			
			log.info("添加终端人员的部门信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public NBPT_SP_REGION getTerminalDeptInfo(String terminalId, Connection connection) throws SQLException {

		try {
			String sql =  "  SELECT A.*" + 
							"  FROM NBPT_COMMON_XZQXHF A" + 
							"  LEFT JOIN NBPT_SP_PERSON_XZQX B" + 
							"  ON A.NBPT_COMMON_XZQXHF_ID = B.NBPT_SP_PERSON_XZQX_XID" + 
							"  LEFT JOIN NBPT_SP_PERSON C" + 
							"  ON B.NBPT_SP_PERSON_XZQX_PID = C.NBPT_SP_PERSON_ID" + 
							"  WHERE C.NBPT_SP_PERSON_PID = '"+ terminalId +"'";
			
			NBPT_SP_REGION resultList = this.oneQuery(sql, connection, NBPT_SP_REGION.class);
		
			return resultList;
		} catch (SQLException e) {
			
			log.info("获取终端人员的部门信息" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public CurrentPerson receiveCurrentTerminal(String changePersonPid, Connection connection) throws SQLException {

		try {
			String sql =  "  SELECT * "
						+ "  FROM NBPT_SP_PERSON "
						+ "  WHERE NBPT_SP_PERSON_PID = '" + changePersonPid + "'";
			
			CurrentPerson person;
			person = this.oneQuery(sql, connection, CurrentPerson.class);
			return person;
		} catch (SQLException e) {
			
			log.info("获取终端人员信息出错" + CommonUtil.getTrace(e));
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
			log.info("更新终端人员信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> checkPlace(String string, Connection connection) throws SQLException {

		try {
			String sql = "  SELECT B.*" + 
						"  FROM NBPT_COMMON_XZQXHF A" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF B" + 
						"  ON A.NBPT_COMMON_XZQXHF_PID = B.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE A.NBPT_COMMON_XZQXHF_ID = '" + string + "'";
			List<NBPT_COMMON_XZQXHF> placeInfo = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
			return placeInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			log.info("更新终端人员信息出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}
	
	
}
