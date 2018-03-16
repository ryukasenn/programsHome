package com.cn.lingrui.common.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.userManage.UmUserPojoIn;


public interface UserManageService {

	/**
	 * 获取用户管理首页
	 * @return
	 */
	public ModelAndView getHomePage();

	/**
	 * 执行添加用户
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postAddUser(UmUserPojoIn in) throws Exception;

	/**
	 * 执行修改用户
	 * @param in
	 * @return
	 */
	public ModelAndView postUpdateUser(UmUserPojoIn in);

	/**
	 * 执行删除用户
	 * @param in
	 * @return
	 */
	public ModelAndView postDeleteUser(UmUserPojoIn in);

	/**
	 * 获取添加用户页面
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getAddUser(UmUserPojoIn in) throws Exception;

	/**
	 * 获取修改用户页面
	 * @param in
	 * @return
	 */
	public ModelAndView getUpdateUser(UmUserPojoIn in);

}
