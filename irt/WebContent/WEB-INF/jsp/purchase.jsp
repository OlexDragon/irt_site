<%@page import="irt.data.Error"%>
<%@page import="irt.data.purchase.PurchaseOrder"%>
<%@ page import="irt.data.companies.Company"%>
<%@ page import="irt.data.dao.PurchaseDAO"%>
<%@ page import="irt.data.dao.CompanyDAO"%>
<%@ page import="irt.work.ComboBoxField"%>
<%@ page import="irt.data.HTMLWork"%>
<%@ page import="java.util.List"%>
<jsp:useBean id="purchase_order" scope="request" type="irt.data.purchase.PurchaseOrder" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />
<%@ include file="inc/top.tag" %>

<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div class="cCenter" id="content">
	<hr /><h3>IRT Purchase Order.</h3><hr />

	<form method="post" action="/irt/purchase">
	<p>
	<input type="radio" id="radio_vendor" name="company_type" value="vendor" onchange="oneClick('submit')" <%=purchase_order.isVendor() ? "checked=\"checked\"" : "" %> <%=purchase_order.isEdit() ? "" : "disabled=\"disebled\"" %> /><label for="radio_vendor" >Vendor</label>
	<input type="radio" id="radio_cm" name="company_type" value="cm" onchange="oneClick('submit')" <%=purchase_order.isCM() ? "checked=\"checked\"" : "" %> <%=purchase_order.isEdit() ? "" : "disabled=\"disebled\"" %> /><label for="radio_cm" >CM</label>
<%	List<Company> companies;
	if(purchase_order.isVendor()){
		companies = new CompanyDAO().getAllSellers(false);
	}else{
		companies = new CompanyDAO().getAllSuppliers(false);
	}
	if(!companies.isEmpty()){
		ComboBoxField[] fields = new ComboBoxField[companies.size()];%>
		<%=HTMLWork.getHtmlSelect(companies.toArray(fields), ""+purchase_order.getSellerId(), "cbSeller", "onchange=\"oneClick('submit')\"", "Select")%>
<%	} %>
 		<input type="submit" name="submit" id="submit" value="submit" />
<%	if(purchase_order.isSet() && purchase_order.getStatus()!=PurchaseOrder.CLOSE && purchase_order.getStatus()!=PurchaseOrder.COMPLETE && purchase_order.isEdit()){
		if(purchase_order.getPONumber()==null){%>
			<input type="submit" name="submit-save" id="submit-save" value="Save New PO" />
<%		}else if(purchase_order.getStatus()==PurchaseOrder.OPEN){ %>
			<input type="submit" name="submit-resave" id="submit-resave" value="Resave PO" />
<%		}else{ %>
			<input type="submit" name="submit-update" id="submit-update" value="Save Update" />
<%	}	} %>
	<input type="checkbox" id="chbxGST" name="chbxGST" onchange="oneClick('submit')" <%=purchase_order.isGST() ? "checked=\"checked\"" : "" %> <%=purchase_order.isEdit() ? "" : "disabled=\"disebled\"" %> /><label for="chbxGST">GST,</label>
	<input type="checkbox" id="chbxQST" name="chbxQST" onchange="oneClick('submit')" <%=purchase_order.isQST() ? "checked=\"checked\"" : "" %> <%=purchase_order.isEdit() ? "" : "disabled=\"disebled\"" %> /><label for="chbxQST">QST,</label>
	<select id="status" name="status" onchange="oneClick('submit')">
	<option value="<%=PurchaseOrder.ALL%>" <%=purchase_order.getStatusToShow()==PurchaseOrder.ALL ? "selected=\"selected\"" : "" %>>All POs</option>
	<option value="<%=PurchaseOrder.OPEN%>" <%=purchase_order.getStatusToShow()==PurchaseOrder.OPEN ? "selected=\"selected\"" : "" %>>Open POs</option>
	<option value="<%=PurchaseOrder.ACTIVE%>" <%=purchase_order.getStatusToShow()==PurchaseOrder.ACTIVE ? "selected=\"selected\"" : "" %>>Active POs</option>
	<option value="<%=PurchaseOrder.COMPLETE%>" <%=purchase_order.getStatusToShow()==PurchaseOrder.COMPLETE ? "selected=\"selected\"" : "" %>>Completed POs</option>
	<option value="<%=PurchaseOrder.CLOSE%>" <%=purchase_order.getStatusToShow()==PurchaseOrder.CLOSE ? "selected=\"selected\"" : "" %>>Closed POs</option>
	</select>
