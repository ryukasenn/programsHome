$(function(){

	if("" == $("#NBPT_SP_PERSON_PID").val()){

	} else {
		// 负责区域初始化选定事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveTerminalXzqx", {TerminalId : $("#NBPT_SP_PERSON_PID").val()},function(jsonData){
						
			$_copyParent = $(".reponseAreas").eq(0);

			for(var i = 0; i < jsonData.length; i++){
				
				if(0 == i){
					$_copyParent.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
				}
				
				else {
					
					
					 $_copy = $_copyParent.clone().insertAfter($_copyParent);
					 $_copy.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
					 $_copy.children().find("button").remove();
				}
			}
			
		})
	}
	
	// 必填项目列表
	var inputItems = new Array("NBPT_SP_PERSON_NAME","NBPT_SP_PERSON_IDNUM","NBPT_SP_PERSON_PLACE",
							   "NBPT_SP_PERSON_MOB1","NBPT_SP_PERSON_MOB2",
							   "NBPT_SP_PERSON_ENTRYDATA","NBPT_SP_PERSON_POLICYNO",
							   "NBPT_SP_PERSON_POLICY_DATA1","NBPT_SP_PERSON_POLICY_DATA2",
							   "NBPT_SP_PERSON_QQ","NBPT_SP_PERSON_CHAT",
							   "NBPT_SP_PERSON_MAIL","NBPT_SP_PERSON_SCHOOL",
							   "NBPT_SP_PERSON_PROFESS");
	
	// 需要验证的单选框
	var radioItems = new Array("NBPT_SP_PERSON_MALE", "NBPT_SP_PERSON_JOB");
	
	// 需要验证的下拉框
	var selectItems = new Array("NBPT_SP_PERSON_POLICYTYPE");
	
	// 时间格式验证
	var timeItems = new Array("NBPT_SP_PERSON_ENTRYDATA", "NBPT_SP_PERSON_POLICY_DATA1", "NBPT_SP_PERSON_POLICY_DATA2");

	/**
	 * 点击提交事件
	 */
	$("#addPerson").on("click", function(){
		
		var areanos = "";
		

		if(1 == $(".reponseAreas").length && "" == $(".reponseAreas").find("select").val()){
			
			$(".reponseAreas").addClass("has-error");
			return;
		}
		
		for(var i = 0; i < $(".reponseAreas").length; i++){
			
			var selectValue = $(".reponseAreas").eq(i).find("select").val();
			
			if(null != selectValue && "" != selectValue ){
				
				areanos += selectValue + "&" ;
			} 
		}
		
		
		$("input[name='NBPT_SP_PERSON_AREANO']").val(areanos);
		
		// 输入框统一为空检验
		if(necessaryCheck(inputItems,radioItems,selectItems)){
			
			// 如果没有错误了,提交
			if(0 == $(".has-error").length){

				$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/addTerminal").attr("method", "POST").submit();
			}
			
		} else {
			return;
		}
	})

	/**
	 * 职务点击事件,职务点击要跟身份证验证同时进行
	 */
	$("input[name='NBPT_SP_PERSON_JOB']").on("click", function(){
		
		$_this = $("input[name='NBPT_SP_PERSON_IDNUM']");
		$_thisParent = $_this.parents(".form-group");
		idNumCheck($_this, $_thisParent);
	})
	
	/**
	 * 输入框统一去除错误提示
	 */
	$("input").on("focusout", function(){
		
		$_this = $(this)
		$_thisParent = $_this.parents(".form-group");
		
		if(-1 != inputItems.indexOf($_this.attr('name'))){
			
			// 如果不为空
			if("" != $_this.val().trim()){

				// 身份证验证
				if ("NBPT_SP_PERSON_IDNUM" == $_this.attr('name')){
					
					if(idNumCheck($_this, $_thisParent)){
						
						$_thisParent.removeClass("has-error");
						
						// 验证通过后,同时如果能取到籍贯信息,添加籍贯信息
						AjaxForGet(baseUrl + "/sellPersonnel/receiveTerminalPlace", {idNum : $_this.val().trim()},function(jsonData){
							
							if(0 == jsonData.length){
								return;
							}
								
							for(var i = 0; i < jsonData.length; i++){
								$("input[name='NBPT_SP_PERSON_PLACE']").val("");
								$("input[name='NBPT_SP_PERSON_PLACE']").val(jsonData[i].NBPT_COMMON_XZQXHF_NAME);
							}
						})
					}
				}
				
				// 时间格式验证
				if(-1 != timeItems.indexOf($_this.attr('name'))){
					
					if($_this.val().length != 8){

						$_thisParent.addClass("has-error");
						
					} else {

						$_thisParent.removeClass("has-error");
					}
				}
				
				$_thisParent.removeClass("has-error");
				
			} 
			
			// 如果为空
			else {
				$_thisParent.addClass("has-error");
			}
			
		}
	})
	
	$(".form-group").on("click",".extraBtn",function (){
		
		// 多地区管理的额外点击事件
		var $_this = $(this);
		$_copy = $_this.parents(".form-group").clone().insertAfter($_this.parents(".form-group"));
		$_copy.children().find("button").remove();
	})
	
	
	$(".form-group").on("change","select",function (){
		
		if($(this).val != ''){
			
			$(this).parents(".form-group").removeClass("has-error");
		}
	})
	
	/**
	 * 三级行政区县
	 */
