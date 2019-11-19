/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "注文内容", field: "prd_nm", width: 200}
	, {headerName: "", field: "prd_dtl", width: 300}
	, {headerName: "注文数", field: "unit_no", width: 100}
	, {headerName: "読込数", field: "", width: 100}
];

// specify the data
var rowData = [];
// 수정데이터 배열
var modifiedData = [];
var previousData, afterData;

// let the grid know which columns and what data to use
var orderGridOptions = {
	columnDefs: columnDefs,
	rowData: rowData
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#prdMGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);

function setRowData(result) {
	rowData = [];
	
	$("#prdMngName").html("名前： "+result[0].delivery_surname+" "+result[0].delivery_name);
	$("#prdMngName").css("font-size", "25px");
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, order_no: result[i].order_no
				, prd_cnt:result[i].prd_cnt
				, prd_dtl:result[i].prd_dtl
				, prd_nm:result[i].prd_nm
				, baggage_claim_no:result[i].baggage_claim_no
				, jan_cd:result[i].jan_cd
				, prd_cd:result[i].prd_cd
				, unit_no:result[i].unit_no
				, order_surname:result[i].order_surname
				, order_name:result[i].order_name
				, delivery_surname:result[i].delivery_surname
				, delivery_name:result[i].delivery_name
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
	var form = $("#prdMngForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "get",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });

});

