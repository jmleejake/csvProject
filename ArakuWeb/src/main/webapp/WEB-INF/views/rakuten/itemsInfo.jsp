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
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="getItems" id="itemsForm">
	<div class="col-sm-4">
	<label>登録日付</label>
	<div class="row">
	<!-- datepicker -->
	<div class="col-sm-6">
	<input type="text" name="start_date" class="form-control" style="width: 200px;" id="start_date" placeholder="From">
	</div>
	<div class="col-sm-6">
	<input type="text" name="end_date" class="form-control" style="width: 200px;" id="end_date" placeholder="To">
	</div>
	</div>
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 25px;">検索</button>
	</div>
	<div class="col-sm-4">
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_item_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">item</button>
	<button id="btn_cat_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">item-cat</button>
	<button id="btn_sel_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">select</button>
	</div>
	</div>
<div id="itemsGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/rakuten/itemsInfo.js"></script>
<form id="frm_idown" action="itemDown" method="post">
<input id="idown_std" type="hidden" name="start_date">
<input id="idown_ed" type="hidden" name="end_date">
</form>
<form id="frm_catdown" action="catDown" method="post">
<input id="catdown_std" type="hidden" name="start_date">
<input id="catdown_ed" type="hidden" name="end_date">
</form>
<form id="frm_seldown" action="selDown" method="post">
<input id="seldown_std" type="hidden" name="start_date">
<input id="seldown_ed" type="hidden" name="end_date">
</form>
</body>
<script>
$(document).ready(function() {
	$("#items").addClass("active");
	
	$("#start_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
	$("#end_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
});
</script>
</html>