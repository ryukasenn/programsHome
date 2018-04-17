package com.cn.lingrui.sellPersonnel.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.service.CommonService;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;

@Controller
@RequestMapping("/sellPersonnel/support")
public class SupportController {


	@Resource(name = "supportSerivce")
	private SupportSerivce supportSerivce;
	
	/**
	 * 查询当前人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ModelAndView receiveAllTerminals() throws Exception {

		ModelAndView mv = supportSerivce.receiveAllTerminals();

		return mv;
	}
}
