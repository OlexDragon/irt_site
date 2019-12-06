function partNumberAddDashes(pn){

	if (typeof pn === 'string' || pn instanceof String){
		var length = pn.length;

		if(length>15){
			var position = length-3;
			pn = pn.slice(0, position) + "-" + pn.slice(position);
		}

		if(length>10){
			var sub = pn.substring(0,3);

			if(sub==='00I' || sub==='TPB' || sub==='TRS')
				pn = pn.slice(0, 10) + "-" + pn.slice(10);

			else if(sub==='0IS' || sub==='0RF')
				pn = pn.slice(0, 8) + "-" + pn.slice(8);

			else
				pn = pn.slice(0, 9) + "-" + pn.slice(9);
		}

		if(length>3)
			pn = pn.slice(0, 3) + "-" + pn.slice(3);
	}
	return pn;
}

function allowDrop(ev)  {
	ev.preventDefault();
}

function dropToBomMenu(ev)  {
    ev.preventDefault();
    var partNumber = ev.dataTransfer.getData("text").trim();

    if(partNumber.toUpperCase().startsWith('PCA')){
    	Cookies.set("desiredPCA", partNumber, { expires: 7 });
    	location.href = ev.currentTarget.href;
    }else{
    	alert('Part Number should start with "PCA..."')
	}
}
//Search for the first fifteen part numbers containing 'str', search result returned by 'callback(array)' function.
function first15PartNumbersContains(str, callback){
	$.post("/pn/limit15", {desiredPN: str})
		.done(function(pns) { callback(pns); })
		.fail(function(error) {
			alert('"partNumbers"\n' + error.responseText);
		});
}
//Search for the first twenty part numbers containing 'str', search result returned by 'callback(array)' function.
function first20PartNumbersContains(str, callback){
	$.post("/pn/limit20", {desiredPN: str})
		.done(function(pns) { callback(pns); })
		.fail(function(error) {
			alert('"partNumbers"\n' + error.responseText);
		});
}

//function keyFilter(){
//	alert('Yee');
//}