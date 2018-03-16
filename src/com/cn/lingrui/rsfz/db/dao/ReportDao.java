package com.cn.lingrui.rsfz.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.GZBMZD;
import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.rsfz.pojos.report.AgeReport;
import com.cn.lingrui.rsfz.pojos.report.SexReport;
import com.cn.lingrui.rsfz.pojos.report.WorkAgeReport;
import com.cn.lingrui.rsfz.pojos.report.WorkLevelReport;
import com.cn.lingrui.rsfz.pojos.report.XlReport;

public interface ReportDao {

	public List<PMBASE> getReportData(Connection conn)throws SQLException;
	public List<GZBMZD> getBmData(Connection conn)throws SQLException;
	public XlReport getXlReport(String procName,Connection conn)throws SQLException;
	public SexReport getSexReport(String procName, Connection conn)throws SQLException;
	public WorkLevelReport getWorkLevelReport(String procName, Connection conn)throws SQLException;
	public AgeReport getAgeReport(String procName, Connection conn, AgeReport result) throws SQLException;
	public WorkAgeReport getWorkAgeReport(String string, Connection connection, WorkAgeReport result)throws SQLException;
}
