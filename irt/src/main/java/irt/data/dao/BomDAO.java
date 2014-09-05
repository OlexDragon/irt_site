package irt.data.dao;

import irt.data.components.Capacitor;
import irt.data.components.Component;
import irt.data.components.ComponentIds;
import irt.data.components.Data;
import irt.data.partnumber.PartNumberDetails;
import irt.product.BillOfMaterials;
import irt.product.BomRef;
import irt.product.BomUnit;
import irt.product.BomUnitInterface;
import irt.table.HTMLHeader;
import irt.table.OrderByService;
import irt.table.Table;
import irt.work.TextWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


public class BomDAO extends DataAccessObject {

   private Logger logger = LogManager.getLogger();

	private static final String SQL_COLUMN_PART_REFERENCE_AS = "`irt`.ref(IF(`cosl`.`schematic_letter`IS NOT NULL,`cosl`.`schematic_letter`,`c`.`schematic_letter`),`ref`)AS";
	private static final String FOOTPRINT = "Footprint";
	private static final String MFR_P_N = "Mfr P/N";
	private static final String MID = "MID";
	public static final String PART_NUMBER = "Part Number";
	private static final String VALUE = "Value";
	public static final String PART_REFERENCE = "Part Reference";
	private static final String QTY = "Qty";
	private static final String STOCK_QTY = "SQty";

	public boolean insert(BillOfMaterials billOfMaterials) {

		boolean isInserted = false;

		if (billOfMaterials!=null && !billOfMaterials.isError())
			if (!billOfMaterials.hasBom()) {
				// set BOM creation date
				for(BomUnitInterface u:billOfMaterials.getUnits()){
					if(billOfMaterials.isTop()){
						if(insertToBomTop(u)>=0)
							isInserted = true;
					}else if(insertReferences(u)>=0){
							isInserted = true;
						}
					if(isInserted)
						insert(billOfMaterials.getTopId(), u);//fill in bom_ref and bom tables
					else
						getError().setErrorMessage("Error. <small>(E018)</small>");
					if(!billOfMaterials.isTop())
						new ComponentDAO().setFootprint(u.getComponent());
				}
				billOfMaterials.setBomCreationDate("IRT BOM "+getDate());//Fill Manufacture Manufacture part number like "IRT BOM YYYY.MM.DD"
			} else
				getError().setErrorMessage("Error. <small>(E019)</small>");

		return isInserted;
	}

	public String getDate() {
		Calendar calendar = Calendar.getInstance();
		String date = calendar.get(Calendar.YEAR)+"."
				+ TextWorker.addZeroInFront(
						"" + (calendar.get(Calendar.MONTH)+1), 2)+"."
				+ TextWorker.addZeroInFront(
						"" + calendar.get(Calendar.DATE), 2);
		return date;
	}

	private boolean insert(int topCompIndex, BomUnitInterface u) {
		boolean isInserted = false;

		int compId = u.getComponent().getId();
		if (topCompIndex>0 && compId>0 && !isInBom(topCompIndex, compId)) {

			String query = "INSERT INTO `irt`.`bom` (`id_top_comp`,`id_components`, `id_bom_ref`) VALUES ("
					+ topCompIndex + "," + compId +","+u.getRefId()+ ")";

				isInserted = executeUpdate(query)>0;
		}
		return isInserted;
	}

	/**
	 * @param billOfMaterialsUnit
	 * @return reference ID
	 */
	private long insertReferences(BomUnitInterface u) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		getIdBomRef(u);

