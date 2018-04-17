package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
import com.cn.lingrui.sellPersonnel.pojos.region.UpdateRegionPojo;
import com.cn.lingrui.sellPersonnel.service.RegionManageService;


@Controller
@RequestMapping("/sellPersonnel/regionController")
public class RegionManageController {


	@Resource(name = "regionManageService")
	private RegionManageService regionManageService;

	/**
	 * 查看部门划分情况
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/regions", method = RequestMethod.POST)
	public ModelAndView receiveCurrentRegions(RegionsPojo pojo) throws Exception {

		ModelAndView mv = regionManageService.receiveCurrentRegions(pojo);

		return mv;
	}
	
	/**
	 * 查看部门划分情况
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/regions", method = RequestMethod.GET)
	public ModelAndView getReceiveCurrentRegions(RegionsPojo pojo) throws Exception {

		ModelAndView mv = regionManageService.receiveCurrentRegions(pojo);

		return mv;
	}
	
	/**
	 * 获取修改部门基本信息页面
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeRegion", method = RequestMethod.POST)
	public ModelAndView getChangeRegion(RegionsPojo pojo) throws Exception {

		ModelAndView mv = regionManageService.getChangeRegion(pojo.getRegionUid());

		return mv;
	}
	
	/**
	 * 获取修改部门基本信息页面
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeRegion", method = RequestMethod.GET)
	public ModelAndView getChangeRegion_get(String regionUid) throws Exception {

		ModelAndView mv = regionManageService.getChangeRegion(regionUid);

		return mv;
	}
	
	/**
	 * 提交修改部门信息页面
	 * @param pojo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateRegion", method = RequestMethod.POST)
	public ModelAndView postUpdateRegion(UpdateRegionPojo pojo) throws Exception {

		ModelAndView mv = regionManageService.postUpdateRegion(pojo);

		return mv;
	}

	@RequestMapping(value = "/addRegion", method = RequestMethod.GET)
	public ModelAndView getAddRegion() throws Exception {

		ModelAndView mv = regionManageService.getAddRegion();

		return mv;
	}
	

	@RequestMapping(value = "/addRegion", method = RequestMethod.POST)
	public ModelAndView postAddRegion(AddRegionPojoIn in) throws Exception {

		ModelAndView mv = regionManageService.postAddRegion(in);

		return mv;
	}


	/**
	 * 获取地区划分页面
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkXzqxs", method = RequestMethod.GET)
	public ModelAndView getCheckXzqxs(String regionUid) throws Exception {

		ModelAndView mv = regionManageService.getCheckXzqxs(regionUid);

		return mv;
	}
	
	/**
	 * 提交地区下辖行政区县增加
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addRegionXzqx", method = RequestMethod.POST)
	public ModelAndView postAddRegionXzqx(RegionsPojo in) throws Exception {

		ModelAndView mv = regionManageService.postAddRegionXzqx(in);

		return mv;
	}
	
	/**
	 * 提交地区下辖行政区县删除
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteRegionXzqx", method = RequestMethod.GET)
	public ModelAndView postDeleteRegionXzqx(String regionUid, String regionId, String cityValue) throws Exception {

		ModelAndView mv = regionManageService.postDeleteRegionXzqx(regionUid, regionId, cityValue);

		return mv;
	}
	
	/**
	 * 获取大区信息下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveRegionsSelect")
	public String receiveRegionsSelect() throws Exception {
		
		String jsonData = regionManageService.receiveRegionsSelect();
		
		return jsonData;
	}

	/**
	 * 获取负责人信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveRegionReper")
	public String receiveRegionReper(String personType,String searchName) throws Exception {
		
		String jsonData = regionManageService.receiveRegionReper(personType, searchName);
		
		return jsonData;
	}
	
	/**
	 * 获取省份信息下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveProvinceSelect")
	public String receiveProvinceSelect() throws Exception {
		
		String jsonData = regionManageService.receiveProvinceSelect();
		
		return jsonData;
	}
	
	/**
	 * 获取区县信息下拉框
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody  
    @RequestMapping("/receiveAreaContainSelects")
	public String receiveProvinceSelect(String parentId) throws Exception {
		
		String jsonData = regionManageService.receiveAreaContainSelects(parentId);
		
		return jsonData;
	}
	
}


































