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
									<th style="width: 200px">标题</th>
									<th style="width: 120px">发布人</th>
									<th style="width: 80px">状态</th>
									<th style="width: 120px">发布日期</th>
									<th style="width: 80px">浏览次数</th>
									<th style="width: 100px">操作</th>	
								</tr>
							</thead>
							<tbody>						
								<c:forEach items="${result.result }"  var="data"   varStatus="status">				
									<tr>
										<td>${status.index+1}</td>
										<td>${ data.title}</td>
										<td>${ data.realName}<c:if test="${ data.from_type=='1'}">-机构</c:if><c:if test="${ data.from_type=='2'}">-园长</c:if><c:if test="${ data.from_type=='3'}">-园务</c:if><c:if test="${ data.from_type=='4'}">-老师</c:if></td>
										<td><c:if test="${ data.state=='0'}">已读</c:if><c:if test="${ data.state!='0'}">未读</c:if></td>
										<td>${ data.publishDate.substring(0, 10)}</td>
										<td>${ data.times}</td>
										<td>
											<input type="button" value="查看详情" class="content" contentType="${ data.contentType}" dataid="${ data.id}">
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
							var contentType=$(this).attr("contentType");
							var nid=$(this).attr("dataid");
							var id="${member.result.id }";
							window.open("${path }/webnotice/read.jhtml?nid="+nid+"&type="+contentType+"&id="+id, '公告内容', 'height=600, width=800, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
						});
		
	</script>
