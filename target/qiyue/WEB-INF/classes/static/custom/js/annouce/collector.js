$(function(){
	init();
	awiShow();
	awiAdd();
	awiSubmit();
	
	aciAdd();
	aciSubmit();
	
	aciTest()
})
function init(){
	var width = $(window).width();
	var height = $(window).height();
	$(".upWindow div").css("overflow-y","auto");
	$(".upWindow div").css("overflow-x","auto");
	$(".upWindow div.modal-dialog").css("min-width",width*0.4)
								.css("max-width",width*0.9);
	$(".upWindow div.modal-body").css("min-height",height*0.5)
								.css("max-height",height*0.9);
	$("body").css("padding","0");							
}
/*
 * 显示标题列表
 */
function awiShow(){
	$("#awiTable").bootstrapTable({ // 对应table标签的id
	      url: "awiShow", // 获取表格数据的url
	      cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
	      striped: true,  //表格显示条纹，默认为false
	      sortName: 'modifyTime', // 要排序的字段
	      sortOrder: 'desc', // 排序规则
	      columns: [
	    	  {
	              title: '序号', // 表格表头显示文字
	              align: 'center', // 左右居中
	              valign: 'middle', // 上下居中
	              formatter: function (value,row,index){
	            	  row.rowNo = index;
	            	  return index+1;
	              }
	          }, 
	          {
	              field: 'websiteName', // 返回json数据中的name
	              title: '网站名称', // 表格表头显示文字
	              align: 'center', // 左右居中
	              valign: 'middle' // 上下居中
	          }, 
//	          {
//	              field: 'websiteCode',
//	              title: '网站代码',
//	              align: 'center',
//	              valign: 'middle'
//	          }, 
	          {
	              field: 'websiteUrl',
	              title: '网站地址',
	              align: 'center',
	              valign: 'middle'
	          }, 
	          {
	        	  field: 'encode',
	              title: '网站编码',
	              align: 'center',
	              valign: 'middle'
	          },
	          {
	        	  field: 'aciShow',
	        	  title: '采集器规则',
	        	  align: 'center',
	        	  valign: 'middle',
	        	  formatter: function (value,row, index){
	        		  return '<a href="#" class="aciShowA" data-toggle="modal" data-target="#aciModal">查看</a>';
	        	  },
	        	  events:window.operateEvents={
	        		  'click .aciShowA':function (event,value,row, index){
	        			  aciShow(row);
	        		  }
	        	  }
	          },
	          {
	        	  field: 'modifyTime',
	        	  title: '修改时间',
	        	  align: 'center',
	        	  valign: 'middle'
	          },
	          {
	        	  field: 'modifyUsername',
	        	  title: '修改用户',
	        	  align: 'center',
	        	  valign: 'middle'
	          },
	          {
	        	  title: '操作',
	        	  align: 'center',
	        	  valign: 'middle',
	        	  formatter: function (value,row, index){
	        		  var oprations = '<a href="#" class="awiModify" data-toggle="modal" data-target="#awiModal">修改</a>'
				        			  +'&nbsp;&nbsp;&nbsp;&nbsp;'
				        			  +'<a href="#" class="awiDelete">删除</a>';
	        		  return oprations;
	        	  },
	        	  events:window.operateEvents={
        			  'click .awiModify':function(event,value,row, index){
        				  awiModify(row);
        			  },
        			  'click .awiDelete':function(event,value,row, index){
        				  if(confirm("你确定要删除【"+row.websiteName+"】吗？")){
        					  awiDelete(row);
        				  }
        			  },
        			  'click .awiAddAci':function(event,value,row, index){
        				  aciAdd(row);
        			  }
	        	  }
	          }
	      ]
	});
}

function awiAdd(row){
	$("#addAwiButton").click(function(){
		$("#awiAddA").parent().show();
		$("#awiModifyA").parent().hide();
		
		$("#awiAddA").click();
		
		$("#awiForm").attr("action","awiAdd");
		$("#awiForm input").val("");
		
	});
}

function awiModify(row){
	$("#awiAddA").parent().hide();
	$("#awiModifyA").parent().show();
	
	$("#awiModifyA").addClass("active show");
	
	$("#awiForm").attr("action","awiModify");
	$("#awiForm input[name='websiteName']").val(row.websiteName);
	$("#awiForm input[name='websiteUrl']").val(row.websiteUrl);
	$("#awiForm select[name='encode']").val(row.encode);
	$("#awiForm input[name='flowNo']").val(row.flowNo);
}

