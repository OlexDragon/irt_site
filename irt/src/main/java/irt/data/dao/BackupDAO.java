package irt.data.dao;

import irt.data.components.movement.interfaces.ComponentQuantity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class BackupDAO extends DataAccessObject {

	public int creatBackup(Statement statement, int userId, String tableName, String columnName, String whereClause, List<ComponentQuantity> componentsQuantity) throws SQLException {
		ResultSet resultSet;
		int backupId = 0;

		String query = "SELECT DISTINCT`id`FROM`irt`.`backup`";
		try {
			resultSet = statement.executeQuery(query);

			if(resultSet.last())
				backupId = resultSet.getRow();

			String componentId = tableName.equals("components") ? "`id`=" : "`id_components`=";
			for(ComponentQuantity cq:componentsQuantity){
				query = "INSERT INTO`irt`.`backup`" +
						"(`id`,`id_components`,`qty`,`user`,`date`)" +

						"SELECT "+backupId+"," +
								cq.getId()+"," +
								"`"+columnName+"`AS`cn`,"+
								userId+","+
								"now()" +
							"FROM`irt`.`"+tableName+"`" +
						"WHERE"+(whereClause!=null ? whereClause+" AND" : "") +componentId+cq.getId()+
						" HAVING`cn`IS NOT NULL";
//				irt.work.Error.setErrorMessage(query);
				statement.executeUpdate(query);
			}
		} catch (SQLException e) {
			delete(statement, backupId);
			new ErrorDAO().saveError(e, "BackupDAO.creatBackup");
			throw new SQLException(e);
		}

		return backupId;
	}

	public void delete(Statement statement, int backupId) throws SQLException {
		String query = "DELETE FROM`irt`.`backup`WHERE`id`="+backupId;
//		irt.work.Error.setErrorMessage(query);
		statement.executeUpdate(query);
	}

	public void retrieveComponents(Statement statement, int backupId) throws SQLException {
		String query = "UPDATE`irt`.`components`AS`c`" +
						"JOIN`irt`.`backup`AS`b`ON`b`.`id_components`=`c`.`id`" +
						"SET`c`.`qty`=`b`.`qty`" +
						"WHERE`b`.`id`="+backupId;
		statement.executeUpdate(query);

		delete(statement, backupId);
	}

	public void retrieveKitDetails(Statement statement, int kitId, int backupId) throws SQLException {

		String query = "UPDATE`irt`.`kit_details`AS`kd`" +
						"LEFT JOIN`irt`.`backup`AS`b`ON`b`.`id_components`=`kd`.`id_components`AND`b`.`id`="+backupId+
						" SET`kd`.`qty`=IF(`b`.`qty`IS NULL, 0,`b`.`qty`)" +
						"WHERE`kd`.`id_kit`="+kitId;

		statement.executeUpdate(query);

		delete(statement, backupId);
	}
}
