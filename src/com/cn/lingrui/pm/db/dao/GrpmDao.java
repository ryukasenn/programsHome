package com.cn.lingrui.pm.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.pm.pojos.PmInfo;

public interface GrpmDao extends PmBaseDao{

	
	//获取同段位的排名信息
	public List<PmInfo> getSameDwPm(String dw,Connection connection) throws SQLException;
	
	//获取当前人员的排名信息
	public PmInfo getcurrentDwpm(String ryxm,Connection connection) throws SQLException;
	
	//获取起正负值
	public int zfValue(String ryxm,Connection connection) throws SQLException;
	
	//获取本人历史
	public List<PmInfo> getAllResult(String ryxm,Connection connection) throws SQLException;
	


}
