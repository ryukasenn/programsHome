package com.cn.lingrui.common.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.DownLoadIn;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.common.utils.HttpUtil;

@Controller
@RequestMapping("/download")
public class DownLoadController {

	
	/**
	 * 下载测试
	 * @param req
	 * @param in
	 * @return
	 */
	@Deprecated
	public ResponseEntity<byte[]> downLoad(HttpServletRequest req,DownLoadIn in){
		
		String path = GlobalParams.FILE_PATH;
		File file = new File(path + in.getFileName());
		HttpHeaders headers = new HttpHeaders();
		
		// 初始化返回数据
		ResponseEntity<byte[]> resutl = null;
		//下载显示的文件名，解决中文名称乱码问题  
        try {
			String downloadFielName = new String(in.getFileName().getBytes("UTF-8"),"iso-8859-1");
			//通知浏览器以attachment（下载方式）
	        headers.setContentDispositionFormData("attachment", downloadFielName);
	        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        
	        resutl = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
	                headers, HttpStatus.CREATED);  
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        return resutl;
	}
		
	/**
     * 文件下载
     * @Description: 
     * @param fileName
     * @param request
     * @param response
     * @return
	 * @throws UnsupportedEncodingException 
     */
	@RequestMapping(value="", method=RequestMethod.POST)
    public String downloadFile(DownLoadIn in,
            HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        if (in.getFileName() != null) {
    		String path = GlobalParams.FILE_PATH;
            File file = new File(path + in.getFileName());
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + new String(in.getFileName().getBytes("utf-8"),"iso-8859-1"));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                	
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
