package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

public interface PersonManageService {

	/**
	 * 查看大区下省区信息
	 * @param regionUid 大区32位UID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getProvincePersons(String regionUid) throws Exception;

	/**
	 * 查看地区下人员详细信息
	 * @param areaUid
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getAreaPersons(String areaUid, String regionUid) throws Exception;

	/**
	 * 根据人员PID,获取人员详细信息
	 * @param areaUid 所在地区
	 * @param regionUid 所在大区
	 * @param personPid 人员PID
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receivePerson(String areaUid, String regionUid, String personPid) throws Exception;

}
