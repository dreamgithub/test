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
<link rel="stylesheet" href="${path }/css/zyUpload.css" type="text/css">
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
<script type="text/javascript">
var firstpicname="";
</script>
</head>
<body>
        
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">校史</div>
			<c:if test="${member.result.type=='2'||member.result.type=='3'}">				
				<ul class="nav right center-text">
						<li id="read" class="active">查看校史</li>
						<li id="publish" class="active btn">发布校史</li>
						<li id="mypublish" class="active">我的发布</li>
						
				</ul>
			</c:if>
		</nav>
		<div>
				<form action="${path }/webhistories/addhistories.jhtml">
					<div style=" width: 100%;height: 50px;margin-top: 30px;margin-bottom: -20px;margin-left: 30px">
						<span style="font-weight:bold;margin-right: 20px ">标题</span><input type="text" name="title" style="width: 250px" id="title">
						<span style="font-weight:bold;margin-right: 20px ">校史日期</span><input type="date" name="date" style="width: 250px" id="date">
					</div>
					<!-- 加载编辑器的容器 -->
					<script id="container" name="content" type="text/plain">
      		 			 这里写你的初始化内容
    				</script>
					<input type="button" value="提交" id="sub">
					 
				</form>

				<!-- 配置文件 -->
				<script type="text/javascript"
					src="${path }/UEditor/ueditor.config.js"></script>
				<script type="text/javascript"
					src="${path }/UEditor/ueditor.parse.js"></script>
				<!-- 编辑器源码文件 -->
				<script type="text/javascript" src="${path }/UEditor/ueditor.all.js"></script>
				<!-- 实例化编辑器 -->
				<script type="text/javascript">
					var ue = UE.getEditor('container', {
						toolbars : [ [ 'fullscreen', 'source', '|', 'undo',
								'redo', '|', 'bold', 'italic', 'underline',
								'fontborder', 'strikethrough', 'superscript',
								'subscript', 'removeformat', 'formatmatch',
								'autotypeset', 'blockquote', 'pasteplain', '|',
								'forecolor', 'backcolor', 'insertorderedlist',
								'insertunorderedlist', 'selectall', 'cleardoc',
								'|', 'rowspacingtop', 'rowspacingbottom',
								'lineheight', '|', 'customstyle', 'paragraph',
								'fontfamily', 'fontsize', '|',
								'directionalityltr', 'directionalityrtl',
								'indent', '|', 'justifyleft', 'justifycenter',
								'justifyright', 'justifyjustify', '|',
								'touppercase', 'tolowercase', '|', 'link',
								'unlink', 'anchor', '|', 'imagenone',
								'imageleft', 'imageright', 'imagecenter', '|',
								'simpleupload', 'insertimage', 'emotion',
								'scrawl', 'map', 'insertframe', 'insertcode',
								'template', 'background', '|', 'horizontal',
								'date', 'time', 'spechars', 'snapscreen',
								'wordimage', '|', 'inserttable', 'deletetable',
								'insertparagraphbeforetable', 'insertrow',
								'deleterow', 'insertcol', 'deletecol',
								'mergecells', 'mergeright', 'mergedown',
								'splittocells', 'splittorows', 'splittocols',
								'charts', '|', 'print', 'preview',
								'searchreplace', 'help' ] ],
						autoHeightEnabled : true,
						autoFloatEnabled : true
					});
					uParse('.content', {
						rootPath : URL
					})    ;
    
				</script>

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
		$("#sub")
		.click(
				function() {
					var content;
					var schoolId="${member.schools.size()==0?null:member.schools.get(0).id}";
					var title=$("#title").val();
					var date=$("#date").val();
					ue.ready(function(){
						content=ue.getContent();
					});
					if (title=="") {
						$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
						jAlert("标题不能为空!", '消息提醒', function() {
							$.alerts.dialogClass = null; // 重置到默认值
						});
					}else if (date=="") {
						$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
						jAlert("校史日期不能为空!", '消息提醒', function() {
							$.alerts.dialogClass = null; // 重置到默认值
						});
					}else{
						firstpicname=firstpicname!=""?firstpicname:"notice.png";
						$.post("${path }/webhistories/addhistories.jhtml",{content:content,schoolId:schoolId,title:title,date:date,picname:firstpicname },
								   function(data){
								       //var json = eval('(' + data + ')'); 字符串转化为json对象
										if (data.flag=='1') {
											$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
											jAlert("发布成功!", '消息提醒', function() {
												$.alerts.dialogClass = null; // 重置到默认值
											});
										} else {
											$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
											jAlert("发布失败，请重试!", '消息提醒', function() {
												$.alerts.dialogClass = null; // 重置到默认值
											});
										}
								   }
						);
								
							
						}									
				});
	</script>

</body>
</html>