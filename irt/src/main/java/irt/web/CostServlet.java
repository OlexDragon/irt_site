package irt.web;

import irt.data.CookiesWorker;
import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.Jackson;
import irt.data.ToDoClass;
import irt.data.ToDoClass.ToDo;
import irt.data.companies.Company;
import irt.data.dao.CompanyDAO;
import irt.data.dao.CostDAO;
import irt.data.purchase.AlternativeComponentBean;
import irt.data.purchase.AlternativeComponentService;
import irt.data.purchase.CostBean;
import irt.data.purchase.CostCompanyBean;
import irt.data.purchase.CostCompanyService;
import irt.data.purchase.CostService;
import irt.data.purchase.CostUnitBean;
import irt.data.purchase.ForPriceBean;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;

import java.io.IOException;
import java.math.BigDecimal;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

@SuppressWarnings("serial")
public class CostServlet extends HttpServlet {

    public static final String PARAMETER_TOP_COMP_ID = "top_comp_id";

    public static final String PARAMETER_COST = "cost";

	protected final Logger logger =LogManager.getLogger();

    private final String httpAddress = "cost";
	private RequestDispatcher jsp;
	private Error error = new Error();

	private RequestParameters parameters = new RequestParameters();

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/cost.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		parameters.getParameters(request, response);

		CostService costService = getCostService(request, response);

		UserBean userBean = getUserBean(request, response);
		if (userBean.isEditCost()) {
			String cookieValue = CookiesWorker.getCookieValue(request, "isEdit");
			if(cookieValue!=null && cookieValue.equals("true"))
				costService.setEdit(true);
		}

		logger.trace("\n\t"
				+ "httpAddress\t{}\n\t"
				+ "costService\t{}\n\t"
				+ "parameters\t{}",
				httpAddress,
				costService,
				parameters);

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("parameters", parameters);
		request.setAttribute("cost", costService);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		parameters.getParameters(request, response);

		CookiesWorker.addCookie(request, response, "position", parameters.getPosition(), 24*60*60);

		CostService costService = getCostService(request, response);

		String fieldName = request.getParameter("what");
		boolean isStartWithFor = fieldName.startsWith("for");
		logger.trace("\n\tfieldName:\t{}", fieldName);

		int componentId = 0;
		boolean isSetFor = false;
		if (fieldName != null)
			if (isStartWithFor) {
				String[] split = fieldName.split(":");
				componentId = Integer.parseInt(split[0].replaceAll("\\D", ""));
				isSetFor = costService.setForPriceIndex(componentId, Integer.parseInt(split[1]));
			} else if (fieldName.startsWith("vendor"))
				costService.setSelectedCompanyIndex(fieldName);
			else if (fieldName.startsWith("mfrPN") || fieldName.startsWith("cpn"))
				costService.setSelectedMfrPNIndex(fieldName);

		int setIndex = Integer.parseInt(request.getParameter("set"));
		costService.setSetIndex(setIndex);

		UserBean userBean = getUserBean(request, response);

		if (userBean.isEditCost()) {
			boolean isEdit = request.getParameter("is_edit") != null;
			CookiesWorker.addCookie(request, response, "isEdit", isEdit, 24*60*60);
			costService.setEdit(isEdit);
			if (isEdit) {
				if (!fieldName.isEmpty()) {
					if (isStartWithFor && !isSetFor)
						setNewPrice(request, response, costService, componentId);
				} else
					setPrices(request, costService);

				buttonAction(request, response, costService, componentId);
			}
		}

		String objectToJsonString = Jackson.objectToJsonString(costService.getCostBean());

		logger.trace("\n\t"
				+ "httpAddress:\t{}\n\t"
				+ "costService:\t{}\n\t"
				+ "parameters:\t{}\n\t"
				+ "objectToJsonString:\t{}",
				httpAddress ,
				costService,
				parameters,
				objectToJsonString);

		if(objectToJsonString.length()<4000)
			CookiesWorker.addCookie(request, response, PARAMETER_COST, objectToJsonString, 24*60*60);
		else
			error.setErrorMessage("Error(E073)");

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("cost", costService);
		request.setAttribute("parameters", parameters);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	private void setPrices(HttpServletRequest request, CostService costService) {
		List<String> names = Init.getFieldsNames(request, "price");
		logger.trace("\n\t{}", names);
		for (String s : names) {
			String price = request.getParameter(s);
			if(isNumber(price))
				costService.setPrices(	Integer.parseInt(s.replaceAll("\\D", "")), price);
			else
				error.setErrorMessage("Is not a Number(E072)");
		}
	}