function awiDelete(row){
	$.ajax({
		url:"awiDelete",
		type:"post",
		dateType:"json",
		data:{
			"flowNo":row.flowNo
		},
		statusCode: {
		    999: function() {
		      location.href = "/";
		    }
		},
		success:function(data){
			$('#awiTable').bootstrapTable('refresh', {silent: true});
		}
	});
}

function awiSubmit(){
	$('#awiSubmit').click(function(){
		$('#awiForm').submit();
//		window.location.reload();
		$('#awiTable').bootstrapTable('refresh', {silent: true});
		$('#awiModal').modal('hide');
	});
}

function aciTable(awiFlowNo){
	if ($('#aciTable td').length>0){
		var param = {
				silent: true,
				query: {"awiFlowNo":awiFlowNo}
		};
		$('#aciTable').bootstrapTable('refresh', param);
		return ;
	}
	
	$("#aciTable").bootstrapTable({ // 对应table标签的id
		url:"aciShow",
		queryParams:{"awiFlowNo":awiFlowNo},
		striped: true,  //表格显示条纹，默认为false
		sortName: 'modifyTime', // 要排序的字段
		sortOrder: 'desc', // 排序规则
		escape: true,//特殊字符转义
		columns: [
			{
	             title: '序号', // 表格表头显示文字
	             align: 'center', // 左右居中
	             valign: 'middle', // 上下居中
	             formatter: function (value,row,index){
	            	 return index+1;
	             }
	        },
			{
				field: 'categoryName',
				title: '类别名称', 
				align: 'center',
				valign: 'middle' 
			}, 
			{
				field: 'titleUrlWhite',
				title: '标题地址',
				align: 'center',
				valign: 'middle'
			},
			{
				field: 'textUrlRegexWhite',
				title: '正文页白名单',
				align: 'center',
				valign: 'middle'
				
			},
			{
				field: 'textUrlRegexBlack',
				title: '正文页黑名单',
				align: 'center',
				valign: 'middle',
				formatter: function (value){
					if (value==null){
						return "";
					}
				}
					
			},
			{
				field: 'publishTimeSelector',
				title: '发布时间选择器',
				align: 'center',
				valign: 'middle',
				formatter: function (value){
					return value;
				}
					
			},
			{
				field: 'titleSelector',
				title: '标题选择器',
				align: 'center',
				valign: 'middle'
					
			},
			{
				field: 'textSelector',
				title: '正文选择器',
				align: 'center',
				valign: 'middle'
					
			},
			{
				field: 'modifyTime',
				title: '修改时间',
				align: 'center',
				valign: 'middle'
			},
			{
				field: 'modifyUsername',
				title: '修改用户',
				align: 'center',
				valign: 'middle'
			},
			{
				title: '操作',
				align: 'center',
				valign: 'middle',
				formatter: function (){
					var oprations = '<a href="#" class="aciModify" data-toggle="modal" data-target="#addAci">修改</a>'
										+'&nbsp;&nbsp;&nbsp;&nbsp;'
										+'<a href="#" class="aciDelete">删除</a>';
					return oprations;
				},
				events:window.operateEvents={
						'click .aciModify':function(event,value,row, index){
							aciModify(row);
						},
						'click .aciDelete':function(event,value,row, index){
							if (confirm("确定删除【"+row.categoryName+"】吗？")){
								aciDelete(row);
							}
						}
				}
			}
			],
			onLoadSuccess:function(){
				$("#aciTable td").css("white-space","nowrap");
			}
	});
}

