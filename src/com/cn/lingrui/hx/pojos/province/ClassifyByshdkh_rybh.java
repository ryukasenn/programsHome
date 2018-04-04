package com.cn.lingrui.hx.pojos.province;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDD;
import com.cn.lingrui.hx.db.dbpojos.hx.Hx_insert_xsddhx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSHK;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsdd;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsddmx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xshk;

public class ClassifyByshdkh_rybh {

	private String shdkh_rybh; // 分类下 客户^业务员 拼接
	private String shdkh; // 客户
	private String rybh; // 业务员
	private List<Zdhx_XSDD> xsdds = new ArrayList<Zdhx_XSDD>(); // 分类下发货单
	private List<Zdhx_XSHK> xshks = new ArrayList<Zdhx_XSHK>(); // 分类下回款单
	private List<Zdhx_XSDDMX> xsddmxs = new ArrayList<Zdhx_XSDDMX>(); // 发货单明细
	private List<String> xsddlss = new ArrayList<String>(); // 分类下发货单订单流水列表
	private List<Hx_insert_xsddhx> insertXsddhxParams = new ArrayList<Hx_insert_xsddhx>(); // 分类下xsddhx表插入语句列表
	private List<Zdhx_update_xshk> updateXshkParams = new ArrayList<Zdhx_update_xshk>();
	private List<Zdhx_update_xsdd> updateXsddParams = new ArrayList<Zdhx_update_xsdd>();
	private List<Zdhx_update_xsddmx> updateXsddmxParams = new ArrayList<Zdhx_update_xsddmx>();

	public String getShdkh_rybh() {
		return shdkh_rybh;
	}

	public void setShdkh_rybh(String shdkh_rybh) {
		this.shdkh_rybh = shdkh_rybh;
	}

	public String getShdkh() {
		return shdkh;
	}

	public void setShdkh(String shdkh) {
		this.shdkh = shdkh;
	}

	public String getRybh() {
		return rybh;
	}

	public void setRybh(String rybh) {
		this.rybh = rybh;
	}

	public List<Zdhx_XSDD> getXsdds() {
		return xsdds;
	}

	public void setXsdds(List<Zdhx_XSDD> xsdds) {
		this.xsdds = xsdds;
	}

	public List<Zdhx_XSHK> getXshks() {
		return xshks;
	}

	public void setXshks(List<Zdhx_XSHK> xshks) {
		this.xshks = xshks;
	}

	public List<String> getXsddlss() {
		return xsddlss;
	}

	public void setXsddlss(List<String> xsddlss) {
		this.xsddlss = xsddlss;
	}

	public List<Zdhx_XSDDMX> getXsddmxs() {
		return xsddmxs;
	}

	public void setXsddmxs(List<Zdhx_XSDDMX> xsddmxs) {
		this.xsddmxs = xsddmxs;
	}

	public List<Hx_insert_xsddhx> getInsertXsddhxParams() {
		return insertXsddhxParams;
	}

	public void setInsertXsddhxParams(List<Hx_insert_xsddhx> insertXsddhxParams) {
		this.insertXsddhxParams = insertXsddhxParams;
	}

	public List<Zdhx_update_xshk> getUpdateXshkParams() {
		return updateXshkParams;
	}

	public void setUpdateXshkParams(List<Zdhx_update_xshk> updateXshkParams) {
		this.updateXshkParams = updateXshkParams;
	}

	public List<Zdhx_update_xsdd> getUpdateXsddParams() {
		return updateXsddParams;
	}

	public void setUpdateXsddParams(List<Zdhx_update_xsdd> updateXsddParams) {
		this.updateXsddParams = updateXsddParams;
	}

	public List<Zdhx_update_xsddmx> getUpdateXsddmxParams() {
		return updateXsddmxParams;
	}

	public void setUpdateXsddmxParams(List<Zdhx_update_xsddmx> updateXsddmxParams) {
		this.updateXsddmxParams = updateXsddmxParams;
	}

}
