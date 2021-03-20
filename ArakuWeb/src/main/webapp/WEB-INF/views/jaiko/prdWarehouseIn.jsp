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
	<button id="btn_sel_partner" type="button" class="btn btn-primary" data-toggle="modal" data-target="#partnerModal" style="width: 150px; margin-bottom: 10px;">取引先情報設定</button>
	<table class="table table-bordered">
		<thead>
		  <tr>
		    <th>取引先No.</th>
		    <th>取引先名</th>
		  </tr>
		</thead>
		<tbody>
		  <tr>
		    <td id="partner_id"></td>
		    <td id="partner_nm"></td>
		  </tr>
		</tbody>
	</table>
</div>
<!-- 거래처정보설정 E -->
</div>
<div class="container-fluid col-sm-6">
<!-- 바코드 읽기 S -->
<div class="well container-fluid">
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
<!-- 바코드 읽기 E -->
</div>
</div>


<!-- table S -->
<div class="well container-fluid">
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
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
</div>
<script src="<%=svrInfo %>/resources/jaiko/prdWarehouseIn.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#prdWareIn").addClass("active");
	$("#area_feedback").css("display", "none");
	$("#jan_cd").focus();
});
</script>
</body>
</html>