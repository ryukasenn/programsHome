package com.cn.lingrui.common.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.login.LoginPojoIn;
import com.cn.lingrui.common.services.LoginService;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;


@Controller
@RequestMapping("/login")
public class LoginController {
		
	@Resource(name = "loginService")
	private LoginService loginService;

	private static Logger log = LogManager.getLogger();
	/**
	 * 获取登录页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView getLogin(HttpServletRequest req) throws Exception {
		
		String referer = req.getHeader("referer");
		
		ModelAndView mv = null;
		
		if(CommonUtil.isEmpty(referer)) 
		{
			log.info("来自浏览器的直接访问");
			mv = HttpUtil.getModelAndView("common/login", "登录");
		} 
		
		else 
		{
			try {
				log.info("来自OA的连接访问");
				String username = HttpUtil.decodeToken(HttpUtil.getCookieValue(req, "LtpaToken"));
				
				if(!"".equals(username)) {
					
					LoginPojoIn in = new LoginPojoIn();
					in.setUserId(username);
					in.setLoginModel("1");
					mv = loginService.otherLogin(in);
				}
			} catch (Exception e) {
				
				log.info("来自OA的连接访问登录失败,转至手动登录");
				mv = HttpUtil.getModelAndView("common/login", "登录");
			}
			
		}
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
