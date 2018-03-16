package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_ROLE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;
import com.cn.lingrui.common.pojos.roleManage.RmRolePojoIn;

public interface RoleManageDao extends BaseDao {

	/**
	 * 获取所有角色
	 * @param conn
	 * @return
	 */
	public List<NBPT_RSFZ_ROLE> getRoleList(Connection conn) throws SQLException;

	/**
	 * 修改角色信息
	 * @param connection
	 * @param role
	 */
	public void postUpdateRole(Connection connection, NBPT_RSFZ_ROLE role) throws SQLException;

	/**
	 * 更新角色权限信息
	 * @param connection
	 * @param in
	 * @throws SQLException 
	 */
	public void postUpdateR_P(Connection connection, RmRolePojoIn in) throws SQLException;
	
	/**
	 * 添加角色
	 * @param connection
	 * @param role
	 */
	public void postAddRole(Connection connection, NBPT_RSFZ_ROLE role) throws SQLException;

	/**
	 * 删除角色
	 * @param connection
	 * @param role
	 */
	public void postDeleteRole(Connection connection, NBPT_RSFZ_ROLE role) throws SQLException;

	/**
	 * 根据ID,获取角色
	 * @param conn
	 * @param role
	 * @return
	 */
	public NBPT_RSFZ_ROLE getRole(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException;

	/**
	 * 根据ID,获取角色权限
	 * @param connection
	 * @param role
	 * @return
	 */
	public List<NBPT_RSFZ_R_P> getRole_Page(Connection connection, NBPT_RSFZ_ROLE role) throws SQLException;
	
}

