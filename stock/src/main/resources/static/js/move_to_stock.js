
$('#descriptionToStock').on('input', enableButtun);

$('#qtyToStock').on('input', function(){
	enableButtun();

// Can not move more than there is in the CO Manufacture
	var val = $('#coMfr option:selected').val();
	if(val){

		var val = parseInt(this.value);
		var max = parseInt(this.max);
		if(val > max){
			this.value = max;
			$(this).addClass('text-danger');
		}else
			$(this).removeClass('text-danger');
	}
});

$('#coMfr').change(function(){
	enableButtun();

	var val = this.value;
	if(val){
		var obj = JSON.parse(val);
		var maxVal = obj.qty;
		var input = $('#qtyToStock').prop('max', maxVal);
		var val = parseInt(input.val());

		if(val > maxVal)
			input.val(maxVal);
	}
});

$('#confirmBtn').click(function(){


	var action = '/component/to_stock';
	var _csrf = $( "input[name='_csrf']" ).val();
	var qty = $('#qtyToStock').val();
	var coMfr = JSON.parse($('#coMfr option:selected').val());
	var descriptionVal = $('#descriptionToStock').val();

	$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, companyId : coMfr.id, description : descriptionVal})
	.done(function(){
		fillFields();
	})
	.fail(function(error) {
		var responseText = error.responseText
		alert(responseText);
	})
	
})

function enableButtun() {

	var coMfr = $('#coMfr option:selected').val();
	var description = $('#descriptionToStock').val();
	var inputQty = $('#qtyToStock');
	var qtyToStock = inputQty.val();

	if(coMfr){
		inputQty.prop('disabled', false);

		if (description && qtyToStock > 0)
			$('#confirmBtn').prop('disabled', false);
		else
			$('#confirmBtn').prop('disabled', true);
	}else{
		inputQty.prop('disabled', true);
		inputQty.val('');
		$('#confirmBtn').prop('disabled', true);
	}
}
