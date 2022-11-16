$("#yymm").datetimepicker({
	locale: "ja"
	, viewMode: 'years'
    , format: 'YYYY-MM'
}).on('dp.change', function() {
	document.location.href = "/jaiko/order?reg_dt="+$('#yymm').val();
});


/*
------------------
modal setting S
------------------
*/
var thisPartnerId, thisDt;
function showOrder(id, nm) {
	thisPartnerId = id;
	$('#selectedDealer').html(': '+nm);
	$.ajax({
		url: "/jaiko/order/getData"
		, type:"get"
		, data: {partner_id:id, reg_dt:$('#yymm').val()}
		, dataType: "json"
		, contentType: 'application/json'
		, success: ordDtlSetRowData
	});
}

function showDetail(id, nm, dt) {
	thisPartnerId = id;
	thisDt = dt;
	$('#selectedDealer').html(': '+nm);
	$('#dtH4').html(thisDt);
	if('' != id) {
		$.ajax({
			url: "/jaiko/order/getData"
			, type:"get"
			, data: {partner_id:id, reg_dt:thisDt}
			, dataType: "json"
			, contentType: 'application/json'
			, success: ordDtlSetRowData
		});
	}else {
		ordDtlGridOptions.api.setRowData([]);
	}
}

var ordDtlColumnDef = [
	/*
	{headerName: "年月日", field: "reg_dt", width: 150}
	, {headerName: "納品先", field: "partner_nm", width: 200}
	, {headerName: "商品名検索", width: 120, cellRenderer:'btnRenderer'}
	, {headerName: "商品名", field: "prd_nm", width: 200}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 150}
	, {headerName: "数量―個数―", field: "prd_cnt", width: 120, editable: true}
	*/
	{headerName: "商品名検索", width: 200, cellRenderer:'btnRenderer'}
	, {headerName: "商品名", field: "prd_nm", width: 800}
	, {headerName: "数量―個数―", field: "prd_cnt", width: 150, editable: true}
];

// specify the data
var ordDtlRowData = [];

//수정데이터 배열
var modifiedData = [];
var prevPrdNm, afterPrdNm;
var prevPrdCnt, afterPrdCnt;
var prevPrdCntBox, afterPrdCntBox;
var srchSeqId;

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
	rowClassRules: {
		'trans-created': function(params) {
    		var target = params.data.reg_dt;
    		return target === thisDt;
    	},
    	'trans-modified': function(params) {
    		var target = params.data.upd_dt;
    		return target === getDate(0,'-');
    	}
	},
	components: {
		btnRenderer: function(param) {
			var html = "<div>";
			html += "<span>";
			html += "<button type='button' class='btn btn-default' data-toggle='modal' data-target='#srchPrd' onclick='javascript:setInvenNo("+param.data.seq_id+")' style='width: 50px; height:30px;'>検索</button>";
			html += "</span>";
			html += "</div>"
			return html;
		}
	},
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
			, jan_cd:result[i].jan_cd
			, gtin_cd:result[i].gtin_cd
			, reg_dt:result[i].reg_dt
			, upd_dt:result[i].upd_dt
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
		};
		ordDtlRowData.push(row);
	}
	ordDtlGridOptions.api.setRowData(ordDtlRowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
	$('#srchPrd').modal('hide');
}

$('#btn_add').on('click', function() {
	console.log("thisDt2="+thisDt);
	if('' === thisPartnerId) {
		alert('取引先を選択してください。');
		return;
	}
	modifiedData.push({partner_id:thisPartnerId, prd_nm:'商品名', prd_cnt_box:'0', prd_cnt:'0', reg_dt:thisDt});
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

function setInvenNo(id) {
	srchSeqId = id;
}

// ----------------------------------------------------------------------------------------

var columnDefs = [
	{headerName: "商品名", field: "prd_nm", width: 400
		, tooltip:function(param) {
			return param.value;
		}
	}
	, {headerName: "現在商品数", field: "now_prd_cnt", width: 120}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200}
];

// specify the data
var rowData = [];

//let the grid know which columns and what data to use
var prdInfoGridOptions = {
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
	rowData: rowData
};

// lookup the container we want the Grid to use
var prdInfoGridDiv = document.querySelector('#prdInfoGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdInfoGridDiv, prdInfoGridOptions);

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, now_prd_cnt:result[i].now_prd_cnt
		};
		rowData.push(row);
	}
	prdInfoGridOptions.api.setRowData(rowData);
}

$("#btn_srch").on("click", function() {
	$.ajax({
		url: "/jaiko/prdInven/getPrdInven"
		, type:"get"
		, data: $("#frm_srch").serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setRowData
	});
});

$('#btn_sel').on('click', function() {
	var selectedRows = prdInfoGridOptions.api.getSelectedRows();
	if (selectedRows.length == 0) {
    	alert('情報を選択してください。');
        return;
    }
	if (selectedRows.length > 1) {
    	alert('一つだけ選択できない。');
        return;
    }
	var row = selectedRows[0];
	modifiedData.push({seq_id:srchSeqId, reg_dt:thisDt, partner_id:thisPartnerId, prd_nm:row.prd_nm, jan_cd:row.jan_cd});
	$.ajax({
		url: "/jaiko/order/mani"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: ordDtlSetRowData
	});
});
/*
------------------
modal setting E
------------------
*/




/*
------------------
売上 S
------------------
*/
$('#btn_sales').on('click', function() {
	//alert('売上');
	ordDtlGridOptions.api.selectAll();
	var selectedRows = ordDtlGridOptions.api.getSelectedRows();
	
	$.ajax({
		url: "/jaiko/sales/data/ins"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(selectedRows)
		, success: function(res) {
			alert(res.retMsg);
		}
	});
});
/*
------------------
売上 E
------------------
*/
