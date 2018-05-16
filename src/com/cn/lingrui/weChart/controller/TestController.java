package com.cn.lingrui.weChart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.utils.HttpUtil;

@Controller
@RequestMapping("/wx")
public class TestController {

	@RequestMapping(value = "/getTestPage", method = RequestMethod.GET)
	public ModelAndView getTestPage() {
		
		return HttpUtil.getModelAndView("weChart/test1");
	}
}
