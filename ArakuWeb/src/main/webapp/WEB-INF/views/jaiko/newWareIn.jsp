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
<div class="container-fluid">
<!-- 거래처정보설정 S -->
<div class="well container-fluid">
	<div class="col-sm-6">
	<button id="btn_sel_partner" type="button" class="btn btn-primary" data-toggle="modal" data-target="#partnerModal" style="width: 150px; margin-bottom: 10px;">取引先設定</button>
	<table class="table table-bordered">
		<thead>
		  <tr>
		    <th>取引先名</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
		    <td id="partner_nm"></td>
		  </tr>
		</tbody>
	</table>
	<input type="hidden" id="partner_id">
	</div>
	<div class="col-sm-6">
	<button id="btn_sel_partner" type="button" class="btn btn-primary" data-toggle="modal" data-target="#tantouModal" style="width: 150px; margin-bottom: 10px;">担当者設定</button>
	<table class="table table-bordered">
		<thead>
		  <tr>
		    <th>担当者名</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
		    <td id="tantou_nm"></td>
		  </tr>
		</tbody>
	</table>
	<input type="hidden" id="tantou_id">
	</div>
</div>
<!-- 거래처정보설정 E -->
</div>

<!-- table S -->
<div class="well container-fluid">
<div class="container-fluid">
<form id="frm_srch">
<div class="col-sm-4"><input type="text" class="form-control" name="prd_nm" placeholder="商品名" onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }"></div>
<div class="col-sm-4"><input type="text" class="form-control" name="jan_cd" placeholder="JANコード" onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }"></div>
<input type="hidden" name="search_type" value="wareIn">
</form>
<div class="col-sm-4"><button id="btn_srch" type="button" class="btn btn-primary" style="width: 120px;"><span class="glyphicon glyphicon-search"></span>検索</button></div>
</div>
<div class="container-fluid">
<div class="col-sm-4">
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px; margin-top: 10px;"">登録</button>
</div>
<div class="col-sm-4"></div>
<div class="col-sm-4"></div>
</div>
<div id="prdWareInGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<!-- table E -->

<!-- Modal S -->
<div class="modal fade" id="partnerModal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div class="well container-fluid">
			<div class="col-sm-9">
			<input type="text" class="form-control" id="keyword" placeholder="取引先No. or 取引先名">
			</div>
			<div class="col-sm-3">
			<button id="btn_part_search" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">検索</button>
			</div>
			</div>
			<div class="well container-fluid">
			<button id="btn_part_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
			<button id="btn_part_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
			<button id="btn_part_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
			<div id="partnerGrid" style="width:auto; height: 200px;" class="ag-theme-balham"></div>
			<div id="area_feedback" class="input-group has-error has-feedback" style="display: none;">
				<input type="text" class="form-control" id="ret_msg">
			</div>
			</div>
			<div class="modal-footer">
				<button id="btn_part_select" type="button" class="btn btn-default">設定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
			</div>
			<script src="<%=svrInfo %>/resources/jaiko/partnerInfo.js"></script>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->

<!-- Modal S -->
<div class="modal fade" id="partnerDetail" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div>
			<button type="button" onclick="javascript:detailCommit();" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">更新</button>
			</div>
			<input type="hidden" id="part_seq_id">
			<table class="table table-bordered">
			<tbody>
			<tr>
			<td>取引先No.</td>
			<td><input type="text" class="form-control" id="part_id" disabled="disabled"></td>
			</tr>
			<tr>
			<td>取引先名</td>
			<td><input type="text" class="form-control" id="part_nm" disabled="disabled"></td>
			</tr>
			<tr>
			<td>郵便番号</td>
			<td><input type="text" class="form-control" id="part_post"></td>
			</tr>
			<tr>
			<td>住所</td>
			<td><input type="text" class="form-control" id="part_add"></td>
			</tr>
			<tr>
			<td>電話番号</td>
			<td><input type="text" class="form-control" id="part_tel"></td>
			</tr>
			<tr>
			<td>FAX番号</td>
			<td><input type="text" class="form-control" id="part_fax"></td>
			</tr>
			<tr>
			<td>担当者名</td>
			<td><input type="text" class="form-control" id="part_tan"></td>
			</tr>
			</tbody>
			</table>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
			</div>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->

<!-- Modal S -->
<div class="modal fade" id="tantouModal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div class="well container-fluid">
			<div class="col-sm-9">
			<form id="frm_tantou">
			<input type="text" class="form-control" name="tantou_nm" placeholder="担当者名">
			</form>
			</div>
			<div class="col-sm-3">
			<button id="btn_tantou_search" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">検索</button>
			</div>
			</div>
			<div class="well container-fluid">
			<button id="btn_tantou_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
			<button id="btn_tantou_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
			<button id="btn_tantou_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
			<div id="tantouGrid" style="width:auto; height: 200px;" class="ag-theme-balham"></div>
			<div id="area_feedback2" class="input-group has-error has-feedback" style="display: none;">
				<input type="text" class="form-control" id="ret_msg2">
			</div>
			</div>
			<div class="modal-footer">
				<button id="btn_tantou_select" type="button" class="btn btn-default">設定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
			</div>
			<script src="<%=svrInfo %>/resources/jaiko/tantouInfo.js"></script>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->
</div>
<script src="<%=svrInfo %>/resources/jaiko/newWareIn.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#prdWareIn").addClass("active");
	$("#area_feedback").css("display", "none");
	$("#area_feedback2").css("display", "none");
	$("#jan_cd").focus();
});
</script>
</body>
</html>