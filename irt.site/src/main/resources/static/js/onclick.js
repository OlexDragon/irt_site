var ctrlKey = false;
function checkCtrl(e){
	ctrlKey = e.ctrlKey;
}
function oneClick(buttonName){
	if(ctrlKey)
		return;
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
