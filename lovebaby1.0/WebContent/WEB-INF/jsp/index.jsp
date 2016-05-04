<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>桌面</title>
<link rel="stylesheet" href="${path }/desktop/css/table/style.css" />
<link rel="stylesheet" type="text/css" href="${path }/desktop/global.css" />
<link rel="stylesheet" href="${path }/desktop/jsLib/themes/base/jquery.ui.all.css" />
<link rel="stylesheet" href="${path }/desktop/jsLib/jquery-smartMenu/css/smartMenu.css" />
<script type="text/javascript" src="${path }/desktop/jsLib/jquery-1.6.2.js"></script>
<script type="text/javascript" src="${path }/desktop/jsLib/myLib.js"></script>
<script type="text/javascript" src="${path }/desktop/jsLib/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="${path }/desktop/jsLib/jquery.winResize.js"></script>
<script type="text/javascript" src="${path }/desktop/jsLib/jquery-smartMenu/js/mini/jquery-smartMenu-min.js"></script>
<script type="text/javascript" src="${path }/desktop/global.js"></script>
</head>
<c:if test="${member.result.backimage=='moren.png'}">
<script type="text/javascript" src="${path }/desktop/js/jquery.cloud.js"></script>
<script type="text/javascript">
	setInterval("Refresh()", 1000);
</script>
<body id="mybody" style="min-width:980px; margin:0 auto; background:rgb(28, 119, 172); background-image:url(${path }/desktop/images/admin/light.png); background-repeat:no-repeat; overflow:hidden;background-position:center top;">
<div class="mainbody">
  <div id="cloud1" class="cloud" ></div>
  <div id="cloud2" class="cloud" ></div>
</div>
</c:if>
<c:if test="${member.result.backimage!='moren.png'}">
<body id="mybody" style="min-width:980px; margin:0 auto; background-image:url('${themeurl }${member.result.backimage }');overflow:hidden;background-position:center top;">

</c:if>
<ul id="deskIcon" path="${path }">
	<c:forEach items="${result.result }"  var="data"   varStatus="status">									
	  <li class="desktop_icon " id="win${status.index+1 }" path="${path }${data.url }?status=0&id=${member.schools.size()==0?null:member.schools.get(0).id}" dataid="${member.result.id }" name="${data.type }" tid="${data.id }"> 
	  	 <c:if test="${data.name=='认证审核'&&result.applycount!=0}">	  	 
		 	<div class="big_count" style="position: absolute;float: right;margin-left: 65px"><span>${ result.applycount}</span></div>
	  	 </c:if>
	  	 <c:if test="${data.name=='公告'&&result.noticecount!=0}">	  	 
		 	<div class="big_count" style="position: absolute;float: right;margin-left: 65px"><span>${ result.noticecount}</span></div>
	  	 </c:if>
		 <span class="icon">
		 	<img src="${ data.ftpimage}"/>
		 </span>
	     <div class="text">${data.name }<div class="right_cron"></div></div>	    
	  </li>
	</c:forEach>				 
</ul>
<div id="taskBar">
  <div id="leftBtn"><a href="#" class="upBtn"></a></div>
  <div id="rightBtn"><a href="#" class="downBtn"></a> </div>
  <div id="task_lb_wrap">
    <div id="task_lb"></div>
  </div>
</div>
<div id="lr_bar">
  <ul id="default_app">
    <li class="index"><img src="${path }/desktop/icon/icon12.png" title="首页"/></li>
    <li id="app0"><span><img src="${path }/desktop/icon/icon1.png" title="全屏"   onclick="launchFullscreen(document.documentElement);"/></span></li>
    <li id="app3"><img src="${path }/desktop/icon/icon2.png" title="联系人" path="../tishi/tishi.html" /></li>
    <li id="app1"><img src="${path }/desktop/icon/icon11.png" title="朋友圈" path="../tishi/tishi.html"/></li>
  </ul>
  <div id="default_tools"> 
	  <span id="showZm_btn" onclick="exitFullscreen(document.documentElement);" title="退出全屏" ></span>
	  <span id="shizhong_btn" class="tools" title="时钟" path="../tools/clock.html"></span>
	  <span id="weather_btn" class="tools" title="天气" path="../tools/weather.html"></span> 
	  <span id="them_btn" class="tools" title="主题" path="${path }/desktop/theme.jhtml?id=${member.result.id}&page_size=8&type=1"></span>
  </div>
  <div id="start_block"> <a title="开始" id="start_btn"></a>
    <div id="start_item">
      <ul class="item admin">
        <li id="adminImg" title="修改资料" path="${path }/desktop/profile.jhtml">
        <div style="margin-left: 10px">
        	<div style="float:left;">
        		<img alt="" src="${member.result.ftpimage }" width="25px" height="25px">     
        	</div>
        	<div style="float: left;margin-left: 12px">
			    <c:if test="${member.result.realName==null }">${member.result.telephone }</c:if>
			    <c:if test="${member.result.realName!=null }">${member.result.realName}</c:if>
        	</div>	
        </div>	        
        </li>
      </ul>
      <ul  class="item">
        <li id="sitting" title="系统设置" path="${path }/desktop/setting.jhtml"><span  class="sitting_btn"></span>系统设置</li>
       <!--  <li id="help" title="使用指南" path="clock.html"><span  class="help_btn"></span>使用指南</li>
        <li id="about" title="关于我们" path="clock.html"><span  class="about_btn"></span>关于我们</li> -->
        <li class="layout"><span  class="logout_btn"></span>退出系统</li>
      </ul>
    </div>
  </div>
</div>


	
  <div style="margin:70px;">
   
  </div>


<script>
var layout="${path }/j_spring_security_logout";
var index="${path }/view.jhtml";
// Find the right method, call on correct element
function launchFullscreen(element) {
  if(element.requestFullscreen) {
    element.requestFullscreen();
  } else if(element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
  } else if(element.webkitRequestFullscreen) {
    element.webkitRequestFullscreen();
  } else if(element.msRequestFullscreen) {
    element.msRequestFullscreen();
  }
}

function exitFullscreen() {
  if(document.exitFullscreen) {
    document.exitFullscreen();
  } else if(document.mozCancelFullScreen) {
    document.mozCancelFullScreen();
  } else if(document.webkitExitFullscreen) {
    document.webkitExitFullscreen();
  }
}

function dumpFullscreen() {
  console.log("document.fullscreenElement is: ", document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement);
  console.log("document.fullscreenEnabled is: ", document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled || document.msFullscreenEnabled);
}

// Events
document.addEventListener("fullscreenchange", function(e) {
  console.log("fullscreenchange event! ", e);
});
document.addEventListener("mozfullscreenchange", function(e) {
  console.log("mozfullscreenchange event! ", e);
});
document.addEventListener("webkitfullscreenchange", function(e) {
  console.log("webkitfullscreenchange event! ", e);
});
document.addEventListener("msfullscreenchange", function(e) {
  console.log("msfullscreenchange event! ", e);
});

//退出设置
$(".layout").click(function(){
	location=layout;
});

//返回首页
$(".index").click(function(){
	location=index;
});
</script>




</body>
</html>
