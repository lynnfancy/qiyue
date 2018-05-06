var dynamic = {
	"pageSize":10,
} 
$(function(){
	init();
	awiList();
})

function init(){
	var width = $(window).width();
	var height = $(window).height();
	$("#container").height((height-$("#navi").height())*0.90);
}

/*
 * 添加菜单
 */
function awiList(){
	$.ajax({
		url:"awiList",
		type:"post",
		dateType:"json",
		data:{},
		statusCode: {
		    999: function() {
		      location.href = "/";
		    }
		},
		success:function(data){
			$("#awiList").empty();
			for (var i=0;i<data.length;i++){
				var awi = data[i];
				var topMenu = '<div class="card"></div>';
				var topMenuTitle = '<div class="card-header" id="headingOne" style="padding:0.75rem 0rem;">'
										+ '<h5 class="mb-0">'
											+ '<button class="btn btn-link" data-toggle="collapse" data-target="#'+awi.flowNo+'" aria-expanded="true" aria-controls="collapseOne">'
									          	+ awi.websiteName
									        + '</button>'
									    + '</h5>'
								   + '</div>';
				topMenu = $(topMenu).append(topMenuTitle);
				
				var aciList = awi.aciList;
				var ul = '<ul class="list-group">'
						+'</ul>';
				for (var j=0;j<aciList.length;j++){
					var li = '<li class="list-group-item"></li>';
					var subMenuTitle = '<a href="#" onclick=show(this)>'+aciList[j].categoryName+'</a>';
					subMenuTitle = $(subMenuTitle).data("awiFlowNo",awi.flowNo);
					subMenuTitle.data("websiteName",awi.websiteName);
					subMenuTitle.data("aciFlowNo",aciList[j].flowNo);
					subMenuTitle.data("categoryName",aciList[j].categoryName);
					subMenuTitle.data("currentPage",1);
					li = $(li).append(subMenuTitle);
					ul = $(ul).append(li);
				}
				//class show 子菜单展开
				var subMenu = 	'<div id="'+awi.flowNo+'" class="collapse" aria-labelledby="headingOne" data-parent="#awiList">'
								+'</div>';
				subMenu = $(subMenu).append(ul);
				topMenu.append(subMenu);
				$("#awiList").append(topMenu);
				$("#awiList .card:first div:eq(1)").addClass("show");
			}
		}
	});
}
/*
 * 添加当前位置
 */
function overview(ele){
	$("#overview").empty();
	var ol = '<ol class="breadcrumb" style="padding:0.75rem 1.25rem"></ol>';
	var home = '<li class="breadcrumb-item"><a href="../index.html">首页</a></li>';
	ol = $(ol).append(home);
	var annouce = '<li class="breadcrumb-item"><a href="/annouce/show">政府公告归集</a></li>';
	ol.append(annouce);
	var websiteName = '<li class="breadcrumb-item"><a href="#">'+$(ele).data("websiteName")+'</a></li>';
	ol.append(websiteName);
	var categoryName = '<li class="breadcrumb-item active">'+$(ele).data("categoryName")+'</li>';
	ol.append(categoryName);
	var nav = '<nav aria-label="breadcrumb"></nav>';
	nav = $(nav).append(ol);
	$("#overview").append(nav);
	$("#overview li:last").addClass("active");
}

function show(ele){
	overview(ele);
	pagination(ele);
	titleShow(ele);
}

/*
 * 显示标题列表
 */
