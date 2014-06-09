package irt.web;

import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.companies.Company;
import irt.data.dao.CompanyDAO;
import irt.data.dao.CostDAO;
import irt.data.partnumber.PartNumber;
import irt.data.purchase.Cost;
import irt.data.purchase.CostCompany;
import irt.data.purchase.ForPrice;
import irt.data.purchase.Price;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;

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

@SuppressWarnings("serial")
public class CostServlet extends HttpServlet {

	private final String httpAddress = "cost";
	private RequestDispatcher jsp;
	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/cost.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		Cost cost = partNumber.getCost();
		if(cost==null)
			cost=new Cost(0, null, null);
		

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(!userBean.isEditCost()){
				cost.setEdit(false);
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("classId", cost.getClassId());
		request.setAttribute("componentId", cost.getComponentId());
		request.setAttribute("cost", cost);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		Cost cost = partNumber.getCost();

		String classId = request.getParameter("class_id");
		String componentId = request.getParameter("component_id");
		cost.setPosition(request.getParameter("position"));
		cost.setSetIndex(Integer.parseInt(request.getParameter("set")));
		cost.setSet(request.getParameter("submit_set")!=null);

		if(componentId!=null && componentId.equals("Select"))
			partNumber.setCost(cost=new Cost(0, null, null));
		else
			if(componentId!=null && !componentId.equals(cost.getComponentId()))
				partNumber.setCost(cost=new CostDAO().getCost(componentId));
		if(cost==null)
			cost = new Cost(0, null, null);

		cost.setClassId(classId);
		cost.setComponentId(componentId);
		String fieldName = request.getParameter("what");
		boolean isSetFor = false;
		boolean isStartWithFor = false;
		int id = 0;
		if (fieldName != null)
			if (isStartWithFor = fieldName.startsWith("for")) {
				String[] split = fieldName.split(":");
				id = Integer.parseInt(split[0].replaceAll("\\D", ""));
				isSetFor = cost.setForPriceIndex(id, Integer.parseInt(split[1]));
			} else if (fieldName.startsWith("vendor"))
				cost.setSelectedCompanyIndex(fieldName);
			else if (fieldName.startsWith("mfrPN"))
				cost.setSelectedMfrPNIndex(fieldName);


		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		if (userBean.isEditCost()) {
			cost.setEdit(request.getParameter("is_edit") != null);
			if (cost.isEdit()) {
				if (!fieldName.isEmpty()) {
					if (isStartWithFor && !isSetFor){
						String addVandor = request.getParameter("companies");
						String addPrice = request.getParameter("price" + id);
						String addFor = request.getParameter("addFor");
						if (!addFor.isEmpty() && !(addFor = addFor.replaceAll("\\D", "")).isEmpty() && !addPrice.isEmpty()) {
							CostCompany costCompany = cost.getCostCompany(id);
							String companyName;
							int companyId;
							if (addVandor.equals("Select")) {
								if(costCompany!=null){
									companyId = costCompany.getId();
									companyName = costCompany.getName();
								}else{
									companyId = 0;
									companyName = cost.getCostMfrPN(id).getMfr();
								}
							}else {
								Company company = new CompanyDAO().getCompany(Integer.parseInt(addVandor));
								companyId = company.getId();
								companyName = company.getCompanyName();
							}

							error.setErrorMessage("CompanyName: "+companyName);
							if(companyName!=null)
								cost.add(id, new CostCompany(companyId,companyName,new ForPrice(new Price(addPrice),Integer.parseInt(addFor),ForPrice.INSERT)));
						} else
							error.setErrorMessage("Company, Price and For fields should be filled");
					}
				} else {
					List<String> names = Init.getFieldsNames(request, "price");
					for (String s : names)
						cost.setPrices(	Integer.parseInt(s.replaceAll("\\D", "")),request.getParameter(s));
				}
				
				String pressedButton = HTMLWork.getSubmitButton(request);
				switch(pressedButton){
				case "submit_save_set":
					new CostDAO().saveSet(cost.getSetIndex(), cost.getId(), cost.getCostUnits());
				case "submit_save":
					if(cost.isChanged())
						new CostDAO().save(cost.getCostUnits());
				}
			}
		}

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("classId", cost.getClassId());
		request.setAttribute("componentId", cost.getComponentId());
		request.setAttribute("cost", cost);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}
}
