package irt.data.dao;

import irt.data.Link;
import irt.data.Menu;
import irt.data.ValueText;
import irt.data.companies.Company;
import irt.data.components.Component;
import irt.data.components.ComponentIds;
import irt.data.components.Data;
import irt.data.components.alternative.AlternativeMfrPN;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.manufacture.ManufacturePartNumber;
import irt.data.partnumber.PartNumber;
import irt.data.partnumber.PartNumberDetails;
import irt.data.purchase.PurchaseOrderUnit;
import irt.product.ProductStructure;
import irt.table.OrderBy;
import irt.table.Row;
import irt.table.Table;
import irt.work.ComboBoxField;
import irt.work.TextWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Sheet;


//import org.apache.log4j.Logger;

public class ComponentDAO extends DataAccessObject {

	public static final int QTY_CANSEL = 0;
	public static final int QTY_ADD 	= 1;
	public static final int QTY_SUBTACT = 2;
	public static final int QTY_SET 	= 3;

//    private Logger logger = Logger.getLogger(this.getClass());
	private static final String componnentsTableQuery = 
			"SELECT %s," +
					"concat('<a href=\"/irt/part-numbers?pn=',`part_number`,'\">',`irt`.part_number(`part_number`),'</a>')AS`PartNumber`," +
					"`Value`,"+
					"`Voltage`AS`Volt`,"+
					"`manuf_part_number`AS`Mfr P/N`," +
					"`Description`," +
					"concat('<a href=\"',`m`.`link`,'\" onclick=\"window.open(this.href);return false;\">',`m`.`name`,'</a>')AS`Mfr`," +
					"`Location`," +
					"IF(`qty`IS NULL,'',`qty`)AS`Qty`," +
					"concat('<a href=\"',`l`.`link`,'\">','detail','</a>')AS`Link`" +
				"FROM`irt`.`components`AS`c`" +
			"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`c`.`manuf_id`" +
			"LEFT JOIN`irt`.`links`AS`l`ON`l`.`id`=`c`.`link`";

    public List<Data> getAll(){
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LinkedList<Data> components = new LinkedList<>();

		try {

			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement("select * from `irt`.`Components` order by `part_numbers`");
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Component data = new PartNumberDetails(null).getComponent(resultSet.getString("part_number").substring(0, 3));
				data.setValue(resultSet);
				components.add(data);
			}

		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getAll");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return components;
	}

	public int getId(String partNumber) {

		String query ="select`id`from`irt`.`components`" +
						"where `part_number` = '"+partNumber+"'";
		Object object = getSQLObject(query);
		return object!=null ? (int)object : -1;
	}

	public Table getComponentsTable(String partNumberStr, OrderBy orderBy) {

		if(orderBy==null)
			orderBy = new OrderBy("part_number");

		String query =String.format(componnentsTableQuery, "concat('<input type=\"checkbox\" name=\"checked',`c`.`id`,'\" id=\"checked',`c`.`id`,'\" />')AS''") + "WHERE`part_number`LIKE'" + partNumberStr + "'" + orderBy ;

		return getTable(query, "part-numbers");
	}

	public Menu getRevisions(String partNumber) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Menu menu = null;

		if(partNumber!=null && partNumber.length()>11)
			partNumber = partNumber.substring(0,11);

