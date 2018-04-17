package com.cn.lingrui.sellPersonnel.service;

import java.sql.SQLException;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
import com.cn.lingrui.sellPersonnel.pojos.region.UpdateRegionPojo;

public interface RegionManageService {

	public ModelAndView receiveCurrentRegions(RegionsPojo pojo) throws SQLException, Exception;

	public ModelAndView getAddRegion();

	public ModelAndView postAddRegion(AddRegionPojoIn in);

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
	ModelAndView postDeleteRegionXzqx(String regionUid, String regionId, String cityValue) throws Exception;

}
