package irt.web;

import irt.data.CookiesWorker;
import irt.data.Error;
import irt.data.dao.UserBeanDAO;
import irt.data.partnumber.PartNumber;
import irt.data.user.LogIn;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

@WebServlet("/LoginServle")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private RequestDispatcher jsp;
	private String httpAddress = "";
	private Error error = new Error();


	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String backPage = request.getParameter("bp");
		if(backPage!=null){
			CookiesWorker.addCookie(request, response, "bp", backPage, 24 * 60 * 60);
		}else{
			backPage = CookiesWorker.getCookieValue(request, "bp");
			if(backPage == null)
				backPage = "";
		}

		LogIn logIn;
		try { logIn = UsersLogsIn.getLogIn(request); } catch (GeneralSecurityException e1) { throw new RuntimeException(e1); }

		if(logIn!=null && logIn.isValid()){
			UsersLogsIn.logOff(request, response, logIn);
			response.sendRedirect(backPage);
			return;
		}

		String rememberStr = CookiesWorker.getCookieValue(request, "remember");

		request.setAttribute("back_page", backPage);
		request.setAttribute("error", error);
		request.setAttribute("checked", rememberStr!=null && rememberStr.equalsIgnoreCase("true"));
		jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserBean userBean = new UserBean();

		String userName = request.getParameter("un");
		String password = request.getParameter("pw");

		if (userName!=null && !userName.isEmpty()) {
			userBean.setUserName(userName);
			if (password!=null && !password.isEmpty()) {
				userBean.setPassword(password);
				new UserBeanDAO().login(userBean);
			} else
				error.setErrorMessage("Type your Password.");
		} else
			error.setErrorMessage("Type your UserName.");

		boolean remember;
		if(userBean.isValid()){

			remember = request.getParameter("remember")!=null;

			LogIn logIn = UsersLogsIn.addUser(request, response, userBean);

			PartNumber.getPartNumber(request.getRemoteHost()).getUser().setFullName(userBean.getFullName());

			String bp = CookiesWorker.getCookieValue(request, "bp")+"?li="+Base64.encodeBase64URLSafeString(logIn.toString().getBytes());
			response.sendRedirect(bp!=null ? bp : "/irt");
			return;
		}else{
			remember = false;
			CookiesWorker.removeCookiesStartWith(request, response, "remember");
			error.setErrorMessage("You have entered invalid login information. Please try again.");
		}
		
		request.setAttribute("back_page", httpAddress );
		request.setAttribute("error", error);
		request.setAttribute("checked", remember);
		jsp.forward(request, response);
	}
}
