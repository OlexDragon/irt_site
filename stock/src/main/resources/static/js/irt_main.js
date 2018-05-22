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
