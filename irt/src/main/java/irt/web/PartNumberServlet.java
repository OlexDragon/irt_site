package irt.web;

import irt.data.Browser;
import irt.data.CookiesWorker;
import irt.data.HTMLWork;
import irt.data.ToDoClass;
import irt.data.ToDoClass.ToDo;
import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.components.Unknown;
import irt.data.dao.ComponentDAO;
import irt.data.partnumber.PartNumber;
import irt.data.partnumber.PartNumberDetails;
import irt.data.purchase.Purchase;
import irt.data.user.LogIn;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.table.OrderBy;
import irt.work.TextWorker.PartNumberFirstChar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartNumberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger logger = LogManager.getLogger();

	private static final String COMPONENT_ID = "component";
	public static final String TABLE = "table";
	public static final String HTTP_ADDRESS = "part-numbers";

	private RequestDispatcher jsp;

	private final ComponentDAO componentDAO = new ComponentDAO();

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/part-number.jsp");
//		logger.debug("init(...); ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		Browser browser = Browser.getBrowser(request);

		String tmpStr = request.getParameter("pn");//Get PartNumber from address bar

		Component component;
		if(tmpStr!=null && !tmpStr.isEmpty()){
			component = PartNumber.parsePartNumber(tmpStr);
			CookiesWorker.addCookie(request, response, COMPONENT_ID, component.getId(), 7*24*60*60);
		}else
			component = cookieToComponent(request);

		if(component==null)
			component = new Unknown();

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		partNumber.setComponent(component);

		logger.trace("exit with:\n\t"
				+ "component:\t{},\n\t"
				+ "browser:\t{},\n\t"
				+ "partNumber:\t{}\n\t"
				+ "tmpStr:\t{}",
				component,
				browser,
				partNumber,
				tmpStr);

		ToDoClass search = getToDoClass(request, TABLE);
		OrderBy orderBy = getOrderBy(request, response);
		if (search != null) {
			if (search.getCommand() == ToDo.SEARCH) {
				String value = search.getValue();
				if (value != null) {
					try {
						Data parseData = Component.parseData(value);
						if (parseData != null) {
							if (orderBy != null && !orderBy.equals(parseData.getOrderBy())) {
								parseData.setOrderBy(orderBy);
								search.setValue(parseData.toString());
								CookiesWorker.addCookie(request, response, TABLE, search, 7 * 24 * 60 * 60);
							}
						}
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			}else if(search.getCommand() == ToDo.PROJECT_SERARCH)
				component.setOrderBy(orderBy);
		}

		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("component", component);
		request.setAttribute("browser", browser);
		request.setAttribute("partNumber", partNumber);
		request.setAttribute("search", search);
		jsp.forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		logger.entry();

		LogIn logIn;
		try { logIn = UsersLogsIn.getLogIn(request); } catch (GeneralSecurityException e) { throw new RuntimeException(e); }
		UserBean userBean = logIn!=null ? logIn.getUserBean() : new UserBean();

		Browser browser = Browser.getBrowser(request);

		Component component = cookieToComponent(request);

		String pnt = request.getParameter("pnText");
	
		String pressedButton = HTMLWork.getSubmitButton(request);

		ToDoClass search = getToDoClass(request, TABLE);

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());

		logger.trace("\n\t"
				+ "browser:\t{}\n\t"
				+ "userBean:\t{}\n\t"
				+ "logIn:\t{}\n\t"
				+ "component:\t{},\n\t"
				+ "pnt:\t{},\n\t"
				+ "pressedButton:\t{},\n\t"
				+ "partNumber:\t{}",
				browser,
				userBean,
				logIn,
				component,
				pnt,
				pressedButton,
				partNumber);

		String tmpStr;
		if(pressedButton!=null)
		switch(pressedButton){
		case "submit_to_text":
			component = getComponent(request,  component, userBean.isAdmin() && userBean.isEditing());
			CookiesWorker.addCookie(request, response, COMPONENT_ID, component.getId(), 7*24*60*60);
			break;
		case "submit-add":
			if(userBean!=null && userBean.isEditing())
				insertOrUpdate(request, userBean, component);
			else{
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
			break;
		case "submit-search":
			component = getComponent(request,  component, userBean.isAdmin() && userBean.isEditing());
			component.resetSequentialNunber();
			search = new ToDoClass(ToDo.SEARCH, component);
			CookiesWorker.addCookie(request, response, TABLE, search, 7*24*60*60);
			break;
		case "submit-price":
			search = new ToDoClass(ToDo.PRICE, component.getId());
			CookiesWorker.addCookie(request, response, TABLE, search, 7*24*60*60);
			break;
		case "submit_show":
			search = new ToDoClass(ToDo.PROJECT_SERARCH, "___"+component.getPartNumber().substring(3,9)+"%");
			CookiesWorker.addCookie(request, response, TABLE, search, 7*24*60*60);
			break;
		case "submit-cancel":
			component = new PartNumberDetails(null).getComponent(component.getClassId());
			CookiesWorker.addCookie(request, response, COMPONENT_ID, component.getId(), 7*24*60*60);
			break;
		case "submit_add_link":
			if(userBean.getID()>0){
				if(userBean.isEditing()){
					response.sendRedirect("add-link");
					return;
				}else
					component.getError().setErrorMessage("You do not have permission.");
			}else{
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
		case "submit-bom":
			response.sendRedirect("product_structure?pn="+component.getPartNumber()+"&bom=true");
			return;
		case "submit-pdf":
			response.sendRedirect("product_structure?pn="+component.getPartNumber()+"&pdf=true&bom=true");
			return;
		case "submit-excel":
			response.sendRedirect("product_structure?pn="+component.getPartNumber()+"&excel=true&bom=true");
			return;
		case "submit-where":
			response.sendRedirect("product_structure?pn="+component.getPartNumber()+"&bom=false");
			return;
		case "submit-alt":
			response.sendRedirect("alt-mfr-pns");
			return;
		case "submit-purchase":
			response.sendRedirect("purchase");
			return;
		case "submit-add-to-purchase":
			List<String> names = Init.getFieldsNames(request, "checked");

			tmpStr = CookiesWorker.getCookieValue(request, "purchase");
			Purchase purchase = null;
			if(tmpStr!=null) {
				purchase = Purchase.parsePurchase(tmpStr);
			}
			if(purchase==null)
				purchase = new Purchase();

			if(names.isEmpty())
				purchase.addComponent(component);
			else
				purchase.addComponents(names);
			purchase.getPurchaseOrder().setEdit(true);
			CookiesWorker.addCookie(request, response, "purchase", purchase, 7*24*60*60);
			break;
		case "submit-move":
			names = Init.getFieldsNames(request, "checked");
			if(names.size()>0){
				String componentsIds = getComponentsIds(names);
				response.sendRedirect("components_movement?l="+HTTP_ADDRESS+componentsIds);
				return;
			}else{
				int componentId = component.getId();
				if(componentId>0){
					response.sendRedirect("components_movement?l="+HTTP_ADDRESS+"&id="+componentId);
					return;
				}else
					component.getError().setErrorMessage("Enter the part number. <small>(E056)</small>");
			}
			break;
		case "submit_to_wo":
			names = Init.getFieldsNames(request, "checked");
			if(names.size()>0){
				String componentsIds = getComponentsIds(names);
				response.sendRedirect(WorkOrderServlet.HTTP_ADDRESS+"?l="+HTTP_ADDRESS+componentsIds);
				return;
			}else{
				int componentId = component.getId();
				if(componentId>0){
					response.sendRedirect(WorkOrderServlet.HTTP_ADDRESS+"?l="+HTTP_ADDRESS+"&id="+componentId);
					return;
				}else
					component.getError().setErrorMessage("Enter the part number. <small>(E056)</small>");
			}
			break;
		case "submit-part":
			if(!component.getPartNumber().equals(pnt.replaceAll("-", ""))){
				if(new ComponentDAO().setSchematicPart(userBean.getID(), component.getId(), pnt.toUpperCase()))
					component.getError().setErrorMessage(pnt+ " synbol name is saved","cBlue");
				else
					component.getError().setErrorMessage("Error. <small>(E057)</small>");
			}else
				component.getError().setErrorMessage("Type the symbol name.");
			break;
		case "submit-letter":
			if(!component.getPartNumber().equals(pnt.replaceAll("-", ""))){
				new ComponentDAO().setSchematicLetter(userBean.getID(),component.getId(), pnt.toUpperCase());
				component.getError().setErrorMessage(pnt+ " letter is saved","cBlue");
			}else
				component.getError().setErrorMessage("Type the schematic letter."); 
			break;
		case "submit_qty_add":
			if(userBean!=null)
				partNumber.addQuantity(request.getParameter("text_qty_description"), request.getParameter("text_qty_set"), userBean);
			else{
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
			break;
		case "submit_location":
			if(userBean!=null)
				partNumber.setLocation(request.getParameter("text_location"), userBean);
			else{
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
			break;
		case "submit-parse":
			if (pnt != null){
				component = PartNumber.parsePartNumber(pnt);
				CookiesWorker.addCookie(request, response, COMPONENT_ID, component.getId(), 7*24*60*60);
				break;
			}
		default:
			if (request.getParameter("computer-choice") != null) {
				String newUser = request.getParameter("new-user");
				PartNumber partNumberTmp = PartNumber.getPartNumber((newUser == null || newUser.length() == 0) ? request.getParameter("selected-user") : newUser);
				if (partNumberTmp != null)
					partNumber = partNumberTmp;
			}
		}

		logger.trace("exit with:\n\t"
				+ "HTTP_ADDRESS:\t{}\n\t"
				+ "component:\t{}\n\tb"
				+ "rowser:\t{}\n\t"
				+ "partNumber:\t{}\n\t"
				+ "search:\t\t{}",
				HTTP_ADDRESS,
				component,
				browser,
				partNumber,
				search);
		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("component", component);
		request.setAttribute("browser", browser);
		request.setAttribute("partNumber", partNumber);
		request.setAttribute("search", search);
		jsp.forward(request, response);
	}

	private Component cookieToComponent(HttpServletRequest request) {

		Component component;
		String tmpStr = CookiesWorker.getCookieValue(request,  COMPONENT_ID);
		if(tmpStr!=null && !(tmpStr = tmpStr.replaceAll("\\D", "")).isEmpty())
			component = componentDAO.getComponent(Integer.parseInt(tmpStr));
		else
			component =  new Unknown();
		return component;
	}

	private ToDoClass getToDoClass(HttpServletRequest request, String cookiesName) {

		String tmpStr = CookiesWorker.getCookieValue(request, cookiesName);

		ToDoClass toDoClass;
		if(tmpStr!=null)
			toDoClass = ToDoClass.parseToDoClass(tmpStr);
		else
			toDoClass = new ToDoClass();

		return toDoClass;
	}

	private void insertOrUpdate(HttpServletRequest request, UserBean userBean, Component component) {
		if (component != null){

			boolean isAdmin = userBean.isAdmin()&&userBean.isEditing();
			component = getComponent(request,  component, isAdmin);
			int id = userBean.getID();

			if ((component.getId()<=0 && new ComponentDAO().insert(component, id)) || (component.getId()>0 && new ComponentDAO().update(component, id, isAdmin)))
				component.getError().setErrorMessage(component.getPartNumberF()+" - have been added.");
			else
				component.getError().setErrorMessage("Database Error. <small>(E036)</small>");
		};
	}

	private String getComponentsIds(List<String> names) {

		String ids = "";

		if (names != null)
			for(String s:names){
				s = s.replaceAll("[^\\d]", "");
					if(!s.isEmpty()){
						if(!ids.isEmpty())
							ids += ',';
						else
							ids += "&ids=";
						ids += s;
					}
				}
		return ids;
	}

	private Component getComponent(HttpServletRequest req, Component component, boolean isAdmin) {
		logger.entry(isAdmin, component);

		String selectedFirst = req.getParameter("first");
		String selectedSecond = req.getParameter("second");

		logger.trace("\n\t"
				+ "selectedFirst:\t'{}'\n\t"
				+ "selectedSecond:\t'{}'",
				selectedFirst,
				selectedSecond);

		boolean isSet = true;

		Component c;
		if (selectedFirst != null && !selectedFirst.isEmpty() && selectedFirst.charAt(0) != '-') {

			char firstChar = PartNumberFirstChar.valueOf(Integer.parseInt(selectedFirst)).getFirstDigit().getFirstChar();

			if (	component == null ||
					component.getClassId().charAt(0) != firstChar ||
					selectedSecond == null ||
					selectedSecond.equals("-"))

				c = new PartNumberDetails(component).getComponent(selectedFirst);
			else
				c = new PartNumberDetails(component).getComponent(firstChar + selectedSecond);

			logger.trace("\n\tcomponent 1\t{}", c);

			if (c != null && (component==null || c==component)) {

				for (int i = 0; i < c.getTitleSize(); i++)
					isSet = c.setValue(i, req.getParameter("arg" + i)) && isSet;

				logger.trace("\n\tcomponent 2\t{}", c);

			} else
				isSet = false;

			if (!isSet)
				c.getError().setErrorMessage("All fields must be filled. <small>(E034)</small>");
			else if (c == null)
				component.getError().setErrorMessage("Coming soon");
		}else
			c = new Unknown();

		return logger.exit(c);
	}

	private OrderBy getOrderBy(HttpServletRequest request, HttpServletResponse response) {

		String orderByStr = request.getParameter("ob");//order by
		OrderBy orderBy = OrderBy.parseOrderBy(CookiesWorker.getCookieValue(request, "ob"));

		logger.trace("ob={}; orderBy={}", orderByStr, orderBy);

		if(orderByStr!=null){
			orderBy.setOrderBy(orderByStr);
			CookiesWorker.addCookie(request, response, "ob", orderBy, 7*24*60*60);
		}

		return logger.exit(orderBy);
	}
}