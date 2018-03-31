$(function(){
		
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);		

		createModal(baseUrl + "/sellPersonnel/receiveRegionReper",{personType : '22'}, '地总名单');
	});

	/**
	 * 添加地区负责人
	 */
	$("#searchResper").on('click', function(){
		
		if('' == $('#searchName').val().trim()){
			
		} else {
			
			createModal(baseUrl + "/sellPersonnel/receiveRegionReper",{personType : '22', searchName : $('#searchName').val().trim()}, '地总名单');
		}
	});
	
	/**
	 * 规划下辖市区县
	 */
	$("#checkXzqxs").on('click', function(){		
		
		// 获取地区编号		
		window.location.href = baseUrl + "/sellPersonnel/checkXzqxs?regionUid=" + $("#updateRegion input[name='regionUid']").val();
	})
	
	/**
	 * 提交地区信息修改
	 */
	$(".changeAreaInfo").on('click', function(){
		
		alert("该功能还没做好");
		// 获取地区编号
//		$("input[name='regionId']").val($("input[name='regionUid']").val());
//		$("#updateRegion").attr("action", baseUrl + "/sellPersonnel/postChangeRegion").attr("method", "POST").submit();
	})
	
})