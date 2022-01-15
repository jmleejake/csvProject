/**
 * 
 */

var statusColumn = [
	{headerName: "JANコード", field: "jan_cd", width: 200, editable:true }
	, {headerName: "商品名", field: "prd_nm", width: 500, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "金額", field: "prd_prc", width: 100, editable:true }
];

// specify the data
var statusRowData = [];
// 수정데이터 배열
var statusModData = [];
var sJanCd, eJanCd;
var sPrdNm, ePrdNm;
var sPrdPrc, ePrdPrc;

// let the grid know which columns and what data to use
var statusGridOptions = {
		defaultColDef: {
			width: 100,
			headerCheckboxSelection: isFirstColumn,
			checkboxSelection: isFirstColumn
		},
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: statusColumn,
		rowData: statusRowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	sPrdNm=start.prd_nm;
	    	sJanCd=start.jan_cd;
	    	sPrdPrc=start.prd_prc;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	ePrdNm=stop.prd_nm;
	    	eJanCd=stop.jan_cd;
	    	ePrdPrc=stop.prd_prc;
	    	
	    	if (!(sPrdNm == ePrdNm)||!(sJanCd == eJanCd)
	    			||!(sPrdPrc == ePrdPrc)) {
	    		statusModData.push(stop);
	    	}
	    }
};

// lookup the container we want the Grid to use
var statusGridDiv = document.querySelector('#statusGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(statusGridDiv, statusGridOptions);

function setStatusRowData(result) {
	statusRowData = [];
	statusModData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, prd_cd: result[i].prd_cd
				, prd_nm: result[i].prd_nm
				, prd_prc: result[i].prd_prc
				, tax_inc: result[i].tax_inc
				, partner_id: result[i].partner_id
				, partner_nm: result[i].partner_nm
				, estimate_id: result[i].estimate_id
				, jan_cd: result[i].jan_cd
				, tax_incld:result[i].tax_incld
				, tax_rt:result[i].tax_rt
				, std_info:result[i].std_info
				, dsku:result[i].dsku
				, dasin:result[i].dasin
				, reg_dt:result[i].reg_dt
				, upd_dt:result[i].upd_dt
		}
		statusRowData.push(row);
	}
	statusGridOptions.api.setRowData(statusRowData);
}


function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$('#btn_srch').on('click', function() {
	if('' === $('#selectedDealer').html()) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	var form = $("#frm");
    $.ajax({
        type: "get"
        , url: "getStatus"
        , dataType: "json"
        , contentType: 'application/json'
        , data: form.serialize()
        , success: setStatusRowData
    });
});

$('#btn_add').on('click', function() {
	if('' === $('#selectedDealer').html()) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	statusModData.push({partner_id:partId, partner_nm:partNm, prd_nm:'商品名', prd_prc:'0', jan_cd:'0'});
	$.ajax({
		url: "mani"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(statusModData)
		, success: setStatusRowData
	});
});

$('#btn_commit').on('click', function() {
	if (statusModData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	if('' === $('#selectedDealer').html()) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	$.ajax({
		url: "mani"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(statusModData)
		, success: setStatusRowData
	});
});

$('#btn_delete').on('click', function() {
	var selectedRows = statusGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    if('' === $('#selectedDealer').html()) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
				url: "del"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(selectedRows)
				, success: setStatusRowData
			});
		} else {
			return false;
		}
	});
});

//거래처 셀렉트박스 선택시
var partId, partNm;
function showOrder(id, nm) {
	partId=id;
	partNm=nm;
	$('input[name=partner_id]').val(id);
	$('#selectedDealer').html(': '+nm);
	$.ajax({
		url: "getStatus"
		, type:"get"
		, data: {
			partner_id:id
		}
		, dataType: "json"
		, contentType: 'application/json'
		, success: setStatusRowData
	});
}

//************************************************
// MODAL
//************************************************