		String query = "select distinct right(`part_number`,3)AS`id`,right(`part_number`,3)AS`description`" +
						"from `irt`.`components`" +
						"where `part_number` like '"+partNumber+"%' " +
						"order by id;";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();
			menu = new Menu(resultSet, Menu.REVISION);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getRevisions");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return menu;
	}

	public Menu getMetalPartsOptions(String partNumber) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Menu menu = null;

		if(partNumber!=null && partNumber.length()>9)
			partNumber = partNumber.substring(0,9);

		String query = "SELECT distinct substr(`part_number`,10,2)AS`id`,substr(`part_number`,10,2)AS`description`"
				+ "FROM`irt`.`components`"
				+ "WHERE`part_number`LIKE'"+partNumber+"%'" + "order by`id`";

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			menu = new Menu(resultSet, Menu.OPTION);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getMetalPartsOptions");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return menu;
	}

	/**
	 * @param classId - three characters class ID
	 * @return 'Menu' where 'id' and 'description' = full part number
	 */
	public Menu getByClassId(String classId, int length) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		String query = "select distinct substr(`part_number`,4,"+length +")as`id`,`part_number`as`description`"
				+ "from `irt`.`components`"
				+ "where left(`part_number`,3) = '"+ classId + "'" 
				+ "order by id";
		Menu menu = null;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();
			menu = new Menu(resultSet, Menu.BY_CLASS_ID);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getByClassId");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return menu;
	}

	public boolean isExists(String partNumber) {

		partNumber = PartNumber.validation(partNumber);
		boolean isExists = false;

		if(partNumber!=null && partNumber.length()>8){
			String query = "select`part_number`from`irt`.`components`where`part_number`='"
							+ partNumber + "'limit 1";
			isExists = isResult(query);
		}
		return isExists;
	}

	private boolean isMPN(String manufPartNumber) {

		manufPartNumber = manufPartNumber.trim();
		boolean isExists = false;

		if (manufPartNumber != null && !manufPartNumber.isEmpty()){ 
			String query = "SELECT`id`FROM`irt`.`components`WHERE`manuf_part_number`='"+ manufPartNumber + "'" +
								"UNION " +
								"SELECT`id_components`FROM`irt`.`components_alternative`WHERE`alt_mfr_part_number`='"+ manufPartNumber + "'";
				isExists = isResult(query);
			}
		return isExists;
	}

	private boolean isAltMPN(AlternativeMfrPN altMfrPN) {
		String manufPartNumber = altMfrPN.getMfrPN().trim();
		int componentID = altMfrPN.getComponentID();

		boolean isExists = false;

		if (manufPartNumber != null && !manufPartNumber.isEmpty()){ 
			String query = "SELECT`id_components`" +
									"FROM`irt`.`components_alternative`" +
								"WHERE`alt_mfr_part_number`='"+ manufPartNumber + "'AND`id_components`="+componentID;

			isExists = isResult(query);
		}
		return isExists;
	}

	public ComponentIds getComponentIds(String partNumber) {
		ComponentIds componentIds = null;
		if (partNumber!=null)
			return selectComponentIds("SELECT`id`,`irt`.part_number(`part_number`)AS`part_number`,`manuf_part_number`,`qty`FROM`irt`.`components`WHERE`part_number`='"+partNumber+"'");

		return componentIds;
	}

	public ComponentIds getComponentIds(int componentId) {
		ComponentIds componentIds = null;
		if (componentId>0)
			return selectComponentIds("SELECT`id`,`irt`.part_number(`part_number`)AS`part_number`,`manuf_part_number`,`qty`FROM`irt`.`components`WHERE`id`="	+ componentId);

		return componentIds;
	}

	private ComponentIds selectComponentIds(String query) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ComponentIds componentIds = null;

			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();
				resultSet = statement.executeQuery(query);

				if(resultSet.next())
					componentIds = new ComponentIds(resultSet.getInt("id"), resultSet.getString("part_number"), resultSet.getString("manuf_part_number"),resultSet.getInt("qty"));

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "ComponentDAO.selectComponentIds");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}

		return componentIds;
	}

	public ComponentToMove getComponentToMove(String partNumberStr) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		ComponentToMove componentToMove = null;

		partNumberStr = PartNumber.validation(partNumberStr);


		String query ="SELECT`id`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`manuf_part_number`AS`mfrPN`," +
								"`description`," +
								"`location`," +
								"`qty`AS`StockQuantity`" +
							"FROM`irt`.`components`" +
						"WHERE`part_number`='"+partNumberStr+"'";

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if (resultSet.next())
				componentToMove = new ComponentToMove(resultSet.getInt("id"),
													resultSet.getString("Part Number"),
													resultSet.getString("mfrPN"),
													resultSet.getString("description"),
													resultSet.getInt("StockQuantity"),
													resultSet.getInt("StockQuantity"),
													resultSet.getString("location"));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponentToMove");
			throw new RuntimeException(e);
		} finally {
			close( resultSet, statement, conecsion);
		}


		return componentToMove;
	}

	public ComponentToMove getComponentToMove(long componentId) {

		ComponentToMove componentToMove = null;

		String query ="SELECT`id`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`manuf_part_number`AS`mfrPN`," +
								"`description`," +
								"`location`," +
								"`qty`AS`StockQuantity`" +
							"FROM`irt`.`components`" +
						"WHERE`id`=?";

		logger.trace("Query:\n\t{}; {}", query, componentId);

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)
				) {

			statement.setLong(1, componentId);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next())
				componentToMove = new ComponentToMove(resultSet.getInt("id"),
													resultSet.getString("Part Number"),
													resultSet.getString("mfrPN"),
													resultSet.getString("description"),
													resultSet.getInt("StockQuantity"),
													resultSet.getInt("StockQuantity"),
													resultSet.getString("location"));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponentToMove");
			throw new RuntimeException(e);
		}

		return componentToMove;
	}

	public ComponentToMove getComponentToMove(int componentId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		ComponentToMove componentToMove = null;


		String query ="SELECT`id`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`manuf_part_number`AS`mfrPN`," +
								"`description`," +
								"`location`," +
								"`qty`AS`StockQuantity`" +
							"FROM`irt`.`components`" +
						"WHERE`id`="+componentId;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if (resultSet.next())
				componentToMove = new ComponentToMove(resultSet.getInt("id"),
													resultSet.getString("Part Number"),
													resultSet.getString("mfrPN"),
													resultSet.getString("description"),
													resultSet.getInt("StockQuantity"),
													resultSet.getInt("StockQuantity"),
													resultSet.getString("location"));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponentToMove 2");
			throw new RuntimeException(e);
		} finally {
			close( resultSet, statement, conecsion);
		}


		return componentToMove;
	}

	public Data getData(String partNumber) {

		Data data = null;
		String query = "SELECT*FROM`irt`.`components`WHERE`part_number`= ?";

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);) {

			statement.setString(1, partNumber);

			try(ResultSet resultSet = statement.executeQuery();){

				if (resultSet.next()){
					data = new PartNumberDetails(null).getComponent(partNumber.substring(0,3));
					data.setValue(resultSet);
				}
			}

		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getData");
			throw new RuntimeException(e);
		}

	return data;
	}

	public int getComponentId(String partNumber) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		int componentId = -1;

		partNumber = PartNumber.validation(partNumber);

		String query = "select`id`from`irt`.`components`where`part_number`='"+partNumber+"'";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();

			if (resultSet.next()) 
				componentId = resultSet.getInt("id");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponentId");
			throw new RuntimeException(e);
		} finally {
			close( resultSet, statement, conecsion);
		}
		
	return componentId;
	}

	private String validate(String string) {

		String returnStr = "";

		if (string != null && !string.isEmpty() && !string.toUpperCase().equals("SELECT"))
			for (char ch : string.toCharArray())
				returnStr += (ch != '*')
								? (ch!='?') ? ch : '_' 
									: '%';
		else
			returnStr = "";

		return returnStr;
	}

	public boolean insert(Component component, int userId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		boolean isInserted = false;

		if (component.isSet()){
			if (isExists(component.getPartNumber())){
				if(!fillFreeFields(component))
					component.setError(component.getPartNumberF() + " - already exist. ");
			}else if(isMPN(component.getManufPartNumber()))
				component.setError(" MfrP/N "+component.getManufPartNumber() + " - already exist. ");
			else
				try {
					String query = "INSERT INTO `irt`.`components` "
							+ "(`part_number`, `manuf_part_number`, `value`, `description`, `manuf_id`, `part_type`, `voltage`)"
							+ "VALUES "
							+ "(";
						query += "'"+component.getPartNumber()+ "',"				//`part_number`
							
							+ ((!component.getManufPartNumber().isEmpty()) ? 	//`manuf_part_number`
										"'"+component.getManufPartNumber()+"'"
									: null)
							+ ",";
						query += ((component.getDbValue()!=null &&
								!component.getDbValue().isEmpty()) ?			//`value`
										 "'"+component.getDbValue()+"'"
									: null)
							+ ",";
						query += ((component.getDescription()!=null &&
								!component.getDescription().isEmpty()) ?		//`description`
										 "'"+component.getDescription()+"'"
									: null)
							+ ",";
						query += ((!component.getManufId().isEmpty()) ?			//`manuf_id`
										"'"+component.getManufId()+"'"
									 : null)
							+ ",";
						query += ((component.getPartType()!=null &&
								!component.getPartType().isEmpty()) ?			//`part_type`
										"'"+component.getPartType()+"'"
									: null)
							+ ",";
						query += ((component.getDbVoltage()!=null &&
								!component.getDbVoltage().isEmpty()) ?		//`voltage`
										"'"+component.getDbVoltage()+"'"
									: null)
							+ ")";

					conecsion = getDataSource().getConnection();
					statement = conecsion.createStatement();
					statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
					resultSet = statement.getGeneratedKeys();

					if(resultSet.next()) {
						int componentId = resultSet.getInt(1);
						if(componentId>0){
							component.setId(componentId);
							isInserted = true;
							new HistoryDAO().insert(statement, userId, "`irt`.`components`", ""+componentId);
						}
					}else
						component.setError("Error <small>(E020)</small>");

				} catch (SQLException e) {
					new ErrorDAO().saveError(e, "ComponentDAO.insert");
					throw new RuntimeException(e);
				} finally{
					close(resultSet, statement, conecsion);
				}
		}else
			component.setError("Part number is not correct. <small>(E021)</small>");

		return isInserted;
	}


	private boolean fillFreeFields(Component component) {

		boolean isUpdated = false;

		Data dataTmp = getData(component.getPartNumber());
		String toUpdate = "";
		
		if(dataTmp.getDescription().isEmpty() && !component.getDescription().isEmpty()){
			toUpdate = "`description`='"+component.getDescription()+"'";
			isUpdated = true;
		}
		
		if(dataTmp.getManufPartNumber().isEmpty() && !component.getManufPartNumber().isEmpty()){
			if(!toUpdate.isEmpty())
				toUpdate +=',';
			toUpdate += "`manuf_part_number`='"+component.getManufPartNumber()+"'";
			isUpdated = true;
		}
		
		if(dataTmp.getManufId().isEmpty() && !component.getManufId().isEmpty()){
			if(!toUpdate.isEmpty())
				toUpdate +=',';
			toUpdate += "`manuf_id`='"+component.getManufId()+"'";
			isUpdated = true;
		}

		if (!toUpdate.isEmpty()){
				String query = "UPDATE `irt`.`components` SET " + toUpdate
						+ " WHERE `part_number`='" + component.getPartNumber()
						+ "'";
				if(executeUpdate(query)>0)
					component.setError("Data have been updated.");
			}
		return isUpdated;
	}

	public String getPSNewID() {
		return count(TextWorker.COUNT_POWER_SUPPLIES, null);
	}

	public String getNewID() {
		return count(TextWorker.COUNT_COMPONENTS, null);
	}

	public String getNewHarnessID() {
		return count(TextWorker.COUNT_HARNESS, null);
	}

	public String getNewMetalID() {
		return count(TextWorker.COUNT_METAL_PARTS, "R01");
	}

	public String getNewBoardID() {
		return count(TextWorker.COUNT_PCB, "R01");
	}

	public String getNewPlugID() {
		return count(TextWorker.COUNT_PLUGS, null);
	}

	public Object getNewSequentialNumber(int componentsToCount) {
		return count(componentsToCount, null);
	}

	public String count(int countPcb, String revision) {

		String count = null;
		String query = "SELECT`part_number`,`s`.`class_id`"
						+ "FROM`irt`.`components` AS `c`"
					+ "JOIN`irt`.`first_digits`AS`fd`ON`fd`.`part_numbet_first_char`=substring(`c`.`part_number`, 1, 1)"
					+ "JOIN`irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`fd`.`id_first_digits`AND`s`.`id`=substring(`part_number`, 2, 2)"
					+ "JOIN`irt`.`counts`AS`co`ON`s`.`class_id`=`co`.`class_id`"
					+ "WHERE`co`.`id`= ? ";

		if(revision!=null)
			query +=" and`part_number`Like'%"+revision+"'";

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);) {
			
			statement.setInt(1, countPcb);

			try(ResultSet resultSet = statement.executeQuery();){

			String pn;//part number
			List<Integer> sn = new ArrayList<>();//sequential numbers
			while(resultSet.next()) {

				pn = resultSet.getString(1);

				switch(resultSet.getInt(2)){

				case TextWorker.POWER_SUPPLY:
					sn.add(Integer.parseInt(pn.substring(11)));
					break;
				case TextWorker.IC_RF:
				case TextWorker.IC_NON_RF:
				case TextWorker.MICROCONTROLLER:
				case TextWorker.VOLTAGE_REGULATOR:
				case TextWorker.AMPLIFIER:
				case TextWorker.OTHER:
				case TextWorker.GASKET:
					sn.add(Integer.parseInt(pn.substring(5,9)));
					break;
				case TextWorker.PLASTIC_PLARTS:
					sn.add(Integer.parseInt(pn.substring(5)));
					break;
				case TextWorker.WIRE_HARNESS:
				case TextWorker.CONNECTOR:
					sn.add(Integer.parseInt(pn.substring(9)));
					break;
				case TextWorker.CABLES:
					sn.add(Integer.parseInt(pn.substring(13)));
					break;
				case TextWorker.SCREW:
				case TextWorker.NUT:
				case TextWorker.WASHER:
				case TextWorker.SPACER:
					sn.add(Integer.parseInt(pn.substring(6)));
					break;
				default:
					sn.add(Integer.parseInt(pn.substring(3,7)));
				}

				for(int i=0; true; i++)
					if(!sn.contains(i)){
						count = i+"";
						break;
					}
			}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.count");
			throw new RuntimeException(e);
		}

		if(count==null)
			count = "0";
	return count;
	}

	public String getNewCapacitorID(String partNumberStr) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		partNumberStr = partNumberStr.substring(0, 12)+"__";

		String query = "SELECT RIGHT(`part_number`,2)AS`seq`FROM`irt`.`components`" +
							"WHERE `part_number`LIKE'"+partNumberStr+"'" +
						"ORDER BY`seq`";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			int i=0;
			for(;resultSet.next(); i++)
				if(i!=resultSet.getInt(1)){
					break;
				}
			partNumberStr = String.format("%2s", i).replace(' ', '0');

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getNewCapacitorID");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return partNumberStr;
	}

	public Data getByMPN(String manPartNum) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Data data = null;

		String query = "select*from`irt`.`components`" +
						"where `manuf_part_number`='"+manPartNum+"'";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();

			if (resultSet.next()){
				data = new PartNumberDetails(null).getComponent(resultSet.getString("part_number").substring(0, 3));
				data.setValue(resultSet);
			}

		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getByMPN");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		
	return data;
	}

	public boolean addLink(int userId, String partNumberStr, int linkId) {
		logger.entry(userId, partNumberStr, linkId);

		boolean isDon = false;
		partNumberStr = PartNumber.validation(partNumberStr);

		String query = "SELECT`id`FROM`irt`.`components`WHERE`part_number`=?";

		logger.trace("Query:\n\tSELECT`id`FROM`irt`.`components`WHERE`part_number`='{}'", partNumberStr);

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)) {

			statement.setString(1, partNumberStr);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {

					int componentId = resultSet.getInt(1);

					if (new HistoryDAO().setHistory(statement, userId, "`irt`.`components`", componentId, "`link`", "" + linkId)) {
						query = "UPDATE`irt`.`components`SET" + "`link`=" + ((linkId != -1) ? linkId : null) + " WHERE`id`=" + componentId;
						// irt.work.Error.setErrorMessage(query);

						isDon = executeUpdate(query) > 0;
					}
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.addLink");
			throw new RuntimeException(e);
		}

		return isDon;
	}

	public String getBomId() {
		String query = "select count(*)from`irt`.`components`where`manuf_id`like'BOM%'";
		query = (String) getSQLObject(query);
		if(query!=null)
			query = "BOM"+query;
	return query;
	}

	public boolean hasBom(String partNumber) {
		partNumber = PartNumber.validation(partNumber);

		String query = "select`manuf_part_number`from`irt`.`components`where`part_number`='"+partNumber+"'and`manuf_part_number`like'irt bom%'";
		return isResult(query);
	}

	public void setFootprint(Component component) {
		Data dbComp = getData(component.getPartNumber());
		String fp = component.getFootprint();
		String dbFp = dbComp.getFootprint();

		if (fp!=null && !fp.isEmpty() && (dbFp==null || dbFp.isEmpty())){

			String query = "update`irt`.`components`set`footprint`='"+fp
						+ "'where`id`=" + component.getId()+" and`footprint`is null";
//			logger.debug(query);
			executeUpdate(query);
		}else
			if(fp==null)
				component.setError(component+" das not have a footprint.<br />");
			else
				if(!fp.equals(dbFp))
					component.setError(component+"- Entered wrong Footprint Name(" +fp+").It should ge: "+dbFp+"<br />");
	}

	public String getDescription(String partNumber) {

		String query = "select`description`from`irt`.`components`" +
				"where`part_number`='"+partNumber+"'";
		return (String) getSQLObject(query);
	}

	public String getDescription(int componentId) {

		String query = "select`description`from`irt`.`components`" +
				"where`id`="+componentId;
		return (String) getSQLObject(query);
	}

	public String getValidRefLetters(String partNumber) {

		String query = "select`schematic_letter`from`irt`.`components`"+
							"where`part_number`='" + partNumber + "'";
		return partNumber != null && !partNumber.isEmpty() ? (String) getSQLObject(query) : null;
	}

	public void setRefLetters(int id, String refLetters) {

		if (refLetters != null 
				&& !refLetters.isEmpty()) {

			String query = "UPDATE`irt`.`components`SET`schematic_letter`='"+refLetters
						+ "'WHERE`id`='" + id+"'and`schematic_letter`is null";
//			logger.debug(query);
			executeUpdate(query);
		}
	}

	public boolean setSchematicPart(int userId, int componentId, String schematicPart) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		boolean isDon = false;
		// logger.debug(query);
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			if(new HistoryDAO().setHistory(statement, userId, "`irt`.`components`", componentId, "`schematic_part`", schematicPart)){

				String query = "UPDATE`irt`.`components`set`schematic_part`='" + schematicPart + "'" +
						"WHERE`id`=" + componentId;
				isDon = statement.executeUpdate(query)>0;
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.setSchematicPart");
			throw new RuntimeException(e);
		}  finally { close(resultSet, statement, conecsion); }

		return isDon;
	}

	public void setSchematicLetter(int userId, int componentId, String letters) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "INSERT INTO `irt`.`components_old_schematic_letters`" +
				"(`last_date`, `id_components`, `schematic_letter`,`userId`)" +
				" SELECT CURDATE(),"+componentId+",`schematic_letter`,"+userId+
					" FROM`irt`.`components`" +
					"WHERE`id`="+componentId;
