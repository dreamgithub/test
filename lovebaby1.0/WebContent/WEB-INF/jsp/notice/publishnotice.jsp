<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- 引用上传图片控制层插件样式 -->
<link href="${path }/desktop/css/templatemo_style.css" rel="stylesheet"
	type="text/css">
	<link rel="stylesheet" href="${path }/desktop/css/table/style.css" />
<link href="${path }/css/zhedie/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="${path }/css/zhedie/lrtk.css" media="screen" type="text/css" />
<script src="${path }/desktop/js/jquery.min.js"></script>
</head>
<body>
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">公告</div>
			<c:if test="${member.result.type!='5'}">				
				<ul class="nav right center-text">
						<li id="read" class="active ">查看公告</li>
						<li id="publish" class="active btn">发布公告</li>
						<li id="mynotice" class="active">我的发布</li>
				</ul>
			</c:if>
		</nav>
	</div>
	<div id="container2" class="content-container">
		<div style="width: 15%;float: left;margin-left: -30px">
			<div id="firstpane" class="menu_list">
			    <p class="menu_head current">发布公告</p>
			    <div style="display:block" class=menu_body >	
				    <a href="${path }/webnotice/chooseNoticeType.jhtml?searchType=1" target="content" class="myclick">网页公告</a>	    			    
				    <a href="${path }/webnotice/chooseNoticeType.jhtml?searchType=0" target="content">文本公告</a>	
			    </div>			    
			</div>
		</div>
		<div style="width: 80%;float: right;">
			<iframe name="content" src="${path }/webnotice/chooseNoticeType.jhtml?searchType=1" width="100%" height="520px" scrolling="auto" style="margin:0px;border-width:0px;">
			</iframe>
		</div>			
	</div>
<script>
$(window).scroll(function(){
    var msg = $(".history-img");
    var item = $(".history_L");
    var items = $(".history_R");
    var windowHeight = $(window).height();
    var Scroll = $(document).scrollTop();
    if((msg.offset().top - Scroll -windowHeight)<=0){
        msg.fadeIn(1500);
    }
    for(var i=0;i<item.length;i++){
        if(($(item[i]).offset().top - Scroll - windowHeight)<= -100){
            $(item[i]).animate({marginRight:'0px'},'50','swing');
        }
    }
    for(var i=0;i<items.length;i++){
        if(($(items[i]).offset().top - Scroll - windowHeight)<= -100){
            $(items[i]).animate({marginLeft:'0px'},'50','swing');
        }
    }});
    
$(document).ready(function(){
	$("#firstpane .menu_body:eq(0)").show();
	$("#firstpane p.menu_head").click(function(){
		$(this).addClass("current").next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
		$(this).siblings().removeClass("current");
	});
	$("#secondpane .menu_body:eq(0)").show();
	$("#secondpane p.menu_head").mouseover(function(){
		$(this).addClass("current").next("div.menu_body").slideDown(500).siblings("div.menu_body").slideUp("slow");
		$(this).siblings().removeClass("current");
	});
	
	$(".menu_body a").click(function(){
		$(".menu_body a").removeClass("myclick");
		$(this).attr("class","myclick")
	});
});
</script>

	<script type="text/javascript">
		$("#publish")
				.click(
						function() {
							location = "${path }/webnotice/publishNotice.jhtml";
						});
		$("#read")
				.click(
						function() {
							location = "${path }/webnotice/notice.jhtml";
						});
		$("#mynotice")
		.click(
				function() {
					var id="${member.result.id}";
					location = "${path }/webnotice/getPublishNotice.jhtml?id="+id;
				});

	</script>

</body>
</html>