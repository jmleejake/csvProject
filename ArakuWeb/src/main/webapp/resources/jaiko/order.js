/**
 * 
 */
/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName:"詳細", width:100, cellRenderer:'btnRenderer'}
	, {headerName: "年月日", field: "reg_dt", width: 120}
	, {headerName: "納品先", field: "partner_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 200}
	, {headerName: "数量（BOX）―箱―", field: "prd_cnt_box", width: 200}
	, {headerName: "数量―個数―", field: "prd_cnt", width: 200}
];

// specify the data
var rowData = [];

//let the grid know which columns and what data to use
var prdOrdGridOption = {
	defaultColDef: {
		resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	components: {
		btnRenderer: function(param) {
			var html = "<div>";
			html += "<span>";
			html += "<button type='button' class='btn btn-default' data-toggle='modal' data-target='#orderDetail' onclick=\"javascript:showDetail('"+param.data.partner_id+"','"+param.data.partner_nm+"');\" style='height:25px; margin-bottom:5px;'>詳細</button>";
			html += "</span>";
			html += "</div>"
			return html;
		}
	},
    onCellEditingStarted: function(event) {
        
    },
    onCellEditingStopped: function(event) {
        
    }
};

//lookup the container we want the Grid to use
var prdOrdGridDiv = document.querySelector('#orderGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdOrdGridDiv, prdOrdGridOption);

$.ajax({
    url: "/jaiko/order/getMonthlyData"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	var beforeDt,tmp;
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, reg_dt:beforeDt === result[i].reg_dt ? '' : tmp === result[i].reg_dt ? '' : result[i].reg_dt
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
			, prd_nm:result[i].prd_nm
			, prd_cnt:result[i].prd_cnt
			, prd_cnt_box:result[i].prd_cnt_box
		};
		rowData.push(row);
		beforeDt = row.reg_dt;
		if('' != beforeDt) {
			tmp=beforeDt;
		}
	}
	prdOrdGridOption.api.setRowData(rowData);
}

/*
------------------
grid setting E
------------------
*/

$("#yymm").datetimepicker({
	locale: "ja"
	, viewMode: 'years'
    , format: 'YYYY-MM'
}).on('dp.change', function() {
	$.ajax({
	    url: "/jaiko/order/getMonthlyData"
    	, data: {reg_dt:$('#yymm').val()}
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setRowData
	});
});


/*
------------------
modal setting S
------------------
*/
var thisPartnerId;
function showOrder(id, nm) {
	thisPartnerId = id;
	$('#selectedDealer').html(': '+nm);
	$.ajax({
		url: "/jaiko/order/getData"
		, type:"get"
		, data: {partner_id:id}
		, dataType: "json"
		, contentType: 'application/json'
		, success: ordDtlSetRowData
	});
}

function showDetail(id, nm) {
	thisPartnerId = id;
	$('#selectedDealer').html(': '+nm);
	$.ajax({
		url: "/jaiko/order/getData"
		, type:"get"
		, data: {partner_id:id}
		, dataType: "json"
		, contentType: 'application/json'
		, success: ordDtlSetRowData
	});
}

var ordDtlColumnDef = [
	{headerName: "年月日", field: "reg_dt", width: 200}
	, {headerName: "納品先", field: "partner_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 200, editable: true}
	, {headerName: "数量（BOX）―箱―", field: "prd_cnt_box", width: 200, editable: true}
	, {headerName: "数量―個数―", field: "prd_cnt", width: 200, editable: true}
];

// specify the data
var ordDtlRowData = [];

//수정데이터 배열
var modifiedData = [];
var prevPrdNm, afterPrdNm;
var prevPrdCnt, afterPrdCnt;
var prevPrdCntBox, afterPrdCntBox;

//let the grid know which columns and what data to use
var ordDtlGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: ordDtlColumnDef,
	rowData: ordDtlRowData,
	onCellEditingStarted: function(event) {
        var previousData = event.node.data;
        prevPrdNm = previousData.prd_nm;
        prevPrdCnt = previousData.prd_cnt;
        prevPrdCntBox = previousData.prd_cnt_box;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterPrdNm = afterData.prd_nm;
        afterPrdCnt = afterData.prd_cnt;
        afterPrdCntBox = afterData.prd_cnt_box;
        
        if (!(prevPrdNm == afterPrdNm) ||
        	!(prevPrdCnt == afterPrdCnt) ||
        	!(prevPrdCntBox == afterPrdCntBox)) {
        	modifiedData.push(afterData);
        }
    }
};

//lookup the container we want the Grid to use
var ordDtlGridDiv = document.querySelector('#ordDtlGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(ordDtlGridDiv, ordDtlGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function ordDtlSetRowData(result) {
	ordDtlRowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_nm:result[i].prd_nm
			, prd_cnt:result[i].prd_cnt
			, prd_cnt_box:result[i].prd_cnt_box
			, reg_dt:result[i].reg_dt
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
		};
		ordDtlRowData.push(row);
	}
	ordDtlGridOptions.api.setRowData(ordDtlRowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

$('#btn_add').on('click', function() {
	modifiedData.push({partner_id:thisPartnerId, prd_nm:'商品名', prd_cnt_box:'0', prd_cnt:'0'});
	$.ajax({
		url: "/jaiko/order/mani"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: ordDtlSetRowData
	});
});

$('#btn_commit').on('click', function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "/jaiko/order/mani"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: ordDtlSetRowData
	});
});

$('#btn_delete').on('click', function() {
var selectedRows = ordDtlGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
				url: "/jaiko/order/del"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(selectedRows)
				, success: ordDtlSetRowData
			});
		} else {
			return false;
		}
	});
});

/*
------------------
modal setting E
------------------
*/
