package com.cn.lingrui.hx.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDD;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSHK;
import com.cn.lingrui.hx.db.dbpojos.hx.Hx_insert_xsddhx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsdd;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsddmx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xshk;

@Repository()
public interface HxDao {

	public List<Zdhx_XSDD> getXSDDS(@Param("province")String provinceId);
	public List<Zdhx_XSHK> getXSHKS(@Param("province")String provinceId,@Param("timeEnd")String timeEnd);
	public List<Zdhx_XSDDMX> getXSDDMXS(@Param("province")String provinceId);
	public void updateXSDD(List<Zdhx_update_xsdd> update_xsdds);
	public void insertXSDDHX(List<Hx_insert_xsddhx> insert_xsddhxs);
	public void updateXSDDMX(List<Zdhx_update_xsddmx> update_xsddmxs);
	public void updateXSHK(List<Zdhx_update_xshk> update_xshks);
}
