/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "注文ID", field: "order_id", width: 200, editable: true}
	, {headerName: "注文商品ID", field: "order_item_id", width: 200, editable: true}
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
				return '<img src="'+document.location.origin+'/resources/img/fastDelivery.png" alt="logo" style="width:40px; margin-left:10px;">';
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
	, {headerName: "発送先郵便番号", field: "ship_postal_code", width: 100}
	, {headerName: "支払方法", field: "payment_method", width: 100}
	, {headerName: "代金引換回収額", field: "cod_collectible_amount", width: 100}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var modifiedData = [];

var previousData, afterData;

// let the grid know which columns and what data to use
var orderGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	},
    	'trans-error' : function(params) {
    		var target = params.data.err_text;
    		return target === 'ERR';
    	}
    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#orderGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);

$.ajax({
    url: "showAList"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{search_type:'srch'}
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var cod_amt = result[i].cod_collectible_amount;
		var row = {
				seq_id: result[i].seq_id
				, order_id: result[i].order_id
				, order_item_id: result[i].order_item_id
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
				, err_text:result[i].err_text
				, quantity_to_ship:result[i].quantity_to_ship
				, register_date:result[i].register_date
				, update_date:result[i].update_date
		}
		rowData.push(row);
	}
	orderGridOptions.api.setRowData(rowData);
}


function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$("#btn_srch").on("click", function() {
    $.ajax({
        type: "post",
        url: "showAList",
        data: {search_type:'srch'},
        success: setRowData
    });

});

$("#btn_sales_down").on("click", function() {
	$("#frm").submit();
});
