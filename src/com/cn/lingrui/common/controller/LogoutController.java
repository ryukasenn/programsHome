package com.cn.lingrui.common.controller;


import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.utils.HttpUtil;


@Controller
@RequestMapping("/logout")
public class LogoutController {
		
	/**
	 * 获取登录页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView getLogin(HttpServletRequest req) throws Exception {
		
				
		ModelAndView mv = HttpUtil.getModelAndView("common/login", "登录");
		return mv;
	}
}
