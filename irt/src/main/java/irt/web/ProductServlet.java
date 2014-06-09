package irt.web;

import irt.data.Error;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;
import irt.data.partnumber.PartNumber;
import irt.files.Excel;
import irt.files.Pdf;
import irt.product.BillOfMaterials;
import irt.product.BillOfMaterialsTop;
import irt.product.ProductStructure;
import irt.table.OrderBy;
import irt.work.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;

public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger logger = (Logger) LogManager.getLogger();
	private static final int ERROR = -1;
	private static final int INSERT = 0;
	private static final int SEARCH = 1;
	private static final int PDF 	= 2;
	private static final int EXCEL 	= 3;
	private static final int SYMBOL = 4;

	private final String httpAddress = "product_structure";
	
	private String realPath;
	private String realPathSymbol;

	private RequestDispatcher jsp;
	private String pathLogo;

	private boolean chckbxSymbol;
	private Error error;

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

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		ProductStructure productStructure = partNumber.getProductStructure();

		String tmpStr;
		boolean isBom = (tmpStr = request.getParameter("bom"))!=null && tmpStr.equals("true");

		tmpStr = request.getParameter("pn");
		if(tmpStr!=null)
			productStructure.setPartNumber(tmpStr, isBom);

		if((tmpStr = request.getParameter("ob"))!=null)//order by
			productStructure.setOrderBy(tmpStr);

		if (request.getParameter("pdf") != null)
			try {
				getPdf(response, productStructure);
				return;
			} catch (DocumentException e) {
				new ErrorDAO().saveError(e, "ProductServlet.doGet");
				throw new RuntimeException(e);
			}
		else if (request.getParameter("excel") != null) {
			try {
				Excel.uploadExcel(response, productStructure.getPartNumber(), pathLogo, false, productStructure.getOrderBy());
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "ProductServlet.doGet 2");
				throw new RuntimeException(e);
			}
		}

		request.setAttribute("product", productStructure);
		request.setAttribute("back_page", httpAddress);
	    jsp.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {

		PartNumber partNumber = PartNumber.getPartNumber(request.getRemoteHost());
		ProductStructure productStructure = partNumber.getProductStructure();

		switch(takeDecision(request, productStructure)){
		case INSERT:
			productStructure.toDatabase();
			break;
		case PDF:
	        try {
				if(productStructure.getOrderBy()!=null && productStructure.getOrderBy().equals("Description"))
					productStructure.setOrderBy(null);
				getPdf(response, productStructure);
				return;
			}catch(DocumentException e) {
				new ErrorDAO().saveError(e, "ProductServlet.doGet");
				throw new RuntimeException(e);
	        }
		case EXCEL:
			try {
				String partNumberStr = productStructure.getPartNumber();
				if(partNumberStr!=null) {
					OrderBy orderBy = productStructure.getOrderBy();
					if(orderBy!=null && orderBy.equals("Description"))
						orderBy = null;
					Excel.uploadExcel(response, partNumberStr, pathLogo, productStructure.isQty, orderBy);
					return;
				} else
					error.setErrorMessage("Type the PartNumber.","red");
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "ProductServlet.doPost 2");
				throw new RuntimeException(e);
			}
			break;
		case SYMBOL:
		}

		request.setAttribute("product", productStructure);
		request.setAttribute("back_page", httpAddress);
	    jsp.forward(request, response);
	}

	private int takeDecision(HttpServletRequest httpServletRequest, ProductStructure productStructure)
			throws IOException {
		String sourceFile = null;
		int decision = ERROR;

		productStructure.setSymbol(chckbxSymbol);
		chckbxSymbol = false;
		productStructure.isQty = false;
		boolean isOrder = false;
		String partNumberStr = null;
		boolean isBom = true;
		//upload sours file
		if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
			InputStream inputStream = null;
			try {
				// Create a new file upload handler
				ServletFileUpload servletFileUpload = new ServletFileUpload();

				// Parse the request
				FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(httpServletRequest);
				while (fileItemIterator.hasNext()) {
					FileItemStream fileItemStream = fileItemIterator.next();
					inputStream = fileItemStream.openStream();
					String name = fileItemStream.getFieldName();

					if (!fileItemStream.isFormField()) {
						String fileName = fileItemStream.getName();
						if (fileName != null && !fileName.isEmpty()) {
							fileName = FilenameUtils.getName(fileName);

							String extension = FilenameUtils.getExtension(fileName);
							if(!(extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls"))){
								error.setErrorMessage("Incorrect file format");
								break;
							}

							String path;
							path = productStructure.isSymbol() ? realPathSymbol : FilenameUtils.concat(realPath, productStructure.getPartNumber());
							mkDirs(path);

							//get file name
							sourceFile = getFileName(path, fileName);
							
							uploadFile(inputStream, sourceFile);
							
							
						}else{
							ProductStructure.setErrorMessage("Select the file.");
							productStructure.setSymbol(false);
						}
					}else{ //if field
						String inputStr = Streams.asString(inputStream);
						if (name.equals("check_symbol"))
							productStructure.setSymbol(chckbxSymbol = true);
						else if (name.equals("check_order"))
							isOrder=true;
						else if (name.equals("check_footprint"))
							productStructure.isFootprint = false;
						else if (name.equals("check_qty"))
							productStructure.isQty = true;
						else if (inputStr.equals("to PDF"))
							decision = PDF;
						else if (inputStr.equals("to Excel"))
							decision = EXCEL;
						else if (name.equals("pn")) {//Part Number
							partNumberStr = inputStr;
						} else if (inputStr.equals("get Symbols"))
							decision = SYMBOL;
						else if (inputStr.equals("Where Used"))
							isBom = false;
						else
							isBom = true;
					}
				}
			} catch (Exception e) {
				new ErrorDAO().saveError(e, "ProductServlet.takeDecision");
				throw new RuntimeException(e);
			} finally { if (inputStream != null)  inputStream.close(); }
		}

		productStructure.setPartNumber(partNumberStr, isBom);

		if (sourceFile != null && !sourceFile.isEmpty()) {
			if (decision == SYMBOL) {
				updateSymbols(sourceFile);
			} else {
				if (!productStructure.isSymbol()) {

			// work with uploaded file
					BillOfMaterials billOfMaterials;
					if(productStructure.isTop())
						billOfMaterials = new BillOfMaterialsTop(sourceFile);
					else
						billOfMaterials = new BillOfMaterials(sourceFile, productStructure.isFootprint);
					productStructure.set(billOfMaterials);
					if (productStructure.isErrorMessage())
						decision = ERROR;
					else if (!productStructure.hasBom())
						decision = INSERT;
					else
						decision = SEARCH;
				}
			}
		}
		productStructure.setOrderByReference(isOrder);

		if (partNumberStr != null
				&& !(partNumberStr = PartNumber.validation(partNumberStr)).isEmpty()) {
			productStructure.setPartNumber(partNumberStr,isBom);
		}

		return decision;
	}

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
				ProductStructure.setErrorMessage("File structure is not correct.");
		}
	}

	private void uploadFile(InputStream inputStream, String sourceFile) throws IOException {
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
		} catch (IOException e) {
			new ErrorDAO().saveError(e, "ProductServlet.uploadFile");
			throw new RuntimeException(e);
		} finally {
			if (buffer != null)
				buffer.close();
			if (outputStream != null)
				outputStream.close();
		}

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

	private void getPdf(HttpServletResponse response, ProductStructure productStructure) throws DocumentException, BadElementException, MalformedURLException, IOException, BadPdfFormatException {
		logger.entry(response, productStructure);

		ByteArrayOutputStream baos = Pdf.getPdf(productStructure.getPartNumber(), pathLogo, productStructure.getOrderBy());

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
}