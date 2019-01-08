package com.cn.lingrui.hx.services.serviceImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.cn.lingrui.hx.db.dao.ZdhxDao;
import com.cn.lingrui.hx.db.dbpojos.LSWLZD;
import com.cn.lingrui.hx.db.dbpojos.NOHX_XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.NOHX_XSHK;
import com.cn.lingrui.hx.db.dbpojos.XSDD;
import com.cn.lingrui.hx.db.dbpojos.XSDDHX;
import com.cn.lingrui.hx.db.dbpojos.XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.XSHK;
import com.cn.lingrui.hx.db.dbpojos.ZWWLDW;
import com.cn.lingrui.hx.db.dbpojos.hx.XSDD_XSDDMXS;
import com.cn.lingrui.hx.services.HxBaseService;
import com.cn.lingrui.hx.services.ZdhxService;
import com.cn.lingrui.hx.utils.HxCommonUtil;
import com.cn.lingrui.hx.utils.HxDbUtil;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;

import net.sf.json.JSONArray;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.hx.pojos.zdhx.HxProvincePojoIn;
import com.cn.lingrui.hx.pojos.zdhx.Shdkh_detail;
import com.cn.lingrui.hx.pojos.zdhx.Xsdd_detail;
import com.cn.lingrui.hx.pojos.zdhx.HxYwyPojoIn;
import com.cn.lingrui.hx.pojos.zdhx.Ywy_detail;
import com.cn.lingrui.hx.pojos.zdhx.Zdhx_global;

@Service("zdhxService")
public class ZdhxServiceImpl extends HxBaseService implements ZdhxService {

	@Resource(name="zdhxDao")
	private ZdhxDao zdhxDao;

	private static Logger log = LogManager.getLogger();

	@Override
	public String hxByProvinceSelect(HxProvincePojoIn in) throws Exception {

		try {
			this.before();
			
			// 初始化全局参数
			Zdhx_global global = new Zdhx_global();
			
			// 1.获取并按业务员和客户分类要核销的XSDD及XSDDMX
			this.provinceXsddGet(in, global);
			
			// 2.获取可核销回款
			this.xshkGet(in.getReturnEnd(), global);
			
			// 3.处理数据
			this.dealWithGlobalUpdateModel(global);
			
			// 4.执行插入和更新
			this.excuteUpdateAndInsert(global);
			
			// 5.初始化返回结果
			JsonDataBack back = new JsonDataBack();
			
			back.setStateOk("本次核销结束");
			return this.after(back.toJsonString());
		} catch (Exception e) {

			log.error("按省份自动核销出错");
			JsonDataBack back = new JsonDataBack();
			back.setStateError("后台未知错误,请联系系统管理员");
			return this.after(back.toJsonString());
		} 
	}
	
