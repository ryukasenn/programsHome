package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.utils.DownloadUtil;
import com.cn.lingrui.sellPersonnel.service.CommonService;


/**
 * 公共方法或AJAX请求
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sellPersonnel")
public class SpCommonController {


	@Resource(name = "commonService")
	private CommonService commonService;
	
	/**
	 * 查询当前人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ModelAndView receiveCurrentTerminal() throws Exception {

		ModelAndView mv = commonService.receiveCurrentTerminals();

		return mv;
	}
	
	/**
	 * 查询当前人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveAllPersons", method = RequestMethod.GET)
	public ModelAndView receiveAllCurrentTerminal(String type, HttpServletResponse response, HttpServletRequest req) throws Exception {

		if("download".equals(type)) {
			
			commonService.createAllCurrentTerminals("所有人员名单");
			DownloadUtil.writeDownloadFile("所有人员名单.xls", response, req.getServletContext().getMimeType("所有人员名单.xls"));
			return null;
		} else {

			ModelAndView mv = commonService.receiveAllCurrentTerminals();
			return mv;
		}
	}

	/**
	 * 获取国家行政区县划分列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveSelect")
	public String receiveSelect(String parentId) throws Exception {
		
		String jsonData = commonService.receiveSelect(parentId);
		
		return jsonData;
	}
	
	/**
	 * 获取国家行政区县划分列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping(value="/receiveSelect", method = RequestMethod.POST)
	public String receiveSelectPost(String parentId) throws Exception {
		
		String jsonData = commonService.receiveSelectPost(parentId);
		
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
	
	/**
	 * 验证登录ID存在性
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveLoginId")
	public String receiveLoginId(String loginId) throws Exception {
		
		String jsonData = commonService.receiveLoginId(loginId);
		
		return jsonData;
	}
	
	/**
	 * 调岗人员查询
	 * @param personName 调岗人员姓名
	 * @param transferType 调岗类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/transferSearchPerson")
	public String transferSearchPerson(String personName, String transferType) throws Exception {
		
		String jsonData = commonService.transferSearchPerson(personName, transferType);
		
		return jsonData;
	}
	
	/**
	 * 调岗地区查询
	 * @param personName 调岗人员姓名
	 * @param transferType 调岗类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/transferSearchRegion")
	public String transferSearchRegion(String regionName, String transferType) throws Exception {
		
		String jsonData = commonService.transferSearchRegion(regionName);
		
		return jsonData;
	}
	
	/**
	 * 调岗地区查询
	 * @param personName 调岗人员姓名
	 * @param transferType 调岗类型
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/support/createMap")
	public String createMap() throws Exception {
		
		String jsonData = commonService.createMap();
		
		return jsonData;
	}
	
	/**
	 * 获取字典列表
	 * @param personName 
	 * @param transferType 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping(value = "/dics", method = RequestMethod.POST)
	public String receiveDics(String params) throws Exception {
		
		String jsonData = commonService.receiveDictionaryList(params); 
		
		return jsonData;
	}
	
	/**
	 * 获取国家行政区县划分列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveXzqxs")
	public String receiveXzqxs(String parentId) throws Exception {
		
		String jsonData = commonService.receiveSelect(parentId);
		
		return jsonData;
	}

	/**
	 * 生成部门树
	 * @return
	 */
	@ResponseBody  
    @RequestMapping("/createTreeRegion")
	public String createTreeRegion(String level) {
		
		String jsonData = commonService.createTreeRegion(level);
		
		return jsonData;
	}
	
	/**
	 * 生成部门选择树
	 * @return
	 */
	@ResponseBody  
    @RequestMapping("/createRegionTree")
	public String createRegionTree(String pid, String rank) {
		
		String jsonData = commonService.createRegionTree(pid, rank);
		
		return jsonData;
	}

	/**
	 * 生成人员选择树
	 * @return
	 */
	@ResponseBody  
    @RequestMapping("/createPersonTree")
	public String createPersonTree() {
		
		String jsonData = commonService.createPersonTree();
		
		return jsonData;
	}
	
	/**
	 * 获取部门选择列表
	 * @return
	 */
	@ResponseBody  
    @RequestMapping("/receiveRegionSelect")
	public String receiveRegionSelect(String regionUid) {
		String jsonData = commonService.receiveRegionSelect(regionUid);
		return jsonData;
	}
}