//	$(".form-group").on("change",".provinceSelect",function (){
//		
//		var $_this = $(this);
//		
//		$_this.parents(".form-group").removeClass("has-error");
//		
//		// 1.先隐藏已出现的市级和县级
//		$_this.parent().nextAll().find("select").css("display", "none");
//		
//		// 2.判断选择
//		if("" == $_this.val().trim()){
//			return;
//		}
//		AjaxForGet(baseUrl + "/sellPersonnel/receiveSelect", {parentId : $_this.val()}, function(jsonData){
//
//			if(1 == jsonData.length){
//				
//				// do nothing
//				return;
//			}
//			// 创建select
//			$_citySelect = $_this.parent().nextAll().find(".citySelect").empty();
//			
//			for(var cityI = 0; cityI < jsonData.length; cityI ++ ){
//				
//				$("<option value='" + jsonData[cityI].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[cityI].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($_citySelect);
//			}
//			
//			$_citySelect.css("display", "block");
//		});
//		
//	})
//	
//	$(".form-group").on("change", ".citySelect", function(){
//		
//		var $_this = $(this);
//
//		$_this.parents(".form-group").removeClass("has-error");
//		
//		// 1.先删除已出现的县级
//		$_this.parent().nextAll().find("select").css("display", "none");
//
//		// 2.判断选择
//		if("" == $_this.val().trim()){
//			return;
//		}
//		AjaxForGet(baseUrl + "/sellPersonnel/receiveSelect", {parentId : $_this.val()}, function(jsonData){
//			
//			if(1 == jsonData.length){
//				
//				// do nothing
//				return;
//			}
//			// 获取select			
//			$_contySelect = $_this.parent().nextAll().find(".contySelect").empty();
//			
//			for(var contyI = 0; contyI < jsonData.length; contyI ++ ){
//				
//				$("<option value='" + jsonData[contyI].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[contyI].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($_contySelect);
//			}
//			$_contySelect.css("display", "block");
//		});
//	})
	
	/**
	 * 身份证验证
	 */
	function idNumCheck($_this, $_thisParent){
		
		// 判断身份证是否合法
		if(isIdCard($_this.val())){
			
			$_thisParent.removeClass("has-error");
			$(".errorMessage").empty();
		} else {

			$_thisParent.addClass("has-error");
			$(".errorMessage").empty();	
			$(".errorMessage").append($("<font color='red' ><p>请输入正确的身份证信息</p></font>"))
			return false;
		}
		
		// 判断年龄是否符合要求
		if(null == $("input[name='NBPT_SP_PERSON_JOB']:checked").val()){
			
			// 如果没有选择职位
			$_thisParent.addClass("has-error");
			$(".errorMessage").empty();	
			$(".errorMessage").append($("<font color='red' ><p>请选择职务后再确认身份证</p></font>"))
			return false;
			
		} else if("25" == $("input[name='NBPT_SP_PERSON_JOB']:checked").val()){
			
			// 如果是推广经理,要求55以下
			if($_this.val().substr(6,4) < 1963){
				alert($_this.val().substr(6,4));
				$_thisParent.addClass("has-error");
				$(".errorMessage").empty();	
				$(".errorMessage").append($("<font color='red' ><p>推广经理要求55岁以下</p></font>"))
				return false;
			} else {

				$_thisParent.removeClass("has-error");
				$(".errorMessage").empty();
				return true;
			}
		} else {
			
			// 如果不是推广经理,要求35以下
			if($_this.val().substr(6,4) < 1983){

				$_thisParent.addClass("has-error");
				$(".errorMessage").empty();	
				$(".errorMessage").append($("<font color='red' ><p>非推广经理要求35岁以下</p></font>"))
				return false;
			} else {

				$_thisParent.removeClass("has-error");
				$(".errorMessage").empty();
				return true;
			}
		}
		
	}
	

})