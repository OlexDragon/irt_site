package irt.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	   private RequestDispatcher jsp;

	   private final String httpAddress = "index.html";


		@Override
		public void init(ServletConfig config) throws ServletException {
		      ServletContext context = config.getServletContext();
		      jsp = context.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
		}

		@Override
		protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse resp)
				throws ServletException, IOException {

			httpServletRequest.setAttribute("back_page", httpAddress);
		    jsp.forward(httpServletRequest, resp);
		}
}
