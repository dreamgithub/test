<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/context/mytags.jsp"%>
    <title>${ result.baby.babyName} ${ checkdate} 考勤记录</title>
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
									<td colspan="4">${ result.baby.babyName} ${ checkdate} 考勤记录</td>
									<td colspan="1">实到:${result.comenum }天  </td>
									<td colspan="1">未到:${result.absentnum }天</td>
									<td colspan="1">总计:${result.daycount }天 </td>
								</tr>
								<tr>
									<th style="width: 60px">编号</th>
									<th style="width: 120px">头像</th>
									<th style="width: 120px">宝宝名称</th>
									<th style="width: 120px">宝宝编号</th>
									<th style="width: 100px">宝宝性别</th>
									<th style="width: 100px">考勤状态</th>
									<th style="width: 120px">考勤日期</th>	
								</tr>
							</thead>
							<tbody>						
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td><img alt="" src="${ result.baby.ftpimage}" width="70px" height="50px"></td>
										<td>${ result.baby.babyName}</td>
										<td>${ result.baby.num}</td>
										<td>${ result.baby.sex}</td>
										<c:if test="${ data.state=='1'}"><td>实到</td></c:if><c:if test="${ data.state=='0'}"><td style="color:red ">未到</td></c:if>
										<td>${data.checkdate}</td>
									</tr>
								</c:forEach>
							</tbody>					
						</table>
				</div>
			</div>