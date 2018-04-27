package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.BaseReport;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;

public interface BaseDao {
	
	/**
	 * 多条更新插入及删除
	 * @param sqls
	 * @param conn
	 * @throws SQLException 
	 */
	public void excuteUpdateGroups(List<String> sqls, Connection conn) throws SQLException;
	
	/**
	 * 单条更新插入及删除
	 * @param sql
	 * @param conn
	 * @return
	 */
	public void excuteUpdate(String sql, Connection conn) throws SQLException;
	
	/**
	 * 单条插入语句实现
	 * @param clasz
	 * @param t
	 * @param connection
	 * @throws SQLException
	 */
	public <T> void insertForClasz(Class<T> clasz, T t, Connection connection) throws SQLException;
	
	/**
	 * 多条更新插入及删除
	 * @param sqls
	 * @param conn
	 * @throws SQLException 
	 */
	public <T> void insertForClaszs(Class<T> clasz, List<T> t, Connection conn) throws SQLException;
	/**
	 * 基类查询方法
	 * @param sql sql语句
	 * @param conn 数据库连接
	 * @param classz 查询后生成对应的bean
	 * @return
	 */
	public <T> List<T> queryForClaszs(String sql, Connection conn, Class<T> classz) throws SQLException;
	
	/**
	 * 调用存储过程
	 * @param procName 存储过程名
	 * @param conn 数据库连接
	 * @param params 带参存储过程
	 * @return
	 */
	public ResultSet callProcedure(String procName, Connection conn, String... params) throws SQLException;
	
	/**
	 * 获取表中ID最大数
	 * @return
	 */
	public String receiveMaxId(Connection conn, String params) throws SQLException;

	/**
	 * 获取表中ID最大数,带条件
	 * @return
	 */
	public String receiveMaxId(Connection conn, String tableName, String where) throws SQLException;
	/**
	 * 获取字典表数据
	 * @param type 字典类型
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_COMMON_DICTIONARY> receiveDictionarys(String type, Connection conn, String... level) throws SQLException;

	/**
	 * 获取国家行政区县列表
	 * @param parentId 上级区县的ID,如果为null或空,则是查省份,如果不为空,则查其下级
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection conn) throws SQLException;

	/**
	 * 报表处理方法
	 * @param report
	 * @param rs
	 * @param reports
	 * @return
	 */
	public BaseReport dealReportData(BaseReport report, ResultSet rs);
	

}
