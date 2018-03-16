package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;
import com.cn.lingrui.common.pojos.pageManage.Module;

public interface PageManageDao extends BaseDao{

	/**
	 * 获取分类好的所有模块,功能及页面
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_RSFZ_PAGE> getPageList(Connection conn) throws SQLException;
	
	/**
	 * 获取模块列表
	 * @param conn
	 * @return
	 */
	public List<NBPT_RSFZ_PAGE> getModuleList(Connection conn) throws SQLException;
	
	/**
	 * 添加模块
	 * @param conn
	 * @param rsfzpage
	 */
	public void addModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 删除模块
	 * @param conn
	 * @param rsfzpage
	 */
	public void deleteModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 修改模块
	 * @param conn
	 * @param rsfzpage
	 */
	public void updateModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 获取功能列表
	 * @param conn
	 * @return
	 */
	public List<NBPT_RSFZ_PAGE> getFuncList(Connection conn) throws SQLException;
	
	/**
	 * 添加功能
	 * @param conn
	 * @param rsfzpage
	 */
	public void addFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 删除功能
	 * @param conn
	 * @param rsfzpage
	 */
	public void deleteFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 修改功能
	 * @param conn
	 * @param rsfzpage
	 */
	public void updateFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 返回所有的页面信息
	 * @param conn
	 * @return
	 */
	public List<NBPT_RSFZ_PAGE> getPmPageList(Connection conn) throws SQLException;
	
	/**
	 * 添加页面
	 * @param conn
	 * @param rsfzpage
	 */
	public void addPage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 删除页面
	 * @param conn
	 * @param rsfzpage
	 */
	public void deletePage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;
	
	/**
	 * 修改页面
	 * @param conn
	 * @param rsfzpage
	 */
	public void updatePage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) throws SQLException;

	/**
	 * 获取分类好的所有模块功能及页面
	 * @param conn
	 * @return
	 */
	public List<Module> getClassifiedModuleList(Connection conn) throws SQLException;
}
