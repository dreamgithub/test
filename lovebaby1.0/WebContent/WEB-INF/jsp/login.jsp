<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>后台登录</title>

<link rel="stylesheet" href="${path }/css/font-awesome.min.css"/>
<link rel="stylesheet" href="${path }/css/loginMy.css"/>

<style>
html,body{width:100%;}
</style>

</head>
<body>

<div class="main">

	<div class="center">
		<div style="margin-left:200px"><span >${msg }</span></div>
		<form action="${path }/j_spring_security_check" id="formOne" method="post" onsubmit="return submitB()" >			
			<i class="fa fa-user Cone">  | </i>
			<input type="text" name="j_username" id="user" class="text" placeholder="用户名" onblur="checkUser()"/>
			<span id="user_pass"></span>
			<br/>
			<i class="fa fa-key Cone">  | </i>
			<input type="password" name="j_password" id="pwd" class="text" placeholder="密码" onblur="checkUser1()"/>
			<span id="pwd_pass"></span>
			<br/>
			
			<input type="submit" value="登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录" id="submit" class="submit" name="submit" />
			<br/>
		</form>
	</div>
	
</div>


<script type="text/javascript" src="${path }/js/loginMy.js"></script>
<div style="text-align:center;">
</div>
</body>
</html>