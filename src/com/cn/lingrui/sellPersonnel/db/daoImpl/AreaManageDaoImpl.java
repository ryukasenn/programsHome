package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.sellPersonnel.db.dao.AreaManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Area;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Province;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Region;

@Repository("areaManageDao")
public class AreaManageDaoImpl extends BaseDaoImpl implements AreaManageDao {

	private static Logger log = LogManager.getLogger();
	
	/**
	 * 获取当前大区
	 * @throws SQLException 
	 */
	@Override
	public List<Region> receiveRegions(Connection conn) throws SQLException {

		String sql = "SELECT NBPT_SP_REGION_ID AS REGION_ID, NBPT_SP_REGION_NAME AS REGION_NAME "
				   + "FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1'";
		List<Region> regions;
		try {
			regions = this.query(sql, conn, Region.class);
			return regions;
		} catch (SQLException e) {
			
			log.info("查询当前大区列表出错");
			throw new SQLException();
		}
	}

	@Override
	public List<Province> receiveProvinces(Connection conn) throws SQLException {
		
		String sql = "SELECT DISTINCT SUBSTRING(A.NBPT_SP_REGION_ID,1,2) AS REGION_ID, B.NBPT_COMMON_XZQXHF_ID AS PROVINCE_ID, B.NBPT_COMMON_XZQXHF_NAME AS PROVINCE_NAME "
				   + "FROM NBPT_SP_REGION A "
				   + "LEFT JOIN NBPT_COMMON_XZQXHF B "
				   + "ON SUBSTRING(A.NBPT_SP_REGION_ID,3,6) = B.NBPT_COMMON_XZQXHF_ID "
				   + "WHERE NBPT_SP_REGION_LEVEL = '2'";
		List<Province> provinces;
		try {
			provinces = this.query(sql, conn, Province.class);
			return provinces;
		} catch (SQLException e) {
			
			log.info("查询当前大区省份列表出错");
			throw new SQLException();
		}
	}

	@Override
	public List<Area> receiveAreas(Connection conn) throws SQLException {

		String sql = "SELECT B.NBPT_COMMON_XZQXHF_ID AS PROVINCE_ID, A.NBPT_SP_REGION_ID AS AREA_ID, A.NBPT_SP_REGION_NAME AS AREA_NAME "
				   + "FROM NBPT_SP_REGION A "
				   + "LEFT JOIN NBPT_COMMON_XZQXHF B "
				   + "ON SUBSTRING(A.NBPT_SP_REGION_ID,3,6) = B.NBPT_COMMON_XZQXHF_ID "
				   + "WHERE NBPT_SP_REGION_LEVEL = '2'";
		List<Area> areas;
		try {
			areas = this.query(sql, conn, Area.class);
			return areas;
		} catch (SQLException e) {
			
			log.info("查询当前大区省份地区列表出错");
			throw new SQLException();
		}
	}
	
	

}
























