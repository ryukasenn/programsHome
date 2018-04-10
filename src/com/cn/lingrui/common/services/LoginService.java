package com.cn.lingrui.common.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.login.LoginPojoIn;

public interface LoginService {

	/**
	 * 普通登录
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkUser(LoginPojoIn in) throws Exception;
	
	/**
	 * OA连接登录
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public ModelAndView otherLogin(LoginPojoIn in) throws Exception;
}
