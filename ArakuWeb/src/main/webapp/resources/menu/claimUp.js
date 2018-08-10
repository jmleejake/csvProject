/**
 * javascript for 荷物伝票番号更新
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
	, {headerName: "商品名", field: "product_name", width: 400}
	, {headerName: "項目・選択肢", field: "product_option", width: 400}
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
var modifiedData = [];

// let the grid know which columns and what data to use
var claimUpGridOptions = {
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
    onCellEditingStarted: function(event) {
        previousData = event.node.data.model;
    },
    onCellEditingStopped: function(event) {
        afterData = event.node.data.model;

        console.log("previous : " + previousData);
        console.log("after : " + afterData);
        if (!(previousData == afterData)) {
                console.log("modified!");
                modifiedData.push(event.node.data);
                console.log(modifiedData);
        }
    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#claimUpGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, claimUpGridOptions);

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
	for (var i=0; i<result.length; i++) {
		var row = {
				req_id: result[i].req_id
				, order_no: result[i].order_no
				, order_status:result[i].order_status
				, delivery_date_sel:result[i].delivery_date_sel
				, total_amt:'¥' + result[i].total_amt
				, baggage_claim_no:result[i].baggage_claim_no
				, delivery_name:result[i].delivery_surname + ' ' + result[i].delivery_name
				, delivery_tel:result[i].delivery_tel1 + '-' +  result[i].delivery_tel2 + '-' +  result[i].delivery_tel3
				, product_name:result[i].product_name
				, product_option:result[i].product_option
				, tomorrow_hope:result[i].tomorrow_hope
				, register_date:result[i].register_date
		}
		rowData.push(row);
	}
	claimUpGridOptions.api.setRowData(rowData);
}

$("#btn_sel").on("click", function() {
        var selectedRows = claimUpGridOptions.api.getSelectedRows();
        
        if (selectedRows.length == 0) {
            alert("no modified data!");
            return;
        }

        for (var i=0; i<selectedRows.length; i++) {
                var data = selectedRows[i];

                console.log(data);
        }
});

$("#btn_mod").on("click", function() {

});

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