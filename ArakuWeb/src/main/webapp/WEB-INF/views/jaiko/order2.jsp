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
<div class="well container-fluid">
	<div class="col-sm-6" >
	<label>年月選択</label>
	<!-- datepicker -->
	<input type="text" name="reg_dt" class="form-control" value="${thisMonth }" style="width: 150px;" id="yymm">
	</div>
	<div class="col-sm-6" >
	</div>
</div>
<div class="well container-fluid">
<!-- 
<div class="row">
<button class="btn btn-primary" 
data-toggle='modal' data-target='#orderDetail' onclick="showDetail('','')" style="width: 120px; margin-bottom: 10px;">列追加</button>
</div>
 -->
<table class="table table-bordered">
<thead>
<tr>
	<th>年月日</th>
	<th>新規登録</th>
</tr>
</thead>
<tbody>
<c:forEach var="ymd" items="${calendar }">
<tr>
<td style="width: 110px;">${ymd }</td>
<td style="width: 80px;"><button class="btn btn-default" data-toggle='modal' data-target='#orderDetail' onclick="showDetail('','','${ymd }')">新規登録</button></td>
	<c:set var="bfrVal" value="" />
	<c:forEach var="order" items="${orderData }">
	<c:if test="${ymd eq order.reg_dt }">
	<c:if test="${bfrVal ne order.partner_nm }">
	<td><button class="btn btn-default" data-toggle='modal' data-target='#orderDetail' onclick="showDetail('${order.partner_id}','${order.partner_nm }','${order.reg_dt }')">${order.partner_nm }</button></td>
	</c:if>
	<c:set var="bfrVal" value="${order.partner_nm }" />
	
	</c:if>
	</c:forEach>
</tr>
</c:forEach>
</tbody>
</table>
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
					<li role="presentation"><a role="menuitem" href="javascript:showOrder('${partner.dealer_id }', '${partner.dealer_nm }')">${partner.dealer_nm }</a></li>
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

<!-- Modal S -->
<div class="modal fade" id="srchPrd" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div class="well container-fluid">
			<div class="row">
			<form id="frm_srch">
			<input type="hidden" name="search_type" value="srch">
			<input type="text" name="prd_nm" class="form-control" id="prd_nm" placeholder="商品名">
			</form>
			<button id="btn_srch" type="button" class="btn btn-default" style="width: 100px; margin-bottom: 10px;">検索</button>
			</div>
			<div id="prdInfoGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
			</div>
			<div class="modal-footer">
				<button id="btn_sel" type="button" class="btn btn-primary" style="width: 100px;">選択</button>
				<button type="button" class="btn btn-default" style="width: 100px;" data-dismiss="modal">閉じる</button>
			</div>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->
<script src="<%=svrInfo %>/resources/jaiko/order2.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
});
</script>
</html>