<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Language" content="zh-CN">
<title>文本公告</title>
<script src="http://libs.baidu.com/jquery/1.7.2/jquery.min.js"></script>
<link rel="stylesheet" href="${path }/css/SYTLE.css" />
</head>
<body>
<div style="width: 940px;margin: auto;">
	<div style="width: 220px;margin: auto;margin-top: 50px"><h1>${result.result.title }</h1></div>
	<div style="word-wrap:break-word; width:920px;margin-top: 10px;margin-left: 10px">&nbsp;&nbsp;&nbsp;&nbsp;${result.result.content }</div>
</div>
<c:if test="${result.result.pics.size()!=0 }">

<div class="foucebox">
  <div class="bd">
  	<c:forEach items="${result.result.pics }"  var="data"   varStatus="status">		
	    <div class="showDiv"> <a href="#"><img src="${data.ftpimage }"></a></div>
  	</c:forEach>
  </div>
  <div class="hd">
    <ul>
      <c:forEach items="${result.result.pics }"  var="data" begin="0" end="0"   varStatus="status">		
	      <li class="on"><a href="#"><img  src="${data.ftpimage }"><span class="mask"></span></a></li>
  	  </c:forEach>
  	  <c:forEach items="${result.result.pics }"  var="data" begin="1"    varStatus="status">		
	      <li><a href="#"><img  src="${data.ftpimage }"><span class="mask"></span></a></li>
  	  </c:forEach>
    </ul>
  </div>
</div>
</c:if>
<script type="text/javascript" src="${path }/js/jquery.SuperSlide.js"></script> 
<script type="text/javascript">
jQuery(".foucebox").slide({ effect:"fold", autoPlay:true, delayTime:300, startFun:function(i){jQuery(".foucebox .showDiv").eq(i).find("h2").css({display:"none",bottom:0}).animate({opacity:"show",bottom:"60px"},300);jQuery(".foucebox .showDiv").eq(i).find("p").css({display:"none",bottom:0}).animate({opacity:"show",bottom:"10px"},300);}});
</script>

</body>
</html>