<%	String[] poNumbers = new PurchaseDAO().getPONumbers(purchase_order.getStatusToShow());
	if(poNumbers!=null){%>
		<select id="poNumber" name="poNumber" onchange="oneClick('submit-view')" >
		<option >Select</option>
<%		for(String s:poNumbers){%>
			<option <%=purchase_order.getPONumber()!=null && s.equals(purchase_order.getPONumber()) ? "selected=\"selected\"" : "" %>><%=s%></option>
<%		} %>
		</select>
		<input type="submit" name="submit-view" id="submit-view" value="View PO" />
<%		if(!purchase_order.isEdit()){ %>
			<input type="submit" name="submit-edit" id="submit-edit" value="Edit PO" />
<%			if(purchase_order.getStatus()==PurchaseOrder.OPEN || purchase_order.getStatus()==PurchaseOrder.ACTIVE){ %>
			<input type="submit" name="submit-close" id="submit-close" value="Close PO" onclick="return confirm('Do you want to close the Purchase Order?');" />
<%			}
		}else{%>
			<input type="submit" name="submit-cancel" id="submit-cancel" value="Cancel" onclick="return confirm('Do you want to cansel?');" />
<%	}	}
	if(purchase_order.getPONumber()!=null){%>
		<input type="submit" name="submit-pdf" id="submit-pdf" value="to PDF" />
		<input type="checkbox" id="chbxShippingInfo" name="chbxShippingInfo" onchange="oneClick('submit')" <%=purchase_order.isShippingInfo() ? "checked=\"checked\"" : "" %> /><label for="chbxShippingInfo">Shipping Info</label>
<%		if(purchase_order.getInvoiceLink()==null){%>
			<input type="submit" name="submit-invoice" id="submit-invoice" value="Add Invoice" />
<%		}else{%>
			<a class="cBlue" href="<%=purchase_order.getInvoiceLink()%>">Invoice</a>
<%	}	}%>
	</p>
	<hr />

<%
	if(error.isError()){
%>
		<h3 class="red" ><%=error.getErrorMessage()%></h3>
<% }
	String poNumber = purchase_order.getPONumber();
%>	<p><br /><span class="cBlue"><%=poNumber!=null ? poNumber+" - " : "" %></span>Status: <span class="<%=poNumber==null ? "cBlue\">New PO" :
						purchase_order.getStatus()==PurchaseOrder.OPEN ? 	"cDarkBlue\">Open PO" :
						purchase_order.getStatus()==PurchaseOrder.ACTIVE ? 	"cGreen\">Active PO" :
						purchase_order.getStatus()==PurchaseOrder.CLOSE ? 	"cFireBrick\">Closed PO" :
																			"cDarkSlateGray\">Completed PO" %>
					</span>
	</p>
	<%=purchase_order.getTable()%>
		<p><textarea id="comments" name="comments" rows="5" cols="60" <%=purchase_order.isEdit() ? "" : "disabled=\"disabled\"" %> onfocus="if(this.value == 'Other Comments or Special Instructions') { this.value = ''; }" onblur="if (this.value == '') { this.value='Other Comments or Special Instructions'; }" ><%= purchase_order.getComments()!=null ? purchase_order.getComments() : "Other Comments or Special Instructions" %></textarea></p>
	</form>
<%	if(purchase_order.getUser()!=null){ %>
	<p class="cDarkBlue" >Created By: <span class="cBlue" ><%=purchase_order.getUser().getFullName() %></span><br />
		Date: <span class="cBlue" ><%=purchase_order.getDate() %></span>
	</p>
<%	} %>
</div>

<%@ include file="inc/bottom.tag" %>
