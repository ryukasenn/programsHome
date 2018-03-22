package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.check.CheckPojoIn;
import com.cn.lingrui.sellPersonnel.service.CheckManageService;

@Controller
@RequestMapping("/sellPersonnel")
public class CheckManageController {

	@Resource(name = "checkManageService")
	private CheckManageService checkManageService;

	/**
	 * 获取未审核人员列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUnchecks", method = RequestMethod.GET)
	public ModelAndView receiveUnchecks() throws Exception {

		ModelAndView mv = checkManageService.receiveUnchecks();

		return mv;
	}
	
	/**
	 * 获取未审核人员详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUncheck", method = RequestMethod.POST)
	public ModelAndView receiveUncheck(CheckPojoIn in) throws Exception {

		ModelAndView mv = checkManageService.receiveUncheck(in.getUNCHECKPID());

		return mv;
	}
}
