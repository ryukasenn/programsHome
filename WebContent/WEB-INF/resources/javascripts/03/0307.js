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
		new Confirm({
			message : '请选择相应选项,若数据内容较多,请选择直接下载并稍等',
			type : 'download',
			cancelCallBack : createAttendance,
			sureCallBack : createAttendance
		});
	})
})
	function createAttendance(type){
		if('download' === type){
			// 如果是下载请求,直接发送请求
			$("input[name='type']").val("download")
			window.location.href = baseUrl + "/sellPersonnel/support/createAttendance?type=download"
			//$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/createAttendance").attr("method", "GET").addClass('download').submit().removeClass('download');
		} else if('display' === type){
			$("input[name='type']").val("display")
			$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/createAttendance").attr("method", "GET").submit();
		}
	}

























