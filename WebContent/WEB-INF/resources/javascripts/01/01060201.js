$(function(){

	/**
	 * 修改操作
	 */
	$("button[name='update']").on("click", function(){

		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		var data = {
			updateId : $_tr.find('td').eq(0).html(),
			updateName : $_tr.find('td').eq(1).html(),
			updateBz : $_tr.find('td').eq(2).html()
		};
		
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'update', // 生成表单的类型
			page : 'Module', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '模块编号',
							inputId : 'moduleId',
							inputVal : data.updateId
						},
						{
							inputLableFor : 'moduleName',
							inputLableHtml : '模块名称',
							inputId : 'moduleName',
							inputVal : data.updateName
						},
						{
							inputLableFor : 'moduleBz',
							inputLableHtml : '模块描述',
							inputId : 'moduleBz',
							inputVal : data.updateBz
						}
					 ],
			title : '修改模块信息',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
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
			deleteId : $_tr.find('td').eq(0).html(),
			deleteName : $_tr.find('td').eq(1).html(),
			deleteBz : $_tr.find('td').eq(1).html()
		};
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'delete', // 生成表单的类型
			page : 'Module', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '模块编号',
							inputId : 'moduleId',
							inputVal : data.deleteId
						},
						{
							inputLableFor : 'moduleName',
							inputLableHtml : '模块名称',
							inputId : 'moduleName',
							inputVal : data.deleteName
						},
						{
							inputLableFor : 'moduleBz',
							inputLableHtml : '模块描述',
							inputId : 'moduleBz',
							inputVal : data.deleteBz
						}
					 ],
			title : '删除模块',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
		$_modalForm.find("#moduleId").attr("readOnly", "true");
		$_modalForm.find("#moduleBz").attr("readOnly", "true");
		$_modalForm.find("#moduleName").attr("readOnly", "true");

		// 4.显示modal
		$('#0106Modal').modal('show')
	})
	
	
	/**
	 * 增加模块操作
	 */
	$("button[name='add']").on("click", function(){
		
		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'add', // 生成表单的类型
			page : 'Module', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'moduleId',
							inputLableHtml : '模块编号',
							inputId : 'moduleId',
							inputVal : '输入模块编号'
						},
						{
							inputLableFor : 'moduleName',
							inputLableHtml : '模块名称',
							inputId : 'moduleName',
							inputVal : '输入模块名称'
						},
						{
							inputLableFor : 'moduleBz',
							inputLableHtml : '模块描述',
							inputId : 'moduleBz',
							inputVal : '输入模块描述'
						}
					 ],
			title : '添加模块',
			modal : $('#0106Modal')
		});
		
		// 3.显示modal
		$('#0106Modal').modal('show')
	})
	
		
	
})