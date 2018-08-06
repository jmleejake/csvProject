/**
 * javascript for 注文情報
 */
// specify the columns
var columnDefs = [
    {headerName: "Make", field: "make", width: 120
		, cellStyle: {cursor: 'pointer'}
		, cellRenderer: function(params) {
		        return '<a onclick="getInfo('+params.node.data.id+');">'+params.value+'</a>';
		}
    }
    , {headerName: "Model", field: "model", width: 120, editable: true}
    , {headerName: "Price", field: "price", width: 120}
    , {headerName: "Image", field: "image", width: 120
		, cellStyle: {cursor: 'pointer'}
		, cellRenderer: function(params) {
				console.log(params.value);
		        //return '<a onclick="getInfo('+params.node.data.id+');">'+params.value+'</a>';
				return '<img src="./resources/img/'+params.value+'.png" alt="logo" style="width:50px;">';
		}
	}
];

// specify the data
var rowData = [
    {make: "Toyota", model: "Celica", price: 35000, id:11, image:"fastDelivery"},
    {make: "Ford", model: "Mondeo", price: 32000, id:22, image:"fastDelivery2"},
    {make: "Porsche", model: "Boxter", price: 72000, id:33, image:"fastDelivery3"}
];

var previousData, afterData;
var modifiedData = [];

// let the grid know which columns and what data to use
var orderGridOptions = {
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
//    onRowSelected: onRowSelected,
    rowData: rowData,
    onCellEditingStarted: function(event) {
        previousData = event.node.data.model;
    },
    onCellEditingStopped: function(event) {
        afterData = event.node.data.model;

        console.log("previous : " + previousData);
        console.log("after : " + afterData);
        if (!(previousData == afterData)) {
                console.log("modified!");
                modifiedData.push(event.node.data);
                console.log(modifiedData);
        }
    }
};

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function onRowSelected(event) {
	console.log("row selected");
	console.log(event.node.data);
}

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#orderGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);


$("#btn_mod").on("click", function() {
    if (modifiedData.length == 0) {
            alert("no modified data!");
            return;
    }

    var param = [];
    for (var i=0; i<modifiedData.length; i++) {
            var data = modifiedData[i];
            param.push({"id":data.id, "make":data.make});
    }

    $.ajax({
            url: "dataMod"
            , dataType: "json"      
            , contentType : "application/json"
            , data:{data:JSON.stringify(param)}
            , success: function(result){
                    console.log(result);
    }
    });
});

$("#btn_trans").on("click", function() {
    var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
        $.notify({
			// options
			message: '情報を選択してください。' 
		},{
			// settings
			placement: {
        		from: "top",
        		align: "left"
    		},
    		z_index: 1031,
        	delay: 2000,
        	timer: 1000,
        	animate: {
        		enter: 'animated fadeInDown',
        		exit: 'animated fadeOutUp'
        	},
			type: 'danger'
		});
        return;
    }

    for (var i=0; i<selectedRows.length; i++) {
            var data = selectedRows[i];

            console.log(data);
    }
    
    alertify.set({
		labels : {
			ok     : "確認",
			cancel : "キャンセル"
		},
		delay : 5000,
		buttonReverse : true,
		buttonFocus   : "ok"
	});
    
	alertify.confirm("the information that order name and options before and after translation", function (e) {
		if (e) {
			$.notify({
				// options
				message: '注文情報の置換を完了しました。' 
			},{
				// settings
				placement: {
		    		from: "top",
		    		align: "left"
				},
				z_index: 1031,
		    	delay: 2000,
		    	timer: 1000,
		    	animate: {
		    		enter: 'animated fadeInDown',
		    		exit: 'animated fadeOutUp'
		    	},
		    	onClosed: function() {
		    		// 노티스가 닫히고 나서
		    		console.log("closed");
		    		$("#btn_down").prop("disabled", "");
		    	},
				type: 'success'
			});
		} else {
			return false;
		}
	});
	
});

$("#btn_down").on("click", function() {
	console.log("download clicked");
});

function getInfo(data) {
        console.log("getInfo");
        console.log(data);
        $('form').attr('action', "getInfo");
        $('form').attr('method', "post");
        $("#car_id").val(data);
        $("form").submit();
}