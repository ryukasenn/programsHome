package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;

/**
 * 部门管理相关数据库操作
 * @author lhc
 *
 */
@Repository("regionManageDao")
public class RegionManageDaoImpl extends SellPersonnelBaseDaoImpl implements RegionManageDao {

	private static Logger log = LogManager.getLogger();

	@Override
	public List<NBPT_SP_REGION> receiveRegionsSelect(Connection conn) throws SQLException {  

		try {
			String sql = DBUtils.beanToSql(NBPT_SP_REGION.class, "SELECT", "NBPT_SP_REGION");

			List<NBPT_SP_REGION> resultList = this.queryForClaszs(
					sql + " WHERE NBPT_SP_REGION_LEVEL = '1' ORDER BY NBPT_SP_REGION_ID ASC", conn,
					NBPT_SP_REGION.class);

			return resultList;
		} catch (SQLException e) {

			log.error("查询大区信息下拉框出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_REGION> receiveCurrentRegions(RegionsPojo pojo, Connection conn) throws SQLException {

		try {

			List<NBPT_VIEW_REGION> resultList = null;

			StringBuffer sql = new StringBuffer(  "  SELECT A.*"
												+ "  FROM NBPT_VIEW_REGION A"
												+ "  WHERE 1=1");
			if (null == pojo.getRegionName() || "".equals(pojo.getRegionName())) {

			} else {

				String regionName = pojo.getRegionName();
				String regionId = regionName.substring(0, 2);
				sql.append("  AND A.NBPT_SP_REGION_ID LIKE '" + regionId + "%'");
			}

			if ("1".equals(pojo.getTypeRadio())) {

				sql.append("  AND A.NBPT_SP_REGION_LEVEL = '1'");

			} else if ("2".equals(pojo.getTypeRadio())) {

			}

			resultList = this.queryForClaszs(sql.toString() + " ORDER BY NBPT_SP_REGION_ID ASC ", conn, NBPT_VIEW_REGION.class);

			return resultList;
		} catch (SQLException e) {

			log.error("根据选择查询大区出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_SP_PERSON receiveCurrentResper(String personPid, Connection connection) throws SQLException {

		try {

			if ("".equals(personPid)) {

				return new NBPT_SP_PERSON();
			}
			String sql = "SELECT * FROM NBPT_SP_PERSON WHERE NBPT_SP_PERSON_PID = '" + personPid + "'";

			NBPT_SP_PERSON personInfo = this.oneQueryForClasz(sql, connection, NBPT_SP_PERSON.class);

			return personInfo;
		} catch (SQLException e) {

			log.error("查询负责人信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_SP_PERSON> receiveRegionReper(String personType, Connection connection) throws SQLException {

		try {

			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT A.* ");
			sql.append("FROM NBPT_SP_PERSON A ");
			sql.append("WHERE NOT EXISTS ");
			
			List<NBPT_SP_PERSON> resultList = new ArrayList<>();
			if ("26".equals(personType)) {
				
				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION ");
				sql.append("WHERE NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND NBPT_SP_REGION_LEVEL = '1'");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("ORDER BY NBPT_SP_PERSON_ID ASC ");
				
				resultList = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
				
			} else if ("22".equals(personType)) {

				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION ");
				sql.append("WHERE NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND NBPT_SP_REGION_LEVEL = '2'");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('22', '26') ");
				sql.append("ORDER BY NBPT_SP_PERSON_ID ASC ");
				
				resultList = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
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
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT A.* ");
			sql.append("FROM NBPT_SP_PERSON A ");
			sql.append("WHERE NOT EXISTS ");
			
			List<NBPT_SP_PERSON> result = new ArrayList<>();
			if ("26".equals(personType)) {
				
				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION B ");
				sql.append("WHERE B.NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND B.NBPT_SP_REGION_LEVEL = '1' ");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("AND (");
				sql.append("NBPT_SP_PERSON_NAME LIKE '%" + searchName + "%' ");
				sql.append("OR NBPT_SP_PERSON_LOGINID LIKE '%" + searchName + "%'");
				sql.append(") ");
				sql.append("ORDER BY NBPT_SP_PERSON_ID ASC ");
				
				result = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
				
			} else if ("22".equals(personType)) {

				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION B ");
				sql.append("WHERE B.NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND B.NBPT_SP_REGION_LEVEL = '1' ");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("AND (");
				sql.append("NBPT_SP_PERSON_NAME LIKE '%" + searchName + "%' ");
				sql.append("OR NBPT_SP_PERSON_LOGINID LIKE '%" + searchName + "%'");
				sql.append(") ");
				sql.append("ORDER BY NBPT_SP_PERSON_ID ASC ");
				
				result = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
			}
			return result;
		} catch (SQLException e) {

			log.error("查询分配负责人列表出错" + CommonUtil.getTraceInfo());
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

			List<Area_Xzqx_Info> resultList = this.queryForClaszs(sql, connection, Area_Xzqx_Info.class);

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
			
			resultList = this.queryForClaszs(sql, connection, CurrentRegion.class);
			
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

	@Override
	public void updateRegion(NBPT_SP_REGION updateRegion, Connection connection) throws SQLException {

		try {
			String sql =  "UPDATE NBPT_SP_REGION SET "
						+ "NBPT_SP_REGION_RESPONSIBLER = '" + updateRegion.getNBPT_SP_REGION_RESPONSIBLER() + "', "
						+ "NBPT_SP_REGION_NOTE = '" + updateRegion.getNBPT_SP_REGION_NOTE() + "', "
						+ "NBPT_SP_REGION_ONAME = '" + updateRegion.getNBPT_SP_REGION_ONAME() + "' "
						+ "WHERE NBPT_SP_REGION_UID = '" + updateRegion.getNBPT_SP_REGION_UID() + "'";
			this.excuteUpdate(sql, connection);
			
		} catch (SQLException e) {
			
			log.error("提交修改大区或地区信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void deleteRegionXzqx(String regionId, String cityValue, Connection connection) throws SQLException {
		try {
			String sql =  "DELETE FROM NBPT_SP_REGION_XZQX "
						+ "WHERE NBPT_SP_REGION_XZQX_REGIONID = '" + regionId + "' AND "
						+ "NBPT_SP_REGION_XZQX_XZQXID = '" + cityValue + "' AND "
						+ "NBPT_SP_REGION_XZQX_TYPE = '22'";
			this.excuteUpdate(sql, connection);
			
		} catch (SQLException e) {
			
			log.error("提交删除地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}

	@Override
	public void deletePersonXzqx(String cityValue, Connection connection) throws SQLException {
		
		try {
			String sql =  "DELETE FROM NBPT_SP_PERSON_XZQX "
						+ "WHERE NBPT_SP_PERSON_XZQX_XID = '" + cityValue + "'";
			this.excuteUpdate(sql, connection);
			
		} catch (SQLException e) {
			
			log.error("提交删除地区下辖行政区县出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public String receiveRegonNeedSum(String regionUid, Connection connection) throws SQLException {

		String sql = "SELECT SUM(CAST(A.NBPT_SP_REGION_NEED AS INT)) FROM NBPT_VIEW_REGION A WHERE A.NBPT_SP_REGION_PARENT_UID = '" + regionUid + "'";
		
		try {
			
			return this.oneQueryForObject(sql, connection);
		} catch (SQLException e) {
			
			log.error("获取大区应配参考数出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
}
























