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
	public NBPT_SP_PERSON receiveCurrentPerson(String userId, Connection conn) throws SQLException {
		
		String sql = "SELECT * FROM NBPT_SP_PERSON WHERE NBPT_SP_PERSON_LOGINID = '" + userId + "'";
		
		try {
			
			NBPT_SP_PERSON resultList = this.oneQuery(sql, conn, NBPT_SP_PERSON.class);
			
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
	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer("SELECT * FROM NBPT_COMMON_XZQXHF ");
		
		if(null == parentId || "".equals(parentId)) {
			
			sql.append("WHERE NBPT_COMMON_XZQXHF_PID = '1' ");
		} else {
			
			sql.append("WHERE NBPT_COMMON_XZQXHF_PID = '" + parentId + "' ");
		}
		
		sql.append("ORDER BY NBPT_COMMON_XZQXHF_ID ASC ");
		try {
			
			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql.toString(), conn, NBPT_COMMON_XZQXHF.class);
			
			return resultList;
		} catch (SQLException e) {
			
			log.info("查询行政区县划分出错" + CommonUtil.getTrace(e));
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
	public List<CurrentPerson> receiveCurrentTerminals(String deptId, Connection conn) throws SQLException {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public List<NBPT_SP_REGION> getRegions(Object object, Connection conn) throws SQLException {

		String sql = "SELECT * FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1'";
		try {
			List<NBPT_SP_REGION> resultList = this.query(sql, conn, NBPT_SP_REGION.class);
			return resultList;
		} catch (SQLException e) {
			log.info("获取大区列表出错" + CommonUtil.getTrace(e));
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
	public List<CurrentPerson> receiveCurrentPersonInfos(NBPT_SP_PERSON nbpt_SP_PERSON, Connection connection) throws SQLException {
		
		List<CurrentPerson> persons = new ArrayList<>();
		

		try {
			
			// 如果登录人员信息为空,为后勤人员,查询所有大区总
			if(null == nbpt_SP_PERSON) {
				
				
				String sql =  "SELECT A.*,B.NBPT_SP_REGION_NAME,"
							+ "DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE()) AS NBPT_SP_PERSON_AGE "
							+ "FROM NBPT_SP_PERSON A "
							+ "LEFT JOIN NBPT_SP_REGION B "
							+ "ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_ID ";
				persons = this.query(sql, connection, CurrentPerson.class);
			} 
			
			// 如果登录人员的JOB是2,则是地总,查询该地总下的所有人员
			else if("22".equals(nbpt_SP_PERSON.getNBPT_SP_PERSON_JOB())){
				
				String sql =  "SELECT A.NBPT_SP_PERSON_NAME,C.NBPT_COMMON_DICTIONARY_KEY_NAME AS NBPT_SP_PERSON_TYPE,"
							+ "A.NBPT_SP_PERSON_MOB1,A.NBPT_SP_PERSON_MOB2,A.NBPT_SP_PERSON_JOB,"
							+ "CASE A.NBPT_SP_PERSON_MALE "
							+ "    WHEN '0' THEN '女' "
							+ "    WHEN '1' THEN '男' "
							+ "    END AS NBPT_SP_PERSON_MALE,"
							+ "A.NBPT_SP_PERSON_ENTRYDATA,A.NBPT_SP_PERSON_PLACE,A.NBPT_SP_PERSON_DEGREE,"
							+ "DATEDIFF(yy,A.NBPT_SP_PERSON_ENTRYDATA,GETDATE()) AS NBPT_SP_PERSON_WORKAGE,"
							+ "DATEDIFF(yy,CONVERT(datetime,SUBSTRING(A.NBPT_SP_PERSON_IDNUM,7,8),112),GETDATE()) AS NBPT_SP_PERSON_AGE "
							+ "FROM NBPT_SP_PERSON A "
							+ "LEFT JOIN NBPT_SP_REGION B "
							+ "ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_ID "
							+ "LEFT JOIN NBPT_COMMON_DICTIONARY C "
							+ "ON A.NBPT_SP_PERSON_TYPE = C.NBPT_COMMON_DICTIONARY_KEY_VALUE "
							+ "WHERE B.NBPT_SP_REGION_RESPONSIBLER = '" + nbpt_SP_PERSON.getNBPT_SP_PERSON_PID() + "' "
							+ "AND C.NBPT_COMMON_DICTIONARY_KEY = 'SAILTYPE'";
	
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
	
			String sql =  "SELECT A.NBPT_SP_REGION_NAME AS AREANAME, A.NBPT_SP_REGION_ID AS AREAID "
						+ "FROM NBPT_SP_REGION A "
						+ "LEFT JOIN NBPT_SP_PERSON_REGION B "
						+ "ON B.NBPT_SP_PERSON_REGION_RID = A.NBPT_SP_REGION_UID "
						+ "LEFT JOIN NBPT_SP_PERSON C "
						+ "ON B.NBPT_SP_PERSON_REGION_PID = C.NBPT_SP_PERSON_PID "
						+ "WHERE C.NBPT_SP_PERSON_LOGINID = '" + loginId + "'";
			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
			return resultList;
		} catch (SQLException e) {
			
			log.info("添加管理区域出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
		
	}

	@Override
	public String receiveTerminalDeptId(List<NBPT_SP_PERSON_XZQX> reposAreas, Connection connection) throws SQLException {

		try {
			StringBuffer sql = new StringBuffer(  "SELECT DISTINCT NBPT_SP_REGION_XZQX_REGIONID "
												+ "FROM NBPT_SP_REGION_XZQX "
												+ "WHERE NBPT_SP_REGION_XZQX_XZQXID IN (");
			for(int i = 0; i < reposAreas.size(); i++){
				if(i == reposAreas.size() - 1) {
	
					sql.append(reposAreas.get(i).getNBPT_SP_PERSON_XZQX_XID() + ")");
				}else {
	
					sql.append(reposAreas.get(i).getNBPT_SP_PERSON_XZQX_XID() + ",");
				}
			}
			
			List<CurrentPerson> TerminalDeptId = this.query(sql.toString(), connection, CurrentPerson.class);
			
			if(TerminalDeptId.size() == 1) {

				return TerminalDeptId.get(0).getNBPT_SP_REGION_XZQX_REGIONID();
			} else {
				
				return "";
			}
		} catch (SQLException e) {
			
			log.info("添加管理区域出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public void addPersonRegion(String nbpt_SP_PERSON_REGION, Connection connection) {
		// TODO 自动生成的方法存根
		
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
	
	
}
