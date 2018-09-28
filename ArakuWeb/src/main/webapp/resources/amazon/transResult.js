/**
 * javascript for 置換結果
 */

var columnDefs = [
	{headerName: "注文ID", field: "order_id", width: 200}
	, {headerName: "出荷予定日", field: "promise_date", width: 130}
	, {headerName: "商品名", field: "product_name", width: 300
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "配送サービスレベル", field: "ship_service_level", width: 100
		, cellRenderer: function(params) {
			if (params.value == 'NextDay') {
				return '<img src="./../resources/img/fastDelivery.png" alt="logo" style="width:40px; margin-left:10px;">';
			}
		}
	}
	, {headerName: "受取人", field: "recipient_name", width: 170}
	, {headerName: "お届け先", field: "ship_address", width: 300
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "市区町村", field: "ship_city", width: 100}
	, {headerName: "都道府県", field: "ship_state", width: 100}
	, {headerName: "配送会社", field: "delivery_company", width: 80}
	, {headerName: "発送先郵便番号", field: "ship_postal_code", width: 100}
	, {headerName: "支払方法", field: "payment_method", width: 100}
	, {headerName: "代金引換回収額", field: "cod_collectible_amount", width: 100}
];

// specify the data
var rowData = [];

var startResultTxt, stopResultTxt;

// let the grid know which columns and what data to use
var gridOptions = {
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	onCellEditingStarted: function(event) {
		var start = event.node.data;
		startResultTxt = start.result_text;
	},
	onCellEditingStopped: function(event) {
		var stop = event.node.data;
		stopResultTxt = stop.result_text;
		
		console.log("start");
		console.log(startResultTxt);
		console.log("stop");
		console.log(stopResultTxt);
		if (!(startResultTxt == stopResultTxt)) {
			console.log("modified!");
			$.ajax({
			    url: "modTransResult"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:{
			    	seq_id : stop.seq_id
			    	, result_text : stopResultTxt
			    }
			});
		}
	}
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#resultGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, gridOptions);

function getData() {
	$.ajax({
	    url: "getTransResult"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{id_lst:$("#id_lst").val()}
	    , success: setRowData
	});
}

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var cod_amt = result[i].cod_collectible_amount;
		var row = {
				seq_id: result[i].seq_id
				, order_id: result[i].order_id
				, promise_date:result[i].promise_date
				, product_name:result[i].product_name
				, ship_service_level:result[i].ship_service_level
				, recipient_name:result[i].recipient_name
				, ship_address:result[i].ship_address1 + '-' +  result[i].ship_address2 + '-' +  result[i].ship_address3
				, ship_city:result[i].ship_city
				, ship_state:result[i].ship_state
				, ship_postal_code:result[i].ship_postal_code
				, payment_method:result[i].payment_method
				, cod_collectible_amount:cod_amt != '' ? '¥' + cod_amt : ''
				, delivery_company:result[i].delivery_company
				, register_date:result[i].register_date
				, update_date:result[i].update_date
		}
		rowData.push(row);
	}
	gridOptions.api.setRowData(rowData);
}

// 유프리아루 CSV파일 다운로드
$("#btn_yu_down").on("click", function() {
	/*
	console.log("yupuri download button clicked");
	$("#yu_code").val("1003");
	console.log("code : " + $("#yu_code").val());
	$("#frm_down3").submit();
	*/
	pleaseSelectNotify('구현예정');
});

//야마토 CSV파일 다운로드
$("#btn_ya_down").on("click", function() {
	console.log("yamato download button clicked");
	$("#ya_code").val("1001");
	console.log("code : " + $("#ya_code").val());
	$("#frm_down1").submit();
});

//사가와 CSV파일 다운로드
$("#btn_sa_down").on("click", function() {
	console.log("sagawa download button clicked");
	 $("#sa_code").val("1002");
	console.log("code : " + $("#sa_code").val());
	 $("#frm_down2").submit();
});