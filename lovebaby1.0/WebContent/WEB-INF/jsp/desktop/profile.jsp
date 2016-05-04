<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="${path }/desktop/css/basic.css" rel="stylesheet" type="text/css">
<link href="${path }/desktop/css/style.css" rel="stylesheet" type="text/css">
<link href="${path }/css/flat-ui/bootstrap.min.css" rel="stylesheet">
<link href="${path }/css/flat-ui/demo.css" rel="stylesheet">
 <!-- Loading Flat UI -->
<link href="${path }/css/flat-ui/flat-ui.css" rel="stylesheet">
<script src="${path }/desktop/js/jquery.min.js"></script>
<script src="${path }/desktop/js/index.js"></script>
<script src="${path }/js/jquery.alerts.js" type="text/javascript"></script>
<script type="text/javascript" src="${path }/js/swfobject.js"></script>
<script type="text/javascript" src="${path }/js/fullAvatarEditor.js"></script>
<link href="${path }/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${path }/css/alertstyle.css" rel="stylesheet" type="text/css" media="screen" />

</head>
	<body>
	<div style="max-width: 1000px;margin:auto;background: white;box-shadow: 0 0 5px rgba(107, 110, 112, 0.4);margin-top: 80px ">		
		<div>
			<nav style="border-bottom: #DDDDDD 1px solid;height: 100px">
				<div id="logo">个人资料</div>
			</nav>
		</div>
		<div style="height: 500px;width: 100%;position: relative;">
			<ul>
				<li id="baseinfo"><A  class="hover" href="#">基本信息</A></li>
				<li id="updatepsw"><A href="#">修改密码</A></li>
				<li id="updateprotrait"><A href="#">上传头像</A></li>
				<div id="lanPos"></div>
			</ul>
			<!-- 修改基本信息 -->
			<div id="info" style="height: 400px;border-left: #DDDDDD 1px solid;width:70%;margin-left:250px;position: absolute;">
				<div  style="margin-top: 50px;margin-left: 20%">
					<form  action="${path }/applogin/updateInfo.jhtml" method="post">
						<div class="row" style="border-bottom:#DDDDDD 1px solid;width: 450px; ">							
							<div class="col-xs-12" style="margin-bottom: 10px;">
								<div class="col-xs-3">
						            	姓&nbsp;&nbsp;&nbsp;名:		
        						</div>
						        <div class="col-xs-9">
						            <input type="text" value="${member.result.realName}" name="realName"  class="form-control" id="realName" >
						        </div>			
        					</div>							
						</div>
						<div class="row" style="border-bottom:#DDDDDD 1px solid;width: 450px; ">							
							<div class="col-xs-12" style="margin-bottom: 10px;margin-top: 10px">
								<div class="col-xs-3">
						            	生&nbsp;&nbsp;&nbsp;日:		
        						</div>
						        <div class="col-xs-9">
						           <input type="date" name="birthday" id="birthday" class="form-control" value="${member.result.birthday}"/>
						        </div>			
        					</div>							
						</div>
						<div class="row" style="border-bottom:#DDDDDD 1px solid;width: 450px; ">							
							<div class="col-xs-12" style="margin-bottom: 10px;margin-top: 10px">
								<div class="col-xs-3">
						            	性&nbsp;&nbsp;&nbsp;别:		
        						</div>
						        <div class="col-xs-9" id="wrap">
						        	<c:if test="${member.result.sex=='男'}">
							        	<div class="col-xs-6">
		          						   <label class="radio">
		            							<input type="radio" name="sex" id="optionsRadios2" value="男" data-toggle="radio" checked="" class="custom-radio">
		            							<span class="icons"><span class="icon-unchecked"></span><span class="icon-checked"></span></span>
		           								 男
		          						   </label>
	          						    </div>
							        	<div class="col-xs-6" >
								           <label class="radio">
		           							 <input type="radio" name="sex" id="optionsRadios1" value="女" data-toggle="radio" class="custom-radio" />
		           							 女<span class="icons"><span class="icon-unchecked"></span><span class="icon-checked"></span>
		           							 </span>
		          						   </label>
		          						 </div>
	          						 </c:if>
	          						 <c:if test="${member.result.sex!='男'}">
							        	<div class="col-xs-6">
		          						   <label class="radio">
		            							<input type="radio" name="sex" id="optionsRadios2" value="男" data-toggle="radio"  class="custom-radio">
		            							<span class="icons"><span class="icon-unchecked"></span><span class="icon-checked"></span></span>
		           								 男
		          						   </label>
	          						    </div>
							        	<div class="col-xs-6" >
								           <label class="radio">
		           							 <input type="radio" name="sex" id="optionsRadios1" value="女" data-toggle="radio" checked="" class="custom-radio" />
		           							 女<span class="icons"><span class="icon-unchecked"></span><span class="icon-checked"></span>
		           							 </span>
		          						   </label>
		          						 </div>
	          						 </c:if>
						        </div>			
        					</div>							
						</div>	
						<div class="col-xs-12" style="margin-top: 50px">
							<div class="col-xs-3">
					        </div>
							<div class="col-xs-4">
					          <a href="#" class="btn btn-block btn-lg btn-primary" id="myinfo">提&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;交</a>
					        </div>
						</div>					
					</form>
				</div>
			</div>
			<!-- 修改密码 -->
			<div id="psw" style="height: 400px;border-left: #DDDDDD 1px solid;width:70%;margin-left:250px;position: absolute;display: none">
						<div  style="margin-top: 50px;margin-left: 20%">
					<form  action="" method="post">
						<div class="row" style="border-bottom:#DDDDDD 1px solid;width: 450px; ">							
							<div class="col-xs-12" style="margin-bottom: 10px;">
								<div class="col-xs-3">
						            	原密码:		
        						</div>
						        <div class="col-xs-9">
						            <input type="text" value="" name="oldpsw"  class="form-control" id="oldpsw" >
						        </div>			
        					</div>							
						</div>
						<div class="row" style="border-bottom:#DDDDDD 1px solid;width: 450px; ">							
							<div class="col-xs-12" style="margin-bottom: 10px;margin-top: 10px">
								<div class="col-xs-3">
						            	新密码:		
        						</div>
						        <div class="col-xs-9">
						            <input type="text" value="" name="newpsw"  class="form-control" id="newpsw" >
						        </div>			
        					</div>							
						</div>
						<div class="col-xs-12" style="margin-top: 50px">
							<div class="col-xs-3">
					        </div>
							<div class="col-xs-4">
					          <a href="#" class="btn btn-block btn-lg btn-primary" id="mypsw">提&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;交</a>
					        </div>
						</div>					
					</form>
				</div>
			</div>
			<!-- 修改头像 -->
			<div id="protrait" style="height: 400px;border-left: #DDDDDD 1px solid;width:70%;margin-left:250px;position: absolute;display: none">
				<div>
					<div class="col-xs-12" >
						<p id="swfContainer">本组件需要安装Flash Player后才可使用，请从<a href="http://www.adobe.com/go/getflashplayer">这里</a>下载安装。</p>
	               </div>
				</div>
				
			</div>
		</div>
		
	</div>
	
	<script type="text/javascript">
		$("#myinfo").click(function(){
			var id="${member.result.id}";
			var realName=$("#realName").val();
			var birthday=$("#birthday").val();
			var obj_sex = $('#wrap input[name="sex"]');
			var sex = $('#wrap input[name="sex"]:checked ').val();
			$.post("${path }/applogin/updateInfo.jhtml",{id:id,realName:realName,birthday:birthday,sex:sex},
					   function(data){
							if(data.flag==1){
								$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
								jAlert("信息修改成功!", '消息提醒', function() {
									$.alerts.dialogClass = null; // 重置到默认值
								});
							}else{
								$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
								jAlert("信息修改失败!", '消息提醒', function() {
									$.alerts.dialogClass = null; // 重置到默认值
								});
							}
					   });
		});
		$("#mypsw").click(function(){
			var id="${member.result.id}";
			var oldpsw=$("#oldpsw").val();
			var password=$("#newpsw").val();
			$.post("${path }/applogin/updatepsw.jhtml",{id:id,oldpsw:oldpsw,password:password},
					   function(data){
							if(data.flag==1){
								$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
								jAlert("信息修改成功!", '消息提醒', function() {
									$.alerts.dialogClass = null; // 重置到默认值
								});
							}else{
								$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
								jAlert(data.msg, '消息提醒', function() {
									$.alerts.dialogClass = null; // 重置到默认值
								});
							}
					   });
		});
		$("#baseinfo").click(function(){
			$("#info").show();
			$("#psw").hide();
			$("#protrait").hide();
		});
		$("#updatepsw").click(function(){

			$("#info").hide();
			$("#protrait").hide();
			$("#psw").show();
		});
		$("#updateprotrait").click(function(){
			$("#protrait").show();
			$("#info").hide();
			$("#psw").hide();
		});
	</script>
	<script type="text/javascript">
            swfobject.addDomLoadEvent(function () {
				var swf = new fullAvatarEditor("${path }/uploadportrait/fullAvatarEditor.swf", "${path }/uploadportrait/expressInstall.swf", "swfContainer", {
					    id : 'swf',
						upload_url : '${path }/applogin/updatePortrait.jhtml?id=${member.result.id}',	//上传接口
						method : 'post',	//传递到上传接口中的查询参数的提交方式。更改该值时，请注意更改上传接口中的查询参数的接收方式
						src_upload :0,		//是否上传原图片的选项，有以下值：0-不上传；1-上传；2-显示复选框由用户选择
						avatar_box_border_width : 0,
						avatar_sizes : '100*100',
						avatar_sizes_desc : '100*100像素',
						avatar_field_names :'file',
						isShowUploadResultIcon :false
					}, function (msg) {
						switch(msg.code)
						{
							case 1 :break;
								document.getElementById("upload").style.display = "inline";
								break;
							case 3 :
								if(msg.type == 0)
								{
									alert("摄像头已准备就绪且用户已允许使用。");
								}
								else if(msg.type == 1)
								{
									alert("摄像头已准备就绪但用户未允许使用！");
								}
								else
								{
									alert("摄像头被占用！");
								}
							break;
							case 5 : 
								if(msg.content.flag=='1'){
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("头像上传成功!", '消息提醒', function() {
										$.alerts.dialogClass = null; // 重置到默认值
									});
								}else{
									$.alerts.dialogClass = "style_1"; // 设置自定义样式的Class
									jAlert("头像上传失败!", '消息提醒', function() {
										$.alerts.dialogClass = null; // 重置到默认值
									});
								}								
							break;
						}
					}
				);
				document.getElementById("upload").onclick=function(){
					swf.call("upload");
				};
            });
			
        </script>
</body>
</html>
