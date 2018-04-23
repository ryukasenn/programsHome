$(function(){
		
	/**
	 * 修改按钮点击事件
	 */
	$('.changeButton').on('click', function(){
		
		$_this = $(this);		
		var regionUid = $_this.parent().parent().children().eq(1).find("p").html();
		$('input[name="regionUid"]').val(regionUid);
		$('#controlForm').attr("action", baseUrl + "/sellPersonnel/regionController/changeRegion").attr("method", "POST").submit();
	});
	
	/**
	 * 添加大区操作
	 */
	$('#addRegion').on('click', function(){
		
		$('#controlForm').attr("action", baseUrl + "/sellPersonnel/regionController/addRegion").attr("method", "GET").submit();
	});
	
	/**
	 * 添加大区操作
	 */
	$('#addArea').on('click', function(){
		
		$('#controlForm').attr("action", baseUrl + "/sellPersonnel/regionController/addArea").attr("method", "GET").submit();
	});
})