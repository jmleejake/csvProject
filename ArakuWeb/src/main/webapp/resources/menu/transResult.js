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
	, {headerName: "登録日付", field: "register_date", width: 100}
];

// specify the data
var rowData = [];

// let the grid know which columns and what data to use
var gridOptions = {
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
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
		}
		rowData.push(row);
	}
	gridOptions.api.setRowData(rowData);
}

// 유프리아루 CSV파일 다운로드
$("#btn_down").on("click", function() {
	console.log("download button clicked");
	console.log($("#id_lst").val());
	$("#frm_down").submit();
});