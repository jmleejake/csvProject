/**
 * javascript file for araku mainpage
 */

$("#btn_rakuten").on("click", function() {
	console.log("rak");
	location.href="fileView";
});

$("#btn_amazon").on("click", function() {
	console.log("ama");
	location.href="amazon/fileView";
});