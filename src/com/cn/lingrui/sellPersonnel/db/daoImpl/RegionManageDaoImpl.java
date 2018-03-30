package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;

@Repository("regionManageDao")
public class RegionManageDaoImpl extends BaseDaoImpl implements RegionManageDao {

	private static Logger log = LogManager.getLogger();

	@Override
	public List<NBPT_SP_REGION> receiveRegionsSelect(Connection conn) throws SQLException {

		try {
			String sql = DBUtils.beanToSql(NBPT_SP_REGION.class, "SELECT", "NBPT_SP_REGION");

			List<NBPT_SP_REGION> resultList = this.query(
					sql + " WHERE NBPT_SP_REGION_LEVEL = '1' ORDER BY NBPT_SP_REGION_ID ASC", conn,
					NBPT_SP_REGION.class);

			return resultList;
		} catch (SQLException e) {

			log.error("查询大区信息下拉框出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentRegion> receiveCurrentRegions(RegionsPojo pojo, Connection conn) throws SQLException {

		try {

			List<CurrentRegion> resultList = null;

			StringBuffer sql = new StringBuffer("  SELECT A.* , B.NBPT_SP_PERSON_NAME" + 
												"  FROM NBPT_SP_REGION A" + 
												"  LEFT JOIN NBPT_SP_PERSON B" + 
												"  ON A.NBPT_SP_REGION_RESPONSIBLER = B.NBPT_SP_PERSON_PID" + 
												"  WHERE 1 = 1");

			if (null == pojo.getRegionName() || "".equals(pojo.getRegionName())) {

			} else {

				String regionName = pojo.getRegionName();
				String regionId = regionName.substring(0, 2);
				sql.append("  AND NBPT_SP_REGION_ID LIKE '" + regionId + "%'");
			}

			if ("1".equals(pojo.getTypeRadio())) {

				sql.append("  AND NBPT_SP_REGION_LEVEL = '1'");

			} else if ("2".equals(pojo.getTypeRadio())) {

			}

			resultList = this.query(sql.toString() + " ORDER BY NBPT_SP_REGION_ID ASC ", conn, CurrentRegion.class);

			return resultList;
		} catch (SQLException e) {

			log.error("查询所有大区出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentRegion> receiveCurrentRegions_Provinces(Connection connection) throws SQLException {
		try {

			String sql = "  SELECT B.NBPT_SP_REGION_XZQX_REGIONID AS REGIONID, "
					+ "  A.NBPT_COMMON_XZQXHF_ID AS XZQXID, " + "  A.NBPT_COMMON_XZQXHF_NAME AS XZQXNAME "
					+ "  FROM NBPT_SP_REGION_XZQX B " + "  LEFT JOIN NBPT_COMMON_XZQXHF A "
					+ "  ON A.NBPT_COMMON_XZQXHF_ID = B.NBPT_SP_REGION_XZQX_XZQXID "
					+ "  WHERE B.NBPT_SP_REGION_XZQX_TYPE = '11' "
					+ "  ORDER BY B.NBPT_SP_REGION_XZQX_REGIONID+0 ASC, A.NBPT_COMMON_XZQXHF_ID ASC";
			List<CurrentRegion> resultList = this.query(sql, connection, CurrentRegion.class);

			return resultList;
		} catch (SQLException e) {

			log.error("查询所有大区对应省份出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_REGION receiveCurrentRegion(String regionId, Connection connection) throws SQLException {

		try {
			String sql = "SELECT * FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_UID = '" + regionId + "'";

			NBPT_SP_REGION regionInfo = this.oneQuery(sql, connection, NBPT_SP_REGION.class);

			return regionInfo;
		} catch (SQLException e) {

			log.error("查询部门信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON receiveCurrentPerson(String personPid, Connection connection) throws SQLException {

		try {

			if ("".equals(personPid)) {

				return new NBPT_SP_PERSON();
			}
			String sql = "SELECT * FROM NBPT_SP_PERSON WHERE NBPT_SP_PERSON_PID = '" + personPid + "'";

			NBPT_SP_PERSON personInfo = this.oneQuery(sql, connection, NBPT_SP_PERSON.class);

			return personInfo;
		} catch (SQLException e) {

			log.error("查询负责人信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_SP_PERSON> receiveRegionReper(String personType, Connection connection) throws SQLException {

		try {
			String sql = DBUtils.beanToSql(NBPT_SP_PERSON.class, "SELECT", "NBPT_SP_PERSON");

			List<NBPT_SP_PERSON> resultList = new ArrayList<>();
			if ("26".equals(personType)) {
				resultList = this.query(sql + " WHERE (NBPT_SP_PERSON_JOB = '21' OR NBPT_SP_PERSON_JOB = '26') "
						+ " ORDER BY NBPT_SP_PERSON_ID ASC", connection, NBPT_SP_PERSON.class);
			} else if ("22".equals(personType)) {
				resultList = this.query(sql + " WHERE NBPT_SP_PERSON_JOB = '22' " + " ORDER BY NBPT_SP_PERSON_ID ASC ",
						connection, NBPT_SP_PERSON.class);
			}

			return resultList;
		} catch (SQLException e) {

			log.error("查询分配负责人列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_SP_PERSON> searchRegionReper(String personType, String searchName, Connection connection)
			throws SQLException {

		try {
			String sql = DBUtils.beanToSql(NBPT_SP_PERSON.class, "SELECT", "NBPT_SP_PERSON");

			List<NBPT_SP_PERSON> resultList = new ArrayList<>();
			if ("26".equals(personType)) {
				resultList = this.query(sql + " WHERE (NBPT_SP_PERSON_JOB = '21' OR NBPT_SP_PERSON_JOB = '26') "
						+ " AND (NBPT_SP_PERSON_NAME LIKE '%" + searchName + "%' OR NBPT_SP_PERSON_LOGINID LIKE '%"
						+ searchName + "%') " + " ORDER BY NBPT_SP_PERSON_ID ASC", connection, NBPT_SP_PERSON.class);
			} else if ("22".equals(personType)) {
				resultList = this.query(sql + " WHERE NBPT_SP_PERSON_JOB = '22' " + " AND (NBPT_SP_PERSON_NAME LIKE '%"
						+ searchName + "%' OR NBPT_SP_PERSON_LOGINID LIKE '%" + searchName + "%') "
						+ " ORDER BY NBPT_SP_PERSON_ID ASC ", connection, NBPT_SP_PERSON.class);
			}

			return resultList;
		} catch (SQLException e) {

			log.error("查询分配负责人列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_COMMON_XZQXHF> receiveProvinceSelect(Connection connection) throws SQLException {

		try {
			String sql = "SELECT * FROM NBPT_COMMON_XZQXHF WHERE NBPT_COMMON_XZQXHF_LEVEL = '1' ORDER BY NBPT_COMMON_XZQXHF_ID ASC ";

			List<NBPT_COMMON_XZQXHF> resultList = this.query(sql, connection, NBPT_COMMON_XZQXHF.class);

			return resultList;

		} catch (SQLException e) {

			log.error("查询省份信息列表出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<Area_Xzqx_Info> receiveCurrentXzqxs(String regionId, Connection connection) throws SQLException {

		try {
			String sql = "SELECT B.NBPT_COMMON_XZQXHF_LEVEL AS XZQX_LEVEL," + "B.NBPT_COMMON_XZQXHF_ID AS XZQX_ID, "
					+ "B.NBPT_COMMON_XZQXHF_NAME AS XZQX_NAME, " + "A.NBPT_SP_REGION_XZQX_TYPE AS XZQX_TYPE "
					+ "FROM NBPT_SP_REGION_XZQX A " + "LEFT JOIN NBPT_COMMON_XZQXHF B "
					+ "ON A.NBPT_SP_REGION_XZQX_XZQXID = B.NBPT_COMMON_XZQXHF_ID "
					+ "WHERE NBPT_SP_REGION_XZQX_REGIONID = '" + regionId + "' "
					+ "ORDER BY NBPT_SP_REGION_XZQX_REGIONID ASC, " + "NBPT_SP_REGION_XZQX_TYPE ASC ";

			List<Area_Xzqx_Info> resultList = this.query(sql, connection, Area_Xzqx_Info.class);

			return resultList;

		} catch (SQLException e) {

			log.error("查询本地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<CurrentRegion> receiveRegion_Xzqxs(String regionUid, Connection connection) throws SQLException {

		try {
			
			String sql ="  SELECT " + 
						"  A.*," + 
						"  B.NBPT_SP_REGION_XZQX_TYPE," + 
						"  C.NBPT_COMMON_XZQXHF_ID," + 
						"  C.NBPT_COMMON_XZQXHF_NAME," + 
						"  C.NBPT_COMMON_XZQXHF_LEVEL," + 
						"  (SELECT NBPT_COMMON_XZQXHF_NAME FROM NBPT_COMMON_XZQXHF WHERE NBPT_COMMON_XZQXHF_ID = C.NBPT_COMMON_XZQXHF_PID) AS NBPT_COMMON_XZQXHF_PNAME," + 
						"  D.NBPT_SP_REGION_NAME AS REGION_NAME," + 
						"  D.NBPT_SP_REGION_ID AS REGION_ID" + 
						"  FROM NBPT_SP_REGION A" + 
						"  LEFT JOIN NBPT_SP_REGION_XZQX B" + 
						"  ON A.NBPT_SP_REGION_ID = B.NBPT_SP_REGION_XZQX_REGIONID" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF C" + 
						"  ON B.NBPT_SP_REGION_XZQX_XZQXID = C.NBPT_COMMON_XZQXHF_ID" + 
						"  LEFT JOIN NBPT_SP_REGION D" + 
						"  ON SUBSTRING(A.NBPT_SP_REGION_ID,1,2) = D.NBPT_SP_REGION_ID" + 
						"  WHERE A.NBPT_SP_REGION_UID = '" + regionUid + "'" + 
						"  ORDER BY C.NBPT_COMMON_XZQXHF_ID";
			List<CurrentRegion> resultList;
			
			resultList = this.query(sql, connection, CurrentRegion.class);
			
			return resultList;
		} catch (SQLException e) {

			log.error("查询本地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	/**
	 * 添加地区下辖区县时获取添加下拉表方法
	 */
	@Override
	public List<NBPT_COMMON_XZQXHF> receiveAreaContainSelects(String parentId, Connection connection) throws SQLException {

		try {
			
			List<NBPT_COMMON_XZQXHF> resultList = this.getXzqxhfs(parentId, connection);
			
			return resultList;
			
		} catch (SQLException e) {
			
			log.error("查询本地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void postAddRegionXzqx(NBPT_SP_REGION_XZQX region_XZQX, Connection connection) throws SQLException {

		try {
			
			String sql = DBUtils.beanToSql(NBPT_SP_REGION_XZQX.class, "INSERT", "NBPT_SP_REGION_XZQX", region_XZQX);
			
			this.excuteUpdate(sql, connection);
			
			
		} catch (SQLException e) {
			
			log.error("提交添加地区下辖行政区县" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}

}
