<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../jaikoTop.jsp"></jsp:include>
<div class="container-fluid">
	<div class="well container-fluid">
		<form id="frm">
		<input type="hidden" name="gbn" value="TOT" />
		<input type="hidden" name="partner_id" />
		<input type="hidden" name="from_dt" />
		<input type="hidden" name="to_dt" />
		<table class="table table-hover">
			<tbody>
				<tr>
					<td>
					<div class="input-group">
						<span class="input-group-addon">売上日</span>
						<input type="text" class="form-control" id="startDate"/>
						<span class="input-group-addon">~</span>
						<input type="text" class="form-control" id="endDate">
				    </div>
					</td>
					<td>
					<div class="dropdown">
						<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
						取引先選択<span id="selectedDealer"></span>  <span class="caret"></span></button>
						<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
							<c:forEach items="${partners }" var="partner">
							<li role="presentation"><a role="menuitem" href="javascript:showOrder('${partner.dealer_id }', '${partner.dealer_nm }')">${partner.dealer_nm }</a></li>
							</c:forEach>
						</ul>
					</div>
					</td>
					<td>
					<button id="btn_srch" type="button" class="btn btn-default btn-lg">
					<span class="glyphicon glyphicon-search"></span>&nbsp;検索
					</button>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	<div class="well container-fluid">
		<div id="salesGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
	</div>
</div>
<script src="<%=svrInfo %>/resources/jaiko/salesMain.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#sales").addClass("active");
});
</script>
</html>