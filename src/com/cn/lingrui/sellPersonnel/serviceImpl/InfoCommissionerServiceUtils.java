package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.StatisticsTable;

public class InfoCommissionerServiceUtils {

	private static Logger log = LogManager.getLogger();
	
	/**
	 * 终端人员按大区进行分类
	 * @param persons
	 * @return
	 * @throws NoSuchFieldException 
	 */
	public static List<List<NBPT_VIEW_CURRENTPERSON>> dealByKey(List<NBPT_VIEW_CURRENTPERSON> persons, String key) {

		// 分类查询到的结果
		List<List<NBPT_VIEW_CURRENTPERSON>> classifyedList = new ArrayList<>();
		
		try {
			
			classifyedList = CommonUtil.classify(persons, key, NBPT_VIEW_CURRENTPERSON.class);

		} catch (NoSuchFieldException e) {
			
			log.error("按某属性分类时发生错误");
			e.printStackTrace();
		}

		return classifyedList;
	}
	
	/**
	 * 按大区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byRegion(List<List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(List<NBPT_VIEW_CURRENTPERSON> classifyedList : classifyedLists) {
			
			StatisticsTable table = new StatisticsTable();
			
			for(NBPT_VIEW_CURRENTPERSON person : classifyedList) {
				
				// 注入大区名
				table.setRegionName(person.getNBPT_SP_PERSON_REGION_NAME());
				
				// 注入大区UID
				table.setRegionUid(person.getNBPT_SP_PERSON_REGION_UID());
				
				// 注入大区配额
				table.setNeed(Integer.valueOf(person.getNBPT_SP_PERSON_REGION_NEED()));
				
				// 分类加1
				table = InfoCommissionerServiceUtils.count(table, person);
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
	public static List<StatisticsTable> countClassifyList_byProvince(List<List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {
		
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(List<NBPT_VIEW_CURRENTPERSON> classifyedList : classifyedLists) {
			
			StatisticsTable table = new StatisticsTable();

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
				table = InfoCommissionerServiceUtils.count(table, person);
			}
			resultList.add(table);
		}
		
		return resultList;
	}
	
	/**
	 * 按地区分类的计算方法
	 * @param classifyedLists
	 * @return
	 */
	public static List<StatisticsTable> countClassifyList_byArea(List<List<NBPT_VIEW_CURRENTPERSON>> classifyedLists) {

		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(List<NBPT_VIEW_CURRENTPERSON> classifyedList : classifyedLists) {
			
			StatisticsTable table = new StatisticsTable();

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
				table = InfoCommissionerServiceUtils.count(table, person);
			}
			resultList.add(table);
		}
		
		return resultList;
	}
	
	/**
	 * 单一终端的计算方法
	 * @param table
	 * @param person
	 * @return
	 */
	private static StatisticsTable count(StatisticsTable table, NBPT_VIEW_CURRENTPERSON person) {
		
		if("3".equals(person.getNBPT_SP_PERSON_FLAG())) {
			
			// 离职合计数
			table.setDismission(table.getDismission() + 1);
			
			return table;
		}

		String job = person.getNBPT_SP_PERSON_JOB();
		
		switch (job) {
		
			// 如果是区县总
			case "23":
				table.setXzquResper(table.getXzquResper() + 1);
				break;
				
			// 如果是预备区县总
			case "24":
				table.setXzquResper_preparatory(table.getXzquResper_preparatory() + 1);
				break;
				
			// 如果是推广经理
			case "25":
				table.setPromote(table.getPromote() + 1);
				break;
				
		default:
			break;
		}
		
		if("23".equals(job) || "24".equals(job) || "25".equals(job)) {

			// 添加合计数
			table.setTotal(table.getTotal()+ 1);
		} else {

		}
		
		return table;		
	}

	
	

}


























