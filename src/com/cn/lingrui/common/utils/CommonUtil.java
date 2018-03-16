package com.cn.lingrui.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	 * 判断字符串非空
	 * 
	 * @param checkString
	 * @return 如果为空返回false
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
	

	public static String getYYYYMMDDHHMMSS() {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		return df.format(new Date());
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
}
