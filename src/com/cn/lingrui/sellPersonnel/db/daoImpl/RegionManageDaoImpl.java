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
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_PROCEDURE_TREE;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.region.CurrentRegion;
import com.cn.lingrui.sellPersonnel.pojos.region.Area_Xzqx_Info;
import com.cn.lingrui.sellPersonnel.pojos.region.RegionsPojo;
import com.sun.rowset.CachedRowSetImpl;

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

			sql.append("  AND A.NBPT_SP_REGION_FLAG = '1'");
			resultList = this.queryForClaszs(sql.toString() + " ORDER BY NBPT_SP_REGION_ID ASC ", conn, NBPT_VIEW_REGION.class);

			return resultList;
		} catch (SQLException e) {

			log.error("根据选择查询大区出错" + CommonUtil.getTraceInfo());
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
				sql.append("FROM NBPT_SP_REGION B ");
				sql.append("WHERE B.NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND B.NBPT_SP_REGION_LEVEL = '1' ");
				sql.append("AND B.NBPT_SP_REGION_FLAG = '1' ");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("ORDER BY NBPT_SP_PERSON_ID ASC ");
				
				resultList = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
				
			} else if ("22".equals(personType)) {

				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION B ");
				sql.append("WHERE B.NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND B.NBPT_SP_REGION_LEVEL = '2' ");
				sql.append("AND B.NBPT_SP_REGION_FLAG = '1' ");
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
				sql.append("AND B.NBPT_SP_REGION_FLAG = '1' ");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("AND (");
				sql.append("A.NBPT_SP_PERSON_NAME LIKE '%" + searchName + "%' ");
				sql.append("OR A.NBPT_SP_PERSON_LOGINID LIKE '%" + searchName + "%'");
				sql.append(") ");
				sql.append("ORDER BY A.NBPT_SP_PERSON_ID ASC ");
				
				result = this.queryForClaszs(sql.toString(), connection, NBPT_SP_PERSON.class);
				
			} else if ("22".equals(personType)) {

				sql.append("(");
				sql.append("SELECT * ");
				sql.append("FROM NBPT_SP_REGION B ");
				sql.append("WHERE B.NBPT_SP_REGION_RESPONSIBLER = A.NBPT_SP_PERSON_PID ");
				sql.append("AND B.NBPT_SP_REGION_LEVEL = '1' ");
				sql.append("AND B.NBPT_SP_REGION_FLAG = '1' ");
				sql.append(") ");
				sql.append("AND A.NBPT_SP_PERSON_JOB IN ('21', '26') ");
				sql.append("AND (");
				sql.append("A.NBPT_SP_PERSON_NAME LIKE '%" + searchName + "%' ");
				sql.append("OR A.NBPT_SP_PERSON_LOGINID LIKE '%" + searchName + "%'");
				sql.append(") ");
				sql.append("ORDER BY A.NBPT_SP_PERSON_ID ASC ");
				
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
						+ "NBPT_SP_REGION_ONAME = '" + updateRegion.getNBPT_SP_REGION_ONAME() + "', "
						+ "NBPT_SP_REGION_NAME = '" + updateRegion.getNBPT_SP_REGION_NAME() + "' "
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

	@Override
	public List<NBPT_SP_REGION> receiveDumpedRegion(String provinceId, Connection connection) throws SQLException {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM NBPT_SP_REGION A ");
		
		if("".equals(provinceId)) {

			sql.append("WHERE A.NBPT_SP_REGION_FLAG = '0' ");
			sql.append("AND A.NBPT_SP_REGION_LEVEL = '1' ");
		} else {

			sql.append("WHERE A.NBPT_SP_REGION_FLAG = '0' ");
			sql.append("AND A.NBPT_SP_REGION_PROVINCE_ID = '" + provinceId + "' ");
			sql.append("AND A.NBPT_SP_REGION_LEVEL = '2' ");
		}

		try {
			
			return this.queryForClaszs(sql.toString(), connection, NBPT_SP_REGION.class);
		} catch (SQLException e) {
			
			log.error("查询废弃的部门出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public NBPT_VIEW_XZQX receiveRegion_Xzqxs(String regionType, String xzqxId, String type, Connection connection)
			throws SQLException {

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM NBPT_VIEW_XZQX A ");	
			sql.append("WHERE 1=1 ");
			sql.append("AND A.NBPT_SP_REGION_TYPE = '" + regionType + "' ");
			sql.append("AND A.NBPT_COMMON_XZQXHF_ID = '" + xzqxId + "' ");
			sql.append("AND A.NBPT_SP_REGION_XZQX_TYPE = '" + type + "' ");
			return this.oneQueryForClasz(sql, connection, NBPT_VIEW_XZQX.class);
		} catch (SQLException e) {
			log.error("查询部门行政区县绑定关系出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void updateRegionXzqx_rp(String rpUid, String newUid, Connection connection)
			throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE NBPT_SP_REGION_XZQX SET NBPT_SP_REGION_XZQX_REGIONUID = '" + newUid + "' WHERE NBPT_SP_REGION_XZQX_UID = '" + rpUid + "'");
		
		try {
			
			this.excuteUpdate(sql, connection);
		} catch (SQLException e) {
			
			log.error("查询部门行政区县绑定关系出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public void addRegion(NBPT_SP_REGION newRegion, Connection connection) throws SQLException {
		
		try {
			String sql = DBUtils.beanToSql(NBPT_SP_REGION.class, "insert", "NBPT_SP_REGION", newRegion);
			this.excuteUpdate(sql, connection);
		} catch (SQLException e) {
			
			log.error("添加新的大区或地区出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}

	@Override
	public List<NBPT_PROCEDURE_TREE> receiveRegionTree(String pid, Connection conn) throws SQLException {
		
		try {
			
			List<CachedRowSetImpl> rss = this.callProcedureForMoreRs("NBPT_PROCEDURE_REGION_TREE", conn, new String[] {pid});
			List<NBPT_PROCEDURE_TREE> result = DBUtils.rsToBean(NBPT_PROCEDURE_TREE.class
					, rss.get(0));
			result.addAll(DBUtils.rsToBean(NBPT_PROCEDURE_TREE.class
					, rss.get(1)));
			return result;
		} catch (SQLException e) {
			
			log.error("获取部门选取树出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_PROCEDURE_TREE> receivePersonTree(Connection connection) throws SQLException {

		try {
			
			List<CachedRowSetImpl> rss = this.callProcedureForMoreRs("NBPT_PROCEDURE_PERSON_TREE", connection);
			List<NBPT_PROCEDURE_TREE> result = DBUtils.rsToBean(NBPT_PROCEDURE_TREE.class
					, rss.get(0));
			result.addAll(DBUtils.rsToBean(NBPT_PROCEDURE_TREE.class
					, rss.get(1)));
			return result;
		} catch (SQLException e) {
			
			log.error("获取部门选取树出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public List<NBPT_VIEW_CURRENTPERSON> receiveAbleResper(String type, String personType, Connection connection) throws SQLException {

		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT * FROM NBPT_VIEW_PERSON A ");
			sql.append("WHERE 1=1 ");
			if("area".equals(type)) {
				sql.append("AND (SUBSTRING(A.NBPT_SP_PERSON_JOB,2,1) = '2' OR SUBSTRING(A.NBPT_SP_PERSON_JOB,2,1) = '6') ");
				sql.append("AND (A.NBPT_SP_PERSON_AREA_UID = '' OR A.NBPT_SP_PERSON_AREA_UID IS NULL) ");
			}
			if("region".equals(type)) {
				sql.append("AND (SUBSTRING(A.NBPT_SP_PERSON_JOB,2,1) = '1' OR SUBSTRING(A.NBPT_SP_PERSON_JOB,2,1) = '6') ");
				sql.append("AND (A.NBPT_SP_PERSON_REGION_UID = '' OR A.NBPT_SP_PERSON_REGION_UID IS NULL) ");
			}
			sql.append(" AND A.NBPT_SP_PERSON_TYPE = '" + personType + "' ");
			List<NBPT_VIEW_CURRENTPERSON> result = this.queryForClaszs(sql, connection, NBPT_VIEW_CURRENTPERSON.class);
			return result;
		} catch (Exception e) {
			log.error("获取可用负责人出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}
}
