var prdColumn = [
	{headerName: "JANコード", field: "jan_cd", width: 120}
	, {headerName: "商品コード", field: "prd_cd", width: 120}
	, {headerName: "商品名", field: "prd_nm", width: 300}
	, {headerName: "金額", field: "prd_unit_prc", width: 100, editable:true }
];

// specify the data
var prdRowData = [];
// 수정데이터 배열
var prdModData = [];
var sPrdPrc2, ePrdPrc2;

// let the grid know which columns and what data to use
var prdGridOptions = {
		defaultColDef: {
			width: 100,
			headerCheckboxSelection: isFirstColumn,
			checkboxSelection: isFirstColumn
		},
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: prdColumn,
		rowData: prdRowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	sPrdPrc2=start.prd_unit_prc;
	    	console.log(sPrdPrc2);
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	ePrdPrc2=stop.prd_unit_prc;
	    	console.log(ePrdPrc2);
	    	
	    	if (!(sPrdPrc2 == ePrdPrc2)) {
	    		prdModData.push({jan_cd:stop.jan_cd, prd_prc:ePrdPrc2, partner_id:partId2, partner_nm:partNm2});
	    	}
	    }
};

// lookup the container we want the Grid to use
var prdGridDiv = document.querySelector('#prdGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdGridDiv, prdGridOptions);

function setPrdRowData(result) {
	prdRowData = [];
	prdModData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, prd_cd:result[i].prd_cd
				, brand_nm:result[i].brand_nm
				, prd_nm:result[i].prd_nm
				, jan_cd:result[i].jan_cd
				, prd_cnt:result[i].prd_cnt
				, prd_unit_prc:result[i].prd_unit_prc
				, tax_incld:result[i].tax_incld
				, tax_rt:result[i].tax_rt
				, register_date:result[i].reg_dt
				, update_date:result[i].upd_dt
		}
		prdRowData.push(row);
	}
	prdGridOptions.api.setRowData(prdRowData);
}

//거래처 셀렉트박스 선택시
var partId2, partNm2;
function showPrd(id, nm) {
	partId2=id;
	partNm2=nm;
	$('#prdDealer').html(': '+nm);
	$.ajax({
		url: "/jaiko/prdInfo/getPrdInfo"
		, type:"get"
		, data: {
			search_type:'srch'
			,partner_id:id
		}
		, dataType: "json"
		, contentType: 'application/json'
		, success: setPrdRowData
	});
}

$('#prd_srch').on('click', function() {
	if('' === $('#prdDealer').html()) {
		alert('取引先を選択してください。');
		return;
	}
	
	var form = $("#frm_prd");
    $.ajax({
        type: "get"
        , url: "/jaiko/prdInfo/getPrdInfo"
        , dataType: "json"
        , contentType: 'application/json'
        , data: form.serialize()
        , success: setPrdRowData
    });
});

$('#btn_batch').on('click', function() {
	if('' === $('input[name=percent]').val()) {
		alert('掛け率を入力してください。');
		return;
	}
	if('' === $('#prdDealer').html()) {
		alert('取引先を選択してください。');
		return;
	}
	
	prdModData.push({percent:$('input[name=percent]').val(), partner_id:partId2})
 	$.ajax({
		url: "create"
		, type:"post"
		, data:JSON.stringify(prdModData)
 		, dataType: "json"
		, contentType: 'application/json'
		, success: function(res) {
			prdModData = [];
			$('#selectedDealer').html(': '+partNm2);
			setStatusRowData(res);
			$('#create').modal('hide');
		}
	});
});

$('#prd_commit').on('click', function() {
	if (prdModData.length == 0) {
		alert('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "create"
		, type:"post"
		, data:JSON.stringify(prdModData)
 		, dataType: "json"
		, contentType: 'application/json'
		, success: function(res) {
			prdModData = [];
			$('#selectedDealer').html(': '+partNm2);
			setStatusRowData(res);
			$('#create').modal('hide');
		}
	});
});
