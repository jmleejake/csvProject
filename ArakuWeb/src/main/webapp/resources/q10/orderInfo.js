/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "注文番号", field: "order_no", width: 150}
	, {headerName: "注文日", field: "order_date", width: 150}
	, {headerName: "商品名", field: "product_name", width: 250, editable: true
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "オプション情報", field: "option_info", width: 250
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "数量", field: "qty", width: 80}
	, {headerName: "受取人名(フリガナ)", field: "kana_name", width: 250}
	, {headerName: "住所", field: "address", width: 300
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "郵便番号", field: "post_no", width: 150}
	, {headerName: "配送会社", field: "delivery_company_q10", width: 180}
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
    url: "showQList"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{}
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, order_no: result[i].order_no
				, product_name:result[i].product_name
				, option_info:result[i].option_info
				, kana_name:result[i].recpt_name + " (" +result[i].recpt_name_kana+")"
				, address:result[i].address
				, post_no:result[i].post_no
				, order_date:result[i].order_date
				, qty:result[i].qty
				, delivery_company_q10:result[i].delivery_company_q10
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
	var form = $("#orderForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });

});

$("#btn_trans").on("click", function() {
    var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    loadMask('/resources/img/page-loader.gif');
    $.ajax({
		url: "executeTrans"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(selectedRows)
		, success: function(params){
			closeMask();
			$("#hid_data").val(params);
			$("#order_frm").submit();
    	}
	});
});

function errMsg(errSize) {
	if (errSize == "0" || errSize == "") {
		return false;
	}
	
	alertInit();
	alertify.confirm(errSize + "つのエラーが発生しました。　ファイルをダウンロードしますか？", function (e) {
		if (e) {
			$("#edown_frm").submit();
			alertify.confirm().destroy();
		} else {
			return false;
		}
	});
}

$("#btn_delete").on("click", function() {
	var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delQ10"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData
			});
		} else {
			return false;
		}
	});
});
