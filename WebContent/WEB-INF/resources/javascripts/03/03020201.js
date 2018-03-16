$(function(){
		
	/**
	 * 修改按钮点击事件
	 */
	$('.changeButton').on('click', function(){
		
		$_this = $(this);		
		var regionId = $_this.parent().parent().children().eq(1).html();
		$('#regionId').val(regionId);
		$('#controlForm').attr("action", baseUrl + "/sellPersonnel/changeRegion").attr("method", "POST").submit();
	});
})