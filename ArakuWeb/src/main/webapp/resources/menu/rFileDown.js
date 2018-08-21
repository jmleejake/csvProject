/**
 * javascript for 楽天更新ファイル
 */
// specify the columns
var columnDefs = [
	{headerName: "受注番号", field: "order_no", width: 230}
	, {headerName: "受注ステータス", field: "order_status", width: 130}
	, {headerName: "お届け日指定", field: "delivery_date_sel", width: 130}
	, {headerName: "合計金額", field: "total_amt", width: 100}
	, {headerName: "お荷物伝票番号", field: "baggage_claim_no", width: 170
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
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
				console.log("true in");
				return '<img src="./resources/img/fastDelivery.png" alt="logo" style="width:40px; margin-left:10px;">';
			}
		}
	}
];

// specify the data
var rowData = [];

var previousData, afterData;

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
    onCellEditingStopped: function(event) {
        afterData = event.node.data;
        console.log("modified!");
        console.log(afterData);
        $.ajax({
            url: "modRakuten"
            , dataType: "json"  
            , contentType : "application/json"
            , data:{
            	seq_id:afterData.seq_id
            	, baggage_claim_no:afterData.baggage_claim_no
            }
            , success: setRowData
        });
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

function getInfo(data) {
        console.log("getInfo");
        console.log(data);
        $('form').attr('action', "getInfo");
        $('form').attr('method', "post");
        $("#car_id").val(data);
        $("form").submit();
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