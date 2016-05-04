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
<link href="${path }/desktop/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">
<link href="${path }/desktop/css/magnific-popup.css" rel="stylesheet">
<link href="${path }/desktop/css/templatemo_style.css" rel="stylesheet"
	type="text/css">
<link href="${path }/css/jquery.alerts.css" rel="stylesheet"
	type="text/css" media="screen" />
<link href="${path }/css/alertstyle.css" rel="stylesheet"
	type="text/css" media="screen" />
<script src="${path }/desktop/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${path }/desktop/js/jquery.magnific-popup.min.js"></script>
<script type="text/javascript"
	src="${path }/desktop/js/templatemo_script.js"></script>
<script src="${path }/js/jquery.alerts.js" type="text/javascript"></script>

<!-- 表格资源引入 -->
    <link rel="stylesheet" href="${path }/desktop/css/table/style.css" />
	<!-- jQuery -->
	<script src="${path }/desktop/js/table/jquery.dataTables.min.js"></script>
	<script src="${path }/desktop/js/table/colResizable-1.3.js"></script>
	<script src="${path }/desktop/js/table/jquery-ui-1.8.21.min.js"></script>
	<script src="${path }/desktop/js/table/jquery.uniform.js"></script>
	<script src="${path }/desktop/js/table/kanrisha.js"></script>
</head>
<body>
        
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">校史</div>
			<c:if test="${member.result.type=='2'||member.result.type=='3'}">				
				<ul class="nav right center-text">
						<li id="read" class="active">查看校史</li>
						<li id="publish" class="active ">发布校史</li>
						<li id="mypublish" class="active btn">我的发布</li>
						
				</ul>
			</c:if>
		</nav>
		<div>
			<div class="widget_contents noPadding twCheckbox" style="margin-bottom: 20px">
						<table class="tables datatable" style="word-break:break-all;word-wrap:break-word">
							<thead>
								<tr>
									<th style="width: 40px">编号</th>
									<th style="width: 80px">图片</th>
									<th style="width: 200px">标题</th>
									<th style="width: 80px">校史日期</th>
									<th style="width: 100px">操作</th>	
								</tr>
							</thead>
							<tbody>							
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td><img alt="" src="${ data.ftpimage}" width="70px" height="40px"></td>
										<td>${ data.title}</td>
										<td>${ data.date}</td>
										<td>
											<input type="button" value="删除" class="delete"  dataid="${ data.id}">
											<input type="button" value="编辑" class="update"  dataid="${ data.id}">
										</td>
									</tr>
								</c:forEach>				
							</tbody>					
						</table>
				</div>
		</div>
	</div>
	<script type="text/javascript">
		$("#publish")
				.click(
						function() {
							location = "${path }/webhistories/histories.jhtml";
						});
		$("#mypublish")
		.click(
				function() {
					location = "${path }/webhistories/getPublishHistory.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
				});
		$("#read")
				.click(
						function() {
							location = "${path }/webhistories/readhistories.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
						});
		$(".delete")
		.click(
				function() {
					var id=$(this).attr("dataid");
					$.post("${path }/webhistories/deleteHistories.jhtml",{id:id },
							   function(data){
							       //var json = eval('(' + data + ')'); 字符串转化为json对象
									if (data.flag=='1') {
										$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
										jAlert("删除成功!", '消息提醒', function() {
											$.alerts.dialogClass = null; // 重置到默认值
											location = "${path }/webhistories/getPublishHistory.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
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
					var id=$(this).attr("dataid");
					window.open('${path }/webhistories/toUpdateHistories.jhtml?id='+id, '修改校史', 'height=400, width=900, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')   
				});
	</script>

</body>
</html>