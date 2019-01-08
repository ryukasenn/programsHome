package com.cn.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_KH;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class XZQX_create {

	@Test
	public void init() throws ClassNotFoundException, SQLException, NoSuchFieldException {
		
		// 添加行政区域划分
		//this.addXZQXHF();
		
		// 添加大区
		//this.addRegions();
		
		// 添加地区
		//this.addAreas();
		
		// 添加地总
		//this.addDiZong();
		
		// 添加大区省份对应关系
		//this.addRegins_XZQXHF();
		
		// 添加地区与行政区县关系
		//this.addArea_Xzqxhf();
		
		//System.out.println(CommonUtil.getUUID_32());
		
		// 地区负责人添加
		//this.checkResponseble();
		//this.responsebleAdd();
		
		// 添加部门
		//this.test1();
		
		// 添加终端
		// 1.添加人员
		//this.addTerminals();
		
		// 添加人员配置
		//this.addNeed();
		
		// 添加地区省份对应关系
		//this.addArea_Province();
		
		// 添加负责人和部门关系列表
		//this.addResponseble();
		
		// 添加用户名
		//this.addLoginId();
		
		//System.out.println(CommonUtil.getUUID_32());
		
		// 删除离职人员
		//this.deleteOut();
		
		// 添加大区总
		//this.addRegionResper();
		//this.addRegionResperRights();
		
		// 查看身份证有误
		//this.checkIdnum();
		
		// 添加其他类别人员
		//this.addOtherType();
		
		// 修改性别
		//this.changMale();
		
		// 回复

		this.tianjiaPid();
	}
	
	private void tianjiaPid()throws ClassNotFoundException, SQLException, NoSuchFieldException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		System.out.println("SELECT * FROM NBPT_SP_KH_BACK1 WHERE NBPT_SP_KH_PID IS NULL");
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_SP_KH_BACK1 WHERE NBPT_SP_KH_PID IS NULL" );
		ResultSet rs1 = ps1.executeQuery();
		List<NBPT_SP_KH> resultList = XZQX_create.rsToBean(NBPT_SP_KH.class, rs1);
		conn.setAutoCommit(false);
		PreparedStatement ps2 = conn.prepareStatement("UPDATE NBPT_SP_KH_BACK1 SET NBPT_SP_KH_PID = ? WHERE NBPT_SP_KH_ID = ?");
		for(NBPT_SP_KH kh : resultList) {
			
			ps2.setString(1, CommonUtil.getUUID_32());
			ps2.setInt(2, Integer.valueOf(kh.getNBPT_SP_KH_ID()));
			ps2.addBatch();
			
		}
		ps2.executeBatch();
		rs1.close();
		ps1.close();
		ps2.close();
		conn.commit();
		conn.close();
	}
	
	private void changMale() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_VIEW_CURRENTPERSON A WHERE A.NBPT_SP_PERSON_JOB IN ('25','23','24')");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询所有地总
		List<NBPT_VIEW_CURRENTPERSON> resultList = XZQX_create.rsToBean(NBPT_VIEW_CURRENTPERSON.class, rs1);
		
		List<String> sqls = new ArrayList<>();
		
		for(NBPT_VIEW_CURRENTPERSON person : resultList) {
			
			if(CommonUtil.idNumCheck(person.getNBPT_SP_PERSON_IDNUM())) {

				// 身份证验证为男
				if(1 == CommonUtil.getPersonMale(person.getNBPT_SP_PERSON_IDNUM())) {
					
					// 数据库中为女
					if("0".equals(person.getNBPT_SP_PERSON_MALE())) {
						
						sqls.add("UPDATE NBPT_SP_PERSON SET NBPT_SP_PERSON_MALE = '1' WHERE NBPT_SP_PERSON_PID = '" + person.getNBPT_SP_PERSON_PID() + "'");
					}
				} else {
					
					if("1".equals(person.getNBPT_SP_PERSON_MALE())) {
						
						sqls.add("UPDATE NBPT_SP_PERSON SET NBPT_SP_PERSON_MALE = '0' WHERE NBPT_SP_PERSON_PID = '" + person.getNBPT_SP_PERSON_PID() + "'");
					}
				}
			} else {
				
				System.out.println(person.getNBPT_SP_PERSON_REGION_NAME() + "\t" 
							+ person.getNBPT_SP_PERSON_AREA_NAME() + "\t" 
							+ person.getNBPT_SP_PERSON_AREA_RESPONSIBLER_NAME() + "\t"
							+ person.getNBPT_SP_PERSON_NAME() + "\t"
							+ person.getNBPT_SP_PERSON_IDNUM());
			}
			
		}
		Statement stmt;
		stmt = conn.createStatement();


		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		//stmt.executeBatch();
	}

	private void addOtherType() throws ClassNotFoundException, SQLException {
		
		String fileNameTemp = GlobalParams.FILE_PATH + "qita.xls";
		File targetFile = new File(fileNameTemp);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
//		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
				
		// 查询所有地总
		if(!targetFile.exists()) {
			
		} else {
			try {
				List<String> sqls = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
			
			
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 2; rowi < realRows; rowi++) {
						
						if("".equals(sheet.getCell(4,rowi).getContents())) {
							
							break;
						} else {
							
							
							String male = "男".equals(sheet.getCell(4,rowi).getContents())?"1":"0";
							String birs = "";
							
							if(!"".equals(sheet.getCell(18,rowi).getContents().trim())) {

								birs= sheet.getCell(18,rowi).getContents().substring(6, 14);
							}else {

								System.out.println("姓名为" + sheet.getCell(2,rowi).getContents() + "身份证号为空");
							}
							String entryDate = "";
							
							if (sheet.getCell(15,rowi).getType() == CellType.DATE) { 
						         DateCell dc = (DateCell) sheet.getCell(15,rowi);
						         entryDate = CommonUtil.dateToYYYYMMDD(dc.getDate()); 
							} 

							if("".equals(entryDate)){
								

								System.out.println("姓名为" + sheet.getCell(2,rowi).getContents() + "入职日期有误");
							}
							String type = "临床部".equals(sheet.getCell(1,rowi).getContents())? "3" : "基层医疗拓展部".equals(sheet.getCell(1,rowi).getContents())? "4" : "1";
							String job = "";
							if("1".equals(type)) {
								
								job = "大区总经理".equals(sheet.getCell(8,rowi).getContents()) ? "11" : "12";
							} 
							
							else if("3".equals(type)) {
								
								job = "大区总经理".equals(sheet.getCell(8,rowi).getContents()) ? "31" : "32";
							}
							
							else if("4".equals(type)) {
								
								job = "大区总经理".equals(sheet.getCell(8,rowi).getContents()) ? "41" : "42";
							}
							String sql = "INSERT NBPT_SP_PERSON ("
									+ "NBPT_SP_PERSON_PID,"
									+ "NBPT_SP_PERSON_ID,"
									+ "NBPT_SP_PERSON_TYPE,"
									+ "NBPT_SP_PERSON_NAME,"
									+ "NBPT_SP_PERSON_MALE,"
									+ "NBPT_SP_PERSON_BIRS,"
									+ "NBPT_SP_PERSON_IDNUM,"
									+ "NBPT_SP_PERSON_MOB1,"
									+ "NBPT_SP_PERSON_MOB2,"
									+ "NBPT_SP_PERSON_PLACE,"
									+ "NBPT_SP_PERSON_JOB,"
									+ "NBPT_SP_PERSON_DEGREE,"
									+ "NBPT_SP_PERSON_FLAG,"
									+ "NBPT_SP_PERSON_ENTRYDATA,"
									+ "NBPT_SP_PERSON_ENTERINGTIME"
									+ ")"
									+ " VALUES ("
									+ "'" + CommonUtil.getUUID_32() + "',"
									+ "'" + (103096 + rowi - 1) + "',"
									+ "'" + type + "',"
									+ "'" + sheet.getCell(3,rowi).getContents() + "',"
									+ "'" + male + "',"
									+ "'" + birs + "',"
									+ "'" + sheet.getCell(18,rowi).getContents() + "',"
									+ "'" + sheet.getCell(19,rowi).getContents() + "',"
									+ "'" + sheet.getCell(23,rowi).getContents() + "',"
									+ "'" + sheet.getCell(17,rowi).getContents() + "',"
									+ "'" + job + "',"
									+ "'" + sheet.getCell(11,rowi).getContents() + "',"
									+ "'2',"
									+ "'" + entryDate + "',"
									+ "'" + CommonUtil.getYYYYMMDD() +"')";
							sqls.add(sql);
							
						}
					}
				}
				Statement stmt;
				stmt = conn.createStatement();


				for(String sql : sqls) {
					stmt.addBatch(sql);
				}

				stmt.executeBatch();
			} catch (BiffException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
		

	private void checkIdnum() throws ClassNotFoundException, SQLException {}

	private void addRegionResperRights() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
		//Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_SP_PERSON WHERE NBPT_SP_PERSON_JOB = '21'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询所有地总
		List<NBPT_SP_PERSON> resultList = XZQX_create.rsToBean(NBPT_SP_PERSON.class, rs1);	
		

		List<String> sqls1 = new ArrayList<String>();
		List<String> sqls2 = new ArrayList<String>();
		for(NBPT_SP_PERSON person : resultList) {
			
			String sql1 = "INSERT NBPT_RSFZ_USER (NBPT_RSFZ_USER_ID, NBPT_RSFZ_USER_NAME, NBPT_RSFZ_USER_PASSWORD) "
													 + "VALUES ('" + person.getNBPT_SP_PERSON_LOGINID() + "',"
													 + "'" + person.getNBPT_SP_PERSON_NAME() + "', "
													 + "'lingrui123')";
			sqls1.add(sql1);
			
			String sql2 = "INSERT NBPT_RSFZ_U_R VALUES ('" + person.getNBPT_SP_PERSON_LOGINID() + "', '600006')";
			sqls2.add(sql2);
			
		}

		Statement stmt;
		stmt = conn.createStatement();


		for(String sql : sqls1) {
			stmt.addBatch(sql);
		}
		for(String sql : sqls2) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}

	private void addRegionResper() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "daquzong.xls";
		File targetFile = new File(fileNameTemp);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
		//Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '1'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询所有地总
		List<NBPT_SP_REGION> resultList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs1);	
		if(!targetFile.exists()) {
			
		} else {

			try {
				List<String> sqls = new ArrayList<String>();
				List<String> chekcItems = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
			
			
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 3; rowi < realRows; rowi++) {
						
						if("".equals(sheet.getCell(4,rowi).getContents())) {
							
							break;
						} else {
							
							String deptId = "";
							for(NBPT_SP_REGION region : resultList) {
								
								if(sheet.getCell(1,rowi).getContents().trim().equals(region.getNBPT_SP_REGION_NAME())) {
									
									deptId = region.getNBPT_SP_REGION_UID();
									break;
								}
							}
							
							if("".equals(deptId)) {
								
								System.out.println(sheet.getCell(2,rowi).getContents()+"的大区名有误");
								continue;
							}
							
							String male = "男".equals(sheet.getCell(3,rowi).getContents())?"1":"0";
							String birs = "";
							
							if(!"".equals(sheet.getCell(18,rowi).getContents().trim())) {

								birs= sheet.getCell(18,rowi).getContents().substring(6, 14);
							}else {

								System.out.println("姓名为" + sheet.getCell(2,rowi).getContents() + "身份证号为空");
							}
							String entryDate = "";
							
							if (sheet.getCell(15,rowi).getType() == CellType.DATE) { 
						         DateCell dc = (DateCell) sheet.getCell(15,rowi);
						         entryDate = CommonUtil.dateToYYYYMMDD(dc.getDate()); 
							} 

							if("".equals(entryDate)){
								

								System.out.println("姓名为" + sheet.getCell(2,rowi).getContents() + "入职日期有误");
							}
							String sql = "INSERT NBPT_SP_PERSON ("
									+ "NBPT_SP_PERSON_PID,"
									+ "NBPT_SP_PERSON_ID,"
									+ "NBPT_SP_PERSON_DEPT_ID,"
									+ "NBPT_SP_PERSON_TYPE,"
									+ "NBPT_SP_PERSON_NAME,"
									+ "NBPT_SP_PERSON_MALE,"
									+ "NBPT_SP_PERSON_BIRS,"
									+ "NBPT_SP_PERSON_IDNUM,"
									+ "NBPT_SP_PERSON_MOB1,"
									+ "NBPT_SP_PERSON_MOB2,"
									+ "NBPT_SP_PERSON_PLACE,"
									+ "NBPT_SP_PERSON_JOB,"
									+ "NBPT_SP_PERSON_DEGREE,"
									+ "NBPT_SP_PERSON_FLAG,"
									+ "NBPT_SP_PERSON_ENTRYDATA"
									+ ")"
									+ " VALUES ("
									+ "'" + CommonUtil.getUUID_32() + "',"
									+ "'" + (102827 + rowi - 3) + "',"
									+ "'" + deptId + "',"
									+ "'2',"
									+ "'" + sheet.getCell(2,rowi).getContents() + "',"
									+ "'" + male + "',"
									+ "'" + birs + "',"
									+ "'" + sheet.getCell(18,rowi).getContents() + "',"
									+ "'" + sheet.getCell(19,rowi).getContents() + "',"
									+ "'',"
									+ "'" + sheet.getCell(17,rowi).getContents() + "',"
									+ "'21',"
									+ "'" + sheet.getCell(9,rowi).getContents() + "',"
									+ "'2',"
									+ "'" + entryDate + "'"
									+ ")";
							sqls.add(sql);
							
						}
					}
				}
				Statement stmt;
				stmt = conn.createStatement();


				for(String sql : sqls) {
					stmt.addBatch(sql);
				}

				stmt.executeBatch();
			} catch (BiffException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		
	}
	private void deleteOut() throws ClassNotFoundException, SQLException {
		
		String fileNameTemp = GlobalParams.FILE_PATH + "lizhi3.xls";
		File targetFile = new File(fileNameTemp);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
		//Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_SP_REGION WHERE NBPT_SP_REGION_LEVEL = '2'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询所有地总
		List<NBPT_SP_REGION> resultList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs1);	
		if(!targetFile.exists()) {
			
		} else {

			try {
				List<String> sqls = new ArrayList<String>();
				List<String> chekcItems = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
			
			
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {
						
						if("".equals(sheet.getCell(4,rowi).getContents())) {
							
							break;
						} else {
							
							String dept_name = sheet.getCell(2,rowi).getContents();
							String dept_UID = "";
							String personName = sheet.getCell(4,rowi).getContents();
							for(NBPT_SP_REGION region : resultList) {
								
								if(dept_name.trim().equals(region.getNBPT_SP_REGION_NAME().trim())) {
									
									dept_UID = region.getNBPT_SP_REGION_UID();
								} 
							}
							
							if("".equals(dept_UID)) {
									
								System.out.println(personName + "的地区有误");
							}
							
							
							String sql = "delete from nbpt_sp_person where nbpt_sp_person_name = '" + personName + "' and nbpt_sp_person_dept_id = '" + dept_UID + "'";
							
							sqls.add(sql);
						}
					}
				}
				Statement stmt;
				stmt = conn.createStatement();


				for(String sql : sqls) {
					stmt.addBatch(sql);
				}

				stmt.executeBatch();
			} catch (BiffException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}
	private void addLoginId() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_SP_PERSON WHERE NBPT_SP_PERSON_JOB = '22'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询所有地总
		List<NBPT_SP_PERSON> resultList = XZQX_create.rsToBean(NBPT_SP_PERSON.class, rs1);
		
		List<String> sqls = new ArrayList<>();
