/**
 * javascript for 楽天更新ファイル
 */
// specify the columns
var columnDefs = [
	{headerName: "配送状態", field: "delivery_sts", width: 200}
	, {headerName: "注文番号", field: "order_no", width: 200}
	, {headerName: "カート番号", field: "cart_no", width: 200}
	, {headerName: "配送会社", field: "delivery_company_q10", width: 100}
	, {headerName: "送り状番号", field: "invoice_no", width: 200
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
	, {headerName: "発送日", field: "ship_date", width: 180}
	, {headerName: "発送予定日", field: "ship_eta", width: 300}
	, {headerName: "商品名", field: "product_name", width: 200}
	, {headerName: "数量", field: "qty", width: 200}
	, {headerName: "オプション情報", field: "option_info", width: 200}
	, {headerName: "オプションコード", field: "option_cd", width: 200}
	, {headerName: "受取人名", field: "recpt_name", width: 200}
	, {headerName: "販売者商品コード", field: "product_cd", width: 200}
	, {headerName: "外部広告", field: "ads_no_site", width: 200}
	, {headerName: "決済サイト", field: "pay_site", width: 200}
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
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
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
    	startData = start.invoice_no;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	stopData = stop.invoice_no;
    	id = stop.seq_id;
    	if (!(startData == stopData)) {
    		console.log("modified!");
    		modifiedData.push({
    			seq_id: id
    			, invoice_no: stopData
    		});
    	}
    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#qFileDownGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, aFileDownGridOptions);

var d = new Date();
var yesterday = d.getFullYear() +'/'+ (d.getMonth()+1) + '/' + (d.getDate());

$.ajax({
    url: "showQList"
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
				, delivery_sts:result[i].delivery_sts
				, cart_no:result[i].cart_no
				, delivery_company_q10:result[i].delivery_company_q10
				, invoice_no:result[i].invoice_no
				, ship_date:result[i].ship_date
				, ship_eta:result[i].ship_eta
				, product_name:result[i].product_name
				, qty:result[i].qty
				, option_info:result[i].option_info
				, option_cd:result[i].option_cd
				, recpt_name:result[i].recpt_name
				, product_cd:result[i].product_cd
				, ads_no_site:result[i].ads_no_site
				, pay_site:result[i].pay_site
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