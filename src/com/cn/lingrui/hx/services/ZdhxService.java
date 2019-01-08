package com.cn.lingrui.hx.services;

import com.cn.lingrui.hx.pojos.zdhx.HxProvincePojoIn;
import com.cn.lingrui.hx.pojos.zdhx.HxYwyPojoIn;

public interface ZdhxService {

	/**
	 * 根据省份自动核销
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public String hxByProvinceSelect(HxProvincePojoIn in) throws Exception;

	/**
	 * 根据业务员自动核销
	 * @param in
	 * @return
	 */
	public String hxByYwySelect(HxYwyPojoIn in);

	/**
	 * 添加不参与核销订单明细
	 * @param xsddDdbh
	 * @param xsddCpbh
	 * @return
	 */
	public String addNotHx(String type, String dh, String cpbh);

	/**
	 * 获取不参与核销订单明细
	 * @return
	 */
	public String receiveNoHx(String type);
}
