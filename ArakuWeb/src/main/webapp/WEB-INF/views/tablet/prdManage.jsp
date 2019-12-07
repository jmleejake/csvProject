<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script src="./../resources/bootstrap-notify/bootstrap-notify.min.js"></script>
<script>
$(document).ready(function() {
	$("#prdM").addClass("active");
	$("#bgc_no").focus();
});
</script>
</head>
<body>
<jsp:include page="../tabletTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<div class="col-sm-4" style="margin-top: 10px;">
	<label id="prdMngName"></label>
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<label>お荷物伝票番号</label>
	<input type="text" id="bgc_no" class="form-control" style="width: 200px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
</div>
<div class="well container-fluid">
<!-- 
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
-->

<div id="prdMGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/tablet/prdManage.js"></script>
</body>
</html>