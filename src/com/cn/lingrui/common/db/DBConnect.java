package com.cn.lingrui.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.utils.GlobalParams;

public class DBConnect {

	private static Logger log = LogManager.getLogger();

	private Connection conn = null;

	/**
	 * 取数据库连接
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param db
	 *            可选参数,PARAM1:数据库指定IP,PARAM2:数据库指定库名,如果不指定,则默认cwmaster
	 * @throws SQLException 
	 */
	public DBConnect(String userName, String password, String... dbParams) throws SQLException {
		if (conn != null) {

		}
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			// 只传入用户名和密码
			if (dbParams.length == 0) {

				conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwmaster", "cw001" + userName, password);
			} 
			
			// 加数据库名
			else if(dbParams.length == 1){

				conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + dbParams[0], userName, password);
			} 
			
			// IP加数据库名
			else if(dbParams.length == 2){

				conn = DriverManager.getConnection(GlobalParams.DBBASE_URL.replace(GlobalParams.COMMON_IP, dbParams[0]) + dbParams[1], userName, password);
			}else {
				log.error("数据库指定错误" );
				throw new SQLException();
			}
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			// 正常情况不会出现这种异常
		} catch (SQLException e) {
			
			if(dbParams.length == 0) {
			
				log.error("获取数据库连接出错:登录账户为" + userName + ", 登录密码为:" + password + ", 登录数据库为:cwmaster" );
			} else if(dbParams.length == 1){
				
				log.error("获取数据库连接出错:登录账户为" + userName + ", 登录密码为:" + password + ", 登录数据库为:" + dbParams[0]);
			}else {
				
				log.error("获取数据库连接出错:登录账户为" + userName + ", 登录密码为:" + password + ", 登录数据库IP为:" + dbParams[0] + ", 登录数据库为:" + dbParams[1]);
			}
		}
	}

	public Connection getConnection() {

		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeConnection() {

		try {

			if(null == conn) {
				
				log.error("连接获取异常");
			} else {

				conn.commit();
				conn.close();
				conn = null;
			}
		} catch (Exception e) {

			try {

				if (conn != null && !conn.isClosed()) {

					conn.rollback();
					conn.close();
					log.error("数据库业务提交失败,正常回滚结束");
				}
			} catch (Exception e1) {

				log.error("数据库业务回滚失败");
			}
		}
	}
	
	public void closeException() {

		try {
			
			conn.rollback();
			conn.close();
			log.error("数据库业务提交失败,正常回滚结束");
			
		} catch (SQLException e) {
			log.error("数据库业务回滚失败");
		}
	}
}
