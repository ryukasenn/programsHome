package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;

public class SupportServiceUtils {

	private static Logger log = LogManager.getLogger();
	
	public static List<StatisticsTable> dealCurrentPerson_total(List<NBPT_VIEW_CURRENTPERSON> persons) throws Exception{
		
		// 初始化返回结果
		List<StatisticsTable> totalInfos = new ArrayList<>();
		
		try {
			
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByTypePersons = CommonUtil.classify(persons, "NBPT_SP_PERSON_TYPENAME", NBPT_VIEW_CURRENTPERSON.class);
		
			for(String key : classifyByTypePersons.keySet()) {
				
				StatisticsTable info = new StatisticsTable();
				
				info.setRegionName(key);
				info.setTotal(classifyByTypePersons.get(key).size());
				
				totalInfos.add(info);
			}

			StatisticsTable table = new StatisticsTable();
			table.setRegionName("合计");
			
			for(StatisticsTable info : totalInfos) {

				table.setTotal(table.getTotal() + info.getTotal());
			}
			totalInfos.add(table);
			return totalInfos;
			
		} catch (NoSuchFieldException e) {
			
			log.error("统计各分类合计信息时出错");
			throw new Exception();
		}
	}
	
	public static List<StatisticsTable> dealCurrentPerson_otc(List<NBPT_VIEW_CURRENTPERSON> persons, List<NBPT_VIEW_REGION> regions) throws Exception{
		
		try {
			// 初始化返回结果
			List<StatisticsTable> OTCInfos = new ArrayList<>();

			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByRegions = CommonUtil.classify(persons, "NBPT_SP_PERSON_REGION_UID", NBPT_VIEW_CURRENTPERSON.class);
			
			for(NBPT_VIEW_REGION region : regions) {

				if(!"1".equals(region.getNBPT_SP_REGION_LEVEL())) {
					
					continue;
				}
				
				StatisticsTable info = new StatisticsTable();
				
				info.setNeed(info.getNeed() + Integer.valueOf(region.getNBPT_SP_REGION_NEED()));
				info.setRegionUid(region.getNBPT_SP_REGION_UID());
				info.setRegionName(region.getNBPT_SP_REGION_NAME());
				
				// 统计数据
				for(NBPT_VIEW_CURRENTPERSON otcType : classifyByRegions.get(region.getNBPT_SP_REGION_UID())) {
					CommonServiceUtils.count(info, otcType);
				}
	
				// 差额计算
				Integer balance = info.getTotal() - info.getNeed();
				info.setBalance(balance);
				
				OTCInfos.add(info);
			}
			
			return OTCInfos; 
		}catch (NoSuchFieldException e) {
			
			log.error("统计OTC合计信息时出错");
			throw new Exception();
		}
	}
	
}
























