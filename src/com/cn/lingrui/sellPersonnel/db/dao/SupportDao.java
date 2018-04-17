package com.cn.lingrui.sellPersonnel.db.dao;

import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public interface SupportDao extends SellPersonnelBaseDao{

	public List<NBPT_VIEW_CURRENTPERSON> receiveAllTerminals();
}
