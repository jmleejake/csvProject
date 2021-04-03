/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName: "商品コード", field: "prd_cd", width: 200, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
	, {headerName: "ブランド", field: "brand_nm", width: 200, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "商品名", field: "prd_nm", width: 400
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
	, {headerName: "入数", field: "prd_cnt", width: 100, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
	, {headerName: "単価", field: "prd_unit_prc", width: 100, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
	, {headerName: "商品税(抜、込)", field: "tax_incld", width: 100, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
	, {headerName: "税率", field: "tax_rt", width: 100, editable: true
    	, cellEditor: 'agPopupTextCellEditor'}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var modifiedData = [];
var prevPrdCd, afterPrdCd;
var prevBrandNm, afterBrandNm;
var prevPrdNm, afterPrdNm;
var prevJanCd, afterJanCd;
var prevPrdCnt, afterPrdCnt;
var prevPrdUnitPrc, afterPrdUnitPrc;
var prevTaxIncld, afterTaxIncld;
var prevTaxRt, afterTaxRt;

// let the grid know which columns and what data to use
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
	columnDefs: columnDefs,
	rowData: rowData,
	rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	},
    	'trans-error' : function(params) {
    		var target = params.data.err_text;
    		return target === 'ERR';
    	}
    },
    onCellEditingStarted: function(event) {
        var previousData = event.node.data;
        prevPrdCd = previousData.prd_cd;
        prevBrandNm = previousData.brand_nm;
        prevPrdNm = previousData.prd_nm;
        prevJanCd = previousData.jan_cd;
        prevPrdCnt = previousData.prd_cnt;
        prevPrdUnitPrc = previousData.prd_unit_prc;
        prevTaxIncld = previousData.tax_incld;
        prevTaxRt = previousData.tax_rt;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterPrdCd = afterData.prd_cd;
        afterBrandNm = afterData.brand_nm;
        afterPrdNm = afterData.prd_nm;
        afterJanCd = afterData.jan_cd;
        afterPrdCnt = afterData.prd_cnt;
        afterPrdUnitPrc = afterData.prd_unit_prc;
        afterTaxIncld = afterData.tax_incld;
        afterTaxRt = afterData.tax_rt;
        
        if (!(prevPrdCd == afterPrdCd) || 
        	!(prevBrandNm == afterBrandNm) ||
        	!(prevPrdNm == afterPrdNm) ||
        	!(prevJanCd == afterJanCd) ||
        	!(prevPrdCnt == afterPrdCnt) ||
        	!(prevPrdUnitPrc == afterPrdUnitPrc) ||
        	!(prevTaxIncld == afterTaxIncld) ||
        	!(prevTaxRt == afterTaxRt)) {
        	console.log("modified!");
        	modifiedData.push({
        		seq_id:afterData.seq_id
				, prd_cd:afterPrdCd
				, brand_nm:afterBrandNm
				, prd_nm:afterPrdNm
				, jan_cd:afterJanCd
				, prd_cnt:afterPrdCnt
				, prd_unit_prc:afterPrdUnitPrc
				, tax_incld:afterTaxIncld
				, tax_rt:afterTaxRt
        	});
        }
    }
};

// lookup the container we want the Grid to use
var prdInfoGridDiv = document.querySelector('#prdInfoGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdInfoGridDiv, prdInfoGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$.ajax({
    url: "/jaiko/prdInfo/getPrdInfo"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData
});

/*
------------------
grid setting E
------------------
*/


/*
------------------
button action S
------------------
*/
function setRowData(result) {
	rowData = [];
	
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
		};
		rowData.push(row);
	}
	prdInfoGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
	$("#jan_cd").val(""); // 바코드번호 (jan_cd)
	$("#prd_cd").val(""); // 상품코드
	$("#prd_nm").val(""); // 상품명
}

$("#btn_add").on("click", function() {
	console.log("列追加");
	modifiedData.push({
		prd_cd: "商品コード"
			, brand_nm: "ブランド"
			, prd_nm: "商品名"
			, jan_cd: "ＪＡＮコード"
			, prd_cnt: "0"
			, prd_unit_prc: "0"
			, tax_incld: "0"
			, tax_rt: "0"
	});
	$.ajax({
		url: "/jaiko/prdInfo/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
	//prdInfoGridOptions.api.updateRowData({add:[data], addIndex:0});
});

$("#btn_commit").on("click", function() {
	console.log("登録");
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "/jaiko/prdInfo/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$("#btn_delete").on("click", function() {
	console.log("削除");
	var selectedRows = prdInfoGridOptions.api.getSelectedRows();
	
	if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "/jaiko/prdInfo/delete"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData
			});
		} else {
			return false;
		}
	});
});

$("#btn_search").on("click", function() {
	$.ajax({
		url: "/jaiko/prdInfo/getPrdInfo"
		, type:"get"
		, data: $("#frm_srch").serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setRowData
	});
});


/*
------------------
button action E
------------------
*/
