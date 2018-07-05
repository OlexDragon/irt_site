$('table thead th').click(function(event){
	event.preventDefault();

	var id =  $(this).prop('id');
	var _csrf = $( "input[name='_csrf']" ).val();
	$('#modal').load('/production/filter/' + id, {_csrf : _csrf});
})
$('.partNumber').click(function(){
	var partNumber = $(this).text();
	Cookies.set("desiredPN", partNumber, { expires: 7 });	
});