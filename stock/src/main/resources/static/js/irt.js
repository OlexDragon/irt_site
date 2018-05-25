var componentId;
var pnsOut = $('#pns_out');
if(pnsOut[0].length==1)
	componentId = pnsOut[0][0].value;
else
	componentId = Cookies.get("stockComponentId");
if(componentId)
	selectComponent();

var stockQty = 0;
var companyQties;
// Get vendor
var vendors;
$.get("/companies/vendors")
.done(function(data){
	vendors = data;
})
.fail(function(error) {
	alert(error.responseText);
});
var coManufactures;
$.get("/companies/co_mfr")
.done(function(data){
	coManufactures = data;
})
.fail(function(error) {
	alert(error.responseText);
});
// Part number input listener
timer = 0;
$("#pn_input").on('input', function() {
    if (timer) {
        clearTimeout(timer);
    }
    timer = setTimeout(partNumbers, 600, $(this).val());
});

// On select part number
pnsOut.change(function() {

	$('#toBulkBtn').addClass('d-none');
	componentId = $(this).val()[0];
	Cookies.set("stockComponentId", componentId, { expires: 7 });
	fillFields();
});

//On select PO or Invoice
$('#savePOSelect').change(function() {
	var selected = $(this).val();
	if(selected=='PO')
		$('#savePO').prop('placeholder', 'PO numbet');
	else
		$('#savePO').prop('placeholder', 'Invoice numbet');
});

//Add dashes to the part numbers
$('#pns_out > option').each(function() {
	option = $(this);
	option.text(partNumberAddDashes(this.text));
	if(option.val()==componentId){
		option.prop('selected', true);
		fillFields();
	}
});

// Save Component price
$('#savePriceForm').submit(function(event){
	event.preventDefault();

	var action = getAction();
	var data = {};
	data['_csrf'] 		= $( "input[name='_csrf']" ).val();
	data['componentId'] 	= componentId;
	data['alternativeId'] = $('#selectMfrPN option:selected').val();
	data['vendorId'] 		= $('#selectVendor option:selected').val();
	data['forQty'] 		= $('#saveForQty').text();
	data['orderType'] 	= $('#savePOSelect option:selected').val();
	data['orderNumber'] 	= $('#savePO').val();
	data['action'] 		= action;

	if(action=='ALL' || action=='SAVE_PRICE'){
		data['price'] 	= $('#price').val()/$('#selectDivider option:selected').val();
		data['currency'] 	= $('#selectCurrency option:selected').val();
	}

	$.post('/component/price', data)
	.done(function() {
		fillFields();
	})
	.fail(function(error) {
		alert(error.responseText);
	});

//	location.reload();
	$('#confirmSet').modal('hide');
});
//what submit button was pressed
var submitButton;
$('button:submit').click(function(){
	submitButton = this.id;
});

//move to bulk modal
$('#toBulkBtn').click(function(){ $('#modalLoad').load('/modal/to_bulk/' + componentId); });

//move to co manufacture
$('#toCoMfrBtn').click(function(){ $('#modalLoad').load('/modal/to_co_mfr/' + componentId); });

//move from co manufacture to STOCK
$('#toStockBtn').click(function(){ $('#modalLoad').load('/modal/to_stock/' + componentId); });

//Move all BOM component from MFR to PCA
$('#toAssenbly').click(function(){ $('#modalLoad').load('/modal/to_assembly/' + componentId); });

//Move all BOM component from MFR to PCA
$('#mfrToBulk').click(function(){ $('#modalLoad').load('/modal/mfr_to_bulk/' + componentId); });

//Copy part number to Clipboard
$('#infoPN').dblclick(function(){

	if (document.body.createTextRange) {
        const range = document.body.createTextRange();
        range.moveToElementText(this);
        range.select();
    } else if (window.getSelection) {
        const selection = window.getSelection();
        const range = document.createRange();
        range.selectNodeContents(this);
        selection.removeAllRanges();
        selection.addRange(range);
    } else {
        alert("Could not select text in node: Unsupported browser.");
        return;
    }
	document.execCommand("copy");
	alert('Part number copied to the clipboard')
})

