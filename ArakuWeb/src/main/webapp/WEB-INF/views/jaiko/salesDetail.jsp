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
<jsp:include page="../jaikoTop.jsp"></jsp:include>
<div class="container-fluid">
	<div class="well container-fluid">
		<table class="table table-hover">
			<tbody>
				<tr>
					<td>
					<div class="input-group">
						<span class="input-group-addon">売上日</span>
						<input type="text" class="form-control" value="${dt }" disabled>
				    </div>
					</td>
					<td>
					<div class="input-group">
						<span class="input-group-addon">得意先</span>
						<input type="text" class="form-control" value="${partner_nm }" disabled>
				    </div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="well container-fluid">
		<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
		<!-- 
		<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
		 -->
		<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
		<div id="salesGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
	</div>
	<div class="well container-fluid">
		<table class="table table-hover">
			<tbody>
				<tr>
					<td colspan="2">
					<div class="input-group">
						<span class="input-group-addon">社内メモ</span>
						<input id="memo" type="text" class="form-control" value="${detail.memo }">
				    </div>
					</td>
					<td></td>
					<td>
					<div class="input-group">
						<span class="input-group-addon">小計(税抜)</span>
						<input id="subTot" type="text" class="form-control" disabled>
				    </div>
					</td>
					<td>
					<div class="input-group">
						<span class="input-group-addon">消費税</span>
						<input id="cnspTax" type="text" class="form-control" disabled>
				    </div>
					</td>
				</tr>
				<tr>
					<td>
					<div class="input-group">
						<span class="input-group-addon">請求日付</span>
						<input id="dlvDt" type="text" class="form-control" value="${detail.billDt }" disabled>
				    </div>
					</td>
					<td>
					<div class="input-group">
						<span class="input-group-addon">請求書番号</span>
						<input id="dlvNo" type="text" class="form-control" value="${detail.dlvNo }" disabled>
				    </div>
					</td>
					<td></td>
					<td colspan="2">
					<div class="input-group">
						<span class="input-group-addon">総合計</span>
						<input id="midTot" type="text" class="form-control" disabled>
				    </div>
					</td>
				</tr>
				<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td style="text-align:right;">
				<button id="btn_save" type="button" class="btn btn-info" style="width: 120px; margin-bottom: 10px;">保存</button>
				</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<!-- 상품선택모달 S -->
<div class="modal fade" id="srchPrd" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
			<div class="well container-fluid">
			<div class="row">
			<form id="frm_srch">
			<input type="hidden" name="search_type" value="srch">
			<input type="text" name="prd_nm" class="form-control" id="prd_nm" placeholder="商品名">
			</form>
			<button id="btn_srch" type="button" class="btn btn-default" style="width: 100px; margin-bottom: 10px;">検索</button>
			</div>
			<div id="prdInfoGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
			</div>
			<div class="modal-footer">
				<button id="btn_sel" type="button" class="btn btn-primary" style="width: 100px;">選択</button>
				<button type="button" class="btn btn-default" style="width: 100px;" data-dismiss="modal">閉じる</button>
			</div>
			</div>
		</div>
	</div>
</div>
<!-- 상품선택모달 E -->

<script src="<%=svrInfo %>/resources/jaiko/salesDetail.js"></script>
</body>
<script>
$(document).ready(function() {
	showDetail('${partner_id}', '${partner_nm}', '${dt}');
	$("#sales").addClass("active");
});
</script>
</html>