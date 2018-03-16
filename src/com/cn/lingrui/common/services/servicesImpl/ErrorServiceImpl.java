package com.cn.lingrui.common.services.servicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.ErrorService;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("errorService")
public class ErrorServiceImpl extends BServiceLogic implements ErrorService {

	@Override
	public ModelAndView receiveError() {

		ModelAndView mv = HttpUtil.getModelAndView("common/" + this.getFunNum());
		return this.after(mv);
	}

	@Override
	protected String getFunNum() {
		
		return "404";
	}


}
