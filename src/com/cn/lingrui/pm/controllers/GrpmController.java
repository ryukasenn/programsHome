package com.cn.lingrui.pm.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.pm.service.GrpmService;

@Controller
@RequestMapping(value="/pm")
public class GrpmController {
	
	@Resource(name="grpmService")
	public GrpmService grpmService;
	
	@RequestMapping(value = "/grpm", method = RequestMethod.GET)
	public ModelAndView getCurrentGrpm() throws Exception {

		ModelAndView mv = grpmService.getGrpmInfoView();

		return mv;
	}
	@RequestMapping(value = "/sameDqcx", method = RequestMethod.GET)
	public ModelAndView getSameDqmcPM(String pmrq) throws Exception {

		ModelAndView mv = grpmService.getSameDqPmView(pmrq);

		return mv;
	}
	
	@RequestMapping(value = "/slbcx", method = RequestMethod.GET)
	public ModelAndView getslbPM(String pmrq,String mc) throws Exception {

		ModelAndView mv = grpmService.getslbView(pmrq,mc);

		return mv;
	}
	@RequestMapping(value = "/grxx", method = RequestMethod.GET)
	public ModelAndView getgrxx(String rq,String xm,String dw) throws Exception {

		ModelAndView mv = grpmService.getgrxxView(rq, xm, dw);		
		return mv;
	}
	
}
