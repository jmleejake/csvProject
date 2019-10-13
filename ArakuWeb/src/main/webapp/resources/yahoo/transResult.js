/**
 * javascript for 置換結果
 */

var columnDefs = [
	{headerName: "注文番号", field: "order_id", width: 150}
	, {headerName: "注文日", field: "order_time", width: 150}
	, {headerName: "お届け先氏名", field: "ship_nm", width: 200}
	, {headerName: "お届け先電話番号", field: "ship_phone_no", width: 200}
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
	, {headerName: "商品名", field: "title", width: 400
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
	, {headerName: "オプション情報", field: "item_option_val", width: 400
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
	, {headerName: "数量", field: "qty", width: 80}
	, {headerName: "ストアアカウント付き注文番号", field: "id", width: 300}
	, {headerName: "配送会社コード", field: "ship_company_cd", width: 180}
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
		var cod_amt = result[i].cod_collectible_amount;
		var row = {
				seq_id: result[i].seq_id
				, order_id: result[i].order_id
				, line_id:result[i].line_id
				, ship_nm:result[i].ship_nm
				, ship_phone_no:result[i].ship_phone_no
				, title:result[i].title
				, item_option_val:result[i].item_option_val
				, order_time:result[i].order_time
				, ship_post_no:result[i].ship_post_no
				, ship_pre:result[i].ship_pre
				, ship_city:result[i].ship_city
				, ship_add1:result[i].ship_add1
				, ship_add2:result[i].ship_add2
				, ship_company_cd:result[i].ship_company_cd
				, qty:result[i].qty
				, id:result[i].id
				, lead_time_txt:result[i].lead_time_txt
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, result_text:result[i].result_text
				, err_text:result[i].err_text
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

//クリックポスト CSV파일 다운로드
$("#btn_cp_down").on("click", function() {
	console.log("clickpost download button clicked");
	 $("#frm_down4").submit();
});
