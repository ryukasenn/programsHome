package com.cn.lingrui.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;



public class CommonUtil {

	/**
	 * 异常信息输出
	 * 
	 * @return
	 */
	public static String getTraceInfo() {

		StringBuffer stringBuffer = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		stringBuffer.append("Exception in [class: ").append(stacks[1].getClassName()).append("][method: ")
				.append(stacks[1].getMethodName()).append("][line: ").append(stacks[1].getLineNumber() + "]");
		return stringBuffer.toString();
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param checkString
	 * @return 如果为空返回true
	 */
	public static Boolean isEmpty(String checkString) {

		if (null == checkString || "".equals(checkString.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断数组中是否包含某值
	 * 
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean checkArrayContain(String[] arr, String targetValue) {

		for (String s : arr) {
			if (s.equals(targetValue))
				return true;
		}
		return false;
	}

	public static boolean checkArrayContain(Integer[] arr, Integer targetValue) {

		for (Integer s : arr) {
			if (s.equals(targetValue))
				return true;
		}
		return false;
	}

	/**
	 * 减法运算
	 * 
	 * @param A
	 * @param B
	 * @return A-B
	 */
	public static BigDecimal subtract(String A, String B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B);

		return a.subtract(b);
	}

	/**
	 * 减法运算
	 * 
	 * @param A
	 * @param B
	 * @return A-B
	 */
	public static BigDecimal subtract(Double A, Double B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B.toString());

		return a.subtract(b);
	}

	/**
	 * 减法运算
	 * 
	 * @param A
	 * @param B
	 * @return A-B
	 */
	public static BigDecimal subtract(String A, Double B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B.toString());

		return a.subtract(b);
	}

	/**
	 * 减法运算
	 * 
	 * @param A
	 * @param B
	 * @return A-B
	 */
	public static BigDecimal subtract(Double A, String B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B);

		return a.subtract(b);
	}

