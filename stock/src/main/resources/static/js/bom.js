var componentId;
var pnsOut = $('#pns_out');
if(pnsOut[0].length==1){
	componentId = pnsOut[0][0].value;
	Cookies.set("bomComponentId", componentId, { expires: 7 });
}else
	componentId = Cookies.get("bomComponentId");
if(componentId)
	selectComponent();

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
    timer = setTimeout(pcaPartNumbers, 600, $(this).val());
});

// On select part number
pnsOut.change(function() {

	$('#toBulkBtn').addClass('d-none');
	componentId = $(this).val()[0];
	Cookies.set("bomComponentId", componentId, { expires: 7 });
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
	option.text(partNumberAddDashes(option.text()));
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

//Search part numbers containing 'str'
function pcaPartNumbers(str){

	var str = str.toUpperCase();
	var value;

	if(str.indexOf("PCA")!=0)
		value = 'PCA%' + str + '%';
	else
		value = str + '%';

	$.get("/pn/like", {desiredPN: value})
		.done(function(pns) {

			Cookies.set("desiredPCA", str, { expires: 7 });
			pnsOut.empty();

			if(pns){
				$.each( pns, function(key, partNumber) {

					pnsOut.append($('<option>', { 
						value: partNumber.id,
						text : partNumberAddDashes(partNumber.partNumber),
						class : 'font-weight-bold text-secondary'
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

function selectComponent(){
	if(pnsOut.val()!=componentId){
		pnsOut.val(componentId);
		fillFields();
	}
}

function fillFields(){ $('#bomDetails').load('/bom/details/' + componentId); }

