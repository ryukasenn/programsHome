package com.cn.lingrui.common.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.login.LoginPojoIn;

public interface LoginService {

	public ModelAndView checkUser(LoginPojoIn in) throws Exception;
	public ModelAndView otherLogin(String username) throws Exception;
}
