<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="irt.data.components.Component"%>
<%@page import="irt.data.Search"%>
<%@page import="irt.table.Table"%>
<%@page import="irt.data.ToDoClass.ToDo"%>
<%@page import="irt.data.dao.MenuDAO"%>
<%@page import="irt.data.Browser.BrowserId"%>
<%@page import="irt.work.TextWorker"%>
<%@page import="irt.data.partnumber.PartNumberDetails"%>
<%@page import="irt.data.dao.CostDAO"%>
<%@page import="irt.data.dao.SecondAndThirdDigitsDAO"%>
<%@page import="irt.data.SecondAndThirdDigits"%>
<%@page import="java.util.List"%>
<%@page import="irt.data.dao.FirstDigitDAO"%>
<%@page import="irt.data.FirstDigit"%>
<jsp:useBean id="component" scope="request" type="irt.data.components.Component" />
<jsp:useBean id="orderBy" scope="request" type="irt.table.OrderBy" />
<jsp:useBean id="todo" scope="request" type="irt.data.ToDoClass" />
<jsp:useBean id="search" scope="request" type="irt.data.ToDoClass" />
<jsp:useBean id="browser" scope="request" type="irt.data.Browser" />
<jsp:useBean id="partNumber" scope="request" type="irt.data.partnumber.PartNumber" />

<%@ include file="inc/top.tag" %>

<%
	isPN = true;/*use for static menu*/
%>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>
<%
boolean isExist = component.isExist();
boolean hasLink = component.getLink().getId()>0;
boolean isEditing = ub.isEditing();
String partNumberStr = component.getPartNumberF();
%>
<c:set var="isExist" value="<%=component.isExist()%>" />
<c:set var="isEditing" value="<%=ub.isEditing()%>" />
<c:set var="hasLink" value="<%=component.getLink().getId()>0%>" />

<div id="content" class="cCenter">
	<form id="pn" method="post" action="part-numbers">
	<div id="inputs" class="sticker"><p>
		<select id="first" name="first" onchange="oneClick('submit_to_text');">
			<option value="-">Select</option>
<%
	String componentClassId = component.getClassId();
	List<FirstDigit> firstIterator = new FirstDigitDAO().getAll();
	for(FirstDigit firstDigit:firstIterator){
%>				<option value="<%=firstDigit.getId()%>" <%=(componentClassId!=null && firstDigit.getFirstChar()==componentClassId.charAt(0)) ? "selected=\"selected\"" : ""%>>
					<%=firstDigit.getDescription()%>
				</option>
<%
	}
%>		</select>
<%
	SecondAndThirdDigitsDAO secondAndThirdDigitsDAO = new SecondAndThirdDigitsDAO();
	if(componentClassId!=null){
		List<SecondAndThirdDigits> type = secondAndThirdDigitsDAO.getRequired(componentClassId.charAt(0));
%>		<select id="second" name="second" onchange="oneClick('submit_to_text');">
			<option value="-">Select</option>
<%
	for(SecondAndThirdDigits secondAndThirdDigits:type){
%>				<option value="<%=secondAndThirdDigits.getId()%>" <%=secondAndThirdDigits.getClassIdStr().equals(componentClassId) ? "selected=\"selected\"" : ""%>>
					<%=secondAndThirdDigits.getDescription()%>
				</option>
<%
	}
%>		</select>
<%
	}
%>		<input type="submit" name="submit_to_text" id="submit_to_text" value="Show Part Number" />
		<input type="submit" name="submit-search" id="submit-search" value="Search" />
		<input type="submit" name="submit-cancel" id="submit-cancel" value="Reset" />
		
	<c:if test="${component.isSet() && isEditing && (!isExist || component.isEdit())}">
		<input type="submit" name="submit-add" id="submit-add" value="${component.isEdit() ? 'Update' : 'Add' }" />
	</c:if>

	<c:if test="${isExist && !hasLink && isEditing}">
		<input type="submit" name="submit_add_link" value="Add Link" />
	</c:if>
	<c:set var="hasCost" value="<%=new CostDAO().hasCost(component.getId())%>"/>
	<c:if test="${ub.isEditCost() && isExist || hasCost}">
		<input type="submit" name="submit-price" value="Price" />
	</c:if>

	<br />
		<%=new PartNumberDetails(component).getHTML() %>
	<c:if test="<%=componentClassId!=null && componentClassId.equals(secondAndThirdDigitsDAO.getClassID(TextWorker.PROJECT))%>">
			<input type="submit" name="submit_show" value="Show All" />
	</c:if>

	<br />
	</p>
		<hr />
	<p>
<% String str = browser.getBrowserId()==BrowserId.CHROME ? " onclick=\"this.select();\"" : " onfocus=\"this.select();\""; %>
	<input type="text" name="pnText" id="pnText" value="<%=partNumberStr%>"<%=str%> onkeypress="return oneKeyPress(event,'submit-parse')" />