function fillFields(){
	if(componentId)
	$.get("/pn/details", {pnId : componentId})
	.done(function(data) {

		//fill selected tabs
		if($('#price-hystory-tab').hasClass( 'active' ))
			fillPriceHistoryTab();
		else if($('#component-hystory-tab').hasClass( 'active' ))
			fillComponentHistoryTab();

		//Bulk and 'TO CO MFR' buttons
		stockQty = data ? data.qty : 0;
		if(stockQty>0)
			$('.fromStockBtn').removeClass('d-none');
		else
			$('.fromStockBtn').addClass('d-none');

	//'TO STOCK' buttons
		var maxQt
		if(data){
			companyQties = data.companyQties ? data.companyQties : 0;
			maxQty = companyQties ? Math.max.apply( Math, companyQties.map(function(o){return o.qty;})) : 0;
		}else{
			maxQty = 0;
		}
		if(maxQty>0){
			$('.fromMfrBtn').removeClass('d-none');
			//'TO PCA' button
			if(data.partNumber.indexOf('PCB')==0){
				var _csrf = $( "input[name='_csrf']" ).val();
				$.post('/bom/exists/' + componentId, {_csrf : _csrf})
				.done(function(data){
					if(data)
						$('#toAssenbly').removeClass('d-none');
					else
						$('#toAssenbly').addClass('d-none');
				})
				.fail(function(error) {
					location.reload(true);
//					alert(error.responseText);
				});
			}else
				$('#toAssenbly').addClass('d-none');
		}else{
			$('.fromMfrBtn').addClass('d-none');
			$('#toAssenbly').addClass('d-none');
		}

		$('#infoPN').text(partNumberAddDashes(data.partNumber)).prop('title', 'component ID: ' + componentId);
		$('#infoDescription').text(data.description ? data.description : "N/A");
		$('#infoMfrPN').text(data.manufPartNumber ? data.manufPartNumber : "N/A");
		$('#infoMfr').text(data.manufacture ? data.manufacture.name : "N/A");
//Quantity
		$('#infoQty').empty();
		$('#infoQty').append($('<dt>', { 
			class : 'col-sm-4',
	        text : 'Stock'
	    }))
	    .append($('<dd>', { 
			class : 'col-sm-8',
	        text : data.qty ? data.qty : "N/A"
	    }));

		$(data.companyQties).each(function(){
			$('#infoQty').append($('<dt>', { 
				class : 'col-sm-4',
		        text : this.company.companyName,
		        title : 'company ID: '+ this.company.id
		    }))
		    .append($('<dd>', { 
				class : 'col-sm-8',
		        text : this.qty
		    }));
		});
//Alternative Part Numbers
		var selectMfrPN = $('<select>', {id : 'selectMfrPN', class : 'form-control' }).append($('<option>', { value : 0,  text : data.manufPartNumber }));// Used in the Edit tub; 0 -> not alternative
		$('#infoAlternative').empty();
		$(data.alternatives).each(function(){
			$('#infoAlternative').append($('<dt>', { 
				class : 'col-sm-4',
		        text : this.altMfrPartNumber
		    }))
		    .append($('<dd>', { 
				class : 'col-sm-8',
		        text : this.manufacture.name
		    }));
			selectMfrPN.append($('<option>', { value : this.id,  text : this.altMfrPartNumber }));// Used in the Edit tub; this.id -> Alternative part number ID.
		});
//Cost
		$('#infoCost').empty();
		$('#editPanel').empty();
		$('#editCost').empty()

		// Vendors
		var selectVendors = $('<select>', { id : 'selectVendor', class : 'form-control'}).append($('<option>'));
		$(vendors).each(function(){
			selectVendors.append($('<option>', {value : this.id, text : this.companyName}))
		});
		//Currency
		var selectCurrency = $('<select>',  { id : 'selectCurrency', class : 'form-control'}).append($('<option>')).append($('<option>', {value : 'USD', text : 'USD'})).append($('<option>', {value : 'CAD', text : 'CAD'}));
		// For Quantity
		var forQty = $('<input>', { id : 'forQty', type : 'number', class : 'form-control input-sm', value : ''});
		//Price
		var price = $('<input>', { id : 'price', type : 'number', class : 'form-control input-sm', value : '', step : '0.01'});
		// Set button
		var buttonSet = $('<input>', { id : 'setPrice', type : 'button', value : 'Set', class : 'btn btn-outline-primary', disabled : 'disabled', 'data-toggle' : 'modal', 'data-target' : "#confirmSet" });

		$('#editCost')
			.append($('<tr>'), {class : 'form-group'})
			.append($('<th>').append(selectMfrPN))
			.append($('<td>').append(selectVendors))
			.append($('<td>').append(forQty))
			.append($('<td>').append(price))
			.append($('<td>').append(selectCurrency))
			.append($('<td>').append(buttonSet));

		$(data.costs).each(function(){
			$('#infoCost')
			.append($('<tr>'))
		    .append($('<th>', { text : this.alternative ? this.alternative.altMfrPartNumber : data.manufPartNumber }))
		    .append($('<td>', { text : this.company ? this.company.companyName : 'N/A' }))
		    .append($('<td>', { text : this.id.forQty }))
		    .append($('<td>', { text : this.cost }))
		    .append($('<td>', { text : this.currency ? this.currency : 'N/A'}))
		    .append($('<td>', { text : this.orderType ? this.orderType + ': ' + this.orderNumber : 'N/A'}))	//Purchase order or Invoice
		    .append($('<td>', { text : this.changeDate }));

			$('#editCost')
			.append($('<tr>'))
			.append($('<th>', { text : this.alternative ? this.alternative.altMfrPartNumber : data.manufPartNumber }))
		    .append($('<td>', { text : this.company ? this.company.companyName : 'N/A' }))
			.append($('<td>', { text : this.id.forQty }))
			.append($('<td>', { text : this.cost }))
			.append($('<td>', { text : this.currency ? this.currency : 'N/A' }))
			.append($('<td>')
					.append($('<form>', {
						class : 'deleteCostForm',
						method : 'POST',
						action : ('/component/cost/delete/' + componentId + '/' + (this.alternative ? this.alternative.id : 0) + '/' + (this.company ? this.company.id : 0)+ '/' + this.id.forQty)
					})
					.append($('<button>',{type : 'submit', text : 'Delete', class : 'btn-sm btn-outline-warning'}))));
		});
//Delete Cost 
		$('.deleteCostForm').submit(function(event){
			event.preventDefault();

			var action = this.action;

			$('<div>', { id : 'deleteCostConfirm', class : 'modal fade', tabindex : '-1', role : 'dialog', 'aria-labelledby' : 'exampleModalLabel', 'aria-hidden' : 'true'})
			.appendTo('body')
			.append($('<div>', {class : "modal-dialog", role : 'document'})
					.append($('<div>', {class : 'modal-content'})
							.append($('<div>', {class : 'modal-header'})
									.append($('<h5>', {class : 'modal-title', text : 'Delete Component Cost'}))
									.append($('<button>', {type : 'button', class : 'close', 'data-dismiss' : 'modal', 'aria-label' : "Close"})
											.append($('<span aria-hidden="true">&times;</span>'))
									)
							)
							.append($('<div>', {class : 'modal-body'})
									.append($('<p>', {text : 'Are you sure you want to delete this line from the database?'}))
							)
							.append($('<div>', {class : 'modal-footer'})
									.append($('<button>', {type : 'button', class : 'btn btn-secondary', 'data-dismiss' : 'modal', text : 'Close'}))
									.append($('<button>', {type : 'button', class : 'btn btn-primary', 'data-dismiss' : 'modal', text : 'Delete'})
											.click(function(){
												var _csrf = $( "input[name='_csrf']" ).val();
									  			$.post( action, {_csrf : _csrf})
												.done(function(){
													fillFields();
												})
												.fail(function(error) {
													var responseText = error.responseText
													alert(responseText);
												})
												
											})
									)
							)
					)
		).modal('show')
		.on('hidden.bs.modal', function (e) {
			$(this).remove();
		});
			
	});

		$('#selectDivider')	.change(buttonSaveEnable);//On select quantity change ( 'selectDivider' )
		$(selectVendors) 	.change(buttonSetEnable);
		$(selectCurrency)	.change(buttonSetEnable);

		$('#savePO').on('input', buttonSaveEnable);//On input PO or Invoice change ( 'selectDivider' )
		forQty	 	.on('input', buttonSetEnable);
		price	 	.on('input', buttonSetEnable);

		buttonSet.click(function(){
			$('#saveMfrPN').text($('#selectMfrPN option:selected').text());
			$('#saveVendor').text($('#selectVendor option:selected').text());
			$('#saveForQty').text($(forQty).val());
			$('#savePrice').text($('#selectCurrency option:selected').text()+ ' $' + $(price).val());

			selectDivider = $('#selectDivider').empty();

			// If 'formForQty' > 1 ask for that 'formPrice' is for 1 piece or for 'formForQty'
			if($(forQty).val()>1){
				selectDivider
				.append($('<option>'))
				.append($('<option>', {value : '1', text : 'Price for 1 pc.'}))
				.append($('<option>', {value : $(forQty).val(), text : 'Price for ' + $(forQty).val() + ' pc.'}));
			}else{
				selectDivider.append($('<option>', {value : '1', text : 'Price for 1 pc.'}));
			}
			buttonSaveEnable();
		});
	})
	.fail(function(error) {
		alert(error.responseText);
	});
}