function titleShow(ele){
	if ($(ele).parents("div#awiList").length>0){
		$("#awiList a").removeClass("selected");
		$(ele).addClass("selected");
	}
	$("#overview li.active").html($(ele).data("categoryName"));
	var param = {};
	param = {
		"page":$(ele).data("currentPage"),
		"awiFlowNo":$(ele).data("awiFlowNo"),
		"aciFlowNo":$(ele).data("aciFlowNo")
	};
	$.ajax({
		url:"titleShow",
		type:"post",
		dateType:"json",
		data:param,
		statusCode: {
		    999: function() {
		      location.href = "/";
		    }
		},
		success:function(data){
			$("#titles").empty();
			//展示标题列表
			var ul = $('<ul class="list-group"></ul>');
			for (var i=0;i<data.length;i++){
				var li = '<li class="list-group-item">'
						+ '</li>';
				var title = '<a href="#" onclick=showText(this) data-toggle="modal" data-target="#text"><span>'+data[i].title+'</span></a>';
				title = $(title).data("title",data[i].title);
				title.data("text",data[i].text);
				title.data("url",data[i].url);
				title.data("publishTime",data[i].publishTime);
				li = $(li).append(title);
				var publishTime = '<span style="float:right">'+ data[i].publishTime +'</span>';
				li = $(li).append(publishTime);
				ul.append(li);
			}
			$("#titles").append(ul);
		}
	});
}
/*
 * 显示文本内容
 */
function showText(ele){
	var width = $(window).width();
	var height = $(window).height();
	$("#text div.modal-dialog").css("min-width",width*0.4)
								.css("max-width",width*0.6);
	$("#text div.modal-body").css("min-height",height*0.5)
							.css("max-height",height*0.75)
							.css("overflow-y","auto");
	$("#modalTitle").html($(ele).data("title"));
	$("#textContent").html($(ele).data("text"));
	$("#titles a").removeClass("selected");
	$(ele).addClass("selected");
}
/*
 * 跳转原网页
 */
function locateUrl(ele){
	$("#titles ul:first a").css("color","#007bff");
	$(ele).css("color","red");
}

/*
 * 分页区
 */
function pagination(ele){
	$("#pagination").empty();
	$.ajax({
		url:"pagination",
		type:"post",
		dateType:"json",
		data:{
			"awiFlowNo":$(ele).data("awiFlowNo"),
			"aciFlowNo":$(ele).data("aciFlowNo")
		},
		statusCode: {
		    999: function() {
		      location.href = "/";
		    }
		},
		success:function(data){
			var totalPagesNum = 0;
			if (data%dynamic.pageSize>0){
				totalPagesNum = Math.floor(data/dynamic.pageSize) + 1;
			} else {
				totalPagesNum = data/dynamic.pageSize;
			}
			var nav = '<nav aria-label="pagination"></nav>';
			var ul = '<ul class="pagination"></ul>';
			ul = $(ul).data("currentPage",1);
			ul = $(ul).data("totalPagesNum",totalPagesNum);
			var previous = '<li class="page-item"><a class="page-link" href="#" onclick=previousPage(this)>上一页</a></li>';
			ul.append(previous);
			for (var i=1;i<=totalPagesNum;i++){
				var li = '<li class="page-item"><a class="page-link" href="#" onclick=gotoPage(this)>'+i+'</a></li>';
				li = $(li).data("awiFlowNo",$(ele).data("awiFlowNo"));
				li.data("aciFlowNo",$(ele).data("aciFlowNo"));
				li.data("currentPage",i);
				ul.append(li);
			}
			var next = '<li class="page-item"><a class="page-link" href="#" onclick=nextPage(this)>下一页</a></li>';
			ul.append(next);
			nav = $(nav).append(ul);
			$("#pagination").append(nav);
			$("#pagination li:eq(1)").addClass("active");
		}
		
	});
}
/*
 * 跳转到指定页
 */
function gotoPage(ele){
	titleShow(ele.parentNode);
	$(ele).parent().siblings().removeClass("active");
	$(ele).parent().addClass("active");
	$("#pagination ul").data("currentPage",parseInt($(ele).html()));
}
/*
 * 上一页
 */
function previousPage(ele){
	var currentPage = $(ele).parents("ul").data("currentPage");
	if (currentPage>1){
		$("#pagination li.active").prev().find("a").click();
	}
}
/*
 * 下一页
 */
function nextPage(ele){
	var currentPage = $(ele).parents("ul").data("currentPage");
	var totalPagesNum = $(ele).parents("ul").data("totalPagesNum");
	if (currentPage<totalPagesNum){
		$("#pagination li.active").next().find("a").click();
	}
}
