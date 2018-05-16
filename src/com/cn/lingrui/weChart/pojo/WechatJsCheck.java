package com.cn.lingrui.weChart.pojo;

public class WechatJsCheck {

	/**
	 * 企业应用CorpId
	 */
	private String appId; 
	
	/**
	 * js验证时间串
	 */
	private String timestamp;

	/**
	 * js验证随机串
	 */
	private String nonceStr;

	/**
	 * js验证标志
	 */
	private String signature;
	
	/**
	 * js验证url地址
	 */
	private String url;
	
	/**
	 * 企业应用CorpId
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * 企业应用CorpId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * js验证时间串
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * js验证时间串
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * js验证随机串
	 */
	public String getNonceStr() {
		return nonceStr;
	}
	/**
	 * js验证随机串
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	/**
	 * js验证标志
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * js验证标志
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	/**
	 * js验证url地址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * js验证url地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
