package com.cn.lingrui.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.pojos.XZQXHFPojo;


public class SelectsUtils {

	@Deprecated
	public static String createXzqxhfSelects(List<NBPT_COMMON_XZQXHF> list) {
		
		StringBuffer selectString = new StringBuffer();

		Map<String, XZQXHFPojo > provinces = new HashMap<>();
		Map<String, XZQXHFPojo > citys = new HashMap<>();
		Map<String, XZQXHFPojo > contys = new HashMap<>();
		for(int i = 0; i < list.size(); i++) {
			
			if("1".equals(list.get(i).getNBPT_COMMON_XZQXHF_LEVEL())) {
				
				XZQXHFPojo province = new XZQXHFPojo();
				province.setId(list.get(i).getNBPT_COMMON_XZQXHF_ID());
				province.setName(list.get(i).getNBPT_COMMON_XZQXHF_NAME());
				province.setContents(new ArrayList<XZQXHFPojo>());
				provinces.put(list.get(i).getNBPT_COMMON_XZQXHF_ID(),province);
			} else if("2".equals(list.get(i).getNBPT_COMMON_XZQXHF_LEVEL())) {
				
				XZQXHFPojo city = new XZQXHFPojo();
				city.setId(list.get(i).getNBPT_COMMON_XZQXHF_ID());
				city.setName(list.get(i).getNBPT_COMMON_XZQXHF_NAME());
				city.setContents(new ArrayList<XZQXHFPojo>());
				citys.put(list.get(i).getNBPT_COMMON_XZQXHF_ID(),city);
					provinces.get(list.get(i).getNBPT_COMMON_XZQXHF_PID()).getContents().add(city);
			} else if("3".equals(list.get(i).getNBPT_COMMON_XZQXHF_LEVEL())) {
				
				XZQXHFPojo conty = new XZQXHFPojo();
				conty.setId(list.get(i).getNBPT_COMMON_XZQXHF_ID());
				conty.setName(list.get(i).getNBPT_COMMON_XZQXHF_NAME());
				contys.put(list.get(i).getNBPT_COMMON_XZQXHF_ID(),conty);
				citys.get(list.get(i).getNBPT_COMMON_XZQXHF_PID()).getContents().add(conty);
			}
		}

		selectString.append("{\"data\":[");
		
		for(String provinceId : provinces.keySet()) {
			
			StringBuffer provinceString = new StringBuffer();
			provinceString.append("{");
			provinceString.append("\"id\":\"" + provinceId + "\",");
			provinceString.append("\"name\":\"" + provinces.get(provinceId).getName() + "\",");
			provinceString.append("\"contents\":[");
			
			for(XZQXHFPojo cityOfThisProvince : provinces.get(provinceId).getContents()) {

				StringBuffer cityString = new StringBuffer();
				cityString.append("{");
				cityString.append("\"id\":\"" + cityOfThisProvince.getId() + "\",");
				cityString.append("\"name\":\"" + cityOfThisProvince.getName() + "\",");
				cityString.append("\"contents\":[");
				
				for(XZQXHFPojo contyOfThisCity : cityOfThisProvince.getContents()) {

					StringBuffer contyString = new StringBuffer();
					contyString.append("{");
					contyString.append("\"id\":\"" + contyOfThisCity.getId() + "\",");
					contyString.append("\"name\":\"" + contyOfThisCity.getName() + "\",");
					contyString.append("\"contents\":[]},");
					cityString.append(contyString);
				}
				cityString.deleteCharAt(cityString.length() - 1);
				cityString.append("]},");
				provinceString.append(cityString);
			}
			provinceString.deleteCharAt(provinceString.length() - 1);
			provinceString.append("]},");
			selectString.append(provinceString);
		}
		selectString.deleteCharAt(selectString.length() - 1);
		selectString.append("]");
		return selectString.toString();
	}
}
