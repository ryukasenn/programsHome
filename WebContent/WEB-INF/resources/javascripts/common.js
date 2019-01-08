
$(function(){
	(function ($) {
		$.extend({
			nbptEvent: {
				bind : function(target, event, func){// target为事件对象的id
					$("#" + target).off(event).on(event, func)
				}
			},
			nbptAjax : {
				get : function(targetUrl, param, func){
					var ajaxBackData = '', asyncFlag;
		        	if(func == undefined ){asyncFlag = false}
		        	else if(typeof(func) === 'function'){asyncFlag = true}
					else if(typeof(func) === 'boolean'){if(func) asyncFlag = false; else asyncFlag = true}
					$.ajax({timeout : 600000, type : "GET", url : targetUrl, data : param || {}, async : asyncFlag, traditional:true,
						success : function(data){
				        	try{
				        		var back = JSON.parse(data);
				        		if('OK' === back.state){
			    	        		back.message && $.nbptMsg.alert(back.message);
						        	if(!asyncFlag) {
						        		ajaxBackData = back.data && JSON.parse(back.data) || ""
						        		return;
						        	}
						        	func && func(JSON.parse(back.data));
								} else {
									back.message && $.nbptMsg.alert(back.message) || $.nbptMsg.alert();
								}
				        	}
				        	catch(err){
				        		ajaxBackData = false;
				        		console.log(err)
					        	$.nbptMsg.alert();
				        	}
				        },
				        error : function(){
				        	$.nbptMsg.alert();
				        },
				        beforeSend : function(){
				        	$("#waitModal").modal({backdrop:'static'})
				        },
				        complete : function(XHR,TS){
				        	$("#waitModal").modal('hide')
				        }
				    });
					return ajaxBackData;
				},
				post : function(targetUrl, param, func){
					var ajaxBackData = '', asyncFlag;
		        	if(func == undefined ){asyncFlag = false}
		        	else if(typeof(func) === 'function'){asyncFlag = true}
					else if(typeof(func) === 'boolean'){if(func) asyncFlag = false; else asyncFlag = true}
					$.ajax({timeout : 600000, type : "POST", url : targetUrl, data : param, async : asyncFlag, traditional:true,
				        success : function(data){
				        	try{
				        		var back = JSON.parse(data);
				        		if('OK' === back.state){
			    	        		back.message && $.nbptMsg.alert(back.message);
						        	if(!asyncFlag) {
						        		ajaxBackData = back.data && JSON.parse(back.data) || ""
						        		return;
						        	}
						        	func && func(JSON.parse(back.data));
								} else {
									back.message && $.nbptMsg.alert(back.message) || $.nbptMsg.alert();
								}
				        	}
				        	catch(err){
				        		ajaxBackData = false;
				        		console.log(err)
					        	$.nbptMsg.alert();
				        	}
				        },
				        error : function(){
				        	$.nbptMsg.alert();
				        },
				        beforeSend : function(){
				        	$("#waitModal").modal({backdrop:'static'})
				        },
				        complete : function(XHR,TS){
				        	$("#waitModal").modal('hide')
				        }
				    });
					return ajaxBackData;
				},
				silencePost : function(targetUrl, param, func){
					var ajaxBackData = '', asyncFlag;
		        	if(func == undefined ){asyncFlag = false}
		        	else if(typeof(func) === 'function'){asyncFlag = true}
					else if(typeof(func) === 'boolean'){if(func) asyncFlag = false; else asyncFlag = true}
					$.ajax({timeout : 600000, type : "POST", url : targetUrl, data : param, async : asyncFlag, traditional:true, 
				        success : function(data){
				        	try{
				        		var back = JSON.parse(data);
				        		if('OK' === back.state){
						        	if(!asyncFlag) {
						        		ajaxBackData = back.data && JSON.parse(back.data) || ""
						        		return;
						        	}
						        	func && func(JSON.parse(back.data));
			    	        		back.message && $.nbptMsg.alert(back.message);
								} else {
									back.message && $.nbptMsg.alert(back.message) || $.nbptMsg.alert();
								}
				        	}
				        	catch(err){
				        		ajaxBackData = false;
				        		console.log(err)
					        	$.nbptMsg.alert();
				        	}
				        },
				        error : function(){
				        	$.nbptMsg.alert();
				        },
				    });
					return ajaxBackData;
				},
				jsonPost : function(targetUrl, param, func){
					var ajaxBackData = '', asyncFlag;
		        	if(func == undefined ){asyncFlag = false}
		        	else if(typeof(func) === 'function'){asyncFlag = true}
					else if(typeof(func) === 'boolean'){if(func) asyncFlag = false; else asyncFlag = true}
					$.ajax({timeout : 600000, type : "POST", url : targetUrl, data : param, async : asyncFlag, traditional:true,
						contentType : "application/json",
				        success : function(data){
				        	try{
				        		var back = JSON.parse(data);
				        		if('OK' === back.state){
			    	        		back.message && $.nbptMsg.alert(back.message);
						        	if(!asyncFlag) {
						        		ajaxBackData = back.data && JSON.parse(back.data) || ""
						        		return;
						        	}
						        	func && func(JSON.parse(back.data));
								} else {
									back.message && $.nbptMsg.alert(back.message) || $.nbptMsg.alert();
								}
				        	}
				        	catch(err){
				        		ajaxBackData = false;
				        		console.log(err)
					        	$.nbptMsg.alert();
				        	}
				        },
				        error : function(){
				        	$.nbptMsg.alert();
				        },
				        beforeSend : function(){
				        	$("#waitModal").modal({backdrop:'static'})
				        },
				        complete : function(XHR,TS){
				        	$("#waitModal").modal('hide')
				        }
				    });
					return ajaxBackData;
				},
				silenceJsonPost : function(targetUrl, param, func){
					var ajaxBackData = '', asyncFlag;
		        	if(func == undefined ){asyncFlag = false}
		        	else if(typeof(func) === 'function'){asyncFlag = true}
					else if(typeof(func) === 'boolean'){if(func) asyncFlag = false; else asyncFlag = true}
					$.ajax({timeout : 600000, type : "POST", url : targetUrl, data : param, async : asyncFlag, traditional:true,
						contentType : "application/json",
				        success : function(data){
				        	try{
				        		var back = JSON.parse(data);
				        		if('OK' === back.state){
						        	if(!asyncFlag) {
						        		ajaxBackData = back.data && JSON.parse(back.data) || ""
						        		return;
						        	}
						        	func && func(JSON.parse(back.data));
			    	        		back.message && $.nbptMsg.alert(back.message);
								} else {
									back.message && $.nbptMsg.alert(back.message) || $.nbptMsg.alert();
								}
				        	}
				        	catch(err){
				        		ajaxBackData = false;
				        		console.log(err)
					        	$.nbptMsg.alert();
				        	}
				        },
				        error : function(){
				        	$.nbptMsg.alert();
				        },
				    });
					return ajaxBackData;
				},
			},
			nbptMsg : {
				_alertContainer : null,
				_confirmContainer : null,
				_downloadContainer : null,
				_getAlertContainer : function(){
					if($.nbptMsg._alertContainer){
						return $.nbptMsg._alertContainer;
					}
					$.nbptMsg._alertContainer = $.nbptModal._createNewModal('AlertModal');
					$.nbptMsg._alertContainer.find(".modal-footer")
						.append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="AlertModalSure">确定</buttion>'))
					return $.nbptMsg._alertContainer;
				},
				_getConfirmContainer : function(){
					if($.nbptMsg._confirmContainer){
						return $.nbptMsg._confirmContainer;
					}

					$.nbptMsg._confirmContainer = $.nbptModal._createNewModal('ConfirmModal');
					$.nbptMsg._confirmContainer.find(".modal-footer")
						.append($('<button type="button" data-dismiss="modal" class="btn btn-default" id="ConfirmModalCancel">取消</buttion>'))
						.append($('<button type="button" class="btn btn-primary" id="ConfirmModalSure">确定</buttion>'))
					return $.nbptMsg._confirmContainer;
				},
				_getDownloadContainer : function(){
					if($.nbptMsg._confirmContainer){
						return $.nbptMsg._confirmContainer;
					}
					$.nbptMsg._confirmContainer = $.nbptModal._createNewModal('DownloadModal');
					$.nbptMsg._confirmContainer.find(".modal-footer")
						.append($('<button type="button" data-dismiss="modal" class="btn btn-default" id="DownloadCancel">取消</buttion>'))
						.append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="DownloadSure">确定</buttion>'))
					return $.nbptMsg._confirmContainer;
				},
				alert : function(msg){
					var container = $.nbptMsg._getAlertContainer();
					container.find('.modal-body').empty();
					msg && container.find('.modal-body').append($("<p>" + msg + "</p>")) || container.find('.modal-body').append($("<p>请重新登录重试,仍然出现错误,请联系后台管理员</p>"));
					$.nbptMsg._alertContainer.modal({backdrop:'static'})
					return true;
				},
				confirm : function(options){
					var container = $.nbptMsg._getConfirmContainer();
					container.find('.modal-body').empty();
					if('string' === typeof(options)){
						container.find('.modal-body').append($("<p>" + options + "</p>")) && $.nbptMsg._confirmContainer.modal({backdrop:'static'})
					} else {
						container.find('.modal-body').append($("<p>" + options.msg + "</p>"));
						$.nbptMsg._confirmContainer.find("#ConfirmModalSure").eq(0).off('click').on('click', 
								function(){options.sureCallback && options.sureCallback() ;$.nbptMsg._confirmContainer.modal("hide")});
						$.nbptMsg._confirmContainer.find("#ConfirmModalCancel").eq(0).off('click').on('click', 
								function(){options.cancelCallback && options.cancelCallback()});
						$.nbptMsg._confirmContainer.modal({backdrop:'static'})
					}
					return true;
				},
				download : function(options){
					if('string' === typeof(options)){
						window.location.href = options;
					} else {
						var container = $.nbptMsg._getDownloadContainer();
						container.find('.modal-body').append($("<p>" + options.msg + "</p>"));
						$.nbptMsg._confirmContainer.find("#ConfirmModalSure")
						.eq(0).off('click').on('click', 
								function(){window.location.href = options.url});
						$.nbptMsg._downloadContainer.modal({backdrop:'static'})
					}
				},
				close : function(){
					$.nbptMsg._alertContainer && $.nbptMsg._alertContainer.modal('hide')
					$.nbptMsg._confirmContainer && $.nbptMsg._confirmContainer.modal('hide')
					$.nbptMsg._downloadContainer && $.nbptMsg._downloadContainer.modal('hide')
				},
			},
			nbptCommon : {
				isIdCard : function (idNum){
					if(idNum.length != 18){return false;}
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
				},
				extend : function(oldObj, newObj) { // obj赋值方法
					for(var key in newObj) {
						oldObj[key] = newObj[key];
					}
					return oldObj;
				},
				formItemsToParams : function(form){ // 将form中的input转化成obj, form是一个jQueryForm对象
					var params = {};
					var items = form.find(":text");
					for(var i = 0; i < items.length; i++){
						var key = items.eq(i).attr('name');
						params[key] = items.eq(i).val() && items.eq(i).val().trim() || '';
					}
					var selects = form.find(":selected")
					for(var i = 0; i < selects.length; i++){
						var key = selects.eq(i).parent().attr('name');
						params[key] = selects.eq(i).val() && selects.eq(i).val().trim() || '';
					}
					var radios = form.find(":checked")
					for(var i = 0; i < radios.length; i++){
						var key = radios.eq(i).attr('name');
						params[key] = radios.eq(i).val() && radios.eq(i).val().trim() || '';
					}
					var hiddens = form.find("input[type=hidden]")
					for(var i = 0; i < hiddens.length; i++){
						var key = hiddens.eq(i).attr('name');
						params[key] = hiddens.eq(i).val() && hiddens.eq(i).val().trim() || '';
					}
					var files = form.find(":file");;
					for(var i = 0; i < files.length; i++){
						var key = files.eq(i).attr('name');
						params[key] = files[i].files[0];
					}
					var passwords = form.find(":password");;
					for(var i = 0; i < passwords.length; i++){
						var key = passwords.eq(i).attr('name');
						params[key] = passwords.eq(i).val() && passwords.eq(i).val().trim() || '';
					}
					var nesscesaryChecks = form.find(".nesscesary").not(":radio");;
					for(var i = 0; i < nesscesaryChecks.length; i++){
						$_this = $(nesscesaryChecks[i])
						if(null == $_this.val() || undefined == $_this.val() || '' == $_this.val()){$.nbptMsg.alert("请填写必填项");return;}
					}
					var hasErrors = form.find(".has-error");
					if(hasErrors.length > 0) {$.nbptMsg.alert("请检查相关错误");return;}
					return params;
				},
			},
			nbptModal : {
				_formModalOptions : function(){
					return {
						title : '测试',
						formId : 'newForm',
//						columns : [{id : 'ceshi1', label : '测试1', },{id : 'ceshi2', label : '测试2'}]
					}
				},
				_createNewModal : function(modalId, hasHead, size, index){
					$("#" + modalId).remove();
					// 获取屏幕高度
					var currentHeight = $(window).height()
					var htmls = new Array();
					htmls.push('<div role="dialog" style="z-index:'+ (index || 1050) +'" aria-labelledby="'+ modalId +'ModalLabel" tabindex="-1" class="modal fade" id="'+ modalId +'">');
					size && htmls.push('<div class="modal-dialog modal-'+ size +'" role ="document">') || htmls.push('<div class="modal-dialog" role ="document">');
					htmls.push('<div class="modal-content">');
					hasHead && htmls.push('<div class="modal-header"></div>');
					htmls.push('<div class="modal-body" style="max-height:' + (currentHeight*0.6) + 'px"></div>');
					htmls.push('<div class="modal-footer">');
					htmls.push('</div></div></div></div>');
					return $(htmls.join(""));
				},
				createFormModal : function(options){
					var _options = $.nbptCommon.extend($.nbptModal._formModalOptions(),options);
					if(!_options.columns){
						return;
					} 
					$("#" + _options.formId).remove();
					var container = $.nbptModal._createNewModal(_options.formId, true, "lg", 1049);
					_options.help && container.find('.modal-header').append(_options.help)
					
					container.find('.modal-header').append($('<h5>'+ _options.title +'</h5>'));
					var _form = $("<form role='form' class='form-horizontal' id='" + _options.formId + "'></form>").appendTo(container.find('.modal-body'));
					for(var i = 0; i < _options.columns.length; i++){
						var items = new Array();
						if(_options.columns[i].type === 'hidden'){
							items.push('<input class="form-control" name="'+ _options.columns[i].id +'" ');
							_options.columns[i].type && items.push('type="' + _options.columns[i].type + '" ');
							_options.columns[i].readOnly && items.push('readOnly ');
							_options.columns[i].value && items.push('value="' + _options.columns[i].value + '" ');
							_options.columns[i].placeholder && items.push('value="' + _options.columns[i].placeholder + '" ');
							items.push('/>');
							$(items.join('')).appendTo(_form);
							continue;
						}
						items.push('<div class="row"><div class="form-group">');
						_options.columns[i].type !== 'hidden' 
							&& items.push('<label class="col-xs-2-5 control-label" for="'+ _options.columns[i].id +'">' + _options.columns[i].label);
						_options.columns[i].nesscesary && items.push('<font color="red">&nbsp&nbsp*</font>');
						items.push('</label>');
						items.push('<div class="col-xs-9">')
						if(_options.columns[i].type === 'select'){
							items.push('<select class="form-control" name="'+ _options.columns[i].id +'">');
							for(var j = 0; j < _options.columns[i].options.length; j++){
								_options.columns[i].options[j].checked 
								&& items.push('<option value="'+ _options.columns[i].options[j].value +'" selected="selected">'+ _options.columns[i].options[j].name +'</option>')
								|| items.push('<option value="'+ _options.columns[i].options[j].value +'">'+ _options.columns[i].options[j].name +'</option>');
							}
							items.push('</select>')
						} 
						else if(_options.columns[i].type === 'radio'){
							for(var j = 0; j < _options.columns[i].radios.length; j++){
								items.push("<div class='layoutradio layoutradio-info radio-inline'>");
								items.push("<input type='radio' id='" + _options.formId + _options.columns[i].id + "_" + j + "' ");
								_options.columns[i].radios[j].checked && items.push("checked ");
								items.push("name='"+ _options.columns[i].id +"' ");
								items.push("value='" + _options.columns[i].radios[j].value + "' />");
								items.push("<label for='" + _options.formId + _options.columns[i].id + "_" + j + "'>");
								items.push(_options.columns[i].radios[j].showName + "</label>");
								items.push("</div>");
							}
						}
						else if(_options.columns[i].type === 'date'){
							items.push('<input class="form-control daySelect" name="'+ _options.columns[i].id +'" ');
							items.push('type="text" ');
							_options.columns[i].readOnly && items.push('readOnly ');
							_options.columns[i].value && items.push('value="' + _options.columns[i].value + '" ');
							items.push('/>');
						}
						else if(_options.columns[i].type === 'textarea'){
							items.push('<textarea class="form-control" name="'+ _options.columns[i].id +'" ');
							items.push('autocomplete="off" ');
							items.push('type="' + _options.columns[i].type + '" ');
							items.push('row="3" ');
							_options.columns[i].readOnly && items.push('readOnly ');
							_options.columns[i].value && items.push('value="' + _options.columns[i].value + '" ');
							_options.columns[i].placeholder && items.push('placeholder="' + _options.columns[i].placeholder + '" ');
							items.push('/>');
						}
						else {
							items.push('<input class="form-control" name="'+ _options.columns[i].id +'" ');
							items.push('autocomplete="off" ');
							items.push('type="text" ');
							_options.columns[i].readOnly && items.push('readOnly ');
							_options.columns[i].value && items.push('value="' + _options.columns[i].value + '" ');
							_options.columns[i].placeholder && items.push('placeholder="' + _options.columns[i].placeholder + '" ');
							items.push('/>');
						}
						items.push('</div></div></div>');
						var currentItem = $(items.join('')).appendTo(_form);
						_options.columns[i].type === 'date' && 
						currentItem.find(".daySelect")
						.datetimepicker({autoclose: true,language: 'zh',format: 'yyyy-mm-dd',minView: 'month',todayHighlight:true})
						.on('changeDate', function(ev){$(this).focusout()})
						_options.columns[i].type === 'idCard' 
							&& currentItem.find("input").addClass('special') 
							&& currentItem.find("input").on("focusout"
								, function(){
									var $_this = $(this);
									currentIdCardCheck && currentIdCardCheck.apply($_this)
									if(!currentIdCardCheck){
										$.nbptCommon.isIdCard($_this.val()) && $_this.parents(".form-group").removeClass("has-error")
										|| $_this.parents(".form-group").addClass("has-error")
									}
								})
						_options.columns[i].nesscesary && currentItem.find("input").addClass("nesscesary") 
						_options.columns[i].nesscesary && currentItem.find(".nesscesary").not(":radio").not(".special").on("focusout"
								, function(){
									var $_this = $(this);
									$_this.val() === ''
									&& $_this.parents(".form-group").addClass("has-error")
									|| $_this.parents(".form-group").removeClass("has-error");})
						
						
					}
					container.find(".modal-footer")
					.append($('<button type="button" data-dismiss="modal" class="btn btn-default" id="'+ _options.formId +'Cancel">取消</buttion>'))
					.append($('<button type="button" class="btn btn-primary" id="'+ _options.formId +'Sure">确定</buttion>'))
					container.find("#" +_options.formId + "Sure")
						.eq(0).off('click').on('click'
								, function(){ _options.sureCallback && _options.sureCallback() && $("#" + _options.formId).modal("hide");});
					container.find("#" +_options.formId + "Cancel")
						.eq(0).off('click').on('click', function(){_options.cancelCallback && _options.cancelCallback();});
					container.modal({backdrop:'static'});
				},
			},
			nbptTable : {
				_tableContent : null,
				_selectedData : null,
				_initOptions : function() {
						return {
						target : "ryuukasennTableContent",
						data : null,
						url : null,
						hasSelect : false,
						hasChecked : false,
						onDblClickRow   : function(row){return false},
						onClickRow      : function(row){return false},
						onLoadSuccess   : function(row){return false},
						onAll           : function(row){return false},
						onChangeClick   : function(row){return false},
						onAddClick      : function(row){return false},
						onDeleteClick   : function(row){return false},
						}
				},
				_options : {
				},
				_EVENTS : {
					'all.bs.table'              : 'onAll',
			        'click-row.bs.table'        : 'onClickRow',
			        'dbl-click-row.bs.table'    : 'onDblClickRow',
			        'load-success.bs.table'     : 'onLoadSuccess',
			        'load-error.bs.table'       : 'onLoadError',
			        'changeclick.bs.table'      : 'onChangeClick',
			        'addclick.bs.table'         : 'onAddClick',
			        'deleteclick.bs.table'      : 'onDeleteClick',
				},
				_currentDatas : {},
				createTable : function(options){
					$.nbptTable._initTbale(options);
					$.nbptTable._loadData(options.data);
				},
				createRegionTable : function(options){
					$.nbptTable._initTbale(options);
					$.nbptTable._initData();
					$.nbptTable._initRegionBody();
				},
				getSelected : function(){
					return $.nbptTable._selectedData;
				},
				getRowData : function(){
					return $.nbptTable._selectedData;
				},
				_initRegionBody : function(){
					var $_tableBody = $.nbptTable._tableContent.find(".nbptTableBody").find("table");
					var $_tbody = $("<tbody></tbody>").appendTo($_tableBody);
					var rowData = $.nbptTable._currentDatas;
					for(var trsi=0; trsi< rowData.rows; trsi++){
						var tdContent = rowData.data[trsi];
						var tr = new Array();
						var level = tdContent.SECTION_HTMLID.split("_").length - 2;
						if(tdContent.SECTION_ISPARENT === 'true'){
							tr.push("<tr data-index='" + trsi + "' id='" + tdContent.SECTION_HTMLID + "'>");
							tr.push("<td class='col-md-4 col-sm-4' style='text-align:left;'>");
							tr.push("<span class='treetable-expander glyphicon glyphicon-chevron-right'></span>");
						} else {
							tr.push("<tr data-index='" + trsi + "' id='" + tdContent.SECTION_HTMLID + "' style='display : none'>");
							tr.push("<td class='col-md-4 col-sm-4' style='text-align:left;'>");
							tr.push("<span class='treetable-indent'></span>");
						}
						for(var i = 0 ; i < level; i++){
							tr.push("<span class='treetable-indent'></span>")
						}
						tr.push(tdContent.SECTION_NAME + "</td>");
						tr.push("<td class='col-md-2 col-sm-2' style='text-align:center;'>");
						tdContent.SECTION_FLAG == '1' && tr.push("<span class='badge badge-success'>正常</span>") || tr.push("<span class='badge badge-danger'>停用</span>")
						tr.push("</td>");						
						
						tr.push("<td class='col-md-2 col-sm-2' style='text-align:center;'>" + (tdContent.SECTION_CREATETIME || 0) +"</td>");
						
						tr.push("<td class='col-md-2 col-sm-2' style='text-align:center;'>");
						tdContent.SECTION_CHANGEABLE && tr.push("<button class='btn btn-xs btn-link'><span class='glyphicon glyphicon glyphicon-pencil'>修改</span></button>");
						tdContent.SECTION_ADDABLE && tr.push("<button class='btn btn-xs btn-link'><span class='glyphicon glyphicon glyphicon glyphicon-plus'>添加下级</span></button>");
						tdContent.SECTION_DELETEABLE && tr.push("<button class='btn btn-xs btn-link'><span class='glyphicon glyphicon glyphicon glyphicon-minus'>删除</span></button>");
						tr.push("</td>");
						$(tr.join("")).appendTo($_tbody);
					}
					var $_ths = $.nbptTable._tableContent.find(".nbptTableHead").find("table").children().find('th');
					var $_tds = $_tbody.find("> tr[data-index]").eq(0).find("td");
					for(var i = 0 ; i < $_ths.length; i ++){
						$_ths.eq(i).css("width", $_tds.eq(i).outerWidth(true));
					}
					$_tbody.on("click", ".glyphicon-chevron-right", function(e){
						var $_target = $(e.target);
						$_target.removeClass('glyphicon-chevron-right');
						$_target.addClass('glyphicon-chevron-down');
						var chiledren = $("tr[id^='" + $_target.parent().parent().attr("id") + "_']");
						$.each(chiledren,function(i,o){
							$(o).css("display", "");
						})
						return false;
					})
					$_tbody.on("click", ".glyphicon-chevron-down", function(e){
						var $_target = $(e.target);
						$_target.removeClass('glyphicon-chevron-down');
						$_target.addClass('glyphicon-chevron-right');
						var chiledren = $("tr[id^='" + $_target.parent().parent().attr("id") + "_']");
						$.each(chiledren, function(i,o){
							$(o).css("display", "none");
						})
						return false;
					})
					$_tbody.find("> tr[data-index] > td").off('click').on('click', function(e){
						var $td = $(this);
						var $tr = $td.parent();
						var rowData = $.nbptTable._currentDatas.data[$tr.data('index')];
			            $.nbptTable._trigger('click-row', rowData);
			            $.nbptTable._selectedData = rowData;
					})
					
					$_tbody.find(".glyphicon-pencil").off('click').on('click', function(e){
						var $td = $(this).parent().parent();
						var $tr = $td.parent();
						var rowData = $.nbptTable._currentDatas.data[$tr.data('index')];
			            $.nbptTable._trigger('changeclick', rowData);
					})
					
					$_tbody.find(".glyphicon-plus").off('click').on('click', function(e){
						var $td = $(this).parent().parent();
						var $tr = $td.parent();
						var rowData = $.nbptTable._currentDatas.data[$tr.data('index')];
			            $.nbptTable._trigger('addclick', rowData);
					})
					
				},
				_initTbale : function(options){
					// 初始化参数
					$.nbptTable._options = $.nbptCommon.extend($.nbptTable._initOptions() , options);
					// 初始化table
					$.nbptTable._tableContent = $.nbptTable._tableContent || $("." + $.nbptTable._options.target);
					$.nbptTable._selectedData = null;
					$.nbptTable._tableContent && $.nbptTable._tableContent.empty();
					var tableHead = $("<div class='nbptTableHead'><table class='table'></table></div>").appendTo($.nbptTable._tableContent);
					var tableBody = $("<div class='nbptTableBody'><table class='table table-hover'></table></div>").appendTo($.nbptTable._tableContent);
					
					// 生成头部
					var $_tableHead_head = $("<thead></thead>").appendTo(tableHead.find("table"));
					var $_thead_tr = $("<tr></tr>").appendTo($_tableHead_head);
					$.nbptTable._options.hasSelect && $("<th  style='text-align:center;'></th>").appendTo($_thead_tr); // 单选列
					var columns = $.nbptTable._options.columns;
					for(var i = 0; i < columns.length; i++){ // 其他列
						$("<th  style='text-align:center;'>" + columns[i].title + "</th>").appendTo($_thead_tr);
					}
					$("<th></th>").appendTo($_thead_tr);
				},
				_loadData : function(data){ // 加载数据
					
					// 1.获取数据
					$.nbptTable._initData(data);
					
					// 2.插入数据
					$.nbptTable._initBody();
				},
				_initData : function(data){
					try{
						var insertDatas = data || $.nbptAjax.post($.nbptTable._options.url,$.nbptTable._options.urlParam || {},true);
						if($.isArray(insertDatas)) {
							$.nbptTable._currentDatas.rows = insertDatas.length;
							$.nbptTable._currentDatas.data = insertDatas
						} else {
							$.nbptTable._currentDatas = insertDatas;
						}
					}catch(err){
						console.log(err);
					}
				},
				_initBody : function(){
					var $_tableBody = $.nbptTable._tableContent.find(".nbptTableBody").find("table");
					var $_tbody = $("<tbody></tbody>").appendTo($_tableBody);
					var rowData = $.nbptTable._currentDatas;
					for(var trsi=0; trsi< rowData.rows; trsi++){
						var $_tbody_tr = $("<tr data-index='"+ trsi +"'></tr>").appendTo($_tbody);
						
						$.nbptTable._options.hasSelect && $("<td style='width:5px' ><input type='radio' id='ryuukasennTableSelect'"
								+ trsi +" name='nbptTableSelect' /></td>").appendTo($_tbody_tr);
						for(var i=0; i < $.nbptTable._options.columns.length; i++){
							var tdHtml = new Array();
							
							var tdContent = rowData.data[trsi];
							var key = $.nbptTable._options.columns[i].id;
							tdHtml.push("<td style='text-align:center'>");
							$.nbptTable._options.columns[i].func || tdHtml.push(tdContent[key]);
							$.nbptTable._options.columns[i].func && 
								tdHtml.push($.nbptTable._options.columns[i].func.apply(tdContent));
							tdHtml.push("</td>");
							$(tdHtml.join("")).appendTo($_tbody_tr);
						}
					}
					var $_ths = $.nbptTable._tableContent.find(".nbptTableHead").find("table").children().find('th');
					var $_tds = $_tbody.find("> tr[data-index]").eq(0).find("td");
					for(var i = 0 ; i < $_ths.length; i ++){
						$_ths.eq(i).css("width", $_tds.eq(i).outerWidth(true));
					}
					$_tbody.find("> tr[data-index] > td").off('click').on('click', function(e){
						var $td = $(this);
						var $tr = $td.parent();
						var rowData = $.nbptTable._currentDatas.data[$tr.data('index')];
			            $.nbptTable._trigger('click-row', rowData);
			            $tr.find("input[type='radio']").prop('checked',true);
			            $.nbptTable._selectedData = rowData;
					})
				},
			    _trigger : function (name) {
			        var args = Array.prototype.slice.call(arguments, 1);
			        name += '.bs.table';
			        $.nbptTable._options[$.nbptTable._EVENTS[name]].apply($.nbptTable._options, args);
			        $.nbptTable._tableContent.trigger($.Event(name), args);
			        $.nbptTable._options.onAll(name, args);
			        $.nbptTable._tableContent.trigger($.Event('all.bs.table'), [name, args]);
			    }
			}
		})
	})(jQuery);
	$("form").on('submit', function(){
		
		if($(this).hasClass('download')){
			return;
		}
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

//	            $("#" + $(this).val()).table2excel({
//	                // 不被导出的表格行的CSS class类
//	                exclude: ".noExl",
//	                // 导出的Excel文档的名称，（没看到作用）
//	                name: "Excel Document Name",
//	                // Excel文件的名称
//	                filename: "myExcelTable.xls"
//	            });

		tableToExcel($(this).val());
	})
	
	if($(".daySelect").length > 0){
		
		$(".daySelect").datetimepicker({
			autoclose: true,
			language: 'zh',
			format: 'yyyy-mm-dd',
			minView: 'month',
			todayHighlight:true
		});
		
		if($(".daySelect").val() === ''){
			// 初始化时间
			var nDate = new Date();
			var dateString = nDate.getFullYear() + '-' 
							+ ((nDate.getMonth() + 1) < 10 ? '0' + (nDate.getMonth() + 1) : (nDate.getMonth() + 1)) + '-' 
							+ (nDate.getDate() < 10 ? '0' + nDate.getDate() : nDate.getDate())
			$(".daySelect").val(dateString);
			}
	}	
	
	if($(".province-select").length > 0){
		
		AjaxForPost(baseUrl + "/sellPersonnel/receiveXzqxs", {}, function(jsonData){
			for(var i = 0; i < jsonData.length; i++){
				$(
				"<option value='" 
				+ jsonData[i].NBPT_COMMON_XZQXHF_ID 
				+ "'>" 
				+ jsonData[i].NBPT_COMMON_XZQXHF_NAME 
				+ "</option>")
				.appendTo($(".province-select"))
			}
		});
		
		$(".province-select").on('change', function(){
			
			$_this = $(this);
			$_this.parents(".form-group").find(".city-select").empty()
			$_this.parents(".form-group").find(".conty-select").empty();
			AjaxForPost(baseUrl + "/sellPersonnel/receiveXzqxs", {parentId : $_this.val()}, function(jsonData){
				for(var i = 0; i < jsonData.length; i++){
					$(
					"<option value='" 
					+ jsonData[i].NBPT_COMMON_XZQXHF_ID 
					+ "'>" 
					+ jsonData[i].NBPT_COMMON_XZQXHF_NAME 
					+ "</option>")
					.appendTo($_this.parents(".form-group").find(".city-select"))
				}
			});
		})
		
		$(".city-select").on('change', function(){			
			$_this = $(this);
			$_this.parents(".form-group").find(".conty-select").empty();
			AjaxForPost(baseUrl + "/sellPersonnel/receiveXzqxs", {parentId : $_this.val()}, function(jsonData){
				for(var i = 0; i < jsonData.length; i++){
					$(
					"<option value='" 
					+ jsonData[i].NBPT_COMMON_XZQXHF_ID 
					+ "'>" 
					+ jsonData[i].NBPT_COMMON_XZQXHF_NAME 
					+ "</option>")
					.appendTo($_this.parents(".form-group").find(".conty-select"))
				}
			});
		})
	}
	
	
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
	function AjaxForGet(targetUrl, param, func, contentType){

		$.ajax({
	        timeout : 600000,
	        type : "GET",
	        url : targetUrl,
	        data : contentType === 'json' && JSON.stringify(param) || param,
	        success : function(data){
	        	
	        	func(JSON.parse(data));
	        },
	        error : function(){
	        	new Confirm({type : 'alert'})
	        },
	        beforeSend : function(XHR){
	        	if(contentType === 'json' ){
		        	this.contentType = 'application/json;charset=UTF-8';}
	        		
	    		$("#waitModal").modal({
	    			backdrop:'static'
	    		})
	        },
	        complete : function(XHR,TS){

	        	$("#waitModal").modal('hide')
	        }
	        //注意：这里不能加下面这行，否则数据会传不到后台
	        //contentType:'application/json;charset=UTF-8',
	    });
	}


	/**
	 * Ajax的GET提交
	 * @param targetUrl
	 * @param param
	 * @param func
	 * @returns
	 */
	function AjaxForPost(targetUrl, param, func, contentType){
	
		$.ajax({
	        timeout : 600000,
	        type : "POST",
	        url : targetUrl,
	        data : param,
	        traditional:true,
	        success : function(data){
	        	try{
	        		var back = JSON.parse(data)
		        	if(back.state){
		        		if('OK' === back.state){
		        			func(JSON.parse(back.data));
		    	        	$("#waitModal").modal('hide');
		    	        	if('' !== back.message){
								new Confirm({message : back.message, type : 'alert'})
		    	        	}
						} else {
							new Confirm({message : back.message, type : 'alert'})
						}
		        	} else {
	        			func(back);
		        	}}
	        	catch(err){
	        		console.log(err)
	        	}
	        },
	        error : function(){
	        	$("#waitModal").modal('hide')
	        	new Confirm({'type' : 'alert'})
	        },
	        beforeSend : function(XHR){
	        	if(contentType === 'json' ){
		        	this.contentType = 'application/json;charset=UTF-8';
		        	this.data = JSON.stringify(param)
	        	}
	    		$("#waitModal").modal({backdrop:'static'})
	        },
	        complete : function(XHR,TS){
	        	$("#waitModal").modal('hide')
	        }
	        //注意：这里不能加下面这行，否则数据会传不到后台
	        //contentType:'application/json;charset=UTF-8',
	    });
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
            "message": "出错,请联系后台管理员",
            "cancelCallBack": "",
            "sureCallBack": "",
            "focusTarget" : "",
            "hasHead" : true
        };

        // 扩展默认属性
        options && this.extend(this.configs, options);
        
        // 初始化方法
        this.init();
        // 事件添加
        this.sureBtn   && this.addEvent(this.sureBtn,   "click", this.btnClick.bind(this));
        this.cancelBtn && this.addEvent(this.cancelBtn, "click", this.btnClick.bind(this));
		
		$("#ConfirmModal").modal({backdrop:'static'})
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
			
			if(config.hasHead){
				
				this.contain.find(".modal-body").before($("<div class='modal-header'>" +
						"<button class='close' data-dismiss='modal' type='button'>" +
						"<span aria-hidden='true'>&times</span></button>" +
						"</div>"))
			}

			this.contain.find('.modal-body').append($("<p>" + config.message + "</p>"));
			this.contain.appendTo($('body'));
			
			switch (config.type){
			
				case "alert" :
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="ConfirmModalSure">确定</buttion>'));
					this.sureBtn   = this.contain.find('.btn-primary')[0];
					break;
				case "download" :
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-default" id="ConfirmModalDispaly">页面展示</buttion>'));
					this.contain.find(".modal-footer").append($('<button type="button" data-dismiss="modal" class="btn btn-primary" id="ConfirmModalDownload">直接下载</buttion>'));
					this.sureBtn   = this.contain.find('.btn-primary')[0];
					this.cancelBtn = this.contain.find('.btn-default')[0];
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
                } break;
                case "ConfirmModalDispaly":{
                    _this.close();
                    _configs.cancelCallBack && _configs.cancelCallBack('display');
                } break;
                case "ConfirmModalDownload": {
                    _this.close();
                    _configs.sureCallBack && _configs.sureCallBack('download');
                } break;
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
	 * @param items,只读项目的列表
	 * @returns
	 */
	function setReadOnly(items){
		
		if(undefined != items.inputItems || null != items.inputItems){
			
			var inputItems = items.inputItems;
			// 遍历设置只读的input
			for(var nameI = 0; nameI < inputItems.length; nameI++){				
				var $_checkItem = $("input[name='" + inputItems[nameI] + "']");// 获取只读项
				$_checkItem.attr("readOnly","readOnly");}}
		if(undefined != items.radioItems || null != items.radioItems){			
			var radioItems = items.radioItems;			
			for(var nameI = 0; nameI < radioItems.length; nameI++){// 遍历设置只读的radio				
				var $_checkItem = $("input[name='" + radioItems[nameI] + "']");// 获取只读项
				$_checkItem.attr("disabled","disabled");}}
		if(undefined != items.selectItems || null != items.selectItems){
			var selectItems = items.selectItems;
			// 遍历设置只读的select
			for(var nameI = 0; nameI < selectItems.length; nameI++){				
				// 获取只读项
				var $_checkItem = $("input[name='" + selectItems[nameI] + "']");				
				$_checkItem.attr("disabled","disabled");}}}
	
	/**
	 * 复制obj
	 * @param oldObj
	 * @param newObj
	 * @returns
	 */
	function extend(oldObj, newObj) {
		for(var key in newObj) {
			oldObj[key] = newObj[key];}
		return oldObj;}
	
	/**
	 * bootstrap-table 初始化参数
	 * @param params
	 * @returns
	 */
	function tableParams (params) {
		var flag = true;
		if(!(params.columns && params.columns instanceof Array)){
			flag = false;}
		if (params.sidePagination === 'client'){
			if(!(params.queryParams && params.queryParams instanceof Function)){
				flag = false;}
			if(params.queryUrl === ''){
				flag = false;}}
		if (!flag){
			new Confirm({type : 'alert'})
			return null;}
		var obj = {
				url: params.queryUrl,                      //请求后台的URL（*）
	            method: params.method || 'POST',                      //请求方式（*）
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）
	            sidePagination: params.sidePagination || "server",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
	            pageSize: 13,                     //每页的记录行数（*）
	            pageList: [],        //可供选择的每页的行数（*）
	            search: false,                      //是否显示表格搜索
	            strictSearch: false,
	            showColumns: false,                  //是否显示所有的列（选择显示的列）
	            showRefresh: false,                  //是否显示刷新按钮
	            minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: true,                //是否启用点击选中行
	            uniqueId: params.uniqueId || "",    //每一行的唯一标识，一般为主键列
	            showToggle: false,                  //是否显示详细视图和列表视图的切换按钮
	            cardView: false,                    //是否显示详细视图
	            detailView: false,                  //是否显示父子表
	            //得到查询的参数
	            queryParams : params.queryParams || '',
	            columns: params.columns,
	            onLoadSuccess: function () {
	            	$("#waitModal").modal("hide")},
	            onLoadError: function () {
	            	$("#waitModal").modal("hide")
	                new Confirm({
	                	message : '加载失败',
	                	type : 'alter'
	                });},
	            onDblClickRow: params.onDblClickRow || function (row, $element) {},	
	            ajax:function(request){
	            	$("#waitModal").modal({backdrop:'static'})
	            	$.ajax(request)},};
		return obj;}
	
	/**
	 * 将form中的所有:input元素转化成后台参数
	 * @param form
	 * @returns
	 */
	function formItemsToParams(form){
		var params = {};
		var items = form.find(":input");
		for(var i = 0; i < items.length; i++){
			var key = items.eq(i).attr('name');
			params[key] = items.eq(i).val().trim();
		}
		return params;
	}
	
	/**
	 * 数字补位 将数字补充到length位,如1补位00001
	 * @param theNum 当前数字
	 * @param length 补充到length位
	 * 
	 * @returns
	 */
	function numCoverPosition(theNum,length){
		var length = length || 8;
		var currentNum = "" + theNum;
		var currentLength = currentNum.length;  
		var needLength = length - currentLength;
		var returnNum = '';
		for(var i=0; i < needLength; i++){
			returnNum += '0';
		}  
		return returnNum + theNum;
	}
	
	function createIframe(url, title, func){
		var currentIframe = $("#iframeModal");
		currentIframe.find(".modal-header").empty();
		currentIframe.find(".modal-body").empty();
		title && $("#iframeModal").find(".modal-header").append("<h5>" + title + "</h5>")
		var $newIframe = $("<iframe name='iframeModal_content' onload='this.height=this.document.body.scrollHeight' ></iframe>")
						.appendTo(currentIframe.find(".modal-body"))
		
		$("#iframeModal").modal({backdrop : "static"});
		iframeModal_content.location = url;
		
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	