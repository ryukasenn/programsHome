$(function(){
		
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);
		
		createModal(baseUrl + "/sellPersonnel/receiveRegionReper",{personType : '6'}, '大区总名单');
	});

	$("#searchResper").on('click', function(){
		
		if('' == $('#searchName').val().trim()){
			
		} else {
			
			createModal(baseUrl + "/sellPersonnel/receiveRegionReper",{personType : '6', searchName : $('#searchName').val().trim()}, '大区总名单');
		}
	});
	
})