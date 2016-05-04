<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" href="${path }/css/zyUpload.css" type="text/css">
	<link href="${path }/desktop/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="${path }/desktop/css/magnific-popup.css" rel="stylesheet"> 
	<link href="${path }/desktop/css/templatemo_style.css" rel="stylesheet" type="text/css">	
	<link href="${path }/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
  	<link href="${path }/css/alertstyle.css" rel="stylesheet" type="text/css" media="screen" />
	<script src="${path }/desktop/js/jquery.min.js"></script>
	<script type="text/javascript" src="${path }/desktop/js/jquery.magnific-popup.min.js"></script> 
	<script type="text/javascript" src="${path }/desktop/js/templatemo_script.js"></script>
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
			<div id="logo" class="left">认证审核</div>
			<ul class="nav right center-text">
				<li id="unread" class="active btn">未审核申请</li>
				<li id="read" class="active">已审核申请</li>			
			</ul>
		</nav>
		<!-- 推荐主题 -->
		<div>
			<div style="width: 100%;">
				<div class="g_12">
					<div class="widget_contents noPadding twCheckbox">
						<table class="tables datatable" style="word-break:break-all;word-wrap:break-word">
							<c:if test="${member.result.type!='4'}">
								<thead>
								<tr>
									<th style="width: 40px">编号</th>
									<th style="width: 80px">头像</th>
									<th style="width: 80px">姓名</th>
									<th style="width: 80px">电话</th>
									<th style="width: 200px">申请单位</th>
									<th style="width: 100px">申请类型</th>
									<th style="width: 150px">地址</th>
									<th style="width: 150px">申请日期</th>
									<th style="width: 150px">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td><img  src="${ data.ftpimage}" width="50px" height="50px" style="margin: auto"></td>
										<td>${ data.username}</td>
										<td>${ data.phone}</td>
										<td>${ data.name}</td>
										<td>
											<c:if test="${ data.type=='1'}">机构负责人</c:if>
											<c:if test="${ data.type=='2'}">园长</c:if>
											<c:if test="${ data.type=='3'}">园务</c:if>
											<c:if test="${ data.type=='4'}">老师</c:if>
											<c:if test="${ data.type=='5'}">家长</c:if>
										</td>
										<td>${ data.area}</td>
										<td>${ data.date}</td>
										<td>
											<input type="button" value="通过" class="add" title="${ data.id}">
											<input type="button" value="拒绝" class="deny" title="${ data.id}">
										</td>
									</tr>
								</c:forEach>								
							</tbody>
							</c:if>
							<c:if test="${member.result.type=='4'}">
								<thead>
								<tr>
									<th style="width: 50px">
										<input type="checkbox" class="simple_form tMainC" />
									</th>
									<th style="width: 50px">编号</th>
									<th style="width: 80px">头像</th>
									<th style="width: 80px">姓名</th>
									<th style="width: 80px">电话</th>
									<th style="width: 200px">申请单位</th>
									<th style="width: 100px">申请类型</th>
									<th style="width: 150px">地址</th>
									<th style="width: 80px">宝宝</th>
									<th style="width: 80px">关系</th>
									<th style="width: 150px">申请日期</th>
									<th style="width: 150px">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td><input type="checkbox" class="simple_form" /></td>
										<td>${status.index+1}</td>
										<td><img  src="${ data.ftpimage}" width="50px" height="50px" style="margin: auto"></td>
										<td>${ data.username}</td>
										<td>${ data.phone}</td>
										<td>${ data.name}</td>
										<td>
											<c:if test="${ data.type=='1'}">机构负责人</c:if>
											<c:if test="${ data.type=='2'}">园长</c:if>
											<c:if test="${ data.type=='3'}">园务</c:if>
											<c:if test="${ data.type=='4'}">老师</c:if>
											<c:if test="${ data.type=='5'}">家长</c:if>
										</td>
										<td>${ data.area}</td>
										<td>${ data.babyname}</td>
										<td>${ data.babyrelate}</td>
										<td>${ data.date}</td>
										<td>
											<input type="button" value="通过" class="add" title="${ data.id}">
											<input type="button" value="拒绝" class="deny" title="${ data.id}">
										</td>
									</tr>
								</c:forEach>								
							</tbody>
							</c:if>
						</table>
				</div>
			</div>
		</div>
			<%-- <div id="portfolio-content" class="center-text">				
				<div class="pagination">
					<ul class="nav">
						<c:if test="${result.pagenum!=0}">
							<li class="active">1</li>
						</c:if>
						<c:forEach   begin="2" end="${result.pagenum}" step="1" varStatus="status">					
							<li>${status.index}</li>
						</c:forEach>												
					</ul>
				</div>
			</div> --%>
		</div>		
	</div>
	<script type="text/javascript">

	$("#unread").click(function(){
		location="${path }/webauthentication/getAuthenticationList.jhtml?status=0";
	});
	$("#read").click(function(){
		location="${path }/webauthentication/getAuthenticationList.jhtml?status=1";
	});
	//认证通过
	$(".add").click(function(){
		var id=$(this).attr("title");
		$.post("${path }/webauthentication/authenticationCheck.jhtml",{aid:id,status:'1'},
				   function(data){
						if(data.flag=='1'){
							$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
							jAlert("操作成功!", '消息提醒', function() {
								$.alerts.dialogClass = null; // 重置到默认值
								location="${path }/webauthentication/getAuthenticationList.jhtml?status=0";
							});
						}else{
							$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
							jAlert("操作失败，请重试！", '消息提醒', function() {
								$.alerts.dialogClass = null; // 重置到默认值
							});
						}
				   });
		});
	//认证拒绝
	$(".deny").click(function(){
		var id=$(this).attr("title");
		$.post("${path }/webauthentication/authenticationCheck.jhtml",{aid:id,status:'2'},
				   function(data){
						if(data.flag=='1'){
							$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
							jAlert("操作成功!", '消息提醒', function() {
								$.alerts.dialogClass = null; // 重置到默认值
								location="${path }/webauthentication/getAuthenticationList.jhtml?status=0";
							});
						}else{
							$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
							jAlert("操作失败，请重试！", '消息提醒', function() {
								$.alerts.dialogClass = null; // 重置到默认值
							});
						}
				   });
		});
	</script>

</body>
</html>