$body = $("body");
var componentId = Cookies.get("stockComponentId");
var stockQty = 0;
var companyQties;
// Get vendor
var vendors;
$.get("/companies/vendors")
.done(function(data){
	vendors = data;
});
var coManufactures;
$.get("/companies/co_mfr")
.done(function(data){
	coManufactures = data;
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
$('#pns_out').change(function() {

	$('#bulkBtn').addClass('d-none');
	componentId = $(this).val()[0];
	Cookies.set("stockComponentId", componentId, { expires: 7 });
	fillFields();
	if($('#price-hystory-tab').hasClass( 'active' ))
		fillPriceHistoryTab();
	else if($('#component-hystory-tab').hasClass( 'active' ))
		fillComponentHistoryTab();

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
$('#bulkBtn').click(function(){

	var input = $('<input>', {id : 'qtyToBulk', class : 'form-control', type : 'number'});
	var description = $('<input>', {id : 'descriptionBulk', class : 'form-control', type : 'text'});

	var buttonMove = $('<button>', {type : 'button', class : 'btn btn-outline-primary', 'data-dismiss' : 'modal', text : 'Move to BULK'}).prop('disabled', true);

	$('<div>', { id : 'bulkModal', class : 'modal fade', tabindex : '-1', role : 'dialog', 'aria-labelledby' : 'exampleModalLabel', 'aria-hidden' : 'true'})
	.appendTo('body')
	.append($('<div>', {class : "modal-dialog", role : 'document'})
			.append($('<div>', {class : 'modal-content'})
					.append($('<div>', {class : 'modal-header'})
							.append($('<h5>', {class : 'modal-title', text : 'Move to BULK'}))
							.append($('<button>', {type : 'button', class : 'close', 'data-dismiss' : 'modal', 'aria-label' : "Close"})
									.append($('<span aria-hidden="true">&times;</span>'))
							)
					)
					.append($('<div>', {class : 'modal-body'})
							.append($('<h6>', {text : 'Move the ' + $('#infoPN').text() + ' to the bulk.'}))
							.append($('<div>', {class : 'input-group mb-2'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Qty to move:'}))
									)
									.append(input
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){

												// Can not move more than there is in the stock
												if(this.value > stockQty){
													this.value = stockQty;
													$(this).addClass('text-danger');
												}else
													$(this).removeClass('text-danger');

												if(this.value && description.val() && this.value>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
							.append($('<div>', {class : 'input-group'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Description'}))
									)
									.append(description
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){
												if(this.value && input.val()>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
					)
					.append($('<div>', {class : 'modal-footer'})
							.append($('<button>', {type : 'button', class : 'btn btn-secondary', 'data-dismiss' : 'modal', text : 'Close'}))
							.append(buttonMove
									//Move to Bulk
									.click(function(){

										var _csrf = $( "input[name='_csrf']" ).val();
										var action = '/component/bulk';
										var qty = input.val();
										var descriptionVal = description.val();

										$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, description : descriptionVal})
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
//move to co manufacture
$('#toCoMfrBtn').click(function(){

	var selectCoMfr = $('<select>', {id : 'coMfr', class : 'form-control'}).append($('<option>', {text : 'Select the CO Manufacture'}));
	$(coManufactures).each(function(){
		selectCoMfr.append($('<option>', {value : this.id, text : this.companyName}));
	});
	var inputQty = $('<input>', {id : 'qtyToCoMfr', class : 'form-control', type : 'number'});
	var description = $('<input>', {id : 'descriptionBulk', class : 'form-control', type : 'text'});

	var buttonMove = $('<button>', {type : 'button', class : 'btn btn-outline-primary', 'data-dismiss' : 'modal', text : 'Move to CO Manufacture'}).prop('disabled', true);

	$('<div>', { id : 'bulkModal', class : 'modal fade', tabindex : '-1', role : 'dialog', 'aria-labelledby' : 'exampleModalLabel', 'aria-hidden' : 'true'})
	.appendTo('body')
	.append($('<div>', {class : "modal-dialog", role : 'document'})
			.append($('<div>', {class : 'modal-content'})
					.append($('<div>', {class : 'modal-header'})
							.append($('<h5>', {class : 'modal-title', text : 'Move to CO Manufacture'}))
							.append($('<button>', {type : 'button', class : 'close', 'data-dismiss' : 'modal', 'aria-label' : "Close"})
									.append($('<span aria-hidden="true">&times;</span>'))
							)
					)
					.append($('<div>', {class : 'modal-body'})
							.append($('<h6>', {text : 'Move the ' + $('#infoPN').text() + ' to:'}))
							.append(selectCoMfr
									// CO Manufactures
									.change(function(){

										var coM =  $('#coMfr option:selected').val();

										if(coM && description.val() && inputQty.val()>0)
											buttonMove.prop('disabled', false);
										else
											buttonMove.prop('disabled', true);
									})
							)
							.append($('<div>', {class : 'input-group mt-2'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Qty to move:'}))
									)
									.append(inputQty
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){

												// Can not move more than there is in the stock
												if(this.value > stockQty){
													this.value = stockQty;
													$(this).addClass('text-danger');
												}else
													$(this).removeClass('text-danger');

												var coM =  $('#coMfr option:selected').val();

												if(coM && description.val() && this.value>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
							.append($('<div>', {class : 'input-group mt-2'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Description'}))
									)
									.append(description
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){

												var coM =  $('#coMfr option:selected').val();

												if(coM && description.val() && inputQty.val()>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
					)
					.append($('<div>', {class : 'modal-footer'})
							.append($('<button>', {type : 'button', class : 'btn btn-secondary', 'data-dismiss' : 'modal', text : 'Close'}))
							.append(buttonMove
									//Move to Bulk
									.click(function(){

										var _csrf = $( "input[name='_csrf']" ).val();
										var action = '/component/to_co_mfr';
										var qty = inputQty.val();
										var coMfr = $('#coMfr option:selected').val();
										var descriptionVal = description.val();

										$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, companyId : coMfr, description : descriptionVal})
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
//move from co manufacture to STOCK
$('#toStockBtn').click(function(){

	var cq;

	var selectCoMfr = $('<select>', {id : 'coMfr', class : 'form-control'});
	var inputQty = $('<input>', {id : 'qtyToCoMfr', class : 'form-control', type : 'number'}).prop('disabled', true);
	var description = $('<input>', {id : 'descriptionBulk', class : 'form-control', type : 'text'});
	var buttonMove = $('<button>', {type : 'button', class : 'btn btn-outline-primary', 'data-dismiss' : 'modal', text : 'Move from CO Manufacture'}).prop('disabled', true);

	var coMfrs = companyQties.filter(function(cq){return cq.qty>0 });
	if(coMfrs.length>1){
		selectCoMfr.append($('<option>', {value : '', text : 'Select the CO Manufacture'}));
		inputQty.prop('disabled', true);
	}else{
		inputQty.prop('disabled', false);
		cq = coMfrs[0];
	}

	$(coMfrs).each(function(){
		selectCoMfr.append($('<option>', {value : this.company.id, text : this.company.companyName}));
	});


	$('<div>', { id : 'bulkModal', class : 'modal fade', tabindex : '-1', role : 'dialog', 'aria-labelledby' : 'exampleModalLabel', 'aria-hidden' : 'true'})
	.appendTo('body')
	.append($('<div>', {class : "modal-dialog", role : 'document'})
			.append($('<div>', {class : 'modal-content'})
					.append($('<div>', {class : 'modal-header'})
							.append($('<h5>', {class : 'modal-title', text : 'Move from CO Manufacture to the Stock'}))
							.append($('<button>', {type : 'button', class : 'close', 'data-dismiss' : 'modal', 'aria-label' : "Close"})
									.append($('<span aria-hidden="true">&times;</span>'))
							)
					)
					.append($('<div>', {class : 'modal-body'})
							.append($('<h6>', {text : 'Move the ' + $('#infoPN').text() + ' from:'}))
							.append(selectCoMfr
									// CO Manufactures
									.change(function(){

										var coMfrId =  $('#coMfr option:selected').val();
										cq = companyQties.find(function(o){return o.company.id == coMfrId});
										if(cq)
											inputQty.prop('disabled', false);
										else
											inputQty.prop('disabled', true);

										if(coMfrId && description.val() && inputQty.val()>0)
											buttonMove.prop('disabled', false);
										else
											buttonMove.prop('disabled', true);
									})
							)
							.append($('<div>', {class : 'input-group mt-2'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Qty to move:'}))
									)
									.append(inputQty
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){

												// Can not move more than there is in the CO Manufacture
												coMfrQty = cq.qty;
												if(this.value > coMfrQty){
													this.value = coMfrQty;
													$(this).addClass('text-danger');
												}else
													$(this).removeClass('text-danger');

												var coMfrId =  $('#coMfr option:selected').val();

												if(coMfrId && description.val() && this.value>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
							.append($('<div>', {class : 'input-group mt-2'})
									.append($('<div>', {class : 'input-group-prepend'})
											.append($('<label>', {class : 'input-group-text', 'for' : 'qtyToBulk', text : 'Description'}))
									)
									.append(description
											// Disable/Enable 'Move to BULK' button
											.on('input', function(){

												var coMfrId =  $('#coMfr option:selected').val();

												if(coMfrId && description.val() && inputQty.val()>0)
													buttonMove.prop('disabled', false);
												else
													buttonMove.prop('disabled', true);
											})
									)
							)
					)
					.append($('<div>', {class : 'modal-footer'})
							.append($('<button>', {type : 'button', class : 'btn btn-secondary', 'data-dismiss' : 'modal', text : 'Close'}))
							.append(buttonMove
									//Move to Bulk
									.click(function(){

										var _csrf = $( "input[name='_csrf']" ).val();
										var action = '/component/from_co_mfr';
										var qty = inputQty.val();
										var coMfr = $('#coMfr option:selected').val();
										var descriptionVal = description.val();

										$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, companyId : coMfr, description : descriptionVal})
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
function partNumberAddDashes(pn){

	if (typeof pn === 'string' || pn instanceof String){
		length = pn.length;

		if(length>15){
			position = length-3;
			pn = pn.slice(0, position) + "-" + pn.slice(position);
		}

		if(pn.length>10){
			var sub = pn.substring(0,3);

			if(sub==='00I' || sub==='TPB' || sub==='TRS')
				pn = pn.slice(0, 10) + "-" + pn.slice(10);

			else if(sub==='0IS' || sub==='0RF')
				pn = pn.slice(0, 8) + "-" + pn.slice(8);

			else
				pn = pn.slice(0, 9) + "-" + pn.slice(9);
		}

		if(pn.length>3)
			pn = pn.slice(0, 3) + "-" + pn.slice(3);
	}
	return pn;
}
function fillFields(){
	$.get("/pn/details", {pnId : componentId})
	.done(function(data) {

	//Bulk and 'TO CO MFR' buttons
		stockQty = data.qty ? data.qty : 0;
		if(stockQty>0)
			$('.hiddenBtn').removeClass('d-none');
		else
			$('.hiddenBtn').addClass('d-none');

	//'TO STOCK' buttons
		companyQties = data.companyQties;
		var maxQty = Math.max.apply( Math, data.companyQties.map(function(o){return o.qty;}));
		if(maxQty>0)
			$('#toStockBtn').removeClass('d-none');
		else
			$('#toStockBtn').addClass('d-none');

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
		        text : this.company.companyName
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
		    .append($('<td>', { text : this.company.companyName }))
		    .append($('<td>', { text : this.id.forQty }))
		    .append($('<td>', { text : this.cost }))
		    .append($('<td>', { text : this.currency ? this.currency : 'N/A'}))
		    .append($('<td>', { text : this.orderType ? this.orderType + ': ' + this.orderNumber : 'N/A'}))	//Purchase order or Invoice
		    .append($('<td>', { text : this.changeDate }));

			$('#editCost')
			.append($('<tr>'))
			.append($('<th>', { text : this.alternative ? this.alternative.altMfrPartNumber : data.manufPartNumber }))
		    .append($('<td>', { text : this.company.companyName }))
			.append($('<td>', { text : this.id.forQty }))
			.append($('<td>', { text : this.cost }))
			.append($('<td>', { text : this.currency ? this.currency : 'N/A' }))
			.append($('<td>')
					.append($('<form>', {class : 'deleteCostForm', method : 'POST', action : ('/component/cost/delete/' + componentId + '/' + (this.alternative ? this.alternative.id : 0) + '/' + this.company.id + '/' + this.id.forQty)})
							.append($('<button>',{type : 'submit', text : 'Delete', class : 'btn-sm btn-outline-warning', style : 'cursor:pointer' }))));
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

		$('#selectDivider')	.change(function() { buttonSaveEnable(); });//On select quantity change ( 'selectDivider' )
		$(selectVendors) 	.change(function(){ buttonSetEnable(); });
		$(selectCurrency)	.change(function(){ buttonSetEnable(); });

		$('#savePO').on('input', function(){ buttonSaveEnable(); });//On input PO or Invoice change ( 'selectDivider' )
		forQty	 	.on('input', function(){ buttonSetEnable(); });
		price	 	.on('input', function(){ buttonSetEnable(); });

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

				var quantity = this.qty;
				var from = this.id.componentMovement.from;

				if(from=='STOCK')
					quantity*=-1;

				componentHistory
				.append($('<tr>'))
			    .append($('<th>', { text : this.id.componentMovement.dateTime }))
			    .append($('<td>', { text : this.id.componentMovement.user.username }))
			    .append($('<td>', { text : from }))
			    .append($('<td>', { text : this.id.componentMovement.fromCompany ? this.id.componentMovement.fromCompany.companyName : '' }))
			    .append($('<td>', { text : this.id.componentMovement.to }))
			    .append($('<td>', { text : this.id.componentMovement.toCompany ? this.id.componentMovement.toCompany.companyName : '' }))
			    .append($('<td>', { text : this.id.componentMovement.description }))
			    .append($('<td>', { text : this.oldQty }))
			    .append($('<td>', { text : quantity }))
			    .append($('<td>', { text : this.oldQty || quantity>0 ? this.oldQty + quantity : '' }));
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

		if(currency && price>0){
			setPrice.val('Set');
			$('#selectDivider').prop('disabled', false);
		}
		else{
			setPrice.val('Add');
			$('#selectDivider').prop('disabled', true);
		}

		setPrice.prop('disabled', false);
	}else
		setPrice.prop('disabled', true);
}
function buttonSaveEnable(){
	var disabled = $('#selectDivider').prop('disabled');
	var divider = $('#selectDivider option:selected').val();
	var poNumber = $('#savePO').val();

	if(poNumber){

		if(disabled || divider<=0)
			$('#savePriceBtn').prop('disabled', true);
		else
			$('#savePriceBtn').prop('disabled', false);

		$('#addToStockBtn').prop('disabled', false);
	}else{
		$('#addToStockBtn').prop('disabled', true);
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
}
//Search part numbers containing 'str'
function partNumbers(str){
	$.get("/pn", {desiredPN: str})
		.done(function(data) {

			Cookies.set("desiredPN", str, { expires: 7 });
			$('#pns_out').empty();

			$.each( data, function(key, partNumber) {

				$('#pns_out').append($('<option>', { 
			        value: partNumber.id,
			        text : partNumberAddDashes(partNumber.partNumber)
			    }));
			});
			selectComponent();
		})
		.fail(function(error) {
			alert(error.responseText);
		});
}
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