		if (u.getRefId()<0) {
			String query = "INSERT INTO `irt`.`bom_ref` (`ref`) VALUES ('"
					+ u.getRefNumbersShort()
					+ "')";
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();
				statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
				resultSet = statement.getGeneratedKeys();

				if(resultSet.next())
					u.setRefId(resultSet.getLong(1));
				else
					u.setRefId(-1);

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "BomDAO.insertReferences");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}
		}

		return u.getRefId();
	}

	private long insertToBomTop(BomUnitInterface u) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		getIdBomTop(u);

		if (u.getRefId()<0) {
			String query = "INSERT INTO `irt`.`bom_top` (`drawing_id`,`qty`) VALUES ('"
					+ u.getRefLetters()//get Item No
					+ "',"
					+ u.getQuantity()
					+ ")";
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();
				statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
				resultSet = statement.getGeneratedKeys();
				
				if(resultSet.next())
					u.setRefId(resultSet.getLong(1));
				else
					u.setRefId(-1);

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "BomDAO.insertToBomTop");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}
		}

		return u.getRefId();
	}

	private boolean isInBom(int topCompId, int componentId) {

		String query = "select`id_components`from`irt`.`bom`" +
						"where`id_top_comp`="+topCompId+" and`id_components`="+componentId;
		return isResult(query);
	}

	/**
	 * 
	 * @param u
	 * @return the 
	 * reference id number
	 */
	private void getIdBomRef(BomUnitInterface u) {

		String query = "select`id`from`irt`.`bom_ref`where`ref`='"+u.getRefNumbersShort()+"'";

		Object obj = getSQLObject(query);
		u.setRefId(obj!=null ? (long)obj : -1);
	}

	private void getIdBomTop(BomUnitInterface u) {

		String query = "SELECT`id`FROM`irt`.`bom_top`WHERE`drawing_id`='"+u.getRefLetters()+"'"+//Item No
														"AND`qty`="+u.getQuantity();

		Object obj = getSQLObject(query);
		u.setRefId(obj!=null ? (long)obj : -1);
		}

	public Table getTableWhereUsed(String partNumber) {
		Table table = null;

		if(partNumber!=null && !partNumber.isEmpty()){

			Data component = new ComponentDAO().getData(partNumber);
			if(component!=null){

				String query = "SELECT CONCAT('<a href=\"product_structure?pn=',`top`.`part_number`,'&bom=true\" >',`irt`.part_number(`top`.`part_number`),'</a>')AS`"+PART_NUMBER+"`," +
									"`top`.`Description`," +
									SQL_COLUMN_PART_REFERENCE_AS+"`"+PART_REFERENCE+"`," +
									"`irt`.component_qty(`ref`)AS`"+QTY+"`" +
								"FROM`irt`.`components`AS`top`" +
							"JOIN`irt`.`bom`AS`b`ON`b`.`id_top_comp`=`top`.`id`" +
							"JOIN`irt`.`bom_ref`AS`r`ON`r`.`id`=`b`.`id_bom_ref`" +
							"JOIN`irt`.`components`as`c`ON`c`.`id`=`b`.`id_components`" +
							"LEFT JOIN`irt`.`components_old_schematic_letters`AS`cosl`ON`cosl`.`id_components`=`c`.`id`AND`cosl`.`last_date`>STR_TO_DATE(SUBSTRING_INDEX(`top`.`manuf_part_number`,' ', -1),'%Y.%m.%d')" +
							"WHERE`c`.`part_number`='"+partNumber + "'" +
							"GROUP BY`Part Number`";
//irt.work.Error.setErrorMessage(query);

				if((table = getTable(query, null))!=null){
					table.setClassName("border");
					table.setTitle(new HTMLHeader(component.getDescription()+" ("+component.getPartNumberF()+")", "cBlue", 3));
					table.setColumnClassName(2,"ref");
				}

			}else
				getError().setErrorMessage("Part Number is not correct.");
		}

		return table;
	}

	public BomRef getBomUnit(String topLevelPartNumber, String componentPartNumber){

		BomUnit bomUnit = null;

		String query = "select	 `r`.`ref`," +
								"`c`.`part_number`," +
								"`c`.`manuf_part_number`," +
								"`c`.`manuf_id`," +
								"`c`.`location`," +
								"`c`.`qty`," +
							"from`irt`.`bom_ref`as`r`" +
						"join`irt`.`bom`as`b`on`b`.`id_bom_ref`=`r`.`id`" +
						"join`irt`.`components`as`c`on`r`.`id_top_comp`=`c`.`id`" +
						"join`irt`.`components`as`top`on`top`.`id`=`b`.`id_top_comp`" +
						"where`c`.`part_number`='"+componentPartNumber+"'and`top`.`part_number`='"+topLevelPartNumber+"'";

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			if(resultSet.next()){
				bomUnit = new BomUnit(resultSet.getString("part_number"),
						resultSet.getString("manuf_part_number"),
						resultSet.getString("manuf_id"),
						resultSet.getString("location"),
						resultSet.getString("quantity"),
						resultSet.getString("ref"));
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getBomUnit");
			throw new RuntimeException(e);
		}

		return bomUnit;
	}

	public List<BomUnitInterface> getBomUnits(int topLevelId) {

		List<BomUnitInterface> bomUnits = null;

		String query = "select`c`.`part_number`," +
								"`c`.`manuf_part_number`," +
								"`c`.`manuf_id`," +
								"`c`.`location`," +
								"`c`.`qty`," +
								"`ref`" +
							"from`irt`.`components`as`c`" +
						"join`irt`.`bom`as`b`on`id_components`=`c`.`id`" +
						"join`irt`.`components`as`top`on`id_top_comp`=`top`.`id`" +
						"join`irt`.`bom_ref`as`r`on`r`.`id`=`id_bom_ref`" +
						"where`top`.`id`='"+topLevelId+"'";

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			bomUnits = new ArrayList<>();
			while(resultSet.next()){
				bomUnits.add(new BomUnit(resultSet.getString("part_number"),
						resultSet.getString("manuf_part_number"),
						resultSet.getString("manuf_id"),
						resultSet.getString("location"),
						resultSet.getString("quantity"),
						resultSet.getString("ref")));
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getBomUnits");
			throw new RuntimeException(e);
		}

		return bomUnits;
	}

	/**
	 * @param partNumber
	 * @param logo - Picture (logo)
	 * @param orderBy 
	 * @return
	 */
	public PdfPTable getPdfTable(String partNumber, Image logo, OrderByService orderBy) {
		logger.entry(partNumber, logo, orderBy);

		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);
		Font fontIrt = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
		Data topComponent = new ComponentDAO().getData(partNumber);

		PdfPTable pdfPTable = new PdfPTable(8);
		PdfPCell cell;
