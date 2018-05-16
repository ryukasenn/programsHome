package com.cn.lingrui.weChart.util;

public class WeChatParames {

	/**
	 * 回调验证参数TOKEN
	 */
	public static final String TOKEN = "F9tcpoOxZXWKuJ7MbT7Tpj1YDdM2MNf";
	
	/**
	 * 回调验证参数ENCODINGAESKEY
	 */
	public static final String ENCODINGAESKEY = "2RbOktYMcnvK8TxECTXMMMAMbGEizifdfju6IixxX1Y";
	
	/**
	 * 公众号ID
	 */
	public static final String CORPID = "wx6acaf6ee8bb6876a";	
	
	/**
	 * 企业号应用<测试应用>的秘钥
	 */
	public static final String SERCRET = "e-vZsGeeACTssuOItt2Fx7-mO4JGH5Ta9xD1UwFlALw";
	
	/**
	 * 全局缓存access_token
	 */
	public static String access_token = "";
	
	/**
	 * 全局缓存access_token获取时间
	 */
	public static Long access_token_gettime = 0L;
	
	/**
	 * 全局缓存access_token有效时间
	 */
	public static Integer access_token_effective = 0;
	
	/**
	 * 全局缓存jsapi_ticket
	 */
	public static String jsapi_ticket = "";
	
	/**
	 * 全局缓存jsapi_ticket获取时间
	 */
	public static Long jsapi_ticket_gettime = 0L;
	
	/**
	 * 全局缓存jsapi_ticket有效时间
	 */
	public static Long jsapi_ticket_effective = 0l;
	
	/**
	 * 全局缓存agentId企业应用的ID
	 */
	public static Integer CRM_AGENT_ID = 17;
	
	/**
	 * OAuth2IC_USERID
	 */
	public static String OAuth2IC_USERID = "OAUTH2_USERID";

	/**
	 * 消息类型TYPE_TEXT
	 */
	public static String TYPE_TEXT = "text";
	
	/**
	 * OAuth2验证CODE获取地址
	 */
	public static final String url4CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo#wechat_redirect";
}
