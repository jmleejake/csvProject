/**
 * 発注書
 */

// 거래처 셀렉트박스 선택시
function showOrder(id, nm) {
	$('#selectedDealer').html(': '+nm);
	$('#down_id').val(id);
	$.ajax({
		url: "/jaiko/prdInven/getPrdInven"
		, type:"get"
		, data: {
			search_type:'srch'
			,dealer_id:id
		}
		, dataType: "json"
		, contentType: 'application/json'
		, success: setRowData
	});
}

$('#btn_down').on('click', function() {
	$('#frm_down').submit();
});

/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName: "商品コード", field: "prd_cd", width: 200, editable: true}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200, editable: true}
	, {headerName: "取引先コード", field: "dealer_id", width: 200, editable: true}
	, {headerName: "取引先会社名", field: "dealer_nm", width: 200, editable: true}
	, {headerName: "ブランド", field: "brand_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 400
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }
	}
	, {headerName: "入数", field: "prd_qty", width: 200}
	, {headerName: "ケース数", field: "prd_case", width: 250}
	, {headerName: "バラ数", field: "prd_bara", width: 250}
	, {headerName: "ロート数", field: "prd_lot", width: 250}
	, {headerName: "現在商品数", field: "now_prd_cnt", width: 250}
	, {headerName: "賞味期限", field: "exp_dt", width: 250}
	, {headerName: "本体売価", field: "sell_prc", width: 250}
];

// specify the data
var rowData = [];

//수정데이터 배열
var modifiedData = [];
var prevPrdCd, afterPrdCd;
var prevJanCd, afterJanCd;
var prevBrandNm, afterBrandNm;
var prevPrdNm, afterPrdNm;
var prevPrdQty, afterPrdQty;
var prevPrdCase, afterPrdCase;
var prevPrdBara, afterPrdBara;
var prevNowPrdCnt, afterNowPrdCnt;
var prevExpDt, afterExpDt;
var prevSellPrc, afterSellPrc;
var prevPrdLot, afterPrdLot;
var prevDlrId, afterDlrId;
var prevDlrNm, afterDlrNm;

//let the grid know which columns and what data to use
var prdInvenGridOptions = {
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
    components: {
    	//datePicker: getDatePicker(),
    },
    onCellEditingStarted: function(event) {
        var previousData = event.node.data;
        prevJanCd = previousData.jan_cd;
        prevPrdCd = previousData.prd_cd;
        prevBrandNm = previousData.brand_nm;
        prevPrdNm = previousData.prd_nm;
        prevPrdQty = previousData.prd_qty;
        prevPrdCase = previousData.prd_case;
        prevPrdBara = previousData.prd_bara;
        prevExpDt = previousData.exp_dt;
        prevSellPrc = previousData.sell_prc;
        prevNowPrdCnt = previousData.now_prd_cnt; 
        prevPrdLot = previousData.prd_lot;
        prevDlrId = previousData.dealer_id;
        prevDlrNm = previousData.dealer_nm;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterJanCd = afterData.jan_cd;
        afterPrdCd = afterData.prd_cd;
        afterBrandNm = afterData.brand_nm;
        afterPrdNm = afterData.prd_nm;
        afterPrdQty = afterData.prd_qty;
        afterPrdCase = afterData.prd_case;
        afterPrdBara = afterData.prd_bara;
        afterExpDt = afterData.exp_dt;
        afterSellPrc = afterData.sell_prc;
        afterNowPrdCnt = afterData.now_prd_cnt;
        afterPrdLot = afterData.prd_lot;
        afterDlrId = afterData.dealer_id;
        afterDlrNm = afterData.dealer_nm;
        
        if (!(prevBrandNm == afterBrandNm) ||
        	!(prevPrdNm == afterPrdNm) ||
        	!(prevJanCd == afterJanCd) ||
        	!(prevPrdCd == afterPrdCd) ||
        	!(prevNowPrdCnt == afterNowPrdCnt) ||
        	!(prevPrdQty == afterPrdQty) ||
        	!(prevPrdCase == afterPrdCase) ||
        	!(prevPrdBara == afterPrdBara) ||
        	!(prevExpDt == afterExpDt) ||
        	!(prevSellPrc == afterSellPrc)||
        	!(prevPrdLot == afterPrdLot)||
        	!(prevDlrId == afterDlrId)||
        	!(prevDlrNm == afterDlrNm)) {
        	modifiedData.push(afterData);
        }
    }
};

//lookup the container we want the Grid to use
var prdInvenGridDiv = document.querySelector('#orderGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdInvenGridDiv, prdInvenGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$.ajax({
    url: "/jaiko/prdInven/getPrdInven"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_cd:result[i].prd_cd
			, brand_nm:result[i].brand_nm
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, now_prd_cnt:result[i].now_prd_cnt
			, prd_qty:result[i].prd_qty
			, prd_case:result[i].prd_case
			, prd_bara:result[i].prd_bara
			, exp_dt:result[i].exp_dt
			, sell_prc:result[i].sell_prc
			, prd_lot:result[i].prd_lot
			, dealer_id:result[i].dealer_id
			, dealer_nm:result[i].dealer_nm
			, register_date:result[i].reg_dt
			, update_date:result[i].upd_dt
		};
		rowData.push(row);
	}
	prdInvenGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

/*
------------------
grid setting E
------------------
*/