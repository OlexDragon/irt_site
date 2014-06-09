package irt.web;

import irt.data.Error;
import irt.data.HTMLWork;
import irt.data.dao.UserBeanDAO;
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

public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private RequestDispatcher jsp;

	private final String httpAddress = "users";
	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
	      ServletContext context = config.getServletContext();
	      jsp = context.getRequestDispatcher("/WEB-INF/jsp/users.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

		} catch (GeneralSecurityException e) { throw new RuntimeException(e); }

		String command = request.getParameter("command");

		if(command==null){

			command = "non";

			if(userBean.getID()==0){
				response.sendRedirect("login?bp="+httpAddress);
				return;
			}else if(!(userBean.isUser() || userBean.isUserEdit() || userBean.isAdmin())){
				response.sendRedirect("/irt");
				return;
			}
		}

		request.setAttribute("command", command);
		request.setAttribute("userToEdit", new UserBean());
		request.setAttribute("back_page", httpAddress);
		request.setAttribute("error", error);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String command = request.getParameter("command");
		if(command==null)
			command = "non";

		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

			if(command.equals("non") && !(userBean.isUser() || userBean.isUserEdit() || userBean.isAdmin())){
				response.sendRedirect("login?bp="+httpAddress);
				return;
			}
		} catch (GeneralSecurityException e) { throw new RuntimeException(e); }

		String contains = "btn_";
		String pressedButton = HTMLWork.getHtmlElement(contains, request);

		UserBean tmpUserBean = null;;
		if(pressedButton!=null){
			pressedButton = pressedButton.substring(contains.length());
			UserBeanDAO userBeanDAO = new UserBeanDAO();
			if(Character.isDigit(pressedButton.charAt(0))){
				//Pressed Edit Button. Get User for edit his settings 
				tmpUserBean = userBeanDAO.getBy(Integer.parseInt(pressedButton));
			}else if(pressedButton.startsWith("update")){
				//Pressed button update. Do update.
				tmpUserBean = new UserBean(Integer.parseInt(pressedButton.substring(7)), HTMLWork.getHtmlFields("u_", request));
				if(!userBeanDAO.update(tmpUserBean, userBean.isAdmin(), userBean.getID())){
					tmpUserBean = userBeanDAO.getBy(tmpUserBean.getID());
					error.setErrorMessage("Did Not Updated <small>(E060)</small>");
				}else{
					tmpUserBean = null;
					command = "non";
				}
			}else if(pressedButton.equals("username") || pressedButton.equals("password")){
				command = pressedButton;
			}else if(pressedButton.equals("submitUsername")){
				if(userBean.getPassword().equals(request.getParameter("u_password"))){
					String newUsername = request.getParameter("u_username");
					if(newUsername!=null){
						if(!userBeanDAO.isResult("SELECT`id`FROM`irt`.`users`WHERE`username`='"+newUsername+"'")){
							userBean.setUsername(newUsername);
							if(userBeanDAO.update(userBean, userBean.isAdmin(), userBean.getID())){
								response.sendRedirect("login?bp="+httpAddress+"&lo=logout");
								return;
							}else{
								error.setErrorMessage("Did not Updated <small>(E061)</small>");
							}
						}else
							error.setErrorMessage("Username '"+newUsername+"' already exists.<small>(E062)</small>");
					}else
						error.setErrorMessage("Type Username <small>(E063)</small>");
				}else
					error.setErrorMessage("Password is not correct<small>(E064)</small>");
			}else if(pressedButton.equals("submitPassword")){
				if(userBean.getPassword().equals(request.getParameter("u_password"))){
					String newPassword = request.getParameter("u_newPasword");
					if(newPassword!=null){
						userBean.setPassword(newPassword);
						if(userBeanDAO.update(userBean, userBean.isAdmin(), userBean.getID())){
							response.sendRedirect("login?bp="+httpAddress+"&l=logout");
							return;
						}else{
							error.setErrorMessage("Did not Updated <small>(E065)</small>");
						}
					}else
						error.setErrorMessage("Type New Password <small>(E066)</small>");
				}else
					error.setErrorMessage("Password is not correct<small>(E067)</small>");
			}else if(pressedButton.equals("newUser")){
				command = "New User";
			}else if(pressedButton.startsWith("add")){
				tmpUserBean = new UserBean(-1, HTMLWork.getHtmlFields("u_", request));
				if(tmpUserBean.getUsername().isEmpty())
					tmpUserBean.setUsername(userBeanDAO.getNewUsername());
				if(tmpUserBean.getPassword().isEmpty())
					tmpUserBean.setPassword(userBean.getUsername());
				if(tmpUserBean.getPermission()==0)
					tmpUserBean.setPermission(UserBean.USER);
				try {
					if(userBeanDAO.insert(userBean.getID(), userBean)==0){
						error.setErrorMessage("Did not Added <small>(E068)</small>");
					}else{
						error.setErrorMessage("User "+userBean.getUsername() +" have been Added to the database. <small>(E0069)</small>");
						tmpUserBean = new UserBean();
						command = "non";
					}
				} catch (GeneralSecurityException e) {
					throw new RuntimeException(e);
				}
			}
		}

		request.setAttribute("command", command);
		request.setAttribute("back_page", httpAddress);
		request.setAttribute("userToEdit", tmpUserBean==null ? new UserBean() : tmpUserBean);
		request.setAttribute("error", error);
		jsp.forward(request, response);
	}
}
