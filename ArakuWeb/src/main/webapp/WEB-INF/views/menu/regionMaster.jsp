<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<%=svrInfo %>/resources/menu/regionMaster.css">
</head>
<body>
<c:if test="${'R' == type }">
<jsp:include page="../top.jsp"></jsp:include>
</c:if>
<c:if test="${'A' == type }">
<jsp:include page="../amazonTop.jsp"></jsp:include>
</c:if>
<c:if test="${'Q' == type }">
<jsp:include page="../q10Top.jsp"></jsp:include>
</c:if>
<c:if test="${'Y' == type }">
<jsp:include page="../yahooTop.jsp"></jsp:include>
</c:if>
<c:if test="${'TA' == type }">
<jsp:include page="../tabletTop.jsp"></jsp:include>
</c:if>
<div class="container-fluid">
<div class="well container-fluid">
<div class="col-sm-3">
<button id="btn_weekDataDel" class="btn btn-danger">一週以上データ削除</button>
</div>
<div class="col-sm-6">
<table>
	<tbody id="t_result"></tbody>
</table>
</div>
</div>
</div>
<!-- S상품관리마스터 -->
<div class="container-fluid">
<div class="well container-fluid">
<h3>商品名管理マスタ</h3>

<div class="well container-fluid">
	<form id="prdFrm" action="showPrdMaster">
	<div class="col-sm-4">
	<label>商品管理番号</label>
	<input type="text" name="prd_cd" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ prdSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-4">
	<label>商品名</label>
	<input type="text" name="prd_nm" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ prdSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-4" style="margin-top: 7px;">
	<button id="btn_prd_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_prd_add" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_prd_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_prd_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="prdGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/prdMaster.js"></script>
</div>
<!-- E상품관리마스터 -->

<!-- S거래처,지역마스터 -->
<div class="container-fluid">

<!-- S거래처마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>取引先マスタ</h3>

<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_dealer_add" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_dealer_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_dealer_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="dealerGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/dealerMaster.js"></script>
</div>
<!-- E거래처마스터 -->

<!-- S지역마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>地域区分マスタ</h3>

<div class="well container-fluid">
	<form id="regionFrm" action="showRegionMaster">
	<div class="col-sm-6">
	<label>地域名称</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ search(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
</div>

<div id="regionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/regionMaster.js"></script>
</div>
<!-- E지역마스터 -->

</div>
<!-- E거래처,지역마스터 -->


<!-- S예외데이터, 지역예외데이터 마스터 -->
<div class="container-fluid">

<!-- S예외데이터 마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>第二倉庫マスタ</h3>

<div class="well container-fluid">
	<form id="exceptionFrm" action="showExceptionMaster">
	<div class="col-sm-6">
	<label>例外データ</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ exceptionSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch2" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_add" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit2" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="exceptionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/exceptionMaster.js"></script>
</div>
<!-- E예외데이터 마스터 -->

<!-- S지역예외데이터 마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>例外地域マスタ</h3>

<div class="well container-fluid">
	<form id="exceptionRegionFrm" action="showExceptionRegionMaster">
	<div class="col-sm-6">
	<label>例外地域データ</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ exceptionRegionSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_RegionSrch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_regionAdd" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_regionCommit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_regionDelete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="exceptionRegionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/exceptionRegionMaster.js"></script>
</div>
<!-- E지역예외데이터 마스터 -->

</div>
<!-- E예외데이터, 지역예외데이터 마스터 -->

<!-- S제3창고, 마스터 -->
<div class="container-fluid">

<!-- S제3창고 마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>第三倉庫マスタ</h3>

<div class="well container-fluid">
	<form id="house3Frm" action="getHouse3">
	<div class="col-sm-6">
	<label>例外データ</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ exceptionSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch_house3" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_add_house3" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit_house3" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_delete_house3" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="house3Grid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/house3master.js"></script>
</div>
<!-- E제3창고 마스터 -->

</div>
<!-- E제3창고 -->
<script type="text/javascript">
$(document).ready(function() {
	$("#region").addClass("active");
	showRegionMaster();
	showExceptionMaster();
	showPrdMaster();
	showDealerMaster();
	showExceptionRegionMaster();
	showHouse3Master();
	
	/*
	 * [마스터화면] 일주일 이상의 라쿠텐, 아마존, 큐텐, 야후 데이터 삭제
	 * */
	$("#btn_weekDataDel").on("click", function() {
		var tag = "<tr>";
		$.ajax({
		    url: "delWeekData"
		    , contentType: "application/json; charset=utf-8"
		    , success: function(res) {
		    	res.forEach(function(data) {
		    		tag+="<td style='padding:3px;'>";
		    		tag+=data;
		    		tag+="</td>";
		    	});
		    	tag += "</tr>";
		    	tag += "<tr><td colspan='4' style='text-align:center;'>削除完了</td></tr>";
		    	$("#t_result").append(tag);
		    }
		});
	});
});
</script>
</body>
</html>