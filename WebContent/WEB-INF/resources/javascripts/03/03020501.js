$(function(){
		
	/**
	 * 添加下辖市区县
	 */
	$(".add").on("click", function(){
		
		// 负责区域的单选框点击事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveAreaContainSelects", {parentId : $("#provinceId").val()},function(jsonData){
			
			
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($("#citySelect"));
			}
			

			$("#030205Modal").modal('show')
		})
	})
	
	$("#citySelect").on("change",function (){
		
		var $_thisCity = $(this);

		// 判断选择
		if("" == $_thisCity.val().trim()){
			return;
		}
		
		
		// 市级下拉框选择后,生成区县级下拉框
		AjaxForGet(baseUrl + "/sellPersonnel/receiveAreaContainSelects", {parentId : $_thisCity.val()},function(jsonData){
			
			$("#contySelect").empty();
			
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($("#contySelect"));
			}
		})
	})
	
})