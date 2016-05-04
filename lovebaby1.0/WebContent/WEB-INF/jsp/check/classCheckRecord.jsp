<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/context/mytags.jsp"%>
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
		<div class="g_12">
					<div class="widget_contents noPadding twCheckbox">
						<table class="tables datatable" style="word-break:break-all;word-wrap:break-word">
							<thead>
								<tr>
									<th style="width: 40px">编号</th>
									<th style="width: 150px">图片</th>
									<th style="width: 100px">实到人数</th>
									<th style="width: 100px">未到人数</th>
									<th style="width: 100px">总人数</th>
									<th style="width: 120px">考勤老师</th>
									<th style="width: 120px">考勤日期</th>
									<th style="width: 100px">操作</th>	
								</tr>
							</thead>
							<tbody>						
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td><img alt="" src="${ data.ftpimage}" width="90px" height="50px"></td>
										<td>${ data.comenum}</td>
										<td>${ data.absentnum}</td>
										<td>${ data.sum}</td>
										<td>${ data.teacher}</td>
										<td>${ data.checkdate}</td>
										<td>
											<input type="button" value="查看详情" class="content" date="${ data.checkdate}" dataid="${ data.id}">
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
							var checkdate=$(this).attr("date");
							var id=$(this).attr("dataid");
							window.open("${path }/webCheckBaby/getDayCheckRecordMx.jhtml?id="+id+"&date="+checkdate, '日考勤记录', 'height=600, width=1000, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
						});
		
	</script>
