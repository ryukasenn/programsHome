package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

public interface CommonService{

	/**
	 * 查询当前所有人(根据不同权限,生成不同页面)
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveCurrentTerminals() throws Exception;
	
	/**
	 * 查询当前所有人员信息(根据不同权限,生成不同页面)
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveAllCurrentTerminals() throws Exception;

	public String receiveSelect(String parentId) throws Exception;

	/**
	 * AJAX通过地区ID,获取地区下辖行政区县列表
	 * @param areaId 地区ID
	 * @return
	 * @throws Exception
	 */
	public String receiveAreasSelect(String areaId) throws Exception;


	/**
	 * AJAX获取终端人员负责行政区县
	 * @param TerminalId 终端人员32位PID
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalXzqx(String changePersonPid) throws Exception;

	/**
	 * AJAX根据身份证获取籍贯信息,到市
	 * @param idNum 身份证号码
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalPlace(String idNum) throws Exception;
	
	/**
	 * AJAX根据身份证获取籍贯信息,到市
	 * @param loginId 填写的登录ID
	 * @return
	 * @throws Exception
	 */
	public String receiveLoginId(String loginId) throws Exception;
	
	/**
	 * 调岗人员查询
	 * @param personName 调岗人员姓名
	 * @param transferType 调岗类型
	 * @return
	 * @throws Exception
	 */
	public String transferSearchPerson(String personName, String transferType) throws Exception;

	/**
	 * 调岗地区查询
	 * @param personName 调岗人员姓名
	 * @return
	 * @throws Exception
	 */
	public String transferSearchRegion(String regionName) throws Exception;

	/**
	 * 生成全国在职人数地图
	 * @return
	 * @throws Exception 
	 */
	public String createMap() throws Exception;

	/**
	 * 获取字典列表
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public String receiveDictionaryList(String params) throws Exception;
	
	/**
	 * AJAX通过地区ID,获取地区下辖行政区县列表
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public String receiveXzqxs(String parentId) throws Exception;

	/**
	 * 创建所有人员名单文件.xls
	 * @throws Exception 
	 */
	public String createAllCurrentTerminals(String fileName) throws Exception;

	/**
	 * 部门管理树
	 * @param level 
	 * @return
	 */
	public String createTreeRegion(String level);

	/**
	 * 获取行政区县
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public String receiveSelectPost(String parentId) throws Exception;
	
	/**
	 * 生成部门树,选取用
	 * @param level 
	 * @return
	 */
	public String createRegionTree(String pid, String rank);

	/**
	 * 生成人员选择树
	 * @param pid
	 * @param rank
	 * @return
	 */
	public String createPersonTree();

	public String receiveRegionSelect(String parentUid);

}
