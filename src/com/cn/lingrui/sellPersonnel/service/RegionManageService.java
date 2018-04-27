package com.cn.lingrui.sellPersonnel.service;

import java.sql.SQLException;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
import com.cn.lingrui.sellPersonnel.pojos.region.UpdateRegionPojo;

public interface RegionManageService {
	
	/**
	 * 查看部门划分情况
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receiveCurrentRegions(RegionsPojo pojo) throws SQLException, Exception;
	
	/**
	 * 获取添加大区页面
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAddRegion() throws Exception;
	
	/**
	 * 提交添加大区
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public ModelAndView postAddRegion(AddRegionPojoIn in) throws Exception;

	/**
	 * ajax获取所有大区信息
	 * @return
	 * @throws Exception
	 */
	public String receiveRegionsSelect() throws Exception;

	public ModelAndView getChangeRegion(String regionId) throws Exception;

	public String receiveRegionReper(String personType, String searchName) throws Exception;

	public String receiveProvinceSelect() throws Exception;

	public ModelAndView getCheckXzqxs(String regionUid) throws Exception;

	public String receiveAreaContainSelects(String parentId) throws Exception;

	/**
	 * 提交部门信息更新
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postUpdateRegion(UpdateRegionPojo pojo) throws Exception;

	/**
	 * 提交添加地区下辖行政区县
	 * @param in 包含选中地区的参数
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postAddRegionXzqx(RegionsPojo in) throws Exception;

	/**
	 * 提交删除地区下辖行政区县
	 * @param in 包含选中地区的参数
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postDeleteRegionXzqx(String regionUid, String regionId, String cityValue) throws Exception;
	
	/**
	 * 获取添加地区页面
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAddArea() throws Exception;
	
	/**
	 * 提交添加地区
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public ModelAndView postAddArea(AddRegionPojoIn in) throws Exception;

	/**
	 * AJAX检测是否有废弃的部门
	 * @param type
	 * @param provinceId
	 * @return
	 * @throws Exception 
	 */
	public String checkRegion(String type, String provinceId) throws Exception;

}
