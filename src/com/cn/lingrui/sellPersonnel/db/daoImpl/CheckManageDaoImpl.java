package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.CheckManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.UncheckPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.Province;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.Region;

@Repository("checkManageDao")
public class CheckManageDaoImpl extends BaseDaoImpl implements CheckManageDao {

	private static Logger log = LogManager.getLogger();

	@Override
	public List<UncheckPerson> receiveUnchecks(String userId, Connection conn) throws SQLException {
		
		String sql =    "	SELECT " + 
						"	A.NBPT_SP_PERSON_NAME AS UNCHECKNAME," + 
						"	A.NBPT_SP_PERSON_ID AS UNCHECKID," + 
						"	A.NBPT_SP_PERSON_PID AS UNCHECKPID," + 
						"	B.NBPT_SP_REGION_UID AS UNCHECKDEPTUID," + 
						"	B.NBPT_SP_REGION_ID AS UNCHECKDEPTID," + 
						"	B.NBPT_SP_REGION_NAME AS UNCHECKDEPTNAME," + 
						"	B.NBPT_SP_REGION_RESPONSIBLER AS UNCHECKAREABELONGTOID," + 
						"	D.NBPT_SP_PERSON_NAME AS UNCHECKAREABELONGTONAME" + 
						"	FROM NBPT_SP_PERSON A" + 
						"	LEFT JOIN NBPT_SP_REGION B" + 
						"	ON A.NBPT_SP_PERSON_DEPT_ID = B.NBPT_SP_REGION_UID" + 
						"	LEFT JOIN NBPT_SP_PERSON C" + 
						"	ON B.NBPT_SP_REGION_ID LIKE C.NBPT_SP_PERSON_DEPT_ID + '%'" + 
						"	LEFT JOIN NBPT_SP_PERSON D" + 
						"	ON B.NBPT_SP_REGION_RESPONSIBLER = D.NBPT_SP_PERSON_PID" + 
						"	WHERE C.NBPT_SP_PERSON_LOGINID = '" + userId + "'" + 
						"	AND A.NBPT_SP_PERSON_FLAG = '0'";
		
		try {
			
			List<UncheckPerson> resultList = this.query(sql, conn, UncheckPerson.class);
			
			return resultList;
			
		} catch (SQLException e) {
			
			log.info("查询信息专员下未审核列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON receiveUncheck(String uncheckpid, Connection conn) throws SQLException {

		String sql =    "	SELECT " + 
						"	A.* " + 
						"	FROM NBPT_SP_PERSON A" + 
						"	WHERE A.NBPT_SP_PERSON_PID = '" + uncheckpid + "'";
		
		try {
			
			NBPT_SP_PERSON personInfo = this.oneQuery(sql, conn, NBPT_SP_PERSON.class);
			
			return personInfo;
			
		} catch (SQLException e) {
			
			log.info("查询信息专员下未审核列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> receiveUncheckResponsAreas(String uncheckpid, Connection connection) throws SQLException {

		String sql =    "  SELECT C.*" + 
						"  FROM NBPT_SP_PERSON_XZQX A" + 
						"  LEFT JOIN NBPT_SP_PERSON B" + 
						"  ON A.NBPT_SP_PERSON_XZQX_PID = B.NBPT_SP_PERSON_ID" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF C" + 
						"  ON A.NBPT_SP_PERSON_XZQX_XID = C.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE B.NBPT_SP_PERSON_PID = '" + uncheckpid + "'";
		try {
			
			
			List<NBPT_COMMON_XZQXHF> respons = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);
			
			return respons;
		} catch (SQLException e) {
			
			log.info("查询信息专员下未审核人员负责区域" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}
	
	

}























