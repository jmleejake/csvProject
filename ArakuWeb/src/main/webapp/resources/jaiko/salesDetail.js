/**
 * 売上入力
 */
var thisDt, thisPartner, thisPartNm, thisSeq;
var salesColDefs = [
	{headerName:"No.", valueGetter:'node.rowIndex+1', width: 80}
	, {headerName:"区分", width:100, field:"gbn", width: 150}
	, {headerName: "商品コード", width: 150, cellRenderer:'prdSrch'}
	, {headerName: "商品名", field: "prd_nm", width: 500}
	, {headerName: "仕様・規格", field: "spec", width: 100, editable: true}
	, {headerName: "入数", field: "entry_no", width: 100, editable: true}
	, {headerName: "数量", field: "qty", width: 100, editable: true}
	, {headerName: "単価", field: "dlv_prc", width: 100, valueFormatter: currencyFormatter}
	, {headerName: "金額", field: "tot", width: 100, valueFormatter: currencyFormatter}
	, {headerName: "消費税", field: "cnsp_tax", width: 100, valueFormatter: currencyFormatter}
	, {headerName: "税率", field: "tax_rt", width: 100
		, valueFormatter: function(param) {
			return param.value+'%';
		}
	}
];

//specify the data
var salesRow = [];

// 데이터 수정
var modifiedData = [];
var startSpec, stopSpec;
var startEntry, stopEntry;
var startQty, stopQty;

//let the grid know which columns and what data to use
var salesGridOption = {
		// checkbox on first column
		defaultColDef: {
			width: 20,
			headerCheckboxSelection: isFirstColumn,
			checkboxSelection: isFirstColumn
		},
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: salesColDefs,
		rowData: salesRow,
		components: {
			prdSrch: function(param) {
				var html = '<button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#srchPrd">';
				html += '<span class="glyphicon glyphicon-search"></span> '+param.data.prd_cd;
				html += '</button>';
				return html;
			}
		},
	    onCellEditingStarted: function(event) {
	    	var prevData = event.node.data;
	    	startSpec = prevData.spec;
	    	startEntry = prevData.entry_no;
	    	startQty = prevData.qty;
	    },
	    onCellEditingStopped: function(event) {
	    	var afterData = event.node.data;
	    	stopSpec = afterData.spec;
	    	stopEntry = afterData.entry_no;
	    	stopQty = afterData.qty;
	    	
	    	if(!(startSpec == stopSpec)) {
	    		// 仕様・規格 = 단독 update
	    		modifiedData.push({
	    		    	seq_id: afterData.seq_id
	    		    	, partner_id: afterData.partner_id
	    		    	, dlv_dt: afterData.dlv_dt
	    		    	, spec: stopSpec
			    });
	    		$.ajax({
	    		    url: "/jaiko/sales/data/upd"
			    	, type:"post"	
	    		    , data: JSON.stringify(modifiedData)
	    		    , dataType: "json"  
	    		    , contentType : "application/json"
	    		    , success: setSalesRow
	    		});
	    	}else if(null != stopEntry && null != stopQty) {
	    		// 入数, 数量이 입력됐을때 계산하여 金額를 구한다.
	    		var entryQty = stopEntry*stopQty;
	    		modifiedData.push({
	    		    	seq_id: afterData.seq_id
	    		    	, partner_id: afterData.partner_id
	    		    	, dlv_dt: afterData.dlv_dt
	    		    	, entry_no: stopEntry
	    		    	, qty: stopQty
	    		    	, tot: entryQty*afterData.dlv_prc
	    		    	, sub_tot: entryQty*afterData.cnsp_tax + entryQty*afterData.dlv_prc
			    });
	    		$.ajax({
	    		    url: "/jaiko/sales/data/upd"
			    	, type:"post"	
	    		    , data: JSON.stringify(modifiedData)
	    		    , dataType: "json"  
	    		    , contentType : "application/json"
	    		    , success: setSalesRow
	    		});
	    	}
	    },
	    onSelectionChanged: fnSelected
};

function fnSelected() {
	const row = salesGridOption.api.getSelectedRows();
	thisSeq = row[0].seq_id;
}

function currencyFormatter(param) {
	return '￥'+formatNumber(param.value);
}

function formatNumber(number) {
    return Math.floor(number)
      .toString()
      .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
}

function showDetail(partnerId, partnerNm, dt) {
	thisPartner = partnerId;
	thisPartNm = partnerNm;
	thisDt = dt;
	$.ajax({
	    url: "/jaiko/sales/data/sel"
	    , data:{
	    	partner_id: partnerId
	    	, dt: dt
	    }
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setSalesRow
	});
}

