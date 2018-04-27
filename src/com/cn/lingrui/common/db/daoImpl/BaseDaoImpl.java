package com.cn.lingrui.common.db.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.common.db.dbpojos.BaseReport;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;

public class BaseDaoImpl implements BaseDao{


	private static Logger log = LogManager.getLogger();
	
	/**
	 * 批量执行更新操作,更新操作包括insert和update
	 * @param sqls
	 * @param stmt
	 * @throws SQLException
	 */
	public void excuteUpdateGroups(List<String> sqls, Connection connection) throws SQLException {
		
		Statement stmt;
		stmt = connection.createStatement();
	
	
		for(String sql : sqls) {

			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	
	/**
	 * 单条插入,更新或删除
	 * @param sql
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public void excuteUpdate(String sql, Connection connection)  throws SQLException {
		
		PreparedStatement ps;
		try {
			
			ps = connection.prepareStatement(sql);
		
			ps.executeUpdate();

			DBUtils.closePsRs(ps, null);
			
		} catch (SQLException e) {

			log.error("执行单条插入,更新或删除出错" + CommonUtil.getTrace(e));
			throw new SQLException() ;
		}
	}
	
	/**
	 * 通用查询方法,根据对应的bean生成sql,查询所有
	 * @param sql
	 * @param connection
	 * @return
	 * @throws SQLException 执行查询是的异常
	 */
	public <T> List<T> queryForClaszs(String sql, Connection connection, Class<T> classz) throws SQLException {
		
		PreparedStatement ps = null;
				ResultSet rs = null;
			try {
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
		
				return DBUtils.rsToBean(classz, rs);
			} catch (SQLException e) {

				log.error("通用查询出错" + CommonUtil.getTraceInfo());
				throw new SQLException();
			}
	}
	
	/**
	 * 通用查询方法,根据对应的bean生成sql,查询单个
	 * @param sql
	 * @param connection
	 * @return
	 * @throws SQLException 执行查询是的异常
	 */
	public <T> T oneQueryForClasz(String sql, Connection connection, Class<T> classz) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			List<T> resultList = DBUtils.rsToBean(classz, rs);
			return resultList.size() == 0 ? null : resultList.get(0);
		} catch (Exception e) {
	
			log.error("通用查询出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		} 
	}
	
	/**
	 * 通用查询方法,查询批量
	 * @param sql
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<String> queryForObjects(String sql, Connection connection) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			List<String> results = DBUtils.rsToString(rs);
			return results;
		} catch (Exception e) {
	
			log.error("通用查询出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		} 
	}
	
	/**
	 * 通用查询方法,查询单个字段
	 * @param sql
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public String oneQueryForObject(String sql, Connection connection) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			List<String> results = DBUtils.rsToString(rs);
			return results.size() == 0 ? null : results.get(0);
		} catch (Exception e) {
	
			log.error("通用查询出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		} 
	}
	
	/**
	 * 调用存储过程
	 * @param procName 存储过程名称
	 * @param connection 数据库连接
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet callProcedure(String procName, Connection connection, String... params) throws SQLException {
		
		CallableStatement cs = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		try {
			
			// 拼接执行语句
			sql.append("{call " + procName + "(");
			
			if(0 == params.length) {
				sql.append(")}");
			} else {
				for(int i = 0 ; i < params.length ; i++) {
					
					sql.append("?,");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(")}");
				
			}
			
			// 预执行
			cs = connection.prepareCall(sql.toString());
			
			// 如果有参数,则填入相关参数
			if(0 != params.length) {
				
				for(int i = 0 ; i < params.length ; i++) {
					
					cs.setString(i + 1, params[i]);
				}
			} 
			cs.executeQuery();

			rs = cs.getResultSet();
			return rs;
		} catch (SQLException e) {
			
			log.error("调用存储过程: " + procName + "出错");
			throw new SQLException() ;
		}	
	}

	@Override
	public String receiveMaxId(Connection connection, String tableName) throws SQLException {
		
		String maxId = null;
		try {
			ResultSet rs = this.callProcedure("RECEIVEMAXID", connection, tableName);
			while(rs.next()) {
				maxId = rs.getString("MAXID");
			}
			DBUtils.closePsRs(null, rs);
			
			return maxId;
		} catch (Exception e) {
			
			log.error("获取" + tableName + "最大标号出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}
	
	@Override
	public String receiveMaxId(Connection connection, String tableName, String where) throws SQLException {
		
		String maxId = null;
		try {
			ResultSet rs = this.callProcedure("RECEIVEMAXID_W", connection, tableName, where);
			while(rs.next()) {
				maxId = rs.getString("MAXID");
			}
			DBUtils.closePsRs(null, rs);
			
			return maxId;
		} catch (Exception e) {
			
			log.error("获取" + tableName + "最大标号出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_COMMON_DICTIONARY> receiveDictionarys(String type, Connection connection, String... level) throws SQLException {
		
		String sql = null;
		if(0 == level.length) {
			sql = "SELECT * FROM NBPT_COMMON_DICTIONARY WHERE NBPT_COMMON_DICTIONARY_KEY = '" + type + 
					"' ORDER BY NBPT_COMMON_DICTIONARY_ID ASC";
		} else {
			sql = "SELECT * FROM NBPT_COMMON_DICTIONARY WHERE NBPT_COMMON_DICTIONARY_KEY = '" + type + 
					"' AND NBPT_COMMON_DICTIONARY_KEY_LEVEL = '" + level[0] + 
					"' ORDER BY NBPT_COMMON_DICTIONARY_ID ASC";
		}

		try {
			
			List<NBPT_COMMON_DICTIONARY> resultList = this.queryForClaszs(sql, connection, NBPT_COMMON_DICTIONARY.class);
						
			return resultList;
		} catch (Exception e) {
			
			log.error("获取字典表出错");
			throw new SQLException();
		}
	}
	
	@Override
	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection connection) throws SQLException {
		
		StringBuffer sql = new StringBuffer("SELECT * FROM NBPT_COMMON_XZQXHF ");
		
		// 如果是省份列表
		if(null == parentId || "".equals(parentId)) {
			
			sql.append("WHERE NBPT_COMMON_XZQXHF_PID = '1' ");
		} 
		
		// 非省份列表,要根据上级ID查询
		else {
			
			sql.append("WHERE NBPT_COMMON_XZQXHF_PID = '" + parentId + "' ");
		}
		
		sql.append("ORDER BY NBPT_COMMON_XZQXHF_ID ASC ");
		try {
			
			List<NBPT_COMMON_XZQXHF> resultList = this.queryForClaszs(sql.toString(), connection, NBPT_COMMON_XZQXHF.class);
			
			return resultList;
		} catch (SQLException e) {
			
			log.error("查询行政区县划分出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}
	}

	@Override
	public <T> void insertForClasz(Class<T> clasz, T t, Connection connection) throws SQLException {

		try {
			
			@SuppressWarnings("unchecked")
			String sql = DBUtils.beanToSql(clasz, "insert", clasz.getSimpleName(), t);
			this.excuteUpdate(sql, connection);
		} catch (SQLException e) {

			log.error("通用单条插入出错");
			throw new SQLException();
		}
	}

	@Override
	public <T> void insertForClaszs(Class<T> clasz, List<T> ts, Connection connection) throws SQLException {
		
		try {
			
			List<String> sqls = new ArrayList<>();
			for(T t : ts) {

				@SuppressWarnings("unchecked")
				String sql = DBUtils.beanToSql(clasz, "insert", clasz.getSimpleName(), t);
				sqls.add(sql);
			}
			this.excuteUpdateGroups(sqls, connection);
		} catch (SQLException e) {

			log.error("通用批量插入出错");
			throw new SQLException();
		}
	}
	
	/**
	 * 报表数据公共处理方法
	 * 
	 * @param report
	 * @param rs
	 * @return
	 */
	@Override
	public BaseReport dealReportData(BaseReport report, ResultSet rs) {

		try {

			ResultSetMetaData rsMetaData = rs.getMetaData();

			// 初始化查询结果的列名
			List<String> columnNames = new ArrayList<String>();
			
			// 初始化划定顺序列名
			String[] titles = report.getTitle();
			
			int columnCount = rsMetaData.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {

				columnNames.add(rsMetaData.getColumnName(i).toUpperCase());
			}

			// 设定每行数据
			while (rs.next()) {

				List<String> rowList = new ArrayList<String>();

				for (String title : titles) {
					
					if(columnNames.contains(title)) {
						
						rowList.add(rs.getString(title));
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
