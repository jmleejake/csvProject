/**
 * javascript for 商品番号管理マスタ
 */
// specify the columns
var columnDefs3 = [
	{headerName: "商品ID", field: "prd_cd", width: 200
		, editable: true
	},
	{headerName: "区分", field: "target_type", width: 200
		, cellRenderer: function(params) {
	    	var img;
	    	if("R" === params.value) {
	    		img = "./../resources/img/rakuten.png";
	    	}else if("A" === params.value) {
	    		img = "./../resources/img/amazon.png";
	    	}else if("Q" === params.value) {
	    		img = "./../resources/img/qoo10.png";
	    	}
	    	return "<img src='"+img+"' style='width:130px; height:43px; padding-bottom:10px;'/>";
	    }
	}
];

//rowData 초기화
var rowData3 = [];

//수정 전후를 파악할 변수 선언
var startPrdCd, stopPrdCd;

//수정데이터 배열
var modifiedData3 = [];

var gridOption3 = {
	// 첫번째 컬럼 체크박스 세팅
	defaultColDef: {
        width: 100,
        headerCheckboxSelection: isFirstColumn,
        checkboxSelection: isFirstColumn
    },
	enableColResize: true,
    suppressRowClickSelection: false,
    rowSelection: 'multiple',
	columnDefs: columnDefs3,
	rowData: rowData3,
	onCellEditingStarted: function(event) {
    	var start = event.node.data;
    	startPrdCd = start.prd_cd;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	stopPrdCd = stop.prd_cd;
    	
    	id3 = stop.seq_id;
    	if (!(startPrdCd == stopPrdCd)||!(startJanCd == stopJanCd)) {
    		console.log("modified!");
    		modifiedData3.push({
    			seq_id: id3
    			, prd_cd: stopPrdCd
    			, target_type: globalGbn
    		});
    	}
    }
};

//lookup the container we want the Grid to use
var eGridDiv3 = document.querySelector('#prdCdGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv3, gridOption3);

var globalGbn;
function showPrdCdMaster(gbn) {
	globalGbn = gbn;
	$.ajax({
	    url: "getPrdCdMaster"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{target_type: gbn}
	    , success: setRowData3
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setRowData3(result) {
	rowData3 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, prd_cd: result[i].prd_cd
				, target_type: result[i].target_type
		}
		
		rowData3.push(row);
	}
	gridOption3.api.setRowData(rowData3);
}

$("#btn_prd_commit").on("click", function() {
	if (modifiedData3.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "maniPrdCdMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData3)
		, success: function(result){
			setRowData3(result);
			// 수정데이터 초기화
			modifiedData3 = [];
    	}
	});
});

var prdSearch = function() {
	var form = $("#prdFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "get",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData3
    });
}

$('#btn_prd_srch').on('click', function() {
	prdSearch();
});

$("#btn_prd_add").on("click", function() {
	var rowData = {};
	gridOption3.api.updateRowData({add:[rowData], addIndex:0});
	
	modifiedData3.push({
		prd_cd: ''
		, target_type: globalGbn
	});
	
	$.ajax({
		url: "maniPrdCdMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData3)
		, success: function(result){
			setRowData3(result);
			// 수정데이터 초기화
			modifiedData3 = [];
    	}
	});
});

$("#btn_prd_delete").on("click", function() {
	var selectedRows = gridOption3.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delPrdMaster"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData3
			});
		} else {
			return false;
		}
	});
});