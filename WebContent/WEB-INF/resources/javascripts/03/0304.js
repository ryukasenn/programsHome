$(function(){
	
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receivePersons").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/persons").attr("method", "GET").submit();
	})
	
	
	$("#addTerminal").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/addTerminal").attr("method", "GET").submit();
	})
	
	
	/**
	 * 
	 */
	$("#supportAdd").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/supportAdd").attr("method", "GET").submit();
	})
	
	
	/**
	 * 查看所有人员
	 */
	$("#receiveAllPersons").on("click", function(){
		
		new Confirm({
			message : '请选择相应选项,本数据内容较多,建议选择直接下载并稍等',
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
			
			$("#controlForm").attr("action", baseUrl + "/sellPersonnel/receiveAllPersons").attr("method", "GET").addClass('download').submit().removeClass('download');
		} else if('display' === type){
			$("input[name='type']").val("display")
			$("#controlForm").attr("action", baseUrl + "/sellPersonnel/receiveAllPersons").attr("method", "GET").submit();
		}
	}