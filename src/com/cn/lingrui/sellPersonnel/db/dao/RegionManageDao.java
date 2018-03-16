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

	public NBPT_SP_REGION receiveCurrentRegion(RegionsPojo pojo, Connection connection) throws SQLException;

	public NBPT_SP_PERSON receiveCurrentPerson(String nbpt_SP_REGION_RESPONSIBLER, Connection connection) throws SQLException;

	public List<NBPT_SP_PERSON> receiveRegionReper(String personType, Connection connection) throws SQLException;

	public List<NBPT_SP_PERSON> searchRegionReper(String personType, String searchName, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> receiveProvinceSelect(Connection connection) throws SQLException;

	public List<Area_Xzqx_Info> receiveCurrentXzqxs(String regionId, Connection connection) throws SQLException;

}
