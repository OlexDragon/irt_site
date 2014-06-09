
<%@page import="irt.work.ComboBoxField"%>
<%@page import="java.util.List"%>
<%@page import="sun.org.mozilla.javascript.internal.Kit"%>
<%@page import="irt.data.dao.KitDAO"%>
<%@page import="irt.data.dao.CompanyDAO"%>
<%@page import="irt.data.HTMLWork"%>
<%@page import="irt.data.purchase.PurchaseOrder"%>
<%@ page import="irt.data.companies.Company" %>
<%@ page import="irt.data.purchase.Purchase" %>
<%@ page import="irt.data.dao.PurchaseDAO" %>
<jsp:useBean id="purchase" scope="request" type="irt.data.purchase.Purchase" />
<%@ include file="inc/top.tag" %>

<% isPO = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>
<div id="content">
<%
	String purchaseStr 	= Purchase.PAGE_MENU.getDescription(Purchase.PAGE_PURCHASE);
	String movementStr 	= Purchase.PAGE_MENU.getDescription(Purchase.PAGE_MOVEMENT);
	String cmStr 		= Purchase.PAGE_MENU.getDescription(Purchase.PAGE_CM);
	String vendorsStr 	= Purchase.PAGE_MENU.getDescription(Purchase.PAGE_VENDORS);
	boolean isPurchase 	= purchase.getActivePage()==Purchase.PAGE_PURCHASE;
	boolean isSellers 	= purchase.getActivePage()==Purchase.PAGE_VENDORS;
	boolean isSuppliers = purchase.getActivePage()==Purchase.PAGE_CM;
	boolean isMovement	= purchase.getActivePage()==Purchase.PAGE_MOVEMENT;

	String str = isPurchase ? purchaseStr : isSellers ? vendorsStr : isSuppliers ? cmStr : movementStr;
%>	<hr /><h3>IRT <%=str%>.</h3><hr />

	<form method="post" action="/irt/sellers" onsubmit="storeScrollPosition()">
	<p>
	<input type="hidden" id="scrollx" name="scrollx" value="<%=request.getParameter("scrollx")%>" />
	<input type="hidden" id="scrolly" name="scrolly" value="<%=request.getParameter("scrolly")%>" />
<%	if(ub.isSellers() || ub.isStock()){ %>
		<input type="radio" name="po" id="pur" value='<%=purchaseStr%>' <%=(isPurchase)
				? "checked=\"checked\""
				: "onclick=\"oneClick('submit')\" onmouseover=\"style.cursor='hand'\""%> />

	<label for="pur"<%=(!isPurchase) ? " onmouseover=\"style.cursor='hand'\"" : ""%>><%=purchaseStr%></label>

	<input type="radio" name="po" id="mov" value='<%=movementStr%>' <%=(isMovement)
				? "checked=\"checked\""
				: "onclick=\"oneClick('submit')\" onmouseover=\"style.cursor='hand'\""%> />
	<label for="mov"<%=(!isMovement) ? " onmouseover=\"style.cursor='hand'\"" : ""%>><%=movementStr%></label>
<%}%>
	<input type="radio" name="po" id="sel" value='<%=vendorsStr%>'
			<%=(isSellers)
				? "checked=\"checked\""
				: "onclick=\"oneClick('submit')\" onmouseover=\"style.cursor='hand'\""%> />
	<label for="sel"<%=(!isSellers) ? " onmouseover=\"style.cursor='hand'\"" : ""%>><%=vendorsStr%>s</label>

	<input type="radio" name="po" id="sup" value='<%=cmStr%>'
			<%=(isSuppliers)
				? "checked=\"checked\""
				: "onclick=\"oneClick('submit')\" onmouseover=\"style.cursor='hand'\""%> />
	<label for="sup"<%=(!isSuppliers) ? " onmouseover=\"style.cursor='hand'\"" : ""%>><%=cmStr%>s</label>

	<%=HTMLWork.getHtmlSelect(new CompanyDAO().getComboBoxFields(Company.TYPE_CM), ""+purchase.getCompanyId(), "company", "onchange=\"oneClick('submit')\"", "Select CM")%>
<%	KitDAO kitDAO = new KitDAO();
	if(isMovement && kitDAO.hasKits()){
		List<Company> comps = kitDAO.getAllOpenKITs();
		ComboBoxField[] fields = new ComboBoxField[comps.size()];
%>		<%=HTMLWork.getHtmlSelect(comps.toArray(fields), ""+purchase.getKitId(), "kit", "onchange=\"oneClick('submit')\"", "Select KIT")%>
<%	} %>
	<input type="submit" name="submit" id="submit" value="submit" />
