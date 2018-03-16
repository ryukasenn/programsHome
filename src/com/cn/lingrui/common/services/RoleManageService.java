package com.cn.lingrui.common.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.roleManage.RmRolePojoIn;

public interface RoleManageService {

	/**
	 * 获取角色管理首页
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getHomePage() throws Exception;

	/**
	 * 执行添加角色
	 * @param in
	 * @return
	 */
	public ModelAndView postAddRole(RmRolePojoIn in) throws Exception;

	/**
	 * 执行修改角色
	 * @param in
	 * @return
	 */
	public ModelAndView postUpdateRole(RmRolePojoIn in) throws Exception;

	/**
	 * 执行删除角色
	 * @param in
	 * @return
	 */
	public ModelAndView postDeleteRole(RmRolePojoIn in) throws Exception;

	/**
	 * 获取添加角色页面
	 * @param in
	 * @return
	 */
	public ModelAndView getAddRole(RmRolePojoIn in) throws Exception;

	/**
	 * 获取修改角色页面
	 * @param in
	 * @return
	 */
	public ModelAndView getUpdateRole(RmRolePojoIn in) throws Exception;
}
