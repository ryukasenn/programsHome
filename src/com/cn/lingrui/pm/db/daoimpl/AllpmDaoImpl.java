package com.cn.lingrui.pm.db.daoimpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cn.lingrui.pm.db.dao.AllpmDao;
import com.cn.lingrui.pm.pojos.PmInfo;

@Repository("allpmDao")
public class AllpmDaoImpl extends PmBaseDaoImpl implements AllpmDao {

	@Override
	public List<PmInfo> getAllPm(Connection connection) throws SQLException {
		try {
			
			String sql = "SELECT * FROM xszj_pm WHERE rq =(select MAX(rq) from xszj_pm) order by pm";
			List<PmInfo> allPmList =new ArrayList<>();
			allPmList = this.queryForClaszs(sql, connection, PmInfo.class);
			return allPmList;
		} catch (SQLException e) {
			//log.error("查询个人排名信息出错" + CommonUtil.getTraceInfo());
			e.printStackTrace();
			throw new SQLException();
			
		}
	}
	
}
