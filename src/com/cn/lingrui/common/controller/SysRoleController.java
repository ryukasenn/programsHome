package com.cn.lingrui.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.roleManage.RmRolePojoIn;
import com.cn.lingrui.common.services.RoleManageService;

/**
 * 系统管理员专用通道,角色管理
 * 
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sys")
public class SysRoleController {

	@Resource(name="roleManageService")
	private RoleManageService roleManage;
	
	/**
	 * 角色管理首页
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmHome", method = RequestMethod.GET)
	public ModelAndView getManage() throws Exception {

		ModelAndView mv = roleManage.getHomePage();
		return mv;
	}
	
	/**
	 * 添加角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmAddRole", method = RequestMethod.GET)
	public ModelAndView getAddRole(RmRolePojoIn in) throws Exception {

		ModelAndView mv = roleManage.getAddRole(in);
		return mv;
	}
	
	/**
	 * 添加角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmAddRole", method = RequestMethod.POST)
	public ModelAndView postAddRole(RmRolePojoIn in) throws Exception {

		ModelAndView mv = roleManage.postAddRole(in);
		return mv;
	}
	
	/**
	 * 修改角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmUpdateRole", method = RequestMethod.GET)
	public ModelAndView getUpdateRole(RmRolePojoIn in) throws Exception {

		ModelAndView mv = roleManage.getUpdateRole(in);
		return mv;
	}
	
	/**
	 * 修改角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmUpdateRole", method = RequestMethod.POST)
	public ModelAndView postUpdateRole(RmRolePojoIn in) throws Exception {

		ModelAndView mv = roleManage.postUpdateRole(in);
		return mv;
	}
	
	/**
	 * 删除角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/rmDeleteRole", method = RequestMethod.POST)
	public ModelAndView postDeleteRole(RmRolePojoIn in) throws Exception {

		ModelAndView mv = roleManage.postDeleteRole(in);
		return mv;
	}
}
