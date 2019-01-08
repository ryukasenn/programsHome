package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.lingrui.sellPersonnel.service.PersonManageService;

/**
 * 地总权限管理
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sellPersonnel")
public class AreaHeadController {

	@Resource(name = "personManageService")
	private PersonManageService personManageService;
	
	/**
	 * 申请终端离职
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dimissTerminal", method = RequestMethod.POST)
	public String getDimissTerminal(String dimissTerminalPid) throws Exception {
		String result = personManageService.getDimissTerminal(dimissTerminalPid);
		return result;
	}
	
	/**
	 * 提交终端离职
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/postDimissTerminal", method = RequestMethod.POST)
	public String postDimissTerminal(String personPid, String personLeaveTime, String personLeaveReason) throws Exception {
		String result = personManageService.postDimissTerminal(personPid,personLeaveTime,personLeaveReason);
		return result;
	}
	
}
