<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${path }/css/chinaz.css" rel="stylesheet">
<!-- 引用上传图片控制层插件样式 -->
<link href="${path }/desktop/css/templatemo_style.css" rel="stylesheet"
	type="text/css">
	<link rel="stylesheet" href="${path }/css/cui.css" />
<script src="${path }/desktop/js/jquery.min.js"></script>


</head>
<body>
	<div class="main-container">
		<nav class="main-nav">
			<div id="logo" class="left">校史</div>
			<c:if test="${member.result.type=='2'||member.result.type=='3'}">				
				<ul class="nav right center-text">
						<li id="read" class="active btn">查看校史</li>
						<li id="publish" class="active">发布校史</li>
						<li id="mypublish" class="active">我的发布</li>
				</ul>
			</c:if>
			<c:if test="${member.result.type=='1'&&member.schools.size()!=0}">			
				<div style="margin-left: 33%;margin-top: 40px;position: absolute;">    
					<div class="nice-select" name="nice-select" style="text-align: center;width: 130px;height: 30px">
						<c:if test="${name==null }">			
						    <input type="text" style="text-align: center;" value="-- ${member.schools.get(0).schoolName} --" readonly >
						</c:if>
						<c:if test="${name!=null }">			
						    <input type="text" style="text-align: center;" value="-- ${name} --" readonly>
						</c:if>
						<ul>
						    <c:forEach items="${member.schools}"  var="data"   begin="0"  varStatus="status">			    
							      <li data-value="${data.id}">${data.schoolName}</li>
						    </c:forEach>
						</ul>
		  			</div>
	  			</div>
			</c:if>
		</nav>
	</div>
	<div class="wrapper">
    <div class="history">
        <div class="start-history">
            <p class="cc_history">校史</p>
            <p class="next_history">PHYLOGENY</p>
            <div class="history_left">
				<c:forEach items="${result.result }"  var="data"   begin="1" end="1"  varStatus="status">				
					 <p class="history_L year2006">
	                    <span class="history_2006_span">${data.year }</span>
	                    <b class="history_2006_b">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text " content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="3" end="3"  varStatus="status">				
	                <p class="history_L yearalmost">
	                    <span class="history_2006_span">${data.year }</span>
	                    <b class="history_2006_b">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="5" end="5"  varStatus="status">				
	                <p class="history_L year2009">
	                    <span class="history_2006_span">${data.year }</span>
	                    <b class="history_2006_b">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				
                <c:forEach items="${result.result }"  var="data"   begin="7" end="7"  varStatus="status">				
	                <p class="history_L yearalmost">
	                    <span class="history_2006_span blue">${data.year }</span>
	                    <b class="history_2006_b blue">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
                
                <c:forEach items="${result.result }"  var="data"   begin="9" end="9"  varStatus="status">				
	                <p class="history_L yearalmost">
	                    <span class="history_2006_span blue">${data.year }</span>
	                    <b class="history_2006_b blue">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text smalltext" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="11" end="11"  varStatus="status">				
	                <p class="history_L year2011">
	                    <span class="history_2006_span blue">${data.year }</span>
	                    <b class="history_2006_b blue">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>

				<c:forEach items="${result.result }"  var="data"   begin="13" end="13"  varStatus="status">				
	                <p class="history_L year2011">
	                    <span class="history_2006_span blue">${data.year }</span>
	                    <b class="history_2006_b blue">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="15" end="15"  varStatus="status">				
	               <p class="history_L year2011">
	                   	 <span class="history_2006_span yellow">${data.year }</span>
	                    <b class="history_2006_b yellow">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
               	 	</p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="17" end="17"  varStatus="status">				
	                <p class="history_L year2013">
	                    <span class="history_2006_span yellow">${data.year }</span>
	                    <b class="history_2006_b yellow">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text smalltxt" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="19" end="19"  varStatus="status">				
	                <p class="history_L yearalmost">
	                    <span class="history_2006_span yellow">${data.year }</span>
	                    <b class="history_2006_b yellow">
	                        <span class="history_l_month">${data.month }<br/>月</span>
	                        <span class="history_l_text full" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
            </div>
            <div class="history-img">
                <img class="history_img" src="${path }/images/history/history.png" alt="">
            </div>
            <div class="history_right">
           		<c:forEach items="${result.result }"  var="data"   begin="0" end="0"  varStatus="status">				
	                <p class="history_R history_r_2005">
	                    <span class="history_2005_span">${data.year }</span>
	                    <b class="history_2005_b">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="2" end="2"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span">${data.year }</span>
	                    <b class="history_2005_b">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }">
	                        <c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if>
	                         </span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="4" end="4"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span">${data.year }</span>
	                    <b class="history_2005_b">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if> </span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="6" end="6"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span">${data.year }</span>
	                    <b class="history_2005_b">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if> </span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="8" end="8"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span blue">${data.year }</span>
	                    <b class="history_2005_b blue_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="10" end="10"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span blue">${data.year }</span>
	                    <b class="history_2005_b blue_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="12" end="12"  varStatus="status">				
	                <p class="history_R year211">
	                    <span class="history_2005_span blue">${data.year }</span>
	                    <b class="history_2005_b blue_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="14" end="14"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span yellow">${data.year }</span>
	                    <b class="history_2005_b yellow_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="16" end="16"  varStatus="status">				
	                <p class="history_R year211">
	                    <span class="history_2005_span yellow">${data.year }</span>
	                    <b class="history_2005_b yellow_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="18" end="18"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span yellow">${data.year }</span>
	                    <b class="history_2005_b yellow_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
	                    </b>
	                </p>
				</c:forEach>
				<c:forEach items="${result.result }"  var="data"   begin="20" end="20"  varStatus="status">				
	                <p class="history_R yearalmostr">
	                    <span class="history_2005_span yellow">${data.year }</span>
	                    <b class="history_2005_b yellow_R">
	                        <span class="history_r_month">${data.month }<br/>月</span>
	                        <span class="history_r_text" content="${data.id }"><c:if test="${data.title!=null}">
		                        ${data.title.substring(0, data.title.length()/2) }<br/>${data.title.substring(data.title.length()/2, data.title.length()) }
	                        </c:if></span>
		                       
	                    </b>
	                </p>
				</c:forEach>
            </div>
          
            <div class="clear"></div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<script src="${path }/js/jquery.slidy.js"></script>
