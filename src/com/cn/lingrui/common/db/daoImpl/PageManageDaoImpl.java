package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.PageManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;
import com.cn.lingrui.common.pojos.pageManage.Func;
import com.cn.lingrui.common.pojos.pageManage.Module;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;

@Repository("pageManageDao")
public class PageManageDaoImpl extends BaseDaoImpl implements PageManageDao{

	private static Logger log = LogManager.getLogger();
	
	@Override
	public List<NBPT_RSFZ_PAGE> getPageList(Connection conn) throws SQLException{
		
		// 初始化返回节
		List<NBPT_RSFZ_PAGE> resultList = new ArrayList<NBPT_RSFZ_PAGE>();

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "SELECT", "NBPT_RSFZ_PAGE");
		if(!"".equals(sql)) {

			try {
				resultList = this.query(sql + " order by NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_TYPE ASC,NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_ID ASC", conn, NBPT_RSFZ_PAGE.class);				
			} catch (SQLException e) {

				log.info("查询所有页面出错" + e.getMessage());
				throw new SQLException();
				
			}
		}
		return resultList;
	}

	@Override
	public List<NBPT_RSFZ_PAGE> getModuleList(Connection conn) {
		// 初始化返回节
		List<NBPT_RSFZ_PAGE> resultList = new ArrayList<NBPT_RSFZ_PAGE>();
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "SELECT", "NBPT_RSFZ_PAGE");
		if(!"".equals(sql)) {

			try {
				resultList = this.query(sql + " where NBPT_RSFZ_PAGE_TYPE = '0' ORDER BY NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_ID ASC ", conn, NBPT_RSFZ_PAGE.class);				
			} catch (Exception e) {

				log.info("查询模块出错" + e.getMessage());
			}
		}
		return resultList;
	}

	@Override
	public void addModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "INSERT", "NBPT_RSFZ_PAGE", rsfzpage);
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql, conn);
				
			}catch (Exception e) {

				log.info("添加模块出错" + CommonUtil.getTrace(e));
			}
		}
	}

	@Override
	public void deleteModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = "DELETE FROM NBPT_RSFZ_PAGE WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'";

		try {
			
			this.excuteUpdate(sql, conn);
			
		}catch (Exception e) {

			log.info("删除模块出错" + CommonUtil.getTrace(e));
		}
		
	}

	@Override
	public void updateModule(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "UPDATE", "NBPT_RSFZ_PAGE", rsfzpage);
		
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql + " WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'", conn);
				
			}catch (Exception e) {

				log.info("修改模块出错" + CommonUtil.getTrace(e));
			}
		}
		System.out.println(sql);
	}

	@Override
	public List<NBPT_RSFZ_PAGE> getFuncList(Connection conn) {

		// 初始化返回节
		List<NBPT_RSFZ_PAGE> resultList = new ArrayList<NBPT_RSFZ_PAGE>();
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "SELECT", "NBPT_RSFZ_PAGE");
		if(!"".equals(sql)) {

			try {
				resultList = this.query(sql + " where NBPT_RSFZ_PAGE_TYPE = '1' ORDER BY NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_ID ASC ", conn, NBPT_RSFZ_PAGE.class);
			} catch (Exception e) {

				log.info("查询功能出错" + e.getMessage());
			}
		}
		return resultList;
	}

	@Override
	public void addFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "INSERT", "NBPT_RSFZ_PAGE", rsfzpage);
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql, conn);
				
			}catch (Exception e) {

				log.info("添加功能出错" + CommonUtil.getTrace(e));
			}
		}		
	}

	@Override
	public void deleteFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = "DELETE FROM NBPT_RSFZ_PAGE WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'";

		try {
			
			this.excuteUpdate(sql, conn);
			
		}catch (Exception e) {

			log.info("删除模块出错" + CommonUtil.getTrace(e));
		}
		
	}

	@Override
	public void updateFunc(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "UPDATE", "NBPT_RSFZ_PAGE", rsfzpage);
		
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql + " WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'", conn);
				
			}catch (Exception e) {

				log.info("修改功能出错" + CommonUtil.getTrace(e));
			}
		}
		
	}

	@Override
	public List<NBPT_RSFZ_PAGE> getPmPageList(Connection conn) {
		// 初始化返回节
		List<NBPT_RSFZ_PAGE> resultList = new ArrayList<NBPT_RSFZ_PAGE>();
		
		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "SELECT", "NBPT_RSFZ_PAGE");
		if(!"".equals(sql)) {

			try {
				resultList = this.query(sql + " where NBPT_RSFZ_PAGE_TYPE = '2' ORDER BY NBPT_RSFZ_PAGE.NBPT_RSFZ_PAGE_ID ASC ", conn, NBPT_RSFZ_PAGE.class);
			} catch (Exception e) {

				log.info("查询页面出错" + e.getMessage());
			}
		}
		return resultList;
	}

	@Override
	public void addPage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "INSERT", "NBPT_RSFZ_PAGE", rsfzpage);
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql, conn);
				
			}catch (Exception e) {

				log.info("添加页面出错" + CommonUtil.getTrace(e));
			}
		}		
	}

	@Override
	public void deletePage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		// 1.删除当前页面
		String sql = "DELETE FROM NBPT_RSFZ_PAGE WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'";

		// 2.同时删除
		try {
			
			this.excuteUpdate(sql, conn);
			
		}catch (Exception e) {

			log.info("删除页面出错" + CommonUtil.getTrace(e));
		}
		
	}

	@Override
	public void updatePage(Connection conn, NBPT_RSFZ_PAGE rsfzpage) {

		String sql = DBUtils.beanToSql(NBPT_RSFZ_PAGE.class, "UPDATE", "NBPT_RSFZ_PAGE", rsfzpage);
		
		if(!"".equals(sql)) {
			
			try {
				
				this.excuteUpdate(sql + " WHERE NBPT_RSFZ_PAGE_ID = '" + rsfzpage.getNBPT_RSFZ_PAGE_ID() + "'", conn);
				
			}catch (Exception e) {

				log.info("修改页面出错" + CommonUtil.getTrace(e));
			}
		}
	}
	
	/**
	 * 处理查询到的所有权限页面,包括模块和功能
	 * @param rsfzPages
	 * @throws SQLException 
	 */
	@Override
	public List<Module> getClassifiedModuleList(Connection conn) throws SQLException {

		// 初始化处理结果
		List<Module> moduleList = new ArrayList<Module>();
		List<NBPT_RSFZ_PAGE> rsfzPages = this.getPageList(conn);
		
		// 处理查询结果
		for (NBPT_RSFZ_PAGE rsfzpage : rsfzPages) {

			if ("0".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {

				// 新建模块,加入到moduleList中
				Module module = new Module();

				module.setModuleId(rsfzpage.getNBPT_RSFZ_PAGE_ID());
				module.setModuleName(rsfzpage.getNBPT_RSFZ_PAGE_NAME());
				module.setModuleBz(rsfzpage.getNBPT_RSFZ_PAGE_BZ());
				moduleList.add(module);
			}

			// 如果是功能
			else if ("1".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {

				// 1.判断该功能所属模块
				String moduleId = rsfzpage.getNBPT_RSFZ_PAGE_PID();

				// 2.锁定模块
				Module module = getModule(moduleList, moduleId);

				if(null != module) {

					// 3.追加新的功能
					Func func = new Func();
					func.setFuncId(rsfzpage.getNBPT_RSFZ_PAGE_ID());
					func.setFuncName(rsfzpage.getNBPT_RSFZ_PAGE_NAME());
					func.setFuncBz(rsfzpage.getNBPT_RSFZ_PAGE_BZ());
					module.getFuncList().add(func);
				}
			}
			
			// 如果是页面
			else if ("2".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {
				
				// 1.判断所属模块
				String moduleId = rsfzpage.getNBPT_RSFZ_PAGE_PID().substring(0, 2);
				
				// 2.判断所属功能
				String funcId = rsfzpage.getNBPT_RSFZ_PAGE_PID();
				
				// 3.锁定功能
				Func func = getFunc(moduleList, moduleId, funcId);
				
				if(null != func) {

					// 4.追加新页面
					func.getPageList().add(rsfzpage);
				}
			}
		}
		return moduleList;
	}

	/**
	 * 锁定模块
	 * @param moduleList
	 * @param moduleId
	 * @return
	 */
	private Module getModule(List<Module> moduleList, String moduleId) {

		for (Module module : moduleList) {

			if (moduleId.equals(module.getModuleId())) {

				return module;
			}
		}
		return null;
	}

	/**
	 * 锁定功能
	 * @param moduleList
	 * @param moduleId
	 * @param funcId
	 * @return
	 */
	private Func getFunc(List<Module> moduleList, String moduleId, String funcId) {

		for(Module module : moduleList) {
			
			if(moduleId.equals(module.getModuleId())) {
				
				for(Func func : module.getFuncList()) {
					
					if(funcId.equals(func.getFuncId())) {
						
						return func;
					}
				}
			}
		}
		return null;
	}
}
