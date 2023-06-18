/**
 * javascript for 注文情報
 */
$(function() {
	srch();
	
	$("#end_date").attr("disabled", "disabled");
	$("#start_date").on("change", function() {
		var empty = $("#start_date").val() == "";
		if(empty) {
			$("#end_date").val("");
			$("#end_date").attr("disabled", "disabled");
		} else {
			$("#end_date").removeAttr("disabled");
		}
	});
});

// 수정데이터 배열
var modData = [];
class ColAsDatePicker {
    init(params) {
    	console.log('datePicker');
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
				url: "modExpMng"
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

class ColAsDatePicker2 {
    init(params) {
    	console.log('datePicker2');
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
			modData.push({seq_id: params.data.seq_id, exp_dt: $(this).val()});
			$.ajax({
				url: "modExpMng"
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
	{headerName: "ＪＡＮコード", field: "jan_cd", width: 150, editable: true, resizable: true}
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
	, {headerName: "賞味期限", field: "exp_dt", width: 150, editable: true, resizable: true, cellRenderer:'expDtFrm'}
	, {headerName: "取引先会社名", field: "partner_nm", width: 200
		, resizable: true
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '30',
            rows: '6'
        }		
	}
	, {headerName: "取引先コード", field: "partner_id", width: 80, editable: true, resizable: true}
	, {headerName: "入庫数", field: "prd_qty", width: 60, editable: true, resizable: true}
	, {headerName: "登録日", field: "register_date", width: 150, editable: true, resizable: true, cellRenderer:'regDtFrm'} 
];

// specify the data
var rowData = [];
var sPrdNm, ePrdNm;
var sPartnerNm, ePartnerNm;
var sJanCd, eJanCd;
var sPartnerCd, ePartnerCd;
var sRegDt, eRegDt;
var sPrdQty, ePrdQty;

// let the grid know which columns and what data to use
var orderGridOptions = {
	defaultColDef: {
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	components: {
		regDtFrm: ColAsDatePicker
		, expDtFrm: ColAsDatePicker2
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
		/*
		var sPrdNm, ePrdNm;
var sPartnerNm, ePartnerNm;
var sJanCd, eJanCd;
var sPartnerCd, ePartnerCd;
var sRegDt, eRegDt;
var sPrdQty, ePrdQty;
		 * */
    	var start = event.node.data;
    	sPrdNm = start.prd_nm;
    	sPartnerNm = start.partner_nm;
    	sJanCd = start.jan_cd;
    	sPartnerCd = start.partner_id;
    	sRegDt = start.register_date;
    	sPrdQty = start.prd_qty;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	ePrdNm = stop.prd_nm;
    	ePartnerNm = stop.partner_nm;
    	eJanCd = stop.jan_cd;
    	ePartnerCd = stop.partner_id;
    	eRegDt = stop.register_date;
    	ePrdQty = stop.prd_qty;
    	
    	if (!(sPrdNm == ePrdNm)||!(sPartnerNm == ePartnerNm)
    			||!(sJanCd == eJanCd)||!(sPartnerCd == ePartnerCd)
    			||!(sRegDt == eRegDt)||!(sPrdQty == ePrdQty)) {
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
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, jan_cd: result[i].jan_cd
				, prd_nm: result[i].prd_nm
				, partner_id: result[i].partner_id
				, partner_nm: result[i].partner_nm
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, prd_qty: result[i].prd_qty
				, exp_dt: result[i].exp_dt
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

$("#btn_srch").on("click", function() {
	srch();
});

function srch() {
	var form = $("#frm");
    
    $.ajax({
        type: "get"
        , url: "getExpMng"
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
			jan_cd:"ＪＡＮコード"
			,prd_nm:"商品名"
			,partner_id:"取引先コード"
			,partner_nm:"取引先会社名"
	};
	//orderGridOptions.api.updateRowData({add:[rowData], addIndex:0});
	$.ajax({
        type: "post"
        , url: "addExpMng"
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
		url: "modExpMng"
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
					url: "delExpMng"
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
