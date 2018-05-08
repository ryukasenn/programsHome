$(function(){
	
	$("#addTerminal").on("click", function(){
		
		$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/support/agreeUncheck").attr("method", "POST").submit();
	})
})