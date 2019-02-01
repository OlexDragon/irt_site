package irt.stock.rest.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.production.ProductionUnit;
import irt.stock.data.jpa.beans.production.SoftwareBuild;

public class ShippedLogHelper {
	private final static Logger logger = LogManager.getLogger();


	public static Optional<Date> stringToDate(String dateStr) {
		
		String[] split = Arrays.stream(dateStr.split("[\\s\\/\\-:]")).map(s->s.replaceAll("\\D", "")).filter(s->!s.isEmpty()).toArray(String[]::new);
		String pattern;

		switch(split.length){

		case 1:
			pattern = "yyww";
			break;

		case 3:
		case 4:
			if(split[0].length()==4)
				pattern = "yyyy MM dd";
			else if(split[2].length()==4)
				pattern = "MM dd yyyy";
			else
				return Optional.empty();
			break;

		case 5:
			if(split[0].length()==4)
				pattern = "yyyy MM dd HH mm";
			else if(split[0].length()==4)
				pattern = "dd MM yyyy HH mm";
			else
				return Optional.empty();
			break;

		default:
			if(split.length>0)
				logger.debug("{}, {}", dateStr, split);
			return Optional.empty();
		}
		String join = Arrays.stream(split).collect(Collectors.joining(" "));
		try {

			return Optional.of(new SimpleDateFormat(pattern).parse(join));

		} catch (ParseException e) {
			logger.error("Entry: {}", dateStr);
			logger.catching(e);
			return Optional.empty();
		}
	}

	public static Optional<Date> getDateCellValue(Cell dateCell) {

		Optional<Cell> ofNullable = Optional.ofNullable(dateCell);

		return ofNullable
				.filter(c->c.getCellType()==CellType.NUMERIC)
				.filter(HSSFDateUtil::isCellDateFormatted)
				.map(Cell::getDateCellValue)
				.map(Optional::of)
				.orElseGet(()->{
					if(ofNullable.isPresent())
						logger.debug("getRowIndex:{}; {}", ofNullable.map(c->c.getRowIndex() + "; getColumnIndex:" + c.getColumnIndex()), ofNullable);
					return Optional.empty();
				});
	}

	public static Optional<Date> parseDate(Cell cell) {

		return Optional.ofNullable(cell)
						.filter(c->c.getCellType()==CellType.STRING)
						.map(Cell::getStringCellValue)
						.map(ShippedLogHelper::stringToDate)
						.orElseGet(()->getDateCellValue(cell));
	}

	public static Optional<String> getCustometOrder(Row currentRow) {

		final Optional<String> oCustomerOrder = Optional
												.ofNullable(currentRow.getCell(3))
												.map(Cell::getStringCellValue)
												.map(String::trim)
												.filter(co->!(co.isEmpty() || co.equals("W.O.")));

		if(!oCustomerOrder.isPresent())
			return Optional.empty();

		return oCustomerOrder
				.filter(on->!on.matches(".*\\d.*"))
				.map(on->on + " r#" + (currentRow.getRowNum() + 1))
				.map(Optional::of)
				.orElse(oCustomerOrder);
	}

	public static ProductionUnit getProductionUnit(Cell serialNumberCell, Cell partNumberCell, Cell descriptionCell, Cell softwareBuildCell) {

		Optional<Cell> oSerialNumber = Optional.ofNullable(serialNumberCell);

		String serialNumber = oSerialNumber
								.filter(c->c.getCellType()==CellType.NUMERIC)
								.map(Cell::getNumericCellValue).map(Double::intValue)
								.map(n->n.toString())
								.orElseGet(
										()->
										oSerialNumber
										.filter(c->c.getCellType()==CellType.STRING)
										.map(Cell::getStringCellValue)
										.map(String::trim)
										.filter(s->!s.isEmpty())
										.map(
												s->
												oSerialNumber
												.filter(c->!s.matches(".*\\d.*"))
												.map(c->s + "r#" + (c.getRowIndex() + 1))
												.orElse(s))
										.orElseGet(
												()->
												oSerialNumber
												.map(c->"r#" + (c.getRowIndex() + 1))
												.orElse(null)));

		if(serialNumber==null)
			return null;

		PartNumber partNumber;
		final String stringCellValue = Optional.ofNullable(descriptionCell).map(Cell::getStringCellValue).orElse(null);
		try{ partNumber = new PartNumber(partNumberCell.getStringCellValue(), null, stringCellValue); }catch (Exception e) { partNumber = null; }

		SoftwareBuild softwareBuild = getDateCellValue(softwareBuildCell).map(d->new SoftwareBuild(d, null)).orElse(null);

		return new ProductionUnit(serialNumber, stringCellValue, partNumber, softwareBuild, null, null, null);
	}
}
