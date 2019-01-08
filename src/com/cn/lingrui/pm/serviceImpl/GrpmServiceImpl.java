package com.cn.lingrui.pm.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.pm.db.dao.DwxxDao;
import com.cn.lingrui.pm.db.dao.GrpmDao;
import com.cn.lingrui.pm.pojos.DwInfo;
import com.cn.lingrui.pm.pojos.PmInfo;
import com.cn.lingrui.pm.service.GrpmService;
import com.cn.lingrui.pm.service.PmBaseService;


@Service("grpmService")
public class GrpmServiceImpl extends PmBaseService implements GrpmService{
	
	private static Logger log = LogManager.getLogger();
	
	
	@Resource(name="grpmDao")
	private GrpmDao grpmDao;
	
	@Resource(name="dwxxDao")
	private DwxxDao dwxxDao;
	
	@Override
	public ModelAndView getSameDqPmView(String pmrq) throws Exception {
		this.before();
		//初始化返回视图
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050502"));
		
		// 1.生成同一段位排名
		List<PmInfo> sameDqmcPm = null;
		
		// 获取当前登录人员信息
		String ryxm =this.getUserName();
		String dqmcStr = "";
		
		try {
			dqmcStr = grpmDao.getdqmcStr(ryxm, getConnection());
			sameDqmcPm = grpmDao.getSameDqPm(dqmcStr, pmrq, getConnection());
			
			mv.addObject("sameDqPmList",sameDqmcPm);
			return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("同大区排名查询出错");			
			throw new Exception();
		}
		
		
	}
	
	@Override
	public ModelAndView getGrpmInfoView() throws Exception {
		this.before();
		
		// 初始化返回
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050202"));

		// 1.生成同一段位排名
		List<PmInfo> sameDwPm = null;
		// 2.个人排名信息
		PmInfo grPm = null;	
		
		DwInfo dwInfo =null;
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
				dwList.add(dwxxDao.getDwInfo(allResult.get(i).getDW(), this.getConnection()).getWCBH());
			}			
			
			dwInfo =dwxxDao.getDwInfo(grPm.getDW(), this.getConnection());
			mv.addObject("grPm", grPm);
			mv.addObject("sameDwPm", sameDwPm);
			
			mv.addObject("dwxx", dwInfo);
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

	@Override
	public ModelAndView getslbView(String pmrq,String mc) throws Exception {
		this.before();
		
		String pmmc ="";			
		if("".equals(mc)){
			pmmc="10";
		}else
			pmmc=mc;
		
		//初始化返回视图
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050602"));
		
		// 生成前10排名
		List<PmInfo> beforetenPmList = null;
		// 生成后10排名
		List<PmInfo> lasttenPmList = null;
		
		
		try {
			beforetenPmList =grpmDao.getbeforetenPmList(pmrq,mc, getConnection());
			lasttenPmList = grpmDao.getlasttenPmList(pmrq,mc, getConnection());
			mv.addObject("pmmc",mc);
			mv.addObject("beforetenPmList",beforetenPmList);
			mv.addObject("lasttenPmList",lasttenPmList);
			return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("实力榜排名查询出错");			
			throw new Exception();
		}
	}

	@Override
	public ModelAndView getgrxxView(String rq, String xm,String dw) throws Exception {
		this.before();
		//初始化返回视图
		
		ModelAndView mv = HttpUtil.getModelAndView("05/grxx");
		// 排名列表
		PmInfo grPm = null;
		// 排名列表
		List<PmInfo> allResult = null;
		//同段位
		List<PmInfo> sameDwPm = null;
		//段位信息
		DwInfo dwInfo =null;
		
		try {
				sameDwPm =grpmDao.getSameDwPm(rq, xm, dw, getConnection());			
				grPm = grpmDao.getcurrentDwpm(xm, rq, getConnection());			
				allResult = grpmDao.getAllResult(xm, this.getConnection());
				dwInfo =dwxxDao.getDwInfo(dw, this.getConnection());
				List<String> rqList = new ArrayList<>();
				List<String> lrbList = new ArrayList<>();
				List<String> dwList = new ArrayList<>();
				
				for(int i =0;i<allResult.size();i++) {
					rqList.add(allResult.get(i).getRQ());
					lrbList.add(allResult.get(i).getLRB());
					dwList.add(dwxxDao.getDwInfo(allResult.get(i).getDW(), this.getConnection()).getWCBH());
				}
				
				mv.addObject("dwxx", dwInfo);
				mv.addObject("grPm", grPm);
				mv.addObject("sameDwPm",sameDwPm);
				mv.addObject("rqStr",rqList.toString());
				mv.addObject("lrbStr",lrbList.toString());
				mv.addObject("dwStr",dwList.toString());
				

				return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("跳转时查询个人信息出错");			
			throw new Exception();
		}
	}


	

}
