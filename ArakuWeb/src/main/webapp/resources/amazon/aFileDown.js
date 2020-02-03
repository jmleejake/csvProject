/**
 * javascript for 楽天更新ファイル
 */
// specify the columns
var columnDefs = [
	{headerName: "注文ID", field: "order_id", width: 200}
	, {headerName: "注文商品番号", field: "order_item_id", width: 200}
	, {headerName: "出荷数", field: "quantity_to_ship", width: 100}
	, {headerName: "出荷日", field: "promise_date", width: 200}
	, {headerName: "配送業者コード", field: "delivery_company", width: 180}
	, {headerName: "お問い合わせ伝票番号", field: "baggage_claim_no", width: 300
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
	, {headerName: "代金引換", field: "cod_collectible_amount", width: 200}
];

// rowData 초기화
var rowData = [];

//수정 전후를 파악할 변수 선언
var startData, stopData, id;

//수정데이터 배열
var modifiedData = [];

// let the grid know which columns and what data to use
var aFileDownGridOptions = {
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
var eGridDiv = document.querySelector('#aFileDownGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, aFileDownGridOptions);

var d = new Date();
var yesterday = d.getFullYear() +'/'+ (d.getMonth()+1) + '/' + (d.getDate());

$.ajax({
    url: "showAList"
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
				, order_id: result[i].order_id
				, order_item_id:result[i].order_item_id
				, quantity_to_ship:result[i].quantity_to_ship
				, promise_date:result[i].promise_date
				, delivery_company:result[i].delivery_company
				, baggage_claim_no:result[i].baggage_claim_no
				, cod_collectible_amount:result[i].cod_collectible_amount
		}
		rowData.push(row);
	}
	aFileDownGridOptions.api.setRowData(rowData);
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
	var form = $("#aFileDownForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });

});

$("#btn_adown").on("click", function() {
	var selectedRows = aFileDownGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    var id_lst = [];
    
    for (var i=0; i<selectedRows.length; i++) {
		id_lst.push(selectedRows[i].seq_id);
	}
    
	$("#seq_id_list").val(id_lst);
	$("#frm_aFileDown").submit();
});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modAmazon"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});