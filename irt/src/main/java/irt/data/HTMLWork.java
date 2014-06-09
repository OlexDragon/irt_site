package irt.data;

import irt.data.components.movement.ComponentQty;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.purchase.ForPrice;
import irt.data.user.UserBean;
import irt.work.ComboBoxField;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HTMLWork {


	public static ComponentsQuantity getHtmlFields(HttpServletRequest request, String contains) {
		ComponentsQuantity htmlFields = null;

		String name;
		String quantityStr;
		Enumeration<String> names = request.getParameterNames();
		htmlFields = new ComponentsQuantity();
		while(names.hasMoreElements())
			if((name = names.nextElement()).contains(contains)){
				quantityStr = request.getParameter(name).replaceAll("[^\\d]", "");
				name = name.replaceAll("[^\\d]", "");

				if(!(name.isEmpty() || quantityStr.isEmpty()))
					htmlFields.add(new ComponentQty(Integer.parseInt(name), Integer.parseInt(quantityStr)));
			}	

		return htmlFields;
	}

	public static String getComboBox(List<UserBean> userBeans, String userName) {
		String returnStr = "";

		if(userBeans!=null && !userBeans.isEmpty()){
			returnStr = "<select id=\"username\" name=\"username\" onchange=\"oneClick('submit')\">";
			for(UserBean ub:userBeans){
				String tmpStr = ub.getUsername();
				returnStr += "<option value=\""+tmpStr+"\" "+(tmpStr.equals(userName) ? " selected=\"selected\"" : "" )+">";
				returnStr += ub.getFullName();
				returnStr += "</option>";
			}
			returnStr += "<option value=\"new\" "+("new".equals(userName) ? " selected=\"selected\"" : "" )+">New</option>";
			returnStr += "</select>";
		}

		return returnStr;
	}

	public static String getHtmlSelect(ComboBoxField[] fields, String selectedValue, String name, String javaScript, String firstLine){
		String returnStr = "";

		if(fields!=null && fields.length>0){
			returnStr = "<select "+(name!=null ? "id=\"" +name+"\" name=\""+name+"\" " : "")+(javaScript!=null ? javaScript : "")+" >";
			if(firstLine!=null)
				returnStr += "<option>"+firstLine+"</option>";
			returnStr += getConboBoxOption(fields, selectedValue);
			returnStr += "</select>";
		}

		return returnStr;
	}


	public static String getHtmlSelect(List<ForPrice> forPrices, int selectedFor, String name, String script) {

		String returnStr = "<select "+(name!=null ? "id=\"" +name+"\" name=\""+name+"\" " : "")+(script!=null ? script : "")+" >";

		if(forPrices!=null && !forPrices.isEmpty()){
			ComboBoxField[] fields = new ForPrice[forPrices.size()];
			returnStr += getConboBoxOption(forPrices.toArray(fields), ""+selectedFor);
		}else
			returnStr += "<option>Select</option>";
		returnStr += "<option>Add</option>";
		returnStr += "</select>";

		return returnStr;
	}

	private static String getConboBoxOption(ComboBoxField[] fields,	String selectedValue) {
		String returnStr = "";
		for(ComboBoxField cbf:fields){
			String value = cbf.getValue();
			returnStr += "<option value=\""+value+"\" "+(value.equals(selectedValue) ? " selected=\"selected\"" : "" )+">";
			returnStr += cbf.getText();
			returnStr += "</option>";
		}
		return returnStr;
	}

	public static String getHtmlSelect(ComponentToMove[] ctm, int selectedValue, String htmlName, String javaScript) {

		String returnStr = "<select id=\""+htmlName+"\" name=\""+htmlName+"\" "+(javaScript!=null ? javaScript : "")+" >";

		for(ComponentToMove c:ctm){
			int value = c.getId();
			returnStr += "<option value=\""+value+"\" "+(value==selectedValue ? " selected=\"selected\"" : "" )+">";
			returnStr += c.getPartNumber();
			returnStr += "</option>";
		}
		returnStr += "</select>";

		return returnStr;
	}

	public static String getSubmitButton(HttpServletRequest request) {
		return getHtmlElement("submit", request);
	}

	public static String getHtmlElement(String contains, HttpServletRequest request){
		String str = null;
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((str = names.nextElement()).contains(contains))
				break;
			else
				str = null;
		return str;
	}

	public static String getChecked(boolean isChecked) {
		return isChecked ? "checked=\"checked\"" : "";
	}

	public static Map<String, String> getHtmlFields(String starWith, HttpServletRequest request) {

		Map<String, String> m = new HashMap<>();
		String name;

		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((name = names.nextElement()).startsWith(starWith))
				m.put(name.substring(starWith.length()), request.getParameter(name));

		return m;
	}
}
