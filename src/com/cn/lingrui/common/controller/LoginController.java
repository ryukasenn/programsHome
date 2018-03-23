package com.cn.lingrui.common.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.login.LoginPojoIn;
import com.cn.lingrui.common.services.LoginService;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.common.utils.HttpUtil;


@Controller
@RequestMapping("/login")
public class LoginController {
		
	@Resource(name = "loginService")
	private LoginService loginService;
	
	/**
	 * 获取登录页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView getLogin(HttpServletRequest req) throws Exception {
		
		String url = req.getHeader("referer");
		//String token = req.getParameter("LtpaToken");
		String username = req.getParameter("username");
		if(null != url && GlobalParams.REFER.equals(url)) {
			
			
			//String username = HttpUtil.decodeToken(token);
			if(!"".equals(username)) {
				
				ModelAndView mv = loginService.otherLogin(username);
				return mv;
			}
		}
		
		ModelAndView mv = HttpUtil.getModelAndView("common/login", "登录");
		return mv;
	}
	
	/**
	 * 登录页面登录操作
	 * @param in 页面相关信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	public ModelAndView postLogin(LoginPojoIn in) throws Exception {
		
		ModelAndView mv = loginService.checkUser(in);
		
		return mv;
	}
}
