package irt.web;

import irt.data.dao.ErrorDAO;
import irt.data.dao.PurchaseDAO;
import irt.data.partnumber.PartNumber;
import irt.data.purchase.Purchase;
import irt.data.purchase.PurchaseOrder;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;

@SuppressWarnings("serial")
public class PurchaseServlet extends HttpServlet {

	private final String httpAddress = "purchase";

	private RequestDispatcher jsp;

	private PurchaseOrder purchaseOrder;

	private String pathLogo;

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/purchase.jsp");
	        pathLogo = context.getRealPath("/logo_big.png");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(!(userBean.isEditing() || userBean.isStock())){
				response.sendRedirect("/irt");
				return;
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		Purchase purchase = partNumber.getPurchase();
		purchaseOrder = purchase.getPurchaseOrder();

		String poNumber = request.getParameter("po");
		if(poNumber!=null)
			purchase.setPurchaseOrder(purchaseOrder = new PurchaseDAO().getPurchaseOrder(poNumber));

		request.setAttribute("purchase_order", purchaseOrder );
		request.setAttribute("back_page", httpAddress);
		request.setAttribute("error", Purchase.getError());
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {


		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(!(userBean.isEditing() || userBean.isStock())){
				response.sendRedirect("/irt");
				return;
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		if(purchaseOrder.isEdit()){
			purchaseOrder.setGST(request.getParameter("chbxGST")!=null);
			purchaseOrder.setQST(request.getParameter("chbxQST")!=null);
		}
		purchaseOrder.setShippingInfo(request.getParameter("chbxShippingInfo")!=null);
		purchaseOrder.setComments(request.getParameter("comments"));

		purchaseOrder.checkHtmlFields(request);

		String buttonName = Init.getSubmitButton(request);
		PurchaseDAO purchaseDAO = new PurchaseDAO();
		switch(buttonName){
		case "submit-save":
			purchaseDAO.createNewPO(purchaseOrder, PurchaseOrder.OPEN, userBean.getID());
			break;
		case "submit-resave":
			purchaseOrder.resetDate();
			purchaseOrder.setUser(userBean);
			purchaseDAO.resavePO(purchaseOrder, PurchaseOrder.OPEN, userBean.getID());
			break;
		case "submit-update":
			purchaseOrder.setPONumber(purchaseDAO.getUpdatePONumber(purchaseOrder.getPONumber()));
			purchaseDAO.createNewPO(purchaseOrder, PurchaseOrder.OPEN, userBean.getID());
			break;
		case "submit-close":
			purchaseDAO.setStatus(purchaseOrder.getPONumber(), PurchaseOrder.CLOSE);
			break;
		case "submit-invoice":
			response.sendRedirect("add-link?po="+purchaseOrder.getPONumber());
			return;
		case "submit-view":
			String poNumber = request.getParameter("poNumber");
			if(!poNumber.equals("Select")){
				purchaseOrder = purchaseDAO.getPurchaseOrder(poNumber);
				purchaseOrder.setEdit(false);
				PartNumber.getPartNumber(request.getRemoteHost()).getPurchase().setPurchaseOrder(purchaseOrder);
				break;
			}else{
				PartNumber.getPartNumber(request.getRemoteHost()).getPurchase().setPurchaseOrder(null);
				response.sendRedirect("sellers");
				return;
			}
		case "submit-edit":
			purchaseOrder.setEdit(true);
			break;
		case "submit-cancel":
			purchaseOrder.setEdit(false);
			purchaseOrder.cancel();
			break;
		case "submit-pdf":
			if(purchaseOrder.getStatus()==PurchaseOrder.OPEN)
				purchaseDAO.setStatus(purchaseOrder.getPONumber(), PurchaseOrder.ACTIVE);
			try {
				purchaseOrder.upLoadPDF(response, pathLogo);
			} catch (DocumentException e) {
				new ErrorDAO().saveError(e, "PurchaseServlet.doPost");
				throw new RuntimeException(e);
			}
		}
		purchaseOrder.setStatusToShow(Integer.parseInt(request.getParameter("status")));
		purchaseOrder.setVendor(request.getParameter("company_type"));

		request.setAttribute("purchase_order", purchaseOrder );
		request.setAttribute("back_page", httpAddress);
	    jsp.forward(request, response);
	}
}
