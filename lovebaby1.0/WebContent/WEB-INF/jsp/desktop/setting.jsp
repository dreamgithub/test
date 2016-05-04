<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统设置</title>
<link href="${path }/desktop/css/move.css" type="text/css" rel="stylesheet" />

</head>
<body>
<div style="float:right;width: 10%;position: absolute;margin-top: 50px"">
<ul class="list">
	<li>
		<div class="border"></div>
		<span class="icon">N</span>
		<div class="text">
			<h2><div><a href="${path }/desktop/theme.jhtml?type=1&page_size=8" target="content">主题设置</a></div></h2>
		</div>
	</li>
	<li>
		<div class="border"></div>
		<span class="icon">Z</span>
		<div class="text">
			<h2><a href="${path }/desktop/deskTopTools.jhtml?id=${member.result.id}" target="content">添加桌面工具</a></h2>
		</div>
	</li>
</ul>

</div>

<div id="page-content" style="float:right;width: 100%;height: 900px;margin-top: 25px">
		<iframe name="content" src="${path }/desktop/theme.jhtml?type=1&page_size=8" width="100%" height="100%" scrolling="auto" style="margin:0px;border-width:0px;">
		</iframe>
</div>
</body>
</html>