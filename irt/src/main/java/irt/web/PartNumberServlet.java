package irt.web;

import irt.data.Browser;
import irt.data.CookiesWorker;
import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.ToDoClass;
import irt.data.ToDoClass.ToDo;
import irt.data.components.Component;
import irt.data.components.Unknown;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;
import irt.data.partnumber.PartNumber;
import irt.data.partnumber.PartNumberDetails;
import irt.data.purchase.Purchase;
import irt.data.user.LogIn;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.table.OrderBy;

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
import org.apache.logging.log4j.core.Logger;

public class PartNumberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger logger = (Logger) LogManager.getLogger();

	public static final String HTTP_ADDRESS = "part-numbers";

	private RequestDispatcher jsp;

//	private Browser browser;
	private Error error = new Error();

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

		logger.entry(request, response, browser, tmpStr);

		Component component;
		if(tmpStr!=null && !tmpStr.isEmpty()){
			component = PartNumber.parsePartNumber(tmpStr);
			CookiesWorker.addCookie(request, response, "component", component.toString(), 7*24*60*60);
		}else{
			tmpStr = CookiesWorker.getCookieValue(request,  "component");
			if(tmpStr!=null)
				try { component = (Component) Component.parseData(tmpStr); } catch (CloneNotSupportedException e) { new ErrorDAO().saveError(e, "PartNumberServlet.doGet"); throw new RuntimeException(e); }
			else
				component =  new Component();
		}

		ToDoClass toDoClass = getToDoClass(request, "TODO");

		OrderBy orderBy = getOrderBy(request);
		if(orderBy == null)
			orderBy = new OrderBy("`part_number`");
		logger.trace("{}, {}", tmpStr, orderBy);

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		partNumber.setComponent(component);

		logger.trace("exit with:\n\tcomponent:\t{},\n\tbrowser:\t{},\n\ttoDoClass:\t{},\n\tpartNumber:\t{}", component, browser, toDoClass, partNumber);

		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("component", component!=null ? component : new Unknown());
		request.setAttribute("browser", browser);
		request.setAttribute("todo", toDoClass);
		request.setAttribute("partNumber", partNumber);
		request.setAttribute("search", getToDoClass(request, "SEARCH"));
		jsp.forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		logger.entry(request, response);

		LogIn logIn;
		try { logIn = UsersLogsIn.getLogIn(request); } catch (GeneralSecurityException e) { throw new RuntimeException(e); }
		UserBean userBean = logIn!=null ? logIn.getUserBean() : new UserBean();

		Browser browser = Browser.getBrowser(request);

		Component component = null;
		String tmpStr = CookiesWorker.getCookieValue(request,  "component");
		logger.trace("{}, {}, {}, {}", browser, userBean, logIn, tmpStr);

		if(tmpStr!=null)
			try {
				component = (Component) Component.parseData(tmpStr);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

		String pnt = request.getParameter("pnText");
	
		String pressedButton = HTMLWork.getSubmitButton(request);

		ToDoClass toDoClass = getToDoClass(request, "TODO");
		ToDoClass search = getToDoClass(request, "SEARCH");

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		logger.trace("{}, {}, {}, {}", component, pnt, pressedButton, tmpStr);
		logger.trace("{}, {}", toDoClass, partNumber);

		if(pressedButton!=null)
		switch(pressedButton){
		case "submit_to_text":
			component = getComponent(request,  component, userBean.isAdmin() && userBean.isEditing());
			CookiesWorker.addCookie(request, response, "component", component, 7*24*60*60);
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
			CookiesWorker.addCookie(request, response, "SEARCH", search, 7*24*60*60);
			break;
		case "submit-price":
			toDoClass = new ToDoClass(ToDo.PRICE, component.getId());
			CookiesWorker.addCookie(request, response, "TODO", toDoClass, 7*24*60*60);
			break;
		case "submit_show":
			toDoClass = new ToDoClass(ToDo.PROJECT_SERARCH, "___"+component.getPartNumber().substring(3,9)+"%");
			CookiesWorker.addCookie(request, response, "TODO", toDoClass, 7*24*60*60);
			break;
		case "submit-cancel":
			component = new PartNumberDetails(null).getComponent(component.getClassId());
			CookiesWorker.addCookie(request, response, "component", component, 7*24*60*60);
			break;
		case "submit_add_link":
			if(userBean.getID()>0){
				if(userBean.isEditing()){
					response.sendRedirect("add-link");
					return;
				}else
					component.setError("You do not have permission.");
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
			response.sendRedirect("product_structure?pn="+component.getPartNumber());
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
					component.setError("Enter the part number. <small>(E056)</small>");
			}
			break;
		case "submit-part":
			if(!component.getPartNumber().equals(pnt.replaceAll("-", ""))){
				if(new ComponentDAO().setSchematicPart(userBean.getID(), component.getId(), pnt.toUpperCase()))
					component.setError(pnt+ " synbol name is saved","cBlue");
				else
					component.setError("Error. <small>(E057)</small>");
			}else
				component.setError("Type the symbol name.");
			break;
		case "submit-letter":
			if(!component.getPartNumber().equals(pnt.replaceAll("-", ""))){
				new ComponentDAO().setSchematicLetter(userBean.getID(),component.getId(), pnt.toUpperCase());
				component.setError(pnt+ " letter is saved","cBlue");
			}else
				component.setError("Type the schematic letter."); 
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
				CookiesWorker.addCookie(request, response, "component", component, 7*24*60*60);
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

		logger.trace("exit with:\n\tHTTP_ADDRESS:\t{}\n\tcomponent:\t{},\n\tbrowser:\t{},\n\ttoDoClass:\t{},\n\tpartNumber:\t{}\n\tsearch:\t\t{}",
				HTTP_ADDRESS,
				component,
				browser,
				toDoClass,
				partNumber,
				search);
		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("component", component);
		request.setAttribute("browser", browser);
		request.setAttribute("todo", toDoClass);
		request.setAttribute("partNumber", partNumber);
		request.setAttribute("search", search);
		jsp.forward(request, response);
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
				component.setError(component.getPartNumberF()+" - have been added.");
			else
				component.setError("Database Error. <small>(E036)</small>");
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

		String descriptionFirst = req.getParameter("first");
		String descriptionSecond = req.getParameter("second");
		boolean isSet = true;

		Component c;
		if (descriptionFirst != null && !descriptionFirst.isEmpty() && descriptionFirst.charAt(0) != '-') {
			if (component == null || component.getClassId().charAt(0) != descriptionFirst.charAt(0) || descriptionSecond == null || descriptionSecond.equals("-"))
				c = new PartNumberDetails(component).getComponent(descriptionFirst);
			else
				c = new PartNumberDetails(component).getComponent(descriptionFirst + descriptionSecond);

			if (c != null) {

				if (component != null)
					for (int i = 0; i < c.getTitleSize(); i++)
						isSet = component.setValue(i,
								req.getParameter("arg" + i))
								&& isSet;

			} else
				isSet = false;

			if (!isSet)
				error.setErrorMessage("All fields must be filled. <small>(E034)</small>");
			else if (c == null)
				error.setErrorMessage("Coming soon");
		}else
			c = new Unknown();

		return c;
	}

	private OrderBy getOrderBy(HttpServletRequest request) {

		OrderBy orderBy = OrderBy.getOrderBy(request);

		String orderByStr = request.getParameter("ob");//order by
		if(orderByStr!=null){
			orderBy.setOrderBy(orderByStr);
		}

		return orderBy;
	}
}