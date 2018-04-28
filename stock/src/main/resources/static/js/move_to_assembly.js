
$('#topPNs').change(enableButtun);

$('#pcbQty').on('input', function(){
	enableButtun();

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
		var input = $('#pcbQty').prop('max', maxVal);
		var val = parseInt(input.val());

		if(val > maxVal)
			input.val(maxVal);
	}
});

$('#confirmBtn').click(function(){


	var _csrf = $( "input[name='_csrf']" ).val();
	var qty = $('#pcbQty').val();
	var fromId = JSON.parse($('#coMfr option:selected').val());
	var topId = $('#topPNs option:selected').val();

	$.post('/bom/to_assembly/' + topId + '/' + fromId.id + '/' + qty, {_csrf : _csrf})
	.done(function(){
		fillFields();
	})
	.fail(function(error) { alert(error.responseText); })
})

function enableButtun() {

	var coMfr = $('#coMfr option:selected').val();
	var topPN = $('#topPNs option:selected').val();
	var inputQty = $('#pcbQty');
	var pcbQty = inputQty.val();

	if(coMfr){
		inputQty.prop('disabled', false);

		if (topPN && pcbQty > 0)
			$('#confirmBtn').prop('disabled', false);
		else
			$('#confirmBtn').prop('disabled', true);
	}else{
		inputQty.prop('disabled', true);
		inputQty.val('');
		$('#confirmBtn').prop('disabled', true);
	}
}
