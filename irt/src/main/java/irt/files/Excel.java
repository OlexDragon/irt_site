package irt.files;

import irt.data.components.Data;
import irt.data.dao.BomDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;
import irt.table.OrderBy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	public static void uploadExcel(HttpServletResponse response, String partNumberStr, String pathLogo, boolean isQty, OrderBy orderBy) throws SQLException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet worksheet = workbook.createSheet("IRT Technologies. BOM");
		XSSFFont font = workbook.createFont();
		CellStyle style = workbook.createCellStyle();
	    style.setFont(font);

	    Data topComponent = new ComponentDAO().getData(partNumberStr = partNumberStr.trim().replace("-", ""));

		try {
			//Logo Image
			InputStream is = new FileInputStream(pathLogo);
			byte[] image = IOUtils.toByteArray(is);
			int pictureIndex = workbook.addPicture(image, Workbook.PICTURE_TYPE_JPEG);
		    is.close();

		    CreationHelper helper = workbook.getCreationHelper();
		    ClientAnchor anchor = helper.createClientAnchor();
		    anchor.setCol1(0);
		    anchor.setRow1(0);

		    Drawing drawing = worksheet.createDrawingPatriarch();
		    Picture picture = drawing.createPicture(anchor, pictureIndex);

		    //Text
			XSSFRow row = worksheet.createRow(0);
			XSSFCell cell = row.createCell(2);
			cell.setCellValue("IRT Technologies.");
			cell.setCellStyle(style);

			cell = row.createCell(4);
			cell.setCellValue(topComponent.getPartNumberF());
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cell.setCellStyle(style);

			cell = row.createCell(7);
			cell.setCellValue(Calendar.getInstance());
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
		    cellStyle.setDataFormat(
		        helper.createDataFormat().getFormat("mmm dd, yyyy"));
		    cell.setCellStyle(cellStyle);
		    
			row = worksheet.createRow(1);
			cell = row.createCell(2);
			cell.setCellValue("Bill Of Materials ");
			cell.setCellStyle(style);

			cell = row.createCell(4);
			cell.setCellValue(topComponent.getDescription());
			cell.setCellStyle(style);

			row = worksheet.createRow(2);
			cell = row.createCell(2);
			cell.setCellValue("from "+topComponent.getManufPartNumber().substring(8));
			cell.setCellStyle(style);

			row = worksheet.createRow(3);

			style = workbook.createCellStyle();
			style.setBorderTop(CellStyle.BORDER_THIN);
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setFont(font);

			XSSFCellStyle tableStyle = workbook.createCellStyle();
			tableStyle.setBorderTop(CellStyle.BORDER_THIN);
			tableStyle.setBorderLeft(CellStyle.BORDER_THIN);
			tableStyle.setBorderRight(CellStyle.BORDER_THIN);
			tableStyle.setBorderBottom(CellStyle.BORDER_THIN);
			tableStyle.setFont(font);
			tableStyle.setWrapText(true);

			XSSFFont tableFont = workbook.createFont();
			tableFont.setFontHeightInPoints((short) 9);
			tableStyle.setFont(tableFont);

			new BomDAO().getBomTableExel(worksheet, partNumberStr, isQty, orderBy, style, tableStyle);

			worksheet.autoSizeColumn(0);
			worksheet.setColumnWidth(1, 11000);
			worksheet.autoSizeColumn(2);
			worksheet.autoSizeColumn(3);
			worksheet.setColumnWidth(4, 4000);
			worksheet.autoSizeColumn(5);
			worksheet.autoSizeColumn(6);
			if(isQty){
				worksheet.setColumnWidth(7, 7000);
				worksheet.setColumnWidth(8, 4000);
			}else
				worksheet.setColumnWidth(7, 4000);

			picture.resize(1);

			response.setContentType("application/vnd.openxml");
			response.setHeader("Content-Disposition", "attachment; filename=\"IrtTechnologiesBOM.xlsx\"");
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();

		} catch (IOException e) {
			new ErrorDAO().saveError(e, "Exel.uploadExcel");
			throw new RuntimeException(e);
		}
	}

}
