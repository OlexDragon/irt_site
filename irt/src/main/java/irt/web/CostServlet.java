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
import irt.data.purchase.CostCompanyBean;
import irt.data.purchase.CostCompanyBeanWrapper;
import irt.data.purchase.CostCompanyService;
import irt.data.purchase.CostMfrPNBean;
import irt.data.purchase.CostMfrPNService;
import irt.data.purchase.CostService;
import irt.data.purchase.CostUnitBean;
import irt.data.purchase.ForPriceBean;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

@SuppressWarnings("serial")
public class CostServlet extends HttpServlet {

    private static final String TOP_COMP_ID = "top_comp_id";

	private static final String COST_COMPANY_BEAN_WRAPPERS = "costCompanyBeanWrappers";

	protected final Logger logger =LogManager.getLogger();

    private final String httpAddress = "cost";
	private RequestDispatcher jsp;
	private Error error = new Error();

	private RequestParameters parameters = new RequestParameters();

    private static final CostCompanyBeanWrapper BEAN_WRAPPER = new CostCompanyBeanWrapper();

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/cost.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		parameters.getParameters(request, response);

		CostService costService = getCostServic(request, response);

		UserBean userBean = getUserBean(request, response);
		if (userBean.isEditCost()) {
			String cookieValue = CookiesWorker.getCookieValue(request, "isEdit");
			if(cookieValue!=null && cookieValue.equals("true"))
				costService.setEdit(true);
		}

		String position = CookiesWorker.getCookieValue(request, "position");
		if(position == null)
			position = "0";

		logger.trace("\n\t{}", costService);

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("position", position);
		request.setAttribute("cost", costService);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		parameters.getParameters(request, response);;

		CookiesWorker.addCookie(request, response, "position", parameters.getPosition(), 24*60*60);

		CostService costService = getCostServic(request, response);

		String fieldName = request.getParameter("what");
		boolean isStartWithFor = fieldName.startsWith("for");

