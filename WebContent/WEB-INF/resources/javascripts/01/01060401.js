$(function(){

	/**
	 * 修改操作
	 */
	$("button[name='update']").on("click", function(){

		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr")
		var data = {
			funcId : $_tr.find('td').eq(0).html(),
			updateId : $_tr.find('td').eq(1).html(),
			updateName : $_tr.find('td').eq(2).html(),
			updateBz : $_tr.find('td').eq(3).html()
		};
		
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'update', // 生成表单的类型
			page : 'Page', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'funcId',
							inputLableHtml : '所属功能编号',
							inputId : 'funcId',
							inputVal : data.funcId
						},
						{
							inputLableFor : 'pageId',
							inputLableHtml : '页面编号',
							inputId : 'pageId',
							inputVal : data.updateId
						},
						{
							inputLableFor : 'pageName',
							inputLableHtml : '页面名称',
							inputId : 'pageName',
							inputVal : data.updateName
						},
						{
							inputLableFor : 'pageBz',
							inputLableHtml : '页面描述',
							inputId : 'pageBz',
							inputVal : data.updateBz
						}
					 ],
			title : '修改页面信息',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
		$_modalForm.find("#pageId").attr("readOnly", "true");
		$_modalForm.find("#funcId").attr("readOnly", "true");
		
		// 4.显示modal
		$('#0106Modal').modal('show')
	});

	/**
	 * 删除操作
	 */
	$("button[name='delete']").on("click", function(){
		
		// 1.获取当前行的数据信息
		var $_tr = $(this).parents("tr");
		var data = {
			funcId : $_tr.find('td').eq(0).html(),
			deleteId : $_tr.find('td').eq(1).html(),
			deleteName : $_tr.find('td').eq(2).html(),
			deleteBz : $_tr.find('td').eq(3).html()
		};
		// 2.生成form表单
		var $_modalForm = createForm({
			type : 'delete', // 生成表单的类型
			page : 'Page', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'funcId',
							inputLableHtml : '所属功能编号',
							inputId : 'funcId',
							inputVal : data.funcId
						},
						{
							inputLableFor : 'pageId',
							inputLableHtml : '页面编号',
							inputId : 'pageId',
							inputVal : data.deleteId
						},
						{
							inputLableFor : 'pageName',
							inputLableHtml : '页面名称',
							inputId : 'pageName',
							inputVal : data.deleteName
						},
						{
							inputLableFor : 'pageBz',
							inputLableHtml : '页面描述',
							inputId : 'pageBz',
							inputVal : data.deleteBz
						}
					 ],
			title : '删除页面',
			modal : $('#0106Modal')
		});
		
		// 3.form表单调整
		$_modalForm.find("#pageId").attr("readOnly", "true");
		$_modalForm.find("#pageBz").attr("readOnly", "true");
		$_modalForm.find("#funcId").attr("readOnly", "true");

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
			page : 'Page', // 当前功能所在的页面
			inputs : [ // 生成表单的内容
						{
							inputLableFor : 'funcId',
							inputLableHtml : '所属功能编号',
							inputId : 'funcId',
							inputVal : '请输入所属模块编号'
						},
						{
							inputLableFor : 'pageId',
							inputLableHtml : '页面编号',
							inputId : 'pageId',
							inputVal : '请输入功能编号'
						},
						{
							inputLableFor : 'pageName',
							inputLableHtml : '页面名称',
							inputId : 'pageName',
							inputVal : '请输入功能名称'
						},
						{
							inputLableFor : 'pageBz',
							inputLableHtml : '页面描述',
							inputId : 'pageBz',
							inputVal : '请输入功能描述'
						}
					 ],
			title : '添加页面',
			modal : $('#0106Modal')
		});
		
		// 3.显示modal
		$('#0106Modal').modal('show')
	})
	
		
	
})