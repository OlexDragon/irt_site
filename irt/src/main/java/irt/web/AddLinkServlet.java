package irt.web;

import irt.data.Error;
import irt.data.components.Component;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;
import irt.data.dao.LinkDAO;
import irt.data.dao.PurchaseDAO;
import irt.data.partnumber.PartNumber;
import irt.data.purchase.PurchaseOrder;
import irt.data.user.UserBean;
import irt.data.user.UsersLogsIn;
import irt.work.Buffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class AddLinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final String httpAddress = "add-link";

	private RequestDispatcher jsp;
	private String message;
	private String partNumberStr;
	private String realPath;

	private String purchaseOrderNumber;
	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		realPath = context.getRealPath("/lib");
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/add-link.jsp");
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
		Component component = partNumber.getComponent();
		String manufPN;
		if(component!=null){
			partNumberStr = component.getPartNumberF();
			manufPN = " : " + component.getManufPartNumber();
		}else
			manufPN = "";

		message = "<h4 style=\"text-align:center;color:blue\">" +
				(partNumberStr !=null ? partNumberStr : (purchaseOrderNumber = request.getParameter("po"))) + manufPN
				+ "</h4>\n";

		message += "<form id=\"linkForm\" method=\"post\" action=\"add-link\" enctype=\"multipart/form-data\">\n";

		message += "<p><label for=\"file\"> Link: </label> <input type=\"file\" id=\"file\" name=\"file\" size=\"70\" accept=\"txt\\jpg\\pdf\\zip\" />\n";

		message += "<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Submit\" /></p>";

		message += "</form>";
		request.setAttribute("back_page", httpAddress);
		request.setAttribute("message", message);
		request.setAttribute("error", error);
		jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


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

		int linkId = -1;

		if (ServletFileUpload.isMultipartContent(request)) {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			Buffer buffer = null;
			LinkDAO linkDAO = new LinkDAO();
			try {
				// Create a new file upload handler
				ServletFileUpload servletFileUpload = new ServletFileUpload();

				// Parse the request
				FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
				while (fileItemIterator.hasNext()) {
					FileItemStream fileItemStream = fileItemIterator.next();
					inputStream = fileItemStream.openStream();
					if (!fileItemStream.isFormField()){
						String fileName = fileItemStream.getName();
						if (fileName != null && !fileName.isEmpty()) {
							fileName = FilenameUtils.getName(fileName);
							if (!linkDAO.isExists(fileName)) {
								String folderName = partNumberStr!=null ? partNumberStr.substring(0, 3) : "Invoices";
								String path = FilenameUtils.concat(realPath, folderName);
								File folder = new File(path);
								if (!folder.exists())
									folder.mkdirs();
								outputStream = new FileOutputStream(FilenameUtils.concat(path, fileName));
								buffer = new Buffer(4096);
								while (true) {
									synchronized (Buffer.getBuffer()) {
										int amountRead = inputStream.read(Buffer.getBuffer());
										if (amountRead == -1) {
											break;
										}
										outputStream.write(Buffer.getBuffer(), 0, amountRead);
									}
								}
								linkId = linkDAO.add("/irt/lib/"+folderName+"/"+fileName);
							}else
								linkId = linkDAO.getId(fileName);
								
							if (linkId > 0){
								if(partNumberStr!=null && new ComponentDAO().addLink(userBean.getID(), partNumberStr, linkId)) {
									response.sendRedirect("part-numbers?pn="+ partNumberStr);
									return;
								}else if(purchaseOrderNumber!=null && new PurchaseDAO().addLink(userBean.getID(), purchaseOrderNumber, linkId)){
									new PurchaseDAO().setStatus(purchaseOrderNumber, PurchaseOrder.ACTIVE);
									response.sendRedirect("purchase?po="+ purchaseOrderNumber);
									return;
								}
								error.setErrorMessage("<p style=\"{color:red}\">Error.(E069)</p>");
							}else
								error.setErrorMessage("<p style=\"{color:red}\">Error.(E040)</p>");
					}else
							error.setErrorMessage("<p style=\"{color:red}\">Select a file.</p>");
						break;
					}
				}
			} catch (Exception e) {
				new ErrorDAO().saveError(e, "AddLinkServlet.doPost");
				throw new RuntimeException(e);
			} finally {
				if (buffer != null) {
					buffer.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}

		request.setAttribute("back_page", httpAddress);
		request.setAttribute("message", message);
		request.setAttribute("error", error);
		jsp.forward(request, response);
	}
}