	/**
	 * 加法运算
	 * 
	 * @param A
	 * @param B
	 * @return A+B
	 */
	public static BigDecimal add(String A, String B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B);
		return a.add(b);
	}

	/**
	 * 加法运算
	 * 
	 * @param A
	 * @param B
	 * @return A+B
	 */
	public static BigDecimal add(Double A, Double B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B.toString());
		return a.add(b);
	}

	/**
	 * 加法运算
	 * 
	 * @param A
	 * @param B
	 * @return A+B
	 */
	public static BigDecimal add(Double A, String B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B);
		return a.add(b);
	}

	/**
	 * 加法运算
	 * 
	 * @param A
	 * @param B
	 * @return A+B
	 */
	public static BigDecimal add(String A, Double B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B.toString());
		return a.add(b);
	}

	/**
	 * 比较两数大小-1(A&lt;B) 0(A=B) 1(A&gt;B)
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static int compare(String A, String B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B);
		return a.compareTo(b);
	}

	/**
	 * 比较两数大小-1(A&lt;B) 0(A=B) 1(A&gt;B)
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static int compare(Double A, Double B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B.toString());
		return a.compareTo(b);
	}

	/**
	 * 比较两数大小-1(A&lt;B) 0(A=B) 1(A&gt;B)
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static int compare(String A, Double B) {

		BigDecimal a = new BigDecimal(A);
		BigDecimal b = new BigDecimal(B.toString());
		return a.compareTo(b);
	}

	/**
	 * 比较两数大小-1(A&lt;B) 0(A=B) 1(A&gt;B)
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static int compare(Double A, String B) {

		BigDecimal a = new BigDecimal(A.toString());
		BigDecimal b = new BigDecimal(B);
		return a.compareTo(b);
	}

	public static String getYYYYMMDD() {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		return df.format(new Date());
	}
	
	public static String getYYYY_MM_DD() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		return df.format(new Date());
	}
	

	public static String getYYYYMMDDHHMMSS() {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		return df.format(new Date());
	}
	
	/**
	 * 日期转换YYYYMMDD格式
	 * @param date
	 * @return
	 */
	public static String dateToYYYYMMDD(Date date) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	
	
	
	
	/**
	 * 浪潮系统密码解析
	 * @param mima
	 * @return
	 */
	public static String decodeRs(String mima) {

		// 截取第一位和最后一位
		char firstChar = mima.charAt(0);
		char lastChar = mima.charAt(mima.length() - 1);

		// 转化asc
		int firstAsc = (int) firstChar;
		int lastAsc = (int) lastChar;

		// 求差值
		int vicz = lastAsc - firstAsc;

		// 截取后的密码
		String mimaJiequHou = mima.substring(1, mima.length() - 1);

		int vinum = 0;

		// 初始化解密密码
		String vspass = "";

		// 进行解密运算
		for (int i = mimaJiequHou.length(); i >= 1; i -= 2) {

			vinum += 1;

			char first = 'a';
			char sec = 'a';
			if (vinum / 2 != (vinum + 1) / 2) {
				first = mimaJiequHou.charAt(i - 2);
				sec = mimaJiequHou.charAt(i - 1);
			} else {
				first = mimaJiequHou.charAt(i - 1);
				sec = mimaJiequHou.charAt(i - 2);
			}
			char temp = (char) (((int) first - firstAsc) * 26 + sec - 97 - vicz);
			vspass += temp;
		}

		return vspass;
	}
	
	/**
	 * 浪潮系统密码加密
	 * @param mima
	 * @return
	 */
	public static String encode(String mima) {

		char head = CommonUtil.getRandomLetter(0);
		char foot = CommonUtil.getRandomLetter(0);
		
		List<Character> ss = new ArrayList<>();
		ss.add(foot);
		int vinum = 0;
		for(int i = 0; i < mima.length(); i++) {

			vinum += 1;
			char currentLetter = mima.charAt(i);

//			char temp = (char) (((int) first - firstAsc) * 26 + sec - 97 - vicz);
			
			int indexNum = CommonUtil.getLetterIndex(head);
			
			char first = CommonUtil.getLetter(indexNum + new Random().nextInt(5));
			
//			System.out.println(sec);
			int sec = (int) currentLetter - ((int) first - (int)head) * 26 + 97 + ((int)foot - (int)head) ;
//			System.out.println((char) first);
			

			if (vinum / 2 != (vinum + 1) / 2) {

				ss.add((char)sec);
				ss.add(first);
			} else {
				ss.add(first);
				ss.add((char)sec);
			}
		}
		ss.add(head);
		Collections.reverse(ss);
		String code = "";
		for(char s : ss) {
			
			code += s;
		}
		return code;
	}
	
	/**
	 * 获取异常信息
	 * @param t
	 * @return
	 */
	public static String getTrace(Throwable t) {   
		StringWriter stringWriter= new StringWriter();   
		PrintWriter writer= new PrintWriter(stringWriter);   
		t.printStackTrace(writer);   
		StringBuffer buffer= stringWriter.getBuffer();   
		return buffer.toString();   
	}
	
	/**
	 * 首字母变大写
	 * @param word
	 * @return
	 */
	public static String upFirstWord(String word) {
		
		StringBuffer sb = new StringBuffer(word.substring(0, 1));
		
		return sb.append(sb).toString();
	}

	/**
	 * 获取32位随机码
	 * @return
	 */
	public static String getUUID_32() {
		
		String rtnVal = Long.toHexString(System.currentTimeMillis());
		rtnVal += UUID.randomUUID();
		rtnVal = rtnVal.replaceAll("-", "");
		return rtnVal.substring(0, 32);
	}
	
	/**
	 * 18位身份证真伪验证
	 * @param idNum
	 * @return
	 */
	public static Boolean idNumCheck(String idNum) {
		 
		if(18 != idNum.length()) {

			return false;
		}
		
		if(-1 != idNum.indexOf(" ")) {

			return false;
		}
		// 分界身份证数字
		String[] nums = idNum.split("");
		String[] checkNums = new String[] {"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"};
		String[] realNums = new String[] {"1","0","X","9","8","7","6","5","4","3","2"};
		Integer addResult = 0;
		for(int i = 0; i < nums.length - 1; i++){
			
			addResult += Integer.valueOf(nums[i]) * Integer.valueOf(checkNums[i]);
		}
		int result = addResult % 11;
		
		// 如果验证结果跟最后一位不匹配
		if (!realNums[result].toUpperCase().equals(nums[17].toUpperCase())){
			
			return false;
		}
		return true;
	}
	
	/**
	 * 读取Properties文件中的配置
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static String readProperties(String fileName, String key) {
		
        Properties pps = new Properties();
        try {
            InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("com/cn/config/" + fileName);;
            pps.load(in);
            String value = pps.getProperty(key);

            in.close();
            return value;
            
        }catch (IOException e) {
            e.printStackTrace();
            return "";
        }
	}
	
	/**
	 * 获取base.Propertie 文件中的值
	 * @param key
	 * @return
	 */
	public static String getBasePropertieValue(String key) {
		
		return readProperties("base.properties", key);
	}
	
	/**
	 * 从数据库取出的数据传到页面
	 * @param date
	 * @return
	 */
	public static String formateTimeToPage(String date) {
		
		return "".equals(date)? getYYYY_MM_DD() : new StringBuffer().append(date.substring(0, 4)).append("-")
																   .append(date.substring(4, 6)).append("-")
																   .append(date.substring(6, 8)).toString();
	}
	
	/**
	 * 从页面传进的数据到数据库
	 * @param date
	 * @return
	 */
	public static String formateTiemToBasic(String date){
		
		return "".equals(date) || null == date? "" : date.replaceAll("-", "").replaceAll("/", "");
	}
	
	/**
	 * 获取两数百分比 befor/after
	 * @param before
	 * @param after
	 * @return
	 */
	public static String getPercenttage(Float before, Float after) {

		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		Float result = before/after;
		return nt.format(result);
	}
	
	/**
	 * 获取两数百分比 befor/after
	 * @param before
	 * @param after
	 * @return
	 */
	public static String getPercenttage(Integer before, Integer after) {

		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		Float result = (float) before/after;
		return nt.format(result);
	}
	
	/**
	 * 字符串转数字
	 * @param obj
	 * @return
	 */
	public static Integer objToInteger(String obj) {
		
		try {
			
			Integer returnValue = Integer.valueOf(obj.trim());
			return returnValue;
		} catch(Exception e) {
			
			return 0;
		}
	}
	
	/**
	 * 获取随机字母
	 * @return
	 */
	public static char getRandomLetter() {

		// 定义随机字母
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return letters.charAt(new Random().nextInt(52));
	}
	

	/**
	 * 获取随机字母
	 * @param type 0:小写字母,非0:大写字母
	 * @return
	 */
	public static char getRandomLetter(int type) {

		String letters = null;
		// 定义随机字母
		if(0 == type) {

			letters = "abcdefghijklmnopqrstuvwxyz";
		} else {
			letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}
		return letters.charAt(new Random().nextInt(26));
	}
	
	/**
	 * 获取固定位置字母
	 * @param type 0:小写字母,非0:大写字母
	 * @return
	 */
	public static char getLetter(int index) {

		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return letters.charAt(index);
	}
	
	/**
	 * 获取字母的位置
	 * @param type 0:小写字母,非0:大写字母
	 * @return
	 */
	public static Integer getLetterIndex(Character s) {

		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return letters.indexOf(s);
	}
	
	/**
	 * 将一个列表,按某个属性值分类成多个列表
	 * @param undealList
	 * @param byKey
	 * @param clasz
	 * @return
	 */
	public static <T> Map<String, List<T>> classify(List<T> undealList, String byKey, Class<T> clasz) throws NoSuchFieldException{
				
		// 返回列表
		Map<String, List<T>> resultMap = new LinkedHashMap<>();
		
		for(T t : undealList ) {
			
			try {
				Field field = t.getClass().getDeclaredField(byKey);
				
				// 如果属性包含该属性,则获取
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				
				String key = (String) field.get(t);
				
				// 如果标志列表中,包含了该标志
				if(resultMap.containsKey(key)) {
					
					// 取得当前list
					List<T> currentList = resultMap.get(key);
					
					// 添加至分类list中
					currentList.add(t);
				} else {

					// 取得当前list
					List<T> currentList = new ArrayList<>();
					
					// 添加至分类list中
					currentList.add(t);
					resultMap.put(key, currentList);
					
				}

				field.setAccessible(flag);
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				
				e.printStackTrace();
			} 
		}
		return resultMap;
	}
	
	/**
	 * 按key值将分类号的列表取出
	 * @param classifyiedList
	 * @param key
	 * @return
	 */
	public static <T> List<T> getListInMapByKey(Map<String, List<T>> classifyiedList, String key){
		
		if(null == classifyiedList.get(key)) {
			return new ArrayList<T>();
		}else {
			
			return classifyiedList.get(key);
		}
	}
}
