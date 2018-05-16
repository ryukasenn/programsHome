package com.cn.lingrui.weChart.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.lingrui.weChart.pojo.WechatJsCheck;
import com.cn.lingrui.weChart.util.GetSecretKey;
import com.cn.lingrui.weChart.util.PojoUtils;
import com.cn.lingrui.weChart.util.WeChatParames;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/jscheck")
public class JsCheckController {


	private static Logger log = LogManager.getLogger();
	
	/**
     * 页面JS注入验证方法,供wechat.js的ajax调用
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("")
    public String checkJson(HttpServletRequest request) throws Exception {
    	   
    	log.debug("进行了页面JS注入验证");
    	// 定义签名所需参数
    	WechatJsCheck wcjc = new WechatJsCheck();
    	wcjc.setAppId(WeChatParames.CORPID);// 企业ID
    	wcjc.setNonceStr(UUID.randomUUID().toString());// 随机串
    	wcjc.setTimestamp(PojoUtils.getTimeStamp());// 时间戳
    	wcjc.setUrl(request.getParameter("localurl"));
    	
    	// 获取验证结果
    	GetSecretKey.getJsSdkSign(wcjc);
    	
    	// 讲验证结果转换成JSON字符串并返回
    	JSONObject jo = JSONObject.fromObject(wcjc);
    	return jo.toString();
    }
}
