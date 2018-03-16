package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;

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
	 * 基类查询方法
	 * @param sql sql语句
	 * @param conn 数据库连接
	 * @param classz 查询后生成对应的bean
	 * @return
	 */
	public <T> List<T> query(String sql, Connection conn, Class<T> classz) throws SQLException;
	
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
	public String receiveMaxId(String procName, Connection conn, String params) throws SQLException;
	
	/**
	 * 获取字典表数据
	 * @param type 字典类型
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_COMMON_DICTIONARY> receiveDictionarys(String type, Connection conn, String... level) throws SQLException;
}
