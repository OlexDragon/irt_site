$body = $("body");
var componentId;
// Get vendor
var vendors;
$.get("/companies/vendors")
.done(function(data){
	vendors = data;
});
// Search part numbers containing 'str'
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
		})
		.fail(function(error) {
			alert(error.responseText);
		});
}
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

	componentId = $(this).val()[0];
	fillFields(componentId);
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
	$(this).text(partNumberAddDashes(this.text));
});

// Save Component price
$('#savePriceForm').submit(function(event){
	event.preventDefault();

	var _csrf = $( "input[name='_csrf']" ).val();
	var formMfrPN = $('#selectMfrPN option:selected').val();
	var formVendor = $('#selectVendor option:selected').val();
	var formForQty = $('#saveForQty').text();
	var formPrice = $(price).val() / $('#selectDivider option:selected').val();
	var currency = $('#selectCurrency option:selected').val();
	var poOrInv = $('#savePOSelect option:selected').val();
	var poNumber = $('#savePO').val();

	$.post('/component/price', { _csrf : _csrf, componentId : componentId, alternativeId : formMfrPN, vendorId : formVendor, forQty : formForQty, price : formPrice, currency : currency, orderType : poOrInv, orderNumber : poNumber})
	.done(function() {
		fillFields();
	})
	.fail(function(data) {
		var d = data;
//		alert( "error: " + data );
	});

//	location.reload();
	$('#confirmSet').modal('hide');
})
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
					.append($('<input>',{type : 'submit', value : 'Delete', class : 'btn-sm', style : 'cursor:pointer' })));
		});

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
		var buttonSet = $('<input>', { id : 'setPrice', type : 'button', value : 'Set', class : 'btn btn-primary', disabled : 'disabled', 'data-toggle' : 'modal', 'data-target' : "#confirmSet" });

		$('#editCost')
			.append($('<tr>'), {class : 'form-group'})
			.append($('<th>').append(selectMfrPN))
			.append($('<td>').append(selectVendors))
			.append($('<td>').append(forQty))
			.append($('<td>').append(price))
			.append($('<td>').append(selectCurrency))
			.append($('<td>').append(buttonSet));

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
		var responseText = error.responseText
		alert(responseText);
	});

}
function buttonSetEnable(){
	var vendorId = $('#selectVendor').val();
	var forQty = $('#forQty').val();
	var price = $('#price').val();
	var currency = $('#selectCurrency').val();
	if( vendorId && forQty>0 && price>0 && currency)
		$('#setPrice').prop('disabled', false);
	else
		$('#setPrice').prop('disabled', true);
}
function buttonSaveEnable(){
	var divider = $('#selectDivider option:selected').val();
	var poNumber = $('#savePO').val();

	if( poNumber && divider>0)
		$('#savePriceBtn').prop('disabled', false);
	else
		$('#savePriceBtn').prop('disabled', true);
}
function copyToClipboard(element) {
    var $temp = $("<div>", { text : element });
    $("body").append($temp);
    $temp.select();
    document.execCommand("copy");
    $temp.remove();
}
//loading spinning circle
//$(document).on({
//    ajaxStart: function() { $body.addClass("loading");    },
//     ajaxStop: function() { $body.removeClass("loading"); }    
//});