	private boolean isNumber(String str) {
		logger.entry(str);
		return !str.isEmpty() && str.matches("(\\d+)?(\\.\\d+)?");
	}

	private void setNewPrice(HttpServletRequest request, HttpServletResponse response, CostService costService, int componentId) throws JsonGenerationException, JsonMappingException, IOException {
		String addVandor = request.getParameter("companies");
		String addPrice = request.getParameter("price" + componentId);
		String addFor = request.getParameter("addFor");

		logger.debug("\n\t"
				+ "id:\t{}\n\t"
				+ "addVandor:\t{}\n\t"
				+ "addPrice:\t{}\n\t"
				+ "addFor:\t{}\n\t",
				componentId,
				addVandor,
				addPrice,
				addFor);

		if (!addFor.isEmpty() && !(addFor = addFor.replaceAll("\\D", "")).isEmpty() && !addPrice.isEmpty()) {
			CostCompanyBean costCompanyBean = costService.getCostCompany(componentId);
			AlternativeComponentBean costMfrPN = costService.getCostMfrPN(componentId);
			logger.debug("\n\tid:\t{}\n\t{}\n\t{}", componentId, costMfrPN, costCompanyBean);
			String companyName;
			int companyId;
			if (addVandor.equals("Select")) {
				if(costCompanyBean!=null){
					companyId = costCompanyBean.getId();
					companyName = CostCompanyService.getCompanyName(companyId);
				}else{
					companyId = 0;
					companyName = AlternativeComponentService.getCompanyName(costMfrPN);
				}
			}else {
				Company company = new CompanyDAO().getCompany(Integer.parseInt(addVandor));
				companyId = company.getId();
				companyName = company.getCompanyName();
			}

			if(companyName!=null) {
				costCompanyBean = new CostCompanyBean()
								.setId(companyId);
				CostCompanyService.addForPriceBean(costCompanyBean, new ForPriceBean()
															.setNewPrice(new BigDecimal(addPrice))
															.setForUnits(Integer.parseInt(addFor)));
				costService.add(componentId, costCompanyBean);
				costService.setSelectedIndex(componentId, costCompanyBean);
			}
		} else
			error.setErrorMessage("Company, Price and For fields should be filled");
	}

	private void buttonAction(HttpServletRequest request, HttpServletResponse response, CostService costService, int componentId) {
		String pressedButton = HTMLWork.getSubmitButton(request);
		List<CostUnitBean> costUnitBeans = costService.getCostUnitBeans();

		logger.debug("\n\t{}", costUnitBeans);

		switch(pressedButton){
		case "submit_save_set":
			new CostDAO().saveSet(costService.getSetIndex(), costService.getId(), costUnitBeans);
		case "submit_save":
			if(new CostDAO().save(costUnitBeans)!=0){
				CookiesWorker.removeCookiesStartWith(request, response, PARAMETER_COST);
				costUnitBeans.clear();
			}else
				error.setErrorMessage("Can not be saved.");
			break;
		case "submit_cansel":
			if(!costUnitBeans.isEmpty()) {
				logger.debug("\n\tcomponentId:\t{}\n\t{}", componentId, PARAMETER_COST);
				CookiesWorker.removeCookiesStartWith(request, response, PARAMETER_COST);
				costUnitBeans.clear();
			}
			break;
		case "submit_set":
			costService.setSet(true);
		}
	}

	private UserBean getUserBean(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		return userBean;
	}

