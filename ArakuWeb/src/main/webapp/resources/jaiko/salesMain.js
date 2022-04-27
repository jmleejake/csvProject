/**
 * 売上一覧
 */

/*
 * 검색 S
 * */
$(document).ready(function() {
	$('#startDate').datepicker({
		language: "ja"
	    ,format: "yyyy-mm-dd"
	    ,todayHighlight: true
	    ,autoclose: true
	// update "toDate" defaults whenever "fromDate" changes
	}).on('changeDate', function(){
	    // set the "toDate" start to not be later than "fromDate" ends:
	    $('#endDate').val("").datepicker("update");
	    $('#endDate').datepicker('setStartDate', new Date($(this).val()));
	});

	$('#endDate').datepicker({
		language: "ja"
	    ,format: "yyyy-mm-dd"
	    ,todayHighlight: true
	    ,autoclose: true
	});
});

function showOrder(id, nm) {
	$('input[name=partner_id]').val(id);
	$('#selectedDealer').html(': '+nm);
}

$('#btn_srch').on('click', function() {
	$('input[name=from_dt]').val($('#startDate').val().replaceAll('-','')+'000000');
	$('input[name=to_dt]').val($('#endDate').val().replaceAll('-','')+'235959');
	$.ajax({
	    url: "/jaiko/sales/main/srch"
	    , data: $('#frm').serialize()
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setSalesRow
	});
});

/*
 * 검색 E
 * */

var salesColDefs = [
	{headerName:"売上番号", width:90, field:"bill_no", cellRenderer:'goDetail'}
	, {headerName: "売上日", width: 110, field:"dlv_dt"}
	, {headerName: "得意先", field: "partner_nm", width: 300}
	, {headerName: "売上合計金額", field: "mid_tot", width: 200, valueFormatter: currencyFormatter}
	, {headerName: "担当", field: "manager", width: 200}
	, {headerName: "操作", width: 80, cellRenderer:'dataDel'}
	, {headerName: "メール", field: "mail", width: 300}
];

//specify the data
var salesRow = [];

//let the grid know which columns and what data to use
var salesGridOption = {
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: salesColDefs,
		rowData: salesRow,
		components: {
			dataDel: function(param) {
				var html = '<button type="button" class="btn btn-danger btn-sm" onclick="fnDel('+param.data.seq_id+','+param.data.partner_id+',\''+param.data.dlv_dt+'\')">';
				html += '<span class="glyphicon glyphicon-trash"></span> ';
				html += '</button>';
				return html;
			}
			, goDetail: function(param) {
				var html = '<button type="button" class="btn btn-info btn-sm" onclick="fnMove('+param.data.partner_id+',\''+param.data.dlv_dt+'\')">';
				html += param.data.bill_no+'<span class="glyphicon glyphicon-triangle-right"></span> ';
				html += '</button>';
				return html;
			}
		},
	    onCellEditingStarted: function(event) {
	    },
	    onCellEditingStopped: function(event) {
	    }
};

function fnDel(id, part, dt) {
	var delObj = [{seq_id:id, partner_id:part, dlv_dt:dt, gbn:'TOT'}]
	alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
				url: "/jaiko/sales/data/del"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(delObj)
				, success: setSalesRow
			});
		} else {
			return false;
		}
	});
}

function fnMove(part, dt) {
	document.location.href = '/jaiko/sales/detail?partner_id='+part+'&dt='+dt;
}

function currencyFormatter(param) {
	return '￥'+formatNumber(param.value);
}

function formatNumber(number) {
    return Math.floor(number)
      .toString()
      .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
}

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
}