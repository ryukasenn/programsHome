$(function(){
	
	$("#transfer").on('click', function(){

		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/transfer").attr("method", "GET").submit();
	})
})