function aciShow(row){
	$("#aciShowA").click(function(){
		//加上活动样式
		$("#aciModal a").removeClass("active show");
		$('#aciShowA').addClass("active show");
		$('#aciShowDiv').addClass("active show");
		//处理切换栏
		$('#aciShowA').parent().show();
			
		//处理结果显示区 
		$('#aciShowDiv').show();
		$('#aciAddDiv').hide();
		$('#aciTestDiv').hide();
		
		$('#aciSubmit').hide();
		$('#aciTest').hide();
	});
	
	//加上活动样式
	$("#aciModal a").removeClass("active show");
	$('#aciShowA').addClass("active show");
	$('#aciShowDiv').addClass("active show");
	$("#aciForm input[name='awiFlowNo']").val(row.flowNo);
	//处理切换栏
	$('#aciShowA').parent().show();
	$('#aciAddA').parent().hide();
	$('#aciModifyA').parent().hide();
	$('#aciTestA').parent().hide();
		
	//处理结果显示区 
	$('#aciShowDiv').show();
	$('#aciAddDiv').hide();
	$('#aciTestDiv').hide();
	
	$('#aciSubmit').hide();
	$('#aciTest').hide();
	//处理其他数据
	aciTable(row.flowNo);

}

function aciAdd(){
	$("#aciAddA").click(function(){
		
		$("#aciModal a").removeClass("active show");
		$("#aciAddA").addClass("active show");
		$("#aciAddDiv").addClass("active show");
		
		$('#aciShowA').parent().show();
		$('#aciAddA').parent().show();
		$('#aciModifyA').parent().hide();
		$('#aciTestA').parent().hide();
		
		$("#aciShowDiv").hide();
		$("#aciAddDiv").show();
		$('#aciTestDiv').hide();
		$('#aciSubmit').show();
		$('#aciTest').show();
		
		$("#aciForm").attr("action","aciAdd");
	});
	
	$("#aciAddButton").click(function(){
		$("#aciModal a").removeClass("active show");
		$("#aciAddA").addClass("active show");
		$("#aciAddDiv").addClass("active show");
		
		$('#aciShowA').parent().show();
		$('#aciAddA').parent().show();
		$('#aciModifyA').parent().hide();
		$('#aciTestA').parent().hide();
		
		$("#aciShowDiv").hide();
		$("#aciAddDiv").show();
		$('#aciSubmit').show();
		$('#aciTest').show();
		
		$("#aciForm").attr("action","aciAdd");
		$("#aciForm input[type='text']").val("");
		//销毁acr表
		$('#acrTable').bootstrapTable('destroy');
		
		$("#aciForm input[name='titleUrlWhite']").change(function(){
			var value = $("#aciForm input[name='titleUrlWhite']").val();
			var regExp = /[\/\.]/g;
			var regStr ;
			var result = "";
			while ((regStr = regExp.exec(value)) != null){
				var temp = value.substring(0,regExp.lastIndex);
				temp = temp.replace(regStr,"\\"+regStr);
				result += temp;
				value = value.substring(regExp.lastIndex);
				regExp.lastIndex = 0;
			}
			result += ".+";
			$("#aciForm input[name='textUrlRegexWhite']").val(result);
		});
		
		
	});
}

function aciModify(row){
	$("#aciModifyA").click(function(){
		$("#aciModal a").removeClass("active show");
		$("#aciModifyA").addClass("active show");
		$("#aciAddDiv").addClass("active show");
		
		$("#aciModifyA").parent().show();
		$("#aciShowA").parent().show();
		$('#aciAddA').parent().hide();
		
		$('#aciAddDiv').show();
		$('#aciShowDiv').hide();
		$('#aciTestDiv').hide();
		
		$('#aciSubmit').show();
		$('#aciTest').show();
		
		$("#aciForm").attr("action","aciModify");
	});
	
	$("#aciModal a").removeClass("active show");
	$("#aciModifyA").addClass("active show");
	$("#aciAddDiv").addClass("active show");
	
	$("#aciModifyA").parent().show();
	$("#aciShowA").parent().show();
	$('#aciAddA').parent().hide();
	$('#aciTestA').parent().hide();
	
	$('#aciAddDiv').show();
	$('#aciShowDiv').hide();	
	$('#aciSubmit').show();
	$('#aciTest').show();
	//销毁acr表
	$('#acrTable').bootstrapTable('destroy');
	
	$("#aciForm").attr("action","aciModify");
	$("#aciForm input[name='flowNo']").val(row.flowNo);
	$("#aciForm input[name='categoryName']").val(row.categoryName);
	$("#aciForm input[name='titleUrlWhite']").val(row.titleUrlWhite);
	$("#aciForm input[name='textUrlRegexWhite']").val(row.textUrlRegexWhite);
	$("#aciForm input[name='textUrlRegexBlack']").val(row.textUrlRegexBlack=="null"?"":row.textUrlRegexBlack);
	$("#aciForm input[name='publishTimeSelector']").val(row.publishTimeSelector);
	$("#aciForm input[name='titleSelector']").val(row.titleSelector);
	$("#aciForm input[name='textSelector']").val(row.textSelector);
	
}