<script type="text/javascript">
    jQuery(function(){
    	$("#pnText").autocomplete("/irt/autocomplete/parse.jsp");
    });
</script>
	<input type="submit" name="submit-parse" id="submit-parse" value="Parse" />
<% if(ub!=null && isExist){ 
		if(ub.isSchematicPart() && (component.getSchematicPart()!=null || ub.isAdmin())){ %>
		<input type="submit" name="submit-part" id="submit-part" value="Schematic Part" onclick="return confirm('Do you want to save Schematic Part?');" />
<%		}
		if(ub.isSchematicLetter() && component.getSchematicLetter()!=null){%>
			<input type="submit" name="submit-letter" id="submit-letter" value="Schematic Letter" onclick="return confirm('Do you want to resave Schematic Letter?');" />
<% }	}
	if(isExist){
%>		<input type="submit" name="submit-where" id="submit-where" value="Where Used" />
<%	}
	String mfrPN = component.getManufPartNumber();
	if(mfrPN!=null && mfrPN.startsWith("IRT BOM")){//has BOM
%>
		<input type="submit" name="submit-bom" id="submit-bom" value="get BOM" />
		<input type="submit" name="submit-pdf" id="submit-pdf" value="to PDF" />
<%		if(new MenuDAO().shawExel(componentClassId)){%>
			<input type="submit" name="submit-excel" id="submit-excel" value="to Excel" />
<%	}	}
	if(ub.isAlt() && partNumberStr.length()>4 && !partNumberStr.contains("?") && isEditing && isExist){ %>
		<input type="submit" name="submit-alt" id="submit-alt" value="Alt." />
<%	}
	if(ub.isStock() && isExist){
		if(partNumber.isInMoving()){
%>			<span class="red">exist in the moving list</span>
<%		}else if( component.getQuantity()>0){
%>			<input type="submit" name="submit-move" id="submit-move" value="Move from Stock" />
<%		}else {
%>			<span class="red">Not in stock</span>
<%		}
		if(partNumber.getPurchase()==null || partNumber.getPurchase().getPurchaseOrder()==null || partNumber.getPurchase().getPurchaseOrder().isEdit()){
			if(partNumber.isInPurchaseOrder()){
%>				<span class="red">Is in Purchase Order</span>
<%			}else{ %>
				<input type="submit" name="submit-add-to-purchase" id="submit-add-to-purchase" value="to PO" />
<%		}	} %>
<%		if(partNumber.getPurchase()!=null && partNumber.getPurchase().getPurchaseOrder()!=null){ %>
			<input type="submit" name="submit-purchase" id="submit-purchase" value="Purchase" />
<%	}	}
%>	<c:if test="<%=ub.isAdmin() || (ub.isWorkOrder()) %>">
			<input type="submit" name="submit_to_wo" id="submit_to_wo" value="to WO" />
	</c:if>
	</p></div>
<%	str = component.getTable();
	if(!str.isEmpty()){
%>		<div class="sticker">
		<%=str%>
<% 		if(ub.isStock()){%>
			<span>Location</span>
			<input type="text" id="text_location" name="text_location" class="c4em" onkeypress="return oneKeyPress(event,'submit_location')" />
			<input type="submit" id="submit_location" name="submit_location" value="SET" title='Set Locathion' onclick="return confirm('Do you want to change the location?');" />
			<span>Description:</span>
			<input type="text" id="text_qty_description" name="text_qty_description" title="Description (PO number, Company name ...)" />
			<span>Qty:</span>
			<input type="text" id="text_qty_set" name="text_qty_set" class="c3em" value="0" title="The number of received components" />
			<input type="submit" id="submit_qty_add" name="submit_qty_add" value="+" title='Better to use the "Components Movement" tab' onclick="return confirm('Do you want to update the stock quantity?');" />
<% } %>	</div>
<%		}%>	
	<c:if test="${component.getError().isError()}">
		<h3 class="red" >${component.getError().getErrorMessage()}</h3>
	</c:if>
<%	Table table;
	switch(search.getCommand()){
	case PRICE:
		table = partNumber.getPrices();
		break;
	case PROJECT_SERARCH:
		table =  new Search().componentBy(search.getValue(), orderBy);
		break;
	default:
		table = new Search().componentBy((Component)Component.parseData(search.getValue()));
	}
	if(table!=null ){
		str = partNumber.getPartNumber();
		if(ub.isStock() && !str.isEmpty() && str.charAt(0)=='0'){
			table.showColumn(0);
		}else
			table.hideColumn(0);
%>		<%=table.toString() %>
<%	}%>
	</form>
</div>

 
<%@ include file="inc/bottom.tag" %>
