<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
	
	$("#promise_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
	errMsg("${errSize}");
});
</script>

</head>
<body>
<jsp:include page="../amazonTop.jsp"></jsp:include>
<div class="container-fluid">
<!-- Search? -->
<div class="well container-fluid">
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/amazon/orderInfo.js"></script>
<form action="resultView" id="order_frm" method="post">
<input type="hidden" id="hid_data" name="list" >
</form>
<form action="errListDown" id="edown_frm" method="post"></form>
</body>
</html>