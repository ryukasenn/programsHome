package com.cn.lingrui.sellPersonnel.service;

import java.sql.SQLException;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;

public interface RegionManageService {

	public ModelAndView receiveCurrentRegions(RegionsPojo pojo) throws SQLException, Exception;

	public ModelAndView getAddRegion();

	public ModelAndView postAddRegion(AddRegionPojoIn in);

	public String receiveRegionsSelect() throws Exception;

	public ModelAndView getChangeRegion(RegionsPojo pojo) throws Exception;

	public String receiveRegionReper(String personType, String searchName) throws Exception;

}
