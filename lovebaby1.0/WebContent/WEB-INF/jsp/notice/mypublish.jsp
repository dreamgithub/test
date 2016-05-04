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
 <!-- 表格资源引入 -->
    <link rel="stylesheet" href="${path }/desktop/css/table/style.css" />
	<!-- jQuery -->
	<script src="${path }/desktop/js/table/jquery.dataTables.min.js"></script>
	<script src="${path }/desktop/js/table/colResizable-1.3.js"></script>
	<script src="${path }/desktop/js/table/jquery-ui-1.8.21.min.js"></script>
	<script src="${path }/desktop/js/table/jquery.uniform.js"></script>
	<script src="${path }/desktop/js/table/kanrisha.js"></script>
	
<link href="${path }/css/jquery.alerts.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="${path }/css/alertstyle.css" rel="stylesheet"
	type="text/css" media="screen" />
	<script src="${path }/js/jquery.alerts.js" type="text/javascript"></script>
	
</head>
<body>
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">公告</div>
			<c:if test="${member.result.type!='5'}">				
				<ul class="nav right center-text">
						<li id="read" class="active">查看公告</li>
						<li id="publish" class="active">发布公告</li>
						<li id="mynotice" class="active btn">我的发布</li>
				</ul>
			</c:if>
		</nav>
		<div style="margin-bottom: 20px">
						<table class="tables datatable" style="word-break:break-all;word-wrap:break-word">
								<thead>
									<tr>
										<th style="width: 40px">编号</th>
										<th style="width: 200px">标题</th>
										<th style="width: 120px">发布日期</th>
										<th style="width: 120px">公告类型</th>
										<th style="width: 80px">浏览次数</th>
										<th style="width: 100px">操作</th>	
									</tr>
								</thead>
								<tbody>							
									<c:forEach items="${result.result }"  var="data"   varStatus="status">				
										<tr>
											<td>${status.index+1}</td>
											<td>${ data.title}</td>
											<td>${ data.publishDate.substring(0, 10)}</td>
											<td><c:if test="${ data.contentType=='1'}">网页公告</c:if><c:if test="${ data.contentType=='0'}">文本公告</c:if></td>
											<td>${ data.times}</td>
											<td>
											<input type="button" value="删除" class="delete"  dataid="${ data.id}" datatype="${ data.contentType}">
											<input type="button" value="编辑" class="update"  dataid="${ data.id}" datatype="${ data.contentType}">
											</td>
										</tr>
									</c:forEach>				
								</tbody>					
							</table>	
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
		$(".delete")
		.click(
				function() {
					var id=$(this).attr("dataid");
					$.post("${path }/webnotice/deleteNotice.jhtml",{id:id },
							   function(data){
							       //var json = eval('(' + data + ')'); 字符串转化为json对象
									if (data.flag=='1') {
										$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
										jAlert("删除成功!", '消息提醒', function() {
											$.alerts.dialogClass = null; // 重置到默认值
											location = "${path }/webnotice/getPublishNotice.jhtml?id=${member.result.id}";
										});
									} else {
										$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
										jAlert("删除失败，请重试!", '消息提醒', function() {
											$.alerts.dialogClass = null; // 重置到默认值
										});
									}
							   }
					);
							
							
															
				});
		$(".update")
		.click(
				function() {
					var id="${member.result.id}";
					var nid=$(this).attr("dataid");
					var contentType=$(this).attr("datatype");
					window.open('${path }/webnotice/toUpdateNotice.jhtml?id='+id+"&type="+contentType+"&nid="+nid, '修改校史', 'height=400, width=900, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')   
				});
	</script>

</body>
</html>