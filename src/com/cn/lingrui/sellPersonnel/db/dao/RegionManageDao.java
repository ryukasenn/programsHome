package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_PROCEDURE_TREE;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;

public interface RegionManageDao extends SellPersonnelBaseDao{

	/**
	 * 根据页面条件,查询大区和地区
	 * @param pojo
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveCurrentRegions(RegionsPojo pojo, Connection conn) throws SQLException ;

	/**
	 * AJAX获取查看大区页面输入框自动补全信息
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_SP_REGION> receiveRegionsSelect(Connection connection) throws SQLException;


	/**
	 * 根据personType获取当前没有分配负责区域的大区总或地总
	 * @param personType 22:地总,26:大区总
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_SP_PERSON> receiveRegionReper(String personType, Connection connection) throws SQLException;
	
	/**
	 * 根据personType和名字获取指定没有分配负责区域的大区总或地总
	 * @param personType 22:地总,26:大区总
	 * @param searchName 名称
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_SP_PERSON> searchRegionReper(String personType, String searchName, Connection connection) throws SQLException;


	public List<Area_Xzqx_Info> receiveCurrentXzqxs(String regionId, Connection connection) throws SQLException;

	public List<CurrentRegion> receiveRegion_Xzqxs(String regionUid, Connection connection) throws SQLException;

	/**
	 * 根据部门UID或省份ID获取部门行政区县绑定关系
	 * @param regionType 部门的类型
	 * @param xzqxId 绑定行政区县ID
	 * @param type 绑定类型
	 * @param connection 
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_XZQX receiveRegion_Xzqxs(String regionType, String xzqxId, String type, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> receiveAreaContainSelects(String parentId, Connection connection) throws SQLException;

	public void postAddRegionXzqx(NBPT_SP_REGION_XZQX region_XZQX, Connection connection) throws SQLException;

	/**
	 * 更新大区信息
	 * @param updateRegion
	 * @throws SQLException 
	 */
	public void updateRegion(NBPT_SP_REGION updateRegion, Connection connection) throws SQLException;

	/**
	 * 删除地区行政区县对应关系
	 * @param regionId 地区ID
	 * @param cityValue 行政区县ID
	 * @param connection
	 * @throws SQLException 
	 */
	public void deleteRegionXzqx(String regionId, String cityValue, Connection connection) throws SQLException;

	/**
	 * 因为删除了该地区的下辖行政区县,所以要删除与该行政区县相关的负责人对应信息
	 * @param cityValue 被删除的下辖行政区县
	 * @param connection
	 * @throws SQLException 
	 */
	public void deletePersonXzqx(String cityValue, Connection connection) throws SQLException;

	/**
	 * 获取大区配额计算数值,供基本信息修改参考
	 * @param regionUid 大区UID
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public String receiveRegonNeedSum(String regionUid,Connection connection) throws SQLException;
	
	/**
	 * 查询废弃的大区
	 * provinceId 省份ID
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_SP_REGION> receiveDumpedRegion(String provinceId, Connection connection) throws SQLException;
	

	/**
	 * 修改大区,省份绑定信息
	 * provinceId 省份ID
	 * @return
	 * @throws SQLException 
	 */
	public void updateRegionXzqx_rp(String rpUid, String newUid, Connection connection) throws SQLException;
	
	/**
	 * 添加新的大区或地区
	 * @param newRegion
	 * @param connection
	 * @throws SQLException 
	 */
	public void addRegion(NBPT_SP_REGION newRegion, Connection connection) throws SQLException;
	
	/**
	 * 获取部门选取树
	 * @param pid
	 * @param rank
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_PROCEDURE_TREE> receiveRegionTree(String pid, Connection conn) throws SQLException;

	/**
	 * 获取人员选择树
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_PROCEDURE_TREE> receivePersonTree(Connection connection) throws SQLException;

	/**
	 * 获取可用负责人
	 * @param type region,area 类型
	 * @param personType 人员分类
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveAbleResper(String type, String personType, Connection connection) throws SQLException;
}
