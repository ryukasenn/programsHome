package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;

public interface RegionManageDao {

	public List<CurrentRegion> receiveCurrentRegions(RegionsPojo pojo, Connection conn) throws SQLException ;

	public List<CurrentRegion> receiveCurrentRegions_Provinces(Connection connection) throws SQLException;

	public List<NBPT_SP_REGION> receiveRegionsSelect(Connection connection) throws SQLException;

	/**
	 * 根据部门UID获取部门信息
	 * @param regionId 部门UID
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public NBPT_SP_REGION receiveCurrentRegion(String regionId, Connection connection) throws SQLException;

	public NBPT_SP_PERSON receiveCurrentPerson(String nbpt_SP_REGION_RESPONSIBLER, Connection connection) throws SQLException;

	public List<NBPT_SP_PERSON> receiveRegionReper(String personType, Connection connection) throws SQLException;

	public List<NBPT_SP_PERSON> searchRegionReper(String personType, String searchName, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> receiveProvinceSelect(Connection connection) throws SQLException;

	public List<Area_Xzqx_Info> receiveCurrentXzqxs(String regionId, Connection connection) throws SQLException;

	public List<CurrentRegion> receiveRegion_Xzqxs(String regionUid, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> receiveAreaContainSelects(String parentId, Connection connection) throws SQLException;

	public void postAddRegionXzqx(NBPT_SP_REGION_XZQX region_XZQX, Connection connection) throws SQLException;

	/**
	 * 更新大区信息
	 * @param updateRegion
	 * @throws SQLException 
	 */
	void updateRegion(NBPT_SP_REGION updateRegion, Connection connection) throws SQLException;

	/**
	 * 删除地区行政区县对应关系
	 * @param regionId 地区ID
	 * @param cityValue 行政区县ID
	 * @param connection
	 * @throws SQLException 
	 */
	public void deleteRegionXzqx(String regionId, String cityValue, Connection connection) throws SQLException;
	

}
