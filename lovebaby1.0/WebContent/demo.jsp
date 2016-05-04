<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-US">

<head>
    <title>ueditor demo</title>
</head>

<body>
	<form action="${path }/webhistories/addhistories.jhtml">
	
	    <!-- 加载编辑器的容器 -->
	    <script id="container" name="content" type="text/plain">
      		  这里写你的初始化内容
    	</script>
    	<input type="submit" value="提交">
	</form>
    
    
    
    <!-- 配置文件 -->
    <script type="text/javascript" src="${path }/UEditor/ueditor.config.js"></script>
     <script type="text/javascript" src="${path }/UEditor/ueditor.parse.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${path }/UEditor/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('container', {
        	toolbars: [[
        	            'fullscreen', 'source', '|', 'undo', 'redo', '|',
        	            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
        	            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
        	            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
        	            'directionalityltr', 'directionalityrtl', 'indent', '|',
        	            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
        	            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
        	            'simpleupload', 'insertimage', 'emotion', 'scrawl',   'map', 'insertframe', 'insertcode',  'template', 'background', '|',
        	            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
        	            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
        	            'print', 'preview', 'searchreplace', 'help'
        	        ]],
            autoHeightEnabled: true,
            autoFloatEnabled: true
        });
        uParse('.content', {
            rootPath: URL
        })
    </script>
</body>

</html>