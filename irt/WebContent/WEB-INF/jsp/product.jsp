<%@page import="irt.data.Error"%>
<%@page import="irt.product.ProductStructure"%>

<jsp:useBean id="product" scope="request" type="irt.product.ProductStructure" />

<%@ include file="inc/top.tag" %>

<% isPS = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
<form id="linkForm" method="post" action="product_structure" enctype="multipart/form-data">
<p>
<%if(!product.isSymbol()){ %>
	<input type="text" name="pn" id="pn" value="<%=product.getPartNumber()%>" />
<%}		String htmlStr = product.htmlFile();%>
	<%=htmlStr%>
<%if(ub.isAdmin() && !product.isSymbol()){%>
	<input type="checkbox" name="check_footprint" id="check_footprint"<%=!product.isFootprint ? " checked=\"checked\"" : "" %> onclick="oneClick('submit-add-bom')" />
	<label for="check_footprint">Footprint</label>
<%}else
	product.isFootprint = true;
  if(ub.isSchematicPart() || ub.isAdmin()){%>
		<input type="checkbox" name="check_symbol" id="check_symbol"<%=product.isSymbol() ? " checked=\"checked\"" : "" %> onclick="oneClick('submit-add-bom')" />
		<label for="check_symbol">Symbol</label>
		<%}else
			product.setSymbol(false);
%>	<br /><input type="submit" name="submit-add-bom" id="submit-add-bom" value="<%=(product.isSymbol() ? "get Symbols" : htmlStr.isEmpty() ? "get BOM" : "Add BOM")%>" />
<%if(!product.isSymbol()){
%>	<input type="submit" name="submit-where" id="submit-where" value="Where Used" />
<%
	if(product.hasBom() && !product.isErrorMessage()){
%>
		<input type="submit" name="submit-pdf" id="submit-pdf" value="to PDF" />
		<input type="submit" name="submit-excel" id="submit-excel" value="to Excel" />
		<input type="checkbox" name="check_qty" id="check_qty"<%=product.isQty ? " checked=\"checked\"" : ""%> onclick="oneClick('submit-add-bom')" />
		<label for="check_qty">Excel with <%=product.isQty ?"Quantity" : "Footprint"%></label>
<%
	}	}
	if(product.isBom() && !product.isTop()){
%>
		<input type="checkbox" name="check_order" id="check_order"<%=product.isOrderByReference() ? " checked=\"checked\"" : ""%> onclick="oneClick('submit-add-bom')" />
		<label for="check_order">Order by Reference</label>
<%
	}
%></p>
	<hr />
</form>

<%
	if((product.isErrorMessage())){
%>
		<h3 class="red" ><%=product.getErrorMessage()%></h3>
<%
	}
%>
<%
	if(product.isErrorMessage()){
%>
		<h3 class="cBlue" ><%=product.getErrorMessage()%></h3>
<%}%>
<%@ page import="irt.table.Table" %>
	<%Table table = product.getTable();
	if(table!=null){%>
		<%=table.toString()%>
<script type="text/javascript">
	document.title = "<%=table.getTitle().getText()%>";
</script>
	<%}%>
</div>
<%@ include file="inc/bottom.tag" %>
