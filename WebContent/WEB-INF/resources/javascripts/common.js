

$(function(){

	$("form").on('submit', function(){
		
		$("#waitModal").modal({
			backdrop:'static'
		})
	})
	
	$("a").on('click', function(){

		$("#waitModal").modal({
			backdrop:'static'
		})
	})
	
	$(".downLoadCheckOut").on('click', function(){
		
		tableToExcel($(this).val());
	})
	
})


	/**
	 * 页面table信息导出方法
	 * 不兼容IE
	 */
	var tableToExcel = (function() {  
        var uri = 'data:application/vnd.ms-excel;base64,',  
                template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>',  
                base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },  
                format = function(s, c) {  
                    return s.replace(/{(\w+)}/g,  
                            function(m, p) { return c[p]; }) }  
        return function(table, name) {  
            if (!table.nodeType) table = document.getElementById(table)  
            var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}  
            window.location.href = uri + base64(format(template, ctx))  
        }  
    })()
    
    
    
	/**
	 * Ajax的GET提交
	 * @param targetUrl
	 * @param param
	 * @param func
	 * @returns
	 */
	function AjaxForGet(targetUrl, param, func){


		$.ajax({
	        timeout : 5000,
	        type : "GET",
	        url : targetUrl,
	        data : param || "",
	        success : function(data){
	        	func(JSON.parse(data));
	        }
	        //注意：这里不能加下面这行，否则数据会传不到后台
	        //contentType:'application/json;charset=UTF-8',
	    });
	}
	
	/**
	 * 身份证验证
	 * @param idNum
	 * @returns
	 */
	function isIdCard(idNum){
		
		// 分界身份证数字
		var nums = idNum.split("");
		var checkNums = new Array("7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2");
		var realNums = new Array("1","0","X","9","8","7","6","5","4","3","2");
		var addResult = 0;
		for(var i = 0; i < nums.length - 1; i++){
			
			addResult += nums[i]*checkNums[i];
		}
		var result = addResult % 11;
		
		// 如果验证结果跟最后一位不匹配
		if (realNums[result].toUpperCase() != nums[17].toUpperCase()){
			
			return false;
		}
		return true;
	}
	
	
	/**
	 * 必填项目验证
	 * @param inputNames 输入框项目
	 * @param radioNames 单选框项目
	 * @param selectNames 下拉框项目
	 * @returns
	 */
	function necessaryCheck(inputNames, radioNames, selectNames){
		
		if(undefined == inputNames || null == inputNames){
			
		} else {
			// 遍历必须check列表names
			for(var nameI = 0; nameI < inputNames.length; nameI++){
				
				// 获取必填项
				var $_checkItem = $("input[name='" + inputNames[nameI] + "']");
				
				// 验证填写内容
				if("" == $_checkItem.val().trim()){
					
					// 如果必填项为空
					$_checkItem.parents(".form-group").addClass("has-error");

					new Confirm({
						
						'message' : '有必填项没有填写',
						'type' : 'alert',
						'focusTarget' : inputNames[nameI]
					})
					return false;
				}
			}
		}
		
		if(undefined == radioNames || null == radioNames){
			
		} else {
			
			// 遍历必须check列表names
			for(var nameI = 0; nameI < radioNames.length; nameI++){

				// 获取必填项
				var $_checkItem = $("input[name='" + radioNames[nameI] + "']:checked");
				
				// 验证填写内容
				if(undefined == $_checkItem.val()){

					// 如果没有选择
					$_checkItem.parents(".form-group").addClass("has-error");

					new Confirm({
						
						'message' : '有必填项没有填写',
						'type' : 'alert',
						'focusTarget' : radioNames[nameI]
					})
					return false;
				};
			}
		}
		
		if(undefined == selectNames || null == selectNames){
			
		} else {
			
			// 遍历必须check列表names
			for(var nameI = 0; nameI < selectNames.length; nameI++){
				
				// 获取必填项
				var $_checkItem = $("select[name='" + selectNames[nameI] + "']");
					
				// 验证选择内容
				if("" == $_checkItem.val()){

					// 如果没有选择
					$_checkItem.parents(".form-group").addClass("has-error");

					new Confirm({
						
						'message' : '有必填项没有填写',
						'type' : 'alert',
						'focusTarget' : selectNames[nameI]
					})
					return false;
				}
				
			}
		}
		return true;
	}
	
	/**
	 * 长度验证
	 * @param item 验证的项目
	 * @param length 规定的长度
	 * @returns
	 */
	function lengthCheck(item, length){
		
		// 如果项目的长度大于要求长度,
		if(length < item.val().length){
			
			item.parents(".form-group").addClass("has-error");
			return false;
		}
	}
	
	/**
	 * 添加错误提示框
	 * @param item 验证的项目
	 * @param content 提示的内容
	 * @returns
	 */
	function addMessage(item, content){
		
		$_messageBox = $("<div class='col-xs-2 col-sm-2 errorMessages'>" +
						 "  <font color='red' ><p>" + content + "</p></font>" +
						 "</div>").insertAfter(item.parents(".form-group"));
	}
	
	/**
	 * 删除错误提示框
	 * @param item 验证的项目
	 * @returns
	 */
	function removeMessage(item){
		
		$_parentItem = item.parents(".form-group");
		$_messageBox = $_parentItem.nextAll(".errorMessages").remove();
	}
	
	function isTime(item){

		var reg1 = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
		var reg2 = /^[1-9]\d{3}\/(0[1-9]|1[0-2])\/(0[1-9]|[1-2][0-9]|3[0-1])$/;
		var regExp1 = new RegExp(reg1);
		var regExp2 = new RegExp(reg2);
		
		if(regExp1.test(item.val()) || regExp2.test(item.val())){

			return true;
		}
		return false;
	}
	
	/**
	 * 代替confirm,alert模态框
	 */
	var Confirm = function (options){
		
		// 定义属性
        this.sureBtn    = null;
        this.cancelBtn  = null;
        this.textInput  = null;
        this.contain = null;
        
        // 设置默认属性
        this.configs = {
            "type": "confirm",
            "title": "",
            "message": "这是一个提示",
            "cancelCallBack": "",
            "sureCallBack": "",
            "focusTarget" : ""
        };

        // 扩展默认属性
        options && this.extend(this.configs, options);
        
        // 初始化方法
        this.init();
        // 事件添加
        this.sureBtn   && this.addEvent(this.sureBtn,   "click", this.btnClick.bind(this));
        this.cancelBtn && this.addEvent(this.cancelBtn, "click", this.btnClick.bind(this));
		
		$("#ConfirmModal").modal({
			backdrop:'static'
		})
	}
	
	Confirm.prototype = {
		init: function () {
			var config = this.configs
			
			this.contain = $( '<div role="dialog" aria-labelledby="ConfirmModalLabel" tabindex="-1" class="modal fade" id="ConfirmModal">' +
					'<div class="modal-dialog" role ="document">' +
					'<div class="modal-content">' +
						'<div class="modal-body"></div>' +
						'<div class="modal-footer"></div>' +
					'</div>' +
				'</div>' +
			'</div>');

			this.contain.find('.modal-body').append($("<p>" + config.message + "</p>"));
			this.contain.appendTo($('body'));
			
			switch (config.type){
			
				case "alert" :
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="ConfirmModalSure">确定</buttion>'));
					this.sureBtn   = this.contain.find('.btn-primary')[0];
					break;
			
				default:
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-default" id="ConfirmModalCancel">取消</buttion>'));
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="ConfirmModalSure">确定</buttion>'));
					this.sureBtn   = this.contain.find('.btn-primary')[0];
					this.cancelBtn = this.contain.find('.btn-default')[0];
					break;
			}
		},
		extend: function (oldObj, newObj) {
			for(var key in newObj) {
				oldObj[key] = newObj[key];
			}
			return oldObj;
		},
		addEvent: function(el, type, callBack) {
            if (el.attachEvent) {
                el.attachEvent('on' + type, callBack);
            } else {
                el.addEventListener(type, callBack, false);
            }
        },
		btnClick: function (e) {
            e = e || event;
            var _this    = this,
                _tarId   = e.target.id,
                _configs = this.configs;
            switch(_tarId) {
                // 点击取消按钮
                case "ConfirmModalCancel":{
                    _this.close();
                    _configs.cancelCallBack && _configs.cancelCallBack();
                } break;
                // 点击确认按钮
                case "ConfirmModalSure": {
                    _this.close();
                    _configs.sureCallBack && _configs.sureCallBack();
                }break;
            }
            
        },
        close: function(){
        	
        	$("body").removeClass("modal-open")
        	this.contain.next(".modal-backdrop").remove();
        	this.contain.remove();
        	$("input[name='" + this.configs.focusTarget + "']").focus();
        }
		
	}
	
	/**
	 * 获取当前时间YYYY-MM-DD格式
	 * @returns
	 */
	function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }
	
	/**
	 * 设置项目只读
	 * @param items
	 * @returns
	 */
	function setReadOnly(items){
		
		if(undefined != items.inputItems || null != items.inputItems){
			
			var inputItems = items.inputItems;
			// 遍历设置只读的input
			for(var nameI = 0; nameI < inputItems.length; nameI++){
				
				// 获取只读项
				var $_checkItem = $("input[name='" + inputItems[nameI] + "']");
				
				$_checkItem.attr("readOnly","readOnly");
			}
		}
		if(undefined != items.radioItems || null != items.radioItems){
			
			var radioItems = items.radioItems;
			// 遍历设置只读的radio
			for(var nameI = 0; nameI < radioItems.length; nameI++){
				
				// 获取只读项
				var $_checkItem = $("input[name='" + radioItems[nameI] + "']");

				$_checkItem.attr("disabled","disabled");
			}
		}
		if(undefined != items.selectItems || null != items.selectItems){
	
			var selectItems = items.selectItems;
			// 遍历设置只读的select
			for(var nameI = 0; nameI < selectItems.length; nameI++){
				
				// 获取只读项
				var $_checkItem = $("input[name='" + selectItems[nameI] + "']");
				
				$_checkItem.attr("disabled","disabled");
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	