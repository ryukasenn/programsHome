package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.pojos.support.AttendanceForm;
import com.cn.lingrui.sellPersonnel.pojos.support.EvaluationForm;

public interface SupportDao extends SellPersonnelBaseDao{

	/**
	 * 获取所有已登记人员信息,除信息专员外
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveAllPersons(Connection conn) throws SQLException;
	
	/**
	 * 查询考核报表
	 * @param endTime 截止时间
	 * @return
	 * @throws SQLException 
	 */
	public EvaluationForm receiveEvalutionForm(String endTime, Connection conn) throws SQLException;
	
	/**
	 * 获取考勤报表
	 * @return
	 * @throws SQLException 
	 */
	public AttendanceForm receiveAttendance(String regionUid, Connection conn) throws SQLException;

	/**
	 * 删除指定终端
	 * @param terminalPid
	 * @throws SQLException 
	 */
	public void deleteTerminal(String terminalPid, Connection conn) throws SQLException;
	
	/**
	 * 获取未审核列表
	 * @param userId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveUnchecks(Connection conn) throws SQLException;

	/**
	 * 获取指定未审核人员信息
	 * @param uncheckpid
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_SP_PERSON receiveUncheck(String uncheckpid, Connection conn) throws SQLException;


}





