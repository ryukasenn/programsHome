package com.cn.lingrui.hx.pojos.province;

import org.apache.commons.lang.StringUtils;

import com.cn.lingrui.hx.pojos.BasePojoIn;
import com.cn.lingrui.common.utils.CommonUtil;

public class ProvincePojoIn extends BasePojoIn{

	private String province;// 选择省份
	private String timeEnd;// 选择时间
	private String privinceId;// 省份编号
	private String privinceMc;// 省份名称
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPrivinceId() {
		
		if(CommonUtil.isEmpty(privinceId)) {
			this.privinceId = StringUtils.substringAfter(this.province, " ");
		}
		return privinceId;
	}
	public void setPrivinceId(String privinceId) {
		this.privinceId = privinceId;
	}
	public String getPrivinceMc() {

		if(CommonUtil.isEmpty(this.privinceMc)) {
			this.privinceMc = StringUtils.substringBefore(this.province, " ");
		}
		return this.privinceMc;
	}
	public void setPrivinceMc(String privinceMc) {
		this.privinceMc = privinceMc;
	}
	public String getTimeEnd() {
		
		return timeEnd.replaceAll("-", "");
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	
}
