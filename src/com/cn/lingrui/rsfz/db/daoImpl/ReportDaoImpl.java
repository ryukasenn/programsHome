package com.cn.lingrui.rsfz.db.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.GZBMZD;
import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.rsfz.db.dao.ReportDao;
import com.cn.lingrui.rsfz.pojos.report.AgeReport;
import com.cn.lingrui.rsfz.pojos.report.BaseReport;
import com.cn.lingrui.rsfz.pojos.report.SexReport;
import com.cn.lingrui.rsfz.pojos.report.WorkAgeReport;
import com.cn.lingrui.rsfz.pojos.report.WorkLevelReport;
import com.cn.lingrui.rsfz.pojos.report.XlReport;

@Repository("reportDao")
public class ReportDaoImpl extends BaseDaoImpl implements ReportDao {

	private static Logger log = LogManager.getLogger();

	/**
	 * 获取所有员工信息
	 * @throws SQLException 
	 */
	@Override
	public List<PMBASE> getReportData(Connection conn) throws SQLException {

		// 初始化返回数据
		List<PMBASE> listPMBASE = new ArrayList<PMBASE>();

		// 生成查询语句
		String sql = DBUtils.beanToSql(PMBASE.class, "SELECT", "PMBASE");
		if (!"".equals(sql)) {

			try {

				listPMBASE = query(sql + "order by PMBASE_ZGBH DESC", conn, PMBASE.class);

			} catch (SQLException e) {

				log.info("查询错误" + e.getMessage());
				throw new SQLException();
			}
		}
		return listPMBASE;
	}

	/**
	 * 获取部门信息列表
	 * @throws SQLException 
	 */
	@Override
	public List<GZBMZD> getBmData(Connection conn) throws SQLException {

		// 初始化返回数据
		List<GZBMZD> result = new ArrayList<GZBMZD>();

		// 生成查询语句
		String sql = "select * from GZBMZD where GZBMZD_JS = '1' and GZBMZD_MX = '0'";
		try {

			result = query(sql, conn, GZBMZD.class);
			return result;

		} catch (Exception e) {

			log.info("查询错误" + e.getMessage());
			throw new SQLException();
		}
	}

	/**
	 * 获取年龄构成报表
	 * 
	 * @param procName
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public AgeReport getAgeReport(String procName, Connection conn, AgeReport result) throws SQLException {


		// 初始化返回参数 = new AgeReport();
		ResultSet rs = this.callProcedure(procName, conn);
		dealReportData(result, rs);
		return result;
	}

	/**
	 * 获取性别构成报表
	 * 
	 * @param procName
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public SexReport getSexReport(String procName, Connection conn) throws SQLException {

		// 初始化返回参数
		SexReport result = new SexReport();
		ResultSet rs = this.callProcedure(procName, conn);
		dealReportData(result, rs);
		return result;

	}

	/**
	 * 获取学历构成报表
	 * 
	 * @param procName
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public XlReport getXlReport(String procName, Connection conn) throws SQLException {

		// 初始化返回参数
		XlReport result = new XlReport();
		ResultSet rs = this.callProcedure(procName, conn);
		dealReportData(result, rs);
		return result;
	}

	/**
	 * 获取职务级别构成报表
	 * @param procName
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public WorkLevelReport getWorkLevelReport(String procName, Connection conn) throws SQLException {
		
		WorkLevelReport result = new WorkLevelReport();
		ResultSet rs = this.callProcedure(procName, conn);
		dealReportData(result, rs);
		return result;
	}

	/**
	 * 获取工龄构成报表
	 * @param procName
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public WorkAgeReport getWorkAgeReport(String procName, Connection conn, WorkAgeReport result) throws SQLException {

		ResultSet rs = this.callProcedure(procName, conn);
		dealReportData(result, rs);
		return result;
	}
	/**
	 * 报表数据公共处理方法
	 * 
	 * @param report
	 * @param rs
	 * @return
	 */
	private BaseReport dealReportData(BaseReport report, ResultSet rs, BaseReport... reports) {

		try {

			ResultSetMetaData rsMetaData = rs.getMetaData();

			// 初始化查询结果的列名
			List<String> columnNames = new ArrayList<String>();
			List<String> title = new ArrayList<>();
			// 获取列名
			if(0 == reports.length) {
				
				title = report.getTitle();
			} else {
				
				title = reports[0].getTitle();
			}

			int columnCount = rsMetaData.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {

				columnNames.add(rsMetaData.getColumnName(i).toUpperCase());
			}

			if (columnNames.size() - 1 > title.size()) {

				return report;
			} else {

				List<String> deleteTitleName = new ArrayList<String>();
				
				// 重新设定title
				for(String s : title) {
					
					boolean flag = true;
					for (String g : columnNames) {
						
						if(s.equals(g)) {
							
							flag = false;
							break;
						}
					}
					
					if(flag) {
						
						deleteTitleName.add(s);
					}
				}
				
				// 完成title变化
				for(String s : deleteTitleName) {
					
					title.remove(s);
				}
				
			}

			// 设定每行数据
			while (rs.next()) {

				List<String> rowList = new ArrayList<String>();

				for (int i = 0; i < title.size(); i++) {
					
					for (int k = 0; k < columnNames.size(); k++) {

						if (title.get(i).equals(columnNames.get(k))) {

							rowList.add(rs.getString(columnNames.get(k)));
							
							break;
						} 
					}
				}
				report.getReportData().add(rowList);
			}
		} catch (Exception e) {

			log.info(CommonUtil.getTraceInfo());
		}
		return report;
	}


}
