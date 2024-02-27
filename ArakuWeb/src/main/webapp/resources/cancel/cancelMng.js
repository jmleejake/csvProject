/**
 * キャンセル管理 javascript
 */

var statusColumn = [
    {headerName: "注文番号", field: "order_no", width: 300, editable: true},
    {headerName: "区分", field: "order_gbn", width: 100, editable: true},
    {headerName: "JANコード", field: "jan_cd", width: 200, editable: true},
    {
        headerName: "置換前商品名", field: "before_trans", width: 300, editable: true,
        cellEditor: 'agLargeTextCellEditor',
        cellEditorParams: {
            maxLength: '500', cols: '50', rows: '6'
        },
        // tooltipValueGetter 사용
        tooltipValueGetter: function(params) {
            return params.value;
        }
    },
    {
        headerName: "置換後商品名", field: "after_trans", width: 300, editable: true,
        cellEditor: 'agLargeTextCellEditor',
        cellEditorParams: {
            maxLength: '500', cols: '50', rows: '6'
        },
        // tooltipValueGetter 사용
        tooltipValueGetter: function(params) {
            return params.value;
        }
    },
    {headerName: "商品数", field: "prd_cnt", width: 200, editable: true, valueFormatter: currencyFormatter},
    {headerName: "お荷物伝票番号", field: "baggage_claim_no", width: 500, editable: true, valueFormatter: currencyFormatter}
];

// specify the data
var statusRowData = [];
// 수정데이터 배열
var statusModData = [];
var sOrderNo, eOrderNo;
var sOrderGbn, eOrderGbn;
var sJanCd, eJanCd;
var sBeforeTrans, eBeforeTrans;
var sAfterTrans, eAfterTrans;
var sPrdPrc, ePrdPrc;
var sBaggageClaimNo, eBaggageClaimNo;

// let the grid know which columns and what data to use
var statusGridOptions = {
		defaultColDef: {
			width: 100
			, headerCheckboxSelection: isFirstColumn
			, checkboxSelection: isFirstColumn
			, resizable: true
		},
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: statusColumn,
		rowData: statusRowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	sOrderNo=start.order_no;
	    	sOrderGbn=start.order_gbn;
			sJanCd=start.jan_cd;
			sBeforeTrans=start.before_trans;
			sAfterTrans=start.after_trans;
			sPrdCnt=start.prd_cnt;
			sBaggageClaimNo=start.baggage_claim_no;

	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	eOrderNo=stop.order_no;
	    	eOrderGbn=stop.order_gbn;
			eJanCd=stop.jan_cd;
			eBeforeTrans=stop.before_trans;
			eAfterTrans=stop.after_trans;
			ePrdCnt=stop.prd_cnt;
			eBaggageClaimNo=stop.baggage_claim_no;
	    	
	    	if (!(sOrderNo == eOrderNo)||!(sOrderGbn == eOrderGbn)||!(sJanCd == eJanCd)||!(sBeforeTrans == eBeforeTrans)
	    			||!(sAfterTrans == eAfterTrans) ||!(sBaggageClaimNo == eBaggageClaimNo)||!(sPrdCnt == ePrdCnt)) {
	    		statusModData.push(stop);
	    	}
	    }
};

// lookup the container we want the Grid to use
var statusGridDiv = document.querySelector('#statusGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(statusGridDiv, statusGridOptions);

function currencyFormatter(param) {
	return formatNumber(param.value);
}

function formatNumber(number) {
    return Math.floor(number)
      .toString();
}

function setRowData(result) {
	statusRowData = [];
	statusModData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, register_date:result[i].register_date
				, update_date:result[i].update_date		
				, order_no: result[i].order_no
				, order_gbn: result[i].order_gbn
				, jan_cd: result[i].jan_cd
				, before_trans: result[i].before_trans
				, after_trans: result[i].after_trans
				, prd_cnt: result[i].prd_cnt
				, baggage_claim_no: result[i].baggage_claim_no
		}
		
		statusRowData.push(row);
	}
	// ag-Gridに新しいデータを追加する。
    statusGridOptions.api.updateRowData({add: statusRowData});
	//statusGridOptions.api.setRowData(statusRowData);
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$("#btn_srch").on("click", function() {
	search();
	$("#bgc_no").val("");
	$("#bgc_no").focus();
});

function search() {
    var bgc_no = $("#bgc_no").val(); // 検索パラメータを取得

    $.ajax({
        type: "GET", // GET 요청으로 변경
        url: "showCancelManage",
        dataType: "json",
        data: {bgc_no: bgc_no}, // 데이터를 쿼리 파라미터로 전달
        success: setRowData,
            error: function(xhr, status, error) {
        		console.error("Error occurred: " + status + ", " + error);
    	}
    });
}

// propertychange change keyup paste input
$("#bgc_no").on("change paste", function() {
	baggageClaimNo = $("#bgc_no").val();
	//search();
	//$("#bgc_no").val("");
	$("#bgc_no").focus();
});

$("#btn_jaikosuhanei").on("click", function() {
	var selectedRows = statusGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }

    var cancel_lst = [];
	
	// 각 selectedRows의 항목을 JSON 객체로 변환하여 추가합니다.
    for (var i = 0; i < selectedRows.length; i++) {
    	var cancelData = {
	        seq_id: selectedRows[i].seq_id, // 필드 이름이 "seqId"인지 확인하세요.
	        register_date: selectedRows[i].register_date,
	        update_date: selectedRows[i].update_date,
	        order_no: selectedRows[i].order_no,
	        order_gbn: selectedRows[i].order_gbn,
	        jan_cd: selectedRows[i].jan_cd,
	        before_trans: selectedRows[i].before_trans,
	        after_trans: selectedRows[i].after_trans,
	        prd_cnt: selectedRows[i].prd_cnt,
	        baggage_claim_no: selectedRows[i].baggage_claim_no
	    };
	    cancel_lst.push(cancelData);
    }

	$.ajax({
		url: "jaikoHanei"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(cancel_lst)
		, success: function(res) {
	    	if("S" === res.retCd) {
	    		alert("反映完了しました。");
	    	}else {
	    		alert(res.retMsg);
	    	}
	    }
	});
});
