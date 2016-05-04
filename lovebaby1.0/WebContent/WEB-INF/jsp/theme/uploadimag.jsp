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
				<li id="tuijian" class="active">推荐主题</li>
				<li id="shangchuan" class="active">我的上传</li>
				<li id="moren" class="active">默认主题</li>	
				<li id="upload" class="btn active">上传主题</li>				
			</ul>
		</nav>	
		<!-- 上传主题 -->
		<div id="container4" class="content-container" >
			<header>
				<h1 class="center-text">上传主题</h1>
			</header>
			<div id="portfolio-content" class="center-text">			
				<div id="demo" class="demo"><span style="font-size: 12px">(推荐尺寸为1440X900以上)</span></div>										
			</div>
		</div>	       
	</div>
	<script type="text/javascript">
	var uploadurl='${path }/desktop/uploadTheme.jhtml?tid=${member.result.id }';
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
			
			$(".delete").click(function(){
				var id=$(this).attr("title");
				$.post("${path }/desktop/deleteTheme.jhtml",{id:id},
						   function(data){
								if(data.flag=='1'){
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("删除成功!", '消息提醒', function() {
										$.alerts.dialogClass = null; // 重置到默认值
										location="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8";
									});
								}else{
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("删除失败，请重试！", '消息提醒', function() {
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
		<!-- 引用s上传图片核心层插件 -->
	<script type="text/javascript" src="${path }/js/upload/uploadtheme.js"></script>
	<script type="text/javascript" src="${path }/js/upload/zyUpload.js"></script>
	<script type="text/javascript" src="${path }/js/upload/demo.js"></script>	
</body>
</html>