function aciDelete(row){
	$.ajax({
		url:"aciDelete",
		type:"post",
		dateType:"json",
		data:{
			"flowNo":row.flowNo
		},
		statusCode: {
		    999: function() {
		      location.href = "/";
		    }
		},
		success:function(data){
			$('#aciTable').bootstrapTable('refresh', {silent: true});
//			window.location.reload();
		}
	});
}

function aciSubmit(){
	$('#aciSubmit').click(function(){
		$('#aciForm').submit();
//		window.location.reload();
		$('#aciTable').bootstrapTable('refresh', {silent: true});
		$('#aciModal').modal('hide');
	});
}

function aciTest(){
	$("#aciTest").click(function(){
		$("#aciModal a").removeClass("active show");
		$("#aciTestA").addClass("active show");
		$("#aciTestDiv").addClass("active show");
		
		$("#aciTestA").parent().show();
		$("#aciShowA").parent().show();
		
		$('#aciTestDiv').show();
		$('#aciShowDiv').hide();
		$('#aciAddDiv').hide();
		$('#aciModifyDiv').hide();
		
		$('#aciSubmit').show();
		$('#aciTest').hide();
		
		$("#aciForm").attr("action","aciTestAdd");
		$("#aciForm").submit();
		acrTable();
		
//		setTimeout(function(){
//		},1000);
	});
}

function acrTable(){
	if ($('#acrTable td').length>0){
		$('#acrTable').bootstrapTable('refresh', {silent: true});
		return ;
	}
	$("#acrTable").bootstrapTable({ // 对应table标签的id
	      url: "aciTestShow", // 获取表格数据的url
	      cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
	      striped: true,  //表格显示条纹，默认为false
	      sortName: 'publishTime', // 要排序的字段
	      sortOrder: 'desc', // 排序规则
	      contentType:"application/x-www-form-urlencoded; charset=UTF-8", 
	      columns: [
	    	  {
	              title: '序号', // 表格表头显示文字
	              align: 'center', // 左右居中
	              valign: 'middle', // 上下居中
	              formatter: function (value,row,index){
	            	  row.rowNo = index;
	            	  return index+1;
	              }
	          }, 
	          {
	              field: 'url', // 返回json数据中的name
	              title: '正文地址', // 表格表头显示文字
	              align: 'center', // 左右居中
	              valign: 'middle' // 上下居中
	          }, 
	          {
	              field: 'title',
	              title: '标题',
	              align: 'center',
	              valign: 'middle'
	          },
	          {
	              field: 'text',
	              title: '正文',
	              align: 'center',
	              valign: 'middle',
	              formatter:function(value,row,index){
	            	  value = value.replace(/<[^>^\u4e00-\u9fa5]*>/g,"");
//	            	  value = value.replace(/[^\u4e00-\u9fa5，。“”0-9]/g,"");
	            	  if (value.length>100){
	            		  value = value.substring(0,80);
	            		  value += "...";
	            	  }
	            	  return value;
	              }
	          },
	          {
	              field: 'websiteCode',
	              title: '网站代码',
	              align: 'left',
	              valign: 'middle'
	          }, 
	          {
	        	  field: 'websiteName',
	              title: '网站名称',
	              align: 'center',
	              valign: 'middle'
	          },
	          {
	        	  field: 'categoryCode',
	        	  title: '分类代码',
	        	  align: 'center',
	        	  valign: 'middle'
	          },
	          {
	        	  field: 'categoryName',
	        	  title: '分类名称',
	        	  align: 'center',
	        	  valign: 'middle'
	          },
	          {
	        	  field: 'publishTime',
	        	  title: '发布时间',
	        	  align: 'center',
	        	  valign: 'middle'
	          },
	          {
	        	  field: 'recordTime',
	        	  title: '记录时间',
	        	  align: 'center',
	        	  valign: 'middle'
	          }
	      ],
	      onLoadSuccess: function(data){
	    	  $("#acrTable td").css("white-space","nowrap");
	      }
	});
}
