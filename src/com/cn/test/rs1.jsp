<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.IOException"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		Cookie[] cookies = request.getCookies();    //��request�л��Cookie����ļ���
		String CHARSET = "UTF-8";
		String ltpaDominoSecret="YZLGw22dlpZukZmTb5plaGyWymE=";
		byte[] dominoSecret = Base64.decodeBase64(ltpaDominoSecret.getBytes());
		if(cookies !=null){
	   		for(int i = 0;i < cookies.length;i++){  //����cookie���󼯺�
	        		if(cookies[i].getName().equals("LtpaToken")){//���cookie�����������mrCookie            			
						String preltpaToken=cookies[i].getValue();
						StringBuffer sb=new StringBuffer(preltpaToken);
						if(preltpaToken.length()<64) {
		   					for(int j=0;j<64-preltpaToken.length();j++) {
			  			 		sb.append("=");
		  					}
	   					}
					   String newltpaToken=sb.toString();
						
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
					   // ��ȡLTPAToken�汾��  14stream.read(header, 0, 4);
					   if (header[0] != 0 || header[1] != 1 || header[2] != 2|| header[3] != 3)
						           throw new IllegalArgumentException("Invalid ltpaToken format"); 
//						       // ��ȡ��ʼʱ��   
					   stream.read(creation, 0, 8);
//						      // ��ȡ����ʱ��   
					   stream.read(expires, 0, 8);  
//						 // ��ȡDomino�û�DN   
					   stream.read(username, 0, usernameLength);  
//						   // ��ȡSHAУ���    
					   stream.read(sha, 0, 20);
//						        // ת���û���   
					   char characters[] = new char[usernameLength];  
						   
					    try {   
						    	InputStreamReader isr = new InputStreamReader(  new ByteArrayInputStream(username),  CHARSET); 
						    	isr.read(characters);  
					    	} catch (Exception e) {     }  
//						      // ���Domino�û�DN    
				    	String dn = new String(characters); 
//						    	// ��ô���ʱ��      
				    	Date creationDate = new Date(    Long.parseLong(new String(creation), 16) * 1000);  
//						       // ��õ���ʱ��   40 
				    	Date expiresDate = new Date(    Long.parseLong(new String(expires), 16) * 1000);  
				//
			    	 	//out.println("header:"+new String(header));
			   			
					   			
//						    	// ����LTPA Token     
				    	ByteArrayOutputStream ostream = new ByteArrayOutputStream(); 
				    	try {   
//						    		// LTPA Token�汾��  
				    			ostream.write(header); 
//						    		// ����ʱ��         
					    		ostream.write(creation); 
//						    		// ����ʱ��   
					    		ostream.write(expires);   
//						    		// Domino�û�DN����CN=SquallZhong/O=DigiWin  
					    		ostream.write(username); 
//						    		// Domino LTPA ��Կ   
					    		ostream.write(dominoSecret);  	
					    		ostream.close();   
				    		} catch (IOException e) {
				    				throw new RuntimeException(e);
					    		}  
//						    	// ���� SHA-1 У���  
				    		MessageDigest md; 
				    		try {   
				    			md = MessageDigest.getInstance("SHA-1"); 
				    			md.reset();   
				    		} catch (NoSuchAlgorithmException e) {         throw new RuntimeException(e);     }   
				    		byte[] digest = md.digest(ostream.toByteArray()); 
				    		// ��� SHA-1 У��ͣ�digest����Ϊ20   
				    		//out.println("digest:"+new String(digest));
				    		//out.println("sha:"+new String(sha));
				    		boolean valid = MessageDigest.isEqual(digest, sha); 
				    		if(valid){
							response.sendRedirect("http://10.0.9.63:9000/rsManage/login?username="+new String(username));  }

	        		}
	   		}
		}
			

	%>
	
</body>
</html>