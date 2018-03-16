package com.cn.lingrui.common.services.servicesImpl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dao.AuthorityDao;
import com.cn.lingrui.common.db.dao.PageManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;
import com.cn.lingrui.common.pojos.pageManage.PmModulePojoIn;
import com.cn.lingrui.common.pojos.pageManage.PmPagePojoIn;
import com.cn.lingrui.common.pojos.pageManage.Module;
import com.cn.lingrui.common.pojos.pageManage.PageManagePojoOut;
import com.cn.lingrui.common.pojos.pageManage.PmFuncPojoIn;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.PageManageService;
import com.cn.lingrui.common.utils.AuthorityUtil;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("pageManageService")
public class PageManageServiceImpl extends BServiceLogic implements PageManageService{

	@Resource(name = "pageManageDao")
	private PageManageDao pageManageDao;

	@Resource(name = "authorityDao")
	private AuthorityDao authorityDao;

	/**
	 * 跳转页面管理首页
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getHomePage() throws Exception {
		
		// 有后台数据处理,需要初始化
		this.before();
		PageManagePojoOut out = new PageManagePojoOut();
		ModelAndView mv = HttpUtil.getModelAndView("01/" + this.getFunNum());

		// 1.查询所有的模块功能及页面
		List<Module> moduleList;
		try {
			moduleList = pageManageDao.getClassifiedModuleList(this.getConnection());
			// 2.将分类好的数据放入放回结果中
			mv.addObject("modules", moduleList);
			mv.addObject("messages", out.getMessages());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		
		
		return this.after(mv, "admin");
	}

	@Override
	protected String getFunNum() {
		return "01060101";
	}

	

	/**
	 * 跳转到模块管理页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getPmModule() throws Exception {
		
		// 使用数据库,执行初始化
		this.before();
		
		// 1.查询所有模块
		List<NBPT_RSFZ_PAGE> moduleList;
		ModelAndView mv = HttpUtil.getModelAndView("01/01060201");
		try {
			moduleList = pageManageDao.getModuleList(getConnection());
			
			mv.addObject("modules", moduleList);

			return this.after(mv, "admin");
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
	}
	
	/**
	 * 添加模块业务
	 */
	@Override
	public ModelAndView postAddModule(PmModulePojoIn in) {
		
		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getModuleId());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getModuleBz());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getModulePid());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getModuleType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getModuleName());

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmModule");
		
		// 保存数据
		try {
			pageManageDao.addModule(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postUpdateModule(PmModulePojoIn in) throws Exception {

		this.before();

		// 初始化修改数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getModuleId());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getModulePid());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getModuleBz());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getModuleType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getModuleName());

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmModule");

		// 更新数据
		try {
			pageManageDao.updateModule(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}
	
	/**
	 * 删除模块业务
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postDeleteModule(PmModulePojoIn in) throws Exception {

		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getModuleId());

		// 删除该条数据		
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmModule");
		try {
			pageManageDao.deleteModule(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}

	/**
	 * 跳转到功能管理页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getPmFunc() throws Exception {

		// 使用数据库,执行初始化
		this.before();
		
		// 1.查询所有功能
		List<NBPT_RSFZ_PAGE> funcList;
		try {
			funcList = pageManageDao.getFuncList(getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}

		ModelAndView mv = HttpUtil.getModelAndView("01/01060301");
		
		mv.addObject("funcs", funcList);
		
		return this.after(mv, "admin");
	}
	
	/**
	 * 添加功能业务
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postAddFunc(PmFuncPojoIn in) throws Exception {
		
		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getFuncId());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getFuncBz());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getFuncPid());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getFuncType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getFuncName());

		// 保存数据		
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmFunc");
		try {
			pageManageDao.addFunc(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postUpdateFunc(PmFuncPojoIn in) throws Exception {

		this.before();

		// 初始化修改数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getFuncId());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getFuncBz());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getFuncPid());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getFuncType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getFuncName());

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmFunc");

		// 更新数据
		try {
			pageManageDao.updateFunc(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
			
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}
	
	

	/**
	 * 删除功能业务
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postDeleteFunc(PmFuncPojoIn in) throws Exception {

		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getFuncId());

		// 删除该条数据		
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmFunc");
		try {
			pageManageDao.deleteModule(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView getPmPage() throws Exception {
		// 使用数据库,执行初始化
		this.before();
		
		// 1.查询所有功能
		List<NBPT_RSFZ_PAGE> pageList;

		ModelAndView mv = HttpUtil.getModelAndView("01/01060401");
		try {
			pageList = pageManageDao.getPmPageList(getConnection());
			
			mv.addObject("funcs", pageList);
			
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		
		
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postAddPage(PmPagePojoIn in) throws Exception {
		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getPageId());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getPageBz());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getPagePid());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getPageType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getPageName());

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmPage");
		
		// 保存数据
		try {
			pageManageDao.addPage(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
			
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postUpdatePage(PmPagePojoIn in) throws Exception {
		this.before();

		// 初始化修改数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getPageId());
		rsfzpage.setNBPT_RSFZ_PAGE_BZ(in.getPageBz());
		rsfzpage.setNBPT_RSFZ_PAGE_PID(in.getPagePid());
		rsfzpage.setNBPT_RSFZ_PAGE_TYPE(in.getPageType());
		rsfzpage.setNBPT_RSFZ_PAGE_NAME(in.getPageName());

		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmPage");

		// 更新数据
		try {
			pageManageDao.updatePage(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postDeletePage(PmPagePojoIn in) throws Exception {

		// 需要连接数据库,初始化数据库连接
		this.before();
		
		// 初始化保存数据
		NBPT_RSFZ_PAGE rsfzpage = new NBPT_RSFZ_PAGE();
		
		rsfzpage.setNBPT_RSFZ_PAGE_ID(in.getPageId());

		// 删除该条数据		
		ModelAndView mv = HttpUtil.getModelAndView("redirect:/sys/pmPage");
		try {
			pageManageDao.deletePage(getConnection(), rsfzpage);
			AuthorityUtil.updateAuthority(authorityDao, this.getConnection());
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}
		return this.after(mv, "admin");
	}





}
