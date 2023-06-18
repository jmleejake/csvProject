<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<c:if test="${'R' == type }">
<jsp:include page="../top.jsp"></jsp:include>
</c:if>
<c:if test="${'A' == type }">
<jsp:include page="../amazonTop.jsp"></jsp:include>
</c:if>
<div class="container-fluid">
<div class="well container-fluid">
<h3>タブレット注文画面</h3>

<div class="well container-fluid">
	<div class="col-sm-4">
	<!-- oninput="javascript:goUrl();" onpaste="javascript:isValid=true;" -->
<!-- 	<input type="text" id="baggage_id" class="form-control" style="width: 300px;" placeholder="問い合わせ番号"> -->
	<input type="text" id="baggage_id" class="form-control" style="width: 300px;" placeholder="ＪＡＮコード"
	onkeyup="javascript:if(window.event.keyCode == 13) {srch();}">
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
	</div>
</div>

<div class="well container-fluid">
<table class="table table-bordered">
<thead>
<tr>
<th>商品名</th>
<th>現在商品数</th>
</tr>
</thead>
<tbody id="resBody"></tbody>
</table>
</div>
</div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	$("#tabChumon").addClass("active");
	$('#baggage_id').focus();
	
	$("#btn_srch").on("click", function() {
		goUrl();
		// srch();
	});
	
});

function srch() {
	$.ajax({
		url: "/jaiko/prdInven/getPrdInven"
		, type:"get"
		, dataType: "json"
		, contentType: 'application/json'
		, data:{search_type: 'srch', jan_cd: $('#baggage_id').val()}
		, success: function(result){
			console.log(result);
			console.log(result.length);
			let html = '';
			for (var i=0; i<result.length; i++) {
				console.log(result[i]);
				html += '<tr>';
				html += '<td>'+result[i].prd_nm+'</td>';
				html += '<td>'+result[i].now_prd_cnt+'</td>';
				html += '</tr>';
			}
			$('#resBody').html(html);
    	}
	});
}

function goUrl() {
	var type = '${type}';
	var id = $('#baggage_id').val();
	
	console.log("goUrl");
	console.log(type);
	console.log(id);
	id = id.split('A').join('');
	console.log("after: "+id);
	
	if('R' === type) {
		$.ajax({
            url: "showRList"
            , dataType: "json"  
            , contentType : "application/json"
            , data:{
            	search_type: 'srch',
            	baggage_claim_no:id
            }
            , success: function(res) {
            	if(null != res && '' != res) {
            		console.log(res[0].order_no);
                	$('#baggage_id').val("");
                	isValid = false;
                	$('#baggage_id').focus();
                	window.open(
    	            	"https://order-rp.rms.rakuten.co.jp/order-rb/individual-order-detail-sc/init?orderNumber="+res[0].order_no,
    	       			'_blank' // <- This is what makes it open in a new window.
           			);
            	}else {
            		alert("検索結果がない！");
            	}
            }
        });
		
	}else if('A' === type) {
		$.ajax({
            url: "showAList"
            , dataType: "json"  
            , contentType : "application/json"
            , data:{
            	search_type: 'srch',
            	baggage_claim_no:id
            }
            , success: function(res) {
            	if(null != res && '' != res) {
            		console.log(res[0].order_id);
                	$('#baggage_id').val("");
                	isValid = false;
                	$('#baggage_id').focus();
                	window.open(
    	            	"https://sellercentral.amazon.co.jp/orders-v3/order/"+res[0].order_id,
    	       			'_blank' // <- This is what makes it open in a new window.
           			);
            	}else {
            		alert("検索結果がない！");
            	}
            }
        });
	}
}
</script>
</html>