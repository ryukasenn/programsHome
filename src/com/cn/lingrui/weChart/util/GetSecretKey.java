package com.cn.lingrui.weChart.util;

import java.security.MessageDigest;

import com.cn.lingrui.weChart.pojo.WechatJsCheck;
import com.cn.lingrui.weChart.util.CommonUtils;

public class GetSecretKey {

	/**
	 * 测试用应用token获取方法
	 * @return
	 * @throws Exception
	 */
	public static String getAccessToken() throws Exception {
		
		CommonUtils.getAccessToken(WeChatParames.CORPID, WeChatParames.SERCRET);
		return WeChatParames.access_token;
	}
	
	/**
	 * 测试用应用jsTicket获取方法
	 * @return
	 * @throws Exception
	 */
	public static String getJsapiTicket() throws Exception {
		
		CommonUtils.getJsapiTicket(getAccessToken());
		return WeChatParames.jsapi_ticket;
	}
	
	/**
	 * 获取js验证签名
	 * @param noncestr
	 * @param tsapiTicket
	 * @param timestamp
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public static WechatJsCheck getJsSdkSign(WechatJsCheck wcjc) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("jsapi_ticket=");
		sb.append(getJsapiTicket());
		sb.append("&");
		sb.append("noncestr=");
		sb.append(wcjc.getNonceStr());
		sb.append("&");
		sb.append("timestamp=");
		sb.append(wcjc.getTimestamp());
		sb.append("&");
		sb.append("url=");
		sb.append(wcjc.getUrl());
		String str = sb.toString();
		// SHA1签名生成
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(str.getBytes());
		byte[] digest = md.digest();
		StringBuffer hexstr = new StringBuffer();
		String shaHex = "";
		for (int i = 0; i < digest.length; i++) {
			shaHex = Integer.toHexString(digest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		wcjc.setSignature(hexstr.toString());
		return wcjc;
	}

}
