package com.cn.lingrui.sellPersonnel.serviceImpl;


import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.pojos.support.EvaluationForm;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;


/**
 * 后勤权限逻辑
 * @author lhc
 *
 */
@Service("supportSerivce")
public class SupportServiceImpl extends SellPBaseService implements SupportSerivce{
	
	private static Logger log = LogManager.getLogger();

	@Resource(name = "supportDao")
	private SupportDao supportDao;

	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
		return null;
	}

	
	
	/**
	 * 获取后勤添加人员页面
	 */
	@Override
	public ModelAndView getSupportAdd() throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
	
			// 1.生成保单类型下拉框
			mv.addObject("policytypeSelects", supportDao.receiveDictionarys("POLICYTYPE", this.getConnection()));
	
			return this.after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}



	@Override
	public ModelAndView createEvaluationForm(String endTime) throws Exception {

		try {
			this.before();
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030702"));
			
			EvaluationForm evaluationForm = supportDao.receiveEvalutionForm(CommonUtil.formateTiemToBasic(endTime), this.getConnection());
			
			mv.addObject("evaluationForm", evaluationForm);
			mv.addObject("formTime", endTime);
			return after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
}
