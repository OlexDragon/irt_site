package irt.web;

import irt.data.HTMLWork;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsMovement;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.dao.ComponentDAO;
import irt.data.dao.PurchaseDAO;
import irt.data.partnumber.PartNumber;
import irt.data.purchase.Purchase;
import irt.data.user.UserBean;
import irt.data.Error;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompaniesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String HTTP_ADDRESS = "sellers";

	private RequestDispatcher jsp;

	private PartNumber partNumber;
	private Error error;

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/sellers.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserBean ub = Init.getUserBean(request);
		if(ub==null || !ub.isSellers()){
			response.sendRedirect("logging?bp="+HTTP_ADDRESS);
			return;
		}

		partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		Purchase purchase = partNumber.getPurchase();
		purchase.setEditCompany(ub.isEditCompanies());
		purchase.setShowAll(false);
		purchase.cancelEdit();
		purchase.setTopComponents();

		String orderBy = request.getParameter("ob");
		if(orderBy!=null)
			purchase.setOrderBy(orderBy);

		request.setAttribute("purchase", purchase );
		request.setAttribute("back_page", HTTP_ADDRESS);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserBean ub = Init.getUserBean(request);
		if(ub==null || !ub.isSellers()){
			response.sendRedirect("logging?bp="+HTTP_ADDRESS);
			return;
		}

		partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		Purchase purchase = partNumber.getPurchase();
		purchase.setEditCompany(ub.isEditCompanies());
		if(purchase.getTable()!=null)
			purchase.setOrderByReference(request.getParameter("check_order")!=null);

		String status = request.getParameter("status");
		if(status!=null)
			purchase.setStatusToShow(Integer.parseInt(status));

		String buttonName = Init.getSubmitButton(request);

//		Purchase.setErrorMessage(buttonName);
		switch(buttonName){
		case "submit_purchase"://"Prepare Order" button
			purchase.preparePurchaseOrder(HTMLWork.getHtmlFields(request, "qty"));
		case "submit_purchase_edit"://"Prepare Order" button
			purchase.getPurchaseOrder().setEdit(true);
			response.sendRedirect("purchase");
			return;
		case "submit_view"://"Prepare Order" button
			String poNumber = request.getParameter("poNumber");
			if(!poNumber.equals("Select")){
				purchase.setPurchaseOrder(new PurchaseDAO().getPurchaseOrder(poNumber));
				purchase.getPurchaseOrder().setEdit(false);
				purchase.getPurchaseOrder().setStatusToShow(purchase.getStatusToShow());
				response.sendRedirect("purchase");
				return;
			}else{
				purchase.setPurchaseOrder(null);
			}
			break;
		case "submit_add":
			if(!purchase.isAddCompany() && purchase.getCompany()==null)
				purchase.setAddCompany(true);
			else
				purchase.save(request, ub);
			break;
		case "submit_cancel":
			purchase.cancelEdit();
			break;
		case "submit_add_qty":
			purchase.addOrderQuantity(request);
			break;
		case "submit_clean":
			purchase.setQuantityToAdd(0);
			break;
		case "submit_move":
				if(moveComponents(request)){
				response.sendRedirect("components_movement");
				return;
			}
			break;
		default:
			if(buttonName.contains("edit"))
				purchase.setCompany(buttonName);
			else
				purchase.setActivePage(request);
		}

		request.setAttribute("purchase", purchase );
		request.setAttribute("back_page", HTTP_ADDRESS);
	    jsp.forward(request, response);
	}

	private boolean moveComponents(HttpServletRequest request) {
		boolean isMoved = false;
		ComponentsMovement cm = getComponentMovement();
		ComponentsQuantity idAndValue = HTMLWork.getHtmlFields(request, "qty");
		
		if (cm.getFrom().equals(ComponentsMovement.getPlace(cm, HTTP_ADDRESS))) {
			ComponentToMove c = null;
			if (idAndValue!=null && idAndValue.size()!=0){
				for(ComponentQuantity p:idAndValue.getComponentsQuantity())
					if(p.getStockQuantity()>0 && (c = new ComponentDAO().getComponentToMove(p.getId())) != null){
						c.setQuantityToMove(p.getQuantityToMove(), false);
						cm.add(c);
					}

				isMoved = true;
			}
		}else
			error.setErrorMessage("You did not finish move from "+cm.getFrom().getName()+".(E029)");

		return isMoved;
	}

	private ComponentsMovement getComponentMovement() {
		ComponentsMovement cm = partNumber.getComponentsMovement();
		if(cm==null)
			partNumber.setComponentsMovement(cm = new ComponentsMovement());
		if(cm.getFrom()==null)
			cm.setFrom(ComponentsMovement.getPlace(cm, HTTP_ADDRESS));
		return cm;
	}
}