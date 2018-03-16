$(function(){

	/**
	 * 修改操作
	 */
	$("button[name='update']").on("click", function(){

		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		var data = {
			moduleId : $_tr.find('td').eq(0).html(),
			updateId : $_tr.find('td').eq(1).html(),
			updateName : $_tr.find('td').eq(2).html(),
			updateBz : $_tr.find('td').eq(3).html()
		};
		
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'update', // 生成表单的类型
			page : 'Func', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '模块编号',
							inputId : 'moduleId',
							inputVal : data.moduleId
						},
						{
							inputLableFor : 'funcId',
							inputLableHtml : '功能编号',
							inputId : 'funcId',
							inputVal : data.updateId
						},
						{
							inputLableFor : 'funcName',
							inputLableHtml : '功能名称',
							inputId : 'funcName',
							inputVal : data.updateName
						},
						{
							inputLableFor : 'funcBz',
							inputLableHtml : '功能描述',
							inputId : 'funcBz',
							inputVal : data.updateBz
						}
					 ],
			title : '修改功能信息',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
		$_modalForm.find("#funcId").attr("readOnly", "true");
		$_modalForm.find("#moduleId").attr("readOnly", "true");
		
		// 4.显示modal
		$('#0106Modal').modal('show')
	});

	/**
	 * 删除操作
	 */
	$("button[name='delete']").on("click", function(){
		
		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		var data = {
			moduleId : $_tr.find('td').eq(0).html(),
			deleteId : $_tr.find('td').eq(1).html(),
			deleteName : $_tr.find('td').eq(2).html(),
			deleteBz : $_tr.find('td').eq(3).html()
		};
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'delete', // 生成表单的类型
			page : 'Func', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '所属模块编号',
							inputId : 'moduleId',
							inputVal : data.moduleId
						},
						{
							inputLableFor : 'funcId',
							inputLableHtml : '功能编号',
							inputId : 'funcId',
							inputVal : data.deleteId
						},
						{
							inputLableFor : 'funcName',
							inputLableHtml : '功能名称',
							inputId : 'funcName',
							inputVal : data.deleteName
						},
						{
							inputLableFor : 'funcBz',
							inputLableHtml : '功能描述',
							inputId : 'funcBz',
							inputVal : data.deleteBz
						}
					 ],
			title : '删除功能',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
		$_modalForm.find("#funcId").attr("readOnly", "true");
		$_modalForm.find("#funcBz").attr("readOnly", "true");
		$_modalForm.find("#moduleId").attr("readOnly", "true");

		// 4.显示modal
		$('#0106Modal').modal('show')
	})
	
	
	/**
	 * 增加功能操作
	 */
	$("button[name='add']").on("click", function(){
		
		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'add', // 生成表单的类型
			page : 'Func', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '所属模块编号',
							inputId : 'moduleId',
							inputVal : '请输入所属模块编号'
						},
						{
							inputLableFor : 'funcId',
							inputLableHtml : '功能编号',
							inputId : 'funcId',
							inputVal : '请输入功能编号'
						},
						{
							inputLableFor : 'funcName',
							inputLableHtml : '功能名称',
							inputId : 'funcName',
							inputVal : '请输入功能名称'
						},
						{
							inputLableFor : 'funcBz',
							inputLableHtml : '功能描述',
							inputId : 'funcBz',
							inputVal : '请输入功能描述'
						}
					 ],
			title : '添加功能',
			modal : $('#0106Modal')
		});
		
		// 3.显示modal
		$('#0106Modal').modal('show')
	})
	
		
	
})