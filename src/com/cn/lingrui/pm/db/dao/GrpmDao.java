package com.cn.lingrui.pm.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.pm.pojos.PmInfo;

public interface GrpmDao extends PmBaseDao{

	
	//获取同段位的排名信息
	public List<PmInfo> getSameDwPm(String dw,Connection connection) throws SQLException;
	
	//获取页面跳转时查询同日期，同排名情况
	public List<PmInfo> getSameDwPm(String rq,String xm,String dw,Connection connection) throws SQLException;
	
	//获取当前人员的排名信息
	public PmInfo getcurrentDwpm(String ryxm,Connection connection) throws SQLException;
	
	//获取当前人员的排名信息
	public PmInfo getcurrentDwpm(String ryxm,String rq, Connection connection) throws SQLException;
	
	//获取起正负值
	public int zfValue(String ryxm,Connection connection) throws SQLException;
	
	//获取本人历史
	public List<PmInfo> getAllResult(String ryxm,Connection connection) throws SQLException;
	
	//获取个人大区信息
	public String getdqmcStr (String ryxm,Connection connection) throws SQLException;
	
	//获取同大区的排名信息
	public List<PmInfo> getSameDqPm(String dqmc,String pmrq,Connection connection) throws SQLException;
	
	//获取前10的排名信息
	public List<PmInfo> getbeforetenPmList(String pmrq,String mc,Connection connection) throws SQLException;
	//获取后10的排名信息
	public List<PmInfo> getlasttenPmList(String pmrq,String mc,Connection connection) throws SQLException;
	


}
