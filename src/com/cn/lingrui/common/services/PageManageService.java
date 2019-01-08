package com.cn.lingrui.common.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.pageManage.PmFuncPojoIn;
import com.cn.lingrui.common.pojos.pageManage.PmModulePojoIn;
import com.cn.lingrui.common.pojos.pageManage.PmPagePojoIn;

public interface PageManageService {
	/**
	 * 获取管理首页
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getHomePage() throws Exception;
	
	/**
	 * 获取模块管理页面
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getPmModule() throws Exception;
	
	/**
	 * 添加模块业务
	 * @param in
	 * @return
	 */
	public ModelAndView postAddModule(PmModulePojoIn in);

	/**
	 * 修改模块业务
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postUpdateModule(PmModulePojoIn in) throws Exception;
	
	/**
	 * 删除模块业务
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postDeleteModule(PmModulePojoIn in) throws Exception;
	
	/**
	 * 获取功能管理页面
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getPmFunc() throws Exception;
	
	/**
	 * 添加功能业务
	 * @param in
	 * @return
	 */
	public ModelAndView postAddFunc(PmFuncPojoIn in) throws Exception;

	/**
	 * 修改功能业务
	 * @param in
	 * @return
	 */
	public ModelAndView postUpdateFunc(PmFuncPojoIn in) throws Exception;
	/**
	 * 删除功能业务
	 * @param in
	 * @return
	 */
	public ModelAndView postDeleteFunc(PmFuncPojoIn in) throws Exception;
	
	/**
	 * 获取页面管理页面
	 * @return
	 */
	public ModelAndView getPmPage() throws Exception;
	
	/**
	 * 添加页面业务
	 * @param in
	 * @return
	 */
	public ModelAndView postAddPage(PmPagePojoIn in) throws Exception;

	/**
	 * 修改页面业务
	 * @param in
	 * @return
	 */
	public ModelAndView postUpdatePage(PmPagePojoIn in) throws Exception;
	/**
	 * 删除页面业务
	 * @param in
	 * @return
	 */
	public ModelAndView postDeletePage(PmPagePojoIn in) throws Exception;
	/**
	 * 获取页面管理树形菜单
	 * @return
	 */
	public String receivePmTree();
}