//Header 1
		cell = new PdfPCell(logo);
		cell.setBorder(0);
		pdfPTable.addCell(cell);	//1
		cell = new PdfPCell();
		cell.setBorder(0);
		pdfPTable.addCell(cell);	//2
		cell = new PdfPCell(new Phrase("IRT Tecnologies. Bill Of Materials "+topComponent.getMfrPN().substring(8),fontIrt));
		cell.setBorder(0);
		pdfPTable.addCell(cell);	//3

		cell = new PdfPCell(new Phrase(topComponent.getPartNumberF()+"\n"+topComponent.getDescription(),fontIrt));
		cell.setBorder(0);
		cell.setColspan(4);
		pdfPTable.addCell(cell);	//4,5,6,7

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy");	
		cell = new PdfPCell(new Phrase(date_format.format(date),fontTitle));
		cell.setBorder(0);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(cell);	//7,8
//Header 2
		pdfPTable.addCell(new Phrase());
		pdfPTable.addCell(new Phrase(QTY,fontTitle));
		pdfPTable.addCell(new Phrase(PART_REFERENCE,fontTitle));
		pdfPTable.addCell(new Phrase(VALUE,fontTitle));
		pdfPTable.addCell(new Phrase(PART_NUMBER,fontTitle));
		pdfPTable.addCell(new Phrase(MID,fontTitle));
		pdfPTable.addCell(new Phrase(MFR_P_N,fontTitle));
		pdfPTable.addCell(new Phrase(FOOTPRINT,fontTitle));
		pdfPTable.setWidthPercentage(100);

		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection c = null;
		Statement s = null;
		ResultSet rs = null;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = getBomResultSet(statement, topComponent.getPartNumber(), orderBy);
			c = getDataSource().getConnection();
			s = conecsion.createStatement();

			pdfPTable.setWidths(new float[]{0.5f,0.4f,5f,1f,1.4f,0.4f,1.6f,1.3f});

			int index = 1;
			while(resultSet.next()){
				pdfPTable.addCell(new Phrase(""+index++,font));
				addRowToPdfTable(pdfPTable,resultSet,font);
			}

			pdfPTable.setHeaderRows(2);

		} catch (SQLException | DocumentException e) {
			new ErrorDAO().saveError(e, "BomDAO.getBomUnits");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
			close(rs, s, c);
		}

		return logger.exit(pdfPTable);
	}

	public PdfPTable getTopPdfTable(String partNumber, Image logo) {

		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);
		Font fontIrt = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
		Data topComponent = new ComponentDAO().getData(partNumber);

		int numColumns = 4;
		PdfPTable pdfPTable = new PdfPTable(numColumns);
		PdfPCell cell;
