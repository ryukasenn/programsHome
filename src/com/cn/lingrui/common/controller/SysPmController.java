package com.cn.lingrui.common.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.pageManage.PmFuncPojoIn;
import com.cn.lingrui.common.pojos.pageManage.PmModulePojoIn;
import com.cn.lingrui.common.pojos.pageManage.PmPagePojoIn;
import com.cn.lingrui.common.services.PageManageService;

/**
 * 系统管理员专用通道,页面管理
 * 
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sys")
public class SysPmController {

	@Resource(name = "pageManageService")
	private PageManageService pageManage;

	@RequestMapping(value = "/exout", method = RequestMethod.POST)
	public ModelAndView exout() {

		return null;
	}

	/**
	 * 页面管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pmHome", method = RequestMethod.GET)
	public ModelAndView getManage() throws Exception {

		ModelAndView mv = pageManage.getHomePage();
		return mv;
	}

	/**
	 * 模块管理
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pmModule", method = RequestMethod.GET)
	public ModelAndView getModuleManage() throws Exception {

		ModelAndView mv = pageManage.getPmModule();
		return mv;
	}

	/**
	 * 添加模块
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addModule", method = RequestMethod.POST)
	public ModelAndView postAddModule(PmModulePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postAddModule(in);
		return mv;
	}

	/**
	 * 修改模块
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateModule", method = RequestMethod.POST)
	public ModelAndView postUpdateModule(PmModulePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postUpdateModule(in);
		return mv;
	}

	/**
	 * 删除模块
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteModule", method = RequestMethod.POST)
	public ModelAndView postDeleteModule(PmModulePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postDeleteModule(in);
		return mv;
	}

	/**
	 * 功能管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pmFunc", method = RequestMethod.GET)
	public ModelAndView getFuncManage() throws Exception {

		ModelAndView mv = pageManage.getPmFunc();
		return mv;
	}

	/**
	 * 添加功能
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addFunc", method = RequestMethod.POST)
	public ModelAndView postAddFunc(PmFuncPojoIn in) throws Exception {

		ModelAndView mv = pageManage.postAddFunc(in);
		return mv;
	}

	/**
	 * 修改功能
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateFunc", method = RequestMethod.POST)
	public ModelAndView postUpdateFunc(PmFuncPojoIn in) throws Exception {

		ModelAndView mv = pageManage.postUpdateFunc(in);
		return mv;
	}

	/**
	 * 删除功能
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteFunc", method = RequestMethod.POST)
	public ModelAndView postDeleteFunc(PmFuncPojoIn in) throws Exception {

		ModelAndView mv = pageManage.postDeleteFunc(in);
		return mv;
	}

	/**
	 * 页面管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pmPage", method = RequestMethod.GET)
	public ModelAndView getPageManage() throws Exception {

		ModelAndView mv = pageManage.getPmPage();
		return mv;
	}

	/**
	 * 添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.POST)
	public ModelAndView postAddPage(PmPagePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postAddPage(in);
		return mv;
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updatePage", method = RequestMethod.POST)
	public ModelAndView postUpdatePage(PmPagePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postUpdatePage(in);
		return mv;
	}

	/**
	 * 删除页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deletePage", method = RequestMethod.POST)
	public ModelAndView postDeletePage(PmPagePojoIn in) throws Exception {

		ModelAndView mv = pageManage.postDeletePage(in);
		return mv;
	}
	
	/**
	 * 获取页面管理树形菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/receivePmTree", method = RequestMethod.POST)
	public String receivePmTree() {
		
		String result = pageManage.receivePmTree();
		return result;
	}
}
