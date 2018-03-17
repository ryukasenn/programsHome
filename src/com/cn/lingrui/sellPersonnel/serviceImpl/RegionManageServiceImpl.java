package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.AddRegionPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
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
	 * 获取查看大区页面输入框自动补全信息
	 */
	@Override
	public String receiveRegionsSelect() throws Exception {

		try {
			
			this.before();
	
			List<NBPT_SP_REGION> regions = regionManageDao.receiveRegionsSelect(this.getConnection());
	
			JSONArray ja = JSONArray.fromObject(regions);
	
			return this.after(ja.toString());
			
		} catch (SQLException e) {
			
			log.error("获取大区信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 获取负责人信息
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
			
			log.error("获取分配负责人列表出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 获取修改部门信息页面
	 */
	@Override
	public ModelAndView getChangeRegion(RegionsPojo pojo) throws Exception {
		
		try {
			
			this.before();
	
			// 初始化返回结果
			ModelAndView mv = null;
			
			// 返回地区信息
			NBPT_SP_REGION regionInfo = regionManageDao.receiveCurrentRegion(pojo, this.getConnection());
			
			// 获取负责人信息
			NBPT_SP_PERSON personInfo = regionManageDao.receiveCurrentPerson(regionInfo.getNBPT_SP_REGION_RESPONSIBLER(), this.getConnection());
			
			if(2 == pojo.getRegionId().length()) {

				// 如果是大区,获取大区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030203", this.getRole()));
				
			}else if(6 == pojo.getRegionId().length()) {

				// 如果是地区,获取地区信息及配置处理页面
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030204", this.getRole()));
				
				// 获取省份信息下拉框列表
				List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.receiveProvinceSelect(this.getConnection());
				
				provinces.add(new NBPT_COMMON_XZQXHF());
				mv.addObject("provinces", provinces);
				
				// 获取本地区所属省份
				List<Area_Xzqx_Info> currentXzqxs = regionManageDao.receiveCurrentXzqxs(pojo.getRegionId(), this.getConnection());
				
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
			
		} catch (Exception e) {
			
			log.error("修改部门出错" + CommonUtil.getTraceInfo());
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
				
				List<NBPT_COMMON_XZQXHF> provinces = regionManageDao.receiveProvinceSelect(this.getConnection());
				
				JSONArray ja = JSONArray.fromObject(provinces);
		
				return this.after(ja.toString());
			
		} catch (Exception e) {
			
			log.error("查询省份信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}
	}
	
	/**
	 * 获取地区下辖区县划分页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getCheckXzqxs(RegionsPojo in) throws Exception {
		
		try {
			
			this.before();
			
			// 初始化返回結果
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030205"));
			// 获取要设置的地区编号
			String regionUid = in.getRegionId();
			
			// 1.根据编号，查询地区当前的划分情况
			List<CurrentRegion> region_xzqxs = regionManageDao.receiveRegion_Xzqxs(regionUid, this.getConnection());
		
			// 2.处理查询到的数据
			
			// 2.1分离出省份信息
			for(CurrentRegion currentRegion : region_xzqxs) {
				
				if("21".equals(currentRegion.getNBPT_SP_REGION_XZQX_TYPE()) && "1".equals(currentRegion.getNBPT_COMMON_XZQXHF_LEVEL())) {
					
					mv.addObject("currentProvince", currentRegion.getNBPT_COMMON_XZQXHF_NAME());
					mv.addObject("currentProvinceId", currentRegion.getNBPT_COMMON_XZQXHF_ID());
					mv.addObject("currentAreaName", currentRegion.getNBPT_SP_REGION_NAME());
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
		} catch (Exception e) {
			
			log.error("查询省份信息出错" + CommonUtil.getTraceInfo());
			throw new Exception();
		}	
	}
	

}
