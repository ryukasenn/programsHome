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
	
}
