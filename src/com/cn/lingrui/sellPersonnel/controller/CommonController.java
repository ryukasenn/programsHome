package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.service.CommonService;


/**
 * 公共方法或AJAX请求
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sellPersonnel")
public class CommonController {


	@Resource(name = "commonService")
	private CommonService commonService;
	/**
	 * 查询当前人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ModelAndView receiveCurrentTerminal_support() throws Exception {

		ModelAndView mv = commonService.receiveCurrentTerminals();

		return mv;
	}

	/**
	 * 获取负责区域下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveSelect")
	public String postAddSelect(String parentId) throws Exception {
		
		String jsonData = commonService.receiveSelect(parentId);
		
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
		
		String jsonData = commonService.receiveAreasSelect(areaId);
		
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
		
		String jsonData = commonService.receiveTerminalXzqx(TerminalId);
		
		return jsonData;
	}
	

	/**
	 * 获取终端籍贯信息
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveTerminalPlace")
	public String receiveTerminalPlace(String idNum) throws Exception {
		
		String jsonData = commonService.receiveTerminalPlace(idNum);
		
		return jsonData;
	}
}
