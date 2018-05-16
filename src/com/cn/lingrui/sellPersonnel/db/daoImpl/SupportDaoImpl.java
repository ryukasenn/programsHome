package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.pojos.support.AttendanceForm;
import com.cn.lingrui.sellPersonnel.pojos.support.EvaluationForm;


@Repository("supportDao")
public class SupportDaoImpl extends SellPersonnelBaseDaoImpl implements SupportDao{

	private static Logger log = LogManager.getLogger();
	
	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receiveAllPersons(Connection conn) throws SQLException {
		
		try {
			List<NBPT_VIEW_CURRENTPERSON> resultList = new ArrayList<>();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT A.* ");
			sql.append("FROM NBPT_VIEW_CURRENTPERSON A ");
			sql.append("WHERE A.NBPT_SP_PERSON_JOB <> '27' ");
			sql.append("ORDER BY A.NBPT_SP_PERSON_TYPE,A.NBPT_SP_PERSON_ID");
			resultList = this.queryForClaszs(sql.toString(), conn, NBPT_VIEW_CURRENTPERSON.class);
			return resultList;
		} catch (SQLException e) {
	
			log.error("后勤查询所有人员信息,除信息专员外出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public EvaluationForm receiveEvalutionForm(String endTime, Connection conn) throws SQLException {

		
		try {
			EvaluationForm resultList = new EvaluationForm();
						
			this.dealReportData(resultList, this.callProcedure("NBPT_PROCEDURE_EVALUATION_FORM", conn, endTime));
			return resultList;
		} catch (SQLException e) {
	
			log.error("后勤查询考勤出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public AttendanceForm receiveAttendance(String regionUid, Connection conn) throws SQLException {
		
		try {
			
			AttendanceForm resultList = new AttendanceForm();
			
			this.dealReportData(resultList, this.callProcedure("NBPT_PROCEDURE_ATTENDANCE_FORM", conn, regionUid));
			return resultList;
		} catch (SQLException e) {
	
			log.error("后勤查询考勤出错" + CommonUtil.getTraceInfo());
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
	public List<NBPT_VIEW_CURRENTPERSON> receiveUnchecks(Connection conn) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_VIEW_CURRENTPERSON A ");
		sql.append("WHERE A.NBPT_SP_PERSON_FLAG <> '3' AND A.NBPT_SP_PERSON_FLAG <> '2' "); // 非离职,非在职
		
		
		try {
			
			List<NBPT_VIEW_CURRENTPERSON> resultList = this.queryForClaszs(sql, conn, NBPT_VIEW_CURRENTPERSON.class);
			
			return resultList;
			
		} catch (SQLException e) {
			
			log.error("查询未审核列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON receiveUncheck(String uncheckpid, Connection conn) throws SQLException {

		String sql =    "  SELECT " + 
						"  A.* " + 
						"  FROM NBPT_SP_PERSON A" + 
						"  WHERE A.NBPT_SP_PERSON_PID = '" + uncheckpid + "'";
		
		try {
			
			NBPT_SP_PERSON personInfo = this.oneQueryForClasz(sql, conn, NBPT_SP_PERSON.class);
			
			return personInfo;
			
		} catch (SQLException e) {
			
			log.error("查询指定未审核人员信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void supportAddPerson(NBPT_SP_PERSON person, Connection conn) throws SQLException {

		StringBuffer sql = new StringBuffer(DBUtils.beanToSql(NBPT_SP_PERSON.class, "insert", "NBPT_SP_PERSON", person));
				
		try {
			
			this.excuteUpdate(sql , conn);;
			
		} catch (SQLException e) {
			
			log.error("插入新的大区总地总出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

}

























