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
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;
import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
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
			NBPT_VIEW_XZQX xzqx = regionManageDao.receiveXzqx(in.getProvinceId(), this.getConnection());
			
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
	


	
	
	

}
