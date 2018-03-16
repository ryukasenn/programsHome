package com.cn.lingrui.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.services.ModuleService;

@Controller
@RequestMapping("/module")
public class ModuleController {

	@Resource(name="moduleService")
	private ModuleService moduleService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ModelAndView moduleChange() {
		
		ModelAndView mv = moduleService.pageJump();
		return mv;
	}
}
