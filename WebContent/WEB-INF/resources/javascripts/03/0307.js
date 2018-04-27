$(function(){
		
	
	$("input[name='endTime']").val(getNowFormatDate());
	
	/**
	 * 生成考核列表
	 */
	$("#createEvaluationForm").on("click", function(){

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
	})

})