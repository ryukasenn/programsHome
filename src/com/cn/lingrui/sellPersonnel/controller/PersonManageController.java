package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.service.PersonManageService;

@Controller
@RequestMapping("/sellPersonnel")
public class PersonManageController {

	@Resource(name = "personManageService")
	private PersonManageService personManageService;
	
	/**
	 * 查询当前人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ModelAndView receiveCurrentTerminal_support() throws Exception {

		ModelAndView mv = personManageService.receiveCurrentTerminals_support();

		return mv;
	}

	/**
	 * 添加终端获取
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addTerminal", method = RequestMethod.GET)
	public ModelAndView getAddTerminal() throws Exception {

		ModelAndView mv = personManageService.getAddTerminal();

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

		ModelAndView mv = personManageService.postAddTerminal(in);

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

		ModelAndView mv = personManageService.getChangePerson(changePersonPid);

		return mv;
	}
	
//	/**
//	 * 添加终端负责区域页面获取
//	 * @param in
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/addTerminalRespons", method = RequestMethod.GET)
//	public ModelAndView getAddTerminalRespons(String parentId, String terminalId) throws Exception {
//
//		ModelAndView mv = personManageService.getAddTerminalRespons(parentId, terminalId);
//
//		return mv;
//	}
//	
//	/**
//	 * 添加终端负责区域页面获取
//	 * @param in
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/addTerminalRespons", method = RequestMethod.POST)
//	public ModelAndView postAddTerminalRespons(AddPersonPojoIn in) throws Exception {
//
//		ModelAndView mv = personManageService.postAddTerminalRespons(in);
//
//		return mv;
//	}
	/**
	 * 获取负责区域下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveSelect")
	public String postAddSelect(String parentId) throws Exception {
		
		String jsonData = personManageService.receiveSelect(parentId);
		
		return jsonData;
	}

	/**
	 * 获取地区下级区县下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveAreasSelect")
	public String receiveAreasSelect(String areaId) throws Exception {
		
		String jsonData = personManageService.receiveAreasSelect(areaId);
		
		return jsonData;
	}
	
	/**
	 * 获取终端负责区域列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveTerminalXzqx")
	public String receiveTerminalXzqx(String TerminalId) throws Exception {
		
		String jsonData = personManageService.receiveTerminalXzqx(TerminalId);
		
		return jsonData;
	}
}
