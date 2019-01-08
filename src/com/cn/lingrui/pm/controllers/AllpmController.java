package com.cn.lingrui.pm.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.pm.service.AllpmService;

@Controller
@RequestMapping(value="/pm")
public class AllpmController {
	
	@Resource(name="allpmService")
	public AllpmService allpmService;

	@RequestMapping(value = "/allpm", method = RequestMethod.GET)
	public ModelAndView getAllpm() throws Exception {

		ModelAndView mv = allpmService.getAllpmInfoView();

		return mv;
	}
	@RequestMapping(value = "/pmcx", method = RequestMethod.GET)
	public ModelAndView pmCx(String pmrq,String dqmc,String fzdq) throws Exception {

		ModelAndView mv = allpmService.getPmCxView(pmrq,dqmc,fzdq);

		return mv;
	}
}
