package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.pojos.CreateForm;
import com.cn.lingrui.common.pojos.FormItem;
import com.cn.lingrui.common.pojos.Option;
import com.cn.lingrui.common.pojos.Radio;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;
import com.cn.lingrui.sellPersonnel.pojos.region.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.AddSectionIn;
import com.cn.lingrui.sellPersonnel.pojos.region.ChangeSectionIn;
import com.cn.lingrui.sellPersonnel.pojos.region.PostChangeSectionIn;
import com.cn.lingrui.sellPersonnel.pojos.region.ReceiveSectionIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
import com.cn.lingrui.sellPersonnel.pojos.region.RmShow;
import com.cn.lingrui.sellPersonnel.pojos.region.UpdateRegionPojo;
import com.cn.lingrui.sellPersonnel.service.RegionManageService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

import net.sf.json.JSONArray;

@Service("regionManageService")
public class RegionManageServiceImpl extends SellPBaseService implements RegionManageService{

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="regionManageDao")
	private RegionManageDao regionManageDao;
	
	@Override
	protected String getFunNum() {
		
		return "";
	}
	/**
	 * 查看当前大区和地区
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@Override
	public ModelAndView receiveCurrentRegions(RegionsPojo pojo) throws Exception {

		this.before();
		
		// 初始化返回页面
		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030202"));
		
		// 查询所有区域划分信息
		try {

			List<NBPT_VIEW_REGION> currentRegions = regionManageDao.receiveCurrentRegions(pojo, this.getConnection());
			
			mv.addObject("currentRegions", currentRegions);
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("获取区域划分信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 进入添加大区页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getAddRegion() throws Exception {
		
		try {

			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030206"));
			return this.after(mv);
		} catch (Exception e) {

			this.closeException();
			log.error("获取添加大区页面出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 执行添加大区
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postAddRegion(AddRegionPojoIn in) throws Exception {

		this.before();

		try {

			// 准备插入信息
			NBPT_SP_REGION newRegion = new NBPT_SP_REGION();
			
			String newMaxId = regionManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_REGION", " WHERE NBPT_SP_REGION_LEVEL = '1'");
			newRegion.setNBPT_SP_REGION_ID(newMaxId);
			newRegion.setNBPT_SP_REGION_LEVEL("1");
			newRegion.setNBPT_SP_REGION_NAME(in.getRegionName());
			newRegion.setNBPT_SP_REGION_ONAME(in.getRegionNeed());
			newRegion.setNBPT_SP_REGION_NOTE(in.getRegionNote());
			newRegion.setNBPT_SP_REGION_RESPONSIBLER(in.getRegionResponsibler());
			newRegion.setNBPT_SP_REGION_UID(CommonUtil.getUUID_32());

			ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regionController/regions");
			
			// 执行插入
			regionManageDao.insertForClasz(NBPT_SP_REGION.class, newRegion, this.getConnection());
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("执行添加大区出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	@Override
	public String checkRegion(String type, String provinceId) throws Exception {
		this.before();
		
		
		try {
			// 直接查询是否有弃用的大区
			List<NBPT_SP_REGION> dumpedRegions = new ArrayList<>();
			if("1".equals(type)) {

				// 直接查询是否有弃用的大区
				dumpedRegions = regionManageDao.receiveDumpedRegion("", this.getConnection());
				
			} else {
				// 直接查询是否有弃用的大区
				dumpedRegions = regionManageDao.receiveDumpedRegion(provinceId, this.getConnection());
			}
			
			JSONArray ja = JSONArray.fromObject(dumpedRegions);
	
			return this.after(ja.toString());
			
		} catch (Exception e) {

			this.closeException();
			log.error("获取废弃的部门出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView getAddArea() throws Exception {
		
		try {

			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030207"));
			
			// 查询省份列表
			List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.getXzqxhfs("", this.getConnection());
			provinces.add(0,new NBPT_COMMON_XZQXHF());
			mv.addObject("provinces", provinces);
			
			return this.after(mv);
			
		} catch (Exception e) {

			this.closeException();
			log.error("获取添加地区页面出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView postAddArea(AddRegionPojoIn in) throws Exception {

		this.before();

		try {

			// 准备插入信息
			NBPT_SP_REGION newRegion = new NBPT_SP_REGION();
			
			String newMaxId = regionManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_REGION"
					, " WHERE NBPT_SP_REGION_LEVEL = '2' AND NBPT_SP_REGION_PROVINCE_ID = '" + in.getProvinceId() + "'");
			newRegion.setNBPT_SP_REGION_ID(newMaxId);
			newRegion.setNBPT_SP_REGION_LEVEL("2");
			newRegion.setNBPT_SP_REGION_NAME(in.getRegionName());
			newRegion.setNBPT_SP_REGION_ONAME(in.getRegionNeed());
			newRegion.setNBPT_SP_REGION_NOTE(in.getRegionNote());
			newRegion.setNBPT_SP_REGION_RESPONSIBLER(in.getRegionResponsibler());
			newRegion.setNBPT_SP_REGION_UID(CommonUtil.getUUID_32());
			
			// 获取父大区的UID
			NBPT_VIEW_XZQX xzqx = regionManageDao.receiveXzqx(in.getProvinceId(), "11", this.getConnection());
			
			newRegion.setNBPT_SP_REGION_PARENT_UID(xzqx.getNBPT_SP_REGION_UID());
			newRegion.setNBPT_SP_REGION_PROVINCE_ID(in.getProvinceId());

			ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regionController/regions");
			
			// 执行插入
			regionManageDao.insertForClasz(NBPT_SP_REGION.class, newRegion, this.getConnection());
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("执行添加地区出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * AJAX获取查看大区页面输入框自动补全信息
	 */
	@Override
	public String receiveRegionsSelect() throws Exception {

		try {
			
			this.before();
	
			List<NBPT_SP_REGION> regions = regionManageDao.receiveRegionsSelect(this.getConnection());
	
			JSONArray ja = JSONArray.fromObject(regions);
	
			return this.after(ja.toString());
			
		} catch (SQLException e) {

			this.closeException();
			log.error("获取大区信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * AJAX获取负责人信息
	 * @throws Exception 
	 */
	@Override
	public String receiveRegionReper(String personType, String searchName) throws Exception {
		
		try {

			this.before();
			if(null == searchName || "".equals(searchName)) {
				
				List<NBPT_SP_PERSON> persons = regionManageDao.receiveRegionReper(personType, this.getConnection());
				JSONArray ja = JSONArray.fromObject(persons);
		
				return this.after(ja.toString());				
			} else {
				
				List<NBPT_SP_PERSON> persons = regionManageDao.searchRegionReper(personType, searchName, this.getConnection());
				
				JSONArray ja = JSONArray.fromObject(persons);
		
				return this.after(ja.toString());
			}
			
		} catch (SQLException e) {

			this.closeException();
			log.error("获取分配负责人列表出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 获取修改部门信息页面
	 */
	@Override
	public ModelAndView getChangeRegion(String regionUid) throws Exception {
		
		try {
			
			this.before();
	
			// 初始化返回结果
			ModelAndView mv = null;
			
			// 返回地区信息
			NBPT_VIEW_REGION regionInfo = regionManageDao.receiveRegion(regionUid, this.getConnection());
						
			if("1".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {

				// 如果是大区,获取大区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030203", this.getRole()));
				
				// 获取计算配额数
				String needSum = regionManageDao.receiveRegonNeedSum(regionUid, this.getConnection());
				
				mv.addObject("needSum", needSum);
			}
			
			else if("2".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {

				// 如果是地区,获取地区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030204", this.getRole()));
								
				mv.addObject("currentProvince", regionInfo.getNBPT_SP_REGION_PROVINCE_ID());
			}
			
			mv.addObject("regionInfo", regionInfo);
			mv.addObject("personName", regionInfo.getNBPT_SP_REGION_RESPONSIBLER_NAME());
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("修改部门出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}

	/**
	 * 提交部门基本信息修改
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postUpdateRegion(UpdateRegionPojo pojo) throws Exception {

		try {
			
			this.before();
			// 初始化返回
			ModelAndView mv = null;

			// 返回当前选择部门
			mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regionController/changeRegion?regionUid=" + pojo.getRegionUid());
			
			// 1.查询出部门相关信息
			NBPT_VIEW_REGION regionInfo = regionManageDao.receiveRegion(pojo.getRegionUid(), this.getConnection());
			
			// 2.设定修改信息
			if("1".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {

				// 2.1 如果是大区信息修改,初始化信息
				NBPT_SP_REGION updateInfo = new NBPT_SP_REGION();
				
				// 2.2 设定Uid
				updateInfo.setNBPT_SP_REGION_UID(regionInfo.getNBPT_SP_REGION_UID());
				
				// 2.3 设定其他信息
				RegionManageServiceUtils.setUpdateRegionInfos(updateInfo, pojo, regionInfo);
				
				// 2.4 执行更新
				regionManageDao.updateRegion(updateInfo, this.getConnection());

				
			} else if("2".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {
				
				// 2.1 如果是地区信息修改,初始化信息
				NBPT_SP_REGION updateInfo = new NBPT_SP_REGION();
				
				// 2.2 设定Uid
				updateInfo.setNBPT_SP_REGION_UID(regionInfo.getNBPT_SP_REGION_UID());
				
				// 2.3 设定其他信息
				RegionManageServiceUtils.setUpdateRegionInfos(updateInfo, pojo, regionInfo);

				// 2.4 更新基本信息
				regionManageDao.updateRegion(updateInfo, this.getConnection());
				
			}

			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.error("更新部门基础信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/** 
	 * 获取省份信息下拉框
	 * @throws Exception 
	 */
	@Override
	public String receiveProvinceSelect() throws Exception {

		try {
			
			this.before();
				
			List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.getXzqxhfs(null, this.getConnection());
			
			JSONArray ja = JSONArray.fromObject(provinces);
	
			return this.after(ja.toString());
			
		} catch (SQLException e) {

			this.closeException();
			log.error("查询省份信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 获取地区下辖区县划分页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getCheckXzqxs(String  regionUid) throws Exception {
		
		try {
			
			this.before();
			
			// 初始化返回結果
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030205"));

			// 查询部门信息
			NBPT_VIEW_REGION region = regionManageDao.receiveRegion(regionUid, this.getConnection());
			
			// 2.查询地区下辖区和县
			List<NBPT_VIEW_XZQX> region_xzqxs = regionManageDao.receiveRegionXzqx(regionUid, this.getConnection());
						
			// 3.分离出下辖市,区和县
			Map<String, List<NBPT_VIEW_XZQX>> classifyedRegionXzqxs = CommonUtil.classify(region_xzqxs, "NBPT_COMMON_XZQXHF_LEVEL", NBPT_VIEW_XZQX.class);

			mv.addObject("regionInfo", region);
			mv.addObject("citys", CommonServiceUtils.getListInMapByKey(classifyedRegionXzqxs, "2"));
			mv.addObject("contys", CommonServiceUtils.getListInMapByKey(classifyedRegionXzqxs, "3"));
			return this.after(mv);			
		} catch (SQLException e) {

			this.closeException();
			log.error("查询地区下辖区县出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}	
	}
	

	/**
	 * 添加地区下辖区县时获取添加下拉表方法
	 */
	@Override
	public String receiveAreaContainSelects(String parentId) throws Exception {
		
		try {
			
			this.before();
				
				List<NBPT_COMMON_XZQXHF> resultList = regionManageDao.receiveAreaContainSelects(parentId, this.getConnection());
				
				resultList.add(0, new NBPT_COMMON_XZQXHF());
				JSONArray ja = JSONArray.fromObject(resultList);
		
				return this.after(ja.toString());
			
		} catch (SQLException e) {

			this.closeException();
			log.error("查询添加地区下辖区县时获取添加下拉表出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView postAddRegionXzqx(RegionsPojo in) throws Exception {

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regionController/checkXzqxs?regionUid=" + in.getAddAreaContain_regionUid());
		
		// 初始化存入参数
		NBPT_SP_REGION_XZQX region_XZQX = new NBPT_SP_REGION_XZQX();
		
		// 唯一ID
		region_XZQX.setNBPT_SP_REGION_XZQX_UID(CommonUtil.getUUID_32());
		
		// 类型为地区下辖市区县
		region_XZQX.setNBPT_SP_REGION_XZQX_TYPE("22");
		
		// 
		region_XZQX.setNBPT_SP_REGION_XZQX_REGIONID(in.getAddAreaContain_regionId());
		
		// 如果选择的区县级单位为空,则添加市级
		if("".equals(in.getAddAreaContain_contyValue())) {
			
			region_XZQX.setNBPT_SP_REGION_XZQX_XZQXID(in.getAddAreaContain_cityValue());
		} 
		
		// 如果区县级单位不为空,则添加区县级
		else {

			region_XZQX.setNBPT_SP_REGION_XZQX_XZQXID(in.getAddAreaContain_contyValue());
		}

		try {

			this.before();
			
			regionManageDao.postAddRegionXzqx(region_XZQX, this.getConnection());
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("添加地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	
	@Override
	public ModelAndView postDeleteRegionXzqx(String regionUid, String regionId, String cityValue) throws Exception {

		try {
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regionController/checkXzqxs?regionUid=" + regionUid);
			
			// 删除该下辖区县
			regionManageDao.deleteRegionXzqx(regionId, cityValue, this.getConnection());
			
			// 删除相关负责终端信息
			regionManageDao.deletePersonXzqx(cityValue, this.getConnection());
			
			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.error("删除地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	@Override
	public ModelAndView getcheckProvince(String regionUid) throws Exception {
		try {
			this.before();

			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030208"));
			

			// 查询部门信息
			NBPT_VIEW_REGION region = regionManageDao.receiveRegion(regionUid, this.getConnection());
			
			// 2.查询地区下辖省份
			List<NBPT_VIEW_XZQX> region_xzqxs = regionManageDao.receiveRegionXzqx(regionUid, this.getConnection());
			
			mv.addObject("regionInfo", region);
			mv.addObject("provinces", region_xzqxs);
			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.error("删除地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	@Override
	public String receiveAllSection(ReceiveSectionIn in) {
		try {
			this.before();
			// 初始化返回结果
			JsonDataBack back = new JsonDataBack();
			switch (in.getType()) {
			case "region":

				List<RmShow> regionBackList = new ArrayList<>();
				// 查询到该分类下所有大区,及大区下辖省份
				List<NBPT_VIEW_REGION> regions = regionManageDao.receiveRegion(null, "1", null, this.getConnection());
				regions = CommonUtil.getListInMapByKey(CommonUtil.classify(regions, "NBPT_SP_REGION_TYPE", NBPT_VIEW_REGION.class), in.getTypeValue());
				// 查询大区下省份
				List<NBPT_VIEW_XZQX> regionXzqxs = regionManageDao.receiveXzqxs(null, "1", "11", this.getConnection());
				regionXzqxs = CommonUtil.getListInMapByKey(CommonUtil.classify(regionXzqxs, "NBPT_SP_REGION_TYPE", NBPT_VIEW_XZQX.class), in.getTypeValue());
				// 对省份进行分类
				Map<String, List<NBPT_VIEW_XZQX>>  classifiedXzqxs = CommonUtil.classify(regionXzqxs, "NBPT_SP_REGION_UID", NBPT_VIEW_XZQX.class);
				for(int i = 0 ; i < regions.size(); i++) {
					
					RmShow region = new RmShow();
					region.setSECTION_FLAG(regions.get(i).getNBPT_SP_REGION_FLAG());
//					region.setSECTION_NAME(regions.get(i).getNBPT_SP_REGION_NAME()); // 部门名称
					region.setSECTION_PARENT_NAME(""); // 上级部门
					region.setSECTION_PARENT_UID(""); // 上级部门UID
					region.setSECTION_RESPONSIBLER_NAME(regions.get(i).getNBPT_SP_REGION_RESPONSIBLER_NAME()); // 部门负责人
					region.setSECTION_RESPONSIBLER_PID(regions.get(i).getNBPT_SP_REGION_PARENT_RESPONSIBLER()); // 负责人ID
					region.setSECTION_TYPE(regions.get(i).getNBPT_SP_REGION_TYPE());
					region.setSECTION_UID(regions.get(i).getNBPT_SP_REGION_UID());
					region.setSECTION_HTMLID("tr_" + String.valueOf(i));
					region.setSECTION_ISPARENT("true");
					region.setSECTION_CREATETIME(regions.get(i).getNBPT_SP_REGION_CREATETIME());
					region.setSECTION_CHANGEABLE(true);
					region.setSECTION_ADDABLE(true);
					region.setSECTION_LEVEL("region");
					regionBackList.add(region);
					List<NBPT_VIEW_XZQX> currentXzqxs = CommonUtil.getListInMapByKey(classifiedXzqxs, regions.get(i).getNBPT_SP_REGION_UID());
					region.setSECTION_NAME(regions.get(i).getNBPT_SP_REGION_NAME() + "&nbsp&nbsp&nbsp&nbsp(" + currentXzqxs.size() + ")"); // 部门名称
					for(int j = 0 ; j < currentXzqxs.size() ; j ++) {
						
						RmShow regionChildren = new RmShow();
						regionChildren.setSECTION_FLAG("1");
						regionChildren.setSECTION_NAME(currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_PNAME() + "  " + currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_NAME()); // 部门名称
						regionChildren.setSECTION_PARENT_NAME(region.getSECTION_NAME()); // 上级部门
						regionChildren.setSECTION_PARENT_UID(""); // 上级部门UID
						regionChildren.setSECTION_RESPONSIBLER_NAME(region.getSECTION_RESPONSIBLER_NAME()); // 部门负责人
						regionChildren.setSECTION_RESPONSIBLER_PID(region.getSECTION_RESPONSIBLER_PID()); // 负责人ID
						regionChildren.setSECTION_TYPE("");
						regionChildren.setSECTION_UID(currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_ID());
						regionChildren.setSECTION_HTMLID("tr_" + String.valueOf(i) + "_" + String.valueOf(j));
						regionChildren.setSECTION_LEVEL("province");
						regionBackList.add(regionChildren);
					}
				}
				back.setData(JSONArray.fromObject(regionBackList).toString());
				return this.after(back.toJsonString());
			// 如果是大区,则查询该大区下的所有省份

				
			case "province":
				List<RmShow> areaBackList = new ArrayList<>();
				List<NBPT_VIEW_REGION> areas = regionManageDao.receiveRegion(null, in.getParentId(), this.getConnection());
				areas = CommonUtil.getListInMapByKey(CommonUtil.classify(areas, "NBPT_SP_REGION_TYPE", NBPT_VIEW_REGION.class), in.getTypeValue());
				List<NBPT_VIEW_XZQX> areaXzqxs = regionManageDao.receiveXzqxsByProvince(in.getParentId(), this.getConnection());
				Map<String, List<NBPT_VIEW_XZQX>> classfiedAreaXzqxs_CurrentType = CommonUtil.classify(areaXzqxs, "NBPT_SP_REGION_TYPE", NBPT_VIEW_XZQX.class);
				Map<String, List<NBPT_VIEW_XZQX>>  currentAreas_Xzqxs = CommonUtil.classify(
						CommonUtil.getListInMapByKey(classfiedAreaXzqxs_CurrentType, in.getTypeValue()), "NBPT_SP_REGION_UID", NBPT_VIEW_XZQX.class);
				// 生成数据
				for(int i = 0 ; i < areas.size() ; i++) {
					
					RmShow area = new RmShow();
					area.setSECTION_FLAG(areas.get(i).getNBPT_SP_REGION_FLAG());
//					area.setSECTION_NAME(areas.get(i).getNBPT_SP_REGION_NAME()); // 部门名称
					
					area.setSECTION_PARENT_NAME(""); // 上级部门
					area.setSECTION_PARENT_UID(""); // 上级部门UID
					area.setSECTION_RESPONSIBLER_NAME(areas.get(i).getNBPT_SP_REGION_RESPONSIBLER_NAME()); // 部门负责人
					area.setSECTION_RESPONSIBLER_PID(areas.get(i).getNBPT_SP_REGION_PARENT_RESPONSIBLER()); // 负责人ID
					area.setSECTION_TYPE(areas.get(i).getNBPT_SP_REGION_TYPE());
					area.setSECTION_UID(areas.get(i).getNBPT_SP_REGION_UID());
					area.setSECTION_HTMLID("tr_" + String.valueOf(i));
					area.setSECTION_ISPARENT("true");
					area.setSECTION_CREATETIME(areas.get(i).getNBPT_SP_REGION_CREATETIME());
					area.setSECTION_CHANGEABLE(true);
					area.setSECTION_ADDABLE(true);
					area.setSECTION_LEVEL("area");
					areaBackList.add(area);
					
					List<NBPT_VIEW_XZQX> currentXzqxs = CommonUtil.getListInMapByKey(currentAreas_Xzqxs, areas.get(i).getNBPT_SP_REGION_UID());
					area.setSECTION_NAME(areas.get(i).getNBPT_SP_REGION_NAME() + "&nbsp&nbsp&nbsp&nbsp(" + currentXzqxs.size() + ")"); // 部门名称
					for(int j = 0 ; j < currentXzqxs.size() ; j ++) {
						
						RmShow areaChildren = new RmShow();
						areaChildren.setSECTION_FLAG("1");
						areaChildren.setSECTION_NAME(currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_PNAME() + "  " + currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_NAME()); // 部门名称
						areaChildren.setSECTION_PARENT_NAME(area.getSECTION_NAME()); // 上级部门
						areaChildren.setSECTION_PARENT_UID(""); // 上级部门UID
						areaChildren.setSECTION_RESPONSIBLER_NAME(area.getSECTION_RESPONSIBLER_NAME()); // 部门负责人
						areaChildren.setSECTION_RESPONSIBLER_PID(area.getSECTION_RESPONSIBLER_PID()); // 负责人ID
						areaChildren.setSECTION_TYPE("");
						areaChildren.setSECTION_UID(currentXzqxs.get(j).getNBPT_COMMON_XZQXHF_ID());
						areaChildren.setSECTION_HTMLID("tr_" + String.valueOf(i) + "_" + String.valueOf(j));
						areaChildren.setSECTION_DELETEABLE(true);
						areaChildren.setSECTION_LEVEL("xzqx");
						areaBackList.add(areaChildren);
					}
				}
				back.setData(JSONArray.fromObject(areaBackList).toString());
				return this.after(back.toJsonString());
			}
			return this.after(back.toJsonString());
						
		} catch (Exception e) {

			log.error("添加大区下辖省份出错" + CommonUtil.getTraceInfo());
			// 初始化返回结果
			JsonDataBack back = new JsonDataBack();
			back.setStateError("后台错误,请联系系统管理员");
			return this.after(back.toJsonString());
		}
	}
	
	@Override
	public String getAddSection(AddSectionIn in) throws Exception {

		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			
			// 判断当前操作的分类
			List<NBPT_COMMON_DICTIONARY> dics = regionManageDao.receiveDictionarys("SAILTYPE", this.getConnection());
			switch(in.getType()) {
			case "type":// 如果是添加大区
				/**
				 * 获取添加一个大区的页面
				 * 1.首先要知道添加大区的类型,是OTC还是商务等,由前台参数提供,传递给out中的currentType
				 * 2.没有了
				 */
				CreateForm regionform = new CreateForm("addNewRegion", "添加大区");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						regionform.getColumns().add(new FormItem("type","新大区类型","text",true, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME(), ""));
						regionform.getColumns().add(new FormItem("typeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), ""));
						regionform.getColumns().add(new FormItem("newType", "", "hidden", false, "region", ""));
						break;
					}
				}
				regionform.getColumns().add(new FormItem("newSectionName", "新大区名称", "text", false, true));
				regionform.getColumns().add(new FormItem("newSectionNeed", "新大区配额", "text", false, true));
				regionform.getColumns().add(new FormItem("newSectionNote", "新大区备注", "text", false, "", ""));
				back.setStateOk();
				back.setData(regionform.toJsonString());
				return this.after(back.toJsonString());
			case "region":// 如果是添加下辖省份
				/**
				 * 获取添加省份的页面
				 * 1.首先要知道给哪个大区添加下辖省份
				 * 2.获取省份列表
				 */
				NBPT_VIEW_REGION parentRegion = regionManageDao.receiveRegion(in.getParentId(), this.getConnection());
				CreateForm provinceform = new CreateForm("addNewProvince", "添加下辖省份");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						provinceform.getColumns().add(new FormItem("type","上级部门","text",true
								, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() + "  " + parentRegion.getNBPT_SP_REGION_NAME(), ""));
						provinceform.getColumns().add(new FormItem("typeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), ""));
						provinceform.getColumns().add(new FormItem("newType", "", "hidden", false, "province", ""));
						provinceform.getColumns().add(new FormItem("parentId", "", "hidden", false, parentRegion.getNBPT_SP_REGION_UID(), ""));
						break;
					}
				}
				FormItem provinceSelect = new FormItem("newXzqxid", "选择省份", "select", false, "", "");provinceform.getColumns().add(provinceSelect);
				List<NBPT_COMMON_XZQXHF> provinceLists = regionManageDao.getXzqxhfs(null, this.getConnection());
				provinceLists.add(0,new NBPT_COMMON_XZQXHF());
				for(NBPT_COMMON_XZQXHF province : provinceLists) {
					Option option = new Option();
					provinceSelect.getOptions().add(option);
					option.setName(province.getNBPT_COMMON_XZQXHF_NAME());
					option.setValue(province.getNBPT_COMMON_XZQXHF_ID());
				}
				back.setStateOk();
				back.setData(provinceform.toJsonString());
				return this.after(back.toJsonString());
			case "province":
				/**
				 * 获取添加地区的FORM
				 * 1.获取省份
				 */
				NBPT_VIEW_XZQX parentProvince = regionManageDao.receiveXzqx(in.getParentId(), "11", connection);
				
				if(null == parentProvince) {
					back.setStateOk("该省份没有绑定大区");
					return this.after(back.toJsonString());
				}
				CreateForm areaform = new CreateForm("addNewArea", "添加地区");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						areaform.getColumns().add(new FormItem("type","上级部门","text",true
								, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() + "  " 
										+ parentProvince.getNBPT_SP_REGION_NAME() + "  " 
										+ parentProvince.getNBPT_COMMON_XZQXHF_NAME(), ""));
						areaform.getColumns().add(new FormItem("typeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), ""));
						areaform.getColumns().add(new FormItem("newType", "", "hidden", false, "area", ""));
						areaform.getColumns().add(new FormItem("parentId", "", "hidden", false, parentProvince.getNBPT_COMMON_XZQXHF_ID(), ""));
						break;
					}
				}
				areaform.getColumns().add(new FormItem("newSectionName", "新地区名称", "text", false, true));
				areaform.getColumns().add(new FormItem("newSectionNeed", "新地区配额", "text", false, true));
				areaform.getColumns().add(new FormItem("newSectionNote", "新地区备注", "text"));
				back.setData(areaform.toJsonString());
				return this.after(back.toJsonString());
			case "area":
				/**
				 * 获取添加行政区县的FORM
				 * 1.获取地区
				 */
				NBPT_VIEW_REGION parentArea = regionManageDao.receiveRegion(in.getParentId(), this.getConnection());
				CreateForm xzqxform = new CreateForm("addNewXzqx", "添加下辖行政区县");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						xzqxform.getColumns().add(new FormItem("type","上级部门","text",true
								, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() + "  " + parentArea.getNBPT_SP_REGION_PARENT_NAME()
								+ "  " + parentArea.getNBPT_SP_REGION_PROVINCE_NAME() + "  " + parentArea.getNBPT_SP_REGION_NAME()
								, ""));
						xzqxform.getColumns().add(new FormItem("typeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), ""));
						xzqxform.getColumns().add(new FormItem("newType", "", "hidden", false, "xzqx", ""));
						xzqxform.getColumns().add(new FormItem("parentId", "", "hidden", false, parentArea.getNBPT_SP_REGION_UID(), ""));
						break;
					}
				}

				List<NBPT_COMMON_XZQXHF> xzqxList = regionManageDao.getXzqxhfs(parentArea.getNBPT_SP_REGION_PROVINCE_ID(), this.getConnection());
				xzqxList.add(0, new NBPT_COMMON_XZQXHF());
				FormItem citySelect = new FormItem("newXzqxid_city", "新下辖市区", "select", false, "", "");xzqxform.getColumns().add(citySelect);
				for(NBPT_COMMON_XZQXHF xzqx : xzqxList) {
					Option option = new Option();
					citySelect.getOptions().add(option);
					option.setName(xzqx.getNBPT_COMMON_XZQXHF_NAME());
					option.setValue(xzqx.getNBPT_COMMON_XZQXHF_ID());
				}
				xzqxform.getColumns().add(new FormItem("newXzqxid_conty", "新下辖县", "select", false, "", ""));
				back.setStateOk();
				back.setData(xzqxform.toJsonString());
				return this.after(back.toJsonString());
				default: break;
			}
			return this.after("");
		} catch (Exception e) {
			throw new Exception();
		}
	}
	@Override
	public String postAddSection(AddSectionIn in) {

		try {
			this.before();
			switch (in.getNewType()) {
			case "region":// 如果新添加一个大区
				
				String newRegionId = regionManageDao.receiveMaxId(this.getConnection()
						, "NBPT_SP_REGION", "WHERE NBPT_SP_REGION_LEVEL = '1' AND NBPT_SP_REGION_TYPE = '" + in.getTypeValue() + "'");
				
				if(null == newRegionId) {
					newRegionId = "01";
				}
				
				else if(1 == newRegionId.length()) {
					newRegionId = 0 + newRegionId;
				}
				
				NBPT_SP_REGION newRegion = new NBPT_SP_REGION();
				newRegion.setNBPT_SP_REGION_UID(CommonUtil.getUUID_32());
				newRegion.setNBPT_SP_REGION_ID(newRegionId);
				newRegion.setNBPT_SP_REGION_NAME(in.getNewSectionName());
				newRegion.setNBPT_SP_REGION_ONAME(in.getNewSectionNeed());
				newRegion.setNBPT_SP_REGION_LEVEL("1");
				newRegion.setNBPT_SP_REGION_NOTE(in.getNewSectionNote());
				newRegion.setNBPT_SP_REGION_FLAG("1");
				newRegion.setNBPT_SP_REGION_CREATETIME(CommonUtil.getYYYYMMDD());
				newRegion.setNBPT_SP_REGION_TYPE(in.getTypeValue());
				
				// 添加新的大区
				regionManageDao.addRegion(newRegion, this.getConnection());
				regionManageDao.addRecord(newRegion.getNBPT_SP_REGION_UID()
						, this.getUserName(), "7", this.getConnection(), "添加了一个新的大区:" + newRegion.getNBPT_SP_REGION_NAME());
				
				// 添加新的负责关系
				NBPT_SP_PERSON_REGION person_region = new NBPT_SP_PERSON_REGION();
				person_region.setNBPT_SP_PERSON_REGION_UID(CommonUtil.getUUID_32());
				person_region.setNBPT_SP_PERSON_REGION_PID("");
				person_region.setNBPT_SP_PERSON_REGION_UID(newRegion.getNBPT_SP_REGION_UID());
				person_region.setNBPT_SP_PERSON_REGION_TYPE("1");
				regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "INSERT", "NBPT_SP_PERSON_REGION", person_region), this.getConnection());
				
				break;
	
			case "province":// 如果新添加一个省份
				
				// 取出大区
				NBPT_VIEW_REGION parentRegion = regionManageDao.receiveRegion(in.getParentId(), this.getConnection());
				
				// 取出该省份原绑定关系
				NBPT_VIEW_XZQX oldRegion_xzqx = regionManageDao.receiveRegion_Xzqxs(in.getTypeValue(), in.getNewXzqxid(), "11", this.getConnection());
				
				if(null != oldRegion_xzqx) {// 如果原行政区县有绑定关系,则修改
					// 设定新的绑定关系
					NBPT_SP_REGION_XZQX updateRegionXzqx = new NBPT_SP_REGION_XZQX();
					updateRegionXzqx.setNBPT_SP_REGION_XZQX_UID(oldRegion_xzqx.getNBPT_SP_REGION_XZQX_UID());
					updateRegionXzqx.setNBPT_SP_REGION_XZQX_REGIONUID(parentRegion.getNBPT_SP_REGION_UID());
					updateRegionXzqx.setNBPT_SP_REGION_XZQX_REGIONID(parentRegion.getNBPT_SP_REGION_ID());
					// 执行更新
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "update", "NBPT_SP_REGION_XZQX", updateRegionXzqx), this.getConnection());
					regionManageDao.addRecord(in.getNewXzqxid(), this.getUserName(), "17", this.getConnection()
							, "为大区" + parentRegion.getNBPT_SP_REGION_NAME() + "绑定了下辖省份" + oldRegion_xzqx.getNBPT_COMMON_XZQXHF_ID());
				} else { // 如果没有绑定关系,则添加
					// 添加地区省份绑定关系
					NBPT_SP_REGION_XZQX newAreaXzqx = new NBPT_SP_REGION_XZQX();
					newAreaXzqx.setNBPT_SP_REGION_XZQX_UID(CommonUtil.getUUID_32());
					newAreaXzqx.setNBPT_SP_REGION_XZQX_TYPE("11");
					newAreaXzqx.setNBPT_SP_REGION_XZQX_XZQXID(in.getNewXzqxid());
					newAreaXzqx.setNBPT_SP_REGION_XZQX_REGIONID(parentRegion.getNBPT_SP_REGION_ID());
					newAreaXzqx.setNBPT_SP_REGION_XZQX_REGIONUID(parentRegion.getNBPT_SP_REGION_UID());

					// 执行添加
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "INSERT", "NBPT_SP_REGION_XZQX", newAreaXzqx), this.getConnection());
					regionManageDao.addRecord(in.getNewXzqxid(), this.getUserName(), "17", this.getConnection()
							, "为大区" + parentRegion.getNBPT_SP_REGION_NAME() + "绑定了下辖省份" + in.getNewXzqxid());
					
				}
				
				// 因为部门的ID以后准备弃置不用,所有这里不修改ID了
				break;
			case "area" :// 如果新添加一个地区
				
				String newAreaId = regionManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_REGION"
						, "WHERE NBPT_SP_REGION_LEVEL = '2' "
								+ "AND NBPT_SP_REGION_PROVINCE_ID = '" + in.getParentId() + "' "
								+ "AND NBPT_SP_REGION_TYPE = '" + in.getTypeValue() + "'");
				if(null == newAreaId) {
					newAreaId = in.getParentId().substring(0,2) + "01";
				}
				
				NBPT_SP_REGION newArea = new NBPT_SP_REGION();
				newArea.setNBPT_SP_REGION_UID(CommonUtil.getUUID_32());
				newArea.setNBPT_SP_REGION_ID(newAreaId);
				newArea.setNBPT_SP_REGION_NAME(in.getNewSectionName());
				newArea.setNBPT_SP_REGION_ONAME(in.getNewSectionNeed());
				newArea.setNBPT_SP_REGION_LEVEL("2");
				newArea.setNBPT_SP_REGION_NOTE(in.getNewSectionNote());
				newArea.setNBPT_SP_REGION_FLAG("1");
				newArea.setNBPT_SP_REGION_CREATETIME(CommonUtil.getYYYYMMDD());
				newArea.setNBPT_SP_REGION_TYPE(in.getTypeValue());
				newArea.setNBPT_SP_REGION_PROVINCE_ID(in.getParentId());
				
				// 添加新的大区
				regionManageDao.addRegion(newArea, this.getConnection());
				regionManageDao.addRecord(newArea.getNBPT_SP_REGION_UID()
						, this.getUserName(), "6", this.getConnection(), "添加了一个新的地区:" + newArea.getNBPT_SP_REGION_NAME());
				
				// 添加地区省份绑定关系
				NBPT_SP_REGION_XZQX newAreaProvince = new NBPT_SP_REGION_XZQX();
				newAreaProvince.setNBPT_SP_REGION_XZQX_UID(CommonUtil.getUUID_32());
				newAreaProvince.setNBPT_SP_REGION_XZQX_TYPE("21");
				newAreaProvince.setNBPT_SP_REGION_XZQX_XZQXID(in.getParentId());
				newAreaProvince.setNBPT_SP_REGION_XZQX_REGIONID(newArea.getNBPT_SP_REGION_ID());
				newAreaProvince.setNBPT_SP_REGION_XZQX_REGIONUID(newArea.getNBPT_SP_REGION_UID());
				regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "INSERT", "NBPT_SP_REGION_XZQX", newAreaProvince), this.getConnection());
				regionManageDao.addRecord(in.getNewXzqxid(), this.getUserName(), "17", this.getConnection()
						, "为地区" + newArea.getNBPT_SP_REGION_NAME() + "绑定了省份" + in.getNewXzqxid());
				// 添加新的负责关系
				NBPT_SP_PERSON_REGION person_area = new NBPT_SP_PERSON_REGION();
				person_area.setNBPT_SP_PERSON_REGION_UID(CommonUtil.getUUID_32());
				person_area.setNBPT_SP_PERSON_REGION_PID("");
				person_area.setNBPT_SP_PERSON_REGION_UID(newArea.getNBPT_SP_REGION_UID());
				person_area.setNBPT_SP_PERSON_REGION_TYPE("1");
				regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "INSERT", "NBPT_SP_PERSON_REGION", person_area), this.getConnection());
				break;
			case "xzqx":// 如果新添加一个地区绑定行政区县

				String newXzqxid = (null == in.getNewXzqxid_conty() || "".equals(in.getNewXzqxid_conty())) 
						? in.getNewXzqxid_city() : in.getNewXzqxid_conty();
						
				// 取出地区
				NBPT_VIEW_REGION parentArea = regionManageDao.receiveRegion(in.getParentId(), this.getConnection());
				
				// 取出原绑定关系
				NBPT_VIEW_XZQX oldAreaxzqx = regionManageDao.receiveRegion_Xzqxs(in.getTypeValue(), newXzqxid, "22", this.getConnection());
				
						
				if(null != oldAreaxzqx) {// 如果原行政区县有绑定关系,则修改
					// 设定新的绑定关系
					NBPT_SP_REGION_XZQX updateArea_xzqx = new NBPT_SP_REGION_XZQX();
					updateArea_xzqx.setNBPT_SP_REGION_XZQX_UID(oldAreaxzqx.getNBPT_SP_REGION_XZQX_UID());
					updateArea_xzqx.setNBPT_SP_REGION_XZQX_REGIONUID(parentArea.getNBPT_SP_REGION_UID());
					updateArea_xzqx.setNBPT_SP_REGION_XZQX_REGIONID(parentArea.getNBPT_SP_REGION_ID());
					
					// 执行更新
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "update", "NBPT_SP_REGION_XZQX", updateArea_xzqx), this.getConnection());
					regionManageDao.addRecord(newXzqxid, this.getUserName(), "17", this.getConnection()
							, "为地区" + parentArea.getNBPT_SP_REGION_NAME() + "绑定了下辖市县" + newXzqxid);
				} else { // 如果没有绑定关系,则添加
					NBPT_SP_REGION_XZQX newAreaXzqx = new NBPT_SP_REGION_XZQX();
					newAreaXzqx.setNBPT_SP_REGION_XZQX_UID(CommonUtil.getUUID_32());
					newAreaXzqx.setNBPT_SP_REGION_XZQX_TYPE("22");
					newAreaXzqx.setNBPT_SP_REGION_XZQX_XZQXID(newXzqxid);
					newAreaXzqx.setNBPT_SP_REGION_XZQX_REGIONID(parentArea.getNBPT_SP_REGION_ID());
					newAreaXzqx.setNBPT_SP_REGION_XZQX_REGIONUID(parentArea.getNBPT_SP_REGION_UID());
					
					// 执行添加
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "INSERT", "NBPT_SP_REGION_XZQX", newAreaXzqx), this.getConnection());
					regionManageDao.addRecord(newXzqxid, this.getUserName(), "17", this.getConnection()
							, "为地区" + parentArea.getNBPT_SP_REGION_NAME() + "绑定了下辖市县" + newXzqxid);
					
				}
				break;
			default:
				break;
			}
			JsonDataBack back = new JsonDataBack();
			return this.after(back.toJsonString());
		} catch (SQLException e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}
	
	@Override
	public String getChangeSection(ChangeSectionIn in) throws Exception {
		
		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			// 获取修改对象
			NBPT_VIEW_REGION currentRegion = regionManageDao.receiveRegion(in.getSECTION_UID(), this.getConnection());
			
			switch (currentRegion.getNBPT_SP_REGION_LEVEL()) {
			case "1": // 如果是大区
				CreateForm regionform = new CreateForm("changeRegion", "修改大区");
				regionform.getColumns().add(new FormItem("parentName", "所属分类", "text", true
						, currentRegion.getNBPT_SP_REGION_REALTYPE(), "")); // 添加大区名称
				regionform.getColumns().add(new FormItem("regionUid", "", "hidden", true, in.getSECTION_UID(), "")); // 添加隐藏UID
				regionform.getColumns().add(new FormItem("regionName", "大区名称", "text", false, currentRegion.getNBPT_SP_REGION_NAME(), "")); // 添加大区名称
				regionform.getColumns().add(new FormItem("regionResponsiblerOld", "", "hidden", false, currentRegion.getNBPT_SP_REGION_RESPONSIBLER(), ""));
				
				FormItem regionResponsiblerNames = new FormItem("regionResponsiblerNew", "负责人", "select", false, currentRegion.getNBPT_SP_REGION_RESPONSIBLER_NAME(), "");
				Option option = new Option(currentRegion.getNBPT_SP_REGION_RESPONSIBLER(), currentRegion.getNBPT_SP_REGION_RESPONSIBLER_NAME(), true);
				regionResponsiblerNames.getOptions().add(option);
				
				// 查询当前所有可用大区总
				List<NBPT_VIEW_CURRENTPERSON> regionResponsbers = regionManageDao.receiveAbleResper("region", in.getSECTION_TYPE(), this.getConnection());
				
				// 只选取在职人员
				for(NBPT_VIEW_CURRENTPERSON person : 
							CommonUtil.getListInMapByKey(CommonUtil.classify(regionResponsbers, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class), "2")) {
					if(null == person.getNBPT_SP_PERSON_REGION_RESPONSIBLER_PID() || "".equals(person.getNBPT_SP_PERSON_REGION_RESPONSIBLER_PID()) ) {
						Option newOption = new Option(person.getNBPT_SP_PERSON_PID(), person.getNBPT_SP_PERSON_NAME());
						regionResponsiblerNames.getOptions().add(newOption);
					}
				}
				regionform.getColumns().add(regionResponsiblerNames);
//				regionform.getColumns().add(new FormItem("regionResponsiblerNew", "负责人", "text", true, currentRegion.getNBPT_SP_REGION_RESPONSIBLER_NAME(), ""));

				regionform.getColumns().add(new FormItem("regionNeed", "大区配额", "text", false, currentRegion.getNBPT_SP_REGION_NEED(), ""));
				FormItem regionRadios = new FormItem("regionFlag", "状态", "radio", false, "", "");
				regionRadios.getRadios().add(new Radio("1", "启用", "1".equals(currentRegion.getNBPT_SP_REGION_FLAG().trim()) ? true : false));
				regionRadios.getRadios().add(new Radio("0", "停用", "0".equals(currentRegion.getNBPT_SP_REGION_FLAG().trim()) ? true : false));
				regionform.getColumns().add(regionRadios);
				regionform.getColumns().add(new FormItem("regionNote", "备注", "textarea", false, currentRegion.getNBPT_SP_REGION_NOTE(), ""));
				back.setData(regionform.toJsonString());
				return this.after(back.toJsonString());
				
			case "2": // 如果是地区
				// 获取大区相关信息
				NBPT_VIEW_REGION area = regionManageDao.receiveRegion(in.getSECTION_UID(), this.getConnection());
				CreateForm areaform = new CreateForm("changeArea", "修改地区");
				areaform.getColumns().add(new FormItem("parentName", "上级部门", "text", true, area.getNBPT_SP_REGION_PARENT_NAME() 
						+ "大区 " + area.getNBPT_SP_REGION_PROVINCE_NAME(), "")); // 上级部门				
				areaform.getColumns().add(new FormItem("regionUid", "", "hidden", true, in.getSECTION_UID(), "")); // 添加隐藏UID
				areaform.getColumns().add(new FormItem("regionName", "地区名称", "text", false, area.getNBPT_SP_REGION_NAME(), "")); // 添加大区名称
				areaform.getColumns().add(new FormItem("regionResponsiblerOld", "", "hidden", false, area.getNBPT_SP_REGION_RESPONSIBLER(), ""));
				
				FormItem areaResponsiblerNames = new FormItem("regionResponsiblerNew", "负责人", "select", false, area.getNBPT_SP_REGION_RESPONSIBLER_NAME(), "");
				Option areaResOption = new Option(area.getNBPT_SP_REGION_RESPONSIBLER(), area.getNBPT_SP_REGION_RESPONSIBLER_NAME(), true);
				areaResponsiblerNames.getOptions().add(areaResOption);
				
				// 查询当前所有可用地区总
				List<NBPT_VIEW_CURRENTPERSON> areaResponsbers = regionManageDao.receivePerson(new String[] {"22" ,  "26"}
																				, null, area.getNBPT_SP_REGION_TYPE(), this.getConnection());
				for(NBPT_VIEW_CURRENTPERSON person : areaResponsbers) {
					if(null == person.getNBPT_SP_PERSON_AREA_RESPONSIBLER_PID() || "".equals(person.getNBPT_SP_PERSON_AREA_RESPONSIBLER_PID()) ) {
						Option newOption = new Option(person.getNBPT_SP_PERSON_PID(), person.getNBPT_SP_PERSON_NAME());
						areaResponsiblerNames.getOptions().add(newOption);
					}
				}
				areaform.getColumns().add(areaResponsiblerNames);
				
				areaform.getColumns().add(new FormItem("regionNeed", "地区配额", "text", false, area.getNBPT_SP_REGION_NEED(), ""));
				FormItem areaRadios = new FormItem("regionFlag", "状态", "radio", false, "", "");
				areaRadios.getRadios().add(new Radio("1", "启用", "1".equals(area.getNBPT_SP_REGION_FLAG().trim()) ? true : false));
				areaRadios.getRadios().add(new Radio("0", "停用", "0".equals(area.getNBPT_SP_REGION_FLAG().trim()) ? true : false));
				areaform.getColumns().add(new FormItem("regionNote", "备注", "textarea", false, area.getNBPT_SP_REGION_NOTE(), ""));
				areaform.getColumns().add(areaRadios);
				back.setData(areaform.toJsonString());
				return this.after(back.toJsonString());
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			log.error("获取修改部门信息时出错");
			throw new Exception();
		}
	}
	@Override
	public String postChangeSection(PostChangeSectionIn in) throws Exception {
		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			
			// 获取要修改的部门信息
			NBPT_VIEW_REGION region = regionManageDao.receiveRegion(in.getRegionUid(), this.getConnection());
			
			// 修改部门信息
			NBPT_SP_REGION updateRegion = new NBPT_SP_REGION();
			updateRegion.setNBPT_SP_REGION_UID(region.getNBPT_SP_REGION_UID());
			updateRegion.setNBPT_SP_REGION_FLAG(in.getRegionFlag());
			updateRegion.setNBPT_SP_REGION_NAME(in.getRegionName());
			updateRegion.setNBPT_SP_REGION_ONAME(in.getRegionNeed());
			updateRegion.setNBPT_SP_REGION_NOTE(in.getRegionNote());

			// 如果修改了负责人,同时修改负责人的部门
			if(!in.getRegionResponsiblerNew().equals(in.getRegionResponsiblerOld())) {
				
				// 获取新负责人信息
				NBPT_VIEW_CURRENTPERSON newResper = regionManageDao.receivePerson(in.getRegionResponsiblerNew(), this.getConnection());
				// 获取部门负责关系
				NBPT_SP_PERSON_REGION updateNewP_R = regionManageDao.receivePersonRegionResper(in.getRegionUid(), this.getConnection());
				if(updateNewP_R == null) { 
					updateNewP_R = new NBPT_SP_PERSON_REGION();
					updateNewP_R.setNBPT_SP_PERSON_REGION_UID(CommonUtil.getUUID_32());
					updateNewP_R.setNBPT_SP_PERSON_REGION_RID(in.getRegionUid());
					updateNewP_R.setNBPT_SP_PERSON_REGION_TYPE("1");
					// 添加新的负责关系
					updateNewP_R.setNBPT_SP_PERSON_REGION_PID(in.getRegionResponsiblerNew());
					// 更新负责关系
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "INSERT", "NBPT_SP_PERSON_REGION", updateNewP_R), this.getConnection());
					
				} else {
					updateNewP_R.setNBPT_SP_PERSON_REGION_PID(in.getRegionResponsiblerNew());
					// 更新负责关系
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "UPDATE", "NBPT_SP_PERSON_REGION", updateNewP_R), this.getConnection());
				}
				switch (region.getNBPT_SP_REGION_LEVEL()) {
				case "2": // 如果是修改地区的负责人
					switch (newResper.getNBPT_SP_PERSON_JOB()) {
					case "26"://如果是给混合大区总分配地区,则不需要修改部门从属关系
						break;
					case "22":// 如果是给地总分配地区,则从属关系和负责关系都要修改
						// 还要修改新负责人从属关系
						NBPT_SP_PERSON_REGION updateP_R = regionManageDao.receivePersonRegion(in.getRegionResponsiblerNew(), this.getConnection());
						updateP_R.setNBPT_SP_PERSON_REGION_RID(in.getRegionUid());
						regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "update", "NBPT_SP_PERSON_REGION", updateP_R), this.getConnection());
						
						// 如果原负责人存在
						if(!"".equals(in.getRegionResponsiblerOld())) {
							// 解除原负责人从属关系
							NBPT_SP_PERSON_REGION updateOldP_R = regionManageDao.receivePersonRegion(in.getRegionResponsiblerOld(), this.getConnection());
							updateOldP_R.setNBPT_SP_PERSON_REGION_RID("");
							regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "update", "NBPT_SP_PERSON_REGION", updateOldP_R), this.getConnection());
						}
					default:
						break;
					}
					break;
				case "1": // 如果修改大区的负责人
					// 还要修改新负责人从属关系
					NBPT_SP_PERSON_REGION updateP_R = regionManageDao.receivePersonRegion(in.getRegionResponsiblerNew(), this.getConnection());
					updateP_R.setNBPT_SP_PERSON_REGION_RID(in.getRegionUid());
					regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "update", "NBPT_SP_PERSON_REGION", updateP_R), this.getConnection());
					
					// 如果原负责人存在
					if(!"".equals(in.getRegionResponsiblerOld())) {
						// 解除原负责人从属关系
						NBPT_SP_PERSON_REGION updateOldP_R = regionManageDao.receivePersonRegion(in.getRegionResponsiblerOld(), this.getConnection());
						updateOldP_R.setNBPT_SP_PERSON_REGION_RID("");
						regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "update", "NBPT_SP_PERSON_REGION", updateOldP_R), this.getConnection());
					}
					break;
				default:
					break;
				}
			}
			
			regionManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_REGION.class, "update", "NBPT_SP_REGION", updateRegion), this.getConnection());
			regionManageDao.addRecord(region.getNBPT_SP_REGION_UID(), this.getUserName(), "16", this.getConnection()
					, CommonUtil.getYYYYMMDD() + "修改部门" + region.getNBPT_SP_REGION_NAME() + "的信息");
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("修改出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}
	

	
	
	
	

}