//		irt.work.Error.setErrorMessage(query);
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

				if(statement.executeUpdate(query)>0){
					query = "UPDATE`irt`.`components`set`schematic_letter`='" + letters.toUpperCase() + "'" +
							"WHERE`id`='" + componentId + "'";
					statement.executeUpdate(query);
				}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.setSchematicLetter");
			throw new RuntimeException(e);
		}  finally { close(resultSet, statement, conecsion); }
	}

	public Component[] getComponents(Long[] ids, MenuDAO.OrderBy orderBy){
		Component[] components;

		if(ids==null || ids.length==0)
			return null;

		String query = "SELECT*FROM`irt`.`components`WHERE`id`IN(" +Arrays.toString(ids).replace("[", "").replace("]", "")+ ")";
		if(orderBy!=null)
			query += "ORDER BY "+orderBy;

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)) {

			logger.trace(query);

			try(ResultSet resultSet = statement.executeQuery()){
				components = getComponents(resultSet);
				logger.trace("{}", (Object[])components);
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponents(Long[] ids, MenuDAO.OrderBy orderBy)");
			throw new RuntimeException(e);
		}
		return components;
	}

	private Component[] getComponents(ResultSet resultSet) throws SQLException {

		Component[] components = null;
		if (resultSet.last()){
			int index = resultSet.getRow();
			if(index>0){
				components = new Component[index];
				do
					components[--index] = new Component(
							resultSet.getInt("id"),
							resultSet.getString("part_number"),
							resultSet.getString("manuf_part_number"),
							resultSet.getString("manuf_id"),
							resultSet.getString("description"),
							resultSet.getString("qty"),
							resultSet.getString("location"),
							new Link(resultSet.getInt("link"), null),
							resultSet.getString("footprint"),
							resultSet.getString("schematic_letter"),
							resultSet.getString("schematic_part"));
				while(resultSet.previous());
			}
		}

		return components;
	}

	public Component getComponent(int componentId) {

		Component component = null;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {
			
			component = getComponent(statement, componentId);

		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponent");
			throw new RuntimeException(e);
		} 

	return component;
	}

	public Component getComponent(Statement statement, int componentId) throws SQLException, CloneNotSupportedException {

		String query = "SELECT*FROM`irt`.`components`" +
				"WHERE`id`="+componentId;
		ResultSet resultSet = statement.executeQuery(query);

		Component component = null;

		if (resultSet.next()){
			String pn = resultSet.getString("part_number");
			if(pn!=null && pn.length()>3){
				component = new PartNumberDetails(null).getComponent(pn.substring(0,3));
				if(component!=null)
					component.setValue(resultSet);
			}
		}
		return component;
	}

	public boolean updateMfrPatrNumber(ComponentIds componentIds) {
		return executeUpdate("UPDATE`irt`.`components`SET`manuf_part_number`='"+componentIds.getMfrPartNumber()+"'WHERE`id`="+componentIds.getId())>0;
	}

	public boolean updateQuantity(ComponentToMove ctm) {
		return executeUpdate("UPDATE`irt`.`components`SET`qty`="+ctm.getStockQuantity()+" WHERE`id`="+ctm.getId())>0;
	}

	public boolean setQuantity(int componentId, int qty, int operation) {
		String qtyStr;

		switch(operation){
		case QTY_ADD:
			qtyStr = "if(`qty`IS NULL,"+qty+",`qty`+"+qty+")";
			break;
		case QTY_SUBTACT:
			qtyStr = "if(`qty`IS NULL OR`qty`<"+qty+","+0+",`qty`-"+qty+")";
			break;
		default:
			qtyStr = ""+qty;
		}
		String query = "UPDATE`irt`.`components`SET`qty`="+qtyStr+" WHERE`id`='"+componentId+"'";
//		irt.work.Error.setErrorMessage(query);
		return executeUpdate(query)>0;
	}

	public Table getComponentDetails(int id) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Table table = null;

		String query = "SELECT concat(`cmp`.`company`,' ',`cmp`.`name`)as`Name`, sum(`md`.`qty`)as`Qty`" +
							"FROM`irt`.`companies`AS`cmp`" +
						"JOIN`irt`.`movement`AS`m`ON`m`.`to_detail`=`cmp`.`id`" +
						"JOIN`irt`.`movement_details`AS`md`ON`md`.`id_movement`=`m`.`id`" +
						"WHERE `id_components`="+id+" "+
						"GROUP BY `company`, `name`";

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery(query);
			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] columnNames = new String[metaData.getColumnCount()];

			if(columnNames.length!=0){
				for (int i = 0; i < columnNames.length;)
					columnNames[i] = metaData.getColumnName(++i);

				table = new Table(columnNames, null);
				Row row;
				while(resultSet.next()){
					row = new Row();
					for(int i=1; i<=columnNames.length; i++)
						row.add(resultSet.getString(i));
					table.add(row);
				}
				if(table.getRows().size()<=1)
					table = null;
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComponentDetails");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return table;
	}

	public List<Company> getAssembledPCB() {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Company> assembled = null;

		String query = "select`id`,`irt`.part_number(`part_number`)AS`part_number`from`irt`.`components`" +
				"where `part_number`like'PCA%'and`manuf_part_number`like'IRT BOM%'";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			Company c;
			assembled = new ArrayList<>();
			while(resultSet.next()){
				c = new Company(resultSet.getInt("id"),resultSet.getString("part_number"), null, null, null, null, null, 0, false);
				assembled.add(c);
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getAssembledPCB");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return assembled;
	}

	public Table getComponentTable(int id) {

		String query = "SELECT	`c`.`id`," +
						"`IRT`.PART_NUMBER(`part_number`)AS`Part Numb.`," +
						"`manuf_part_number`AS`Mfr P/N`," +
						"`m`.`name`AS`Mfr`," +
						"`Description`," +
						"`schematic_letter`AS`SL`," +
						"`footprint`AS`FP`," +
						"`Location`," +
						"`Qty`," +
						"IF(`l`.`link` IS NULL, '', concat('<a href=\"',`l`.`link`,'\">','detail','</a>'))AS`Link`" +
					"FROM`irt`.`components`AS`c`" +
					"LEFT JOIN`irt`.`manufacture`as`m`ON`m`.`id`=`manuf_id`" +
					"LEFT JOIN`irt`.`links`AS`l`ON`l`.`id`=`c`.`link`" +
					"WHERE`c`.`id`="+id+

				" UNION " +

					"SELECT`ca`.`id`," +
							"if(`part_number`IS NULL,'',concat('<a href=\"/irt/part-numbers?pn=',`part_number`,'\">',`irt`.part_number(`part_number`),'</a>'))," +
							"`alt_mfr_part_number`," +
							"`m`.`name`AS`Mfr`," +
							"`c`.`description`," +
							"`c`.`schematic_letter`," +
							"`c`.`footprint`," +
							"`c`.`location`," +
							"`c`.`qty`," +
							"if(`c`.`link`IS NULL,'',concat('<a href=\"',`l`.`link`,'\">','detail','</a>'))" +
						"FROM`irt`.`components_alternative`AS`ca`" +
						"LEFT JOIN`irt`.`manufacture`as`m`ON`m`.`id`=`manuf_id`" +
						"LEFT JOIN`irt`.`components`AS`c`ON`c`.`manuf_part_number`=`ca`.`alt_mfr_part_number`" +
						"LEFT JOIN`irt`.`links`AS`l`ON`l`.`id`=`c`.`link`" +
					"WHERE `id_components`="+id+

				" UNION" +

				" SELECT ''," +
						"''," +
						"''," +
						"''," +
						"''," +
						"''," +
						"''," +
						"`company`," +
						"`qty`," +
						"''" +
					"FROM`irt`.`companies`AS`cmp`" +
				"JOIN`irt`.`companies_components`as`cc`ON`cc`.`id_companies`=`cmp`.`id`" +
				"WHERE`cc`.`id_components`="+id;
//irt.work.Error.setErrorMessage(query);

		return getTable(query, null);
	}

	public Table getComponentsTable(Component component) {
	
			boolean needAnd = false;
			boolean needWhereClause = false;
			
			String partNumber 		= pnValidation(component!=null ? component.getPartNumber() : null);
			if(!partNumber.isEmpty()) needWhereClause = true;
			
			String description 		= validate(component!=null ? component.getDescription() : null);
			if(!description.isEmpty()) needWhereClause = true;
	
			String manufactur 		= validate(component!=null ? component.getManufId() : null);
			if(!manufactur.isEmpty()) needWhereClause = true;
	
			String manufPartNumber	= validate(component!=null ? component.getManufPartNumber() : null);
			if(!manufPartNumber.isEmpty()) needWhereClause = true;
	
			String query = String.format(componnentsTableQuery, "concat('<input type=\"checkbox\" name=\"checked',`c`.`id`,'\" id=\"checked',`c`.`id`,'\" />')AS''");
			String query2 = String.format(componnentsTableQuery, "''").replace("manuf_part_number", "alt_mfr_part_number")
												+"JOIN`irt`.`components_alternative`AS`ca`ON`ca`.`id_components`=`c`.`id`";
	 
			if(needWhereClause){
				query += "WHERE";
				query2 += "WHERE";
			}

			if(!partNumber.isEmpty()){
				query += "`part_number`like'"+partNumber+"'";
				query2 += "`part_number`like'"+partNumber+"'";
				needAnd = true;
			}
			if(!description.isEmpty()){
				if(needAnd){
					query += "AND";
					query2 += "AND";
				}else
					needAnd = true;
				query += "`description`like'"+description+"'";
				query2 += "`description`like'"+description+"'";
			}

			if(!manufactur.isEmpty()){
				if(needAnd){
					query += " AND";
					query2 += " AND";
				}else
					needAnd = true;
				query += "`c`.`manuf_id`like'"+manufactur+"'";
				query2 += "`c`.`manuf_id`like'"+manufactur+"'";
			}

			if(!manufPartNumber.isEmpty()){
				if(needAnd){
					query += " AND";
					query2 += " AND";
				}else
					needAnd = true;
				query += "`manuf_part_number`like'"+manufPartNumber+"'";
				query2 += "`alt_mfr_part_number`LIKE'"+manufPartNumber+"'";
			}

			query = "SELECT*FROM("+query+"UNION "+query2+")AS`t`";
			query += (component.getOrderBy()!=null) ? component.getOrderBy() : "ORDER BY`PartNumber`";

 //irt.work.Error.setErrorMessage(query);

		return getTable(query, "part-numbers");
	}

	private String pnValidation(String partNumberStr) {
		String returnStr = "";

		if (partNumberStr != null){
			returnStr = partNumberStr.replaceAll("-", "").replaceAll("\\*", "%").replaceAll("\\?", "_");

			if(returnStr.endsWith("_")){
				while(returnStr.endsWith("_"))
					returnStr = returnStr.replaceAll("_$", "");
				returnStr += '%';
			}
		}

		if (returnStr.length() <= 3 && !returnStr.contains("%"))
			returnStr += '%';

		return returnStr;
	}

	public void updateSymbols(Sheet sheet, int rowNumber, int pnIndex, int symbolIndex) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			for (int i = 1; i <= rowNumber; i++) {
			org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
			String symbolExl = row.getCell(symbolIndex).getRichStringCellValue().toString();

			String pn = row.getCell(pnIndex).getRichStringCellValue().toString().replaceAll("-", "");
			String query = "SELECT`schematic_part`FROM`irt`.`components`WHERE`part_number`='"+pn+"'";
			resultSet = statement.executeQuery(query);
			if(resultSet.next()){
				String symbolSQL = resultSet.getString(1);
				if((symbolSQL==null || symbolSQL.isEmpty()) && symbolExl!=null && !symbolExl.isEmpty()){
					if(symbolExl.contains(" ")){
						ProductStructure.setErrorMessage(pn+" - Remove the space from "+symbolSQL);
						continue;
					}
					query = "UPDATE`irt`.`components`SET`schematic_part`='"+symbolExl+"'WHERE `part_number`='"+pn+"'";
					if(statement.executeUpdate(query)==0)
						ProductStructure.setErrorMessage(pn+" - Symbol '"+symbolExl+"' is not saved");
				}else if(!symbolSQL.equals(symbolExl))
					ProductStructure.setErrorMessage(pn+" - Incorrect Symbol Name <span class=\"cBlue\">'"+symbolExl+"'</span> should use <span class=\"cBlue\">'"+symbolSQL+"'</span>");
			}
		}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getAssembledPCB");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
	}

	public boolean insertAlternativeMfrPN(int userId, AlternativeMfrPN altMfrPN) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		boolean isAdded = false;
		if(altMfrPN!=null && altMfrPN.getMfrPN()!=null && altMfrPN.getComponentID()>0 && !isAltMPN(altMfrPN))
		try {
			String query = "INSERT INTO`irt`.`components_alternative`(`id_components`,`alt_mfr_part_number`,`manuf_id`)" +
							"VALUES ("+altMfrPN.getComponentID()+",'"+altMfrPN.getMfrPN()+"','"+altMfrPN.getMfrId()+"')";
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			isAdded = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)!=0;
			resultSet = statement.getGeneratedKeys();
			if(resultSet.next()){
				int id = resultSet.getInt(1);
				new HistoryDAO().insert(statement, userId, "`irt`.`components_alternative`", ""+id);
				altMfrPN.setId(id);
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.insertAlternativeMfrPN");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return isAdded;
	}

	public List<PurchaseOrderUnit> getPOUnits(ComponentsQuantity componentsQuantity) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<PurchaseOrderUnit> poUnits = null;

		if(componentsQuantity!=null)
		try {
			poUnits = new ArrayList<>();
			String query = "SELECT`c`.`id`," +
									"`irt`.part_number(`part_number`)AS`pn`," +
									"`description`," +
									"`manuf_part_number`," +
									"`name`AS`mfr`" +
								"FROM`irt`.`components`AS`c`" +
								"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`c`.`manuf_id`" +
							"WHERE`c`.`id`=";
//irt.work.Error.setErrorMessage(query);
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			for(ComponentQuantity cq:componentsQuantity.getComponentsQuantity()){
//irt.work.Error.setErrorMessage(", cq.getId(): "+cq.getId());
				resultSet = statement.executeQuery(query+cq.getId());
				if(resultSet.next()){
					PurchaseOrderUnit purchaseOrderUnit = new PurchaseOrderUnit(resultSet.getInt("id"),
														resultSet.getString("pn"),
														resultSet.getString("description"),
														new ManufacturePartNumber(0, resultSet.getString("manuf_part_number"), resultSet.getString("mfr")));
					purchaseOrderUnit.setOrderQuantity(cq.getQuantityToMove());
					poUnits.add(purchaseOrderUnit);
					resultSet.close();
					resultSet = statement.executeQuery(
							"SELECT`ca`.`id`," +
									"`alt_mfr_part_number`," +
									"`m`.`name`" +
								"FROM`irt`.`components_alternative`AS`ca`" +
							"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`ca`.`manuf_id`" +
							"WHERE`id_components`="+cq.getId());
					while(resultSet.next())
						purchaseOrderUnit.addMfrPM( resultSet.getInt("id"), resultSet.getString("alt_mfr_part_number"), resultSet.getString("name"));
					resultSet.close();
				}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getPOUnits");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return poUnits;
	}

	public ComboBoxField[] getComboBoxFields(String classIdStr){
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		ComboBoxField[] cbFields = null;

		String query = "SELECT`id`,`irt`.part_number(`part_number`)AS`pn`FROM`irt`.`components`WHERE`part_number`LIKE'"+classIdStr+"%'ORDER BY`part_number`";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();

			if (resultSet.last()){
				int index = resultSet.getRow();
				cbFields = new ValueText[index];
				cbFields[--index] = new ValueText(resultSet.getString("id"), resultSet.getString("pn"));
				while(resultSet.previous())
					cbFields[--index] = new ValueText(resultSet.getString("id"), resultSet.getString("pn"));					
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getComboBoxFields");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		
	return cbFields;
	}

	public ComponentToMove[] getAlternativeComponentsRows(int componentId) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		ComponentToMove[] components = null;

		String query = "SELECT`c`.`id`," +
								"`irt`.part_number(`part_number`)AS`pn`," +
								"`manuf_part_number`AS`mpn`," +
								"`qty`," +
								"`location`" +
							"FROM`irt`.`components`AS`c`" +
						"WHERE`c`.`id`="+componentId+

						" UNION" +

						" SELECT`c`.`id`," +
								"`irt`.part_number(`part_number`)," +
								"`manuf_part_number`," +
								"`qty`," +
								"`location`" +
							"FROM`irt`.`components`AS`c`" +
						"JOIN`irt`.`components_alternative`AS`ca`ON`ca`.`alt_mfr_part_number`=`c`.`manuf_part_number`" +
						"WHERE`ca`.`id_components`="+componentId;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();

			if (resultSet.last()){
				int index = resultSet.getRow();
				if(index>1){
					components = new ComponentToMove[index];
					do
						components[--index] = new ComponentToMove(resultSet.getInt("id"),
																resultSet.getString("pn"),
																resultSet.getString("mpn"),
																null,
																resultSet.getInt("qty"),
																0,
																resultSet.getString("location"));
					while(resultSet.previous());
				}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.getAlternativeComponentsRows");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		
	return components;
	}

	public String[] getPartNumbersContain(String contains, int limit){
		return getStringArray("SELECT concat(`irt`.part_number(`part_number`),'<br />',`manuf_part_number`)AS`pn`" +
									"FROM`irt`.`components`" +
								"WHERE`part_number`LIKE'%"+contains.replaceAll("-", "")+"%'OR`manuf_part_number`LIKE'%"+contains+"%%'LIMIT "+limit);
	}

	public String[] getPartNumberContain(String contains, int limit){
		return getStringArray("SELECT`irt`.part_number(`part_number`)AS`pn`FROM`irt`.`components`WHERE`part_number`LIKE'%"+contains+"%'LIMIT "+limit);
	}

	public String[] getDescription(String classId, String description, int limit){
		return getStringArray("SELECT DISTINCT`description`FROM`irt`.`components`WHERE`description`LIKE'%"+description+"%'AND`part_number`LIKE'"+classId+"%'LIMIT "+limit);
	}

	public String[] getMfrPartNumbet(String classId, String mftPN, int limit){
		return getStringArray("SELECT*FROM(" +
											"SELECT`manuf_part_number`" +
											"FROM`irt`.`components`" +
											"WHERE`manuf_part_number`LIKE'%"+mftPN+"%'AND`part_number`LIKE'"+classId+"%'" +
										"UNION " +
											"SELECT`alt_mfr_part_number`" +
											"FROM`irt`.`components_alternative`AS`ca`" +
											"JOIN`irt`.`components`AS`c`ON`c`.`id`=`ca`.`id_components`" +
											"WHERE`alt_mfr_part_number`LIKE'%"+mftPN+"%'AND`part_number`LIKE'"+classId+"%'" +
										")AS`t`" +
								"GROUP BY`manuf_part_number`" +
								"LIMIT 10");
	}

	public String[] getValue(String groopId, String value, int limit){
		return getStringArray("SELECT DISTINCT`value`FROM`irt`.`components`WHERE`part_number`LIKE'"+groopId+"%'AND`value`LIKE'%"+value+"%'LIMIT "+limit);
	}

	public void setComponentLocation(int rowId, String newLocation, int userId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try{

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			if(new HistoryDAO().setHistory(statement, userId, "`irt`.`components`", rowId, "`location`", newLocation)){
				String query = "UPDATE `irt`.`components` SET `location`=" +(newLocation!=null ? "'"+newLocation+"'" : null) +" WHERE `id`="+rowId;
//				irt.work.Error.setErrorMessage(query);
				statement.executeUpdate(query);
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.setComponentLocation");
			throw new RuntimeException(e);
		} finally { close(resultSet, statement, conecsion); }
	}

	public boolean update(Component component, int userId, boolean updateAll) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		boolean don = false;
		try{
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			int id = component.getId();
			Component c = getComponent(statement, id);
			if(c!=null){
				Map<String, String> fields = getFieldsToUpdate(component, c, updateAll);
				if(new HistoryDAO().setHistory(statement, userId, "`irt`.`components`", id, fields)){
					String sets = null;
					for(Entry<String, String> f:fields.entrySet())
						if(sets==null)
							sets = f.getKey()+"='"+f.getValue()+"'";
						else
							sets += ","+f.getKey()+"='"+f.getValue()+"'";

					String query = "UPDATE `irt`.`components` SET "+sets+" WHERE `id`="+id;
//irt.work.Error.setErrorMessage(query);
					don = statement.executeUpdate(query)>0;
				}
			}

		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentDAO.update");
			throw new RuntimeException(e);
		} finally { close(resultSet, statement, conecsion); }

		return don;
	}

	private Map<String, String> getFieldsToUpdate(Component componentToSet, Component componentFromDB, boolean updateAll) {
		Map<String, String> fields = new HashMap<>();
		int fn = componentToSet.getFieldsNumber();
		for(int i=0; i<fn; i++){
			String value = componentFromDB.getSuperValue(i);
			String valuetoSet = componentToSet.getSuperValue(i);
			if((value.isEmpty() || updateAll) && !valuetoSet.isEmpty()) {
				if(!valuetoSet.equals(value)){
						String fieldName = componentToSet.getFieldName(i);
						if(fieldName!=null)
							fields.put(fieldName, valuetoSet);
				}
			}
		}
		return fields;
	}
}
