/**
 * javascript for 楽天更新ファイル
 */
// specify the columns
var columnDefs = [
	{headerName: "受注番号", field: "order_no", width: 300}
	, {headerName: "受注ステータス", field: "order_status", width: 200}
	, {headerName: "お荷物伝票番号", field: "baggage_claim_no", width: 300
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
	, {headerName: "配送会社", field: "delivery_company", width: 130}
	/*
	, {headerName: "お届け日指定", field: "delivery_date_sel", width: 130}
	, {headerName: "合計金額", field: "total_amt", width: 100}
	, {headerName: "名前", field: "delivery_name", width: 120}
	, {headerName: "電話番号", field: "delivery_tel", width: 120}
	, {headerName: "商品名", field: "product_name", width: 400
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
	, {headerName: "あす楽希望", field: "tomorrow_hope", width: 100
		, cellRenderer: function(params) {
			if (params.value == 1) {
				return '<img src="./../resources/img/fastDelivery.png" alt="logo" style="width:40px; margin-left:10px;">';
			}
		}
	}
	*/
];

// rowData 초기화
var rowData = [];

//수정 전후를 파악할 변수 선언
var startData, stopData, id;

//수정데이터 배열
var modifiedData = [];

// let the grid know which columns and what data to use
var rFileDownGridOptions = {
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
//    onRowSelected: onRowSelected,
    rowData: rowData,
    rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	}
    },
    onCellEditingStarted: function(event) {
    	var start = event.node.data;
    	startData = start.baggage_claim_no;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	stopData = stop.baggage_claim_no;
    	id = stop.seq_id;
    	if (!(startData == stopData)) {
    		console.log("modified!");
    		modifiedData.push({
    			seq_id: id
    			, baggage_claim_no: stopData
    		});
    	}
    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#rFileDownGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, rFileDownGridOptions);

var d = new Date();
var yesterday = d.getFullYear() +'/'+ (d.getMonth()+1) + '/' + (d.getDate());

$.ajax({
    url: "showRList"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{start_date: yesterday}
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	// 수정데이터 초기화
	modifiedData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, order_no: result[i].order_no
				, order_status:result[i].order_status
				, delivery_date_sel:result[i].delivery_date_sel
				, total_amt:'¥' + result[i].total_amt
				, baggage_claim_no:result[i].baggage_claim_no
				, delivery_name:result[i].delivery_surname + ' ' + result[i].delivery_name
				, delivery_tel:result[i].delivery_tel1 + '-' +  result[i].delivery_tel2 + '-' +  result[i].delivery_tel3
				, product_name:result[i].product_name
				, product_option:result[i].product_option
				, unit_no:result[i].unit_no
				, tomorrow_hope:result[i].tomorrow_hope
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, delivery_company:result[i].delivery_company
		}
		rowData.push(row);
	}
	rFileDownGridOptions.api.setRowData(rowData);
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function onRowSelected(event) {
	console.log("row selected");
	console.log(event.node.data);
}

$("#btn_srch").on("click", function() {
	var form = $("#rFileDownForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });

});

$("#btn_rdown").on("click", function() {
	var selectedRows = rFileDownGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    var id_lst = [];
    
    for (var i=0; i<selectedRows.length; i++) {
		id_lst.push(selectedRows[i].seq_id);
	}
    
	$("#seq_id_list").val(id_lst);
	$("#frm_rFileDown").submit();
});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modRakuten"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});