//		for(NBPT_SP_PERSON person : resultList) {
//			
//			String sql =  "INSERT NBPT_RSFZ_USER VALUES ("
//					+ "'" + person.getNBPT_SP_PERSON_LOGINID() + "',"
//					+ "'" + person.getNBPT_SP_PERSON_NAME() + "',"
//					+ "'',"
//					+ "'',"
//					+ "'',"
//					+ "'lingrui123')";
//			sqls.add(sql);
//		}
		
		for(NBPT_SP_PERSON person : resultList) {
		
			String sql =  "INSERT NBPT_RSFZ_U_R VALUES ("
					+ "'" + person.getNBPT_SP_PERSON_LOGINID() + "',"
					+ "'600006')";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
		
		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	private void addResponseble() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
//		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement(  "  SELECT A.* " + 
														"  FROM NBPT_SP_REGION A " + 
														"  LEFT JOIN NBPT_SP_REGION_XZQX B " + 
														"  ON A.NBPT_SP_REGION_ID = B.NBPT_SP_REGION_XZQX_REGIONID" + 
														"  WHERE (B.NBPT_SP_REGION_XZQX_REGIONID IS NULL OR B.NBPT_SP_REGION_XZQX_REGIONID = '') AND A.NBPT_SP_REGION_LEVEL = '2'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询河南区县划分
		List<NBPT_SP_REGION> resultList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs1);
		
		List<String> sqls = new ArrayList<>();
		for(NBPT_SP_REGION personRegion : resultList) {
			
			String sql = "INSERT NBPT_SP_REGION_XZQX VALUES("
					+ "'" + CommonUtil.getUUID_32() + "',"
					+ "'" + personRegion.getNBPT_SP_REGION_ID() + "',"
					+ "'" + personRegion.getNBPT_SP_REGION_ID().substring(2, 4) + "0000" + "',"
					+ "'21')";
//			String sql =  "UPDATE NBPT_SP_PERSON SET NBPT_SP_PERSON_DEPT_ID = '" + personRegion.getNBPT_SP_REGION_UID() + "' " + 
//						  "WHERE NBPT_SP_PERSON_PID = '" + personRegion.getNBPT_SP_REGION_RESPONSIBLER() + "'";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
		
		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	private void addArea_Province() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps1 = conn.prepareStatement("SELECT DISTINCT NBPT_SP_REGION_XZQX_REGIONID FROM NBPT_SP_REGION_XZQX");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询河南区县划分
		List<NBPT_SP_REGION_XZQX> resultList = XZQX_create.rsToBean(NBPT_SP_REGION_XZQX.class, rs1);
		
		List<String> sqls = new ArrayList<>();
		for(NBPT_SP_REGION_XZQX xzqx : resultList) {
			
			String sql =  "INSERT NBPT_SP_REGION_XZQX VALUES ("
						+ "'" + CommonUtil.getUUID_32() + "',"
						+ "'" + xzqx.getNBPT_SP_REGION_XZQX_REGIONID()+ "',"
						+ "'" + xzqx.getNBPT_SP_REGION_XZQX_REGIONID().substring(2, 4) + "0000',"
						+ "'21')";
			sqls.add(sql);
		}
		Statement stmt;
		stmt = conn.createStatement();
		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
		
	}
	private void addNeed() throws SQLException, ClassNotFoundException {
		String fileNameTemp = GlobalParams.FILE_PATH + "need.xls";
		File targetFile = new File(fileNameTemp); 
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);

