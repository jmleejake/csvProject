/**
 * javascript file for araku mainpage
 */

$("#btn_rakuten").on("click", function() {
	console.log("rak");
	location.href="rakuten/fileView";
});

$("#btn_amazon").on("click", function() {
	console.log("ama");
	location.href="amazon/fileView";
});

$("#btn_q10").on("click", function() {
	console.log("q10");
	location.href="q10/fileView";
});

$("#btn_yahoo").on("click", function() {
	console.log("yahoo");
	location.href="yahoo/fileView";
});

$("#btn_tablet").on("click", function() {
	console.log("tablet");
	location.href = "tablet/prdMng";
});