package irt.web;

import irt.data.CookiesWorker;
import irt.data.HTMLWork;
import irt.data.Jackson;
import irt.data.companies.Company;
import irt.data.components.Component;
import irt.data.components.movement.ComponentQtyBean;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsMovement;
import irt.data.components.movement.ComponentsMovementBean;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.dao.CompanyDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ComponentsMovementDAO;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.work.HttpWorker;
import irt.work.TextWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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

	private static final String COMPOMENTS_MOVEMENT_BEAN = "cmpsMvmntBn";

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

	private final ComponentDAO 			componentDAO 			= new ComponentDAO();
	private final ComponentsMovementDAO componentsMovementDAO 	= new ComponentsMovementDAO();
	private final CompanyDAO 			companyDAO 				= new CompanyDAO();

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
			logger.catching(e);
			throw new RuntimeException(e);
		}

		ComponentsMovementBean cmb = addRequesrParameters(request, response);
		ComponentsMovement cm = getComponentsMovement(cmb, request, response);

		logger.trace("\n\t"
				+ "componentsMovementBean:\t{}\n\t"
				+ "cm:\t{}",
				cmb,
				cm);

		request.setAttribute("back_page", HTTP_ADDRESS);
		request.setAttribute("cm", cm);
		jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

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

		ComponentsMovementBean cmb = getComponentsMovementBean(request);
//
//		Long componentId 		= HttpWorker.getId(request);
//		Long[] componentsIds 	= HttpWorker.getIds(request);
//
//		cm.setShowAll(request.getParameter("show_all")!=null);
		String pn = request.getParameter("pn");
		String error = null;
			//		String link = request.getParameter("l");
//
//		Place p = null;
//		if(link!=null && !link.isEmpty() && cm.getFrom()==null){
//			p = ComponentsMovement.getPlace(cm, link);
//			cm.setFrom(p);
//		}
//
//		logger.trace("\n\t"
//				+ "userBean\t{}\n\t"
//				+ "cm\t{}\n\t"
//				+ "componentId\t{}\n\t"
//				+ "componentsIds\t{}\n\t"
//				+ "pn\t{}\n\t"
//				+ "link\t{}\n\t"
//				+ "p\t{}\n\t",
//				userBean,
//				cm,
//				componentId,
//				componentsIds,
//				pn,
//				link,
//				p);
//
//		if (link==null || link.isEmpty() || cm.getFrom().equals(p)) {
//
			switch(getSubmitButton(request)){
			case "submit_cancel":
				cmb = new ComponentsMovementBean();
				CookiesWorker.removeCookiesStartWith(request, response, COMPOMENTS_MOVEMENT_BEAN);
				break;
			case "submit":
				setComponentsMovementBean(request, cmb);
				CookiesWorker.addCookie(request, response, COMPOMENTS_MOVEMENT_BEAN, Jackson.objectToJsonString(cmb), 7*24*60*60);
				break;
			case "submit_move":
				ComponentsMovement cm = getComponentsMovement(cmb, request, response);
				cm.setWho(userBean);
				if(cm.move(request)){
					cmb = new ComponentsMovementBean();
					CookiesWorker.removeCookiesStartWith(request, response, COMPOMENTS_MOVEMENT_BEAN);
				}
				break;
			case "submit_reset":
				cmb.setPlaceFromId(null);
				cmb.setPlaceToId(null);
				CookiesWorker.addCookie(request, response, COMPOMENTS_MOVEMENT_BEAN, Jackson.objectToJsonString(cmb), 7*24*60*60);
				break;
//			case "submit_view_kit":
//				setCompponentsToMove(request, cm);
//				cm.getComponentsKit();
//				break;
//			case "submit_add_to_kit":
//				cm.setWho(userBean);
//				setCompponentsToMove(request, cm);
//				if(new KitDAO().addToKIT(cm))
//					cm.clean();
//				break;
//			case "submit_create_kit":
//				if(pn!=null && !pn.isEmpty()){
//					if(!new KitDAO().createNewKIT(userBean.getID(), pn))
//						error.setErrorMessage("This KIT Name already exist. <small>(E052)</small>");
//				}else
//					error.setErrorMessage("Enter the KIT Name. <small>(E053)</small>");
//				break;
			case "submit_add":
				if(pn!=null && !pn.isEmpty()){
					int componentId = componentDAO.getComponentId(pn);
					if(componentId>0){
						cmb.getComponentsToMove().put(componentId, new ComponentQtyBean().setComponentId(componentId).setQtyToMove(Integer.MAX_VALUE));
						CookiesWorker.addCookie(request, response, COMPOMENTS_MOVEMENT_BEAN, Jackson.objectToJsonString(cmb), 7*24*60*60);
					}else
						error = "This Part number does not exists. <small>(E074)</small>";
				}else
					error = "Enter the part number. <small>(E054)</small>";
				break;
//			case "submit_history":
//				cm.historyOf(pn);
//				break;
//			default:
//				if(link!=null){
//					if(componentsIds!=null)
//						cm.add(componentsIds, link);
//					else if(componentId != null)
//						cm.add(componentId, link);
//				}
//				logger.trace("id={}, pn={}, link={}", componentId, pn, link);
			}
