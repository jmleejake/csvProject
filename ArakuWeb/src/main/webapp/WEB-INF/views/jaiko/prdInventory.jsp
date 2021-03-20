<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<jsp:include page="../jaikoTop.jsp"></jsp:include>
<div class="container-fluid">
<!-- search S -->
<div class="well container-fluid">
	<form id="frm_srch">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-3">
	<input type="text" name="jan_cd" class="form-control" id="jan_cd" placeholder="JANコード">
	</div>
	<div class="col-sm-3">
	<input type="text" name="prd_cd" class="form-control" id="prd_cd" placeholder="商品コード">
	</div>
	<div class="col-sm-4">
	<input type="text" name="prd_nm" class="form-control" id="prd_nm" placeholder="商品名">
	</div>
	<div class="col-sm-2">
	<button id="btn_search" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">検索</button>
	</div>
	</form>
</div>
<!-- search E -->

<!-- table S -->
<div class="well container-fluid">
<div class="row">
<div class="col-sm-9">
<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
</div>
<div class="col-sm-3">
<button id="btn_down" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">棚卸表DL</button>
<form id="frm_down" action="/jaiko/prdInven/csvDown" method="post"></form>
</div>
</div>

<div id="prdInvenGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<!-- table E -->
</div>
<script src="<%=svrInfo %>/resources/jaiko/prdInventory.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#prdInven").addClass("active");
});
</script>
</body>
</html>