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
  
</head>
<body>
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">主题设置</div>
			<ul class="nav right center-text">
				<li id="tuijian" class=" active">推荐主题</li>
				<li id="shangchuan" class="active">我的上传</li>
				<li id="moren" class="btn active">默认主题</li>	
				<li id="upload" class="active">上传主题</li>				
			</ul>
		</nav>
		
		<!-- 默认主题 -->
		<div id="container3" class="content-container" >
			<header>
				<h1 class="center-text">默认主题</h1>
			</header>
			<div id="portfolio-content" class="center-text">
				<c:forEach   begin="1" end="${result.pagenum==0?1:result.pagenum}" step="1" varStatus="status">
					<c:if test="${status.index==1}">				
						<div class="portfolio-page" id="page-${status.index}">
					</c:if>	
					<c:if test="${status.index!=1}">				
						<div class="portfolio-page" id="page-${status.index}" style="display:none;">
					</c:if>		
						<c:forEach items="${result.result }"  var="data"   begin="${(status.index-1)*(result.pagesize)}" end="${(status.index)*(result.pagesize)-1}" step="1" varStatus="status">				
							<div class="portfolio-group ">
								<a class="portfolio-item" href="${data.ftpimage }">
									<img src="${data.ftpimage }">
									<span class="detail">
										<span  class="btn">预览</span>
										<span  class="btn use" ><span class="choose" title="${data.name }">使用</span></span>
									</span>
								</a>				
							</div>		
						</c:forEach>		
					</div>
				</c:forEach>
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
			</div>
		</div>	     
		
	</div>
	<script type="text/javascript">
		$(function () {
			$(".use").click(function(){				
				return false
			});
			$(".del").click(function(){				
				return false
			});
			$(".choose").click(function(){
				var name=$(this).attr("title");
				//js有类型，字符串值必须加引号
				var id='${member.result.id}';
				$.post("${path }/desktop/chooseTheme.jhtml",{id:id,name:name},
						   function(data){
								if(data.flag=='1'){
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("应用成功，请刷新桌面!", '消息提醒', function() {
										$.alerts.dialogClass = null; // 重置到默认值
									});
								}else{
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("应用失败，请重试！", '消息提醒', function() {
										$.alerts.dialogClass = null; // 重置到默认值
									});
								}
						   });
				});
			$('.pagination li').click(changePage);
			$('.portfolio-item').magnificPopup({ 
				type: 'image',
				gallery:{
					enabled:false
				}
			});
			
			$('.pagination li').click(changePage);
			$('.portfolio-item').magnificPopup({ 
				type: 'image',
				gallery:{
					enabled:false
				}
			});
		});
		
		$("#tuijian").click(function(){
			location="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8&type=1";
		});
		$("#shangchuan").click(function(){
			location="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8&type=2";
		});
		$("#moren").click(function(){
			location="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8&type=0";
		});
		$("#upload").click(function(){
			location="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8&type=3";
		});
	</script>
</body>
</html>