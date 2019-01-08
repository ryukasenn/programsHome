package com.cn.lingrui.pm.db.daoimpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.pm.db.dao.GrpmDao;
import com.cn.lingrui.pm.pojos.PmInfo;


@Repository("grpmDao")
public class GrpmDaoImpl extends PmBaseDaoImpl implements GrpmDao {
	
	private static Logger log = LogManager.getLogger();
	
	//获取数据库中同段位，最大日期的排名情况
	@Override
	public List<PmInfo> getSameDwPm(String dw, Connection connection) throws SQLException {
		
		try {
			String sql = "SELECT  * FROM xszj_pm WHERE rq =(select MAX(rq) from xszj_pm) "
					+ "AND dw ='" + dw + "' order by pm";

			List<PmInfo> pmSamedwList =new ArrayList<>();
			pmSamedwList = this.queryForClaszs(sql, connection, PmInfo.class);
			return pmSamedwList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public PmInfo getcurrentDwpm( String ryxm, Connection connection) throws SQLException {
		try {
			String sql = "SELECT top(1) * FROM xszj_pm WHERE ryxm ='" + ryxm + "' order by rq desc";

			PmInfo personalPmInfo = this.oneQueryForClasz(sql, connection, PmInfo.class);

			return personalPmInfo;
		} catch (SQLException e) {
			log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
	}

	@Override
	public int zfValue(String ryxm, Connection connection) throws SQLException {
		try {
			String sql = "SELECT qs FROM xszj_pm WHERE rq =(select MAX(rq) from xszj_pm) "
					+ "AND ryxm = '" + ryxm + "'";

			int zfz=0;//正负值
			zfz = Integer.parseInt(this.oneQueryForObject(sql, connection));
			
			return zfz;
		} catch (SQLException e) {
			log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
		
	}

	@Override
	public List<PmInfo> getAllResult(String ryxm, Connection connection) throws SQLException {
		try {
			String sql = "SELECT * FROM xszj_pm WHERE ryxm='"
					+ ryxm + "' and rq in (select distinct top(13) rq from xszj_pm) order by rq";

			List<PmInfo> allResultList =new ArrayList<>();
			allResultList = this.queryForClaszs(sql, connection, PmInfo.class);
			return allResultList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public String getdqmcStr(String ryxm, Connection connection) throws SQLException {
		try {
			String sql = "SELECT top(1) dqmc FROM xszj_pm WHERE ryxm='"
					+ ryxm + "' order by rq desc";

			String dqmcStr = "";
			dqmcStr = this.oneQueryForObject(sql, connection);
			return dqmcStr;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public List<PmInfo> getSameDqPm(String dqmc,String pmrq, Connection connection) throws SQLException {
		try {
			String sql = "SELECT * FROM xszj_pm WHERE rq like '%"+ pmrq +"%'"
					+ " AND dqmc ='" + dqmc + "' order by pm";

			List<PmInfo> sameDqmcPmList =new ArrayList<>();
			sameDqmcPmList = this.queryForClaszs(sql, connection, PmInfo.class);
			return sameDqmcPmList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public List<PmInfo> getbeforetenPmList(String pmrq,String mc, Connection connection) throws SQLException {
		try {
			String sql = "";
			String pmmc ="";			
			if("".equals(mc)){
				pmmc="10";
			}else
				pmmc=mc;		
			
			
			if("".equals(pmrq)) {
				sql= "SELECT top("+ pmmc +") * FROM xszj_slb WHERE rq = (select MAX(rq) from xszj_slb )"
				+ " order by ljpm";
			}else
				sql = "SELECT top(" + pmmc + ") * FROM xszj_slb WHERE rq = '" +pmrq + "'"
						+ " order by ljpm ";			

			List<PmInfo> beforetenPmList =new ArrayList<>();
			beforetenPmList = this.queryForClaszs(sql, connection, PmInfo.class);
			return beforetenPmList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public List<PmInfo> getlasttenPmList(String pmrq,String mc, Connection connection) throws SQLException {
		try {
			String sql = "";
			String pmmc ="";			
			if("".equals(mc)){
				pmmc="10";
			}else
				pmmc=mc;
			
			if("".equals(pmrq)) {
				sql= "SELECT top(" + pmmc + ") * FROM xszj_slb WHERE rq = (select MAX(rq) from xszj_slb )"
				+ " order by ljpm desc";
			}else
				sql = "SELECT top(" + pmmc + ") * FROM xszj_slb WHERE rq = '" +pmrq + "'"
						+ " order by ljpm desc";			

			List<PmInfo> lasttenPmList =new ArrayList<>();
			lasttenPmList = this.queryForClaszs(sql, connection, PmInfo.class);
			return lasttenPmList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
		}
	}

	@Override
	public List<PmInfo> getSameDwPm(String rq, String xm, String dw, Connection connection) throws SQLException {
		try {
			String sql = "SELECT * FROM xszj_pm WHERE rq ='" + rq + "' "
					+ "AND dw ='" + dw + "' order by pm";

			List<PmInfo> pmSamedwList =new ArrayList<>();
			pmSamedwList = this.queryForClaszs(sql, connection, PmInfo.class);
			return pmSamedwList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}

	@Override
	public PmInfo getcurrentDwpm(String ryxm, String rq, Connection connection) throws SQLException {
		try {
			String sql = "SELECT  * FROM xszj_pm WHERE ryxm ='" + ryxm + "' and rq ='" + rq+"' order by rq desc";

			PmInfo personalPmInfo = this.oneQueryForClasz(sql, connection, PmInfo.class);

			return personalPmInfo;
		} catch (SQLException e) {
			log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}

	
	
	

}
