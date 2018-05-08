$(function(){
		
	
	$("input[name='endTime']").val(getNowFormatDate());
	
	/**
	 * 生成考核列表
	 */
	$("#createEvaluationForm").on("click", function(){
		
		AjaxForGet(baseUrl + "/sellPersonnel/support/roleCheck", "",function(jsonData){

			// 验证权限
			if(jsonData.flag){
				var $_endTime = $("input[name='endTime']");
				
				if("" == $_endTime.val()){
					
					$_endTime.val(getNowFormatDate());
				}
				
				if(isTime($_endTime)){
					
					$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/createEvaluationForm").attr("method", "GET").submit();
				} else {
					
					new Confirm({
						message:'时间输入有误',
						type:'alert'
					})
				}
			} else {
				
				$(".main-body").empty().append($("<div class='col-xs-12 col-sm-12'><h3>木有权限</h3></div>"));
			}
		})
		
		

	})
	
	/**
	 * 生成考勤列表
	 */
	$("#createAttendance").on("click", function(){

		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/createAttendance").attr("method", "GET").submit();

	})

})