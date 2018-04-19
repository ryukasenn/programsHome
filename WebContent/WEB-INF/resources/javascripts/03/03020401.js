$(function(){
		
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);		

		createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '22'}, '地总名单');
	});

	/**
	 * 添加地区负责人
	 */
	$("#searchResper").on('click', function(){
		
		if('' == $('#searchName').val().trim()){
			
		} else {
			
			createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '22', searchName : $('#searchName').val().trim()}, '地总名单');
		}
	});

	/**
	 * 删除负责人按钮
	 */
	$('.deleteResponsibler').on('click', function(){
				
		$('#ResperName').val("");
		$('#ResperPid').val("");
	});
	
	/**
	 * 规划下辖市区县
	 */
	$("#checkXzqxs").on('click', function(){		
		
		// 获取地区编号		
		window.location.href = baseUrl + "/sellPersonnel/regionController/checkXzqxs?regionUid=" + $("#updateRegion input[name='regionUid']").val();
	})
	
	/**
	 * 提交地区信息修改
	 */
	$(".changeAreaInfo").on('click', function(){
		
		$("#updateRegion").attr("action", baseUrl + "/sellPersonnel/regionController/updateRegion").attr("method","POST").submit();
	})
	
	
	
	
})


























