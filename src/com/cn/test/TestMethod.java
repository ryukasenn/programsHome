package com.cn.test;

import java.security.MessageDigest;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import com.cn.lingrui.common.db.dbpojos.GSDBZTXX;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.hx.db.dbpojos.XSDD;
import com.cn.lingrui.hx.utils.HxCommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;
import com.mysql.fabric.xmlrpc.base.Data;

import net.sf.json.JSONObject;

public class TestMethod {

	@Test
	public void test1() {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwmaster", "cw001" + "0014", "cwpass");

			PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM GSDBZTXX WHERE GSDBZTXX_ZTBH = '001'");

			ResultSet rs1 = ps1.executeQuery();

			List<GSDBZTXX> listGSDBZTXX = DBUtils.rsToBean(GSDBZTXX.class, rs1);
			if (1 == listGSDBZTXX.size()) {

				GlobalParams.RSFZ_PASSWORD = CommonUtil.decodeRs(listGSDBZTXX.get(0).getGSDBZTXX_PASS());
				GlobalParams.RSFZ_USERNAME = listGSDBZTXX.get(0).getGSDBZTXX_OWNER();
				GlobalParams.RSFZ_DBNAME = listGSDBZTXX.get(0).getGSDBZTXX_BASE();
			}
			System.out.println(GlobalParams.RSFZ_PASSWORD + ":" + GlobalParams.RSFZ_USERNAME + ":" + GlobalParams.RSFZ_DBNAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void test2() {
		
		Calendar now = Calendar.getInstance();
		Integer year = now.get(Calendar.YEAR);
		System.out.println(year);
		String s = "411523199201270470";
		System.out.println(s.substring(6, 10));
		Integer age = year - Integer.valueOf(s.substring(7, 9));
		System.out.println(age);
	}

	@Test
	public void test3() {
		
		String s = "/sys/pmHome";
		String[] ss = s.split("\\?");
		System.out.println(ss[0]);
	}
	

	@Test
	public void test4() {
		
		String[] s = null;
		List<String> cao = new ArrayList<String>();
		cao.add("nimabi");
		cao.add("rilegou");
		s = cao.toArray(new String[0]);
		for(int i = 0; i < s.length ; i++) {

			System.out.println(s[i]);
		}
	}

	@Test
	public void numListToString() {
		List<String> list = new ArrayList<String>();
		list.add("01");
		list.add("02");
		list.add("03");
		String s = list.toString();
		String result = s.replaceAll(", ", "\",\" ").replace("[", "[\"").replace("]", "\"]");
		System.out.println(result);
	}
	
	@Test
	public void dateFunc() {
		
		System.out.println(CommonUtil.getYYYYMMDDHHMMSS());
	}
	/**
	 * 获取UUID随机串
	 */
	@Test
	public void getUUIDString() {

		String rtnVal = Long.toHexString(System.currentTimeMillis());
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		System.out.println(rtnVal.substring(0, 32));
	}

	@Test
	public void testList() {
		
		List<NBPT_COMMON_XZQXHF> list = new ArrayList<>();
		
		NBPT_COMMON_XZQXHF xzqxhf1 = new NBPT_COMMON_XZQXHF();
		xzqxhf1.setNBPT_COMMON_XZQXHF_ID("1");
		xzqxhf1.setNBPT_COMMON_XZQXHF_LEVEL("1");
		xzqxhf1.setNBPT_COMMON_XZQXHF_NAME("1");
		xzqxhf1.setNBPT_COMMON_XZQXHF_PID("1");
		NBPT_COMMON_XZQXHF xzqxhf2 = new NBPT_COMMON_XZQXHF();
		xzqxhf2.setNBPT_COMMON_XZQXHF_ID("2");
		xzqxhf2.setNBPT_COMMON_XZQXHF_LEVEL("2");
		xzqxhf2.setNBPT_COMMON_XZQXHF_NAME("2");
		xzqxhf2.setNBPT_COMMON_XZQXHF_PID("2");
		NBPT_COMMON_XZQXHF xzqxhf3 = new NBPT_COMMON_XZQXHF();
		xzqxhf3.setNBPT_COMMON_XZQXHF_ID("3");
		xzqxhf3.setNBPT_COMMON_XZQXHF_LEVEL("3");
		xzqxhf3.setNBPT_COMMON_XZQXHF_NAME("3");
		xzqxhf3.setNBPT_COMMON_XZQXHF_PID("3");
		list.add(xzqxhf1);
		list.add(xzqxhf2);
		list.add(xzqxhf3);
	}
	
	@Test
	public void test5() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		CommonUtil.class.getClassLoader().getResourceAsStream(CommonUtil.class.getResource("/").getPath() + "com/cn/config/base.properties");
		DBUtils.receiveGlobalParam("ERPTGJ_PASSWORD");
	}

	@Test
	public void test() {
		
		CommonUtil.getBasePropertieValue("BASE_URL");
	}
	
	@Test
	public void test6() {
		
		for(int i = 0 ; i < 5; i++) {
			
			System.out.println(CommonUtil.getUUID_32());
		}
	}
	
	@Test
	public void test7() throws InterruptedException {
		
	}
	
	@Test
	public void test8() {
		
		String s = "{flag : true, message : '验证成功'}";
		JSONObject json = JSONObject.fromObject(s);
		json.put("flag", false);
		System.out.println(json.get("flag"));
	}
	
	@Test
	public void test9() {
		
		System.out.println(new Date().getTime());
		System.out.println(new java.sql.Date(new Date().getTime()).getTime());
	}
	
	@Test
	public void test10() {
		
		NBPT_SP_PERSON a = new NBPT_SP_PERSON();
		a.setNBPT_SP_PERSON_PID("123");
		a.setNBPT_SP_PERSON_ID("222");
		a.setNBPT_SP_PERSON_IDNUM("411523");
		
		NBPT_SP_PERSON b = new NBPT_SP_PERSON();
		
		BeanUtils.copyProperties(a, b);
		
		System.out.println(b.getNBPT_SP_PERSON_PID() + "  " + b.getNBPT_SP_PERSON_IDNUM() + "  " + b.getNBPT_SP_PERSON_ID());
	}
	
	@Test
	public void test11() {
		
		JsonDataBack back = new JsonDataBack();
		back.setStateOk("你好");
		System.out.println(back.toJsonString());
		
		
		
	}


	
}
























