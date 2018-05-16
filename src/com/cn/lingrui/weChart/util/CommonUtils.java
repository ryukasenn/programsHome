package com.cn.lingrui.weChart.util;

import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.weChart.util.WeChatParames;

import net.sf.json.JSONObject;

public class CommonUtils {


	private static String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Logger log = LogManager.getLogger();
	/**
	 * 将字符串转化为jsonObj
	 * @param content
	 * @return
	 */
	public static JSONObject string2json(String content) {
		JSONObject jo = JSONObject.fromObject(content);
		return jo;
	}
	
	/**
	 * 获取全局票据,access_token
	 * @param appId
	 * @param appSecret
	 * @throws Exception
	 */
	public static void getAccessToken(String appId, String appSecret) throws Exception {
		
		if(!"".equals(WeChatParames.access_token)) { // 如果票据不为空
			
			// 检验票据生成时间
			Long now_time = Long.valueOf(CommonUtils.getTimeStamp());
			if ((now_time - WeChatParames.access_token_gettime) > Long.valueOf(WeChatParames.access_token_effective * 1000)) {
				// 如果当前时间大于上次的获取时间,并且超过有效时间,则重新获取

				get_token(appId, appSecret);
			} else {		

				// 否则使用当前的缓存值
			}		
		}else {// 如果票据为空,则直接获取

			get_token(appId, appSecret);
		}
	}
	
	private static void get_token(String appId, String appSecret) throws Exception {

		log.info("重新拉取了一次access_token");
		// 初始化请求uri参数
		String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APPID&corpsecret=APPSECRET";
				
		// 确定请求参数
		String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
				
		// 处理返回结果		
		 JSONObject jo = HttpUtil.getJson(requestUrl);
		if("0".equals(jo.getString("errcode")) && "OK".toLowerCase().equals(jo.getString("errmsg").toString().toLowerCase())) {		
			// 进行全局缓存
			WeChatParames.access_token = (String)jo.getString("access_token");
			WeChatParames.access_token_effective = jo.getInt("expires_in");
			WeChatParames.access_token_gettime = Long.valueOf(CommonUtils.getTimeStamp());
		}				
	}
	
public static void getJsapiTicket(String access_token) throws Exception {

		if(!"".equals(WeChatParames.jsapi_ticket)) {
			
			// 如果缓存的ticket不为空
			// 检验票据生成时间
			Long now_time = Long.valueOf(CommonUtils.getTimeStamp());
			if ((now_time - WeChatParames.jsapi_ticket_gettime) > Long.valueOf(WeChatParames.jsapi_ticket_effective * 1000)) {

				// 如果当前时间大于上次的获取时间,并且超过有效时间,则重新获取
				get_jsapi_ticket(access_token);
			} else {		
				// 否则使用当前的缓存值
			}		
		}else {// 如果票据为空,则直接获取
			get_jsapi_ticket(access_token);
		}
	}
	
	private static void get_jsapi_ticket(String access_token) throws Exception {

		log.info("重新拉取了一次jsapi_ticket");
		String jsapi_ticket_url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCES_TOKEN";
		
		String real_url = jsapi_ticket_url.replace("ACCES_TOKEN", access_token);
		
		// 处理返回结果
		JSONObject jo = HttpUtil.getJson(real_url);
		
		if("0".equals(jo.getString("errcode")) && "OK".toLowerCase().equals(jo.getString("errmsg").toString().toLowerCase())) {		
			// 进行全局缓存
			WeChatParames.jsapi_ticket = (String)jo.getString("ticket");
			WeChatParames.jsapi_ticket_effective = jo.getLong("expires_in");
			WeChatParames.jsapi_ticket_gettime = Long.valueOf(CommonUtils.getTimeStamp());
		}			
	}
	
	/**
	 * 获取时间戳
	 * @return
	 */
	public static String getTimeStamp() {
		
		return String.valueOf(new Date().getTime());
	} 
	
	/**
	 * 获取UUID随机串
	 */
	public static String getUUIDString() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取随机长度字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length){
	    StringBuffer sb = new StringBuffer();
	    int len = BASE_STRING.length();
	    for (int i = 0; i < length; i++) {
	        sb.append(BASE_STRING.charAt(getRandom(len-1)));
	    }
	    return sb.toString();
	}
	private static int getRandom(int count) {
		return (int) Math.round(Math.random() * (count));
	}
	
	/**
	 * 减法运算
	 * @param a
	 * @param b
	 * @return
	 */
	public static String subtract(String a, String b) {
		return String.valueOf(Long.valueOf(a) - Long.valueOf(b));
	}
}
