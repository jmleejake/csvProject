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
	<div class="col-sm-6" >
	<label>年月選択</label>
	<!-- datepicker -->
	<input type="text" name="reg_dt" class="form-control" style="width: 150px;" id="yymm">
	</div>
	<div class="col-sm-6" >
	</div>
</div>
<div class="well container-fluid">
<div class="row">
</div>
<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>

<!-- Modal S -->
<div class="modal fade" id="orderDetail" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div class="well container-fluid">
			<div class="dropdown">
				<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
				取引先選択<span id="selectedDealer"></span>  <span class="caret"></span></button>
				<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
					<c:forEach items="${partners }" var="partner">
					<li role="presentation"><a role="menuitem" href="javascript:showOrder('${partner.partner_id }', '${partner.partner_nm }')">${partner.partner_nm }</a></li>
					</c:forEach>
				</ul>
			</div>
			</div>
			<div class="well container-fluid">
			<div class="row">
			<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
			<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
			<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
			</div>
			<div id="ordDtlGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
			</div>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->
<script src="<%=svrInfo %>/resources/jaiko/order.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
});
</script>
</html>