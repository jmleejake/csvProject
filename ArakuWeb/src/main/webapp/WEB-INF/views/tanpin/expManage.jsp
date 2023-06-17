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
<jsp:include page="../tanpinTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	
	<div class="row">
		<form id="frm">
		<div class="col-sm-4" >
		<input type="text" name="prd_nm" class="form-control" style="width: 250px;" placeholder="商品名"
		onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
		<input type="hidden" name="search_type" value="srch"> 
		</div>
		<div class="col-sm-4">
		<input type="text" name="jan_cd" class="form-control" style="width: 250px;" placeholder="商品コード(JAN)"
		onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
		</div>
		</form>
		<div class="col-sm-4">
		<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
		</div>
	</div>
</div>
<div class="well container-fluid">
<div class="row">
	<button id="btn_create" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">登録</button>
	<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
</div>
<div id="prdMGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/tanpin/expManage.js"></script>

<form id="frm_down" action="down" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
</form>
</body>
<script>
$(document).ready(function() {
	$("#expire").addClass("active");
});
</script>
</html>