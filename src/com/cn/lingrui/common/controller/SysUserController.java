package com.cn.lingrui.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.userManage.UmUserPojoIn;
import com.cn.lingrui.common.services.UserManageService;

@Controller
@RequestMapping("/sys")
public class SysUserController {


	@Resource(name="userManageService")
	private UserManageService userManage;
	/**
	 * 获取用户管理首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ruHome", method = RequestMethod.GET)
	public ModelAndView getManage() {

		ModelAndView mv = userManage.getHomePage();
		return mv;
	}
	
	/**
	 * 获取添加角色页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ruAddUser", method = RequestMethod.GET)
	public ModelAndView getAddUser(UmUserPojoIn in) throws Exception {

		ModelAndView mv = userManage.getAddUser(in);
		return mv;
	}
	
	/**
	 * 添加角色
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ruAddUser", method = RequestMethod.POST)
	public ModelAndView postAddUser(UmUserPojoIn in) throws Exception {

		ModelAndView mv = userManage.postAddUser(in);
		return mv;
	}
	
	/**
	 * 获取修改角色页面
	 * @return
	 */
	@RequestMapping(value = "/ruUpdateUser", method = RequestMethod.GET)
	public ModelAndView getUpdateUser(UmUserPojoIn in) {

		ModelAndView mv = userManage.getUpdateUser(in);
		return mv;
	}
	
	/**
	 * 修改角色
	 * @return
	 */
	@RequestMapping(value = "/ruUpdateUser", method = RequestMethod.POST)
	public ModelAndView postUpdateUser(UmUserPojoIn in) {

		ModelAndView mv = userManage.postUpdateUser(in);
		return mv;
	}
	
	/**
	 * 删除角色
	 * @return
	 */
	@RequestMapping(value = "/ruDeleteUser", method = RequestMethod.POST)
	public ModelAndView postDeleteUser(UmUserPojoIn in) {

		ModelAndView mv = userManage.postDeleteUser(in);
		return mv;
	}
}
