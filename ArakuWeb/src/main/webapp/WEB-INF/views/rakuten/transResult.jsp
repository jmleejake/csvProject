<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
	
	$("#sa_chk").val("");
	$("#ya_chk").val("");
	getData();
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
<div class="row">
<div class="col-sm-6">
<button id="btn_ya_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">YAMATO</button>
<button id="btn_sa_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">SAGAWA</button>
<button id="btn_glb_sa_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">グローバル<br/>SAGAWA</button>
<button id="btn_cp_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">クリックポスト</button>
<!-- <button id="btn_yu_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">郵便局</button> -->
</div>
<div class="col-sm-6">
<div class="checkbox">
	<label><input type="checkbox" id="tomoEx">あす楽例外</label>
</div>
</div>
</div>

<div id="resultGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/rakuten/transResult.js"></script>
<form id="frm_down5" action="globalSaDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
</form>
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
<input type="hidden" id="sa_chk" name="isChecked">
</form>
<form id="frm_down1" action="yaDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
<input type="hidden" id="ya_code" name="company">
<input type="hidden" id="ya_chk" name="isChecked">
</form>
</body>
</html>