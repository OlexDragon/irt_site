package irt.data.dao;

import irt.data.companies.Company;
import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.components.movement.ComponentQty;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsMovement;
import irt.data.components.movement.ComponentsQty;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.partnumber.PartNumberDetails;
import irt.table.HTMLHeader;
import irt.table.Row;
import irt.table.Table;
import irt.work.TextWorker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ComponentsMovementDAO extends DataAccessObject {

	public boolean insertDetail(int movementId, ComponentToMove ctm) {

		String query = "INSERT INTO`irt`.`movement_details`(`id_movement`,`id_components`,`qty`)" +
						"VALUES("+movementId		+"," +
								ctm.getId()	+"," +
								ctm.getQuantityToMove()+");";

		return executeUpdate(query)>0;
	}

	public int createMovement(ComponentsMovement componentsMovement) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		Company cmp;
		String query = "INSERT INTO`irt`.`movement`(`who`, `from`, `from_detail`, `to`, `to_detail`, `date_time`)"+
						"VALUES("+componentsMovement.getWho().getID()								+"," +		//who
						componentsMovement.getFrom().getId()										+"," +		//from
						(((cmp = componentsMovement.getFromDetail())!=null) ? cmp.getId() : 0)		+"," +		//from_detail
						componentsMovement.getTo().getId()											+"," +		//to
						(((cmp = componentsMovement.getToDetail())!=null) ? cmp.getId() : 0)		+", now())";//to_detail, date

	try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			
			if(resultSet.next())
				movementId = resultSet.getInt(1);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.createMovement");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}

	public boolean setStatus(int componentsMovementId, int status) {

		String query = "UPDATE`irt`.`movement`SET`status`="+status+" WHERE`id`="+componentsMovementId;
		return executeUpdate(query)>0;
	}

	public int createComponentsMovement(ComponentsMovement componentsMovement, int option) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			movementId = insertMovement(componentsMovement, statement);
			
			if(movementId>0)
					moveComponents(componentsMovement, statement, movementId, option);
			else
				getError().setErrorMessage("Error. <small>(E022)</small>");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.createComponentsMovement");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}
	private int insertMovement(ComponentsMovement componentsMovement, Statement statement) throws SQLException {
		Company cmp;
		String s;
		String query = "INSERT INTO`irt`.`movement`(`who`,`from`,`from_detail`,`to`,`to_detail`,`description`,`date_time`,`status`)"+
				"VALUES("+componentsMovement.getWho().getID()								+"," +		//who
				componentsMovement.getFrom().getId()										+"," +		//from
				(((cmp = componentsMovement.getFromDetail())!=null) ? cmp.getId() : 0)		+"," +		//from_detail
				componentsMovement.getTo().getId()											+"," +		//to
				(((cmp = componentsMovement.getToDetail())!=null) ? cmp.getId() : 0)		+"," +		//to_detail
				(((s = componentsMovement.getDescription())!=null	&&
													!s.isEmpty()) ? "'"+s+"'"	: null)		+"," +		//description
				" now()" 																	+"," +		//date time
				componentsMovement.getStatus()+")";//to_detail, date

		statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
		ResultSet resultSet = statement.getGeneratedKeys();

		int movementId = -1;
		if(resultSet.next())
			movementId = resultSet.getInt(1);
		resultSet.close();

		return movementId;
	}

	private void moveComponents(ComponentsMovement componentsMovement, Statement statement, int movementId, int option) throws SQLException {
		for(ComponentQuantity cq:componentsMovement.getDetails().getComponentsQuantity()){
	
			int componentId = cq.getId();
			int qty = 0;
			Company companyDetail = null;
			int tmpOption;
			switch(option){
			case ComponentDAO.QTY_ADD:
				qty = cq.getSumQuantity();
				companyDetail = componentsMovement.getFromDetail();
				tmpOption = ComponentDAO.QTY_SUBTACT;
				break;
			case ComponentDAO.QTY_SUBTACT:
				qty = cq.getSubtractQuantity();
				companyDetail = componentsMovement.getToDetail();
				tmpOption = ComponentDAO.QTY_ADD;
				break;
			default:
				tmpOption = option;
			}
			updateStockComponentsQuantity(statement, componentId, qty);
			int quantityToMove = cq.getQuantityToMove();
			updateCompaniesComponents(statement, companyDetail!=null ? companyDetail.getId() : 0, componentId, quantityToMove, tmpOption);
			insertMovemenyDetail(statement, movementId, componentId, cq.getStockQuantity(), quantityToMove);

		}
		updateMovemintStatus(statement, movementId, ComponentsMovement.MOVED);
	}

	private static void updateStockComponentsQuantity(Statement statement,int id, int qty)	throws SQLException {
		String query = "UPDATE`irt`.`components`SET`qty`="+qty+" WHERE `id`="+id;
		statement.executeUpdate(query);
	}

	private void updateCompaniesComponents(Statement statement, int companyId, int componentId, int quantityToMove, int option) throws SQLException {
		String query = "SELECT`qty`FROM`irt`.`companies_components`WHERE`id_companies`="+companyId+" AND`id_components`="+componentId;

//irt.work.Error.setErrorMessage(query);

		ResultSet resultSet = statement.executeQuery(query);
		if(resultSet.next()){
			int qty = 0;
			switch(option){
			case ComponentDAO.QTY_SUBTACT:
				qty = resultSet.getInt(1);
				if(qty>quantityToMove)
					qty -= quantityToMove;
				else
					qty = 0;
				break;
			default:
				qty += quantityToMove;
			}
			query = "UPDATE`irt`.`companies_components`SET`qty`="+qty+" WHERE`id_components`="+componentId+" AND`id_companies`="+companyId;
//irt.work.Error.setErrorMessage(query);
			statement.executeUpdate(query);
		}else
			if(option==ComponentDAO.QTY_ADD) {
				query = "INSERT INTO`irt`.`companies_components`" +
										"(`id_companies`,`id_components`,`qty`)VALUES" +
										"("+companyId+","+componentId+","+quantityToMove+")";
//irt.work.Error.setErrorMessage(query);
				statement.executeUpdate(query);
			}
	}

	private void updateCompaniesComponent(Statement statement, int topComponentId, int companyId, int quantityToMove) throws SQLException {

		List<String> queries = new ArrayList<>();
		String query = "SELECT`bom`.`id_components`AS`id`," +
								quantityToMove+"*`irt`.component_qty(`ref`)AS`qty`," +
								"`cc`.`qty`AS`old_qty`" +
							"FROM`irt`.`bom`" +
						"JOIN`irt`.`bom_ref`ON`bom_ref`.`id`=`bom`.`id_bom_ref`" +
						"LEFT JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`bom`.`id_components`AND`cc`.`id_companies`="+companyId+
						" WHERE`id_top_comp`="+topComponentId;
		ResultSet resultSet = statement.executeQuery(query);

		while(resultSet.next()){
			String oldQtyStr = resultSet.getString("old_qty");

			if(oldQtyStr!=null && !oldQtyStr.equals("0")) {
				int qty = resultSet.getInt("qty");
				queries.add("UPDATE`irt`.`companies_components`SET`qty`=IF(`qty`<="+qty+",0,`qty`-"+qty+")WHERE`id_components`="+resultSet.getInt("id")+" AND`id_companies`="+companyId);
			}
		}
		for(String q:queries)
			statement.executeUpdate(q);
	}

	private void updateMovemintStatus(Statement statement, int movementId, int status)throws SQLException {
		String query = "UPDATE`irt`.`movement`SET`status`="+status+" WHERE`id`="+movementId;
		statement.executeUpdate(query);
	}

	private void insertMovmentDetail(Statement statement, int movementId, int componentId, int quantityToMove) throws SQLException {

		String query = "INSERT INTO`irt`.`movement_details`(`id_movement`,`id_components`,`qty`)" +
					"VALUES("+	movementId		+"," +
								componentId		+"," +
								quantityToMove	+");";
		statement.executeUpdate(query);
	}

	public int moveFromSuplierToAssembled( ComponentsMovement componentsMovement) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			ComponentsQuantity details = componentsMovement.getDetails();
			if(details!=null && !details.isEmpty()){
				movementId = insertMovement(componentsMovement, statement);
			
				if(movementId>0){

					for(ComponentQuantity ctm:details.getComponentsQuantity()){
						int componentId = ctm.getId();
						int quantityToMove = ctm.getQuantityToMove();
						insertMovmentDetail(statement, movementId, componentId, quantityToMove);
						addComponentsQuantity(statement, componentId, quantityToMove);
						updateCompaniesComponent(statement, componentId, componentsMovement.getFromDetail().getId(), quantityToMove);
					}

					updateMovemintStatus(statement, movementId, ComponentsMovement.MOVED);
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromSuplierToAssembled");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}

	public int moveFromStockToAssembled(ComponentsMovement componentsMovement, int userId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		int backupId=-1;
		BackupDAO backupDAO = new BackupDAO();
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			ComponentsQuantity details = componentsMovement.getDetails();
			if(details!=null && !details.isEmpty()){
				movementId = insertMovement(componentsMovement, statement);
			
				if(movementId>0){
					ComponentsQuantity csq = getBomQty(details, statement);

					//backup
					List<ComponentQuantity> componentsQuantity = csq.getComponentsQuantity();
					backupId = backupDAO.creatBackup(statement, userId, "components", "qty", null, componentsQuantity);

					for(ComponentQuantity cq:componentsQuantity){
						subtractComponentsQuantity(statement, cq.getId(), cq.getQuantityToMove());
					}

					for(ComponentQuantity ctm:details.getComponentsQuantity()){
						insertMovmentDetail(statement, movementId, ctm.getId(), ctm.getQuantityToMove());
						addComponentsQuantity(statement, ctm.getId(), ctm.getQuantityToMove());	//
					}

					updateMovemintStatus(statement, movementId, ComponentsMovement.MOVED);
				}
			}

			backupDAO.delete(statement, backupId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromSuplierToAssembled 1");
			if(backupId>=0)
				try {
					backupDAO.retrieveComponents(statement, backupId);
				} catch (SQLException e1) {
					new ErrorDAO().saveError(e1, "ComponentsMovementDAO.moveFromSuplierToAssembled 2");
					e1.printStackTrace();
				} finally {	close(resultSet, statement, conecsion);	}
			getError().setErrorMessage("Operation can not be completed. <small>(E023)</small>");
			movementId = -1;
			throw new RuntimeException(e);
		} finally {	close(resultSet, statement, conecsion);	}

		return movementId;
	}

	public int moveFromVendor(ComponentsMovement componentsMovement, int userId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		int backupId = -1;
		BackupDAO backupDAO = new BackupDAO();
		ComponentsQuantity details = componentsMovement.getDetails();
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			if(details !=null && !details.isEmpty()){

				movementId = insertMovement(componentsMovement, statement);

				List<ComponentQuantity> componentQuantities = details.getComponentsQuantity();
			
				if(movementId>0){

					//backup
					backupId = backupDAO.creatBackup(statement, userId, "components", "qty", null, componentQuantities);

					for(ComponentQuantity ctm:componentQuantities){
						insertMovmentDetail(statement, movementId, ctm.getId(), ctm.getStockQuantity());
						addComponentsQuantity(statement, ctm.getId(), ctm.getStockQuantity());	//
					}

					updateMovemintStatus(statement, movementId, ComponentsMovement.MOVED);
				}
			}

			backupDAO.delete(statement, backupId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromVendor 1");
			if(backupId>=0)
				try {
					backupDAO.retrieveComponents(statement, backupId);
				} catch (SQLException e1) {
					new ErrorDAO().saveError(e1, "ComponentsMovementDAO.moveFromVendor 2");
					e1.printStackTrace();
				}
			getError().setErrorMessage("Operation can not be completed. <small>(E024)</small>");
			movementId = -1;
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}

	public static void backup(Statement statement,	ComponentsQty backup) throws SQLException {
		for(ComponentQuantity cq:backup.getComponentsQty())
				updateStockComponentsQuantity(statement, cq.getId(), cq.getStockQuantity());
	}

	public ComponentsMovement getComponentsMovement(int movementId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ComponentsMovement movement = null;
	
		String query = "SELECT`c`.*FROM`irt`.`movment_details`AS`m`" +
						"JOIN`irt`.`components`as`c`ON`c`.`id`=`m`.`id_components`" +
						"WHERE`m`.`id`="+movementId;
	
		List<Component> components = new ArrayList<>();
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);
	
			while(resultSet.next()){
				Data data = new PartNumberDetails(null).getComponent(resultSet.getString("part_number").substring(0, 3));
				data.setValue(resultSet);
				components.add((Component) data);
			}
	
			if(!components.isEmpty()){
				query = "select*from`irt`.`movment_details`as`m`" +
							"join`irt`.`user`as`u`on``u`.`id`=`who`" +
							"join`irt`.`array`as`a`on`a`.`id`=`m`.`from`or`a`.`id`=`m`.`to`" +
							"where`a`.`name`='from_to'and`m`.`id`="+movementId;
	
				resultSet = statement.executeQuery(query);
	
				if(resultSet.next())
					movement = new ComponentsMovement(resultSet);
		}
				
		} catch (SQLException | CloneNotSupportedException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.getComponentsMovement");
			throw new RuntimeException(e);
		} finally { close(resultSet, statement, conecsion); }
	
		return movement;
	}

	public Table getHistory() {

			String query = "SELECT	`m`.`date_time`AS`Date`," +
										"`u`.`firstname`AS`By`," +
										"`from`.`description`AS`From`," +
										"IF(`m`.`from`!=1,`from_d`.`Company`,'')AS`Detail`," +
										"`to`.`description`AS`To`," +
										"IF(`m`.`to`=4,`to_d`.`Company`,`irt`.part_number(`to_c`.`part_number`))AS`Detail`," +
										"`m`.`Description`" +
									"FROM`irt`.`movement`AS`m`" +
							"JOIN`irt`.`users`AS`u`ON`u`.`id`=`m`.`who`" +
							"JOIN`irt`.`arrays`AS`from`ON`from`.`id`=`m`.`from`" +
							"LEFT JOIN`irt`.`companies`AS`from_d`ON`from_d`.`id`=`m`.`from_detail`" +
							"JOIN`irt`.`arrays`AS`to`ON`to`.`id`=`m`.`to`" +
							"LEFT JOIN`irt`.`companies`AS`to_d`ON`to_d`.`id`=`m`.`to_detail`" +
							"LEFT JOIN`irt`.`components`AS`to_c`ON`to_c`.`id`=`m`.`to_detail`" +
							"WHERE`from`.`name`='from_to'AND`to`.`name`='from_to'AND`m`.`status`!=1";//status=1 - CLOSE

			Table table;
			if((table = getTable(query, null))!=null)
				table.setTitle(new HTMLHeader("History <input type=\"checkbox\" name=\"show_all\" id=\"sgow_all\" onclick=\"oneClick('submit')\" />" +
														"<label for=\"show_all\">show all</label>", "blue", 3));

		return table;
	}

	public Table getAllHistory() {

			String query = "(SELECT	 `m`.`id`," +
									"`m`.`date_time`AS`Date`," +
									"`u`.`firstname`AS`By`," +
									"`from`.`description`AS`From`," +
									"IF(`m`.`from`!=1,`from_d`.`Company`,'')AS`Detail`," +
									"`to`.`description`AS`To`," +
									"''," +
									"IF(`m`.`to`=4,`to_d`.`Company`,'')AS`Detail`," +
									"`m`.`Description`," +
									"''AS`Qty`" +
								"FROM`irt`.`movement`AS`m`" +
							"JOIN`irt`.`users`AS`u`ON`u`.`id`=`m`.`who`" +
							"JOIN`irt`.`arrays`AS`from`ON`from`.`id`=`m`.`from`AND`from`.`name`='from_to'" +
							"LEFT JOIN`irt`.`companies`AS`from_d`ON`from_d`.`id`=`m`.`from_detail`" +
							"JOIN`irt`.`arrays`AS`to`ON`to`.`id`=`m`.`to`AND`to`.`name`='from_to'" +
							"LEFT JOIN`irt`.`companies`AS`to_d`ON`to_d`.`id`=`m`.`to_detail`" +
							"LEFT JOIN`irt`.`components`AS`to_c`ON`to_c`.`id`=`m`.`to_detail`)" +

							"UNION" +

							"(SELECT `md`.`id_movement`," +
									"''," +
									"''," +
									"''," +
									"'*'," +
									"'*'," +
									"`c`.`id`," +
									"`irt`.part_number(`c`.`part_number`)," +
									"`c`.`description`," +
									"`md`.`Qty`" +
								"FROM`irt`.`movement_details`AS`md`" +
							"JOIN`irt`.`components`AS`c`ON`c`.`id`=`md`.`id_components`)" +

							"UNION" +

							"(SELECT `md`.`id_movement`," +
									"''," +
									"''," +
									"''," +
									"''," +
									"'-'," +
									"`md`.`id_components`," +
									"`irt`.part_number(`c`.`part_number`)," +
									"`c`.`description`," +
									"concat('-',`irt`.component_qty(`r`.`ref`)*`md`.`qty`)" +
								"FROM`irt`.`components`AS`c`" +
							"JOIN`irt`.`bom`ON`bom`.`id_components`=`c`.`id`" +
							"JOIN`irt`.`bom_ref`AS`r`ON`r`.`id`=`bom`.`id_bom_ref`" +
							"RIGHT JOIN`irt`.`movement_details`as`md`ON`md`.`id_components`=`bom`.`id_top_comp`" +
							"JOIN`irt`.`movement`AS`m`ON`m`.`id`=`md`.`id_movement`WHERE`m`.`to`=3)" +
							
							"ORDER BY 1,2 DESC,7,6";

			Table table;
			if((table = getTable(query, null))!=null){
				table.setTitle(new HTMLHeader("History <input type=\"checkbox\" name=\"show_all\" id=\"sgow_all\" checked=\"checked\" onclick=\"oneClick('submit')\" />" +
						"<label for=\"show_all\">show all</label>", "blue", 3));
				table.hideColumn(0);
				table.hideColumn(1);
				table.hideColumn(6);
				List<Row> rs = table.getRows();
				for(int i=1; i<rs.size(); i++){
					Row r = rs.get(i);
					if(!r.getRow().get(2).getValue().isEmpty())
						r.setClassName("titleBlue");
				}
		}

		return table;
	}

	public Table getOneComponentHistory(String historyOf) {

		String pn = TextWorker.pnValidation(historyOf);

			String query = "(SELECT	 `md`.`id_movement`AS`ID`," +
									"`m`.`date_time`AS`Date`," +
									"`u`.`firstName`AS`By`," +
									"`from`.`description`AS`From`," +
									"IF(`m`.`from`!=1,`from_d`.`Company`,'')AS`Detail`," +
									"`to`.`description`AS`To`," +
									"IF(`m`.`to`=4,`to_d`.`Company`,'')AS`Detail`," +
									"`m`.`Description`,`md`.`Qty`" +
								"FROM`irt`.`components`AS`to_c`" +
							"JOIN`irt`.`movement_details`AS`md`ON`md`.`id_components`=`to_c`.`id`" +
							"JOIN`irt`.`movement`AS`m`ON`m`.`id`=`md`.`id_movement`" +
							"JOIN`irt`.`users`AS`u`ON`u`.`id`=`m`.`who`" +
							"JOIN`irt`.`arrays`AS`from`ON`from`.`id`=`m`.`from`AND`from`.`name`='from_to'" +
							"LEFT JOIN`irt`.`companies`AS`from_d`ON`from_d`.`id`=`m`.`from_detail`" +
							"JOIN`irt`.`arrays`AS`to`ON`to`.`id`=`m`.`to`AND`to`.`name`='from_to'" +
							"LEFT JOIN`irt`.`companies`AS`to_d`ON`to_d`.`id`=`m`.`to_detail`" +
							"WHERE`to_c`.`part_number`='"+pn+"')" +

							"UNION(SELECT	 `md`.`id_movement`AS`ID`," +
											"`m`.`date_time`AS`Date`," +
											"`u`.`firstname`AS`By`," +
											"`from`.`description`AS`From`," +
											"IF(`m`.`from`!=1,`from_d`.`Company`,'')AS`Detail`," +
											"`to`.`description`AS`To`," +
											"`irt`.part_number(`cd`.`part_number`)AS`Detail`," +
											"`c`.`Description`,concat('-',`irt`.component_qty(`r`.`ref`)*`md`.`Qty`)" +
										"FROM`irt`.`components`AS`c`" +
									"JOIN`irt`.`bom`ON`bom`.`id_components`=`c`.`id`" +
									"JOIN`irt`.`bom_ref`AS`r`ON`r`.`id`=`bom`.`id_bom_ref`" +
									"RIGHT JOIN`irt`.`movement_details`as`md`ON`md`.`id_components`=`bom`.`id_top_comp`" +
									"JOIN`irt`.`movement`AS`m`ON`m`.`id`=`md`.`id_movement`" +
									"JOIN`irt`.`users`AS`u`ON`u`.`id`=`m`.`who`" +
									"JOIN`irt`.`arrays`AS`from`ON`from`.`id`=`m`.`from`AND`from`.`name`='from_to'" +
									"LEFT JOIN`irt`.`companies`AS`from_d`ON`from_d`.`id`=`m`.`from_detail`" +
									"JOIN`irt`.`arrays`AS`to`ON`to`.`id`=`m`.`to`AND`to`.`name`='from_to'" +
									"JOIN`irt`.`components`AS`cd`ON`cd`.`id`=`md`.`id_components`"+
									"WHERE`m`.`to`=3 AND`c`.`part_number`='"+pn+"')" +

									"ORDER BY 1 LIMIT 500";

//			irt.work.Error.setErrorMessage(query);

			Table table;
			if((table = getTable(query, null))!=null){
				table.setTitle(new HTMLHeader("History Of "+historyOf, "blue", 3));
				table.hideColumn(0);
				List<Row> rs = table.getRows();
				for(int i=1; i<rs.size(); i++){
					Row r = rs.get(i);
					if(!r.getRow().get(2).getValue().isEmpty())
						r.setClassName("titleBlue");
				}
						
		}

		return table;
	}

	private ComponentsQuantity getBomQty(ComponentsQuantity details, Statement statement) throws SQLException {

		ComponentQuantity componentQuantity = details.get(0);
		int id = componentQuantity.getId();
		String whereClause = "`id_top_comp`="+id;
		String ifClause = "IF(`id_top_comp`="+id+","+componentQuantity.getQuantityToMove()+",";

		for(int i=1; i<details.size(); i++){
			componentQuantity = details.get(i);
			id = componentQuantity.getId();
			whereClause += " OR`id_top_comp`="+id;
			ifClause += "IF(`id_top_comp`="+id+","+componentQuantity.getQuantityToMove()+",";
		}
		ifClause += "1)";
		if(details.size()>1)
			ifClause += ")";

		String query = "SELECT`id_components`as`id`,"+
								"SUM(`irt`.component_qty(`ref`)*"+ifClause+")as`Qty`" +
							"FROM`irt`.`bom_ref`as`r`" +
						"JOIN`irt`.`bom`on`id_bom_ref`=`r`.`id`" +
						"WHERE"+whereClause+
						" GROUP BY`id_components`";
//irt.work.Error.setErrorMessage(query);
		ResultSet resultSet = statement.executeQuery(query);

		ComponentsQuantity csq = new ComponentsQuantity();
		while(resultSet.next())
			csq.add(new ComponentQty(resultSet.getInt("id"), resultSet.getInt("Qty")));

		return csq;
	}

	private void addComponentsQuantity(Statement statement, int id, int qty) throws SQLException {
		String query = "UPDATE`irt`.`components`SET`qty`=if(`qty`IS NULL,"+qty+",`qty`+"+qty+")WHERE`id`="+id;
		statement.executeUpdate(query);
	}

	private void subtractComponentsQuantity(Statement statement, int id, int qty) throws SQLException {
		String query = "UPDATE`irt`.`components`SET`qty`=IF(`qty`>"+qty+",`qty`-"+qty+",0) WHERE `id`="+id;
		statement.executeUpdate(query);
	}

	public int moveFromKIT(ComponentsMovement componentsMovement){
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			int kitId = componentsMovement.getFromDetail().getId();
			if(componentsMovement.getFrom().getId()==Company.KIT && new KitDAO().hasComponents(kitId)){
				movementId = insertMovement(componentsMovement, statement);

				if(movementId>0 && componentsMovement.getFrom().getId()==Company.KIT){

					String query = "UPDATE`irt`.`companies_components`AS`c`" +
									"JOIN(SELECT`cc`.`id_components`," +
												"`kd`.`qty`" +
											"FROM`irt`.`kit_details`AS`kd`" +
										"JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`kd`.`id_components`" +
										"WHERE`id_kit`="+kitId+")AS`t`ON`t`.`id_components`=`c`.`id_components`" +
									"SET`c`.`qty`=`c`.`qty`+`t`.`qty`;";

					statement.executeUpdate(query);

					query = "INSERT INTO`irt`.`companies_components`(`id_companies`,`id_components`,`qty`)"+
							"SELECT "+componentsMovement.getToDetail().getId()+",`kd`.`id_components`,`kd`.`qty`"+
							"FROM`irt`.`kit_details`AS`kd`"+
							"LEFT JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`kd`.`id_components`"+
							"WHERE`id_kit`="+kitId+" AND`cc`.`id_components`IS NULL";

					statement.executeUpdate(query);

					KitDAO.setStatus(statement, kitId, KitDAO.MOVED);
					updateMovemintStatus(statement, movementId, ComponentsMovement.MOVED);
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromKIT");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}

	public int moveToBaulk(ComponentsMovement componentsMovement) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int movementId = -1;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			String tableName = null;
			String idField = null;
			switch(componentsMovement.getFrom().getId()){
			case Company.STOCK:
				tableName = "`irt`.`components`";
				idField = "`id`";
				break;
			case Company.TYPE_CM:
			case Company.TYPE_VENDOR:
				tableName = "`irt`.`companies_components`";
				idField = "`id_companies`="+componentsMovement.getFromDetail().getId()+" AND`id_components`";
				break;
			}

			if(tableName!=null){
				movementId = insertMovement(componentsMovement, statement);

				if(movementId>0){
					for(ComponentQuantity cq:componentsMovement.getDetails().getComponentsQuantity()){
						int quantityToMove = cq.getQuantityToMove();
						int componentId = cq.getId();
						String query = "UPDATE"+tableName+
										"SET`qty`=IF((`qty`>"+quantityToMove+"),`qty`-"+quantityToMove+",0)" +
										"WHERE"+idField+"="+componentId;
// irt.work.Error.setErrorMessage(query);
						if(statement.executeUpdate(query)==0)
							getError().setErrorMessage("0 row effected <small>(E025)</small>");
						insertMovmentDetail(statement, movementId, componentId, quantityToMove);
					}
				}else
					getError().setErrorMessage("<small>(E026)</small>");
			}else
				getError().setErrorMessage("<small>(E027)</small>");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveToBaulk");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return movementId;
	}

	public int getComponyComponentQuantity(int companyId, int componentId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		int qty = 0;

		String query = "SELECT`qty`FROM`irt`.`companies_components`WHERE`id_componies`="+companyId+" `id_components`="+componentId;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
// irt.work.Error.setErrorMessage(query);
			resultSet = statement.executeQuery(query);

			if(resultSet.next())
				qty = resultSet.getInt(1);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveToBaulk");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return qty;
	}

	public void insertMovementHistory(int userId, int from, int fromDetail, int to, int toDetail, String qtyDescription, int componentId, int oldQty, int qtyToMove) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
 
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			int movementId = insertMovementHistory(statement, userId, from, fromDetail, to, toDetail, qtyDescription);
			if(movementId>=0)
				insertMovemenyDetail(statement, movementId, componentId, oldQty, qtyToMove);
			setStatus(movementId, ComponentsMovement.MOVED);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.insertMovementHistory");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

	}

	private static int insertMovemenyDetail(Statement statement, int movementId, int componentId, int oldQty, int qtyToMove) throws SQLException {
		String query = "INSERT INTO`irt`.`movement_details`" +
										"(`id_movement`,`id_components`,`qty`,`old_qty`)" +
									"VALUES" +
										"("+movementId+","+componentId+","+qtyToMove+","+(oldQty<0 ? null : oldQty)+")";

 //irt.work.Error.setErrorMessage(query);
		return statement.executeUpdate(query);
	}

	private static int insertMovementHistory(Statement statement, int userId, int from, int fromDetail, int to, int toDetail, String qtyDescription) throws SQLException {
		String query = "INSERT INTO`irt`.`movement`" +
								"(`who`,`from`,`from_detail`,`to`,`to_detail`,`description`,`date_time`)" +
							"VALUES" +
								"("+userId+","+from+","+fromDetail+","+to+","+toDetail+",'"+qtyDescription+"', now())";

 //irt.work.Error.setErrorMessage(query);
		int movementId = -1;
		if(statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS)>0){
			ResultSet resultSet = statement.getGeneratedKeys();

			if(resultSet.next())
				movementId = resultSet.getInt(1);
		}
 
		return movementId;
	}
}