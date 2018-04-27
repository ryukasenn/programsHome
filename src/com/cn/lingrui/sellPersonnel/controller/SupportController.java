package com.cn.lingrui.sellPersonnel.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.service.SupportSerivce;

@Controller
@RequestMapping("/sellPersonnel/support")
public class SupportController {


	@Resource(name = "supportSerivce")
	private SupportSerivce supportSerivce;
	
	/**
	 * 生成考核列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createEvaluationForm", method = RequestMethod.GET)
	public ModelAndView createEvaluationForm(String endTime) throws Exception {

		ModelAndView mv = supportSerivce.createEvaluationForm(endTime);

		return mv;
	}
	
	/**
	 * 生成考核列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/supportAdd", method = RequestMethod.GET)
	public ModelAndView getSupportAdd() throws Exception {

		ModelAndView mv = supportSerivce.getSupportAdd();

		return mv;
	}
}
