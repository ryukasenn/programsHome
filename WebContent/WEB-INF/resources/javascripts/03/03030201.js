$(function(){
	
	$('.province-collapse').on('show.bs.collapse', function () {

		$(".collapse").collapse('hide');
	})
	
	$('.area-collapse').on('show.bs.collapse', function () {

		$(this).siblings(".collapse").collapse('hide');
	})
	
})