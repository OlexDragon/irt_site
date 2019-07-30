function storeScrollPosition(){
	if( typeof( window.pageYOffset ) == 'number' ) {
		//Netscape compliant
		document.getElementById("scrolly").value = window.pageYOffset;
		document.getElementById("scrollx").value = window.pageXOffset;
	} else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
		    //DOM compliant
		document.getElementById("scrolly").value = document.body.scrollTop;
		document.getElementById("scrollx").value = document.body.scrollLeft;
	} else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
		    //IE6 standards compliant mode
		document.getElementById("scrolly").value = document.documentElement.scrollTop;
		document.getElementById("scrollx").value = document.documentElement.scrollLeft;
	}
}
function setScrollPosition(){
	var x = document.getElementById("scrollx");
	var y = document.getElementById("scrolly");
	if(x!=null && y!=null && !isNaN(x=x.value) && !isNaN(y=y.value) && (x!=0 || y!=0))
		window.scrollTo(x, y);
} 
