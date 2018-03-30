$(function(){
		
	/**
	 * 添加下辖市区县
	 */
	$(".add").on("click", function(){
		
		// 负责区域的单选框点击事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveAreaContainSelects", {parentId : $("#changeRegion_provinceId").val()},function(jsonData){
			
			
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($("#citySelect"));
			}
			

			$("#030205Modal").modal('show')
		})
	})
	
	/**
	 * 提交添加
	 */
	$("#addAreaConfirm").on("click", function(){
		
		// 1.获取选择的市
		var cityValue = $("#citySelect").val();
		
		// 2.获取选择的区县
		var contyValue = $("#contySelect").val();
		
		// 如果市级选项为空,什么都不做
		if("" == cityValue){
			
			return ;
		} else {
			
			if("" == contyValue){
				if(comfirm("是否只选择市级行政单位")){

					$("input[name='addAreaContain_regionId']").val($("#changeRegion_regionId").val());
					$("#addAreaContain").attr("action", baseUrl + "/sellPersonnel/addRegionXzqx").attr("method", "POST").submit();
				}
			} else {

				$("input[name='addAreaContain_regionId']").val($("#changeRegion_regionId").val());
				$("#addAreaContain").attr("action", baseUrl + "/sellPersonnel/addRegionXzqx").attr("method", "POST").submit();
			}
			
		}
	})
	
	
	$("#citySelect").on("change",function (){
		
		var $_thisCity = $(this);

		// 判断选择
		if("" == $_thisCity.val().trim()){
			$("#contySelect").empty();
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