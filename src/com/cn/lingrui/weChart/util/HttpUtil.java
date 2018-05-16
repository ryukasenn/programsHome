package com.cn.lingrui.weChart.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.weChart.util.GetSecretKey;
import com.cn.lingrui.weChart.util.WeChatParames;

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
	 * @return
	 */
	public static ModelAndView getModelAndView(String path) {
		ModelAndView mv = new ModelAndView(path);
		// 定义静态资源路径
		mv.addObject("publicPath", "http://ekp.lingrui.com:9000/WechatLR");
		return mv;
	}
	
	/**
	 * 获取用户ID
	 * @param request
	 * @return
	 */
	public static String getOAuth2IC_USERID(HttpServletRequest request) {
		return (String) request.getAttribute(WeChatParames.OAuth2IC_USERID);
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

	/**
	 * 获取企业号用户的详细信息
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject getUserInfo(String userId) throws Exception {
		
		// 调用通讯录接口,通过userId和accessToken获取
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";
		url = url.replace("ACCESS_TOKEN", GetSecretKey.getAccessToken()).replace("USERID", userId);
		
		JSONObject jo = HttpUtil.getJson(url);
				
		return jo;
	}
}
