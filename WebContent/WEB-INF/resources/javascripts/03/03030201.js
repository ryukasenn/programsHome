$(function(){
	
	$('.passCheckOut').on('click', function () {

		$_this = $(this);
		
		$("input[name='terminalPid']").val($_this.val());
		$("input[name='type']").val('0');
		$("#checkOutForm").attr('method', 'POST').attr('action', baseUrl + "/sellPersonnel/support/receiveUncheck").submit()
	})
	
	$('.leaveCheckOut').on('click', function () {

		$_this = $(this);
		
		$("input[name='terminalPid']").val($_this.val());
		$("input[name='type']").val('1');
		$("#checkOutForm").attr('method', 'POST').attr('action', baseUrl + "/sellPersonnel/support/receiveUncheck").submit()
	})
	
	$('.passDirect').on('click', function () {

		$_this = $(this);
		
		$("input[name='terminalPid']").val($_this.val());
		$("input[name='type']").val('0');
		$("#checkOutForm").attr('method', 'POST').attr('action', baseUrl + "/sellPersonnel/support/agreeUncheck").submit()
	})
		
	$('.leaveDirect').on('click', function () {

		$_this = $(this);
		
		$("input[name='terminalPid']").val($_this.val());
		$("input[name='type']").val('1');
		$("#checkOutForm").attr('method', 'POST').attr('action', baseUrl + "/sellPersonnel/support/agreeUncheck").submit()
	})
	
	$('.passDelete').on('click', function () {
		
		$_this = $(this);
		
		$("input[name='terminalPid']").val($_this.val());
		$("#checkOutForm").attr('method', 'GET').attr('action', baseUrl + "/sellPersonnel/support/deleteTerminal").submit()
	})
})