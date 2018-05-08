package com.cn.lingrui.hx.pojos.province;

import org.apache.commons.lang.StringUtils;

import com.cn.lingrui.hx.pojos.BasePojoIn;
import com.cn.lingrui.common.utils.CommonUtil;

public class ProvincePojoIn extends BasePojoIn{

	private String province;// 选择省份
	private String returnEnd;// 回款截至
	private String shipmentsEnd;// 发货截至
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
	
	public String getPrivinceMc() {

		if(CommonUtil.isEmpty(this.privinceMc)) {
			this.privinceMc = StringUtils.substringBefore(this.province, " ");
		}
		return this.privinceMc;
	}
	
	public String getReturnEnd() {
		
		return CommonUtil.formateTiemToBasic(returnEnd);
	}
	public void setReturnEnd(String returnEnd) {
		
		this.returnEnd = CommonUtil.formateTiemToBasic(returnEnd);
	}
	public String getShipmentsEnd() {
		
		return CommonUtil.formateTiemToBasic(shipmentsEnd);
	}
	public void setShipmentsEnd(String shipmentsEnd) {
		
		this.returnEnd = CommonUtil.formateTiemToBasic(shipmentsEnd);
	}
	
}