$('#whereUsed').click(function(){
	if(componentId)
		$('#modalLoad').load('/modal/where_used/' + componentId);
});

function fillPriceHistoryTab(){
	if(componentId)
		$.get('/component/price/history', {componentId : componentId})
		.done(function(data){
			costHistory = $('#costHistory').empty();
			$(data).each(function(){
				costHistory
				.append($('<tr>'))
			    .append($('<th>', { text : this.alternative ? this.alternative.altMfrPartNumber : $('#infoMfrPN').text() }))
			    .append($('<td>', { text : this.company.companyName }))
			    .append($('<td>', { text : this.forQty }))
			    .append($('<td>', { text : this.price }))
			    .append($('<td>', { text : this.currency ? this.currency : 'N/A'}))
			    .append($('<td>', { text : this.orderType ? this.orderType + ': ' + this.orderNumber : 'N/A'}))	//Purchase order or Invoice
			    .append($('<td>', { text : this.changeDate }));
			});
		})
		.fail(function(error) {
			alert(error.responseText);
		});

}
function fillComponentHistoryTab(){
	if(componentId)
		$.get('/component/history', {componentId : componentId})
		.done(function(data){
			var componentHistory = $('#componentHistory').empty();
			$(data).each(function(){

				if(!this.qty)
					return;

				var to = this.id.componentMovement.to;
				var from = this.id.componentMovement.from;

				if(from=='STOCK' || to=='ASSEMBLED' || to=='BULK')
					this.qty*=-1;

				componentHistory
				.append($('<tr>'))
			    .append($('<th>', { text : this.id.componentMovement.dateTime, title : 'movement ID: ' + this.id.componentMovement.id }))
			    .append($('<td>', { text : this.id.componentMovement.user.username }))
			    .append($('<td>', { text : this.id.componentMovement.fromCompany ? this.id.componentMovement.fromCompany.companyName : from, title : from }))
			    .append($('<td>', { text : this.id.componentMovement.toCompany ? this.id.componentMovement.toCompany.companyName : to, title : to }))
			    .append($('<td>', { text : this.id.componentMovement.description }))
			    .append($('<td>', { text : this.oldQty }))
			    .append($('<td>', { text : this.qty }))
			    .append($('<td>', { text : this.oldQty || this.qty>0 ? this.oldQty + this.qty : '' }));
			});
		})
		.fail(function(error) {
			alert(error.responseText);
		});
}
function buttonSetEnable(){
	var vendorId = 	$('#selectVendor').val();
	var forQty = 	$('#forQty').val();
	var price = 	$('#price').val();
	var currency = 	$('#selectCurrency').val();
	var setPrice = 	$('#setPrice');

	if( vendorId && forQty>0){

		if(currency || price>0){
			setPrice.val('Set');
			$('#selectDivider').prop('disabled', false);

			if(currency && price>0)
				setPrice.prop('disabled', false);
			else
				setPrice.prop('disabled', true);

		}else{
			setPrice.val('Add');
			setPrice.prop('disabled', false);
			$('#selectDivider').prop('disabled', true);
		}
	}else
		setPrice.prop('disabled', true);
}
function buttonSaveEnable(){
	var disabled = $('#selectDivider').prop('disabled');
	var divider = $('#selectDivider option:selected').val();
	var poNumber = $('#savePO').val();

	var addToStockBtn = $('#addToStockBtn');

	if(poNumber){

		if(disabled){
			$('#savePriceBtn').prop('disabled', true);
			addToStockBtn.prop('disabled', false);
			addToStockBtn.prop('title', 'Add this component to the stock.')
		}else{
			if(divider<=0){
				$('#savePriceBtn').prop('disabled', true);
				addToStockBtn.prop('disabled', true);
				addToStockBtn.prop('title', 'Add this component to the stock.')
			}else{
				$('#savePriceBtn').prop('disabled', false);
				addToStockBtn.prop('disabled', false);
				addToStockBtn.prop('title', 'Save the price and add this component to the stock.')
			}
		}

	}else{
		addToStockBtn.prop('disabled', true);
		$('#savePriceBtn').prop('disabled', true);
	}
}
function getAction(){

	if($('#savePriceBtn').prop('disabled'))
		return 'ADD_TO_CTOCK';

	if(submitButton == 'addToStockBtn')
		return 'ALL';

	return 'SAVE_PRICE';
}
//function copyToClipboard(element) {
//    var $temp = $("<div>", { text : element });
//    $("body").append($temp);
//    $temp.select();
//    document.execCommand("copy");
//    $temp.remove();
//}
function selectComponent(){
	$('#pns_out option[value="' + componentId + '"]').prop('selected', true);
	fillFields();
}
//Search part numbers containing 'str'
function partNumbers(str){
	$.get("/pn", {desiredPN: str})
		.done(function(pns) {

			Cookies.set("desiredPN", str, { expires: 7 });
			pnsOut.empty();

			if(pns){
				$.each( pns, function(key, partNumber) {

					pnsOut.append($('<option>', { 
						value: partNumber.id,
						text : partNumberAddDashes(partNumber.partNumber)
					}));
				});

				if(pns.length==1){
					var pn = pns[0];
					componentId = pn.id;
				}

				selectComponent();
			}
		})
		.fail(function(error) {
			alert(error.responseText);
		});
}
//$body = $("body");
//loading spinning circle
//$(document).on({
//    ajaxStart: function() { $body.addClass("loading");    },
//     ajaxStop: function() { $body.removeClass("loading"); }    
//});

//Price history tab
$('#price-hystory-tab').click(function(){
	fillPriceHistoryTab();
});

//Component history tab
$('#component-hystory-tab').click(function(){
	fillComponentHistoryTab();
});