//		PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM NBPT_COMMON_XZQXHF where NBPT_COMMON_XZQXHF_ID LIKE '41%'");
//		ResultSet rs1 = ps1.executeQuery();
//		
//		// 查询河南区县划分
//		List<NBPT_COMMON_XZQXHF> resultList = XZQX_create.rsToBean(NBPT_COMMON_XZQXHF.class, rs1);
//		
//		PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM NBPT_SP_REGION where NBPT_SP_REGION_ID LIKE '11%'");
//		ResultSet rs2 = ps2.executeQuery();
		
		//List<NBPT_SP_REGION> regionList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs2);

		try {
			
			if(!targetFile.exists()) {
				
			} else {

				List<String> sqls = new ArrayList<String>();
				List<String> chekcItems = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {
						
						if(chekcItems.contains(sheet.getCell(2,rowi).getContents())) {
							
						} else {
							
							chekcItems.add(sheet.getCell(2,rowi).getContents());
							
							String sql =  "INSERT NBPT_SP_REGION VALUES ('" + CommonUtil.getUUID_32() + "',"
										+ "'" + sheet.getCell(5,rowi).getContents() + sheet.getCell(6,rowi).getContents().substring(0, 2) + sheet.getCell(7,rowi).getContents() + "',"
										+ "'" + sheet.getCell(2,rowi).getContents() + "',"
										+ "'" + sheet.getCell(4,rowi).getContents() + "',"
										+ "'',"
										+ "'2',"
										+ "'')";
							sqls.add(sql);
						}
					}
					
					Statement stmt;
					stmt = conn.createStatement();
					for(String sql : sqls) {
						stmt.addBatch(sql);
					}
			
					stmt.executeBatch();
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void addTerminals() throws SQLException, ClassNotFoundException {
		String fileNameTemp = GlobalParams.FILE_PATH + "suoyouzhongduan.xls";
		File targetFile = new File(fileNameTemp); 
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.1.1:1433; DatabaseName=ekptest","xsrs", "Lrxsrs2018");
//		Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.9.63:1433; DatabaseName=cwbase1","lc0019999", "lrerp2012");
		
		PreparedStatement ps1 = conn.prepareStatement("SELECT A.* FROM NBPT_SP_REGION A WHERE A.NBPT_SP_REGION_LEVEL = '2'");
		ResultSet rs1 = ps1.executeQuery();
		
		// 查询区县划分
		List<NBPT_SP_REGION> resultList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs1);
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				List<String> sqls = new ArrayList<String>();
				List<String> chekcItems = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 2; rowi < realRows; rowi++) {
						
						{
							
							if("".equals(sheet.getCell(5,rowi).getContents())) {
								break;
							}
							// 加入检查队列
							chekcItems.add(sheet.getCell(12,rowi).getContents().trim());
							
							String male = "男".equals(sheet.getCell(6,rowi).getContents())?"1":"0";
							
							String birs = "";
							if(!"".equals(sheet.getCell(12,rowi).getContents().trim())) {

								birs= sheet.getCell(12,rowi).getContents().substring(6, 14);
							}else {

								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "身份证号为空,位于:" + (rowi + 1) + "行");
							}
							
							if("大区总经理".equals(sheet.getCell(8,rowi).getContents().trim()) || "地区总经理".equals(sheet.getCell(8,rowi).getContents().trim())) {
								
								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "非终端人员,位于:" + (rowi + 1) + "行");
								continue;
							}
							String job = "区县总经理".equals(sheet.getCell(8,rowi).getContents()) ? "23" : "预备区县总".equals(sheet.getCell(8,rowi).getContents()) ? "24" : "25";
							String entryDate = "";
							
							if (sheet.getCell(10,rowi).getType() == CellType.DATE) { 
						         DateCell dc = (DateCell) sheet.getCell(10,rowi);
						         entryDate = CommonUtil.dateToYYYYMMDD(dc.getDate()); 
							} 

							if("".equals(entryDate)){
								

								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "入职日期有误,位于:" + (rowi + 1) + "行");
							}
							
