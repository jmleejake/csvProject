<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
var isValid = false;
$(document).ready(function() {
	$("#tabChumon").addClass("active");
	$('#baggage_id').focus();
});

function goUrl() {
	var type = '${type}';
	var id = $('#baggage_id').val();
	
	console.log("goUrl");
	console.log(type);
	console.log(id);
	console.log(isValid);
	
	if(isValid) {
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
	            	console.log(res[0].order_no);
	            	$('#baggage_id').val("");
	            	isValid = false;
	            	$('#baggage_id').focus();
	            	window.open(
		            	"https://order-rp.rms.rakuten.co.jp/order-rb/individual-order-detail-sc/init?orderNumber="+res[0].order_no,
		       			'_blank' // <- This is what makes it open in a new window.
	       			);
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
	            	console.log(res[0].order_id);
	            	$('#baggage_id').val("");
	            	isValid = false;
	            	$('#baggage_id').focus();
	            	window.open(
		            	"https://sellercentral.amazon.co.jp/orders-v3/order/"+res[0].order_id,
		       			'_blank' // <- This is what makes it open in a new window.
	       			);
	            }
	        });
		}
	}
}
</script>
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
	<input type="text" id="baggage_id" class="form-control" style="width: 300px;" oninput="javascript:goUrl();" onpaste="javascript:isValid=true;" placeholder="問い合わせ番号">
	</div>
</div>
</div>
</div>
</body>
</html>