	private CostService getCostService(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, JsonParseException, JsonMappingException, IOException {

		String 		componentId = parameters.getTopComponentId();
		ToDoClass 	toDoClass 	= parameters.getToDoClass();
		int cID = componentId!=null ? Integer.parseInt(componentId) : 
											toDoClass!=null && toDoClass.getCommand()==ToDo.PRICE ? Integer.parseInt(toDoClass.getValue()) :
												0;

		CostService costService = getCostServicFromCookies(request);

		logger.debug("\n\t"
				+ "componentId:\t{}\n\t"
				+ "toDoClass:\t{}\n\t"
				+ "cID:\t{}",
				componentId,
				toDoClass,
				cID);

		if(		costService==null ||
				costService.getCostBean()==null ||
				costService.getCostBean().getTopComponentId()!=cID){

			if(componentId == null){
				if(toDoClass != null && toDoClass.getCommand()==ToDo.PRICE) {
					if(costService==null || costService.getComponentId()==0) {
						CostUnitBean costUnitBean = costService!=null ? costService.getCostUnitBean(cID) : null;
						if(costUnitBean==null)
							costService = getComponentCostService(toDoClass.getValue());

						logger.debug("\n\t"
								+ "costUnitBean:\t{}\n\t"
								+ "costService:\t{}",
								costUnitBean,
								costService);
					}
				} else
					costService = new CostService(null);
			}else
				costService = getBOMCostService(componentId);

			costService.setClassId(parameters.getClassId());
		}

		return logger.exit(costService);
	}

	private CostService getCostServicFromCookies(HttpServletRequest request){
		String cookieValue = CookiesWorker.getCookieValue(request, PARAMETER_COST);
		CostService cs = null;
		if(cookieValue!=null) {
			logger.debug("\n\t{}", cookieValue);
			CostBean cb = null;
			try {
				cb = Jackson.jsonStringToObject(CostBean.class, cookieValue);
			} catch (IOException e) {
				logger.catching(e);
			}
			if(cb!=null)
				cs = new CostService(cb);
		}
		return cs;
	}

	private CostService getBOMCostService(String componentId) {
		CostService costService;

		if(componentId==null || componentId.isEmpty() || componentId.equals("Select"))
			costService = new CostService(null);
		else
			costService=new CostDAO().getCostService(componentId);

		return logger.exit(costService);
	}

	private CostService getComponentCostService(String componentId) {
		logger.debug(componentId);
		CostService costService;

		if(componentId==null || componentId.isEmpty()){
			costService = new CostService(null);
		}else
			costService=new CostDAO().getCostService(Integer.parseInt(componentId));

		return logger.exit(costService);
	}

	//******************************************************************************
	public class RequestParameters{

		private String position;
		private String topComponentId;
		private String classId;
		private ToDoClass toDoClass;

		public void getParameters(HttpServletRequest request, HttpServletResponse response) {
			position = request.getParameter("position");
			setClassId(request, response);
			setTopComponentId(request, response);
			setToDoClass(request);
		}

		public String getPosition() {
			return position;
		}

		public String getTopComponentId() {
			return topComponentId;
		}

		private void setTopComponentId(HttpServletRequest request, HttpServletResponse response) {
			topComponentId = request.getParameter(PARAMETER_TOP_COMP_ID);
			if(classId==null || classId.equals("Select")){
				topComponentId = null;
			}else{
				CookiesWorker.addCookie(request, response, "class_id", classId, 24*60*60);
				if(topComponentId==null)
					topComponentId = CookiesWorker.getCookieValue(request, PARAMETER_TOP_COMP_ID);
				else
					CookiesWorker.addCookie(request, response, PARAMETER_TOP_COMP_ID, topComponentId, 24*60*60);
			}
			logger.trace("\n\ttopComponentId:\t{}", topComponentId);
		}

		public String getClassId() {
			return classId;
		}

		private void setClassId(HttpServletRequest request, HttpServletResponse response) {
			classId = request.getParameter("class_id");
			if(classId==null)
				classId = CookiesWorker.getCookieValue(request, "class_id");
			else if(classId.equals("Select"))
				CookiesWorker.removeCookiesStartWith(request, response, "class_id");

			logger.trace("\n\tclassId:\t{}", classId);
		}	

		public ToDoClass getToDoClass(){
			return toDoClass;
		}

		private void setToDoClass(HttpServletRequest request){
			String cookieValue = CookiesWorker.getCookieValue(request, PartNumberServlet.TABLE);

			if(cookieValue!=null)
				toDoClass = ToDoClass.parseToDoClass(cookieValue);
			else
				toDoClass = null;

			logger.trace("\n\ttoDoClass:\t{}", toDoClass);
		}

		@Override
		public String toString() {
			return "RequestParameters [position=" + position
					+ ", topComponentId=" + topComponentId + ", classId="
					+ classId + ", toDoClass=" + toDoClass + "]";
		}
	}
}
