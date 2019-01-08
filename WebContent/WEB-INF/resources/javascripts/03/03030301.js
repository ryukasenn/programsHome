$(function(){
	
	/**
	 * 通过审核按钮
	 */
	$("#addTerminal").on("click", function(){
		
		$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/support/agreeUncheck").attr("method", "POST").submit();
	})
	
	/**
	 * 驳回按钮
	 */	
	$("#rejectTerminal").on("click", function(){
		
		$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/support/rejectUncheck").attr("method", "POST").submit();
	})
})