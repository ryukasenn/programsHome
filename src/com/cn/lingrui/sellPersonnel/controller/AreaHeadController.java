package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.service.AreaHeadService;

/**
 * 地总权限管理
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sellPersonnel")
public class AreaHeadController {

	@Resource(name = "areaHeadService")
	private AreaHeadService areaHeadService;
	
	/**
	 * 添加终端获取
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTerminal", method = RequestMethod.GET)
	public ModelAndView getAddTerminal() throws Exception {

		ModelAndView mv = areaHeadService.getAddTerminal();

		return mv;
	}

	/**
	 * 添加终端提交
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTerminal", method = RequestMethod.POST)
	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception {

		ModelAndView mv = areaHeadService.postAddTerminal(in);

		return mv;
	}
	
	/**
	 * 修改终端页面获取
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changePerson", method = RequestMethod.POST)
	public ModelAndView getChangePerson(String changePersonPid) throws Exception {

		ModelAndView mv = areaHeadService.getChangePerson(changePersonPid);

		return mv;
	}
}
