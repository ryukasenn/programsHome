package com.cn.lingrui.pm.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.pm.db.dao.GrpmDao;
import com.cn.lingrui.pm.pojos.PmInfo;
import com.cn.lingrui.pm.service.GrpmService;
import com.cn.lingrui.pm.service.PmBaseService;


@Service("grpmService")
public class GrpmServiceImpl extends PmBaseService implements GrpmService{
	
	private static Logger log = LogManager.getLogger();
	
	
	@Resource(name="grpmDao")
	private GrpmDao grpmDao;
	
	@Override
	public ModelAndView getGrpmInfoView() throws Exception {
		this.before();
		
		// 初始化返回
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050202"));

		// 1.生成同一段位排名
		List<PmInfo> sameDwPm = null;
		// 2.个人排名信息
		PmInfo grPm = null;	
		// 获取当前登录人员信息
		String ryxm =this.getUserName();
		
		//本人历史战绩
		List<PmInfo> allResult = null;
		
		try {
			grPm = grpmDao.getcurrentDwpm(ryxm, this.getConnection());		
			sameDwPm = grpmDao.getSameDwPm(grPm.getDW(), this.getConnection());
			
			allResult = grpmDao.getAllResult(ryxm, this.getConnection());
			
			List<String> rqList = new ArrayList<>();
			List<String> lrbList = new ArrayList<>();
			List<String> dwList = new ArrayList<>();
			
			for(int i =0;i<allResult.size();i++) {
				rqList.add(allResult.get(i).getRQ());
				lrbList.add(allResult.get(i).getLRB());
				dwList.add(allResult.get(i).getDW());
			}			
			
			mv.addObject("grPm", grPm);
			mv.addObject("sameDwPm", sameDwPm);
			
			mv.addObject("rqStr",rqList.toString());
			mv.addObject("lrbStr",lrbList.toString());
			mv.addObject("dwStr",dwList.toString());
			
			
			System.out.println(dwList.toString());
			
			
			return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("个人排名查询出错");			
			throw new Exception();
		}
	}

	@Override
	protected String getFunNum() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