//		}else
//			error.setErrorMessage("You did not finish move from "+cm.getFrom().getName()+". <small>(E055)</small>"); 

		ComponentsMovement cm = getComponentsMovement(cmb, request, response);
		if(error!=null)
			cm.getErrorMessage().setErrorMessage(error);

		request.setAttribute("back_page", HTTP_ADDRESS);
		request.setAttribute("cm", cm);
		jsp.forward(request, response);
	}

	private ComponentsMovementBean addRequesrParameters(HttpServletRequest request, HttpServletResponse response) {
		ComponentsMovementBean cmb = getComponentsMovementBean(request);

		Integer[] componentsIds = null;
		Integer componentId 	= HttpWorker.getId(request);

		String link = request.getParameter("l");
		if(link!=null && !link.isEmpty() && (cmb.getPlaceFromId()==null || cmb.getPlaceFromId()==0))
			cmb.setPlaceFromId(ComponentsMovement.getPlaceId(link));

		Map<Integer, ComponentQtyBean> cstm = cmb.getComponentsToMove();
		if(componentId!=null){
			cstm.put(componentId, new ComponentQtyBean().setComponentId(componentId).setQtyToMove(Integer.MAX_VALUE));
			componentsMovementBeanToCookies(request, response, cmb);
		}else{
			componentsIds = HttpWorker.getIds(request);
			if(componentsIds!=null){
				for(Integer cId:componentsIds)
					cstm.put(cId, new ComponentQtyBean().setComponentId(cId).setQtyToMove(Integer.MAX_VALUE));
				componentsMovementBeanToCookies(request, response, cmb);
			}
		}

		logger.trace("\n\t"
				+ "componentsIds:\t{}\n\t"
				+ "componentId:\t{}",
				componentsIds,
				componentId);

		return cmb;
	}

	private void componentsMovementBeanToCookies(HttpServletRequest request,HttpServletResponse response, ComponentsMovementBean cmb) {
		try {
			CookiesWorker.addCookie(request, response, COMPOMENTS_MOVEMENT_BEAN, Jackson.objectToJsonString(cmb), 7*24*60*60);
		} catch (IOException e) {
			logger.catching(e);
		}
	}

	private ComponentsMovementBean getComponentsMovementBean(HttpServletRequest request) {

		String cookieValue = CookiesWorker.getCookieValue(request, COMPOMENTS_MOVEMENT_BEAN);
		ComponentsMovementBean cmb = null;

		if(cookieValue!=null)
			try {
				cmb = Jackson.jsonStringToObject(ComponentsMovementBean.class, cookieValue);
			} catch (IOException e) {
				logger.catching(e);
			}

		if(cmb==null)
			cmb = new ComponentsMovementBean();

		return cmb;
	}

	private ComponentsMovement getComponentsMovement(ComponentsMovementBean cmb, HttpServletRequest request, HttpServletResponse response) {
		logger.entry("\n\t{}", cmb);

		ComponentsMovement componentsMovement = new ComponentsMovement();
		if(cmb!=null){
			setFrom(componentsMovement, cmb);
			setFromDetail(componentsMovement, cmb);
			setTo(componentsMovement, cmb);
			setToDetail(componentsMovement, cmb);
			componentsMovement.setDate(cmb.getDate());
			componentsMovement.setDescription(cmb.getDescription());
			componentsMovement.setStatus(cmb.getStatus());
			setComponentsToMove(componentsMovement, cmb, request, response);
		}

		return logger.exit(componentsMovement);
	}

	private void setComponentsToMove(ComponentsMovement componentsMovement, ComponentsMovementBean cmb, HttpServletRequest request, HttpServletResponse response) {
		Map<Integer, ComponentQtyBean> componentsToMove = cmb.getComponentsToMove();

		if(componentsToMove!=null && !componentsToMove.isEmpty()){
			Integer[] keySet = componentsToMove.keySet().toArray(new Integer[componentsToMove.size()]);
			
			boolean changed = false;
			for(Integer key:keySet){
				Component component = componentDAO.getComponent(key);
				ComponentToMove ctm = new ComponentToMove(component);

				setAvailableQty(ctm, cmb);

				ctm.setQuantityToMove(componentsToMove.get(key).getQtyToMove(), false);
				if(componentsMovement.add(ctm)==null){
					changed = true;
					logger.trace("\n\tRevoved:\n\t{}", ctm);
					componentsToMove.remove(key);
				}
			}

			if(changed)
				componentsMovementBeanToCookies(request, response, cmb);
		}
	}

	private void setAvailableQty(ComponentToMove ctm, ComponentsMovementBean cmb) {
		logger.trace("\n\t{}\n\t{}", ctm, cmb);
		Byte placeFromId = cmb.getPlaceFromId();
		Integer companyFromId = cmb.getCompanyFromId();
		int componentId = ctm.getId();
		if(componentId>0 && placeFromId!=null && placeFromId!=Company.STOCK && companyFromId!=null && companyFromId>0)
			ctm.setStockQuantity(componentsMovementDAO.getComponyComponentQuantity(companyFromId, componentId));
	}

	private void setTo(ComponentsMovement componentsMovement, ComponentsMovementBean cmb) {
		Byte placeToId = cmb.getPlaceToId();
		if(placeToId!=null)
			componentsMovement.setTo(placeToId);
	}

	private void setFrom(ComponentsMovement componentsMovement, ComponentsMovementBean cmb) {
		Byte placeFromId = cmb.getPlaceFromId();
		if(placeFromId!=null && placeFromId>0)
			componentsMovement.setFrom(placeFromId);
	}

	private void setToDetail(ComponentsMovement componentsMovement, ComponentsMovementBean cmb) {
		Integer companyToId = cmb.getCompanyToId();
		if(companyToId!=null)
			componentsMovement.setToDetail(companyDAO.getCompany(companyToId));
	}

	private void setFromDetail(ComponentsMovement componentsMovement, ComponentsMovementBean cmb) {
		Integer companyFromId = cmb.getCompanyFromId();
		if(companyFromId!=null)
			componentsMovement.setFromDetail(companyDAO.getCompany(companyFromId));
	}

	private void setComponentsMovementBean(HttpServletRequest request, ComponentsMovementBean cmb) {
		logger.trace("\n\t{}", cmb);

		cmb.setDescription(request.getParameter(TXT_DESCRIPTION));

		Integer intTmp = TextWorker.toInt(request.getParameter(MOVE_FROM));
		logger.trace("\n\t MOVE_FROM = {}", intTmp);
		if(intTmp!=null)
			cmb.setPlaceFromId((byte)(intTmp&0xFF));

		intTmp = TextWorker.toInt(request.getParameter(MOVE_FROM_DETAIL));
		logger.trace("\n\t MOVE_FROM_DETAIL = {}", intTmp);
		if(intTmp!=null)
			cmb.setCompanyFromId(intTmp);

		intTmp = TextWorker.toInt(request.getParameter(MOVE_TO));
		logger.trace("\n\t MOVE_TO = {}", intTmp);
		if(intTmp!=null)
			cmb.setPlaceToId((byte)(intTmp&0xFF));

		intTmp = TextWorker.toInt(request.getParameter(MOVE_TO_DETAIL));
		logger.trace("\n\t MOVE_TO_DETAIL = {}", intTmp);
		if(intTmp!=null)
			cmb.setCompanyToId(intTmp);

		cmb.setStatus(ComponentsMovement.CREATING);
		logger.trace("\n\t{}", cmb);

		setQuantityToMove(cmb, request);
	}
