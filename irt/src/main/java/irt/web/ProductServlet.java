package irt.web;

import irt.data.CookiesWorker;
import irt.data.Error;
import irt.data.Jackson;
import irt.data.dao.BomDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;
import irt.files.Excel;
import irt.files.Pdf;
import irt.product.BillOfMaterials;
import irt.product.ProductStructure;
import irt.table.OrderByService;
import irt.table.OrderByService.OrderByBean;
import irt.work.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;

public class ProductServlet extends HttpServlet {
	private static final BomDAO BOM_DAO = new BomDAO();

	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger();
//	private static final int ERROR = -1;
//	private static final int INSERT = 0;
//	private static final int SEARCH = 1;
//	private static final int PDF 	= 2;
//	private static final int EXCEL 	= 3;
//	private static final int SYMBOL = 4;

	private final String httpAddress = "product_structure";
	
	private String realPath;
	private String realPathSymbol;

	private RequestDispatcher jsp;
	private String pathLogo;

	private Error error = new Error();

	@Override
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/product.jsp");
		realPath = context.getRealPath("/lib/bom");
		realPathSymbol = context.getRealPath("/lib/symbols");
        pathLogo = context.getRealPath("images/logo.png");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.entry();

		ProductStructureFields productStructureFields = new ProductStructureFields(request, response);
		productStructureFields.setFields();
		productStructureFields.setOrderByReference(null);

