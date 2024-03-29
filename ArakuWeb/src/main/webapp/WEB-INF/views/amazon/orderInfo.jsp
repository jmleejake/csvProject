<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../amazonTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showAList" id="orderForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 250px;" id="register_date">
	</div>
	<div class="col-sm-4">
	<label>名前</label>
	<input type="text" name="recipient_name" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4">
	<label>注文ID</label>
	<input type="text" name="order_id" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<label>商品名</label>
	<input type="text" name="product_name" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/amazon/orderInfo.js"></script>
<form action="resultView" id="order_frm" method="post">
<input type="hidden" id="hid_data" name="list" >
</form>
<form action="errListDown" id="edown_frm" method="post"></form>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
	errMsg("${errSize}");
});
</script>
</html>