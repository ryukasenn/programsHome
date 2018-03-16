package com.cn.lingrui.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		mv.addObject("myPath", GlobalParams.BASE_URL);
		if(title.length != 0) {

			mv.addObject("title", title[0]);
		}
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

		return readCookie(req).get(key).getValue();
	}
	
	public static String decodeToken(String token) throws UnsupportedEncodingException {

		String CHARSET = "utf-8";
		String ltpaDominoSecret="YZLGw22dlpZukZmTb5plaGyWymE=";
		byte[] dominoSecret = Base64.decodeBase64(ltpaDominoSecret.getBytes());
		String preltpaToken=token;
		StringBuffer sb=new StringBuffer(preltpaToken);
		if(preltpaToken.length()<64) {
				for(int j=0;j<64-preltpaToken.length();j++) {
		 		sb.append("=");
				}
			}
	   String newltpaToken=token.toString();
	   System.out.println(newltpaToken);
	   byte[] ltpa =Base64.decodeBase64(newltpaToken.getBytes());
	   System.out.println("ltpa:"+ltpa);
	   ByteArrayInputStream stream = new ByteArrayInputStream(ltpa); 
	   int usernameLength = ltpa.length - 40;
	   byte header[] = new byte[4];
	   byte creation[] = new byte[8];
	   byte expires[] = new byte[8];
	   byte username[] = new byte[usernameLength];
	   byte[] sha = new byte[20];
	  
	   stream.read(header, 0, 4); 
	   if (header[0] != 0 || header[1] != 1 || header[2] != 2|| header[3] != 3)
		           throw new IllegalArgumentException("Invalid ltpaToken format"); 
	   stream.read(creation, 0, 8);
	   stream.read(expires, 0, 8);  
	   stream.read(username, 0, usernameLength);  
	   stream.read(sha, 0, 20);
	   char characters[] = new char[usernameLength];  
		   
	    try {   
		    	InputStreamReader isr = new InputStreamReader(  new ByteArrayInputStream(username),  CHARSET); 
		    	isr.read(characters);  
	    	} catch (Exception e) {     } 
    	ByteArrayOutputStream ostream = new ByteArrayOutputStream(); 
    	try {   
			ostream.write(header); 
    		ostream.write(creation); 
    		ostream.write(expires);    
    		ostream.write(username); 
    		ostream.write(dominoSecret);  	
    		ostream.close();   
		} catch (IOException e) {
				throw new RuntimeException(e);
		}  
		MessageDigest md; 
		System.out.println("username"+ new String(username));
		//System.out.println("username"+ new String(username));
		try {   
			md = MessageDigest.getInstance("SHA-1"); 
			md.reset();   
		} catch (NoSuchAlgorithmException e) {         throw new RuntimeException(e);     }   
		byte[] digest = md.digest(ostream.toByteArray()); 
		System.out.println("degest: "+ new String(digest,"utf-8"));
		System.out.println("sha: "+ new String(sha,"utf-8"));
		//boolean valid = MessageDigest.isEqual(digest, sha); 
		
		return new String(username);
	}

}
