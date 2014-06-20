<%@page import="irt.data.dao.ComponentDAO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="irt.web.WorkOrderServlet"%>
<%@page import="irt.data.dao.MenuDAO"%>
<%@page import="irt.data.dao.MenuDAO.OrderBy"%>
<%@page import="irt.data.dao.WorkOrderDAO"%>
<jsp:useBean id="prefix" scope="request" type="java.lang.String" />
<jsp:useBean id="wo" scope="request" type="irt.data.workorder.WorkOrder" />
	<c:set var="orderBy" value="<%=OrderBy.SEQUENCE%>"/>
	<c:set var="menuDao" value="<%=new MenuDAO() %>" />
<link rel="stylesheet" href="/irt/css/datepicker.css" type="text/css" />
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<script>
		$(function() {
			$( "#datepicker" ).datepicker();
		});
	</script>
<h3>Create new Work Order</h3>
<div>

<form id="wo_form" method="post" action="work-orders">
<p>
	<select name="prefix" onchange="oneClick('submit_WO');">
		<c:forEach var="menu" items="${menuDao.getComboBoxFields('wo_prefix', orderBy)}">
        	<option value="${menu.value}" ${menu.value == prefix ? 'selected="selected"' : ''}>${menu.text}</option>
		</c:forEach>
	</select>
</p>
<p>Date: <input type="text" id="datepicker"></p>
	<c:set var="woName" value="<%=WorkOrderDAO.getNewWorkOrderName(prefix)%>"/>
	<h4 id="wo_name"><c:out value="${woName}" /></h4>
	<input name="wo_description" id="wo_description" type="text" value="${wo.description}" />
<p>
	<input type="submit" name="submit_WO" id="submit_WO" value="Submit" />
	<input type="submit" name="submit_clear" id="submit_clear" value="Clear" onclick="return confirm('Are you sure that you want to clear the entire list?');" />
	<c:set var="componentDAO" value="<%=new ComponentDAO()%>" />
</p>
	<table class="border" >
		<tr>
			<th>PN</th>
			<th>MPN</th>
			<th>Description</th>
			<th>SQ</th>
			<th>Qty</th>
			<th></th>
		</tr>
		<c:forEach var="c" items="${componentDAO.getComponents(wo.idsArray, null)}">
			<tr>
				<td>${c.partNumberF}</td>
				<td>${c.manufPartNumber}</td>
				<td>${c.description}</td>
				<td><c:if test="${c.quantity>=0}">${c.quantity}</c:if></td>
				<td><input type="text" class="c2em" name="qty_${c.id}" id="qty_${c.id}" value="${wo.quantity}" /></td>
				<td><input type="submit" name="submit_del_${c.id}" id="submit_del_${c.id}" value="Remove" /></td>
			</tr>
		</c:forEach>
	</table>
</form>
</div>