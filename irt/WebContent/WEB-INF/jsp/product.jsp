<%@page import="irt.data.dao.ComponentDAO"%>
<%@page import="irt.data.dao.MenuDAO"%>
<%@page import="irt.data.dao.BomDAO"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="psf" scope="request" type="irt.web.ProductServlet.ProductStructureFields" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />

<%@ include file="inc/top.tag" %>

<% isPS = true;%>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
	<c:set var="ub" value="<%=ub%>" />
	<c:set var="partNumber" value="${psf.partNumber}" />
	<c:set var="menuDAO" value="<%=new MenuDAO()%>"/>
	<c:set var="componentDAO" value="<%=new ComponentDAO()%>"/>
	<c:set var="noBOM" value="${menuDAO.isBomValid(partNumber) && !componentDAO.hasBom(partNumber)}"/>

	<form id="linkForm" method="post" action="product_structure" enctype="multipart/form-data">
	<p>
	<c:if test="${!psf.isSymbol()}">
			<input type="text" name="pn" id="pn" value="${partNumber}" />
	</c:if>
	<c:if test="${noBOM || psf.isSymbol()}">
		<input type="file" name="file" id="file" />
	</c:if>
		<c:if test="${ub.isAdmin() && !psf.isSymbol()}">
			<input type="checkbox" name="check_footprint" id="check_footprint"<%=psf.isIgnoreFootprint() ? " checked=\"checked\"" : "" %> onclick="oneClick('submit-add-bom')" />
			<label for="check_footprint">Footprint</label>
		</c:if>
		<c:if test="${ub.isSchematicPart()}">
			<input type="checkbox" name="check_symbol" id="check_symbol"<%=psf.isSymbol() ? " checked=\"checked\"" : "" %> onclick="oneClick('submit-add-bom')" />
			<label for="check_symbol">Symbol</label>
		</c:if>
		<br />
		<input type="submit" name="submit-add-bom" id="submit-add-bom" value="${psf.isSymbol() ? 'get Symbols' : noBOM ? 'add BOM' : 'get BOM'}" />
		<c:if test="${!psf.isSymbol()}">
			<input type="submit" name="submit-where" id="submit-where" value="Where Used" />
		</c:if>
		<c:if test="${!noBOM}">
			<input type="submit" name="submit-pdf" id="submit-pdf" value="to PDF" />
			<input type="submit" name="submit-excel" id="submit-excel" value="to Excel" />
			<input type="checkbox" name="check_qty" id="check_qty"<%=psf.isWithQty() ? " checked=\"checked\"" : ""%> onclick="oneClick('submit-add-bom')" />
			<label for="check_qty">Excel with <%=psf.isWithQty() ?"Quantity" : "Footprint"%></label>
		</c:if>
		<c:if test="${psf.isBOM()}">
			<input type="checkbox" name="check_order" id="check_order"<%=psf.isOrderByReference() ? " checked=\"checked\"" : ""%> onclick="oneClick('submit-add-bom')" />
			<label for="check_order">Order by Reference</label>
		</c:if>
	<hr />
		<c:if test="${error.isError()}">
			<h3 class="red" >${error.errorMessage}</h3>
		</c:if>
		<c:if test="${!partNumber.isEmpty()}">
			<c:choose>
				<c:when test="${psf.isBOM()}">
					<%=new BomDAO().getBomTable(psf.getPartNumber(), psf.getOrderBy())%>
				</c:when>
				<c:otherwise>
					<%=new BomDAO().getTableWhereUsed(psf.getPartNumber())%>
				</c:otherwise>
			</c:choose>
		</c:if>
	</p>
	</form>
</div>
<%@ include file="inc/bottom.tag" %>
