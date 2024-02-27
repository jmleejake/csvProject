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
<jsp:include page="../cancelTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<div class="row">
		<div class="col-sm-4" style="margin-top: 10px;">
		<label id="prdCancelName"></label>
		</div>
		<div class="col-sm-4" style="margin-top: 10px;">
		<label>お荷物伝票番号</label>
		<input type="text" id="bgc_no" class="form-control" style="width: 200px;">
		</div>
		<div class="col-sm-4" style="margin-top: 10px;">
	      <button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	    </div>
	</div>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
		<button id="btn_jaikosuhanei" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">在庫反映</button> 
		<!-- <button id="sendGridData" >在庫反映</button>-->
	</div>
</div>
<div id="statusGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/cancel/cancelMng.js"></script>
</body>

<script>
$(document).ready(function() {
	   $("#prdCan").addClass("active");
	   $("#bgc_no").focus();
	   });
</script> 
</html>