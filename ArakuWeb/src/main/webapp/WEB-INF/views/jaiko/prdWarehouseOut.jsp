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
<div class="col-sm-12">

<div class="container-fluid col-sm-6">
<!-- 거래처정보설정 S -->
<div class="well container-fluid">
	<div class="col-sm-4">
	<button id="btn_sel_partner" type="button" class="btn btn-primary" data-toggle="modal" data-target="#partnerModal" style="width: 150px; margin-bottom: 10px;">取引先情報設定</button>
	<table class="table table-bordered">
		<thead>
		  <tr>
<!-- 		    <th>取引先No.</th> -->
		    <th>取引先名</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
<!-- 		    <td id="partner_id"></td> -->
		    <td id="partner_nm"></td>
		  </tr>
		</tbody>
	</table>
	</div>
	<div class="col-sm-8">
	<div class="col-sm-9">
	<label>出庫日</label>
	<div class="col-sm-12">
	<div class="col-sm-6">
	<!-- datepicker -->
	<input type="text" id="delivery_dt" class="form-control" style="width: 120px;">
	</div>
	<div class="col-sm-6">
	<button id="btn_search" type="button" class="btn btn-default" style="width: 80px; margin-bottom: 10px;">検索</button>
	</div>
	</div>
	</div>
	<div class="col-sm-3">
	<div class="btn-group-vertical">
	<button id="btn_delivery" type="button" class="btn btn-default" style="width: 80px; margin-bottom: 10px;">納品書</button>
	<button id="btn_bill" type="button" class="btn btn-default" style="width: 80px; margin-bottom: 10px;">請求書</button>
	<button id="btn_receipt" type="button" class="btn btn-default" style="width: 80px; margin-bottom: 10px;">領収書</button>
	</div>
	</div>
	</div>
</div>
<!-- 거래처정보설정 E -->
</div>

<div class="container-fluid col-sm-6">
<!-- 바코드 읽기 S -->
<div class="well container-fluid">
	<div class="col-sm-6">
	<table class="table table-bordered">
		<thead>
		  <tr>
		    <th>JANコード読み込み</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
		    <td>
		    <input type="text" id="jan_cd" class="form-control">
		    </td>
		  </tr>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
	<table class="table">
		<thead>
		  <tr>
		    <th>
		    <button id="btn_pay" type="button" class="btn btn-default" style="width: 100px;">現金支払い</button>
		    </th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
		    <td>
		    <input type="text" id="pay_price" class="form-control" placeholder="支払い金額表示">
		    </td>
		  </tr>
		</tbody>
	</table>
	</div>
</div>
<!-- 바코드 읽기 E -->
</div>

</div>


<!-- table S -->
<div class="well container-fluid">
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
<div id="prdWareOutGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
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
</div>
<script src="<%=svrInfo %>/resources/jaiko/prdWarehouseOut.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#prdWareOut").addClass("active");
	$("#area_feedback").css("display", "none");
	$("#jan_cd").focus();

	$("#delivery_dt").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>
</body>
</html>