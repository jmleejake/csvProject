/**
 * javascript for 置換結果
 */

var columnDefs = [
	{headerName: "受注番号", field: "order_no", width: 230}
	, {headerName: "名前", field: "delivery_name", width: 120}
	, {headerName: "置換結果", field: "result_text", width: 300
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "商品名", field: "product_name", width: 400
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "項目・選択肢", field: "product_option", width: 400
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "個数", field: "unit_no", width: 100}
	, {headerName: "配送会社", field: "delivery_company", width: 80}
	, {headerName: "登録日付", field: "register_date", width: 100}
	, {headerName: "あす楽希望", field: "tomorrow_hope", width: 100
		, cellRenderer: function(params) {
			if (params.value == 1) {
				console.log("true in");
				return '<img src="'+document.location.origin+'/resources/img/fastDelivery.png" alt="logo" style="width:40px; margin-left:10px;">';
			}
		}
	}
];

// specify the data
var rowData = [];

var startResultTxt, stopResultTxt;

// let the grid know which columns and what data to use
var gridOptions = {
	defaultColDef: {
		resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	rowClassRules: {
    	'trans-error' : function(params) {
    		var target = params.data.err_text;
    		return target === 'ERR';
    	}
    },
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
		var row = {
				seq_id: result[i].seq_id
				, order_no: result[i].order_no
				, delivery_name:result[i].delivery_surname + ' ' + result[i].delivery_name
				, product_name:result[i].product_name
				, product_option:result[i].product_option
				, unit_no:result[i].unit_no
				, register_date:result[i].register_date
				, result_text:result[i].result_text
				, err_text:result[i].err_text
				, delivery_company:result[i].delivery_company
				, tomorrow_hope:result[i].tomorrow_hope
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

function yaInfoInit() {
	$("#downType").val("");
	$('#storage_cd').val('');
}

//야마토 CSV파일 다운로드
$("#btn_ya_down_one").on("click", function() {
	yaInfoInit();
	$("#ya_code").val("1001");
	console.log("code : " + $("#ya_code").val());
	$("#downType").val("one");
	$("#frm_down1").submit();
});

$("#btn_ya_down_multi").on("click", function() {
	yaInfoInit();
	$("#ya_code").val("1001");
	console.log("code : " + $("#ya_code").val());
	$("#downType").val("multi");
	$("#frm_down1").submit();
});

//2021-07-23 야마토 제1창고, 2창고 구분 S
$("#btn_ya1_down").on("click", function() {
	yaInfoInit();
	$("#ya_code").val("1001");
	$('#storage_cd').val('1');
	$("#downType").val("one");
	$("#frm_down1").submit();
});

$("#btn_ya2_down").on("click", function() {
	yaInfoInit();
	$("#ya_code").val("1001");
	$('#storage_cd').val('2');
	$("#downType").val("one");
	$("#frm_down1").submit();
});
//2021-07-23 야마토 제1창고, 2창고 구분 E

//사가와 CSV파일 다운로드
$("#btn_sa_down").on("click", function() {
	console.log("sagawa download button clicked");
	 $("#sa_code").val("1002");
	console.log("code : " + $("#sa_code").val());
	 $("#frm_down2").submit();
});

//クリックポスト CSV파일 다운로드
$("#btn_cp_down").on("click", function() {
	console.log("clickpost download button clicked");
	 $("#frm_down4").submit();
});

//글로벌 사가와 CSV파일 다운로드
$("#btn_glb_sa_down").on("click", function() {
	console.log("global sagawa download button clicked");
	 $("#frm_down5").submit();
});

// 2021-06-06 에코하이빈
$("#btn_eco_down").on("click", function() {
	$("#eco_cd").val("1015");
	$("#frm_eco").submit();
});

// あす楽例外 체크박스 클릭시 서버로 보낼 값 세팅
$("#tomoEx").on("click", function() {
	$("#sa_chk").val("");
	$("#ya_chk").val("");
	if ($("input:checked").length > 0) {
		$("#sa_chk").val("1");
		$("#ya_chk").val("1");
	}
});