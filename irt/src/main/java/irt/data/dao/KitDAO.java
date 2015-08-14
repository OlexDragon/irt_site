package irt.data.dao;

import irt.data.companies.Company;
import irt.data.components.movement.ComponentQty;
import irt.data.components.movement.ComponentToMove;
import irt.data.components.movement.ComponentsMovement;
import irt.data.components.movement.ComponentsQuantity;
import irt.data.components.movement.interfaces.ComponentQuantity;
import irt.data.purchase.PurchaseOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class KitDAO extends DataAccessObject {

	public static final int OPEN	= PurchaseOrder.OPEN;//Created new Kit
	public static final int READY	= 2;//Kit ready to send to CM
	public static final int MOVED 	= PurchaseOrder.COMPLETE;

	public boolean createNewKIT(int userId, String kitName) {
		boolean isCeated = false;
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT`id`FROM`irt`.`kit`WHERE`kit_name`='"+kitName+"'AND`status`!="+ComponentsMovement.SENT;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);
			if (!resultSet.next()) {
				query = "INSERT INTO`irt`.`kit`(`id_user`,`kit_name`,`date`)VALUES("+userId+",'"+kitName+"', NOW())";
				statement.executeUpdate(query);
				isCeated = true;
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.createNewKIT");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return isCeated;
	}


	public List<Company> getAllOpenKITs() {
		List<Company> kits = null;
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT*FROM`irt`.`kit`WHERE`status`!="+MOVED;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);
			if(resultSet.next()) {
				kits = new ArrayList<>();
				do
					kits.add(new Company(resultSet.getInt("id"), resultSet.getString("kit_name"), null, null, null, null, null, Company.KIT, resultSet.getInt("status")==ComponentsMovement.SENT));
				while(resultSet.next());
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.getAllOpenKITs");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return kits;
	}


	public boolean moveFromStock(ComponentsMovement cm) {

		boolean wasAdded = false;
		int kitId = cm!=null && cm.getTo()!=null && cm.getTo().getId()==Company.KIT && cm.getToDetail()!=null ? cm.getToDetail().getId() : 0;

		int userId = cm.getWho().getID();
		BackupDAO backupDAO = new BackupDAO();
		int componentsBackupId = 0;
		int kitBackupId = 0;
		if(kitId>0){
		try(Connection connection = getDataSource().getConnection();) {

			String description = cm.getDescription();
			if(description!=null && !description.isEmpty())
				setKITDescription(connection, kitId, description);

			//Create the Backups
			List<ComponentQuantity> csq = cm.getDetails().getComponentsQuantity();
			componentsBackupId = backupDAO.creatBackup(connection, userId, "components", "qty", null, csq);
			kitBackupId = backupDAO.creatBackup(connection, userId, "kit_details", "qty", "`id_kit`="+kitId, csq);

			try(	PreparedStatement statementQty = connection.prepareStatement("SELECT`qty`FROM`irt`.`components`WHERE`id`=?");
					PreparedStatement statementKitQty = connection.prepareStatement("SELECT`qty`FROM`irt`.`kit_details`WHERE`id_components`=? AND`id_kit`=?");
					PreparedStatement statementUpdateKit = connection.prepareStatement("UPDATE`irt`.`kit_details`SET `qty`=? WHERE`id_kit`=? AND`id_components`=?");
					PreparedStatement statementUpdate = connection.prepareStatement("UPDATE`irt`.`components`SET`qty`=? WHERE`id`=?");
					PreparedStatement statementInsert = connection.prepareStatement("INSERT INTO`irt`.`kit_details`(`id_users`,`id_kit`,`id_components`,`qty`)VALUES(?,?,?,?)");){
				for(ComponentQuantity cq:csq){
					int componentId = cq.getId();
					int toMove = cq.getQuantityToMove();
					int stockQty = 0;
					statementQty.setInt(1, componentId);

					try(ResultSet resultSetQty = statementKitQty.executeQuery();){
						if(resultSetQty.next() && (stockQty = resultSetQty.getInt(1))>0){	//is in Stock
							statementKitQty.setInt(1, componentId);
							statementKitQty.setInt(2, kitId);

							try(ResultSet resultSet = statementKitQty.executeQuery();){
								boolean doUpdate = true;
								if(resultSet.next()){
									int kitQty = resultSet.getInt(1);
									stockQty += kitQty;
									if(stockQty<toMove)
										toMove =stockQty;
									if(doUpdate = kitQty!=toMove){
										statementUpdateKit.setInt(1, toMove);
										statementUpdateKit.setInt(2, kitId);
										statementUpdateKit.setInt(3, componentId);
										statementUpdateKit.executeUpdate();
									}
					}else{					
						if(stockQty<toMove)
							toMove =stockQty;
						statementInsert.setInt(1, cm.getWho().getID());
						statementInsert.setInt(2, componentId);
						statementInsert.setInt(3, toMove);
						statementInsert.executeUpdate();
					}
					if(doUpdate){
						statementUpdate.setInt(1, stockQty-toMove);
						statementUpdate.setInt(2, stockQty-componentId);
						statementUpdate.executeUpdate();
					}
				}}}

			}}
			setKitStatus(connection, kitId, READY);
			wasAdded = true;
			backupDAO.delete(connection, kitBackupId);
			backupDAO.delete(connection, componentsBackupId);

		} catch (SQLException e) {
			try(Connection connection = getDataSource().getConnection();) {
				retrieve(connection, backupDAO, kitId, kitBackupId, componentsBackupId);
			} catch (SQLException e1) {
				new ErrorDAO().saveError(e1, "KitDAO.moveFromStock 1");
				throw new RuntimeException(e);
			}
			new ErrorDAO().saveError(e, "KitDAO.moveFromStock 2");
			throw new RuntimeException(e);
		}
		}
		return wasAdded;
	}

	private void setKitStatus(Connection connection, int kitId, int status) throws SQLException {
		String query = "UPDATE`irt`.`kit`SET`status`=? WHERE `id`=?";
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, status);
			statement.setInt(2, kitId);
			statement.executeUpdate(query);
		}
	}


	private void setKITDescription(Connection connection, int kitId, String description) throws SQLException {
		String query = "UPDATE`irt`.`kit`SET`description`=? WHERE`id`=?";
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setString(1, description);
			statement.setInt(2, kitId);
			statement.executeUpdate();
		}
	}

	public String getKitDescription(int kitId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String description = null;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			description = getKITDescription(statement, kitId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.getKitDescription");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return description;
	}

	private String getKITDescription(Statement statement, int kitId) throws SQLException {
		String query = "SELECT`description`FROM`irt`.`kit`WHERE`id`="+kitId;
//		irt.work.Error.setErrorMessage(query);
		ResultSet rs = statement.executeQuery(query);
		return rs.next() ? rs.getString(1) : null;
	}

	public ComponentsQuantity getComponentsQuantity(int kitId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		ComponentsQuantity componentsQuantity = null;
		
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			componentsQuantity = getComponentsQuantity(statement, kitId);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.getComponentsQuantity");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return componentsQuantity;
	}

	private ComponentsQuantity getComponentsQuantity(Statement statement, int kitId) throws SQLException {
		String query = "SELECT`id`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`manuf_part_number`," +
								"`description`," +
								"`location`," +
								"`qty`AS`StockQuantity`," +
								"`qty`AS`QuantityToMove`" +
						"FROM`irt`.`components`AS`c`" +
					"JOIN`irt`.`kit_details`AS`kd`ON`kd`.`id_components`=`c`.`id`" +
					"WHERE`id_kit`="+kitId;
		ResultSet resultSet = statement.executeQuery(query);

		ComponentsQuantity componentsQuantity = null;
		if(resultSet.next()){
			componentsQuantity = new ComponentsQuantity();
			do {
				int movingQty = resultSet.getInt("QuantityToMove");
				componentsQuantity.add(new ComponentToMove(resultSet.getInt("id"),
									resultSet.getString("Part Number"),
									resultSet.getString("manuf_part_number"),
									resultSet.getString("description"),
									resultSet.getInt("StockQuantity")+movingQty,
									movingQty,
									resultSet.getString("location")));
			} while(resultSet.next());
		}
		return componentsQuantity;
	}

	public void removeComponent(int kitId, int componentId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			ComponentQty cq = getComponentQuantity(statement, kitId, componentId);

			if(cq!=null){
				String query = "UPDATE `irt`.`components` SET `qty`=`qty`+"+cq.getQuantityToMove()+" WHERE `id`="+componentId;
				statement.executeUpdate(query);

				query = "DELETE FROM `irt`.`kit_details` WHERE `id_kit`="+kitId+" AND`id_components`="+componentId;
				executeUpdate(query);
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.removeComponent");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
	}


	private ComponentQty getComponentQuantity(Statement statement, int kitId, int componentId) throws SQLException {
		ComponentQty cq = null;
		String query = "SELECT`qty`FROM`irt`.`kit_details`WHERE`id_kit`="+kitId+" AND `id_components`="+componentId;
		ResultSet rs = statement.executeQuery(query);

		if(rs.next())
			cq = new ComponentQty(componentId, rs.getInt("qty"));

		return cq;
	}

	public boolean hasComponents(int kitId) {

		String query = "SELECT*FROM `irt`.`kit_details` WHERE `id_kit`="+kitId+" LIMIT 1";
		return isResult(query);
	}

	public boolean hasKits() {

		String query = "SELECT*FROM`irt`.`kit_details`" +
						"JOIN`irt`.`kit`ON`id`=`id_kit`" +
						"WHERE`status`=2 LIMIT 1";
		return isResult(query);
	}


	public void setStatus(int kitId, int status) {
		try(Connection connection = getDataSource().getConnection();) {	
			setStatus(connection, kitId, status);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "KitDAO.setStatus");
			throw new RuntimeException(e);
		}
	}


	public static void setStatus(Connection connection, int kitId, int status)throws SQLException {
		String query = "UPDATE `irt`.`kit` SET `status`=? WHERE `id`=?";
		try(PreparedStatement statement = connection.prepareStatement(query)){
			statement.setInt(1, status);
			statement.setInt(2, kitId);
			statement.executeUpdate(query);
		}
	}


	public boolean addToKIT(ComponentsMovement cm) {

		boolean wasAdded = false;
		int kitId = cm!=null && cm.getTo()!=null && cm.getTo().getId()==Company.KIT && cm.getToDetail()!=null ? cm.getToDetail().getId() : 0;

		int userId = cm.getWho().getID();
		BackupDAO backupDAO = new BackupDAO();
		int componentsBackupId = 0;
		int kitBackupId = 0;
		if(kitId>0){
		try(Connection connection = getDataSource().getConnection();) {

			//Create the Backups
			List<ComponentQuantity> csq = cm.getDetails().getComponentsQuantity();
			componentsBackupId = backupDAO.creatBackup(connection, userId, "components", "qty", null, csq);
			kitBackupId = backupDAO.creatBackup(connection, userId, "kit_details", "qty", "`id_kit`="+kitId, csq);

			try(	PreparedStatement statementComponents = connection.prepareStatement("SELECT`qty`FROM`irt`.`components`WHERE`id`=?");
					PreparedStatement statementKitDetails = connection.prepareStatement("SELECT`qty`FROM`irt`.`kit_details`WHERE`id_components`=?");
					PreparedStatement statementInsert = connection.prepareStatement("INSERT INTO`irt`.`kit_details`(`id_users`,`id_kit`,`id_components`,`qty`)VALUES(?,?,?,?)");
					PreparedStatement statementUpdateKit = connection.prepareStatement("UPDATE `irt`.`kit_details` SET `qty`=`qty`+? WHERE `id_kit`=? AND`id_components`=?");
					PreparedStatement statementUpdateComponents = connection.prepareStatement("UPDATE`irt`.`components`SET`qty`=? WHERE`id`=?");){

				PreparedStatement statement;

				for(ComponentQuantity cq:cm.getDetails().getComponentsQuantity()){
					int componentId = cq.getId();
					int toMove = cq.getQuantityToMove();
					int stockQty = 0;
				
					statementComponents.setInt(1, componentId);
					try(ResultSet resultSet = statementComponents.executeQuery();){
						if(resultSet.next() && (stockQty = resultSet.getInt(1))>0){	//is in Stock
							if(stockQty<toMove)
								toMove =stockQty;
						statementKitDetails.setInt(1, componentId);
						try(ResultSet resultSetKitDetails = statementKitDetails.executeQuery();){
							if(resultSetKitDetails.next()){
								statementUpdateKit.setInt(1, toMove);
								statementUpdateKit.setInt(2, cm.getToDetail().getId());
								statementUpdateKit.setInt(3, componentId);
								statement = statementUpdateKit;
							}else{
								statementInsert.setInt(1, cm.getWho().getID());
								statementInsert.setInt(2, kitId);
								statementInsert.setInt(3, componentId);
								statementInsert.setInt(4, toMove);
								statement = statementInsert;
							}
						}

						statement.executeUpdate();

						statementUpdateComponents.setInt(1, stockQty-toMove);
						statementUpdateComponents.setInt(1, componentId);
						statementUpdateComponents.executeUpdate();
						}
					}

				}
			}
			wasAdded = true;
			backupDAO.delete(connection, kitBackupId);
			backupDAO.delete(connection, componentsBackupId);

		} catch (SQLException e) {
			try(Connection connection = getDataSource().getConnection();) {
				retrieve(connection, backupDAO, kitId, kitBackupId, componentsBackupId);
			} catch (SQLException e1) {
				new ErrorDAO().saveError(e1, "KitDAO.addToKIT");
				throw new RuntimeException(e);
			}
			new ErrorDAO().saveError(e, "KitDAO.addToKIT");
			throw new RuntimeException(e);
		}
		}
		return wasAdded;
	}


	public void retrieve(Connection connection, BackupDAO backupDAO, int kitId,
			int kitBackupId, int componentsBackupId) throws SQLException {
			if(kitBackupId>0)
				backupDAO.retrieveKitDetails(connection, kitId, kitBackupId);
			if(componentsBackupId>0)
				backupDAO.retrieveComponents(connection, componentsBackupId);
	}


	public int getQuantity(int kitId, int componentId) {

		String query = "SELECT`qty`FROM`irt`.`kit_details`WHERE`id_kit`="+kitId+" AND`id_components`="+componentId;
		Object obj = getSQLObject(query);
		return obj!=null ? (int)obj : 0;
	}
}