	@Override
	public String hxByYwySelect(HxYwyPojoIn in) {
		
		try {
			this.before();
			
			// 初始化全局参数
			Zdhx_global global = new Zdhx_global();
			
			// 1.获取并按业务员和客户分类要核销的XSDD及XSDDMX
			this.ywyXsddGet(in, global);
			
			// 2.获取可核销回款
			this.xshkGet(in.getReturnEnd(), global);
			
			// 3.处理数据
			this.dealWithGlobalUpdateModel(global);
			
			// 4.执行插入和更新
			this.excuteUpdateAndInsert(global);
			
			// 5.初始化返回结果
			JsonDataBack back = new JsonDataBack();
			
			back.setStateOk("本次核销结束");
			
			return this.after(back.toJsonString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			log.error("按业务员自动核销出错");
			JsonDataBack back = new JsonDataBack();
			back.setStateError("后台未知错误,请联系系统管理员");
			return this.after(back.toJsonString());
		} 
	}
	/**
	 * 执行更新
	 * @param global
	 * @throws SQLException
	 */
	private void excuteUpdateAndInsert(Zdhx_global global) throws SQLException {

		// 执行xsddhx 的插入
		zdhxDao.excuteInsertXsddhx(global.getResultSql().getInsertXsddhxSqls(), this.getConnection());
		
		// 执行xsddmx 的更新
		zdhxDao.excuteUpdateXsddmx(global.getResultSql().getUpdateXsddmxSqls(), this.getConnection());
		
		// 执行xsdd 的更新
		zdhxDao.excuteUpdateXsdd(global.getResultSql().getUpdateXsddSqls(), this.getConnection());

		// 执行xshk 的更新
		zdhxDao.excuteUpdateXshk(global.getResultSql().getUpdateXshkSqls(), this.getConnection());

		for(String s : global.getResultSql().getInsertXsddhxSqls()) {
			log.info("核销语句" + s);
		}
		for(String s : global.getResultSql().getUpdateXsddmxSqls()) {
			log.info("订单明细更新语句" + s);
		}
		for(String s : global.getResultSql().getUpdateXsddSqls()) {
			log.info("订单更新语句" + s);
		}
		for(String s : global.getResultSql().getUpdateXshkSqls()) {
			log.info("回款更新语句" + s);
		}
	}

	/**
	 * 获取销售回款记录
	 * @param in
	 * @param global
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 */
	private void xshkGet(String returnEnd, Zdhx_global global) throws SQLException, NoSuchFieldException {

		// 1.获取所有回款记录
		List<XSHK> xshks = zdhxDao.receiveXshks_valid(returnEnd, this.getConnection());
		
		// 2.按业务员分类销售回款
		Map<String, List<XSHK>> xshkYwyClassify = CommonUtil.classify(xshks, "XSHK_RYBH", XSHK.class);
		
		for(Ywy_detail ywyDetail : global.getYwy_details()) {
			
			Map<String, List<XSHK>> xshkShdkhClassify = CommonUtil.classify(CommonUtil.getListInMapByKey(xshkYwyClassify, ywyDetail.getRybh()), "XSHK_SHDKH", XSHK.class); 
			
			for(Shdkh_detail shdkhDetail : ywyDetail.getShdkh_details()) {
				
				shdkhDetail.setXshks(CommonUtil.getListInMapByKey(xshkShdkhClassify, shdkhDetail.getShdkh()));
			}
		}
	}

	private void dealWithGlobalUpdateModel(Zdhx_global global) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {

		// 1.
		
		// 2.遍历global,生成更新,插入语句所需参数
		for(Ywy_detail currentYwyDetail : global.getYwy_details()) {
			
			this.dealWithOneYwy(currentYwyDetail, global);
		}
		
		// 3.生成insertXsddhx sql语句
		global.getResultSql().getInsertXsddhxSqls().addAll(HxDbUtil.BeanToSql(XSDDHX.class, "INSERT", global.getNeedInsertXsddhx()));
		
		// 4.生成updateXsddmx sql语句
		for (XSDDMX xsddmx : global.getNeedUpdateXsddmx()) {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE XSDDMX ");
			sql.append("SET XSDDMX_HKHX = '"+ xsddmx.getXSDDMX_HKHX() +"', ");
			sql.append("XSDDMX_YHXJE = '" + xsddmx.getXSDDMX_YHXJE() + "', ");
			sql.append("XSDDMX_HXSJHK = '" + xsddmx.getXSDDMX_HXSJHK() + "' ");
			sql.append("WHERE XSDDMX_DDLS = '" + xsddmx.getXSDDMX_DDLS() + "' AND XSDDMX_WLBH = '" + xsddmx.getXSDDMX_WLBH() + "' ");
			global.getResultSql().getUpdateXsddmxSqls().add(sql.toString());
		}
		
		// 5.生成updateXshk sql语句
		for (XSHK xshk : global.getNeedUpdateXshk()) {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE XSHK ");
			sql.append("SET XSHK_DDHX = '"+ xshk.getXSHK_DDHX() +"', ");
			sql.append("XSHK_YDHSJHK = '" + xshk.getXSHK_YDHSJHK() + "', ");
			sql.append("XSHK_YDHXE = '" + xshk.getXSHK_YDHXE() + "' ");
			sql.append("WHERE XSHK_HKLS = '" + xshk.getXSHK_HKLS() + "' ");
			global.getResultSql().getUpdateXshkSqls().add(sql.toString());
		}
		// 6.XSDD更新语句已在核销过程中加入了
		
	}
	
	private void dealWithOneYwy(Ywy_detail currentYwyDetail, Zdhx_global global){
		for(Shdkh_detail currentKh : currentYwyDetail.getShdkh_details()) {
			for(XSHK xshk : currentKh.getXshks()) {
				int state = HxCommonUtil.compare(xshk.getXSHK_BHKE(),xshk.getXSHK_YDHSJHK());
				switch (state) {
				case -1: // 如果回款总数<已核销回款
					log.error("回款单号为" + xshk.getXSHK_HKBH() + "的回款单,核销有问题");
					break;
				case 0: // 如果回款总数==已核销回款
					log.info("回款单号为" + xshk.getXSHK_HKBH() + "的回款单,已经核销完毕,却仍然被拉取了,所以要更新改XSHK记录");
					xshk.setXSHK_DDHX("1");
					if(!global.getNeedUpdateXshk().contains(xshk)) {global.getNeedUpdateXshk().add(xshk);}
					break;
				case 1: // 如果回款总数>已核销回款
					// 1.计算剩余可供核销回款
					String remainAblehx = HxCommonUtil.subtract(xshk.getXSHK_YHKE(), xshk.getXSHK_YDHSJHK());
					// 2.有未核销金额,直接循环所有xsdd和xsddmx组合
					xsdddetailLoop : for(Xsdd_detail xsddDetail : currentKh.getXsddDetails()) {
						xsddmxLoop : for(XSDDMX xsddmx : xsddDetail.getXsddmxs()) {
							
							// 如果剩余可核销金额为0,退出所有循环
							if(0 == HxCommonUtil.compare(remainAblehx, "0")) {
								if(!global.getNeedUpdateXshk().contains(xshk)) {xshk.setXSHK_DDHX("1");global.getNeedUpdateXshk().add(xshk);}
								break xsdddetailLoop;
							}
							// 判断订单明细是否可以核销
							int hxState = HxCommonUtil.compare(xsddmx.getXSDDMX_YYFE(), xsddmx.getXSDDMX_YHXJE());
							// 无需核销
							switch(hxState) {
							case 0 :
								// 如果核销标志为1,
								if("1".equals(xsddmx.getXSDDMX_HKHX().trim())) {continue xsddmxLoop;
								} else if(!global.getNeedUpdateXsddmx().contains(xsddmx)) { 
									xsddmx.setXSDDMX_HKHX("1");global.getNeedUpdateXsddmx().add(xsddmx);}
								continue xsddmxLoop;
							case -1 :
								log.error("订单流水为" + xsddmx.getXSDDMX_DDLS() + ",产品编号为" + xsddmx.getXSDDMX_WLBH() +"的订单明细核销有问题,请注意");
								continue xsddmxLoop;
							case 1 :
								// 计算剩余需核销金额
								String remainNeedHx = HxCommonUtil.subtract(xsddmx.getXSDDMX_YYFE(), xsddmx.getXSDDMX_YHXJE());
								// 计算剩余需核销金额状态
								int hxType = HxCommonUtil.compare(remainNeedHx, remainAblehx);
								// 如果需要核销的金额>可核销的金额
								if(1 == hxType) {
									xsddmx.setXSDDMX_YHXJE(HxCommonUtil.add(xsddmx.getXSDDMX_YHXJE(), remainAblehx));
									xsddmx.setXSDDMX_HXSJHK(HxCommonUtil.add(xsddmx.getXSDDMX_HXSJHK(), remainAblehx));
									XSDDHX xsddhx = new XSDDHX();
									xsddhx.setXSDDHX_DDLS(xsddmx.getXSDDMX_DDLS());
									xsddhx.setXSDDHX_DDFL(xsddmx.getXSDDMX_DDFL());
									xsddhx.setXSDDHX_HKLS(xshk.getXSHK_HKLS());
									xsddhx.setXSDDHX_WLBH(xsddmx.getXSDDMX_WLBH());
									xsddhx.setXSDDHX_HXSJHK(remainAblehx);
									xsddhx.setXSDDHX_HXJE(remainAblehx);
									xsddhx.setXSDDHX_HXXM(this.getUserName());
									global.getNeedInsertXsddhx().add(xsddhx);
									// 改变回款的记录
									xshk.setXSHK_YDHSJHK(xshk.getXSHK_BHKE());
									xshk.setXSHK_YDHXE(xshk.getXSHK_BHKE());
									xshk.setXSHK_DDHX("1");
									remainAblehx = "0";
								}
								// 如果需核销金额=可核销金额
								if(0 == hxType) {
									// 改变明细的记录
									xsddmx.setXSDDMX_YHXJE(xsddmx.getXSDDMX_YYFE());
									xsddmx.setXSDDMX_HXSJHK(xsddmx.getXSDDMX_YYFE());
									xsddmx.setXSDDMX_HKHX("1");
									// 生成订单核销记录
									XSDDHX xsddhx = new XSDDHX();
									xsddhx.setXSDDHX_DDLS(xsddmx.getXSDDMX_DDLS());
									xsddhx.setXSDDHX_DDFL(xsddmx.getXSDDMX_DDFL());
									xsddhx.setXSDDHX_HKLS(xshk.getXSHK_HKLS());
									xsddhx.setXSDDHX_WLBH(xsddmx.getXSDDMX_WLBH());
									xsddhx.setXSDDHX_HXSJHK(remainNeedHx);
									xsddhx.setXSDDHX_HXJE(remainNeedHx);
									xsddhx.setXSDDHX_HXXM(this.getUserName());
									global.getNeedInsertXsddhx().add(xsddhx);
									// 改变回款的记录
									xshk.setXSHK_YDHSJHK(xshk.getXSHK_BHKE());
									xshk.setXSHK_YDHXE(xshk.getXSHK_BHKE());
									xshk.setXSHK_DDHX("1");
									remainAblehx = "0";
								}
								// 如果应核销金额<可核销金额
								if(-1 == hxType) {
									// 改变明细的记录
									xsddmx.setXSDDMX_YHXJE(xsddmx.getXSDDMX_YYFE());
									xsddmx.setXSDDMX_HXSJHK(xsddmx.getXSDDMX_YYFE());
									xsddmx.setXSDDMX_HKHX("1");
									// 生成订单核销记录
									XSDDHX xsddhx = new XSDDHX();
									xsddhx.setXSDDHX_DDLS(xsddmx.getXSDDMX_DDLS());
									xsddhx.setXSDDHX_DDFL(xsddmx.getXSDDMX_DDFL());
									xsddhx.setXSDDHX_HKLS(xshk.getXSHK_HKLS());
									xsddhx.setXSDDHX_WLBH(xsddmx.getXSDDMX_WLBH());
									xsddhx.setXSDDHX_HXSJHK(remainNeedHx);
									xsddhx.setXSDDHX_HXJE(remainNeedHx);
									xsddhx.setXSDDHX_HXXM(this.getUserName());
									global.getNeedInsertXsddhx().add(xsddhx);
									// 改变回款的记录
									xshk.setXSHK_YDHSJHK(HxCommonUtil.add(xshk.getXSHK_YDHSJHK(), remainNeedHx));
									xshk.setXSHK_YDHXE(HxCommonUtil.add(xshk.getXSHK_YDHXE(), remainNeedHx));
									remainAblehx = HxCommonUtil.subtract(remainAblehx, remainNeedHx);
								}
								if(!global.getNeedUpdateXsddmx().contains(xsddmx)) {global.getNeedUpdateXsddmx().add(xsddmx);}
								if(!global.getNeedUpdateXshk().contains(xshk)) {global.getNeedUpdateXshk().add(xshk);}
							}
						}
						
					}
					break;
				default:
					break;
				}
			}
			for(Xsdd_detail xsdd_detail : currentKh.getXsddDetails()) {
				// 判断XSDD是否核销完毕
				Boolean hxResult = true;
				if(xsdd_detail.getXsddmxs().size() == 0) {
					break;
				}
				for(XSDDMX xsddmx : xsdd_detail.getXsddmxs()) {
					if(0 != HxCommonUtil.compare(xsddmx.getXSDDMX_YYFE(), xsddmx.getXSDDMX_YHXJE())) {
						hxResult = false;
						break;
					}
				}
				if(hxResult) {
					global.getResultSql().getUpdateXsddSqls().add("UPDATE XSDD SET XSDD_HKHX = '1' WHERE XSDD_DDLS = '" + xsdd_detail.getDdls() + "'");
				}
			}
		}
	}

	/**
	 * 按省份核销时,查询相应XSDD和XSDDMX
	 * @param in
	 * @param global
	 * @throws SQLException
	 * @throws NoSuchFieldException
	 */
	private void provinceXsddGet(HxProvincePojoIn in, Zdhx_global global) throws SQLException, NoSuchFieldException{
		
		// 1.根据省份信息,查询所有需要核销的XSDD及XSDDMX
		XSDD_XSDDMXS xsdd_xsddmxs = zdhxDao.receiveXsdds_byProvince(in.getPrivinceId(), CommonUtil.formateTiemToBasic(in.getShipmentsEnd()) , this.getConnection());

		dealWithXsdd_xsddmx(xsdd_xsddmxs, global);
		
	}
	
	private void dealWithXsdd_xsddmx(XSDD_XSDDMXS xsdd_xsddmxs, Zdhx_global global) throws NoSuchFieldException {
		List<XSDD> xsdds = xsdd_xsddmxs.getXsdds();
		List<XSDDMX> xsddmxs = xsdd_xsddmxs.getXsddmxs();
		
		// 根据订单流水将xsddmx分类
		Map<String, List<XSDDMX>> xsddmxs_classify = CommonUtil.classify(xsddmxs, "XSDDMX_DDLS", XSDDMX.class);
		
		// 2.根据业务员分类
		Map<String, List<XSDD>> xsdd_classifyByYwy = CommonUtil.classify(xsdds, "XSDD_RYBH", XSDD.class);
		
		for(String ywykey : xsdd_classifyByYwy.keySet()) {
			
			// 2.1 分出业务员
			Ywy_detail currentYwyDetail = new Ywy_detail();
			
			currentYwyDetail.setRybh(ywykey);
			
			// 2.2分出客户的订单
			Map<String, List<XSDD>> xsdd_classifyByShdkh = CommonUtil.classify(CommonUtil.getListInMapByKey(xsdd_classifyByYwy, ywykey), "XSDD_SHDKH", XSDD.class);
			
			for(String khkey : xsdd_classifyByShdkh.keySet()) {

				Shdkh_detail currentShdkhDetail = new Shdkh_detail();
				
				currentShdkhDetail.setShdkh(khkey);
				
				// 获取该业务员下,该客户下的所有XSDD
				List<XSDD> ywy_kh_xsdds = CommonUtil.getListInMapByKey(xsdd_classifyByShdkh, khkey);
				
				for(XSDD xsdd : ywy_kh_xsdds) {
					
					Xsdd_detail currentXsddDetail = new Xsdd_detail();
					currentXsddDetail.setXsdd(xsdd);
					currentXsddDetail.setDdls(xsdd.getXSDD_DDLS());
					
					// 从分类过的ddmx中找出该XSDD下的XSDDMX
					currentXsddDetail.setXsddmxs(CommonUtil.getListInMapByKey(xsddmxs_classify, xsdd.getXSDD_DDLS()));
					currentShdkhDetail.getXsddDetails().add(currentXsddDetail);
				}
				
				currentYwyDetail.getShdkh_details().add(currentShdkhDetail);
			}
			
			global.getYwy_details().add(currentYwyDetail);
		}
	}

	

	private void ywyXsddGet(HxYwyPojoIn in, Zdhx_global global) throws NoSuchFieldException, SQLException {

		// 1.根据省份信息,查询所有需要核销的XSDD及XSDDMX
		XSDD_XSDDMXS xsdd_xsddmxs = zdhxDao.receiveXsdds_byYwy(in.getYwyId(), in.getShipmentsEnd() , this.getConnection());
		dealWithXsdd_xsddmx(xsdd_xsddmxs, global);
	}

	@Override
	public String addNotHx(String type, String dh, String cpbh) {

		this.before();
		JsonDataBack back = new JsonDataBack();		
		switch(type) {
		case "1":
			back = this.addNotHxXsddmx(dh,cpbh);
			break;
		case "2":
			back = addNotHxXshk(dh);
			break;
		case "3":
			back = addPingzhangKh(dh);
			break;
		case "4":
			back = addZhengce12(cpbh);
			break;
		default: break;
		}
		return this.after(back.toJsonString());
	}
	

	private JsonDataBack addZhengce12(String cpbh) {
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 根据订单编号和产品编号,查询该订单明细是否已经核销
			LSWLZD kh = zdhxDao.receiveCp(cpbh, this.getConnection());
			
			if(null == kh) {

				back.setStateError("该产品不存在,无法加入本列表中");
				return back;
			}
			
			else {
				back.setStateOk();
				// 将该订单及产品加入表中,并重新查询一次
				zdhxDao.addNotHxCp(cpbh, this.getConnection());
				// 查询所有政策12产品
				List<LSWLZD> cps = zdhxDao.receiveZhengce12(this.getConnection());
				back.setData(JSONArray.fromObject(cps).toString());
				return back;
			}
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("添加政策12产品出错");
			back.setStateError();
			return back;
		}
	}

