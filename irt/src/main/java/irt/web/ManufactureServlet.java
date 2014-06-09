package irt.web;

import irt.data.Error;
import irt.data.dao.ErrorDAO;
import irt.data.dao.ManufactureDAO;
import irt.data.manufacture.Manufacture;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.files.Pdf;
import irt.table.OrderBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;

public class ManufactureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//	private Logger logger = Logger.getLogger(this.getClass());
	private RequestDispatcher jsp;
	private String httpAddress = "manufacture-links";
	private OrderBy orderBy;

	private String pathLogo;
	private Error error;

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/ManufactureLinks.jsp");
        pathLogo = context.getRealPath("images/logo.png");
//		logger.debug("init(...); m m m m m m m m m m m m m");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		logger.debug("doGet(...)");


		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);
		} catch (GeneralSecurityException e) { throw new RuntimeException(e); }

		Manufacture manufacture = new Manufacture();

		String orderBy	= request.getParameter("order_by");
		if(orderBy!=null && !orderBy.isEmpty()){
			if(this.orderBy==null)
				this.orderBy = new OrderBy(orderBy);
			else
				this.orderBy.setOrderBy(orderBy);
			manufacture.setOrderBy(this.orderBy);
		}

		manufacture.setUserBean(userBean);

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("manuf", manufacture);
		jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		UserBean userBean = null;
		try {
			userBean = UsersLogsIn.getUserBean(request, response);

		} catch (GeneralSecurityException e) { throw new RuntimeException(e); }

		Manufacture manufacture = null;
		if (request.getParameter("submit") != null) {

			manufacture = new Manufacture(request.getParameter("manuf_id"),
													request.getParameter("manuf_name"),
													request.getParameter("manuf_link"));
			ManufactureDAO manufactureDAO = new ManufactureDAO();

			if (userBean!=null && manufacture.isSet())
				if (!manufactureDAO.isExist(manufacture))
					manufactureDAO.insert(userBean.getID(), manufacture);
				else
					error.setErrorMessage("Manufacture ID or Name already exist.");
		}else{
			try {
				getPdf(response);
				return;
			} catch (DocumentException e) {
				new ErrorDAO().saveError(e, "ManufactureServlet.doPost");
				throw new RuntimeException(e);
			}
		}

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("manuf", manufacture);
		jsp.forward(request, response);
	}

	private void getPdf(HttpServletResponse response) throws DocumentException, MalformedURLException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = Pdf.openDocument( baos, PageSize.A4.rotate());

		Image logo = Image.getInstance(pathLogo);

		document.add(new ManufactureDAO().getPdfTable(logo));
		document.close();

		// setting the content type
		response.setContentType("application/pdf");
		// the content length
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}
}