<%
	if(purchase.getTable()!=null)
		if(ub.isStock() && isMovement){
%>			<input type="submit" name="submit_move" id="submit_move" value="Prepare to Move" />
<%		}
	if(isPurchase){
		if(purchase.getTable()!=null && purchase.getPurchaseOrder()==null){
%>				<input type="submit" name="submit_purchase" id="submit_purchase" value="Prepare Order" />
<%		} %>
		<select id="status" name="status" onchange="oneClick('submit')">
		<option value="<%=PurchaseOrder.ALL%>" <%=purchase.getStatusToShow()==PurchaseOrder.ALL ? "selected=\"selected\"" : "" %>>All POs</option>
		<option value="<%=PurchaseOrder.OPEN%>" <%=purchase.getStatusToShow()==PurchaseOrder.OPEN ? "selected=\"selected\"" : "" %>>Open POs</option>
		<option value="<%=PurchaseOrder.ACTIVE%>" <%=purchase.getStatusToShow()==PurchaseOrder.ACTIVE ? "selected=\"selected\"" : "" %>>Active POs</option>
		<option value="<%=PurchaseOrder.COMPLETE%>" <%=purchase.getStatusToShow()==PurchaseOrder.COMPLETE ? "selected=\"selected\"" : "" %>>Completed POs</option>
		<option value="<%=PurchaseOrder.CLOSE%>" <%=purchase.getStatusToShow()==PurchaseOrder.CLOSE ? "selected=\"selected\"" : "" %>>Closed POs</option>
		</select>
<%		String[] poNumbers = new PurchaseDAO().getPONumbers(purchase.getStatusToShow());
		if(poNumbers!=null){%>
			<select id="poNumbeer" name="poNumber" onchange="oneClick('submit_view')" >
			<option >Select</option>
<%			for(String s:poNumbers){%>
				<option <%=(purchase.getPurchaseOrder()!=null && purchase.getPurchaseOrder().getPONumber()!=null && s.equals(purchase.getPurchaseOrder().getPONumber()) ? "selected=\"selected\"" : "")%> ><%=s%></option>
<%			} %>
			</select>
			<input type="submit" name="submit_view" id="submit_view" value="View PO" />
<%		}
		if(ub.isEditCompanies() && purchase.getPurchaseOrder()!=null && purchase.getPurchaseOrder().isEdit()){%>
			<input type="submit" name="submit_purchase_edit" id="submit_purchase_edit" value="Edit PO" />
<%	}	}%>
	</p>
	<hr />
<%
		if(ub.isEditCompanies()){
			if(isSellers || isSuppliers){
%>
<p>
<%		boolean isAdd = purchase.isAddCompany();
		boolean isEdit = purchase.isEdit();
		if(isAdd || isEdit){
			Company company = purchase.getCompany();
			str = company!=null ? " value=\""+company.getCompanyName()+"\"" : "";
			if(!ub.isAdmin() && isEdit)
				str += " readonly=\"readonly\"";
%>
			<label>Company:</label><input type="text" id="company_name" name="company_name"<%=str%> />

<%			str = company!=null ? " value=\""+company.getName()+"\"" : "";
			if(!ub.isAdmin() && isEdit)
				str += " readonly=\"readonly\"";
%>			<label>Representative:</label><input type="text" id="seller_name" name="seller_name" <%=str%> />
<%			str = company!=null ? " value=\""+company.getEMail()+"\"" : "";
%>		<label>E-Mail:</label><input type="text" id="e_mail" name="e_mail"<%=str%> />

<%
	str = company!=null ? " value=\""+company.getTelephone()+"\"" : "";
%>		<label>Tel:</label><input type="text" id="telephon" name="telephon"<%=str%> onchange="telValidation(event,this);" />

<%		str = company!=null ? " value=\""+company.getFax()+"\"" : "";
%>		<label>Fax:</label><input type="text" id="fax" name="fax"<%=str%> onchange="telValidation(event,this);" />

<%		str = company!=null && company.isActive() ? " checked=\"checked\"" : "";
%>		<input type="checkbox" id="chckbx_active" name="chckbx_active"<%=str%> /><label for="chckbx_active">Active</label>

		<br />
		<label>Address:</label><textarea class="cMargin5" cols="40" rows="5" id="address" name="address"><%=company!=null ? company.getAddress() : "" %></textarea>

		<br />
		<input type="submit" name="submit_add" id="submit_add" 	value="Save <%=isSellers ? vendorsStr : cmStr %>" />
		<input type="submit" name="submit_cancel" id="submit_cancel" 	value="Cancel" />

<%	}else{ %>
		<input type="submit" name="submit_add" id="submit_add" 	value="Add New <%=isSellers ? vendorsStr : cmStr %>" />
<%	} %>
</p>
<%	}	}
	if(isPurchase || isMovement){
%>		<p>
			<%=purchase.chooseComponentsWithBomHTML()%>
		</p>
<%	}
	if((purchase.isErrorMessage())){
%>		<h3 class="red" ><%=purchase.getErrorMessage()%></h3>
<%}%>
	<%=purchase.getPage()%>
	<script type="text/javascript">
	/* <![CDATA[ */
		function telValidation(e, field){
	       	// look for window.event in case event isn't passed in
        	if (typeof e == 'undefined' && window.event) { e = window.event; }
        	if (e.keyCode<=40 && e.keyCode!=32)
         		return true;
 			var tel = field.value.replace(/[^0-9]+/g, '');
			if(tel.length>4){
				tel = insertChar(tel, 4, '-');
				if(tel.length>8){
					tel = insertChar(tel, 8, ') ');
					if(tel.length>12){
						tel = insertChar(tel, 13, '(');
						if(tel.length>14)
							tel = tel.substr(tel.length-14);
					}
				}
			}
			field.value = tel;
		}
		function insertChar(str, position, charToInsert){
			return str.substr(0,str.length-position)+charToInsert+str.substr(str.length-position);
		}
   /* ]]> */
	</script>
	</form>
</div>
<%@ include file="inc/bottom.tag" %>
