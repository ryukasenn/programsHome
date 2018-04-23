package com.cn.lingrui.sellPersonnel.serviceImpl;

import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.region.UpdateRegionPojo;

public class RegionManageServiceUtils {

	public static void setUpdateRegionInfos(NBPT_SP_REGION updateInfo, UpdateRegionPojo pojo, NBPT_VIEW_REGION regionInfo) {
		
		// 设定ID
		updateInfo.setNBPT_SP_REGION_ID(regionInfo.getNBPT_SP_REGION_ID());
		
		// 设定配额
		updateInfo.setNBPT_SP_REGION_ONAME(pojo.getRegionNeed());
		
		// 设定负责人
		updateInfo.setNBPT_SP_REGION_RESPONSIBLER(pojo.getRegionResponsibler());
		
		// 设定名称
		updateInfo.setNBPT_SP_REGION_NAME(pojo.getRegionName());
		
		// 设定Level
		updateInfo.setNBPT_SP_REGION_LEVEL(regionInfo.getNBPT_SP_REGION_LEVEL());
		
		// 设定备注
		updateInfo.setNBPT_SP_REGION_NOTE(pojo.getRegionNote());
	}
}
