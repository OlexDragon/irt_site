
$('#descriptionMfrToBulk').on('input', enableButtun);

$('#qtyMfrToBulk').on('input', function(){
	enableButtun();

	if(this.value){

		var val = parseInt(this.value);
		var max = parseInt($(this).prop('max'));

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
		var input = $('#qtyMfrToBulk').prop('max', maxVal);
		var val = parseInt(input.val());

		if(val > maxVal)
			input.val(maxVal);
	}
});

$('#confirmBtnFromMfr').click(function(){

	var action = '/component/mfr_to_bulk/';
	var _csrf = $( "input[name='_csrf']" ).val();
	var qty = $('#qtyMfrToBulk').val();
	var descriptionVal = $('#descriptionMfrToBulk').val();
	var coMfr =  JSON.parse($('#coMfr option:selected').val());

	$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, description : descriptionVal, coMfr : coMfr.id })
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
	var description = $('#descriptionMfrToBulk').val();
	var inputQty = $('#qtyMfrToBulk');
	var qtyToStock = inputQty.val();

	if(coMfr){
		inputQty.prop('disabled', false);

		if (description && qtyToStock > 0)
			$('#confirmBtnFromMfr').prop('disabled', false);
		else
			$('#confirmBtnFromMfr').prop('disabled', true);
	}else{
		inputQty.prop('disabled', true);
		inputQty.val('');
		$('#confirmBtnFromMfr').prop('disabled', true);
	}
}