//							if(!(8 == entryDate.length())) {
//								
//								System.out.println("姓名为" + sheet.getCell(4,rowi).getContents() + "入职日期有误");
//								continue;
//							}
							
							String deptId = "";
							
							for(NBPT_SP_REGION region : resultList) {
								
								if(sheet.getCell(2,rowi).getContents().trim().equals(region.getNBPT_SP_REGION_NAME().trim())) {
									
									deptId = region.getNBPT_SP_REGION_UID();
									break;
								}
							}
							
							if("".equals(deptId)) {

								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "地区信息有误,位于:" + (rowi + 1) + "行");
								continue;
							}
							
							String mob1="", mob2 = "";
							if(sheet.getCell(14,rowi).getContents().length() != 11) {

								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "紧急联系人有误,位于:" + (rowi + 1) + "行");
							} else {
								mob2 = sheet.getCell(14,rowi).getContents();
							}

							if(sheet.getCell(13,rowi).getContents().length() != 11) {

								System.out.println("姓名为" + sheet.getCell(5,rowi).getContents() + "联系人有误,位于:" + (rowi + 1) + "行");
							} else {
								mob1 = sheet.getCell(13,rowi).getContents();
							}
							
							String sql = "INSERT NBPT_SP_PERSON ("
									+ "NBPT_SP_PERSON_PID,"
									+ "NBPT_SP_PERSON_ID,"
									+ "NBPT_SP_PERSON_DEPT_ID,"
									+ "NBPT_SP_PERSON_TYPE,"
									+ "NBPT_SP_PERSON_NAME,"
									+ "NBPT_SP_PERSON_MALE,"
									+ "NBPT_SP_PERSON_BIRS,"
									+ "NBPT_SP_PERSON_IDNUM,"
									+ "NBPT_SP_PERSON_MOB1,"
									+ "NBPT_SP_PERSON_MOB2,"
									+ "NBPT_SP_PERSON_PLACE,"
									+ "NBPT_SP_PERSON_JOB,"
									+ "NBPT_SP_PERSON_DEGREE,"
									+ "NBPT_SP_PERSON_FLAG,"
									+ "NBPT_SP_PERSON_ENTRYDATA"
									+ ")"
									+ " VALUES ("
									+ "'" + CommonUtil.getUUID_32() + "',"
									+ "'" + (100271 + rowi - 2) + "',"
									+ "'" + deptId + "',"
									+ "'2',"
									+ "'" + sheet.getCell(5,rowi).getContents() + "',"
									+ "'" + male + "',"
									+ "'" + birs + "',"
									+ "'" + sheet.getCell(12,rowi).getContents() + "',"
									+ "'" + mob1 + "',"
									+ "'" + mob2 + "',"
									+ "'" + sheet.getCell(11,rowi).getContents() + "',"
									+ "'" + job + "',"
									+ "'" + sheet.getCell(9,rowi).getContents() + "',"
									+ "'2',"
									+ "'" + entryDate + "'"
									+ ")";
							sqls.add(sql);
						}
					}
				}
				
				Statement stmt;
				stmt = conn.createStatement();


				for(String sql : sqls) {
					stmt.addBatch(sql);
				}
		
				stmt.executeBatch();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void test1() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement("SELECT * FROM NBPT_SP_REGION where NBPT_SP_REGION_RESPONSIBLER <> '' ");
		rs = ps.executeQuery();
		List<NBPT_SP_REGION> resultList = XZQX_create.rsToBean(NBPT_SP_REGION.class, rs);
		
		try {
			
			List<String> sqls = new ArrayList<>();
			for(NBPT_SP_REGION region : resultList) {
				
				String sql =  "UPDATE NBPT_SP_PERSON SET NBPT_SP_PERSON_DEPT_ID = '" + region.getNBPT_SP_REGION_ID().substring(0, 2) + "' "
							+ "WHERE NBPT_SP_PERSON_PID = '" + region.getNBPT_SP_REGION_RESPONSIBLER() + "'";
				sqls.add(sql);
			}
			Statement stmt;
			stmt = conn.createStatement();


			for(String sql : sqls) {
				stmt.addBatch(sql);
			}
	
			stmt.executeBatch();
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	private void responsebleAdd() throws SQLException, ClassNotFoundException {
		
		String fileNameTemp = GlobalParams.FILE_PATH + "diquxzqx.xls";
		File targetFile = new File(fileNameTemp);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement("SELECT * FROM NBPT_SP_PERSON where NBPT_SP_PERSON_JOB = '22' ORDER BY NBPT_SP_PERSON_ID");
		
		rs = ps.executeQuery();
		List<NBPT_SP_PERSON> resultList = XZQX_create.rsToBean(NBPT_SP_PERSON.class, rs);
		

		List<String> sqls = new ArrayList<String>();
		for(NBPT_SP_PERSON pojo : resultList) {
			
			String sql =  "UPDATE NBPT_SP_REGION SET NBPT_SP_REGION_RESPONSIBLER = '" + pojo.getNBPT_SP_PERSON_PID() + "' "
					+ "WHERE NBPT_SP_REGION_UID = '" + pojo.getNBPT_SP_PERSON_DEPT_ID() + "'";
			sqls.add(sql);
			
		}
		Statement stmt;
		stmt = conn.createStatement();


		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();

//		try {
//			
//			if(!targetFile.exists()) {
//				
//			} else {
//
//				List<String> sqls = new ArrayList<String>();
//				List<String> names = new ArrayList<String>();
//				// 如果存在,获取工作簿
//				Workbook book = null;
//				book = Workbook.getWorkbook(targetFile);
//				
//				// 获取sheet页
//				int maxSheet = book.getNumberOfSheets();
//				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
//					
//					// 获取sheet页
//					Sheet sheet = book.getSheet(sheeti); 
//					
//					// 获取行数
//					int realRows = sheet.getRows();
//					
//					// 初始化
//					for(int rowi = 1; rowi < realRows; rowi++) {
//
//
//						if(names.contains(sheet.getCell(7,rowi).getContents() + sheet.getCell(8,rowi).getContents().substring(0,2) + sheet.getCell(6,rowi).getContents())) {
//							
//						}else {
//							
//
//							String pid = "";
//							for(NBPT_SP_PERSON pojo : resultList) {
//
//								if(sheet.getCell(5,rowi).getContents().toString().trim().equals(pojo.getNBPT_SP_PERSON_NAME().trim())) {
//									pid = pojo.getNBPT_SP_PERSON_PID();
//									
//									continue;
//								}
//							}
//							
//							if("".equals(pid)) {
//								
//							}else {
//								names.add(sheet.getCell(7,rowi).getContents() + sheet.getCell(8,rowi).getContents().substring(0,2) + sheet.getCell(6,rowi).getContents());
//								String sql =  "UPDATE NBPT_SP_REGION SET NBPT_SP_REGION_RESPONSIBLER = '" + pid + "' "
//											+ "WHERE NBPT_SP_REGION_ID = '" + sheet.getCell(7,rowi).getContents() + sheet.getCell(8,rowi).getContents().substring(0,2) + sheet.getCell(6,rowi).getContents() + "'";
//								sqls.add(sql);
//								
//							}
//						}
//					}
//					
//					Statement stmt;
//					stmt = conn.createStatement();
//		
//		
//					for(String sql : sqls) {
//						stmt.addBatch(sql);
//					}
//			
//					stmt.executeBatch();
//				}
//			}
//		}catch (Exception e) {
//			
//			e.printStackTrace();
//		}
		
	}
	private void checkResponseble() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "need.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		

		PreparedStatement ps = null;
				ResultSet rs = null;
		ps = conn.prepareStatement("SELECT * FROM NBPT_SP_PERSON ORDER BY NBPT_SP_PERSON_ID");
		rs = ps.executeQuery();
		List<NBPT_SP_PERSON> resultList = XZQX_create.rsToBean(NBPT_SP_PERSON.class, rs);
		
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				List<String> sqls = new ArrayList<String>();
				List<String> names = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {

						String pid = "";
						if("待定".equals(sheet.getCell(3,rowi).getContents().toString().trim())) {
							
						}
						else {
							
							for(NBPT_SP_PERSON person : resultList) {
								
								if(sheet.getCell(3,rowi).getContents().toString().trim().equals(person.getNBPT_SP_PERSON_NAME().trim())) {
									
									pid = person.getNBPT_SP_PERSON_PID();
									break;
								}
							}
							String sql =  "UPDATE NBPT_SP_REGION SET NBPT_SP_REGION_RESPONSIBLER = '" + pid + "' "
										+ "WHERE NBPT_SP_REGION_ID = '" + sheet.getCell(5,rowi).getContents() + sheet.getCell(6,rowi).getContents().substring(0,2) + sheet.getCell(7,rowi).getContents() + "'";
							sqls.add(sql);
						}
					}

					
					Statement stmt;
					stmt = conn.createStatement();
		
		
					for(String sql : sqls) {
						stmt.addBatch(sql);
					}
			
					stmt.executeBatch();
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private void addArea_Xzqxhf() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "diquxzqx.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		

		PreparedStatement ps = null;
				ResultSet rs = null;
		ps = conn.prepareStatement("SELECT * FROM NBPT_COMMON_XZQXHF ORDER BY NBPT_COMMON_XZQXHF_ID");
		rs = ps.executeQuery();
		List<XZQXPOJO> resultList = XZQX_create.rsToBean(XZQXPOJO.class, rs);
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				List<String> sqls = new ArrayList<String>();
				List<String> notExists = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {

						String xzqxid = "";
						for(XZQXPOJO pojo : resultList) {
							
							if(sheet.getCell(3,rowi).getContents().toString().trim().equals(pojo.getNBPT_COMMON_XZQXHF_NAME().trim())) {
								
								if(sheet.getCell(8,rowi).getContents().substring(0, 2).equals(pojo.getNBPT_COMMON_XZQXHF_ID().trim().substring(0, 2))) {

									xzqxid = pojo.getNBPT_COMMON_XZQXHF_ID();
									continue;
								}
							}
						}
						if(xzqxid.equals("")) {
							
							notExists.add(sheet.getCell(3,rowi).getContents().toString().trim());
						} else {
							
							String sql = "insert NBPT_SP_REGION_XZQX values ('" + CommonUtil.getUUID_32() + "', "
										+"'" + sheet.getCell(7,rowi).getContents() + sheet.getCell(8,rowi).getContents().substring(0,2) + sheet.getCell(6,rowi).getContents() + "', "
										+"'" + xzqxid + "', "
										+"'22') ";
							
							sqls.add(sql);							
						}
					}

					
					Statement stmt;
					stmt = conn.createStatement();
		
		
					for(String sql : sqls) {
						stmt.addBatch(sql);
					}

					for(String sql : notExists) {
						System.out.println(sql);
					}
			
					stmt.executeBatch();
				}
			}
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	private void addRegins_XZQXHF() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "diqhf.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				List<String> sqls = new ArrayList<String>();
				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {
						
						String sql = "insert NBPT_SP_REGION_XZQX values ('" + CommonUtil.getUUID_32() + "', "
									+"'" + sheet.getCell(7,rowi).getContents() + "', "
									+"'" + sheet.getCell(5,rowi).getContents() + "', "
									+"'21') ";
						sqls.add(sql);
						
					}
				}
		
				Statement stmt;
				stmt = conn.createStatement();
	
	
				for(String sql : sqls) {
					stmt.addBatch(sql);
				}
		
				stmt.executeBatch();
			}
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void change() throws SQLException, ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		List<String> sqls = new ArrayList<String>();
		for(int i=0; i< 277; i++) {

			String sql = "UPDATE NBPT_SP_REGION SET NBPT_SP_REGION_ID = '" + String.valueOf( 10000 + i ) +"' WHERE NBPT_SP_REGION_RESPONSIBLER = '" + String.valueOf(100000 + i + 1) + "'";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
	
	
		for(String sql : sqls) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
		
	}
	
	private void addDiZong() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "need.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		
		try {
					
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);

				List<NBPT_SP_PERSON> persons = new ArrayList<NBPT_SP_PERSON>();
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 2; rowi < realRows; rowi++) {
						
						if("".equals(sheet.getCell(3,rowi).getContents().replaceAll(" ", ""))) {
							
							continue;
						}
						
						NBPT_SP_PERSON person = new NBPT_SP_PERSON();
						
						// NBPT_SP_PERSON_PID,32位ID
						person.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
						
						// NBPT_SP_PERSON_ID
						person.setNBPT_SP_PERSON_ID(String.valueOf(100000 + rowi - 2));
						
						// NBPT_SP_PERSON_DEPT_ID 部门ID
						person.setNBPT_SP_PERSON_DEPT_ID(sheet.getCell(5,rowi).getContents() + sheet.getCell(6,rowi).getContents().substring(0, 2) + sheet.getCell(7,rowi).getContents());
						
						// NBPT_SP_PERSON_TYPE 销售类别,这里统一OTC
						person.setNBPT_SP_PERSON_TYPE("2");
						
						// NBPT_SP_PERSON_NAME 姓名
						person.setNBPT_SP_PERSON_NAME(sheet.getCell(3,rowi).getContents().replaceAll(" ", ""));
						
						// NBPT_SP_PERSON_MALE 性别
						person.setNBPT_SP_PERSON_MALE(sheet.getCell(9,rowi).getContents().equals("男") ? "1" : "0");
						
						// 身份证号码
						String idnum = sheet.getCell(23,rowi).getContents();
						
						// NBPT_SP_PERSON_BIRS 生日
						person.setNBPT_SP_PERSON_BIRS(idnum.length() == 18 ? idnum.substring(6, 14) : "19" + idnum.substring(6, 12));
						
						// NBPT_SP_PERSON_IDNUM 身份证
						person.setNBPT_SP_PERSON_IDNUM(idnum);
						
						// NBPT_SP_PERSON_MOB1 联系电话
						person.setNBPT_SP_PERSON_MOB1(sheet.getCell(24,rowi).getContents());
						
						// NBPT_SP_PERSON_MOB2 紧急电话
						person.setNBPT_SP_PERSON_MOB2(sheet.getCell(28,rowi).getContents());
						
						// NBPT_SP_PERSON_ENTRYDATA 入职时间
						
						
						if (sheet.getCell(20,rowi).getType() == CellType.DATE) { 
					         DateCell dc = (DateCell) sheet.getCell(20,rowi);
					         person.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.dateToYYYYMMDD(dc.getDate())); 
						}
						
						// NBPT_SP_PERSON_DEGREE 学历
						person.setNBPT_SP_PERSON_DEGREE(sheet.getCell(14,rowi).getContents());
						
						// NBPT_SP_PERSON_PLACE 籍贯
						person.setNBPT_SP_PERSON_PLACE(sheet.getCell(22,rowi).getContents());
						
						// NBPT_SP_PERSON_SCHOOL 毕业学校
						person.setNBPT_SP_PERSON_SCHOOL(sheet.getCell(15,rowi).getContents());
						
						// NBPT_SP_PERSON_PROFESS 专业
						person.setNBPT_SP_PERSON_PROFESS(sheet.getCell(16,rowi).getContents());
						
						// NBPT_SP_PERSON_TITLE 职称
						person.setNBPT_SP_PERSON_PROFESS(sheet.getCell(18,rowi).getContents());

						if("管理人员".equals(sheet.getCell(13,rowi).getContents())) {
							
							person.setNBPT_SP_PERSON_JOB("26");
						} else {

							// NBPT_SP_PERSON_JOB 职务, 统一地总
							person.setNBPT_SP_PERSON_JOB("22");
						}
						
						// NBPT_SP_PERSON_POLICYNO 保单编号
						// NBPT_SP_PERSON_POLICY_DATA1 保单起始时间
						// NBPT_SP_PERSON_POLICY_DATA2 保单结束时间
						// NBPT_SP_PERSON_AREANO 管理区域
						// NBPT_SP_PERSON_LOGINID 登录ID
						person.setNBPT_SP_PERSON_LOGINID(sheet.getCell(8,rowi).getContents());
						
						// NBPT_SP_PERSON_NOTE备注
						// NBPT_SP_PERSON_FLAG离职标志
						person.setNBPT_SP_PERSON_FLAG("0");
						
						// NBPT_SP_PERSON_POLICYTYPE 保单种类
						
						persons.add(person);
					}
				}
				this.dzxxUpdate(persons, conn);
			}
			conn.close();
		
		}catch (Exception e) {
					
					e.printStackTrace();
		}
	}
	
	private void dzxxUpdate(List<NBPT_SP_PERSON> persons, Connection conn) throws SQLException {
		
		List<String> sqls = new ArrayList<String>();
		List<String> sql2s = new ArrayList<String>();
		for(NBPT_SP_PERSON person : persons) {
			
			String sql2 = "UPDATE NBPT_SP_REGION SET NBPT_SP_REGION_RESPONSIBLER = '" + person.getNBPT_SP_PERSON_PID() + "' "
						+ "WHERE NBPT_SP_REGION_ID = '" + person.getNBPT_SP_PERSON_DEPT_ID() + "'";
			sql2s.add(sql2);
			String sql = 
					"INSERT INTO NBPT_SP_PERSON "+
					"("+ 
					"NBPT_SP_PERSON_PID," + 
					"NBPT_SP_PERSON_ID," + 
					"NBPT_SP_PERSON_DEPT_ID," + 
					"NBPT_SP_PERSON_TYPE," + 
					"NBPT_SP_PERSON_NAME," + 
					"NBPT_SP_PERSON_MALE," + 
					"NBPT_SP_PERSON_BIRS," + 
					"NBPT_SP_PERSON_IDNUM," + 
					"NBPT_SP_PERSON_MOB1," + 
					"NBPT_SP_PERSON_MOB2," + 
					"NBPT_SP_PERSON_QQ," + 
					"NBPT_SP_PERSON_CHAT," + 
					"NBPT_SP_PERSON_MAIL," + 
					"NBPT_SP_PERSON_ENTRYDATA," + 
					"NBPT_SP_PERSON_LEAVEDATA," + 
					"NBPT_SP_PERSON_DEGREE," + 
					"NBPT_SP_PERSON_PLACE," + 
					"NBPT_SP_PERSON_SCHOOL," + 
					"NBPT_SP_PERSON_PROFESS," + 
					"NBPT_SP_PERSON_TITLE," + 
					"NBPT_SP_PERSON_JOB," + 
					"NBPT_SP_PERSON_POLICYNO," + 
					"NBPT_SP_PERSON_POLICY_DATA1," + 
					"NBPT_SP_PERSON_POLICY_DATA2," + 
					"NBPT_SP_PERSON_AREANO," + 
					"NBPT_SP_PERSON_LOGINID," + 
					"NBPT_SP_PERSON_NOTE," + 
					"NBPT_SP_PERSON_FLAG," + 
					"NBPT_SP_PERSON_POLICYTYPE)"+ 
					" VALUES (" +
					"'" + person.getNBPT_SP_PERSON_PID() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_ID() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_DEPT_ID() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_TYPE() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_NAME() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_MALE() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_BIRS() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_IDNUM() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_MOB1() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_MOB2() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_QQ() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_CHAT() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_MAIL() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_ENTRYDATA() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_LEAVEDATA() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_DEGREE() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_PLACE() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_SCHOOL() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_PROFESS() + "', "+ 
					"'" + person.getNBPT_SP_PERSON_TITLE() + "', "+  
					"'" + person.getNBPT_SP_PERSON_JOB() + "', "+  
					"'" + person.getNBPT_SP_PERSON_POLICYNO() + "', "+  
					"'" + person.getNBPT_SP_PERSON_POLICY_DATA1() + "', "+  
					"'" + person.getNBPT_SP_PERSON_POLICY_DATA2() + "', "+  
					"'" + person.getNBPT_SP_PERSON_AREANO() + "', "+  
					"'" + person.getNBPT_SP_PERSON_LOGINID() + "', "+  
					"'" + person.getNBPT_SP_PERSON_NOTE() + "', "+  
					"'" + person.getNBPT_SP_PERSON_FLAG() + "', "+  
					"'" + person.getNBPT_SP_PERSON_POLICYTYPE() + "' "+ 
					")";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
	
	
		for(String sql : sqls) {
			stmt.addBatch(sql);
		}
		
		for(String sql : sql2s) {
			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	
	
	
	private void addAreas() throws ClassNotFoundException, SQLException {
		String fileNameTemp = GlobalParams.FILE_PATH + "need.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		
		try {
					
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);

				List<DQHFPOJO> provinces = new ArrayList<DQHFPOJO>();
				
				List<String> checkItems = new ArrayList<String>();

				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化
					for(int rowi = 1; rowi < realRows; rowi++) {
						
						if(checkItems.contains(sheet.getCell(2,rowi).getContents().trim())) {
							
							System.out.println(sheet.getCell(2,rowi).getContents().trim() + ":" + realRows);
						} else {

							checkItems.add(sheet.getCell(2,rowi).getContents().trim());
							DQHFPOJO dahfpojo = new DQHFPOJO();
							dahfpojo.setId(sheet.getCell(5,rowi).getContents() + sheet.getCell(6,rowi).getContents().substring(0,2) + sheet.getCell(7,rowi).getContents());
							dahfpojo.setName(sheet.getCell(2,rowi).getContents());
							dahfpojo.setNeed(sheet.getCell(4,rowi).getContents());
							provinces.add(dahfpojo);
						}
					}
				}
				this.diqhfUpdate(provinces, conn);
			}
			conn.close();
		
		}catch (Exception e) {
					
					e.printStackTrace();
		}
		
	}
	private void diqhfUpdate(List<DQHFPOJO> areas, Connection conn) throws SQLException {
		List<String> sqls = new ArrayList<String>();
		for(DQHFPOJO area : areas) {
			
			String sql = 
					"INSERT NBPT_SP_REGION (NBPT_SP_REGION_UID, NBPT_SP_REGION_ID, NBPT_SP_REGION_NAME, NBPT_SP_REGION_LEVEL, NBPT_SP_REGION_ONAME)"
					+ " VALUES ("
							+ "'" + CommonUtil.getUUID_32() + "', "
							+ "'" + area.getId() + "', "
							+ "'" + area.getName() + "', "
							+ "'2',"
							+ "'" + area.getNeed() + "')";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
	
	
		for(String sql : sqls) {

			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	
	private void addRegions() throws ClassNotFoundException, SQLException {
		// 读取文件
				String fileNameTemp = GlobalParams.FILE_PATH + "dqhf.xls";
				File targetFile = new File(fileNameTemp);

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

				Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
				
				try {
							
					if(!targetFile.exists()) {
						
					} else {

						// 如果存在,获取工作簿
						Workbook book = null;
						book = Workbook.getWorkbook(targetFile);

						List<DQHFPOJO> provinces = new ArrayList<DQHFPOJO>();
						// 获取sheet页
						int maxSheet = book.getNumberOfSheets();
						for(int sheeti = 0; sheeti < maxSheet; sheeti++){
							
							// 获取sheet页
							Sheet sheet = book.getSheet(sheeti); 
							
							// 获取行数
							int realRows = sheet.getRows();
							
							// 初始化

							for(int rowi = 0; rowi < realRows; rowi++) {
								
								DQHFPOJO dahfpojo = new DQHFPOJO();
								dahfpojo.setId(sheet.getCell(1,rowi).getContents());
								dahfpojo.setName(sheet.getCell(0,rowi).getContents());
								provinces.add(dahfpojo);
							}
						}
						this.dqhfUpdate(provinces, conn);
					}
					conn.close();
				
				}catch (Exception e) {
							
							e.printStackTrace();
				}
	}
	
	private void dqhfUpdate(List<DQHFPOJO> provinces, Connection conn) throws SQLException {
		List<String> sqls = new ArrayList<String>();
		for(DQHFPOJO xzqxpojo : provinces) {
			
			String sql = 
					"INSERT INTO NBPT_SP_REGION (NBPT_SP_REGION_ID, NBPT_SP_REGION_RID, NBPT_SP_REGION_NAME, NBPT_SP_REGION_LEVEL)"
					+ " VALUES ("
							+ "'" + CommonUtil.getUUID_32() + "', "
							+ "'" + xzqxpojo.getId() + "', "
							+ "'" + xzqxpojo.getName() + "', "
							+ "'1'"
							+ ")";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
	
	
		for(String sql : sqls) {

			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	private void addXZQXHF() throws SQLException, ClassNotFoundException {
		
		// 读取文件
		String fileNameTemp = GlobalParams.FILE_PATH + "xzqx.xls";
		File targetFile = new File(fileNameTemp);

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		Connection conn = DriverManager.getConnection(GlobalParams.DBBASE_URL + "cwbase1",GlobalParams.COMMON_USERNAME, GlobalParams.COMMON_PASSWORD);
		
		try {
					
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);

				List<XZQXPOJO> provinces = new ArrayList<XZQXPOJO>();
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();
					
					// 初始化

					for(int rowi = 0; rowi < realRows; rowi++) {
						
						XZQXPOJO xzqxpojo = new XZQXPOJO();
						xzqxpojo.setNBPT_COMMON_XZQXHF_ID(sheet.getCell(0,rowi).getContents());
						xzqxpojo.setNBPT_COMMON_XZQXHF_NAME(sheet.getCell(1,rowi).getContents());
						xzqxpojo.setNBPT_COMMON_XZQXHF_LEVEL(sheet.getCell(2,rowi).getContents());
						xzqxpojo.setNBPT_COMMON_XZQXHF_PID(sheet.getCell(3,rowi).getContents());
						provinces.add(xzqxpojo);
					}
				}
				this.update(provinces, conn);
			}
		conn.close();
		
		}catch (Exception e) {
					
					e.printStackTrace();
		}
	}
	
	private void update(List<XZQXPOJO> provinces, Connection conn) throws SQLException {
		
		List<String> sqls = new ArrayList<String>();
		for(XZQXPOJO xzqxpojo : provinces) {
			
			String sql = 
					"INSERT INTO NBPT_COMMON_XZQXHF (NBPT_COMMON_XZQXHF_ID, NBPT_COMMON_XZQXHF_PID, NBPT_COMMON_XZQXHF_NAME, NBPT_COMMON_XZQXHF_LEVEL)"
					+ "VALUES ("
							+ "'" + xzqxpojo.getNBPT_COMMON_XZQXHF_ID() + "', "
							+ "'" + xzqxpojo.getNBPT_COMMON_XZQXHF_PID() + "', "
							+ "'" + xzqxpojo.getNBPT_COMMON_XZQXHF_NAME() + "', "
							+ "'" + xzqxpojo.getNBPT_COMMON_XZQXHF_LEVEL() + "'"
							+ ")";
			sqls.add(sql);
		}
		
		Statement stmt;
		stmt = conn.createStatement();
	
	
		for(String sql : sqls) {

			stmt.addBatch(sql);
		}

		stmt.executeBatch();
	}
	
	private static <T> List<T> rsToBean(Class<T> classz, ResultSet rs) throws SQLException {

		// 初始化返回数据
		List<T> list = new ArrayList<T>();

		// 获取表的列名
		List<String> columnNames = new ArrayList<String>();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {

			columnNames.add(rsMetaData.getColumnName(i).toUpperCase());
		}

		while (rs.next()) {

			T entity = null;

			try {
				entity = classz.newInstance();

				Field[] fs = entity.getClass().getDeclaredFields();

				for (Field f : fs) {

					if (columnNames.contains(f.getName())) {

						// 如果属性包含该属性,则添加
						boolean flag = f.isAccessible();
						f.setAccessible(true);
						f.set(entity, String.valueOf(rs.getObject(f.getName())));
						f.setAccessible(flag);
					}
				}
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
			list.add(entity);
		}

		return list;
	}
	
}
