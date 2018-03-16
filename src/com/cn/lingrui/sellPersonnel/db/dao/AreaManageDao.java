package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Area;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Province;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Region;

public interface AreaManageDao {

	public List<Region> receiveRegions(Connection conn) throws SQLException;

	public List<Province> receiveProvinces(Connection connection) throws SQLException;

	public List<Area> receiveAreas(Connection connection) throws SQLException;

	
}