	private JsonDataBack addPingzhangKh(String dh) {
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 根据订单编号和产品编号,查询该订单明细是否已经核销
			ZWWLDW kh = zdhxDao.receiveKh(dh, this.getConnection());
			
			if(null == kh) {

				back.setStateError("该客户不存在,无法加入本列表中");
				return back;
			}
			
			else {
				back.setStateOk();
				// 将该订单及产品加入表中,并重新查询一次
				zdhxDao.addNotHxKh(dh, this.getConnection());
				List<ZWWLDW> xskhs = zdhxDao.receivePingzhangKh(this.getConnection());
				back.setData(JSONArray.fromObject(xskhs).toString());
				return back;
			}
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("添加平账客户出错");
			back.setStateError();
			return back;
		}
	}

	private JsonDataBack addNotHxXshk(String dh) {
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 根据订单编号和产品编号,查询该订单明细是否已经核销
			XSHK xshk = zdhxDao.receiveXshk(dh, this.getConnection());
			
			if(null == xshk) {

				back.setStateError("该回款不存在,无法加入本列表中");
				return back;
			}
			// 验证该XSHK是否参加过核销
			if("1".equals(xshk.getXSHK_DDHX().trim()) || !("".equals(xshk.getXSHK_YDHXE()) || "0".equals(xshk.getXSHK_YDHXE()))) {
				
				back.setStateError("该回款已经参与核销,无法加入本列表中");
				return back;
			}
			else {
				back.setStateOk();
				
				// 将该订单及产品加入表中,并重新查询一次
				zdhxDao.addNotHxXshk(dh, xshk.getXSHK_HKLS(), this.getConnection());
				List<NOHX_XSHK> xsddmxs = zdhxDao.receiveNohxXshk(this.getConnection());
				back.setData(JSONArray.fromObject(xsddmxs).toString());
				return back;
			}
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("添加不核销销售回款出错");
			back.setStateError();
			return back;
		}
	}

	private JsonDataBack addNotHxXsddmx(String xsddDdbh, String xsddCpbh) {

		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 根据订单编号和产品编号,查询该订单明细是否已经核销
			XSDDMX xsddmx = zdhxDao.receiveXsddmx(xsddDdbh, xsddCpbh, this.getConnection());
			
			if(null == xsddmx) {

				back.setStateError("该订单不存在,无法加入本列表中");
				return back;
			}
			// 验证该xsddmx
			if("1".equals(xsddmx.getXSDDMX_HKHX().trim()) || !("".equals(xsddmx.getXSDDMX_YHXJE().trim()) || "0".equals(xsddmx.getXSDDMX_YHXJE().trim()))) {
				
				back.setStateError("该订单中的指定产品已经参与核销,无法加入本列表中");
				return back;
			}
			else {
				back.setStateOk();
				
				// 将该订单及产品加入表中,并重新查询一次
				zdhxDao.addNotHxXsddmx(xsddDdbh, xsddCpbh, xsddmx.getXSDDMX_DDLS(),this.getConnection());
				List<NOHX_XSDDMX> xsddmxs = zdhxDao.receiveNohxXsddmxs(this.getConnection());
				back.setData(JSONArray.fromObject(xsddmxs).toString());
				return back;
			}
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("添加不核销订单明细出错");
			back.setStateError();
			return back;
		}
	}
	

	@Override
	public String receiveNoHx(String type) {
		
		this.before();
		JsonDataBack back = new JsonDataBack();		
		switch(type) {
		case "1":
			back = receiveNoHxXsddmxs();
			break;
		case "2":
			back = receiveNoHxXshk();
			break;
		case "3":
			back = receivePingzhangKh();
			break;
		case "4":
			back = receiveZhengce12();
			break;
		default: break;
		}
		return this.after(back.toJsonString());
	}
	
	private JsonDataBack receiveZhengce12() {
		
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 查询所有政策12产品
			List<LSWLZD> cps = zdhxDao.receiveZhengce12(this.getConnection());

			back.setStateOk();
			back.setData(JSONArray.fromObject(cps).toString());
			
			return back;
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("获取平账客户出错");
			back.setStateError();
			return back;
		}
	}

	private JsonDataBack receivePingzhangKh() {
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 查询所有平账客户
			List<ZWWLDW> xskhs = zdhxDao.receivePingzhangKh(this.getConnection());

			back.setStateOk();
			back.setData(JSONArray.fromObject(xskhs).toString());
			
			return back;
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("获取平账客户出错");
			back.setStateError();
			return back;
		}
	}

	/**
	 * 获取不核销回款
	 * @return
	 */
	private JsonDataBack receiveNoHxXshk() {
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 查询所有不参与核销订单明细
			List<NOHX_XSHK> xshks = zdhxDao.receiveNohxXshk(this.getConnection());
			List<NOHX_XSHK> deleteHks = new ArrayList<>();
			List<String> delSqls = new ArrayList<>();
			// 循环检测核销状态
			for(NOHX_XSHK xshk : xshks) {

				// 验证该xshk,如果参与了核销,从该列表中删除
				if("1".equals(xshk.getXSHK_DDHX().trim()) || !("".equals(xshk.getXSHK_YDHXE().trim()) || "0".equals(xshk.getXSHK_YDHXE().trim()))) {
					deleteHks.add(xshk);
					String sql = "DELETE FROM NBPT_HX_NOHX WHERE NBPT_HX_NOHX_UID = '" + xshk.getNBPT_HX_NOHX_UID() + "'";
					delSqls.add(sql);
				}
			}
			
			// 如有参与核销的,删除
			if(0 != delSqls.size()) {
				zdhxDao.excuteUpdateGroups(delSqls, this.getConnection());
			}
			for(NOHX_XSHK xshk : deleteHks) {
				xshks.remove(xshk);
			}
			back.setStateOk();
			back.setData(JSONArray.fromObject(xshks).toString());
			
			return back;
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("获取不核销回款单出错");
			back.setStateError();
			return back;
		}
	}
	/**
	 * 获取不核销订单
	 * @return
	 */
	private JsonDataBack receiveNoHxXsddmxs() {
		
		// 初始化返回内容
		JsonDataBack back = new JsonDataBack();
		try {

			// 查询所有不参与核销订单明细
			List<NOHX_XSDDMX> xsddmxs = zdhxDao.receiveNohxXsddmxs(this.getConnection());
			List<NOHX_XSDDMX> deleteMxs = new ArrayList<>();
			List<String> delSqls = new ArrayList<>();
			// 循环检测核销状态
			for(NOHX_XSDDMX xsddmx : xsddmxs) {

				// 验证该xsddmx,如果参与了核销,从该列表中删除
				if("1".equals(xsddmx.getXSDDMX_HKHX().trim()) || !("".equals(xsddmx.getXSDDMX_YHXJE().trim()) || "0".equals(xsddmx.getXSDDMX_YHXJE().trim()))) {
					deleteMxs.add(xsddmx);
					String sql = "DELETE FROM NBPT_HX_NOHX WHERE NBPT_HX_NOHX_UID = '" + xsddmx.getNBPT_HX_NOHX_UID() + "'";
					delSqls.add(sql);
				}
			}
			
			// 如有参与核销的,删除
			if(0 != delSqls.size()) {
				zdhxDao.excuteUpdateGroups(delSqls, this.getConnection());
			}
			for(NOHX_XSDDMX xsddmx : deleteMxs) {
				xsddmxs.remove(xsddmx);
			}
			back.setStateOk();
			back.setData(JSONArray.fromObject(xsddmxs).toString());
			
			return back;
			// 根据订单编号和产品编号,将该明细加入列表中
		} catch (SQLException e) {

			log.error("获取不核销销售明细出错");
			back.setStateError();
			return back;
		}
	}
}





























