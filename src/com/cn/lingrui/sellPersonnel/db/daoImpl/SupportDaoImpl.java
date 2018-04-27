package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
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

}
