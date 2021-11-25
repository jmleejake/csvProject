/**
 * javascript file for araku mainpage
 */

$("#btn_rakuten").on("click", function() {
	console.log("rak");
	location.href="/araku/rakuten/fileView";
});

$("#btn_amazon").on("click", function() {
	console.log("ama");
	location.href="/araku/amazon/fileView";
});

$("#btn_q10").on("click", function() {
	console.log("q10");
	location.href="/araku/q10/fileView";
});

$("#btn_yahoo").on("click", function() {
	console.log("yahoo");
	location.href="/araku/yahoo/fileView";
});

$("#btn_tablet").on("click", function() {
	console.log("tablet");
	location.href = "/araku/tablet/prdMng";
});

$("#btn_tanpin").on("click", function() {
	console.log("tanpin");
	location.href = "/araku/prdAnalysis/prdMng";
});

$("#btn_prdAnal").on("click", function() {
	console.log("product analysis");
	location.href = "/araku/prdAnalysis/listView";
});

$('#btn_order').on('click', function() {
	console.log('purchase order');
	location.href = "/araku/prdAnalysis/orderView";
});