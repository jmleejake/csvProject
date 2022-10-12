/**
 * javascript for 注文情報
 */
$(function() {
	srch();
});

// 수정데이터 배열
var modData = [];
class ColAsDatePicker {
    init(params) {
    	console.log(params);
		// create the cell
		this.eGui = document.createElement('input');
		$(this.eGui).addClass('form-control');
		$(this.eGui).css('width','100px');
		$(this.eGui).css('height','22px');
		$(this.eGui).val(params.value);
		$(this.eGui).datepicker({
			language: "ja"
			 ,format: "yyyy/mm/dd"
			 ,todayHighlight: true
			 ,autoclose: true
		}).on('changeDate', function(){
			modData.push({seq_id: params.data.seq_id, register_date: $(this).val()});
			$.ajax({
				url: "modTanpin"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(modData)
				, success: function(result){
					setRowData(result);
					// 수정데이터 초기화
					modData = [];
		    	}
			});
		});
    }

    getGui() {
    	return this.eGui;
    }

    // gets called whenever the cell refreshes
    refresh(params) {
    	return true;
    }

    // gets called when the cell is removed from the grid
    destroy() {
    }

 }

var columnDefs = [
	{headerName: "商品メーカー名", field: "maker_nm", width: 200
		, resizable: true
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }		
	}
	, {headerName: "商品コード(JAN)", field: "prd_cd", width: 150, editable: true, resizable: true}
	, {headerName: "商品名", field: "prd_nm", width: 300
		, resizable: true
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }	
	}
	, {headerName: "容量", field: "capacity", width: 100, editable: true, resizable: true}
	, {headerName: "取引先会社名", field: "dealer_nm", width: 200
		, resizable: true
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }		
	}
	, {headerName: "仕入金額", field: "inprice", width: 100, editable: true, resizable: true}
	, {headerName: "販売金額", field: "price", width: 100, editable: true, resizable: true}
	, {headerName: "商品メーカー", field: "maker_cd", width: 80, editable: true, resizable: true}
	, {headerName: "取引先コード", field: "dealer_id", width: 80, editable: true, resizable: true}
	, {headerName: "登録日", field: "register_date", width: 150, editable: true, resizable: true, cellRenderer:'regDtFrm'} 
	, {headerName: "メモ", field: "memo", width: 80, editable: true, resizable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }
	}
];

// specify the data
var rowData = [];
var sMakerNm, eMakerNm;
var sPrdNm, ePrdNm;
var sCapa, eCapa;
var sDealerNm, eDealerNm;
var sInPrc, eInPrc;
var sPrc, ePrc;
var sMakerCd, eMakerCd;
var sPrdCd, ePrdCd;
var sDealerCd, eDealerCd;
var sMemo, eMemo;
var sRegDt, eRegDt;

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
	rowData: rowData,
	components: {
		regDtFrm: ColAsDatePicker
	},
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
    	var start = event.node.data;
    	sMakerNm = start.maker_nm;
    	sPrdNm = start.prd_nm;
    	sCapa = start.capacity;
    	sDealerNm = start.dealer_nm;
    	sInPrc = start.inprice;
    	sPrc = start.price;
    	sMakerCd = start.maker_cd;
    	sPrdCd = start.prd_cd;
    	sDealerCd = start.dealer_id;
    	sMemo = start.memo;
    	sRegDt = start.register_date;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	eMakerNm = stop.maker_nm;
    	ePrdNm = stop.prd_nm;
    	eCapa = stop.capacity;
    	eDealerNm = stop.dealer_nm;
    	eInPrc = stop.inprice;
    	ePrc = stop.price;
    	eMakerCd = stop.maker_cd;
    	ePrdCd = stop.prd_cd;
    	eDealerCd = stop.dealer_id;
    	eMemo = stop.memo;
    	eRegDt = stop.register_date;
    	
    	if (!(sMakerNm == eMakerNm)||!(sPrdNm == ePrdNm)
    			||!(sCapa == eCapa)||!(sDealerNm == eDealerNm)
    			||!(sInPrc == eInPrc)||!(sMakerCd == eMakerCd)
    			||!(sPrdCd == ePrdCd)||!(sDealerCd == eDealerCd)
    			||!(sInPrc == eInPrc)||!(sMakerCd == eMakerCd)||!(sPrc == ePrc)
    			||!(sMemo == eMemo)||!(sRegDt == eRegDt)) {
    		modData.push(stop);
    	}
    }
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
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, memo: result[i].memo
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
	srch();
});

function srch() {
	var form = $("#frm");
    
    $.ajax({
        type: "get"
        , url: "getTanpin"
        , dataType: "json"
        , contentType: 'application/json'
        , data: form.serialize() // serializes the form's elements.
        , success: setRowData
    });
}

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

$('#btn_create').on('click', function() {
	var rowData = {
			maker_nm: "商品メーカー名"
			,prd_nm:"商品名"
			,capacity:"0"
			,dealer_nm:"取引先会社名"
			,inprice:"0"
			,price:"0"
			,maker_cd:"商品メーカー"
			,prd_cd:"商品コード"
			,dealer_id:"取引先コード"
	};
	//orderGridOptions.api.updateRowData({add:[rowData], addIndex:0});
	$.ajax({
        type: "post"
        , url: "addTanpin"
    	, data: rowData
        , success: setRowData
    });
});

$('#btn_commit').on('click', function() {
	if (modData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modTanpin"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modData)
		, success: function(result){
			setRowData(result);
			// 수정데이터 초기화
			modData = [];
    	}
	});
});

$('#btn_delete').on('click', function() {
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
