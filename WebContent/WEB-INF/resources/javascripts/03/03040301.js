$(function(){

	if("" == $("#NBPT_SP_PERSON_PID").val()){

	} else {
		// 负责区域的单选框点击事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveTerminalXzqx", {TerminalId : $("#NBPT_SP_PERSON_PID").val()},function(jsonData){
						
			$_copyParent = $(".reponseAreas").eq(0);

			for(var i = 0; i < jsonData.length; i++){
				
				if(0 == i){
					$_copyParent.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
				}
				
				else {
					 $_copy = $_copyParent.clone().insertAfter($_copyParent);
					 $_copy.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
				}
			}
			
		})
	}
	
	
	// 需要验证的输入框
	var inputItems = new Array("NBPT_SP_PERSON_NAME","NBPT_SP_PERSON_IDNUM","NBPT_SP_PERSON_MOB1");
//							   "NBPT_SP_PERSON_MOB2","NBPT_SP_PERSON_ENTRYDATA",
//							   "NBPT_SP_PERSON_PLACE","NBPT_SP_PERSON_POLICYNO",
//							   "NBPT_SP_PERSON_POLICY_DATA1","NBPT_SP_PERSON_POLICY_DATA2");
	
	// 需要验证的单选框
	var radioItems = new Array("NBPT_SP_PERSON_MALE", "NBPT_SP_PERSON_JOB");
	
	// 需要验证的下拉框
	var selectItems = new Array("NBPT_SP_PERSON_DEPT_ID");//,"NBPT_SP_PERSON_AREANO_PROVINCE","NBPT_SP_PERSON_AREANO_CITY","NBPT_SP_PERSON_AREANO_CONTY");
	
	// 时间格式验证
	var timeItems = new Array("NBPT_SP_PERSON_ENTRYDATA", "NBPT_SP_PERSON_POLICY_DATA1", "NBPT_SP_PERSON_POLICY_DATA2");
	
	/**
	 * 点击提交事件
	 */
	$("#addPerson").on("click", function(){
		
		/**
		 * 暂时放开条件限制
		 */
		var areanos = "";
		for(var i = 0; i < $(".reponseAreas").length; i++){
			
			var selectValue = $(".reponseAreas").eq(i).find("select:visible").last().val();
			
			if(null != selectValue && "" != selectValue ){
				
				areanos += selectValue + "&" ;
			} 
		}
		
		$("input[name='NBPT_SP_PERSON_AREANO']").val(areanos);
		
		// 输入框统一为空检验
		if(necessaryCheck(inputItems,radioItems,null)){
			
			$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/addTerminal").attr("method", "POST").submit();
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
		$_this.parents(".form-group").clone(true).insertAfter($_this.parents(".form-group"));
	})
	
	
	$(".form-group").on("click","input[name='NBPT_SP_PERSON_REGION']",function (){
		
		// 获取点击的地区的id
		$_thisRadio = $("input[name='NBPT_SP_PERSON_REGION']:checked")
		
		// 负责区域的单选框点击事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveAreasSelect", {areaId : $_thisRadio.val()},function(jsonData){
			

			var $_select = $("<select></select>").insertAfter($("input[name='NBPT_SP_PERSON_REGION']:last"));
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($_select);
			}
		})
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
			
		} else if("5" == $("input[name='NBPT_SP_PERSON_JOB']:checked").val()){
			
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