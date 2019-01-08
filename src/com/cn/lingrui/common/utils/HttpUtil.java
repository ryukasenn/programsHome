package com.cn.lingrui.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.servlet.ModelAndView;


import net.sf.json.JSONObject;

public class HttpUtil {

	/**
	 * 发送get请求
	 * @return
	 * @throws Exception 
	 */
	public static String get(String url) throws Exception {
		
		// 初始化连接uri
		URI uri = new URI(url);
		SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
		ClientHttpRequest req = schr.createRequest(uri, HttpMethod.GET);
		ClientHttpResponse res = req.execute(); 
		
		// 获取请求结果
		InputStream ins = res.getBody();
		InputStreamReader isr = new InputStreamReader(ins);
		BufferedReader br = new BufferedReader(isr);
		
		// 解析请求结果内容
		String str = br.readLine();
		

		// 释放资源
		ins.close();
		isr.close();
		br.close();
		
		// 如果存在,则返回结果
		if(str != null) {
			return str;
		}		
		// 如果不存在,则返回空
		return "";
	}
	
	/**
	 * 发送post请求
	 * @return
	 * @throws Exception 
	 */
	public static String post(String url, String param) throws Exception {
		
		// 构造访问连接
    	URI uri = new URI(url);
		SimpleClientHttpRequestFactory schr = new SimpleClientHttpRequestFactory();
		
		// 设定访问类型
		ClientHttpRequest req = schr.createRequest(uri, HttpMethod.POST);
		
		// 设定访问参数
		OutputStreamWriter out = new OutputStreamWriter(req.getBody(), "utf-8");
		out.write(param);
		out.flush();
		// 执行访问
		ClientHttpResponse res = req.execute(); 
		
		// 获取访问结果
		// 获取请求结果
		InputStream ins = res.getBody();
		InputStreamReader isr = new InputStreamReader(ins, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		
		// 解析请求结果内容
		String str = br.readLine();
		
		// 释放资源
		out.close();
		ins.close();
		isr.close();
		br.close();
		
		// 如果存在,则返回结果
		if(str != null) {
			return str;
		}		
		// 如果不存在,则返回空
		return "";
	}
	/**
	 * 直接返回get请求的json结果
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getJson(String url) throws Exception {

		String req = get(url);
		if ("".equals(req))
				return null;
		return JSONObject.fromObject(req);		
	}
	
	/**
	 * 获取前台传递model
	 * @param path 返回页面路径
	 * @param title 页面标题
	 * @return
	 */
	public static ModelAndView getModelAndView(String path, String... title) {
		ModelAndView mv = new ModelAndView(path);
		// 定义静态资源路径
		mv.addObject("myPath", CommonUtil.getBasePropertieValue("BASE_URL"));
		mv.addObject("baseUrl", CommonUtil.getBasePropertieValue("BASE_URL"));
		mv.addObject("randomJsVersion", "?v=" + CommonUtil.getUUID_32());
		if(title.length != 0) {

			mv.addObject("title", title[0]);
		}
		return mv;
	}
	
	/**
	 * 获取前台传递model,非过滤器页面
	 * @param path 返回页面路径
	 * @param title 页面标题
	 * @return
	 */
	public static ModelAndView getUncheckModelAndView(String path) {
		ModelAndView mv = new ModelAndView(path);
		// 定义静态资源路径
		mv.addObject("randomJsVersion", "?v=" + CommonUtil.getUUID_32());
		mv.addObject("myPath", CommonUtil.getBasePropertieValue("BASE_URL"));
		mv.addObject("baseUrl", CommonUtil.getBasePropertieValue("BASE_URL"));
		return mv;
	}
	
	/**
	 * 返回post请求的json结果
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject postJson(String url, String param) throws Exception {

		String req = post(url,param);
		if ("".equals(req))
				return null;
		return JSONObject.fromObject(req);		
	}
	
	public static ModelAndView commonInfDeal(ModelAndView mv) {
		
		return null;
	}

	/**
	 * 转化cookie为map
	 * 
	 * @param req
	 * @return
	 */
	public static Map<String, Cookie> readCookie(HttpServletRequest req) {

		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = req.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;

	}

	/**
	 * 获取cookie值
	 * 
	 * @param req
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest req, String key) {

		Cookie cookie = readCookie(req).get(key);
		
		return null == cookie ? "" : readCookie(req).get(key).getValue();
	}
	
	public static String decodeToken(String token) throws UnsupportedEncodingException {
		
		if(null == token || "".equals(token)) {
			return "";
		}
		String CHARSET = "UTF-8";
		String ltpaDominoSecret="YZLGw22dlpZukZmTb5plaGyWymE=";
		byte[] dominoSecret = Base64.decodeBase64(ltpaDominoSecret.getBytes());         			
		String preltpaToken= token;
		StringBuffer sb=new StringBuffer(preltpaToken);

		switch(preltpaToken.length()%4) {  
				  case 3:  
						 sb.append("==="); break; // 注：其实只需要补充一个或者两个等号，不存在补充三个等号的情况  
				  case 2:  
						 sb.append("=="); break;  
				  case 1:  
						 sb.append("="); break;  
				  default:  
				}  


	   String newltpaToken = sb.toString();							
		
	   //System.out.println(newltpaToken);
	   //System.out.println(newltpaToken.length());
	   byte[] ltpa =Base64.decodeBase64(newltpaToken.getBytes());
	   //System.out.println("ltpa:"+ltpa);
	   ByteArrayInputStream stream = new ByteArrayInputStream(ltpa); 
	   int usernameLength = ltpa.length - 40;
	   byte header[] = new byte[4];
	   byte creation[] = new byte[8];
	   byte expires[] = new byte[8];
	   byte username[] = new byte[usernameLength];
	   byte[] sha = new byte[20];
	  
	   stream.read(header, 0, 4); 
	   // 读取LTPAToken版本号  14stream.read(header, 0, 4);
	   if (header[0] != 0 || header[1] != 1 || header[2] != 2|| header[3] != 3)
		           throw new IllegalArgumentException("Invalid ltpaToken format"); 
//						       // 读取开始时间   
	   stream.read(creation, 0, 8);
//						      // 读取到期时间   
	   stream.read(expires, 0, 8);  
//						 // 读取Domino用户DN   
	   stream.read(username, 0, usernameLength);  
//						   // 读取SHA校验和    
	   stream.read(sha, 0, 20);
		
	   return new String(username);
	}

}
