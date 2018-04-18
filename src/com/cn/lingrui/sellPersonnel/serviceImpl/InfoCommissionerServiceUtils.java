package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;

public class InfoCommissionerServiceUtils {

	private static Logger log = LogManager.getLogger();
	
	/**
	 * 终端人员按大区进行分类
	 * @param persons
	 * @return
	 * @throws NoSuchFieldException 
	 */
	public static Map<String, List<NBPT_VIEW_CURRENTPERSON>> dealByKey(List<NBPT_VIEW_CURRENTPERSON> persons, String key) {

		// 分类查询到的结果
		Map<String, List<NBPT_VIEW_CURRENTPERSON>> resultMap = new HashMap<>();
		
		try {
			
			resultMap = CommonUtil.classify(persons, key, NBPT_VIEW_CURRENTPERSON.class);

		} catch (NoSuchFieldException e) {
			
			log.error("按属性" + key + "分类时发生错误");
		}
		return resultMap;
	}
	
	/**
	 * 按大区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byRegion(Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(String key : classifyedLists.keySet()) {
			
			StatisticsTable table = new StatisticsTable();
			
			List<NBPT_VIEW_CURRENTPERSON> classifyedList = classifyedLists.get(key);
			
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 注入大区名
				table.setRegionName(person.getNBPT_SP_PERSON_REGION_NAME());
				
				// 注入大区UID
				table.setRegionUid(person.getNBPT_SP_PERSON_REGION_UID());
				
				// 注入大区配额
				table.setNeed(Integer.valueOf(person.getNBPT_SP_PERSON_REGION_NEED()));
				
				// 分类加1
				table = CommonServiceUtils.count(table, person);
			}
			resultList.add(table);
		}
		
		return resultList;
	}

	/**
	 * 按省区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byProvince(Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(String key : classifyedLists.keySet()) {
			
			StatisticsTable table = new StatisticsTable();
			
			List<NBPT_VIEW_CURRENTPERSON> classifyedList = classifyedLists.get(key);
			
			// 地区checkList
			List<String> areaCheckList = new ArrayList<>();
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 计算省份配额
				if(areaCheckList.contains(person.getNBPT_SP_PERSON_AREA_UID())) {
					
				} else {
					
					areaCheckList.add(person.getNBPT_SP_PERSON_AREA_UID());
					table.setNeed(table.getNeed() + Integer.valueOf(person.getNBPT_SP_PERSON_AREA_NEED().trim()));
				}
				
				// 注入省份名
				table.setProvinceName(person.getNBPT_SP_PERSON_PROVINCE_NAME());
				
				// 注入省份ID
				table.setProvinceId(person.getNBPT_SP_PERSON_PROVINCE_ID());
				
				// 分类加1
				table = CommonServiceUtils.count(table, person);

			}
			resultList.add(table);
		}

		// 差额计算
		for(StatisticsTable info : resultList) {
			
			Integer balance = info.getTotal() - info.getNeed();

			info.setBalance(balance);
		}
		return resultList;
	}
	
	/**
	 * 按地区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byArea(Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {

		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(String key  : classifyedLists.keySet()) {
			
			StatisticsTable table = new StatisticsTable();
			
			List<NBPT_VIEW_CURRENTPERSON> classifyedList = classifyedLists.get(key);
			
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 地区配额
				table.setNeed(Integer.valueOf(person.getNBPT_SP_PERSON_AREA_NEED().trim()));
				
				// 注入地区名
				table.setAreaName(person.getNBPT_SP_PERSON_AREA_NAME());
				
				// 注入地区UID
				table.setAreaUid(person.getNBPT_SP_PERSON_AREA_UID());

				// 注入大区UID,供后退使用
				table.setRegionUid(person.getNBPT_SP_PERSON_REGION_UID());
				
				// 分类加1
				table = CommonServiceUtils.count(table, person);
			}
			resultList.add(table);
		}

		// 差额计算
		for(StatisticsTable info : resultList) {
			
			Integer balance = info.getTotal() - info.getNeed();

			info.setBalance(balance);
		}
		return resultList;
	}
	
	

}


























