package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
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
	 * 查看当前大区及地区分布分布
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

			List<CurrentRegion> currentRegions = regionManageDao.receiveCurrentRegions(pojo, this.getConnection());
			
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
	 */
	@Override
	public ModelAndView getAddRegion() {
		this.before();
		
		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030203"));
		
		return this.after(mv);
	}
	
	/**
	 * 执行添加大区
	 */
	@Override
	public ModelAndView postAddRegion(AddRegionPojoIn in) {

		this.before();
		
		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030202"));
		
		// 
		return this.after(mv);
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
			NBPT_SP_REGION regionInfo = regionManageDao.receiveCurrentRegion(regionUid, this.getConnection());
			
			// 获取负责人信息
			NBPT_SP_PERSON personInfo = regionManageDao.receiveCurrentPerson(regionInfo.getNBPT_SP_REGION_RESPONSIBLER(), this.getConnection());
			
			if("1".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {

				// 如果是大区,获取大区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030203", this.getRole()));
				
			}else if("2".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {

				// 如果是地区,获取地区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030204", this.getRole()));
				
				// 获取省份信息下拉框列表
				List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.receiveProvinceSelect(this.getConnection());
				
				provinces.add(new NBPT_COMMON_XZQXHF());
				mv.addObject("provinces", provinces);
				
				// 获取本地区所属省份
				List<Area_Xzqx_Info> currentXzqxs = regionManageDao.receiveCurrentXzqxs(regionInfo.getNBPT_SP_REGION_ID(), this.getConnection());
				
				for(Area_Xzqx_Info currentXzqx : currentXzqxs) {
					
					if("21".equals(currentXzqx.getXZQX_TYPE())) {
						
						mv.addObject("currentProvince", currentXzqx);
						currentXzqxs.remove(currentXzqxs.indexOf(currentXzqx));
						break;
					}
				}
			}
			
			mv.addObject("regionInfo", regionInfo);
			mv.addObject("personInfo", personInfo);
			
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("修改部门出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}

	/**
	 * 提交部门基本信息修改
	 */
	@Override
	public ModelAndView postChangeRegion(UpdateRegionPojo pojo) {

		try {
			
			// 1.查询出部门相关信息
			NBPT_SP_REGION regionInfo = regionManageDao.receiveCurrentRegion(pojo.getRegionUid(), this.getConnection());
			
			// 2.设定修改信息
			if("1".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {
				
				// 2.1 如果是大区信息修改,初始化信息
				NBPT_SP_REGION updateInfo = new NBPT_SP_REGION();
				
				// 2.2 设定Uid
				updateInfo.setNBPT_SP_REGION_UID(regionInfo.getNBPT_SP_REGION_UID());
				
				// 2.3 设定其他信息
				RegionManageServiceUtils.setUpdateRegionInfos(updateInfo, pojo, regionInfo);
				
				// 2.4 执行更新
				// TODO
			} else if("2".equals(regionInfo.getNBPT_SP_REGION_LEVEL())) {
				
				// 2.1 如果是地区信息修改,初始化信息
				NBPT_SP_REGION updateInfo = new NBPT_SP_REGION();
				
				// 2.2 设定Uid
				updateInfo.setNBPT_SP_REGION_UID(regionInfo.getNBPT_SP_REGION_UID());
				
				// 2.3 设定其他信息
				RegionManageServiceUtils.setUpdateRegionInfos(updateInfo, pojo, regionInfo);
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * 获取省份信息下拉框
	 * @throws Exception 
	 */
	@Override
	public String receiveProvinceSelect() throws Exception {

		try {
			
			this.before();
				
				List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.receiveProvinceSelect(this.getConnection());
				
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
						
			// 1.根据编号，查询地区当前的划分情况
			List<CurrentRegion> region_xzqxs = regionManageDao.receiveRegion_Xzqxs(regionUid, this.getConnection());
		
			// 2.处理查询到的数据
			
			// 2.1分离出省份信息
			for(CurrentRegion currentRegion : region_xzqxs) {
				
				if("21".equals(currentRegion.getNBPT_SP_REGION_XZQX_TYPE()) && "1".equals(currentRegion.getNBPT_COMMON_XZQXHF_LEVEL())) {
					
					mv.addObject("currentProvince", currentRegion.getNBPT_COMMON_XZQXHF_NAME());
					mv.addObject("currentProvinceId", currentRegion.getNBPT_COMMON_XZQXHF_ID());
					mv.addObject("currentAreaName", currentRegion.getNBPT_SP_REGION_NAME());
					mv.addObject("currentAreaUid", regionUid);
					mv.addObject("currentAreaId", currentRegion.getNBPT_SP_REGION_ID());
					mv.addObject("regionName", currentRegion.getREGION_NAME());
					break;
				}
			}
			
			// 2.2分离出下辖市,区和县
			List<CurrentRegion> citys = new ArrayList<>();
			List<CurrentRegion> contys = new ArrayList<>();
			for(CurrentRegion currentRegion : region_xzqxs) {
				
				if("2".equals(currentRegion.getNBPT_COMMON_XZQXHF_LEVEL())) {
					
					citys.add(currentRegion);
				}
				if("3".equals(currentRegion.getNBPT_COMMON_XZQXHF_LEVEL())) {

					contys.add(currentRegion);
				}
			}
			
			mv.addObject("citys", citys);
			mv.addObject("contys", contys);
			return this.after(mv);			
		} catch (SQLException e) {

			this.closeException();
			log.error("查询省份信息出错" + CommonUtil.getTraceInfo());
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
			log.error("查询省份信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	@Override
	public ModelAndView postAddRegionXzqx(RegionsPojo in) throws Exception {

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/checkXzqxs?regionUid=" + in.getAddAreaContain_regionUid());
		
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
	public ModelAndView postUpdateRegion(UpdateRegionPojo in) throws Exception {
			
		try {
			this.before();
			// 获取部门信息
			NBPT_SP_REGION region = regionManageDao.receiveCurrentRegion(in.getRegionUid(),this.getConnection());
			
			ModelAndView mv = null;
			
			// 判断部门等级,如果为大区
			if("1".equals(region.getNBPT_SP_REGION_LEVEL())) {
				
				// 返回大区列表
				mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/regions?typeRadio=1");
				
				// 设置更新数据
				NBPT_SP_REGION updateRegion = new NBPT_SP_REGION();
				
				// 条件
				updateRegion.setNBPT_SP_REGION_UID(region.getNBPT_SP_REGION_UID());
				
				// 内容
				updateRegion.setNBPT_SP_REGION_ONAME(in.getRegionNeed());
				updateRegion.setNBPT_SP_REGION_RESPONSIBLER(in.getRegionResponsibler());
				updateRegion.setNBPT_SP_REGION_NOTE(in.getRegionNote());
				
				regionManageDao.updateRegion(updateRegion, this.getConnection());
			} 
			
			// 如果是地区
			else if("2".equals(region.getNBPT_SP_REGION_LEVEL())) {
				
				// TODO
			}
			
			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.error("修改部门信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	
	

}
