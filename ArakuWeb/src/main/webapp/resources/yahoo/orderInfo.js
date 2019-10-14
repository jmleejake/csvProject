/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "注文番号", field: "order_id", width: 150}
	, {headerName: "注文内容", field: "line_id", width: 150}
	, {headerName: "お届け先氏名", field: "ship_nm", width: 200}
	, {headerName: "お届け先電話番号", field: "ship_phone_no", width: 200}
	, {headerName: "商品名", field: "title", width: 400
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "オプション情報", field: "item_option_val", width: 400
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "個数", field: "qty", width: 100}
	, {headerName: "ストアアカウント付き注文番号", field: "id", width: 300}
	, {headerName: "あす楽", field: "lead_time_txt", width: 300}
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
		width: 100,
		headerCheckboxSelection: isFirstColumn,
		checkboxSelection: isFirstColumn
	},
	enableColResize: true,
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
    url: "showYList"
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
    
    $.ajax({
		url: "executeTrans"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(selectedRows)
		, success: function(params){
			console.log(params);
			$("#hid_data").val(params);
			$("#order_frm").submit();
    	}
	});
});

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
			    url: "delYahoo"
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
