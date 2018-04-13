var constant = {
	"home":"首页",
	"announcement":"政府公告归集",
	"gzgov":"广州市人民政府",
	"tzgg":"通知公告",
	"fggw":"法规公文",
	"cwhy":"常务会议",
	"szgov":"深圳政府在线",
	"bmtx":"便民提醒",
	"gqdt":"各区动态",
	"bmdt":"部门动态",
	"zwdt":"政务动态",
	"tzgg":"通知公告"
};
var dynamic = {
	"gzgov":"tzgg",
	"szgov":"tzgg",
	"pageSize":10,
	"pageShow":5,
	"textHeightPercent":0.62
} 
$(function(){
	navi();
	overview("home","announcement","gzgov","tzgg");
	pageShow("gzgov","tzgg");
})
/*
 * 添加导航栏
 */
function navi(){
	$.ajax({
		url:"navi",
		type:"post",
		dateType:"json",
		data:{},
		success:function(data){
			$("#navigation ul:first").empty();
			for (var i=0;i<data.length;i++){
				var websiteCode = data[i].websiteCode;
				var categoryCode = data[i].categoryCode;
				var navi = $("#"+websiteCode);
				if (navi.html()){
					var a = '<a class="dropdown-item" href="#" onclick="pageShow(\''+websiteCode+'\',\''+categoryCode+'\')">'+eval('constant.'+categoryCode)+'</a>';
					navi.siblings("div:first").append(a);
				} else {
					var li = '<li class="nav-item dropdown">'
								+ '<a class="nav-link dropdown-toggle" href="#" id="'+websiteCode+'" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'+eval('constant.'+websiteCode)+'</a>'
								+ '<div class="dropdown-menu" aria-labelledby="'+websiteCode+'">'
									+ '<a class="dropdown-item" href="#" onclick="pageShow(\''+websiteCode+'\',\''+categoryCode+'\')">'+eval('constant.'+categoryCode)+'</a>'
								+ '</div>'
							+ '</li>';
					$("#navigation ul:first").append(li);
				}
			}
		}
	});
}
/*
 * 添加当前位置
 */
function overview(){
	$("#overview ol:first").empty();
	for (var i=0;i<arguments.length-1;i++){
		var arg = arguments[i];
		if (arg == "home"){
			item = '<li class="breadcrumb-item"><a href="/" >'+eval('constant.'+arg)+'</a></li>';
		} else if (arg == "announcement"){
    		item = '<li class="breadcrumb-item"><a href="./show" >'+eval('constant.'+arg)+'</a></li>';
		} else {
			item = '<li class="breadcrumb-item"><a href="#" onclick=pageShow("'+arg+'","'+eval('dynamic.'+arg)+'")>'+eval('constant.'+arg)+'</a></li>';
		}
		$("#overview ol:first").append(item);
	}
	var current = '<li class="breadcrumb-item active">'+eval('constant.'+arguments[arguments.length-1])+'</li>';
	$("#overview ol:first").append(current);
}

/*
 * 显示标题列表
 */
function titleList(currentPage,websiteCode,categoryCode){
	var param = {};
	if (categoryCode){
		overview("home","announcement",websiteCode,categoryCode);
		param = {
				"page":currentPage,
				"categoryCode":categoryCode,
				"websiteCode":websiteCode
			};
	} else {
		param = {
				"page":1,
				"websiteCode":websiteCode
			};
	}
	$.ajax({
		url:"titles",
		type:"post",
		dateType:"json",
		data:param,
		success:function(data){
			$("#titles ul:first").html("");
			//展示标题列表
			for (var i=0;i<data.length;i++){
				var li = '<li class="list-group-item">'
							+ '<a href="#" onclick=showText(this)><span>'+data[i].title+'</span></a>'
							+ '<a href="'+data[i].url+'" target="_blank" style="float:right" onclick=locateUrl(this)>原网址</a>'
						+ '</li>';
				$("#titles ul:first").append(li);
				$("#titles ul:first li:last a:first").data("title",data[i].title);
				$("#titles ul:first li:last a:first").data("text",data[i].text);
			}
			$("#titles ul:first li:first a:first").click();
		}
	});
}
/*
 * 显示文本内容
 */
