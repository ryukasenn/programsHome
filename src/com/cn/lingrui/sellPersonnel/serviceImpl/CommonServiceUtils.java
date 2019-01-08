package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;

public class CommonServiceUtils {

	private static Logger log = LogManager.getLogger();
	
	/**
	 * 终端人员按某属性分类
	 * @param persons
	 * @return
	 * @throws NoSuchFieldException 
	 */
	public static Map<String, List<NBPT_VIEW_CURRENTPERSON>> dealPersonsByKey(List<NBPT_VIEW_CURRENTPERSON> persons, String key) {

		// 分类查询到的结果
		Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedList = new HashMap<>();
		
		try {
			
			classifyedList = CommonUtil.classify(persons, key, NBPT_VIEW_CURRENTPERSON.class);

		} catch (NoSuchFieldException e) {
			
			log.error("按属性" + key + "分类时发生错误");
			e.printStackTrace();
		}

		return classifyedList;
	}
	
	/**
	 * 地区按某属性分类
	 * @param persons
	 * @return
	 * @throws NoSuchFieldException 
	 */
	public static Map<String, List<NBPT_VIEW_REGION>> dealRegionsByKey(List<NBPT_VIEW_REGION> regions, String key) {

		// 分类查询到的结果
		Map<String, List<NBPT_VIEW_REGION>> classifyedList = new HashMap<>();
		
		try {
			
			classifyedList = CommonUtil.classify(regions, key, NBPT_VIEW_REGION.class);

		} catch (NoSuchFieldException e) {
			
			log.error("按属性" + key + "分类时发生错误");
			e.printStackTrace();
		}

		return classifyedList;
	}
	
	/**
	 * 单一终端的计算方法
	 * @param table
	 * @param person
	 * @return
	 */
	public static StatisticsTable count(StatisticsTable table, NBPT_VIEW_CURRENTPERSON person) {
		
		if("3".equals(person.getNBPT_SP_PERSON_FLAG())) {
			
			// 离职合计数
			table.setDismission(table.getDismission() + 1);
			
			return table;
		}
		
		// 如果不是在职,直接退出
		if(!"2".equals(person.getNBPT_SP_PERSON_FLAG())) {
			
			return table;
		}

		String job = person.getNBPT_SP_PERSON_JOB();
		
		switch (job) {
		
			// 如果是大区总
			case "21":
				table.setRegionResper(table.getRegionResper() + 1);
				break;
				
			// 如果是地总
			case "22":
				table.setAreaResper(table.getAreaResper() + 1);
				break;
		
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
				
			// 如果是混合地总
			case "26":
				table.setRegionResper(table.getRegionResper() + 1);
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
	
	/**
	 * 其他计算
	 * @param tables
	 */
	public static void otherCompute(List<StatisticsTable> tables) {
		
		// 1.差额计算
		for(StatisticsTable table : tables) {
			
			Integer balance = table.getTotal() - table.getNeed();

			table.setBalance(balance);
		}
		
		// 离职率计算
		for(StatisticsTable table : tables) {
			
			table.setDismissionRate(CommonUtil.getPercenttage(table.getDismission(), table.getNeed()));
		}
	}
	
	public static <T> List<T> getListInMapByKey(Map<String, List<T>> classifyiedList, String key){
		
		if(null == classifyiedList.get(key)) {
			return new ArrayList<T>();
		}else {
			
			return classifyiedList.get(key);
		}
	}
	
	public static List<NBPT_VIEW_CURRENTPERSON> getPersonsByKey(Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyiedList, String key){
		
		return getListInMapByKey(classifyiedList, key);
	}
	

	public static List<NBPT_VIEW_REGION> getRegionsByKey(Map<String, List<NBPT_VIEW_REGION>> classifyiedList, String key){

		return getListInMapByKey(classifyiedList, key);
	}
	
	public static String simplefiProvinceName(String provinceName) {
		
		String backName = provinceName;
		String[] provinceNames = GlobalParams.provinceMapNames;
		for(int i = 0; i < provinceNames.length; i++) {
			
			if(-1 < backName.indexOf(provinceNames[i])) {
				
				backName = provinceNames[i];
				break;
			}
		}
		return backName;
	}
}



























