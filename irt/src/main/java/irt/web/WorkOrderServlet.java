package irt.web;

import irt.data.Browser;
import irt.data.CookiesWorker;
import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.data.workorder.WorkOrder;
import irt.work.HttpWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class WorkOrderServlet extends HttpServlet {
	public static final String COOKIE_NAME_MENU = "workOrderMenu";

	private static final long serialVersionUID = 1L;

	private final Logger logger = (Logger) LogManager.getLogger();

	public static final String HTTP_ADDRESS = "work-orders";

	private RequestDispatcher jsp;

//	private Browser browser;
	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/work-orders.jsp");
//		logger.debug("init(...); ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(!userBean.isWorkOrder()){
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		String selectedMenu = getParameter(request, response, "m", COOKIE_NAME_MENU, "Don't need");

		String cookieName = WorkOrder.class.getSimpleName();
		String cookieValue = CookiesWorker.getCookieValue(request,  cookieName);

		WorkOrder workOrder = new WorkOrder();
		if(cookieValue!=null)
			try {
				workOrder = WorkOrder.parseWorkOrder(cookieValue);
			} catch (Exception e) {
				logger.catching(e);
			}

		if(		setIds(request, workOrder) |
				buttonAction(request, workOrder) |
				fillFields(request, workOrder))

			CookiesWorker.addCookie(request, response, cookieName, workOrder, 7*24*60*60);

		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("browser",  Browser.getBrowser(request));
		request.setAttribute("error", error);
		request.setAttribute("menu", selectedMenu);
		request.setAttribute("wo", workOrder);
		request.setAttribute("prefix", getParameter(request, response, "prefix", "prefix", "WO"));
		jsp.forward(request, response);
	}

	private boolean fillFields(HttpServletRequest request, WorkOrder workOrder) {

		String description = request.getParameter("wo_description");
		String woDescription = workOrder.getDescription();

		boolean haveToSave = false;

		if(description!=null && !description.equals(woDescription)){
			workOrder.setDescription(description);
			haveToSave = true;
		}
		
		return haveToSave;
	}

	private boolean buttonAction(HttpServletRequest request, WorkOrder workOrder) {
		String pressedButton = HTMLWork.getSubmitButton(request);


		boolean haveToSaveWO = false;
		if (pressedButton != null) {

			Long id = null;
			if(pressedButton.startsWith("submit_del_")){
				id = Long.parseLong(pressedButton.substring("submit_del_".length()));
				pressedButton = "submit_del_";
			}

			switch (pressedButton) {
			case "submit_clear":
				workOrder.clear();
				haveToSaveWO = true;
				break;
			case "submit_del_":
				haveToSaveWO = workOrder.remove(id);
			}
		}

		return haveToSaveWO;
	}

	private boolean setIds(HttpServletRequest request, WorkOrder workOrder) {
		Long id = HttpWorker.getId(request);
		boolean haveToSaveWO = false;
		if(id!=null){
			workOrder.add(id);
			haveToSaveWO = true;
		}else{
			Long[] ids = HttpWorker.getIds(request);
			if(ids!=null){
				workOrder.add(ids);
				haveToSaveWO = true;
			}
		}
		return haveToSaveWO;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doGet(request, response);
	}

	private String getParameter(HttpServletRequest request, HttpServletResponse response, String parameterName, String cookieName, String defaultValue){
		String value = request.getParameter(parameterName);
		if(value!=null)
			CookiesWorker.addCookie(request, response, cookieName, value, 7*24*60*60);
		else{
			value = CookiesWorker.getCookieValue(request, cookieName);
			if(value==null)
				value = defaultValue;
		}
		return value;
	}
}
