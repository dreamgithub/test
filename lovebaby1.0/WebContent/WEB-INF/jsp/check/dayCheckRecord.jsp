<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/context/mytags.jsp"%>
    <title>${date}考勤记录</title>
<!-- 引用上传图片控制层插件样式 -->
<script src="${path }/desktop/js/jquery.min.js"></script>
    <!-- 表格资源引入 -->
    <link rel="stylesheet" href="${path }/desktop/css/table/style.css" />
	<!-- jQuery -->
	<script src="${path }/desktop/js/table/jquery.dataTables.min.js"></script>
	<script src="${path }/desktop/js/table/colResizable-1.3.js"></script>
	<script src="${path }/desktop/js/table/jquery-ui-1.8.21.min.js"></script>
	<script src="${path }/desktop/js/table/jquery.uniform.js"></script>
	<script src="${path }/desktop/js/table/kanrisha.js"></script>
		<div class="g_12" style="width:1200px;margin-top: 60px;margin-left: 150px">
					<div class="widget_contents noPadding twCheckbox">
						<table class="tables datatable" style="word-break:break-all;word-wrap:break-word">
							<thead>
								<tr>
									<th style="width: 60px">编号</th>
									<th style="width: 120px">头像</th>
									<th style="width: 120px">宝宝名称</th>
									<th style="width: 120px">宝宝编号</th>
									<th style="width: 100px">宝宝性别</th>
									<th style="width: 100px">考勤状态</th>
									<th style="width: 120px">考勤日期</th>
									<th style="width: 100px">操作</th>	
								</tr>
							</thead>
							<tbody>						
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td><img alt="" src="${ data.ftpimage}" width="70px" height="50px"></td>
										<td>${ data.babyName}</td>
										<td>${ data.num}</td>
										<td>${ data.sex}</td>
										<c:if test="${ data.checkstate=='1'}"><td>实到</td></c:if><c:if test="${ data.checkstate=='0'}"><td style="color:red ">未到</td></c:if>
										<td>${date}</td>
										<td>
											<input type="button" value="查看详情" class="content" date="${checkdate}" dataid="${ data.id}" classid="${ data.classid}">
										</td>
									</tr>
								</c:forEach>
							</tbody>					
						</table>
				</div>
			</div>
<script type="text/javascript">
		$(".content")
				.click(
						function() {
							var checkdate="${checkdate}";
							var bid=$(this).attr("dataid");
							var id=$(this).attr("classid");
							window.open("${path }/webCheckBaby/getBabyMonCheckRecord.jhtml?bid="+bid+"&date="+checkdate+"&id="+id, '${ data.babyName}月考勤记录', 'height=600, width=800, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
						});
		
	</script>