//
//	private void setCompponentsToMove(HttpServletRequest request, ComponentsMovementBean cmb) {
//		cmb.setDescription(request.getParameter(TXT_DESCRIPTION));
//		cmb.setFrom		(request.getParameter(MOVE_FROM			));
//		cmb.setFromDetail(request.getParameter(MOVE_FROM_DETAIL	));
//		cmb.setTo		(request.getParameter(MOVE_TO			));
//		cmb.setToDetail	(request.getParameter(MOVE_TO_DETAIL	));
//		cmb.setQuantityToMove(request);
//		cmb.setStatus(ComponentsMovement.CREATING);
//	}

	private void setQuantityToMove(ComponentsMovementBean cmb, HttpServletRequest request) {
		ComponentsQuantity htmlFields = HTMLWork.getHtmlFields(request,"qty");
		if(!htmlFields.isEmpty())
			setQuantityToMove(cmb.getComponentsToMove(), htmlFields.getComponentsQuantity());
	}

	private void setQuantityToMove(Map<Integer, ComponentQtyBean> componentsToMove, List<ComponentQuantity> componentsQuantity) {
		logger.trace("\n\t{}\n\t{}", componentsToMove, componentsQuantity);

		for(ComponentQuantity cq:componentsQuantity){
			int componentId = cq.getId();
			if(componentsToMove.containsKey(componentId))
				componentsToMove.get(componentId).setQtyToMove(cq.getQuantityToMove());
			else
				componentsToMove.put(componentId, new ComponentQtyBean().setComponentId(componentId).setQtyToMove(cq.getQuantityToMove()));
		}

		logger.trace("\n\t{}", componentsToMove);
	}

	private String getSubmitButton(HttpServletRequest request) {
		String str = "";
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((str = names.nextElement()).contains("submit"))
				break;

		return str;
	}
}
