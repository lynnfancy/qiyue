var contextUrl ;
function initNavi(contextUrl){
	contextUrl = contextUrl;
}
$(function(){
	initContainer();
	navi();
});

function initContainer(){
	var width = $(window).width();
	var height = $(window).height();
	$("#container").css("margin-top","4rem");
}

function navi(){
	$.ajax({
		url:contextUrl + "navi",
		data:{},
		type:"post",
		dataType:"json",
		success: function (data) {
			var navi = '<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark"></nav>';
			var home = '<a class="navbar-brand" href="'+ contextUrl + data.element.url+'">'+data.element.name+'</a>'
			navi = $(navi).append(home);
			var button = ' <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">'
					       +' <span class="navbar-toggler-icon"></span>'
					       +' </button>';
			navi.append(button);
			var navbar = '<div class="collapse navbar-collapse" id="navbar"></div>';
			var ul = '<ul class="navbar-nav mr-auto"></ul>';
			ul = $(ul);
			var children = data.children;
			for (var i=0;i<children.length;i++){
				if (Boolean(children[i].hasChild)) {
					var li = '<li class="nav-item dropdown"></li>';
					var a = '<a class="nav-link dropdown-toggle" href="'+children[i].element.url+'" id="annouce" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
						+ children[i].element.name
						+'</a>';
					li = $(li).append(a);
					var div = '<div class="dropdown-menu" aria-labelledby="navbarDropdown"></div>';
					var subChildren = children[i].children;
					for (var j=0;j<subChildren.length;j++) {
						var subA = '<a class="dropdown-item" href="'+ contextUrl + subChildren[j].element.url+'">'+subChildren[j].element.name+'</a>'
					          		+'<div class="dropdown-divider"></div>';
						div = $(div).append(subA);
						li.append(div);
					}
					div.find("div:last").hide();
				} else {
					var li = '<li class="nav-item">'
				            +'<a class="nav-link" href="'+ children[i].element.url+'">'+children[i].element.name+'</a>'
							+'</li>';
				}
				ul.append(li);
			}
			navbar = $(navbar).append(ul);
			navi.append(navbar);
			$("#navi").append(navi);
		}
	});
}