//Header 1
		cell = new PdfPCell(logo);
		cell.setBorder(0);
		cell.setRowspan(2);
		pdfPTable.addCell(cell);	//1
		cell = new PdfPCell(new Phrase("IRT Tecnologies. Bill Of Materials "+topComponent.getMfrPN().substring(8),fontIrt));
		cell.setBorder(0);
		cell.setColspan(2);
		pdfPTable.addCell(cell);	//2,3
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy");	
		cell = new PdfPCell(new Phrase(date_format.format(date),fontTitle));
		cell.setBorder(0);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(cell);	//4

		cell = new PdfPCell(new Phrase(topComponent.getPartNumberF()+" - "+topComponent.getDescription(),fontIrt));
		cell.setBorder(0);
		cell.setColspan(3);
		pdfPTable.addCell(cell);	//2-4

//Header 2
		pdfPTable.addCell(new Phrase("Item No",fontTitle));
		pdfPTable.addCell(new Phrase(PART_REFERENCE,fontTitle));
		pdfPTable.addCell(new Phrase("Description",fontTitle));
		pdfPTable.addCell(new Phrase(QTY,fontTitle));
		pdfPTable.setWidthPercentage(100);

		String query = "SELECT	`drawing_id`AS`Item No`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`Description`," +
								"`bt`.`Qty`" +
							"FROM`irt`.`bom_top`AS`bt`" +
						"JOIN`irt`.`bom`ON`bom`.`id_bom_ref`=`bt`.`id`" +
						"JOIN`irt`.`components`AS`c`ON`c`.`id`=`bom`.`id_components`" +
						"WHERE`bom`.`id_top_comp`="+topComponent.getId()+
						" ORDER BY`drawing_id`";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			pdfPTable.setWidths(new float[]{0.3f,1f,1f,0.3f});

			while(resultSet.next()){
				for(int i=1; i<=numColumns; i++)
					pdfPTable.addCell(new PdfPCell(new Phrase(resultSet.getString(i),font)));
			}

			pdfPTable.setHeaderRows(2);

		} catch (SQLException | DocumentException e) {
			new ErrorDAO().saveError(e, "BomDAO.getTopPdfTable");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return pdfPTable;
	}

	private ResultSet getBomResultSet(Statement statement, String topPartNumber, OrderByService orderBy) throws SQLException{

		String query = "SELECT`c`.`part_number`AS`"+PART_NUMBER+"`," +
				"`c`.`manuf_id`AS`"+MID+"`," +
				"`c`.`manuf_part_number`AS`"+MFR_P_N+"`," +
				SQL_COLUMN_PART_REFERENCE_AS+"`"+PART_REFERENCE+"`," +
				"`c`.`qty`AS`"+STOCK_QTY+"`," +
				"`c`.`footprint`AS`"+FOOTPRINT+"`," +
				"`irt`.component_qty(`ref`)AS`"+QTY+"`"+
			"FROM`irt`.`components`as`c`" +
		"JOIN`irt`.`bom`as`b`ON`c`.`id`=`b`.`id_components`" +
		"JOIN`irt`.`bom_ref`as`r`ON`b`.`id_bom_ref`=`r`.`id`" +
		"JOIN`irt`.`components`as`top`ON`top`.`id`=`b`.`id_top_comp`"+
		"LEFT JOIN`irt`.`components_old_schematic_letters`AS`cosl`ON`cosl`.`id_components`=`c`.`id`AND`cosl`.`last_date`>STR_TO_DATE(SUBSTRING_INDEX(`top`.`manuf_part_number`,' ', -1),'%Y.%m.%d')"+
		"WHERE`top`.`part_number`='"+topPartNumber+"'"+
		(orderBy==null ? "ORDER BY`c`.`schematic_letter`,SUBSTRING_INDEX(`ref`,' ',1)*1" : orderBy);

		return statement.executeQuery(query);	
	}

	private HelpClass addRowToPdfTable(PdfPTable pdfPTable, ResultSet resultSet, Font font) throws SQLException {

		HelpClass hc = null;
		if(new ComponentDAO().isExists(resultSet.getString(PART_NUMBER))){

			//"Qty","Part Reference","Value","Part Number","MID","Mfr P/N","Footprint"

			PdfPCell pdfPCell = new PdfPCell();
			PdfPCell pdfPCell2 = new PdfPCell();

			hc = new HelpClass(resultSet.getString(PART_NUMBER), pdfPCell, pdfPCell2, font);

			Thread t = new  Thread(hc);
			t.start();

			String mid = resultSet.getString(MID);
			String mfrPN = resultSet.getString(MFR_P_N);
			String footprint = resultSet.getString(FOOTPRINT);
			String qty = resultSet.getString(QTY);
			String ref = resultSet.getString(PART_REFERENCE);

			Phrase midPh = new Phrase(mid,font);
			Phrase mfrPh = new Phrase(mfrPN,font);
			Phrase footprintPh = new Phrase(footprint,font);

			pdfPTable.addCell(new PdfPCell(new Phrase(qty,font)));		//Qty par Unit
			pdfPTable.addCell(new PdfPCell(new Phrase(ref,font)));		//References

			try {

				t.join();

			} catch (InterruptedException e) {
				new ErrorDAO().saveError(e, "BomDAO.getTopPdfTable");
				throw new RuntimeException(e);
			}

			pdfPTable.addCell(pdfPCell);						//Value
			pdfPTable.addCell(pdfPCell2);						//Part Number

			pdfPTable.addCell(new PdfPCell(midPh));
			pdfPTable.addCell(new PdfPCell(mfrPh));
			pdfPTable.addCell(new PdfPCell(footprintPh));
		}
		return hc;
	}

	public String getValue(Data comp) {
		String value = "";
		int id = new SecondAndThirdDigitsDAO().getClassID(comp.getClassId());

		switch(id){
		case TextWorker.CAPACITOR:
			value = comp.getValue(Capacitor.VOLTAGE);
		case TextWorker.INDUCTOR:
		case TextWorker.RESISTOR:
			value = comp.getValue(Capacitor.VALUE)+(value.isEmpty() ? "" : ", "+value);
		}
		return value;
	}

	public List<Component> getAllComponentsWithBom() {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Component> componentWithBom = new ArrayList<>();

		String query = "SELECT`c`.*,`l`.`id`as`link_id`,`l`.`link`as`link_link`" +
							"FROM`irt`.`components`AS`c`" +
						"LEFT JOIN`irt`.`links`as`l`ON`c`.`link`=`l`.`id`" +
						"WHERE`manuf_part_number`like'irt bom%'";

		try {

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			while(resultSet.next()){
				Data c = new PartNumberDetails(null).getComponent(resultSet.getString("part_number"));
				c.setValues(resultSet);
				if(c!=null)
					componentWithBom.add((Component) c);
			}
		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "BomDAO.getAllComponentsWithBom");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return componentWithBom;
	}

	public List<ComponentIds> getAllComponentsIdsWithBom() {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<ComponentIds> componentsIdWithBom = new ArrayList<>();

		String query = "SELECT`id`,`irt`.part_number(`part_number`)AS`part_number`,`manuf_part_number`FROM`irt`.`components`WHERE`manuf_part_number`like'irt bom%'";

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			while(resultSet.next())
					componentsIdWithBom.add(new ComponentIds(resultSet.getInt("id"),resultSet.getString("part_number"),resultSet.getString("manuf_part_number")));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getAllComponentsIdsWithBom");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return componentsIdWithBom;
	}

	public Table getBomTable(String partNumber, OrderByService orderBy) {
		Table table = null;

		if(partNumber!=null && !partNumber.isEmpty()){
			Connection conecsion = null;
			Statement statement = null;
			ResultSet resultSet = null;

			String query = "SELECT`id`," +
									"concat(`description`,'(',`irt`.part_number(`part_number`),')')AS`title`" +
								"FROM`irt`.`components`"+
							"WHERE`part_number`='"+partNumber+"'";
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();
				resultSet = statement.executeQuery(query);

				if(resultSet.next()){
					String title = resultSet.getString("title");
					String topComponentId = resultSet.getString("id");

					if(partNumber.charAt(0)=='T')
						query = "SELECT`drawing_id`AS`Item No`," +
										"`bt`.`Qty`," +
										"`irt`.part_number(`part_number`)AS`"+PART_NUMBER+"`," +
										"`Description`,"+
										"`manuf_id`AS`Mfr`," +
										"`manuf_part_number`AS`Mfr Part Number`," +
										"`Location`," +
										"`c`.`qty`AS`"+STOCK_QTY+"`," +
										"`c`.`Description" +
									"FROM`irt`.`bom_top`AS`bt`" +
								"JOIN`irt`.`bom`ON`bom`.`id_bom_ref`=`bt`.`id`" +
								"JOIN`irt`.`components`AS`c`ON`c`.`id`=`bom`.`id_components`" +
								"WHERE`bom`.`id_top_comp`="+topComponentId+
								" ORDER BY`drawing_id`";
					else
						query = "SELECT`irt`.component_qty(`ref`)AS`"+QTY+"`," +
										SQL_COLUMN_PART_REFERENCE_AS+"`"+PART_REFERENCE+"`," +
										"`irt`.part_number(`c`.`part_number`)AS`Part Number`," +
										"`c`.`manuf_id`AS`"+MID+"`," +
										"`c`.`manuf_part_number`AS`"+MFR_P_N+"`," +
										"`c`.`Location`," +
										"`c`.`qty`AS`"+STOCK_QTY+"`," +
										"`c`.`Description`" +
									"FROM`irt`.`bom_ref`AS`br`" +
								"JOIN`irt`.`bom`ON`bom`.`id_bom_ref`=`br`.`id`JOIN`irt`.`components`AS`c`ON`c`.`id`=`bom`.`id_components`" +
								"JOIN`irt`.`components`AS`top`ON`top`.`id`=`bom`.`id_top_comp`" +
								"LEFT JOIN`irt`.`components_old_schematic_letters`AS`cosl`ON`cosl`.`id_components`=`c`.`id`AND`cosl`.`last_date`>STR_TO_DATE(SUBSTRING_INDEX(`top`.`manuf_part_number`,' ', -1),'%Y.%m.%d')" +
								"WHERE`bom`.`id_top_comp`="+topComponentId+
								" GROUP BY`Mfr P/N`"+
								(orderBy==null ? "ORDER BY`c`.`schematic_letter`,SUBSTRING_INDEX(`ref`,' ',1)*1" : orderBy);

//irt.work.Error.setErrorMessage(query);
					if((table = getTable(statement.executeQuery(query), "product_structure"))!=null && table.getRows()!=null && table.getRows().size()>1){
						table.setTitle(new HTMLHeader(title, "cBlue", 3));
						table.setClassName("border");
						if(partNumber.charAt(0)!='T'){
							table.setColumnClassName(1, "ref");
							table.setColumnClassName(2, "pn");
						}
					} else
						table = null;

				}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getBomTable");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		}
		
		return table;
	}

	public void getBomTableExel(XSSFSheet worksheet, String topPartNumber, boolean isQty, OrderByService orderBy, CellStyle titleStyle, XSSFCellStyle tableStyle) throws SQLException{
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT" +
				SQL_COLUMN_PART_REFERENCE_AS+"`"+PART_REFERENCE+"`," +
				"`irt`.part_number(`c`.`part_number`)AS`"+PART_NUMBER+"`," +
				"`c`.`manuf_id`AS`"+MID+"`," +
				"`c`.`manuf_part_number`AS`"+MFR_P_N+"`," +
				"`irt`.component_qty(`ref`)AS`"+QTY+"`,";

		if(isQty)
			query += "`c`.`qty`AS`"+STOCK_QTY+"`," +
					"`c`.`Description`,";
		else
			query += "`c`.`footprint`AS`"+FOOTPRINT+"`,";

		query +="''AS`Comments`"+
				"FROM`irt`.`components`as`c`" +
				"JOIN`irt`.`bom`as`b`ON`c`.`id`=`b`.`id_components`" +
				"JOIN`irt`.`bom_ref`as`r`ON`b`.`id_bom_ref`=`r`.`id`" +
				"JOIN`irt`.`components`as`top`ON`top`.`id`=`b`.`id_top_comp`"+
				"LEFT JOIN`components_old_schematic_letters`AS`cosl`ON`cosl`.`id_components`=`c`.`id`AND`cosl`.`last_date`>STR_TO_DATE(SUBSTRING_INDEX(`top`.`manuf_part_number`,' ', -1),'%Y.%m.%d')"+
				"WHERE`top`.`part_number`='"+topPartNumber+"'"+
				"GROUP BY`Mfr P/N`"+
				(orderBy==null ? "ORDER BY`c`.`schematic_letter`,SUBSTRING_INDEX(`ref`,' ',1)*1" : orderBy);

		query = "SELECT @rowNumber:=@rowNumber+1 AS`#`,`t`.*FROM("+query+")AS`t`,(SELECT @rowNumber:=0)AS`r`";
//		irt.work.Error.setErrorMessage(query);
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] columnNames = new String[metaData.getColumnCount()];

			int rownum = worksheet.getLastRowNum();

			if(columnNames.length!=0){
				XSSFRow row = worksheet.createRow(++rownum);
				for (int i = 0; i < columnNames.length; i++){
					XSSFCell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
					cell.setCellValue(metaData.getColumnLabel(i+1));
					cell.setCellStyle(titleStyle);
				}
			}

			while(resultSet.next()){
				XSSFRow row = worksheet.createRow(++rownum);
				for (int i = 0; i < columnNames.length;){
					int cellType = Cell.CELL_TYPE_BLANK;
					switch(metaData.getColumnClassName(i+1)){
					case "java.lang.Integer":
					case "java.lang.Long":
						cellType = Cell.CELL_TYPE_NUMERIC;
						break;
					case "java.lang.String":
						cellType = Cell.CELL_TYPE_STRING;
					}
					XSSFCell cell = row.createCell(i,cellType);
					cell.setCellValue(resultSet.getString(++i));
					cell.setCellStyle(tableStyle);
				}
			}
 
	}

	public Table getPOTable(List<ComponentIds> selectedComponents, int companyId, OrderByService orderBy) {
		String sqlField = "if(`NQty`<=`SQty`,0,`NQty`-`SQty`)"+(companyId>0 ? "-`CQty`" : "")+"AS`MQty`";
		return getStockTable(selectedComponents, sqlField, companyId, 0, orderBy);
	}

	public Table getCMTable(List<ComponentIds> selectedComponents, int companyId, int kitId, OrderByService orderBy) {
		String sqlField = "if(`NQty`<`SQty`,`NQty`,`SQty`)"+(companyId>0 ? "-`CQty`" : "")+(kitId>0 ? "-`KQty`" : "")+"AS`MQty`";
		return getStockTable(selectedComponents, sqlField, companyId, kitId, orderBy);
	}

	private Table getStockTable(List<ComponentIds> selectedComponents, String sqlField, int companyId ,int kitId, OrderByService orderBy){
		Table table = null;
		if(selectedComponents!=null && !selectedComponents.isEmpty()){
			String query = "SELECT*," +
								sqlField +
								"FROM(SELECT`c`.`id`," +
											"`irt`.part_number(`part_number`)AS`Part Number`," +
											"`manuf_part_number`AS`Mfr P/N`,";
			String qty = null;
			String join = "";
			String where = null;

			for(ComponentIds cIds:selectedComponents){
				if(qty==null){
					qty = "`irt`.component_qty(`r"+cIds.getId()+"`.`ref`)*"+cIds.getQuantity();
					where = "WHERE`id_top_comp`="+cIds.getId();
				}else{
					qty += "+`irt`.component_qty(`r"+cIds.getId()+"`.`ref`)*"+cIds.getQuantity();
					where += " OR`id_top_comp`="+cIds.getId();
				}
				join += "LEFT JOIN`irt`.`bom`AS`b"+cIds.getId()+"`ON`b"+cIds.getId()+"`.`id_components`=`c`.`id`"+"AND`b"+cIds.getId()+"`.`id_top_comp`="+cIds.getId()+
						" LEFT JOIN`irt`.`bom_ref`AS`r"+cIds.getId()+"`ON`r"+cIds.getId()+"`.`id`=`b"+cIds.getId()+"`.`id_bom_ref`"+
						(companyId>0 ? "LEFT JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`c`.`id`AND`cc`.`id_companies`="+companyId+" " : "")+
						(kitId>0 ? "LEFT JOIN`irt`.`kit_details`AS`kd`ON`kd`.`id_components`=`c`.`id`AND`kd`.`id_kit`="+kitId+" " : "");
			}
			query += qty + " AS`NQty`,"+
					"IF(`c`.`qty` IS NULL, 0, `c`.`qty`)AS`SQty`,"+
					(companyId>0 ? "IF(`cc`.`qty` IS NULL, 0, `cc`.`qty`)AS`CQty`," : "")+
					(kitId>0 ? "IF(`kd`.`qty` IS NULL, 0, `kd`.`qty`)AS`KQty`," : "")+
					"`Location`" +
					"FROM(`irt`.`components`AS`c`," +
						"(SELECT DISTINCT`id_components`AS`id`FROM`irt`.`bom`" +where+")AS`t`)";
			query += join+"WHERE`t`.`id`=`c`.`id`" +
					"ORDER BY`c`.`schematic_letter`"+(selectedComponents.size()==1 ? ",SUBSTRING_INDEX(`ref`,' ',1)*1" : "") +")AS`tt`" +
					(orderBy==null ? "" : " "+orderBy);

//irt.work.Error.setErrorMessage(query);
			table = getTable(query,"sellers");
		}
		return table;
	}

//***************************************************************************************************
	private class HelpClass implements Runnable{

		private String partNumberStr;
		private PdfPCell pdfPCell1;
		private PdfPCell pdfPCell2;
		private Font font;

		public HelpClass(String partNumberStr, PdfPCell pdfPCell1, PdfPCell pdfPCell2, Font font) {
			this.partNumberStr = partNumberStr;
			this.pdfPCell1 = pdfPCell1;
			this.pdfPCell2 = pdfPCell2;
			this.font = font;
		}

		@Override
		public void run() {
			Data comp;
			try(Connection connection = getDataSource().getConnection();) {
				synchronized (BomDAO.this) {
					comp = new ComponentDAO().getData(partNumberStr);
				}
			} catch (SQLException ex) {
				new ErrorDAO().saveError(ex, "BomDAO.getData");
				throw new RuntimeException(ex);
			}

			pdfPCell1.addElement(new Phrase(new Chunk(getValue(comp),font)));
			pdfPCell2.addElement(new Phrase(comp.getPartNumberF(),font));
		}
		
	}
}