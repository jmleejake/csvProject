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
<jsp:include page="../q10Top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showQList" id="aFileDownForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 250px;" id="register_date">
	</div>
	<div class="col-sm-4">
	<label>注文番号</label>
	<input type="text" name="order_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4">
	<label>送り状番号</label>
	<input type="text" name="invoice_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_adown" type="button" class="btn btn-default" style="width: 180px; margin-bottom: 10px;">Q10更新ファイル</button>
	</div>
</div>

<div id="qFileDownGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/q10/qFileDown.js"></script>

<form id="frm_aFileDown" action="qFileDown" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
</form>
</body>
<script>
$(document).ready(function() {
	$("#qFile").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>
</html>