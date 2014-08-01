package irt.web;

import irt.data.Error;
import irt.data.companies.Place;
import irt.data.components.movement.ComponentsMovement;
import irt.data.dao.KitDAO;
import irt.data.partnumber.PartNumber;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.work.HttpWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComponentsMovementServlet extends HttpServlet {

	private static final Logger logger = LogManager.getLogger();

	public static final String MOVE_FROM		= "move_from";
	public static final String MOVE_FROM_DETAIL	= "move_from_company";
	public static final String MOVE_TO			= "move_to";
	public static final String MOVE_TO_DETAIL	= "move_to_company";
	public static final String TXT_DESCRIPTION	= "txt_description";

	private static final long serialVersionUID = 1L;

	//	   private Logger logger = Logger.getLogger(this.getClass());
	   private RequestDispatcher jsp;

	   private final String HTTP_ADDRESS = "components_movement";
	   private Error error;

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/move.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(!userBean.isStock()){
				response.sendRedirect("login?bp="+HTTP_ADDRESS);
				return;
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		ComponentsMovement cm = partNumber.getComponentsMovement();
		if(cm==null)
			partNumber.setComponentsMovement(cm = new ComponentsMovement());

		Long componentId 		= HttpWorker.getId(request);
		Long[] componentsIds 	= HttpWorker.getIds(request);

		cm.setShowAll(request.getParameter("show_all")!=null);
		String pn = request.getParameter("pn");
		String link = request.getParameter("l");

		Place p = null;
		if(link!=null && !link.isEmpty() && cm.getFrom()==null){
			p = ComponentsMovement.getPlace(cm, link);
			cm.setFrom(p);
		}

		if (link==null || link.isEmpty() || cm.getFrom().equals(p)) {

			switch(getSubmitButton(request)){
			case "submit_cancel":
				partNumber.getComponentsMovement().clean();
				break;
			case "submit":
				setCompponentsToMove(request, cm);
				break;
			case "submit_move":
				cm.setWho(userBean);
				if(cm.move(request))
					cm.clean();
				break;
			case "submit_reset":
				cm.resetFromTo();
				break;
			case "submit_view_kit":
				setCompponentsToMove(request, cm);
				cm.getComponentsKit();
				break;
			case "submit_add_to_kit":
				cm.setWho(userBean);
				setCompponentsToMove(request, cm);
				if(new KitDAO().addToKIT(cm))
					cm.clean();
				break;
			case "submit_create_kit":
				if(pn!=null && !pn.isEmpty()){
					if(!new KitDAO().createNewKIT(userBean.getID(), pn))
						error.setErrorMessage("This KIT Name already exist. <small>(E052)</small>");
				}else
					error.setErrorMessage("Enter the KIT Name. <small>(E053)</small>");
				break;
			case "submit_add":
				if(pn!=null && !pn.isEmpty())
					cm.add(pn, link);
				else
					error.setErrorMessage("Enter the part number. <small>(E054)</small>");
				break;
			case "submit_history":
				cm.historyOf(pn);
				break;
			default:
				if(link!=null){
					if(componentsIds!=null)
						cm.add(componentsIds, link);
					else if(componentId != null)
						cm.add(componentId, link);
				}
				logger.trace("id={}, pn={}, link={}", componentId, pn, link);
			}
		}else
			error.setErrorMessage("You did not finish move from "+cm.getFrom().getName()+". <small>(E055)</small>"); 

		request.setAttribute("back_page", HTTP_ADDRESS);
		request.setAttribute("cm", cm);
		jsp.forward(request, response);
	}

	private void setCompponentsToMove(HttpServletRequest request, ComponentsMovement cm) {
		cm.setDescription(request.getParameter(TXT_DESCRIPTION));
		cm.setFrom		(request.getParameter(MOVE_FROM			));
		cm.setFromDetail(request.getParameter(MOVE_FROM_DETAIL	));
		cm.setTo		(request.getParameter(MOVE_TO			));
		cm.setToDetail	(request.getParameter(MOVE_TO_DETAIL	));
		cm.setQuantityToMove(request);
		cm.setStatus(ComponentsMovement.CREATING);
	}

	private String getSubmitButton(HttpServletRequest request) {
		String str = "";
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((str = names.nextElement()).contains("submit"))
				break;

		return str;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
}
