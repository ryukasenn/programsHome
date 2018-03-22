package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

public interface CheckManageService {

	public ModelAndView receiveUnchecks() throws Exception;

	public ModelAndView receiveUncheck(String uncheckpid) throws Exception;

}
