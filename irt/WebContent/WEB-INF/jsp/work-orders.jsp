<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="irt.data.CookiesWorker"%>
<%@page import="irt.data.dao.UserBeanDAO"%>
<%@page import="irt.data.Error"%>
<%@page import="irt.web.WorkOrderServlet"%>
<jsp:useBean id="error" scope="request" type="irt.data.Error" />
<jsp:useBean id="menu" scope="request" type="java.lang.String" />
<%@ include file="inc/top.tag" %>

<% isWO = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content" class="cCenter">
<%	if((error.isError())){
%>		<h3 class="red" ><%=error.getErrorMessage()%></h3>
<%}%>
<jsp:include page="inc/workOrders/menu.jsp"/>

	<c:choose>
		<c:when test="${menu == 'cwo'}">
			<c:import url="inc/workOrders/createWorkOrder.jsp"></c:import>
		</c:when>
	</c:choose>
</div>

<%@ include file="inc/bottom.tag" %>
