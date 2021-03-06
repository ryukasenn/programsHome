package com.cn.lingrui.sellPersonnel.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.utils.DownloadUtil;
import com.cn.lingrui.sellPersonnel.pojos.person.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.support.TransferPojoIn;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;

@Controller
@RequestMapping("/sellPersonnel/support")
public class SupportController {


	@Resource(name = "supportSerivce")
	private SupportSerivce supportSerivce;
	
	/**
	 * 获取调岗页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transfer", method = RequestMethod.GET)
	public ModelAndView getTransfer() throws Exception {

		ModelAndView mv = supportSerivce.getTransfer();

		return mv;
	}
	
	/**
	 * 提交调岗请求
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public ModelAndView postTransfer(TransferPojoIn in) throws Exception {

		ModelAndView mv = supportSerivce.postTransfer(in);

		return mv;
	}
	
	/**
	 * 生成考核列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createEvaluationForm", method = RequestMethod.GET)
	public ModelAndView createEvaluationForm(String endTime) throws Exception {

		ModelAndView mv = supportSerivce.createEvaluationForm(endTime);

		return mv;
	}
	
	/**
	 * 生成考核列表权限验证
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/roleCheck", method = RequestMethod.GET)
	public String getRoleCheck() throws Exception {

		String mv = supportSerivce.getRoleCheck();

		return mv;
	}
	
	/**
	 * 后勤添加人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/supportAdd", method = RequestMethod.GET)
	public ModelAndView getSupportAdd() throws Exception {

		ModelAndView mv = supportSerivce.getSupportAdd();

		return mv;
	}
	
	/**
	 * 后勤添加人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/supportAdd", method = RequestMethod.POST)
	public ModelAndView postSupportAdd(AddPersonPojoIn in) throws Exception {

		ModelAndView mv = supportSerivce.postSupportAdd(in);

		return mv;
	}
	
	/**
	 * 生成考核列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createAttendance", method = RequestMethod.GET)
	public ModelAndView createAttendance(String type, HttpServletResponse response, HttpServletRequest req) throws Exception {

		if("download".equals(type)) {
			
			supportSerivce.createAttendanceFile("考勤");
			DownloadUtil.writeDownloadFile("考勤.xls", response, req.getServletContext().getMimeType("考勤.xls"));
			return null;
		} else {

			ModelAndView mv = supportSerivce.createAttendance();

			return mv;
		}
	}
	
	/**
	 * 删除指定终端人员
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteTerminal", method = RequestMethod.GET)
	public ModelAndView deleteTerminal(String terminalPid, String areaUid, String regionUid) throws Exception {

		ModelAndView mv = supportSerivce.deleteTerminal(terminalPid, areaUid, regionUid);

		return mv;
	}
	
	/**
	 * 获取未审核人员列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUnchecks", method = RequestMethod.GET)
	public ModelAndView receiveUnchecks() throws Exception {

		ModelAndView mv = supportSerivce.receiveUnchecks();

		return mv;
	}
	
	/**
	 * 获取未审核人员详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUncheck", method = RequestMethod.POST)
	public ModelAndView receiveUncheck(String terminalPid, String type) throws Exception {

		ModelAndView mv = supportSerivce.receiveUncheck(terminalPid, type);

		return mv;
	}
	
	/**
	 * 通过审核
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/agreeUncheck", method = RequestMethod.POST)
	public ModelAndView agreeUncheck(String terminalPid, String type) throws Exception {

		ModelAndView mv = supportSerivce.agreeUncheck(terminalPid, type);

		return mv;
	}
	
	/**
	 * 驳回
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/rejectUncheck", method = RequestMethod.POST)
	public ModelAndView rejectUncheck(String terminalPid) throws Exception {

		ModelAndView mv = supportSerivce.rejectUncheck(terminalPid);

		return mv;
	}
	
	/**
	 * 后勤直接修改
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeTerminal", method = RequestMethod.POST)
	public ModelAndView changeTerminal(AddPersonPojoIn terminal) throws Exception {

		ModelAndView mv = supportSerivce.changeTerminal(terminal);

		return mv;
	}

	/**
	 * 获取所有的人员统计信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/receivePersonStatisticsTable", method = RequestMethod.POST)
	public String receivePersonStatisticsTable(String type, String id, String typeValue) {
		
		String back = supportSerivce.receivePerson(type, id, typeValue);
		return back;
	}
	
	/**
	 * 获取所有的人员详细信息
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/receivePersonTable", method = RequestMethod.POST)
	public String receivePersonTable(String type, String id, String typeValue) {
		
		String back = supportSerivce.receivePersonTable(type, id, typeValue);
		return back;
	}
}
