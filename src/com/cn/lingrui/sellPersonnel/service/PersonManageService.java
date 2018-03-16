package com.cn.lingrui.sellPersonnel.service;

import java.sql.SQLException;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;

public interface PersonManageService {

	public ModelAndView receiveCurrentTerminals_support() throws Exception ;

	public ModelAndView getAddTerminal() throws Exception;

	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception;

	public String receiveSelect(String parentId) throws SQLException;

	public ModelAndView getSupportAdd() throws Exception;

	public String receiveAreasSelect(String areaId) throws Exception;

}