$('#btn_add').on('click', function() {
	modifiedData.push({
    	partner_id: thisPartner
    	, dlv_dt: thisDt
	});
	$.ajax({
	    url: "/jaiko/sales/data/upd"
		, type:"post"	
	    , data: JSON.stringify(modifiedData)
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setSalesRow
	});
});

$('#btn_commit').on('click', function() {
	alert('commit');
});

$('#btn_delete').on('click', function() {
	var selectedRows = salesGridOption.api.getSelectedRows();
	
	if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
				url: "/jaiko/sales/data/del"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(selectedRows)
				, success: setSalesRow
			});
		} else {
			return false;
		}
	});
});

$('#btn_save').on('click', function() {
	modifiedData.push({
    	partner_id: thisPartner
    	, dlv_dt: thisDt
    	, memo: $('#memo').val()
    	, gbn: 'TOT'
    	, mid_tot: subTotSum
    	, bill_no: $('#dlvNo').val()
	});
	$.ajax({
	    url: "/jaiko/sales/data/upd"
		, type:"post"	
	    , data: JSON.stringify(modifiedData)
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setSalesRow
	});
});

//lookup the container we want the Grid to use
var salesGridDiv = document.querySelector('#salesGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(salesGridDiv, salesGridOption);

var subTotSum;
function setSalesRow(result) {
	salesRow = [];
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, gbn:result[i].gbn
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
			, dlv_dt:result[i].dlv_dt
			, sales_no:result[i].sales_no
			, prd_nm:result[i].prd_nm
			, prd_cd:result[i].prd_cd
			, spec:result[i].spec
			, entry_no:result[i].entry_no
			, qty:result[i].qty
			, dlv_prc:result[i].dlv_prc
			, tax_rt:result[i].tax_rt
			, tot:result[i].tot
			, cnsp_tax:result[i].cnsp_tax
			, sub_tot:result[i].sub_tot
			, mid_tot:result[i].mid_tot
			, bill_no:result[i].bill_no
			, pay_method:result[i].pay_method
			, pay_comp_yn:result[i].pay_comp_yn
			, manager:result[i].manager
			, mail:result[i].mail
			, memo:result[i].memo
		};
		salesRow.push(row);
	}
	salesGridOption.api.setRowData(salesRow);
	var totSum = new Number();
	subTotSum = new Number();
	salesGridOption.api.forEachNode( function(rowNode, index) {
        totSum += new Number(rowNode.data.tot);
        subTotSum += new Number(rowNode.data.sub_tot);
    });
	
	$('#subTot').val('￥'+formatNumber(totSum));
	$('#cnspTax').val('￥'+formatNumber(subTotSum-totSum));
	$('#midTot').val('￥'+formatNumber(subTotSum));
	
	modifiedData = []; // 수정데이터 초기화
}


/**
 * 상품선택 모달 S
 */

var prdColDef = [
	{headerName: "商品名", field: "prd_nm", width: 400
		, tooltip:function(param) {
			return param.value;
		}
	}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200}
];

// specify the data
var prdRow = [];

//let the grid know which columns and what data to use
var prdInfoGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100,
		headerCheckboxSelection: isFirstColumn,
		checkboxSelection: isFirstColumn
	},
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: prdColDef,
	rowData: prdRow
};

// lookup the container we want the Grid to use
var prdInfoGridDiv = document.querySelector('#prdInfoGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdInfoGridDiv, prdInfoGridOptions);

function setPrdRow(result) {
	prdRow = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, tax_rt:result[i].tax_rt
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
			, prd_cd:result[i].prd_cd
		};
		prdRow.push(row);
	}
	prdInfoGridOptions.api.setRowData(prdRow);
}

$("#btn_srch").on("click", function() {
	$.ajax({
		url: "/jaiko/prdInfo/getPrdInfo"
		, type:"get"
		, data: $("#frm_srch").serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setPrdRow
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
	modifiedData.push({seq_id:thisSeq
		, partner_id:thisPartner, partner_nm: thisPartNm
		, tax_rt: row.tax_rt, prd_nm:row.prd_nm, prd_cd:row.prd_cd
		, jan_cd:row.jan_cd, dlv_dt:thisDt, gbn:'SEL'});
	$.ajax({
	    url: "/jaiko/sales/data/upd"
		, type:"post"	
	    , data: JSON.stringify(modifiedData)
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setSalesRow
	});
	$('#srchPrd').modal('hide');
});

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

/**
 * 상품선택 모달 E
 */