		if (request.getParameter("pdf") != null)
			try {
				getPdf(response, productStructureFields.getPartNumber(), productStructureFields.getOrderBy());
				return;
			} catch (Exception e) {
				new ErrorDAO().saveError(e, "ProductServlet.doGet");
				throw new RuntimeException(e);
			}
		else if (request.getParameter("excel") != null) {
			try {
				Excel.uploadExcel(response, productStructureFields.getPartNumber(), pathLogo, false, productStructureFields.getOrderBy());
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "ProductServlet.doGet 2");
				throw new RuntimeException(e);
			}
		}

		logger.trace("\n\t{}\n\t{}", productStructureFields, error);

		request.setAttribute("psf", productStructureFields);
		request.setAttribute("error", error);
		request.setAttribute("back_page", httpAddress);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		ProductStructureFields productStructureFields;
		try {
			productStructureFields = setProductStructureFields(request, response);

//		switch(takeDecision(request, productStructureFields)){
//		case INSERT:
//			productStructure.toDatabase();
//			break;
//		case PDF:
//	        try {
//				if(productStructure.getOrderBy()!=null && productStructure.getOrderBy().equals("Description"))
//					productStructure.setOrderBy(null);
//				getPdf(response, pn, getOrderBy(ob));
//				return;
//			}catch(Exception e) {
//				new ErrorDAO().saveError(e, "ProductServlet.doGet");
//				throw new RuntimeException(e);
//	        }
//		case EXCEL:
//			try {
//				String partNumberStr = productStructure.getPartNumber();
//				if(partNumberStr!=null) {
//					OrderBy orderBy = productStructure.getOrderBy();
//					if(orderBy!=null && orderBy.equals("Description"))
//						orderBy = null;
//					Excel.uploadExcel(response, partNumberStr, pathLogo, productStructure.isQty, orderBy);
//					return;
//				} else
//					error.setErrorMessage("Type the PartNumber.","red");
//			} catch (SQLException e) {
//				new ErrorDAO().saveError(e, "ProductServlet.doPost 2");
//				throw new RuntimeException(e);
//			}
//			break;
//		case SYMBOL:
//		}
		if(productStructureFields!=null && productStructureFields.getCommand()!=null){
			ProductStructureFields.Command command = productStructureFields.getCommand();
			logger.trace("\n\tcommand:\t{}", command);
			switch (command) {
			case EXSEL:
				Excel.uploadExcel(response,
							productStructureFields.getPartNumber(), pathLogo,
							productStructureFields.isWithQty,
							productStructureFields.getOrderBy());
				return;
			case PDF:
				getPdf(	response,
							productStructureFields.getPartNumber(),
							productStructureFields.getOrderBy());
				return;
			case GET_BOM:
				productStructureFields.setBOM("true");
				break;
			case ADD_BOM:
				addBOM(productStructureFields);
			}
		}
		} catch (Exception e) {
			throw new ServletException(e);
		}

		request.setAttribute("psf", productStructureFields);
		request.setAttribute("error", error);
		request.setAttribute("back_page", httpAddress);
	    jsp.forward(request, response);
	}

	private ProductStructureFields setProductStructureFields(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, IOException {

		ProductStructureFields productStructureFields = new ProductStructureFields(request, response);
		boolean bomIsSet = false;

		if (ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload servletFileUpload = new ServletFileUpload();
			// Parse the request
			FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);
			InputStream inputStream = null;
			while (fileItemIterator.hasNext()) {
				FileItemStream fileItemStream = fileItemIterator.next();
				inputStream = fileItemStream.openStream();
				String name = fileItemStream.getFieldName();
				if (fileItemStream.isFormField()) {
					String inputStr = Streams.asString(inputStream);
					switch(name){
					case "pn":
						productStructureFields.setPartNumber(inputStr);
						break;
					case "check_footprint":
						productStructureFields.setIgnoreFootprint("true");
						break;
					case "check_symbol":
						productStructureFields.setSymbol("true");
						break;
					case "check_qty":
						productStructureFields.setWithQty("true");
						break;
					case "check_order":
						productStructureFields.setOrderByReference("true");
						break;
					case "submit-add-bom":
						productStructureFields.setCommand(inputStr.equals("get BOM") ? ProductStructureFields.Command.GET_BOM : ProductStructureFields.Command.ADD_BOM);
						break;
					case "submit-where":
						productStructureFields.setBOM("false");
						break;
					case "submit-pdf":
						productStructureFields.setCommand(ProductStructureFields.Command.PDF);
						break;
					case "submit-excel":
						productStructureFields.setCommand(ProductStructureFields.Command.EXSEL);
					default:
						logger.warn("\n\t"
								+ "name=\t{}\n\t"
								+ "fname=\t{}",
								name,
								inputStr);
					}
				}else{
					String fileName = fileItemStream.getName();
					if (fileName != null && !fileName.isEmpty()) {
						fileName = FilenameUtils.getName(fileName);

						String extension = FilenameUtils.getExtension(fileName);
						if(!(extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls"))){
							error.setErrorMessage("Incorrect file format");
							break;
						}

						String path;
						boolean isSymbol = productStructureFields.isSymbol();
						path = isSymbol ? realPathSymbol : FilenameUtils.concat(realPath, productStructureFields.getPartNumber());
						mkDirs(path);

						String sourceFile = getFileName(path, fileName);
						if(uploadFile(inputStream, sourceFile))
							productStructureFields.setSourceFile(sourceFile);

					}else{
						error.setErrorMessage("Select the file.");
						productStructureFields.setSymbol("false");
					}
				}
			}
		}

		if(!bomIsSet)
			productStructureFields.setBOM(null);
		productStructureFields.setOrderBy(null);

		return productStructureFields;
	}

	private void addBOM(ProductStructureFields productStructureFields) {
		String sourceFile = productStructureFields.getSourceFile();
		if (sourceFile != null && !sourceFile.isEmpty()){		
//		if(isSymbol)
//			updateSymbols(sourceFile);
//		else{

			String partNumber = productStructureFields.getPartNumber();

			// work with uploaded file
			BillOfMaterials billOfMaterials = new BillOfMaterials(partNumber, sourceFile, productStructureFields.isIgnoreFootprint());
			ProductStructure productStructure = new ProductStructure();
			productStructure.set(billOfMaterials);
			productStructure.setPartNumber(partNumber, productStructureFields.isBOM());

			if(billOfMaterials.isError())
				error.setErrorMessage(billOfMaterials.getErrorMessage());
			if (productStructure.isErrorMessage())
				error.setErrorMessage(productStructure.getErrorMessage());

			if (!(error.isError() || productStructure.hasBom()))
				BOM_DAO.insert(billOfMaterials);

			if(BOM_DAO.getError().isError())
				error.setErrorMessage(BOM_DAO.getError().getErrorMessage());
		}
	}

//	private int takeDecision(HttpServletRequest httpServletRequest, ProductStructureFields productStructureFields) throws IOException {
//		String sourceFile = null;
//		int decision = ERROR;

		//upload sours file
//		if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
//			InputStream inputStream = null;
//			try {
//				// Create a new file upload handler
//				ServletFileUpload servletFileUpload = new ServletFileUpload();
//
//				// Parse the request
//				FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpServletRequest);
//				while (fileItemIterator.hasNext()) {
//					FileItemStream fileItemStream = fileItemIterator.next();
//					inputStream = fileItemStream.openStream();
//					String name = fileItemStream.getFieldName();
//
//					if (!fileItemStream.isFormField()) {
//						String fileName = fileItemStream.getName();
//						if (fileName != null && !fileName.isEmpty()) {
//							fileName = FilenameUtils.getName(fileName);
//
//							String extension = FilenameUtils.getExtension(fileName);
//							if(!(extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls"))){
//								error.setErrorMessage("Incorrect file format");
//								break;
//							}
//
//							String path;
//							path = productStructureFields.isSymbol() ? realPathSymbol : FilenameUtils.concat(realPath, productStructureFields.getPartNumber());
//							mkDirs(path);
//
//							//get file name
//							sourceFile = getFileName(path, fileName);
//							
//							uploadFile(inputStream, sourceFile);
//
//						}else{
//							ProductStructure.setErrorMessage("Select the file.");
//							productStructureFields.setSymbol("false");
//						}
//					}
//				}
//			} catch (Exception e) {
//				new ErrorDAO().saveError(e, "ProductServlet.takeDecision");
//				throw new RuntimeException(e);
//			} finally { if (inputStream != null)  inputStream.close(); }
//		}
//
//		if (sourceFile != null && !sourceFile.isEmpty()) {
//			if (decision == SYMBOL) {
//				updateSymbols(sourceFile);
//			} else {
//				if (!productStructureFields.isSymbol()) {
//
//			// work with uploaded file
//					BillOfMaterials billOfMaterials;
//					if(productStructureFields.isTop())
//						billOfMaterials = new BillOfMaterialsTop(sourceFile);
//					else
//						billOfMaterials = new BillOfMaterials(sourceFile, productStructureFields.isFootprint);
//					productStructureFields.set(billOfMaterials);
//					if (productStructureFields.isErrorMessage())
//						decision = ERROR;
//					else if (!productStructureFields.hasBom())
//						decision = INSERT;
//					else
//						decision = SEARCH;
//				}
//			}
//		}
//		productStructureFields.setOrderByReference(isOrder);
//
//		if (partNumberStr != null
//				&& !(partNumberStr = PartNumber.validation(partNumberStr)).isEmpty()) {
//			productStructureFields.setPartNumber(partNumberStr,isBom);
//		}
//
//		return decision;
//	}

	private void updateSymbols(String sourceFile) {
		Workbook workbook = null;

		try {

			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			workbook = WorkbookFactory.create(fileInputStream);

		} catch (InvalidFormatException | IOException e) {
			new ErrorDAO().saveError(e, "ProductServlet.updateSymbols");
			throw new RuntimeException(e);
		}

		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(0);
			int rowNumber = sheet.getLastRowNum();
			//check titles
			Row row = sheet.getRow(0);
			int columnNumber = row.getLastCellNum();
			int pnIndex = -1;
			int symbolIndex = -1;
			for(int i=0; i<columnNumber; i++){
				if(row.getCell(i).getRichStringCellValue().toString().equalsIgnoreCase("part number"))
					pnIndex = i;
				if(row.getCell(i).getRichStringCellValue().toString().equalsIgnoreCase("source package"))
					symbolIndex = i;
			}

			if (pnIndex!=-1 && symbolIndex!=-1)
				new ComponentDAO().updateSymbols(sheet, rowNumber, pnIndex, symbolIndex);
			else
				error.setErrorMessage("File structure is not correct.");
		}
	}

	private boolean uploadFile(InputStream inputStream, String sourceFile) throws IOException {
		boolean don = false;
		FileOutputStream outputStream = null;
		Buffer buffer = null;
		try {
			outputStream = new FileOutputStream(sourceFile);
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
			don = true;
		} catch (IOException e) {
			new ErrorDAO().saveError(e, "ProductServlet.uploadFile");
			throw new RuntimeException(e);
		} finally {
			if (buffer != null)
				buffer.close();
			if (outputStream != null)
				outputStream.close();
		}
		return don;

	}

	private String getFileName(String path, String fileName) {
		int index = 1;
		String sourceFile = null;
		if (fileName != null && !fileName.isEmpty()) {
			sourceFile = fileName;
			do {
				sourceFile = FilenameUtils.concat(path, sourceFile);

				if (!new File(sourceFile).exists())
					break;

				sourceFile = FilenameUtils.getBaseName(fileName) + "("
						+ (index++) + ")."
						+ FilenameUtils.getExtension(fileName);
			} while (true);
		}

		return sourceFile;
	}

	public void mkDirs(String path) {
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdirs();
	}

	private void getPdf(HttpServletResponse response, String partNumber, OrderByService orderBy) throws Exception {
		logger.entry(response, partNumber, orderBy);

		ByteArrayOutputStream baos = Pdf.getPdf(partNumber, pathLogo, orderBy);

		// setting the content type
		response.setContentType("application/pdf");
		// the content length
		response.setContentLength(baos.size());

		// write ByteArrayOutputStream to the ServletOutputStream
		try(OutputStream os = response.getOutputStream()){
			baos.writeTo(os);
		}

		logger.exit();
	}

	public static class ProductStructureFields{

		private enum Command{
			EXSEL,
			PDF,
			ADD_BOM,
			GET_BOM
		}

		private String partNumber;
		private OrderByService orderBy;
		private boolean isBOM;
		private boolean isIgnoreFootprint;
		private boolean isWithQty;
		private boolean isSymbol;
		private boolean isOrderByReference;
		private Command command;
		private String sourceFile;

		private HttpServletRequest request;
		private HttpServletResponse response;

		public ProductStructureFields(HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
			this.request = request;
			this.response = response;
		}

		public void setSourceFile(String sourceFile) {
			this.sourceFile = sourceFile;
		}

		public void setFields() throws JsonParseException, JsonMappingException, IOException {
			setPartNumber(request.getParameter("pn"));
			setOrderBy(getOrderBy(request.getParameter("ob")));
			setBOM(request.getParameter("bom"));
		}

		public String getPartNumber() {
			return partNumber!=null ? partNumber : "";
		}
		public void setPartNumber(String partNumber) {
			logger.entry(partNumber);
			if(partNumber==null)
				this.partNumber = CookiesWorker.getCookieValue(request, "BomPN");
			else{
				CookiesWorker.addCookie(request, response, "BomPN", partNumber, 24*60*60*1000);
				this.partNumber = partNumber;
			}
			logger.trace("\n\tthis.partNumber=\t{}", this.partNumber);
		}

		public OrderByService getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(OrderByService orderBy) throws JsonParseException, JsonMappingException, IOException {
			logger.entry(orderBy);
			if(orderBy==null) {
				String cookieValue = CookiesWorker.getCookieValue(request, "BomOB");
				logger.trace("\n\tBomOB={}", cookieValue);
				if(cookieValue!=null) {
					try{
						OrderByBean orderByBean = Jackson.jsonStringToObject(OrderByBean.class, cookieValue);
						this.orderBy = new OrderByService().setOrderByBean(orderByBean);
					}catch(UnrecognizedPropertyException ex){
						this.orderBy = null;
					}
				} else{
					this.orderBy = null;
					CookiesWorker.removeCookiesStartWith(request, response, "BomOB");
				}
			} else{
				setOrderByReference("false");
				CookiesWorker.addCookie(request, response, "BomOB", Jackson.objectToJsonString(orderBy.getOrderByBean()), 24*60*60*1000);
				this.orderBy = orderBy;
			}
			logger.trace("\n\t"
					+ "this.orderBy=\t{}\n\t"
					+ "isOrderByReference=\t{}",
					this.orderBy,
					isOrderByReference);
		}

		public boolean isOrderByReference() {
			return isOrderByReference;
		}
		public void setOrderByReference(String isOrderByReference) {
			logger.entry(isOrderByReference);

			if(isOrderByReference==null)
				isOrderByReference = CookiesWorker.getCookieValue(request, "isOrderByReference");
			else
				CookiesWorker.addCookie(request, response, "isOrderByReference", isOrderByReference, 24*60*60*1000);

			this.isOrderByReference = isOrderByReference==null && orderBy==null ||
										isOrderByReference!=null && isOrderByReference.equals("true");

			if(this.isOrderByReference){
				orderBy = null;
				CookiesWorker.removeCookiesStartWith(request, response, "BomOB");
			}

			logger.trace("\n\t"
					+ "this.isOrderByReference=\t{}\n\t"
					+ "orderBy=\t{}",
					this.isOrderByReference,
					orderBy);
		}

		public boolean isBOM() {
			return isBOM;
		}

		public void setBOM(String isBOM) {
			logger.entry(isBOM);
			if(isBOM==null)
				isBOM = CookiesWorker.getCookieValue(request, "isBOM");
			else
				CookiesWorker.addCookie(request, response, "isBOM", isBOM, 24*60*60*1000);
			this.isBOM = isBOM!=null && isBOM.equals("true");
			logger.trace("\n\tthis.isBOM=\t{}", this.isBOM);
		}

		public boolean isIgnoreFootprint() {
			return isIgnoreFootprint;
		}
		public void setIgnoreFootprint(String isIgnoreFootprint) {
			logger.entry(isIgnoreFootprint);
			if(isIgnoreFootprint==null)
				isIgnoreFootprint = CookiesWorker.getCookieValue(request, "isIgnoreFootprint");
			else
				CookiesWorker.addCookie(request, response, "isIgnoreFootprint", isIgnoreFootprint, 24*60*60*1000);
			this.isIgnoreFootprint = isIgnoreFootprint!=null && isIgnoreFootprint.equals("true");
			logger.trace("\n\tthis.isIgnoreFootprint=\t{}", this.isIgnoreFootprint);
		}

		public boolean isWithQty() {
			return isWithQty;
		}

		public void setWithQty(String isWithQty) {
			logger.entry(isWithQty);
			if(isWithQty==null)
				isWithQty = CookiesWorker.getCookieValue(request, "isWithQty");
			else
				CookiesWorker.addCookie(request, response, "isWithQty", isWithQty, 24*60*60*1000);
			this.isWithQty = isWithQty!=null && isWithQty.equals("true");
			logger.trace("\n\tthis.isWithQty=\t{}", this.isWithQty);
		}

		public boolean isSymbol() {
			return isSymbol;
		}
		public void setSymbol(String isSymbol) {
			logger.entry(isSymbol);
			if(isSymbol==null)
				isSymbol = CookiesWorker.getCookieValue(request, "isSymbol");
			else
				CookiesWorker.addCookie(request, response, "isSymbol", isSymbol, 24*60*60*1000);
			this.isSymbol = isSymbol!=null && isSymbol.equals("true");
			logger.trace("\n\tthis.isSymbol=\t{}", this.isSymbol);
		}

		public Command getCommand() {
			return command;
		}

		public void setCommand(Command command) {
			this.command = command;
		}

		private OrderByService getOrderBy(String orderByStr) {

			OrderByService orderBy = null;
			if(orderByStr!=null && !orderByStr.isEmpty())
				orderBy = new OrderByService(orderByStr);

			return orderBy;
		}

		public String getSourceFile() {
			return sourceFile;
		}
	}
}