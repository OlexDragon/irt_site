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
import irt.data.dao.ComponentDAO.Action;
import irt.data.partnumber.PartNumberDetails;
import irt.table.HTMLHeader;
import irt.table.Row;
import irt.table.Table;
import irt.work.TextWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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

	public int moveComponents(ComponentsMovement componentsMovement, Action option) {
		int movementId = -1;

		try(	Connection connection = getDataSource().getConnection();) {
			
			movementId = insertMovement(componentsMovement, connection);
			
			if(movementId>0)
					moveComponents(componentsMovement, connection, movementId, option);
			else
				getError().setErrorMessage("Error. <small>(E022)</small>");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.createComponentsMovement");
			throw new RuntimeException(e);
		}

		return movementId;
	}
	private int insertMovement(ComponentsMovement componentsMovement, Connection connection) throws SQLException {
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

		int movementId = -1;
		try(Statement statement = connection.createStatement()){

			statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);

			try(ResultSet resultSet = statement.getGeneratedKeys();){

				if(resultSet.next())
					movementId = resultSet.getInt(1);
			}
		}
		return movementId;
	}

	private void moveComponents(ComponentsMovement componentsMovement, Connection connection, int movementId, Action option) throws SQLException {
		for(ComponentQuantity cq:componentsMovement.getDetails().getComponentsQuantity()){
	
			int componentId = cq.getId();
			int qty = 0;
			Company companyDetail = null;
			switch(option){
			case QTY_ADD:
				qty = cq.getQuantityToMove();
				break;
			case QTY_SUBTACT:
				qty = -cq.getQuantityToMove();
				break;
			default:
				logger.warn("Action not specified");
				return;
			}
			updateStockComponentsQuantity(connection, componentId, qty);
			updateCompaniesComponents(connection, companyDetail!=null ? companyDetail.getId() : 0, componentId, -qty);
			insertMovemenyDetail(connection, movementId, componentId, cq.getStockQuantity(), Math.abs(qty));

		}
		updateMovemintStatus(connection, movementId, ComponentsMovement.MOVED);
	}

	private static void updateStockComponentsQuantity(Connection connection,int id, int qty)	throws SQLException {
		String query = "UPDATE`irt`.`components`SET`qty`=if(((qty/-1)-?)<0, qty+?, 0)WHERE id = ?;";
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, qty);
			statement.setInt(2, qty);
			statement.setInt(3, id);
			statement.executeUpdate();
		}
	}

	private void updateCompaniesComponents(Connection connection, int companyId, int componentId, int quantityToMove) throws SQLException {
		String query = "SELECT`qty`FROM`irt`.`companies_components`WHERE`id_companies`=? AND`id_components`=?";


		boolean update = false;
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, companyId);
			statement.setInt(2, componentId);
			try(ResultSet resultSet = statement.executeQuery();){
				if(resultSet.next()){
					query = "UPDATE`irt`.`companies_components`SET`qty`=if(((qty/-1)-?)<0, qty+?, 0)WHERE`id_components`=? AND`id_companies`=?";
					update = true;
				}else
					if(quantityToMove<0)
						return;
					else
						query = "INSERT INTO`irt`.`companies_components`(`qty`,`id_components`,`id_companies`)VALUES(?,?,?)";
			}
		}
		logger.trace("\n\t"
				+ "{}\n\t"
				+ "1) quantityToMove\t{}\n\t"
				+ "2) componentId\t{}\n\t"
				+ "3) companyId\t{}",
				query,
				quantityToMove,
				componentId,
				companyId);
		try(PreparedStatement statement = connection.prepareStatement(query)){
			int i = 1;
			statement.setInt(i, quantityToMove);
			if(update) statement.setInt(++i, quantityToMove);
			statement.setInt(++i, componentId);
			statement.setInt(++i, companyId);
			statement.executeUpdate();
		}
	}

	private void updateCompaniesComponent(Connection connection, int topComponentId, int companyId, int quantityToMove) throws SQLException {

		String sql = "SELECT`bom`.`id_components`AS`id`," +
								"?*`irt`.component_qty(`ref`)AS`qty`," +
								"`cc`.`qty`AS`old_qty`" +
							"FROM`irt`.`bom`" +
						"JOIN`irt`.`bom_ref`ON`bom_ref`.`id`=`bom`.`id_bom_ref`" +
						"LEFT JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`bom`.`id_components`AND`cc`.`id_companies`=? WHERE`id_top_comp`=?";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, quantityToMove);
			statement.setInt(2, companyId);
			statement.setInt(3, topComponentId);
			sql = "UPDATE`irt`.`companies_components`SET`qty`=IF(`qty`<=?,0,`qty`-?)WHERE`id_components`=? AND`id_companies`=?";
			try(	ResultSet resultSet = statement.executeQuery();
					PreparedStatement statement2 = connection.prepareStatement(sql)){
				statement2.setInt(4, companyId);
				while(resultSet.next()){
					String oldQtyStr = resultSet.getString("old_qty");
					if(oldQtyStr!=null && !oldQtyStr.equals("0")) {
						int qty = resultSet.getInt("qty");
						statement2.setInt(1, qty);
						statement2.setInt(2, qty);
						statement2.setInt(3, resultSet.getInt("id"));
						statement2.executeUpdate();
					}
				}
			}
		}
	}

	private void updateMovemintStatus(Connection connection, int movementId, int status)throws SQLException {
		String sql = "UPDATE`irt`.`movement`SET`status`=? WHERE`id`=?";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, status);
			statement.setInt(2, movementId);
			statement.executeUpdate();
		}
	}

	private void insertMovmentDetail(Connection connection, int movementId, int componentId, int quantityToMove) throws SQLException {

		String sql = "INSERT INTO`irt`.`movement_details`(`id_movement`,`id_components`,`qty`)" +
					"VALUES(?,?,?);";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, movementId);
			statement.setInt(2, componentId);
			statement.setInt(3, quantityToMove);
			statement.executeUpdate();
		}
	}

	public int moveFromSuplierToAssembled( ComponentsMovement componentsMovement) {
		int movementId = -1;

		try(Connection connection = getDataSource().getConnection();) {
			ComponentsQuantity details = componentsMovement.getDetails();
			if(details!=null && !details.isEmpty()){
				movementId = insertMovement(componentsMovement, connection);
			
				if(movementId>0){

					for(ComponentQuantity ctm:details.getComponentsQuantity()){
						int componentId = ctm.getId();
						int quantityToMove = ctm.getQuantityToMove();
						insertMovmentDetail(connection, movementId, componentId, quantityToMove);
						addComponentsQuantity(connection, componentId, quantityToMove);
						updateCompaniesComponent(connection, componentId, componentsMovement.getFromDetail().getId(), quantityToMove);
					}

					updateMovemintStatus(connection, movementId, ComponentsMovement.MOVED);
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromSuplierToAssembled");
			throw new RuntimeException(e);
		} 

		return movementId;
	}

	public int moveFromStockToAssembled(ComponentsMovement componentsMovement, int userId) {
		int movementId = -1;

		int backupId=-1;
		BackupDAO backupDAO = new BackupDAO();
		try(Connection connection = getDataSource().getConnection();) {
			
			ComponentsQuantity details = componentsMovement.getDetails();
			if(details!=null && !details.isEmpty()){
				movementId = insertMovement(componentsMovement, connection);
			
				if(movementId>0){
					ComponentsQuantity csq = getBomQty(details, connection);

					//backup
					List<ComponentQuantity> componentsQuantity = csq.getComponentsQuantity();
					backupId = backupDAO.creatBackup(connection, userId, "components", "qty", null, componentsQuantity);

					for(ComponentQuantity cq:componentsQuantity){
						subtractComponentsQuantity(connection, cq.getId(), cq.getQuantityToMove());
					}

					for(ComponentQuantity ctm:details.getComponentsQuantity()){
						insertMovmentDetail(connection, movementId, ctm.getId(), ctm.getQuantityToMove());
						addComponentsQuantity(connection, ctm.getId(), ctm.getQuantityToMove());	//
					}

					updateMovemintStatus(connection, movementId, ComponentsMovement.MOVED);
				}
			}

			backupDAO.delete(connection, backupId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromSuplierToAssembled 1");
			if(backupId>=0)
				try(Connection connection = getDataSource().getConnection();) {
					backupDAO.retrieveComponents(connection, backupId);
				} catch (SQLException e1) {
					new ErrorDAO().saveError(e1, "ComponentsMovementDAO.moveFromSuplierToAssembled 2");
					e1.printStackTrace();
				}
			getError().setErrorMessage("Operation can not be completed. <small>(E023)</small>");
			movementId = -1;
			throw new RuntimeException(e);
		}

		return movementId;
	}

	public int moveFromVendor(ComponentsMovement componentsMovement, int userId) {
		int movementId = -1;

		int backupId = -1;
		BackupDAO backupDAO = new BackupDAO();
		ComponentsQuantity details = componentsMovement.getDetails();
		try(Connection connection = getDataSource().getConnection();) {
			if(details !=null && !details.isEmpty()){

				movementId = insertMovement(componentsMovement, connection);

				List<ComponentQuantity> componentQuantities = details.getComponentsQuantity();
			
				if(movementId>0){

					//backup
					backupId = backupDAO.creatBackup(connection, userId, "components", "qty", null, componentQuantities);

					for(ComponentQuantity ctm:componentQuantities){
						insertMovmentDetail(connection, movementId, ctm.getId(), ctm.getStockQuantity());
						addComponentsQuantity(connection, ctm.getId(), ctm.getStockQuantity());	//
					}

					updateMovemintStatus(connection, movementId, ComponentsMovement.MOVED);
				}
			}

			backupDAO.delete(connection, backupId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromVendor 1");
			if(backupId>=0)
				try(Connection connection = getDataSource().getConnection();) {
					backupDAO.retrieveComponents(connection, backupId);
				} catch (SQLException e1) {
					new ErrorDAO().saveError(e1, "ComponentsMovementDAO.moveFromVendor 2");
					e1.printStackTrace();
				}
			getError().setErrorMessage("Operation can not be completed. <small>(E024)</small>");
			movementId = -1;
			throw new RuntimeException(e);
		}

		return movementId;
	}

	public static void backup(Connection connection,	ComponentsQty backup) throws SQLException {
		for(ComponentQuantity cq:backup.getComponentsQty())
				updateStockComponentsQuantity(connection, cq.getId(), cq.getStockQuantity());
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
				data.setValues(resultSet);
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
							"WHERE`from`.`name`='from_to'AND`to`.`name`='from_to'AND`m`.`status`!=1"//status=1 - CLOSE
							+ " ORDER BY `Date`DESC LIMIT 500";

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
							
							"ORDER BY 1 DESC,2 DESC,7,6 LIMIT 500";

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

	private ComponentsQuantity getBomQty(ComponentsQuantity details, Connection connection) throws SQLException {

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

		ComponentsQuantity csq = new ComponentsQuantity();
		try(Statement statement = connection.createStatement()){
			ResultSet resultSet = statement.executeQuery(query);

			while(resultSet.next())
				csq.add(new ComponentQty(resultSet.getInt("id"), resultSet.getInt("Qty")));
		}
		return csq;
	}

	private void addComponentsQuantity(Connection connection, int id, int qty) throws SQLException {
		String sql = "UPDATE`irt`.`components`SET`qty`=if(`qty`IS NULL,?,`qty`+?)WHERE`id`=?";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, qty);
			statement.setInt(2, qty);
			statement.setInt(3, id);
			statement.executeUpdate();
		}
	}

	private void subtractComponentsQuantity(Connection connection, int id, int qty) throws SQLException {
		String sql = "UPDATE`irt`.`components`SET`qty`=IF(`qty`>?,`qty`-?,0)WHERE`id`=?";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, qty);
			statement.setInt(2, qty);
			statement.setInt(3, id);
			statement.executeUpdate();
		}
	}

	public int moveFromKIT(ComponentsMovement componentsMovement){
		int movementId = -1;

		try(Connection connection = getDataSource().getConnection();) {

			int kitId = componentsMovement.getFromDetail().getId();
			if(componentsMovement.getFrom().getId()==Company.KIT && new KitDAO().hasComponents(kitId)){
				movementId = insertMovement(componentsMovement, connection);

				if(movementId>0 && componentsMovement.getFrom().getId()==Company.KIT){

					String query = "UPDATE`irt`.`companies_components`AS`c`" +
									"JOIN(SELECT`cc`.`id_components`," +
												"`kd`.`qty`" +
											"FROM`irt`.`kit_details`AS`kd`" +
										"JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`kd`.`id_components`" +
										"WHERE`id_kit`=?)AS`t`ON`t`.`id_components`=`c`.`id_components`" +
									"SET`c`.`qty`=`c`.`qty`+`t`.`qty`;";

					try(PreparedStatement statement = connection.prepareStatement(query)){
						statement.setInt(1, kitId);
						statement.executeUpdate(query);
					}

					query = "INSERT INTO`irt`.`companies_components`(`id_companies`,`id_components`,`qty`)"+
							"SELECT "+componentsMovement.getToDetail().getId()+",`kd`.`id_components`,`kd`.`qty`"+
							"FROM`irt`.`kit_details`AS`kd`"+
							"LEFT JOIN`irt`.`companies_components`AS`cc`ON`cc`.`id_components`=`kd`.`id_components`"+
							"WHERE`id_kit`=? AND`cc`.`id_components`IS NULL";

					try(PreparedStatement statement = connection.prepareStatement(query)){
						statement.setInt(1, kitId);
						statement.executeUpdate(query);
					}

					KitDAO.setStatus(connection, kitId, KitDAO.MOVED);
					updateMovemintStatus(connection, movementId, ComponentsMovement.MOVED);
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveFromKIT");
			throw new RuntimeException(e);
		} 
		return movementId;
	}

	public int moveToBaulk(ComponentsMovement componentsMovement) {
		int movementId = -1;

		try(Connection connection = getDataSource().getConnection();) {

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
				movementId = insertMovement(componentsMovement, connection);

				if(movementId>0){
					for(ComponentQuantity cq:componentsMovement.getDetails().getComponentsQuantity()){
						int quantityToMove = cq.getQuantityToMove();
						int componentId = cq.getId();

						try(PreparedStatement statement = connection.prepareStatement("UPDATE "+tableName+" SET`qty`=IF((`qty`>?),`qty`-?,0)WHERE"+idField+"=?")){
							statement.setInt(1, quantityToMove);
							statement.setInt(2, quantityToMove);
							statement.setInt(3, componentId);
						if(statement.executeUpdate()==0)
							getError().setErrorMessage("0 row effected <small>(E025)</small>");
						}
						insertMovmentDetail(connection, movementId, componentId, quantityToMove);
					}
				}else
					getError().setErrorMessage("<small>(E026)</small>");
			}else
				getError().setErrorMessage("<small>(E027)</small>");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveToBaulk");
			throw new RuntimeException(e);
		}

		return movementId;
	}

	public int getComponyComponentQuantity(int companyId, int componentId) {
		logger.entry(companyId, componentId);
		int qty = 0;

		String query = "SELECT`qty`FROM`irt`.`companies_components`WHERE`id_companies`=? AND `id_components`=?;";
		logger.debug("\n\t{}\n\t{}\n\t{}", companyId, componentId, query);

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);) {

			statement.setInt(1, companyId);
			statement.setInt(2, componentId);

			try(ResultSet resultSet = statement.executeQuery();){
				if(resultSet.next())
					qty = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.moveToBaulk");
			throw new RuntimeException(e);
		}

		return qty;
	}

	public void insertMovementHistory(int userId, int from, int fromDetail, int to, int toDetail, String qtyDescription, int componentId, int oldQty, int qtyToMove) {
 
		try(	Connection conecsion = getDataSource().getConnection();){

			int movementId = insertMovementHistory(conecsion, userId, from, fromDetail, to, toDetail, qtyDescription);
			if(movementId>=0)
				insertMovemenyDetail(conecsion, movementId, componentId, oldQty, qtyToMove);
			setStatus(movementId, ComponentsMovement.MOVED);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ComponentsMovementDAO.insertMovementHistory");
			throw new RuntimeException(e);
		}

	}

	private static int insertMovemenyDetail(Connection connection, int movementId, int componentId, int oldQty, int qtyToMove) throws SQLException {
		String query = "INSERT INTO`irt`.`movement_details`" +
										"(`id_movement`,`id_components`,`qty`,`old_qty`)" +
									"VALUES" +
										"(?,?,?,?)";

		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, movementId);
			statement.setInt(2, componentId);
			statement.setInt(3, qtyToMove);
			if(oldQty<0)
				statement.setNull(4, Types.NULL);
			else
				statement.setInt(4, oldQty);
			return statement.executeUpdate();
		}
	}

	private static int insertMovementHistory(Connection connection, int userId, int from, int fromDetail, int to, int toDetail, String qtyDescription) throws SQLException {
		String query = "INSERT INTO`irt`.`movement`" +
								"(`who`,`from`,`from_detail`,`to`,`to_detail`,`description`,`date_time`)" +
							"VALUES" +
								"(?,?,?,?,?,?, now())";

		int movementId = -1;
		try(PreparedStatement statement = connection.prepareStatement(query)){
			if(statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS)>0){

				statement.setInt(1, userId);
				statement.setInt(2, from);
				statement.setInt(3, fromDetail);
				statement.setInt(4, to);
				statement.setInt(5, toDetail);
				statement.setString(6, qtyDescription);
				try(ResultSet resultSet = statement.getGeneratedKeys();){

					if(resultSet.next())
						movementId = resultSet.getInt(1);
		}}}
 
		return movementId;
	}
}