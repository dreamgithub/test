<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>jquery手机网站数据列表分页样式代码 - xw素材网</title>

<style>
*{margin:0px; padding:0px;}
body{margin:0 auto; font-size:12px; color:#666; font-family:"微软雅黑", Simsun;}
li{list-style:none;}
img{border:none;}
a, a:visited{text-decoration:none;}


.wrap{ width:auto; max-width:320px; margin:0 auto;}
.center{ width:103%; margin:0 auto;}

/***************分页******************/
.fenye{ float:left; margin-top:10px;font-size:12px;}
.fenye ul{ float:left; margin-left:32px;font-size:12px; }
.fenye ul li{ float:left; margin-left:5px;padding: 4px 6px; border:1px solid #ccc;  font-weight:bold; cursor:pointer; color:#999;font-size:12px;}
.fenye ul li a{ color:#999;font-size: 12px}
.fenye ul li.xifenye{ width:38px; text-align:center; float:left; position:relative;cursor: pointer;font-size:12px;}
.fenye ul li .xab{ float:left; position:absolute; width:39px; border:1px solid #ccc; height:123px; overflow-y: auto;overflow-x: hidden;top:-125px; background-color: #fff; display:inline;left:-1px; width:50px;}
.fenye ul li .xab ul{ margin-left:0; padding-bottom:0;font-size:12px;}
.fenye ul li .xab ul li{ border:0; padding:4px 0px; color:#999; width:34px; margin-left:0px; text-align:center;font-size:12px;}



</style>


</head>

<body>

<!--下一页--->
<div class="wrap" >
    <div class="fenye">
    	<ul>
        	<li id="first">首页</li>
            <li id="top" onclick="topclick()">上一页</li>
            <li class="xifenye" id="xifenye">
            	<a id="xiye">
            	<c:choose >
            		<c:when test="${ fenye.page_num lt fenye.page_total}">${fenye.page_num}</c:when>
            		<c:otherwise>${fenye.page_total}</c:otherwise>
            	</c:choose>
            		
            	</a>/<a id="mo">${fenye.page_total}</a>
                <div class="xab" id="xab" style="display:none;">
                	<ul id="uljia">
                    	
                    </ul>
                </div>
            </li>
            <li id="down" onclick="downclick()">下一页</a></li>
            <li id="last">末页</li>
        </ul>
    </div>
</div>

<script src="${path }/js/fenye.js" type="text/javascript"></script>

</body>
</html>