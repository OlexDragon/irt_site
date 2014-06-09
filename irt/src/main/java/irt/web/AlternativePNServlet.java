package irt.web;

import irt.data.Error;
import irt.data.components.alternative.AlternativeMfrPN;
import irt.data.dao.ComponentDAO;
import irt.data.partnumber.PartNumber;
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

@SuppressWarnings("serial")
public class AlternativePNServlet extends HttpServlet {

	public static final String HTTP_ADDRESS = "alt-mfr-pns";
	private RequestDispatcher jsp;
	private PartNumber partNumber;
	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/alt-mfr-pns.jsp");

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		if(partNumber.getComponent()==null || partNumber.getComponent().getId()<0){
			response.sendRedirect("part-numbers");
			return;
		}

		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("partNumber", partNumber);
		jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);


			if(!userBean.isEditing() || partNumber==null || partNumber.getComponent()==null || partNumber.getComponent().getId()<0){
				error.setErrorMessage("You do not have permission for this action");
				response.sendRedirect("part-numbers");
				return;
			}
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		AlternativeMfrPN altMfrPN = null;
		if(request.getParameter("submit-add")!=null){
			altMfrPN = new AlternativeMfrPN(-1, partNumber.getComponent().getId(), request.getParameter("mfr-pn").trim().toUpperCase(), request.getParameter("mfr"));
			if(altMfrPN.isPrepared()){
				if(new ComponentDAO().insertAlternativeMfrPN(userBean.getID(), altMfrPN)){
					response.sendRedirect("part-numbers");
					return;
				}else
					error.setErrorMessage("Alternative Mfr PN <span class=\"cBlue\">"+altMfrPN.getMfrPN()+"</span> already exist.(E037)","red");
			}else
				error.setErrorMessage("All fields must be filled.(E036)","red");
		}else
			altMfrPN = new AlternativeMfrPN(-1, -1, null, null);

		request.setAttribute("back_page", HTTP_ADDRESS );
		request.setAttribute("alt_pn", altMfrPN );
		request.setAttribute("partNumber", partNumber);
		jsp.forward(request, response);
	}
}
