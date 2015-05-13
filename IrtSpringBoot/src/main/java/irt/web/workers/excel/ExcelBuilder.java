package irt.web.workers.excel;

import irt.web.controllers.BomController;
import irt.web.entities.bom.BomEntity;
import irt.web.entities.component.ComponentEntity;
import irt.web.view.BomView;
import irt.web.view.workers.component.PartNumbers;
import irt.web.workers.beans.OrderNameVisibility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
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
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

@Service
public class ExcelBuilder {

	private final Logger logger = LogManager.getLogger();

	public byte[] getExcel(BomView bomView, OrderNameVisibilityWorker orderNameVisibilityWorker, String pathToLogo) throws IOException{
		logger.entry(pathToLogo);

		List<BomEntity> bomEntities = bomView.getBomEntities();
		if(bomEntities==null)
			return null;

		byte[] bytes = null;

		try(XSSFWorkbook workbook = new XSSFWorkbook();){
		XSSFSheet worksheet = workbook.createSheet("IRT Technologies. BOM");
		XSSFFont font = workbook.createFont();
		CellStyle style = workbook.createCellStyle();
	    style.setFont(font);
	    CreationHelper helper = workbook.getCreationHelper();

	    ComponentEntity topComponentEntity = bomEntities.get(0).getTopComponentEntity();

			//Logo Image
	    	try(InputStream is = BomController.class.getResourceAsStream(pathToLogo);){
	    		logger.trace(is);
	    		byte[] image = IOUtils.toByteArray(is);
	    		int pictureIndex = workbook.addPicture(image, Workbook.PICTURE_TYPE_JPEG);

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
				cell.setCellValue(PartNumbers.format(topComponentEntity.getPartNumber()));
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
				cell.setCellValue(topComponentEntity.getDescription());
				cell.setCellStyle(style);

				row = worksheet.createRow(2);
				cell = row.createCell(2);
				cell.setCellValue("from "+topComponentEntity.getManufPartNumber().substring(8));
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

				fillBomTable(worksheet, tableStyle, tableStyle, bomEntities, orderNameVisibilityWorker);

				int columnIndex = 0;
				worksheet.autoSizeColumn(columnIndex++);
				for(OrderNameVisibility onv:orderNameVisibilityWorker.getOrderNameVisibilities(false)){
					if(onv.isVisible()){
						String className = onv.getClassName();
						if(className!=null)
							switch(className){
							case "col-md-3":
								worksheet.setColumnWidth(columnIndex++, 11000);
								break;
							default:
								worksheet.autoSizeColumn(columnIndex++);
							}
						else
							worksheet.setColumnWidth(columnIndex++, 1500);
					}
				}

				picture.resize(3);

				try(ByteArrayOutputStream os = new ByteArrayOutputStream();){
					workbook.write(os);
					bytes = os.toByteArray();
				}
			}
		}
		return bytes;
	}

	private void fillBomTable(XSSFSheet worksheet, CellStyle style, XSSFCellStyle tableStyle, List<BomEntity> bomEntities, OrderNameVisibilityWorker orderNameVisibilityWorker) {
		int rownum = worksheet.getLastRowNum();
		XSSFRow row = worksheet.createRow(++rownum);

		List<OrderNameVisibility> onvs = orderNameVisibilityWorker.getOrderNameVisibilities(false);

		//Table header
		row = worksheet.createRow(++rownum);
		int cellIndex = 0;
		XSSFCell cell = row.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
		cell.setCellValue("#");
		cell.setCellStyle(style);
		for(OrderNameVisibility onv:onvs){
			if(onv.isVisible()){
				cell = row.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
				cell.setCellValue(onv.getName());
				cell.setCellStyle(style);
			}
		}

		cell = row.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Comments");
		cell.setCellStyle(style);

		//Table
		int rowCount = 1;
		for(BomEntity be:bomEntities){

			row = worksheet.createRow(++rownum);
			cellIndex = 0;

			cell = row.createCell(cellIndex++, Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(rowCount++);
			cell.setCellStyle(style);

			for(OrderNameVisibility onv:onvs){
				if(onv.isVisible()){
					Object value = onv.getBomEntityFieldToString().value(be);
					cell = row.createCell(cellIndex++, getCellType(onv.getBomEntityFieldToString().returnType(), value));
					if(value instanceof Integer)
						cell.setCellValue((Integer)value);
					else
						cell.setCellValue((String)value);
					cell.setCellStyle(style);
				}
			}

			cell = row.createCell(cellIndex++, Cell.CELL_TYPE_BLANK);
			cell.setCellStyle(style);
		}

		cell = row.createCell(cellIndex++, Cell.CELL_TYPE_STRING);
		cell.setCellValue("Comments");
		cell.setCellStyle(style);
	}

	private int getCellType(Class<?> clazz, Object value) {
		logger.entry(clazz, value);

		int cellType;

		switch (clazz.getSimpleName()) {
		case "Integer":
			cellType = Cell.CELL_TYPE_NUMERIC;
			break;
		default:
			if (value == null)
				cellType = Cell.CELL_TYPE_BLANK;
			else
				cellType = Cell.CELL_TYPE_STRING;
		}
		return logger.exit(cellType);
	}
}
