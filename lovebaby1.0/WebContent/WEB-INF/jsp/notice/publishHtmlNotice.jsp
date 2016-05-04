<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>发布网页公告</title>
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
<link href="${path }/css/duoxuan.css" rel="stylesheet" type="text/css" />

<script src="${path }/desktop/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${path }/desktop/js/jquery.magnific-popup.min.js"></script>
<script type="text/javascript"
	src="${path }/desktop/js/templatemo_script.js"></script>
<script src="${path }/js/jquery.alerts.js" type="text/javascript"></script>
<script type='text/javascript' src="${path }/js/jquery.selectlayer.js"></script>
<script type="text/javascript">
var firstpicname="";
</script>
</head>
<body>
	<div class="main-container" style="margin-top: -30px">
		<div >
				<form action="${path }/webnotice/addnotice.jhtml" >
					<div style=" width: 100%;height: 50px;margin-top: 30px;margin-bottom: -20px;">
						<span style="font-weight:bold;margin-right: 20px ">标题</span><input type="text" name="title" style="width: 200px" id="title">
						<span style="font-weight:bold;margin-right: 20px ">&nbsp;&nbsp;&nbsp;&nbsp;添加推送对象</span>
						<input name="district_cn"  id="district_cn" type="text" value="请选择推送对象"  readonly="true" class="sltinput" />
						<input name="citycategory" id="citycategory" type="hidden" value="" />
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
		
	
	<div style="display:none;position: absolute;margin-top: -300px;" id="sel_district">
		
		<div class="OpenFloatBox" style="background-color:#e1e1e1">
			<div class="title">
				<h4>请选择推送对象</h4>
				<div class="DialogClose" title="关闭"></div>
			</div>
			<div class="content">
				<div class="txt">
					<c:if test="${member.result.type=='1'}">					
						<div class="item">
							<label title="全部" class="titem"><input  type="checkbox" value="593"  title="全部" class="b" choose="all"/>全部</label>
							<div class="sitem"></div>
						</div>
						<c:forEach items="${result.danwei }"  var="data"   varStatus="status">
							<div class="item">
								<label title="${data.schoolName }" class="titem"><input  type="checkbox" value="${data.id }"  title="${data.schoolName }" class="b" choose="a"/>${data.schoolName }</label>
								<div class="sitem"></div>
							</div>
						</c:forEach>
					</c:if>
					<c:if test="${member.result.type=='2'||member.result.type=='3'}">	
									
						<div class="item">
							<label title="校内和家长" class="titem"><input id="allschool"  type="checkbox" value="0"  title="校内和家长" class="b" choose="a"/>校内和家长</label>
							<div class="sitem"></div>
						</div>
						<div class="item" >
							<label title="仅校内" class="titem"><input id="toschool"   type="checkbox" value="1"   title="仅校内" class="b" choose="a"/>仅校内</label>
							<div class="sitem"></div>
						</div>
					</c:if>
					<c:if test="${member.result.type=='4'}">					
						<div class="item">
							<label title="全部" class="titem"><input  type="checkbox" value="593"  title="全部" class="b" choose="all"/>全部</label>
							<div class="sitem"></div>
						</div>
						<c:forEach items="${result.danwei }"  var="data"   varStatus="status">
							<div class="item">
								<label title="${data.className }" class="titem"><input  type="checkbox" value="${data.id }"  title="${data.className }" class="b" choose="a"/>${data.className }</label>
								<div class="sitem"></div>
							</div>
						</c:forEach>
					</c:if>
				</div>
				<div class="txt">
					<div class="selecteditem"></div>
				</div>
				<div class="txt">
					<div align="center"><input type="button"  class="but80 Set" value="确定" /></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">

		$("#publish")
				.click(
						function() {
							location = "${path }/webhistories/histories.jhtml";
						});
		$("#read")
				.click(
						function() {
							location = "${path }/webhistories/readhistories.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
						});
		$("#sub")
		.click(
				function() {
					firstpicname=firstpicname!=""?firstpicname:"notice.png";
					var content;
					var from_id="${member.danwei.get(0).tid}";
					var from_type="${member.result.type}";
					var title=$("#title").val();
					var uid="${member.result.id}"; 
					var choose=$("#district_cn").val();
					var toids="";
					var textContent;
					ue.ready(function(){
						//获取html内容
						content=ue.getContent();
						//获取纯文本内容
						var txt=ue.getContentTxt();
						//截取文本前100位
						textContent= txt.substr(0, 100);
					});
					if (title=="") {
						$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
						jAlert("标题不能为空!", '消息提醒', function() {
							$.alerts.dialogClass = null; // 重置到默认值
						});
					}else if(choose=="请选择推送对象"||choose=="未选择"){
						$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
						jAlert("请选择推送对象!", '消息提醒', function() {
							$.alerts.dialogClass = null; // 重置到默认值
						});
					}else{
						if (from_type=='1'||from_type=='4') {
							if ($("input[choose='all']:checkbox:checked").length==0) {								
								var to_type="1";
								$("input[choose='a']:checkbox:checked").each(function(){ 
									toids+=$(this).val()+"," 
									});		
								$.post("${path }/webnotice/addHtmlNotice.jhtml",{from_id:from_id,from_type:from_type,title:title,content:content,contentType:'1',to_type:to_type,toids:toids,uid:uid,textContent:textContent,picname:firstpicname},
										   function(data){
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
							}else{
								var to_type="0";
								$.post("${path }/webnotice/addHtmlNotice.jhtml",{from_id:from_id,from_type:from_type,title:title,content:content,contentType:'1',to_type:to_type,uid:uid,textContent:textContent,picname:firstpicname },
										   function(data){
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
						}else if (from_type=='2'||from_type=='3') {
							var to_type=$("input[choose='a']:checkbox:checked").val();
							$.post("${path }/webnotice/addHtmlNotice.jhtml",{from_id:from_id,from_type:from_type,title:title,content:content,contentType:'1',to_type:to_type,uid:uid,textContent:textContent,picname:firstpicname },
									   function(data){
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
									
					}
				});
		
		//多选菜单操作
		$(document).ready(function(){
			var QS_city=new Array() 
			OpenCategoryLayer(
				"#district_cn",
				"#sel_district",
				"#district_cn",
				"#citycategory",
				QS_city,
			14);
		});
		
		$("#allschool")
		.click(
				function() {
					$("#toschool").removeAttr("checked");
				});
		$("#toschool")
		.click(
				function() {
					$("#allschool").removeAttr("checked");
					
				});
		$("input[choose='all']")
		.click(
				function() {
					$("input[choose='a']").removeAttr("checked");
					
				});
		$("input[choose='a']")
		.click(
				function() {
					$("input[choose='all']").removeAttr("checked");
					
				});

	</script>
</body>
</html>