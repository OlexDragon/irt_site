function oneClick(buttonName){
	document.getElementById(buttonName).click();
}
function oneKeyPress(e, buttonName){
	// look for window.event in case event isn't passed in
	if (typeof e == 'undefined' && window.event) { e = window.event; }
	if (e.keyCode == 13){
		document.getElementById(buttonName).click();
		return false;
	}else
		return true;
}
