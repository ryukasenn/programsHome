$(function(){
	
	$('.checkOutPersonInfo').on('click', function () {

		$_this = $(this);
		
		$("input[name='UNCHECKPID']").val($_this.val());
		$("#checkOutForm").attr('method', 'POST').attr('action', baseUrl + "/sellPersonnel/receiveUncheck").submit()
	})
	
})