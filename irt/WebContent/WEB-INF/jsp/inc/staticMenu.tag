<%@ page import="org.apache.commons.codec.binary.Base64"%>
<a id="page_top" />
<ul class="tabrow">
	<li class="<%=isPN ? "active" : "passive" %>"><a href="part-numbers">Part Numbers</a></li>
	<li class="<%=isML ? "active" : "passive" %>"><a href="manufacture-links">Manufactures</a></li>
	<li class="<%=isPS ? "active" : "passive" %>"><a href="product_structure">Product Structure</a></li>
	<li class="<%=isCost ? "active" : "passive" %>"><a href="cost">Cost</a></li>
<%if(logIn!=null && logIn.isValid()){
	if(ub.isStock()){
%>			<li class="<%=isCM ? "active" : "passive" %>"><a href="components_movement">Components Movement</a></li>
<%	}
	if(ub.isSellers() || ub.isStock() || ub.isEditCompanies()){%>
		<li class="<%=isPO ? "active" : "passive" %>"><a href="sellers">Purchase Order</a></li>
<%}	if(ub.isWorkOrder()){
%>		<li class="<%=isWO ? "active" : "passive" %>"><a href="work-orders" >Work Orders</a></li>
<%}
	if(ub.isUser()){
%>		<li class="<%=isU ? "active" : "passive" %>"><a href="users">Users</a></li>
<%	}
%>	<li class="<%=isH ? "active" : "passive" %>"><a href="history">History</a></li>
<%}%>		<jsp:useBean id="back_page" scope="request" type="java.lang.String" />
	<li class="<%=isLog ? "active" : "passive" %>"><a href="login?bp=<%=back_page%>"><%=(logIn!=null && logIn.isValid()) ? "Logout" : "Login"%></a></li>
</ul>

