$(function(){

	$("#deleteTerminal").on('click', function(){
		
		new Confirm({
			
			message : '该操作将彻底删除该终端人员,请谨慎操作',
			sureCallBack : function(){
				
				$("#deleteTerminalA")[0].click();
			}
		});
	})
	
	$("#changeTerminal").on('click', function(){
		
		$(".personInfo").attr('action', baseUrl + '/sellPersonnel/support/changeTerminal').attr('method', 'POST').submit();
	})
})