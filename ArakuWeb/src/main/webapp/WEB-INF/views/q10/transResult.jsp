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
<div class="row">
<div class="col-sm-6">
<button id="btn_ya1_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">YAMATO１倉庫</button>
<button id="btn_ya2_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">YAMATO２倉庫</button>
<button id="btn_sa_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">SAGAWA</button>
<button id="btn_cp_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">クリックポスト</button>
<!-- <button id="btn_yu_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">郵便局</button> -->
</div>
</div>

<div id="resultGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/q10/transResult.js"></script>
<form id="frm_down4" action="cpDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
</form>
<form id="frm_down3" action="yuDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
<input type="hidden" id="yu_code" name="company">
</form>
<form id="frm_down2" action="saDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
<input type="hidden" id="sa_code" name="company">
</form>
<form id="frm_down1" action="yaDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
<input type="hidden" id="ya_code" name="company">
<input type="hidden" id="storage_cd" name="storage">
</form>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
	getData();
});
</script>
</html>