		int componentId = 0;
		boolean isSetFor = false;
		if (fieldName != null)
			if (isStartWithFor) {
				String[] split = fieldName.split(":");
				componentId = Integer.parseInt(split[0].replaceAll("\\D", ""));
				isSetFor = costService.setForPriceIndex(componentId, Integer.parseInt(split[1]));
			} else if (fieldName.startsWith("vendor"))
				costService.setSelectedCompanyIndex(fieldName);
			else if (fieldName.startsWith("mfrPN"))
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
						setNewPrice(request, response, costService, fieldName, componentId);
				} else 
					setPrices(request, costService);

				buttonAction(request, response, costService, componentId);
			}
		}

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("cost", costService);
		request.setAttribute("position", parameters.getPosition());
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	private void setPrices(HttpServletRequest request, CostService costService) {
		List<String> names = Init.getFieldsNames(request, "price");
		for (String s : names)
			costService.setPrices(	Integer.parseInt(s.replaceAll("\\D", "")), request.getParameter(s));
	}

	private void setNewPrice(HttpServletRequest request,
			HttpServletResponse response, CostService costService,
			String fieldName, int componentId) throws JsonGenerationException,
			JsonMappingException, IOException {
		String addVandor = request.getParameter("companies");
		String addPrice = request.getParameter("price" + componentId);
		String addFor = request.getParameter("addFor");

		logger.debug("\n\t"
				+ "id:\t{}\n\t"
				+ "fieldName:\t{}\n\t"
				+ "addVandor:\t{}\n\t"
				+ "addPrice:\t{}\n\t"
				+ "addFor:\t{}\n\t",
				componentId,
				fieldName,
				addVandor,
				addPrice,
				addFor);

		if (!addFor.isEmpty() && !(addFor = addFor.replaceAll("\\D", "")).isEmpty() && !addPrice.isEmpty()) {
			CostCompanyBean costCompanyBean = costService.getCostCompany(componentId);
			CostMfrPNBean costMfrPN = costService.getCostMfrPN(componentId);
			logger.debug("\n\tid:\t{}\n\t{}\n\t{}", componentId, costMfrPN, costCompanyBean);
			String companyName;
			int companyId;
			if (addVandor.equals("Select")) {
				if(costCompanyBean!=null){
					companyId = costCompanyBean.getId();
					companyName = CostCompanyService.getName(companyId);
				}else{
					companyId = 0;
//TODO					companyName = CostMfrPNService.getMfr()costMfrPN.getMfr();
				}
			}else {
				Company company = new CompanyDAO().getCompany(Integer.parseInt(addVandor));
				companyId = company.getId();
				companyName = company.getCompanyName();
			}

//TODO			error.setErrorMessage("CompanyName: "+companyName);
//			if(companyName!=null) {
//				costCompanyBean = new CostCompanyBean()
//								.setId(companyId);
//				CostCompanyService.addForPriceBean(costCompanyBean, new ForPriceBean()
//															.setNewPrice(new BigDecimal(addPrice))
//															.setForUnits(Integer.parseInt(addFor)));
//				costService.add(componentId, costCompanyBean);
//				CookiesWorker.addCookie(request, response, COST_COMPANY_BEAN_WRAPPERS, Jackson.objectToJsonString(BEAN_WRAPPER.set(componentId, costService.getCostCompany(companyId))), 24*60*60);
//			}
		} else
			error.setErrorMessage("Company, Price and For fields should be filled");
	}

	private void buttonAction(HttpServletRequest request,
			HttpServletResponse response, CostService costService,
			int componentId) {
		String pressedButton = HTMLWork.getSubmitButton(request);
		List<CostUnitBean> costUnitBeans = costService.getCostUnitBeans();

		logger.debug("\n\t{}", costUnitBeans);

		Map<Integer, CostCompanyBean> costCompanyBeans = BEAN_WRAPPER.getCostCompanyBeans();
		switch(pressedButton){
		case "submit_save_set":
			new CostDAO().saveSet(costService.getSetIndex(), costService.getId(), costUnitBeans);
		case "submit_save":
			if(new CostDAO().save(costCompanyBeans)!=0){
				CookiesWorker.removeCookiesStartWith(request, response, COST_COMPANY_BEAN_WRAPPERS);
				costCompanyBeans.clear();
			}else
				error.setErrorMessage("Can not be saved.");
			break;
		case "submit_cansel":
			if(!costCompanyBeans.isEmpty()) {
				logger.debug("\n\tcomponentId:\t{}\n\t{}", componentId, BEAN_WRAPPER);
				for(Integer compId:costCompanyBeans.keySet())
					costService.remove(compId, costCompanyBeans.get(compId));
				CookiesWorker.removeCookiesStartWith(request, response, COST_COMPANY_BEAN_WRAPPERS);
				costCompanyBeans.clear();
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

	private CostService getCostServic(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, JsonParseException, JsonMappingException, IOException {

		String 		componentId = parameters.getTopComponentId();
		ToDoClass 	toDoClass 	= parameters.getToDoClass();

		logger.trace("\n\tcomponentId:\t{}\n\ttoDoClass:\t{}", componentId, toDoClass);

		CostService costService;
		if(componentId == null){
			if(toDoClass == null || toDoClass.getCommand()!=ToDo.PRICE)
				costService = new CostService(null);
			else {
				componentId = toDoClass.getValue();
				costService = getComponentCostService(componentId);
			}
		}else
			costService = getBOMCostService(componentId);

		costService.setClassId(parameters.getClassId());
		setCostServicValues(costService, request);

		return logger.exit(costService);
	}

	private void setCostServicValues(CostService costService, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		String cookieValue = CookiesWorker.getCookieValue(request, COST_COMPANY_BEAN_WRAPPERS);
		if(cookieValue!=null) {
			logger.debug("\n\t{}", cookieValue);
			CostCompanyBeanWrapper ccbw = Jackson.jsonStringToObject(CostCompanyBeanWrapper.class, cookieValue);
			if(ccbw!=null){
				Map<Integer, CostCompanyBean> costCompanyBeans = ccbw.getCostCompanyBeans();
				BEAN_WRAPPER.setCostCompanyBeans(costCompanyBeans);
				for(Integer compId:costCompanyBeans.keySet())
					costService.add(compId, costCompanyBeans.get(compId));
			}
		}
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
		logger.entry(componentId);
		CostService costService;

		if(componentId==null || componentId.isEmpty()){
			costService = new CostService(null);
		}else
			costService=new CostDAO().getCost(Integer.parseInt(componentId));

		return logger.exit(costService);
	}

	private class RequestParameters{

		private String position;
		private String topComponentId;
		private String classId;
		private ToDoClass toDoClass;

		public void getParameters(HttpServletRequest request, HttpServletResponse response) {
			position = request.getParameter("position");
			setClassId(request);
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
			topComponentId = request.getParameter(TOP_COMP_ID);
			if(classId==null || classId.equals("Select")){
				topComponentId = null;
			}else{
				CookiesWorker.addCookie(request, response, "class_id", classId, 24*60*60);
				if(topComponentId==null)
					topComponentId = CookiesWorker.getCookieValue(request, TOP_COMP_ID);
				else
					CookiesWorker.addCookie(request, response, TOP_COMP_ID, topComponentId, 24*60*60);
			}
		}

		public String getClassId() {
			return classId;
		}

		private void setClassId(HttpServletRequest request) {
			classId = request.getParameter("class_id");
			if(classId==null)
				classId = CookiesWorker.getCookieValue(request, "class_id");
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

		}
	}
}
