package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.RoleManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_ROLE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;
import com.cn.lingrui.common.pojos.roleManage.RmRolePojoIn;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;

@Repository("roleManageDao")
public class RoleManageDaoImpl extends BaseDaoImpl implements RoleManageDao{

	private static Logger log = LogManager.getLogger();
	
	@Override
	public List<NBPT_RSFZ_ROLE> getRoleList(Connection conn) throws SQLException {
		
		// 初始化返回结果
		List<NBPT_RSFZ_ROLE> resultList = new ArrayList<NBPT_RSFZ_ROLE>();
		String sql = DBUtils.beanToSql(NBPT_RSFZ_ROLE.class, "SELECT", "NBPT_RSFZ_ROLE");
		if(!"".equals(sql)) {

			try {
				resultList = this.queryForClaszs(sql + " WHERE NBPT_RSFZ_ROLE.NBPT_RSFZ_ROLE_LEVEL >= '1' "
						+ " order by NBPT_RSFZ_ROLE.NBPT_RSFZ_ROLE_ID ASC,NBPT_RSFZ_ROLE.NBPT_RSFZ_ROLE_LEVEL ASC", conn, NBPT_RSFZ_ROLE.class);
			} catch (SQLException e) {

				log.error("查询所有角色出错" + e.getMessage());
				throw new SQLException();
			}
		}
		return resultList;
	}

	@Override
	public void postUpdateRole(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException {


		String sql = DBUtils.beanToSql(NBPT_RSFZ_ROLE.class, "update", "NBPT_RSFZ_ROLE", role);
		if(!"".equals(sql)) {
			
			this.excuteUpdate(sql + " WHERE NBPT_RSFZ_ROLE_ID = '" + role.getNBPT_RSFZ_ROLE_ID() + "' ", conn);
		}
	}

	@Override
	public void postAddRole(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_ROLE.class, "insert", "NBPT_RSFZ_ROLE", role);
		this.excuteUpdate(sql, conn);
	}

	@Override
	public void postDeleteRole(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException {

		String sql = "DELETE FROM NBPT_RSFZ_ROLE WHERE NBPT_RSFZ_ROLE_ID = '" + role.getNBPT_RSFZ_ROLE_ID() + "'";
		
		this.excuteUpdate(sql, conn);
		
	}

	@Override
	public NBPT_RSFZ_ROLE getRole(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException {
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_ROLE.class, "select", "NBPT_RSFZ_ROLE");
		
		List<NBPT_RSFZ_ROLE> results = this.queryForClaszs(sql + " WHERE NBPT_RSFZ_ROLE_ID = '" + role.getNBPT_RSFZ_ROLE_ID() + "' ", conn, NBPT_RSFZ_ROLE.class);
		
		if(0 == results.size()) {
			
			return null;
		} 
		
		return results.get(0);
	}

	@Override
	public List<NBPT_RSFZ_R_P> getRole_Page(Connection conn, NBPT_RSFZ_ROLE role) throws SQLException {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_R_P.class, "select", "NBPT_RSFZ_R_P");
		
		List<NBPT_RSFZ_R_P> resultList = new ArrayList<NBPT_RSFZ_R_P>();
				
		resultList = this.queryForClaszs(sql + " WHERE NBPT_RSFZ_R_P_RID = '" + role.getNBPT_RSFZ_ROLE_ID() + "' ", conn, NBPT_RSFZ_R_P.class);
		
		
		return resultList;
	}

	@Override
	public void postUpdateR_P(Connection conn, RmRolePojoIn in) throws SQLException{
		
		// 先删除该角色权限信息
		this.excuteUpdate("DELETE FROM NBPT_RSFZ_R_P WHERE NBPT_RSFZ_R_P_RID = '" + in.getNBPT_RSFZ_ROLE_ID() + "'", conn);
		
		// 更新角色权限
		List<String> sqls = new ArrayList<String>();
		for(String module : in.getModules()) {
			
			String sql = "insert into NBPT_RSFZ_R_P (NBPT_RSFZ_R_P_RID, NBPT_RSFZ_R_P_PID, NBPT_RSFZ_R_P_PTYPE) VALUES ('" + in.getNBPT_RSFZ_ROLE_ID() + "', '" + module + "', '0')";
			sqls.add(sql);
		}
		for(String func : in.getFuncs()) {
			
			String sql = "insert into NBPT_RSFZ_R_P (NBPT_RSFZ_R_P_RID, NBPT_RSFZ_R_P_PID, NBPT_RSFZ_R_P_PTYPE) VALUES ('" + in.getNBPT_RSFZ_ROLE_ID() + "', '" + func + "', '1')";
			sqls.add(sql);
		}
		for(String page : in.getPages()) {
			
			String sql = "insert into NBPT_RSFZ_R_P (NBPT_RSFZ_R_P_RID, NBPT_RSFZ_R_P_PID, NBPT_RSFZ_R_P_PTYPE) VALUES ('" + in.getNBPT_RSFZ_ROLE_ID() + "', '" + page + "', '2')";
			sqls.add(sql);
		}
		
		try {
			this.excuteUpdateGroups(sqls, conn);
			
		} catch (SQLException e) {

			log.error("更新角色权限出错" + CommonUtil.getTrace(e));
			throw new SQLException();
		}		
	}

}






