function showText(ele){
	$("#title").css("height",$("#overview").height());
	var cHeight = $("#overview").height() + $("#pagination").height() + $("#titles").height();
	var tHeight = $("#title").height();
	var wheight = $(window).height();
	var textHeight = cHeight-tHeight-16;
	textHeight = textHeight>wheight*dynamic.textHeightPercent?textHeight:wheight*dynamic.textHeightPercent;
	$("#text").css("height",textHeight);
	$("#titles ul:first a").css("color","#007bff");
	$(ele).css("color","red");
	$("#title").empty();
	$("#text").empty();
	var title = $(ele).data("title");
	var text = $(ele).data("text");
	var title = '<h4>'+title+'</h4>';
	$("#title").append(title);
	$("#text").append(text);
}
/*
 * 跳转原网页
 */
function locateUrl(ele){
	$("#titles ul:first a").css("color","#007bff");
	$(ele).css("color","red");
}

function pageShow(websiteCode,categoryCode){
	$("#pagination ul:first").empty();
	$.ajax({
		url:"pageTotal",
		type:"post",
		dateType:"json",
		data:{
			"websiteCode":websiteCode,
			"categoryCode":categoryCode
		},
		success:function(data){
			var pageTotal = 0;
			if (data%dynamic.pageSize>0){
				pageTotal = Math.floor(data/dynamic.pageSize) + 1;
			} else {
				pageTotal = data/dynamic.pageSize;
			}
			//显示分页区
			var previous = '<li class="page-item" onclick=previousPage(this)>'
								+ '<a class="page-link" href="#" aria-label="Previous">'
							       + '<span aria-hidden="true">&laquo;</span>'
							       + '<span class="sr-only">Previous</span>'
							    + '</a>'
						   + '</li>';
			$("#pagination ul:first").append(previous);
			for (var i=1;i<=pageTotal;i++){
				var page = '<li class="page-item"><a class="page-link" href="#" onclick=gotoPage(this)>'+i+'</a></li>';
				$("#pagination ul:first").append(page);
			}
			var next = '<li class="page-item" onclick=nextPage(this)>'
					      + '<a class="page-link" href="#" aria-label="Next">'
					      	+ '<span aria-hidden="true">&raquo;</span>'
					      	+ '<span class="sr-only">Next</span>'
					      + '</a>'
					  +'</li>';
			$("#pagination ul:first").append(next);
			$("#pagination ul:first").data("websiteCode",websiteCode);
			$("#pagination ul:first").data("categoryCode",categoryCode);
			$("#pagination ul:first").data("pageTotal",pageTotal);
			$("#pagination ul:first a:contains(1)").click();
		}
	});
}
/*
 * 跳转到指定页
 */
function gotoPage(ele){
	titleList($(ele).html(),$("#pagination ul:first").data("websiteCode"),$("#pagination ul:first").data("categoryCode"));
	$("#pagination ul:first li").removeClass("active");
	$(ele).parent().addClass("active");
	$("#pagination ul:first li:first").data("currentPage",parseInt($(ele).html()));
	$("#pagination ul:first li:last").data("currentPage",parseInt($(ele).html()));
}
/*
 * 上一页
 */
function previousPage(ele){
	var currentPage = $(ele).data("currentPage");
	if (currentPage>1){
		$("#pagination ul:first a:contains("+(currentPage-1)+")").click();
	}
}
/*
 * 上一页
 */
function nextPage(ele){
	var currentPage = $(ele).data("currentPage");
	var pageTotal = $("#pagination ul:first").data("pageTotal");
	if (currentPage<pageTotal){
		$("#pagination ul:first a:contains("+(currentPage+1)+")").click();
	}
}
