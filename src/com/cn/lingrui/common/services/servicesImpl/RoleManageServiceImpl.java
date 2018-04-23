package com.cn.lingrui.common.services.servicesImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.db.dao.PageManageDao;
import com.cn.lingrui.common.db.dao.RoleManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_ROLE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;
import com.cn.lingrui.common.pojos.pageManage.Module;
import com.cn.lingrui.common.pojos.roleManage.RmRolePojoIn;
import com.cn.lingrui.common.pojos.roleManage.RmRolePojoOut;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.RoleManageService;
import com.cn.lingrui.common.utils.AuthorityUtil;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("roleManageService")
public class RoleManageServiceImpl extends BServiceLogic implements RoleManageService{
	
	@Resource(name="roleManageDao")
	private RoleManageDao roleManageDao;
	
	@Resource(name="pageManageDao")
	private PageManageDao pageManageDao;
	
	@Override
	protected String getFunNum() {
		
		return "01040101";
	}

	@Override
	public ModelAndView getHomePage() throws Exception {

		this.before();
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("01/" + this.getFunNum());
		
		// 获取所有的角色信息
		List<NBPT_RSFZ_ROLE> roleList;
		try {
			roleList = roleManageDao.getRoleList(this.getConnection());

			mv.addObject("roles", roleList);
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView getAddRole(RmRolePojoIn in) throws Exception {

		this.before();
		
		// 获取页面,首先要获取所有的模块功能及页面
		List<Module> moduleList;
		
		ModelAndView mv = HttpUtil.getModelAndView("01/01040201");
		try {
			moduleList = pageManageDao.getClassifiedModuleList(this.getConnection());
			mv.addObject("modules", moduleList);
			
			return this.after(mv, "admin");
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView postAddRole(RmRolePojoIn in) throws Exception {

		this.before();
		
		// 初始化返回结果
		RmRolePojoOut out = new RmRolePojoOut();
		
		ModelAndView mv = null;
		// 1.验证重复性
		List<NBPT_RSFZ_ROLE> rsfzrole;
		try {
			rsfzrole = roleManageDao.queryForClaszs("SELECT * FROM NBPT_RSFZ_ROLE WHERE NBPT_RSFZ_ROLE_NAME = '" + 
															in.getNBPT_RSFZ_ROLE_NAME() + "'", this.getConnection(), NBPT_RSFZ_ROLE.class);
			
			if(0 == rsfzrole.size()) {
				
				// 如果查不到数据,什么都不做
			} else {

				out.getMessages().add("该角色已存在,请修改角色名称");
				// 初始化返回结果 TODO
				mv = HttpUtil.getModelAndView("01/01040201");
				mv.addObject("messages", out.getMessages());
				return this.after(mv, "admin");
			}
			
			// 2.查询角色最大ID
			String maxId = roleManageDao.receiveMaxId(this.getConnection(), "NBPT_RSFZ_ROLE");
			
			
			// 添加角色信息
			NBPT_RSFZ_ROLE role = new NBPT_RSFZ_ROLE();
			
			role.setNBPT_RSFZ_ROLE_ID(maxId);
			role.setNBPT_RSFZ_ROLE_NAME(in.getNBPT_RSFZ_ROLE_NAME());
			role.setNBPT_RSFZ_ROLE_BZ(in.getNBPT_RSFZ_ROLE_BZ());
			role.setNBPT_RSFZ_ROLE_LEVEL(in.getRSFZROLE_LEVEL());
			roleManageDao.postAddRole(this.getConnection(), role);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());

			// 初始化返回结果
			mv = HttpUtil.getModelAndView("redirect:/sys/rmUpdateRole?roleId=" + maxId);
			
		} catch (SQLException e) {

			this.closeException();
			throw new Exception();
		}

		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView getUpdateRole(RmRolePojoIn in) throws Exception {

		this.before();
		
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("01/01040301");
		RmRolePojoOut out = new RmRolePojoOut();
		
		NBPT_RSFZ_ROLE role = new NBPT_RSFZ_ROLE();
		
		// 如果没有传入参数,则是重定向过来的
		if(null == in.getNBPT_RSFZ_ROLE_ID() || "".equals(in.getNBPT_RSFZ_ROLE_ID())) {
			
			role.setNBPT_RSFZ_ROLE_ID(getRequest().getParameter("roleId"));
		} else {
			
			role.setNBPT_RSFZ_ROLE_ID(in.getNBPT_RSFZ_ROLE_ID());
		}
		
		// 获取要修改角色信息
		NBPT_RSFZ_ROLE rsfzrole;
		try {
			rsfzrole = roleManageDao.getRole(this.getConnection(), role);
			
			// 获取要修改角色的权限
			List<NBPT_RSFZ_R_P> rsfz_R_Ps = roleManageDao.getRole_Page(this.getConnection(), role);
			
			// 1.获取所有权限信息
			List<NBPT_RSFZ_PAGE> pageList = pageManageDao.getPageList(this.getConnection());
			
			// 2.分类权限信息
			for(NBPT_RSFZ_PAGE rsfzpage : pageList) {
				
				if("0".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {
					
					// 如果是模块
					out.getModuleList().add(rsfzpage);
				} else if ("1".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {

					// 如果是功能
					out.getFuncList().add(rsfzpage);
				} else if ("2".equals(rsfzpage.getNBPT_RSFZ_PAGE_TYPE())) {

					// 如果是页面
					out.getPageList().add(rsfzpage);
				}
			}
			List<String> modules = new ArrayList<String>();
			List<String> funcs = new ArrayList<String>();
			List<String> pages = new ArrayList<String>();
			
			// 权限信息跟角色权限对比
			for(NBPT_RSFZ_R_P rsfz_R_P : rsfz_R_Ps) {

				switch (rsfz_R_P.getNBPT_RSFZ_R_P_PTYPE()) {

					case "0":
						modules.add(rsfz_R_P.getNBPT_RSFZ_R_P_PID());
						break;
					case "1":
						funcs.add(rsfz_R_P.getNBPT_RSFZ_R_P_PID());
						break;
					case "2":
						pages.add(rsfz_R_P.getNBPT_RSFZ_R_P_PID());
						break;
				}
			}
			
			out.setRsfzrole(rsfzrole);
			// 对比后放入返回结果中
			out.setModules(modules.toString());
			out.setFuncs(funcs.toString());
			out.setPages(pages.toString());
			mv.addObject("pageContent", out);
		} catch (SQLException e) {

			this.closeException();
			throw new Exception();
		}
		
		return this.after(mv, "admin");
	}
	
	@Override
	public ModelAndView postUpdateRole(RmRolePojoIn in) throws Exception {

		this.before();
		
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/rmHome");
		
		// 更新角色信息
		NBPT_RSFZ_ROLE role = new NBPT_RSFZ_ROLE();
		
		role.setNBPT_RSFZ_ROLE_ID(in.getNBPT_RSFZ_ROLE_ID());
		role.setNBPT_RSFZ_ROLE_NAME(in.getNBPT_RSFZ_ROLE_NAME());
		role.setNBPT_RSFZ_ROLE_BZ(in.getNBPT_RSFZ_ROLE_BZ());
		role.setNBPT_RSFZ_ROLE_LEVEL(in.getRSFZROLE_LEVEL());
		try {
			
			roleManageDao.postUpdateRole(this.getConnection(), role);
			roleManageDao.postUpdateR_P(this.getConnection(), in);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
			
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postDeleteRole(RmRolePojoIn in) throws Exception {

		this.before();
		
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/rmHome");
		
		// 添加角色信息
		NBPT_RSFZ_ROLE role = new NBPT_RSFZ_ROLE();
		
		role.setNBPT_RSFZ_ROLE_ID(in.getNBPT_RSFZ_ROLE_ID());
		try {
			roleManageDao.postDeleteRole(this.getConnection(), role);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
			
		} catch (SQLException e) {

			this.closeException();
			throw new Exception();
		}

		return this.after(mv, "admin");
	}



}





























