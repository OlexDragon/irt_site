<%@ page import="irt.table.Table"%>
<%@ page import="irt.data.companies.Company"%>
<%@ page import="irt.data.purchase.CostService"%>
<%@ page import="irt.data.dao.BomDAO"%>
<%@ page import="irt.data.dao.MenuDAO"%>
<%@ page import="irt.data.dao.MenuDAO.OrderBy"%>
<%@ page import="irt.data.dao.ComponentDAO"%>
<%@ page import="irt.data.dao.CompanyDAO"%>
<%@ page import="irt.data.HTMLWork" %>

<jsp:useBean id="cost" scope="request" type="irt.data.purchase.CostService" />
<jsp:useBean id="position" scope="request" type="java.lang.String" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />

<%@ include file="inc/top.tag" %>
<%
	isCost = true;
%>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
	<hr /><h3>IRT Cost</h3><hr />
<%	String classId = cost.getClassId();
	String componentId = cost.getComponentId();
	boolean isCostEdit = cost.isEdit();
	boolean isEditCost = ub.isEditCost();
%>
<form method="post" action="/irt/cost" onsubmit="storeScrollPosition()">
	<p>
		<input type="hidden" id="scrollx" name="scrollx" value="<%=request.getParameter("scrollx")%>" />
		<input type="hidden" id="scrolly" name="scrolly" value="<%=request.getParameter("scrolly")%>" />
		<input type="hidden" id="position" name="position" value="<%=position%>" />
		<input type="hidden" id="what" name="what" />
		<%=HTMLWork.getHtmlSelect(new MenuDAO().getComboBoxFields("cost_class", OrderBy.DESCRIPTION), classId, "class_id", "onchange=\"oneClick('submit')\"", "Select")%>
<%	if(!classId.isEmpty() && !classId.equals("Select")){
%>		<%=HTMLWork.getHtmlSelect(new ComponentDAO().getComboBoxFields(classId), componentId, "component_id", "onchange=\"oneClick('submit')\"", "Select")%>
<%	}
%>		<input type="submit" id="submit" name="submit" value="submit" />
		<select id="set" name="set" onchange="oneClick('submit')" >
			<option value="0">Select Set</option>
<%	for(int i=1; i<4; i++){
%>			<option value="<%=i%>" <%=i==cost.getSetIndex() ? "selected=\"selected\"" : ""%>>Set #<%=i%></option>
<%	}
%>		</select>
<%	if(cost.hasSet()){
%>		<input type="submit" id="submit_set" name="submit_set" value="Set" />
<%	}
%>
<%	if(ub.isEditCost() && isCostEdit){
%>		<input type="submit" id="submit_save_set" name="submit_save_set" value="Save Set" />
<%	}
%>	</p>
<%	if(ub.isEditCost()){
		if(cost.isSet()){
%>			<p>
			<input type="checkbox" id="is_edit" name="is_edit" <%=isCostEdit ? "checked=\"checked\"" : ""%> onclick="oneClick('submit')" /><label for="is_edit">Edit</label>
<%	if(isCostEdit && cost.isChanged()){
%>				<input type="submit" id="submit_save" name="submit_save" value="Save" />
				<input type="submit" id="submit_cansel" name="submit_cansel" value="Cancel" />
<%	}
%>			</p>
<%	}
		if(isCostEdit){
%>		<div id="drag" onmouseover="style.cursor='hand'" >
			<%=HTMLWork.getHtmlSelect(new CompanyDAO().getComboBoxFields(Company.ALL), null, "companies", null, "Select")%>
			<label>MOQ:</label><input class="c3em" type="text" id="addFor" name="addFor" />
		</div>
<%	} 	}
	if(error.isError()){
%>	<h3 class="red"><%=error.getErrorMessage()%></h3>
<%	}
	if(cost.isSet()){%>
		<%=cost.getTable().toString()%>
<%	} %>
	<script type="text/javascript">
	/* <![CDATA[ */
	             
		var position = document.getElementById('position').value;
		if(position.indexOf(':')!=-1){
			var splitPosition = position.split(':')
			position = document.getElementById('drag');
			position.style.left = splitPosition[0];
			position.style.top = splitPosition[1];
		}

		function whatEdit(text){
			document.getElementById("what").value = text;
			oneClick('submit');
		}

		var offsetX = 0;
		var offsetY = 0;
		var object = null;
		document.onmousedown = OnMouseDown;
	    document.onmouseup = OnMouseUp;
		function OnMouseDown(e){
		    if (e == null) 
		        e = window.event; 
		    // IE uses srcElement, others use target
		    var target = e.target != null ? e.target : e.srcElement;
		    // for IE, left click == 1
		    // for Firefox, left click == 0
		    if ((e.button == 1 && window.event != null || e.button == 0) && target.id=='drag'){
				offsetX = target.offsetLeft-e.clientX;
				offsetY = target.offsetTop-e.clientY;
				object = target;

				document.onmousemove = OnMouseMove;
		        // cancel out any text selections
		        document.body.focus();

		        // prevent text selection in IE
		        document.onselectstart = function () { return false; };
		        // prevent IE from trying to drag an image
		        target.ondragstart = function() { return false; };
		        
		        // prevent text selection (except IE)
		        return false;
		    }
		}
		function OnMouseMove(e){
		    if (e == null) 
		        var e = window.event; 

		    object.style.left = (offsetX + e.clientX) + 'px';
		    object.style.top = (offsetY + e.clientY) + 'px';   
		}
		function OnMouseUp(e)
		{
		    if (object != null){

		        // we're done with these events until the next OnMouseDown
		        document.onmousemove = null;
		        document.onselectstart = null;
		        object.ondragstart = null;

		    	obj = document.getElementById('position');
				obj.value = (offsetX + e.clientX) +'px:'+(offsetY + e.clientY)+'px'

				// this is how we know we're not dragging      
		        object = null;
		    }
		}
		function ExtractNumber(value){
		    var n = parseInt(value);
		    return n == null || isNaN(n) ? 0 : n;
		}
	/* ]]> */
	</script>
</form>
</div>

<%@ include file="inc/bottom.tag" %>
