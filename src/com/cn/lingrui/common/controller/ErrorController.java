package com.cn.lingrui.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.services.ErrorService;

public class ErrorController {

	@Resource(name="errorService")
	private ErrorService errorService;
	@RequestMapping(name="*", method=RequestMethod.GET)
	public ModelAndView get_404() {
		
		ModelAndView mv = errorService.receiveError();
		
		return mv;
	}
	

	@RequestMapping(name="*", method=RequestMethod.POST)
	public ModelAndView post_404() {

		ModelAndView mv = errorService.receiveError();
		
		return mv;
	}
}
