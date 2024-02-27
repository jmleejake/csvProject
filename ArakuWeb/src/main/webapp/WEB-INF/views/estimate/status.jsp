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
	<div class="col-sm-6" >
	<div class="dropdown">
		<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
		取引先選択<span id="selectedDealer"></span>  <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
			<c:forEach items="${dealers }" var="dealer">
			<li role="presentation"><a role="menuitem" href="javascript:showOrder('${dealer.dealer_id }', '${dealer.dealer_nm }')">${dealer.dealer_nm }</a></li>
			</c:forEach>
		</ul>
	</div>
	</div>
	<div class="col-sm-6" >
	<button type="button" id="btn_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
	</div>
</div>
<div class="well container-fluid">
	<form id="frm">
	<input type="hidden" name="partner_id">
	<div class="col-sm-4" >
	<input type="text" name="jan_cd" class="form-control" style="width: 250px;" placeholder="JANコード">
	</div>
	<div class="col-sm-4" >
	<input type="text" name="prd_nm" class="form-control" style="width: 250px;" placeholder="商品名">
	</div>
	</form>
	<div class="col-sm-4">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
	</div>
</div>
<div class="well container-fluid">
<div class="row">
<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
<button type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;"
data-toggle='modal' data-target='#create'>作成</button>
</div>
<div id="statusGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<form id="frm_down" action="esimateDown" method="post">
	<input type="hidden" name="partner_id">
</form>
</div>

<!-- Modal S -->
<div class="modal fade" id="create" role="dialog">
	<div class="modal-dialog" style="width:1320px;">
		<div class="modal-content">
			<div class="modal-body">
				<div class="container-fluid">
					<div class="well container-fluid">
						<div class="dropdown">
							<button class="btn btn-primary dropdown-toggle" id="menu2" type="button" data-toggle="dropdown">
							取引先選択<span id="prdDealer"></span>  <span class="caret"></span></button>
							<ul class="dropdown-menu" role="menu" aria-labelledby="menu2">
								<c:forEach items="${dealers }" var="dealer">
								<li role="presentation"><a role="menuitem" href="javascript:showPrd('${dealer.dealer_id }', '${dealer.dealer_nm }')">${dealer.dealer_nm }</a></li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="well container-fluid">
						<div class="col-sm-6" >
						<div class="col-sm-6">
						<input type="text" name="percent" class="form-control" style="width: 100px;" placeholder="掛け率" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');">
						</div>
						<div class="col-sm-6" style="font-size: 15pt; margin-top: 3px;">%</div>
						</div>
						<div class="col-sm-6" >
						<button type="button" id="btn_batch" class="btn btn-info" style="width: 120px;">一括登録</button>
						</div>
					</div>
					<div class="well container-fluid">
						<form id="frm_prd">
						<input type="hidden" name="search_type" value="srch">
						<div class="col-sm-4" >
						<input type="text" name="jan_cd" class="form-control" style="width: 300px;" placeholder="JANコード">
						</div>
						<div class="col-sm-4" >
						<input type="text" name="prd_nm" class="form-control" style="width: 300px;" placeholder="商品名">
						</div>
						</form>
						<div class="col-sm-4">
						<button type="button" id="prd_srch" class="btn btn-default" style="width: 120px;">検索</button>
						</div>
					</div>
					<div class="well container-fluid">
					<div class="row">
					<button id="prd_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
					</div>
					<div id="prdGrid" style="width:auto; height: 400px;" class="ag-theme-balham"></div>
					</div>
				</div>
			</div><!-- body -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">閉じる</button>
			</div>
		</div>
	</div>
</div>
<!-- Modal E -->

<script src="<%=svrInfo %>/resources/estimate/status.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#estimate").addClass("active");
});
</script>
</html>