<script>
$(window).scroll(function(){
    var msg = $(".history-img");
    var item = $(".history_L");
    var items = $(".history_R");
    var windowHeight = $(window).height();
    var Scroll = $(document).scrollTop();
    if((msg.offset().top - Scroll -windowHeight)<=0){
        msg.fadeIn(1500);
    }
    for(var i=0;i<item.length;i++){
        if(($(item[i]).offset().top - Scroll - windowHeight)<= -100){
            $(item[i]).animate({marginRight:'0px'},'50','swing');
        }
    }
    for(var i=0;i<items.length;i++){
        if(($(items[i]).offset().top - Scroll - windowHeight)<= -100){
            $(items[i]).animate({marginLeft:'0px'},'50','swing');
        }
    }});
</script>
<script>	
$('[name="nice-select"]').click(function(e){
	$('[name="nice-select"]').find('ul').hide();
	$(this).find('ul').show();
	e.stopPropagation();
});
$('[name="nice-select"] li').hover(function(e){
	$(this).toggleClass('on');
	e.stopPropagation();
});
$('[name="nice-select"] li').click(function(e){
	var val = $(this).text();
	var dataVal = $(this).attr("data-value");
	$(this).parents('[name="nice-select"]').find('input').val(val);
	$('[name="nice-select"] ul').hide();
	e.stopPropagation();
	location = "${path }/webhistories/readhistories.jhtml?id="+dataVal+"&name="+val;
});
$(document).click(function(){
	$('[name="nice-select"] ul').hide();
});
</script>
	<script type="text/javascript">
		$("#publish")
				.click(
						function() {
							location = "${path }/webhistories/histories.jhtml";
						});
		$("#mypublish")
		.click(
				function() {
					location = "${path }/webhistories/getPublishHistory.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
				});
		$("#read")
				.click(
						function() {
							location = "${path }/webhistories/readhistories.jhtml?id=${member.schools.size()==0?null:member.schools.get(0).id}";
						});
		$(".history_l_text")
		.click(
				function() {
					var id=$(this).attr("content");
					window.open('${path }/webhistories/readhistoriescontent.jhtml?id='+id, '校史内容', 'height=400, width=1000, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')   
				});
		$(".history_r_text")
		.click(
				function() {
					var id=$(this).attr("content");
					window.open('${path }/webhistories/readhistoriescontent.jhtml?id='+id, '校史内容', 'height=400, width=1000, top=200, left=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no') 
				});
	</script>

</body>
</html>