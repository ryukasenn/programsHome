$(function(){
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receivePersons").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/persons").attr("method", "GET").submit();
	})
	
	
	$("#addTerminal").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/addTerminal").attr("method", "GET").submit();
	})
	
	
	$("#supportAdd").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/supportAdd").attr("method", "GET").submit();
	})
	
})