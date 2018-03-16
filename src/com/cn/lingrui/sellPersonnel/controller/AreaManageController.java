package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.service.AreaManageService;

@Controller
@RequestMapping("/sellPersonnel")
public class AreaManageController {

	@Resource(name = "areaManageService")
	private AreaManageService areaManageService;

	@RequestMapping(value = "/areas", method = RequestMethod.GET)
	public ModelAndView receiveCurrentTerminal() throws Exception {

		ModelAndView mv = areaManageService.receiveCurrentAreas();

		return mv;
	}
}
