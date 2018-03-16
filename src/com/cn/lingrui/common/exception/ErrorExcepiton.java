package com.cn.lingrui.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;

public class ErrorExcepiton implements HandlerExceptionResolver {

	private static Logger log = LogManager.getLogger();
	
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object arg2,
			Exception arg3) {

		arg3.printStackTrace();
		return HttpUtil.getModelAndView("common/404");
	}

	
}
