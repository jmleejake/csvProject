/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "商品メーカー名", field: "maker_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 300}
	, {headerName: "容量", field: "capacity", width: 100}
	, {headerName: "取引先会社名", field: "dealer_nm", width: 200}
	, {headerName: "仕入金額", field: "inprice", width: 100}
	, {headerName: "販売金額", field: "price", width: 100}
	, {headerName: "商品メーカー", field: "maker_cd", width: 80}
	, {headerName: "商品コード(JAN)", field: "prd_cd", width: 80}
	, {headerName: "取引先コード", field: "dealer_id", width: 80}
];

// specify the data
var rowData = [];
// 수정데이터 배열
var modifiedData = [];
var previousData, afterData;

// let the grid know which columns and what data to use
var orderGridOptions = {
		defaultColDef: {
			width: 100,
			headerCheckboxSelection: isFirstColumn,
			checkboxSelection: isFirstColumn
		},
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: columnDefs,
		rowData: rowData
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#prdMGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);

function setRowData(result) {
	rowData = [];
	$('#selectedMaker').html("");
	$('#hidMakerCd').val("");
	$('#selectedDealer').html("");
	$('#hidDealerId').val("");
	$('input[name=prd_nm]').val("");
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, maker_cd: result[i].maker_cd
				, maker_nm: result[i].maker_nm
				, prd_cd: result[i].prd_cd
				, prd_nm: result[i].prd_nm
				, capacity: result[i].capacity
				, inprice: result[i].inprice
				, price: result[i].price
				, std_price: result[i].std_price
				, tax_inc: result[i].tax_inc
				, tax_rt: result[i].tax_rt
				, dealer_id: result[i].dealer_id
				, dealer_nm: result[i].dealer_nm
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

function setSearch(type, cd, nm) {
	if("M" === type) {
		$('#selectedMaker').html(": "+nm);
		$('#hidMakerCd').val(cd);
	}else if("D" === type) {
		$('#selectedDealer').html(": "+nm);
		$('#hidDealerId').val(cd);
	}
}

$("#btn_srch").on("click", function() {
	var form = $("#frm");
    
    $.ajax({
        type: "get"
        , url: "getTanpin"
        , dataType: "json"
        , contentType: 'application/json'
        , data: form.serialize() // serializes the form's elements.
        , success: setRowData
    });

});

$("#btn_del").on("click", function() {
	var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			 $.ajax({
					url: "delTanpin"
					, type:"post"
					, dataType: "json"
					, contentType: 'application/json'
					, data:JSON.stringify(selectedRows)
					, success: setRowData
				});
		}else {
			return false;
		}
	});
});

$("#btn_down").on("click", function() {
	var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    var id_lst = [];
    
    for (var i=0; i<selectedRows.length; i++) {
		id_lst.push(selectedRows[i].seq_id);
	}
    
	$("#seq_id_list").val(id_lst);
	$("#frm_down").submit();
});

