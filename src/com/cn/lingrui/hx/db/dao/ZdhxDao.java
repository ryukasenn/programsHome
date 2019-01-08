package com.cn.lingrui.hx.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.common.db.dbpojos.GZBMZD;
import com.cn.lingrui.hx.db.dbpojos.LSWLZD;
import com.cn.lingrui.hx.db.dbpojos.NBPT_HX_NOHX;
import com.cn.lingrui.hx.db.dbpojos.NOHX_XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.NOHX_XSHK;
import com.cn.lingrui.hx.db.dbpojos.XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.XSHK;
import com.cn.lingrui.hx.db.dbpojos.ZWWLDW;
import com.cn.lingrui.hx.db.dbpojos.common.ZWZGZD_YWY;
import com.cn.lingrui.hx.db.dbpojos.hx.XSDD_XSDDMXS;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_PROCEDURE_TREE;

@Repository()
public interface ZdhxDao extends BaseDao{

	/**
	 * 根据省份获取所有需要核销销售订单
	 * @param provinceId 省份编号
	 * @param shipmentsEnd 订单截至日期(该日期之前的所有订单)
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public XSDD_XSDDMXS receiveXsdds_byProvince(String provinceId, String shipmentsEnd, Connection conn) throws SQLException;

	/**
	 * 获取可核销的销售回款
	 * @param returnEnd 截至时间
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<XSHK> receiveXshks_valid(String returnEnd, Connection connection) throws SQLException;
	
	/**
	 * 订单核销记录插入
	 * @param insertSqls
	 * @param connection
	 * @throws SQLException 
	 */
	public void excuteInsertXsddhx(List<String> insertSqls, Connection connection) throws SQLException;

	/**
	 * 销售订单明细的更新
	 * @param updateXsddmxSqls
	 * @param connection
	 * @throws SQLException 
	 */
	public void excuteUpdateXsddmx(List<String> updateXsddmxSqls, Connection connection) throws SQLException;

	/**
	 * 销售订单的更新
	 * @param updateXsddmxSqls
	 * @param connection
	 * @throws SQLException 
	 */
	public void excuteUpdateXsdd(List<String> updateXsddSqls, Connection connection) throws SQLException;
	

	/**
	 * 销售回款的更新
	 * @param updateXsddmxSqls
	 * @param connection
	 * @throws SQLException 
	 */
	public void excuteUpdateXshk(List<String> updateXshkSqls, Connection connection) throws SQLException;
	
	/**
	 * 根据业务员获取所有需要核销销售订单
	 * @param provinceId 省份编号
	 * @param shipmentsEnd 订单截至日期(该日期之前的所有订单)
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public XSDD_XSDDMXS receiveXsdds_byYwy(String ywyId, String shipmentsEnd, Connection connection) throws SQLException;

	/**
	 * 根据销售订单销售订单编号和产品编号,查询订单明细
	 * @param xsddDdbh
	 * @param xsddCpbh
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public XSDDMX receiveXsddmx(String xsddDdbh, String xsddCpbh, Connection connection) throws SQLException;

	/**
	 * 添加不参与核销销售订单明细
	 * @param xsddDdbh
	 * @param xsddCpbh
	 * @throws SQLException 
	 */
	public void addNotHxXsddmx(String xsddDdbh, String xsddCpbh, String xsddls,Connection connection) throws SQLException;

	/**
	 * 获取不核销销售订单
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NOHX_XSDDMX> receiveNohxXsddmxs(Connection connection) throws SQLException;

	/**
	 * 根据回款单号,查询销售回款
	 * @param dh
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public XSHK receiveXshk(String dh, Connection connection) throws SQLException;

	/**
	 * 添加不参与核销回款
	 * @param dh 回款单号
	 * @param hkls 回款流水
	 * @param connection
	 * @throws SQLException 
	 */
	public void addNotHxXshk(String dh, String hkls, Connection connection) throws SQLException;

	/**
	 * 获取不核销回款单
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NOHX_XSHK> receiveNohxXshk(Connection connection) throws SQLException;

	/**
	 * 获取销售部门所有人员
	 * @return
	 * @throws SQLException 
	 */
	public List<ZWZGZD_YWY> receiveYwys(Connection connection) throws SQLException;

	/**
	 * 获取销售所有部门
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<GZBMZD> receiveBms(Connection connection) throws SQLException;

	/**
	 * 获取平账客户
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<ZWWLDW> receivePingzhangKh(Connection connection) throws SQLException;

	/**
	 * 获取所有政策12的产品
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<LSWLZD> receiveZhengce12(Connection connection) throws SQLException;

	/**
	 * 获取客户
	 * @param dh
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public ZWWLDW receiveKh(String dh, Connection connection) throws SQLException;

	/**
	 * 添加平账客户
	 * @param dh
	 * @param connection
	 * @throws SQLException 
	 */
	public void addNotHxKh(String dh, Connection connection) throws SQLException;

	/**
	 * 获取政策12产品
	 * @param dh
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public LSWLZD receiveCp(String dh, Connection connection) throws SQLException;

	/**
	 * 添加政策12产品
	 * @param dh
	 * @param connection
	 * @throws SQLException 
	 */
	public void addNotHxCp(String dh, Connection connection) throws SQLException;

	/**
	 * 业务选择树
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_PROCEDURE_TREE> receiveYwySelectTree(String pid, Connection connection) throws SQLException;
}
















