/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName: "ブランド", field: "brand_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 400}
	, {headerName: "入数", field: "prd_cnt", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "単価", field: "prd_unit_prc", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "商品税(抜、込)", field: "tax_incld", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "税率", field: "tax_rt", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200}
	, {headerName: "商品コード", field: "prd_cd", width: 200}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var modifiedData = [];
var prevPrdCnt, afterPrdCnt;
var prevPrdUnitPrc, afterPrdUnitPrc;
var prevTaxIncld, afterTaxIncld;
var prevTaxRt, afterTaxRt;

// let the grid know which columns and what data to use
var prdWareOutGridOptions = {
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
        prevPrdCnt = previousData.prd_cnt;
        prevPrdUnitPrc = previousData.prd_unit_prc;
        prevTaxIncld = previousData.tax_incld;
        prevTaxRt = previousData.tax_rt;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterPrdCnt = afterData.prd_cnt;
        afterPrdUnitPrc = afterData.prd_unit_prc;
        afterTaxIncld = afterData.tax_incld;
        afterTaxRt = afterData.tax_rt;
        
        console.log("modified!");
        if(!(prevPrdCnt == afterPrdCnt)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_cnt:afterPrdCnt
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdUnitPrc == afterPrdUnitPrc)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_unit_prc:afterPrdUnitPrc
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevTaxIncld == afterTaxIncld)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, tax_incld:afterTaxIncld
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevTaxRt == afterTaxRt)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, tax_rt:afterTaxRt
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }
    }
};

// lookup the container we want the Grid to use
var prdWareOutGridDiv = document.querySelector('#prdWareOutGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdWareOutGridDiv, prdWareOutGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

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
var janCd;
function setRowData(result) {
	rowData = [];
	
	if(result.length < 1) {
		alert(janCd + "\n登録されていないコードです。");
		return false;
	}
	
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
	prdWareOutGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

$("#btn_commit").on("click", function() {
	console.log("登録");
	if(modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	if($("#partner_id").text() === "") {
		pleaseSelectNotify('取引先を選択してください。');
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

/*
------------------
button action E
------------------
*/


/*
------------------
 propertychange change keyup paste input S
------------------
*/

function search() {
    $.ajax({
    url: "/jaiko/prdInfo/getPrdInfo"
    , dataType: "json"  
    , contentType : "application/json"
    , data: {
    jan_cd: $("#jan_cd").val()
    , search_type: 'srch'
    }
    , success: setRowData
});
}

$("#jan_cd").on("change paste", function() {
	janCd =  $("#jan_cd").val();
	search();
	$("#jan_cd").val("");
	$("#jan_cd").focus();
});

/*
------------------
 propertychange change keyup paste input E
------------------
*/
