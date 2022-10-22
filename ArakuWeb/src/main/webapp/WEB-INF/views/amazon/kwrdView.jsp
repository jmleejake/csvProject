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
	<form action="getKwrdInfo" id="frm">
	<div class="col-sm-4">
	<input type="text" name="kwrd1" class="form-control" placeholder="メーカー名" onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-4">
	<input type="text" name="kwrd2" class="form-control" placeholder="キーワード名" onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px;">列追加</button>
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px;">登録</button>
<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px;">削除</button>
<div id="srchGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/amazon/kwrdView.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#kwrd").addClass("active");
});
</script>
</html>