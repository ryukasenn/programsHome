package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;

public class InfoCommissionerServiceUtils {
	
	/**
	 * 按大区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byRegion(Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedLists, List<NBPT_VIEW_REGION> regions) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(NBPT_VIEW_REGION region : regions) {
			
			List<NBPT_VIEW_CURRENTPERSON> classifyedList = CommonServiceUtils.getPersonsByKey(classifyedLists, region.getNBPT_SP_REGION_UID());
			
			StatisticsTable table = new StatisticsTable();
			
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 注入大区名
				table.setRegionName(region.getNBPT_SP_REGION_NAME());
				
				// 注入大区UID
				table.setRegionUid(region.getNBPT_SP_REGION_UID());
				
				// 注入大区配额
				table.setNeed(CommonUtil.objToInteger(region.getNBPT_SP_REGION_NEED()));
				
				// 分类加1
				table = CommonServiceUtils.count(table, person);
			}
			resultList.add(table);
		}

		CommonServiceUtils.otherCompute(resultList);
		return resultList;
	}

	/**
	 * 按省区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byProvince(Map<String, List<NBPT_VIEW_REGION>> classfyedRegions, 
																		Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedPersons) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(String key : classfyedRegions.keySet()) {
			
			StatisticsTable table = new StatisticsTable();
			
			// 该分类下的地区
			List<NBPT_VIEW_REGION> currentClassifyRegions = CommonServiceUtils.getRegionsByKey(classfyedRegions, key);
			
			// 该分类下的人员
			List<NBPT_VIEW_CURRENTPERSON> currentClassifyPersons = CommonServiceUtils.getPersonsByKey(classifyedPersons, key);
			
			// 计算省份配额			
			for(NBPT_VIEW_REGION region : currentClassifyRegions) {

				table.setNeed(table.getNeed() + CommonUtil.objToInteger(region.getNBPT_SP_REGION_NEED().trim()));
				
				// 注入省份名
				table.setProvinceName(region.getNBPT_SP_REGION_PROVINCE_NAME());
				// 注入省份ID
				table.setProvinceId(region.getNBPT_SP_REGION_PROVINCE_ID());
			}

			for(NBPT_VIEW_CURRENTPERSON person : currentClassifyPersons) {

				// 分类加1
				table = CommonServiceUtils.count(table, person);
			}
			resultList.add(table);
			
		}

		CommonServiceUtils.otherCompute(resultList);
		return resultList;
	}
	
	/**
	 * 按地区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byArea(List<NBPT_VIEW_REGION> regions , Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {

		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(NBPT_VIEW_REGION region : regions) {
			
			StatisticsTable table = new StatisticsTable();
			
			List<NBPT_VIEW_CURRENTPERSON> classifyedList = CommonServiceUtils.getPersonsByKey(classifyedLists, region.getNBPT_SP_REGION_UID());

			// 地区配额
			table.setNeed(CommonUtil.objToInteger(region.getNBPT_SP_REGION_NEED().trim()));

			// 注入地区名
			table.setAreaName(region.getNBPT_SP_REGION_NAME());
			
			// 注入地区UID
			table.setAreaUid(region.getNBPT_SP_REGION_UID());

			// 注入大区UID,供后退使用
			table.setRegionUid(region.getNBPT_SP_REGION_PARENT_UID());
			
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 分类加1
				table = CommonServiceUtils.count(table, person);
			}
			resultList.add(table);
		}
		
		CommonServiceUtils.otherCompute(resultList);
		return resultList;
	}
	
	

}


























