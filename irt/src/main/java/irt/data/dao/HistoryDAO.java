package irt.data.dao;

import irt.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;


public class HistoryDAO extends DataAccessObject{

	public static final int INSERT = 1;
	public static final int UPDATED = 2;

	/**
	 * use when update table
	 */
	public boolean setHistory(Statement statement, int userId, String tableName, int rowId, String columnName, String newValue) throws SQLException {

		ResultSet resultSet = insertHistory(statement, userId, tableName, rowId, columnName, newValue, UPDATED);

			boolean don = false;
			if(resultSet.next()){
				long hisroryId = resultSet.getLong(1);
				don = insertHistoryDetail(statement, hisroryId, tableName, rowId, columnName, newValue);
			}else
				getError().setErrorMessage(columnName+" - history did not saved. <small>(E028)</small>.");

			return don;
	}

	private boolean insertHistoryDetail(Statement statement, long hisroryId,
			String tableName, int rowId, String columnName, String newValue)
			throws SQLException {
		boolean don;
		String query = "INSERT INTO`irt`.`history_details`" +
						"(`id_history`,`column_name`,`old_value`,`new_value`)" +
					"SELECT "+hisroryId+",'"+columnName+"',"+columnName+", " +(newValue!=null ? "'"+newValue+"'" : null) +
						" FROM "+tableName+
						" WHERE `id`="+rowId;
//irt.work.Error.setErrorMessage(query);
		don = statement.executeUpdate(query)>0;
		return don;
	}

	public boolean setHistory(Statement statement, int userId, String tableName, int rowId, Map<String, String> fields) throws SQLException {

		long hisroryId = -1;
		boolean don = false;

		for(Entry<String, String> f:fields.entrySet()){
			if(hisroryId<0){
				ResultSet resultSet = insertHistory(statement, userId, tableName, rowId, f.getKey(), f.getValue(), UPDATED);

				if(resultSet.next()){
					hisroryId = resultSet.getLong(1);
					don = insertHistoryDetail(statement, hisroryId, tableName, rowId, f.getKey(), f.getValue()) || don;
				}else
					fields.remove(f);
			}else{
				if(!equals(statement, tableName, rowId, f.getKey(), f.getValue()))
					don = insertHistoryDetail(statement, hisroryId, tableName, rowId, f.getKey(), f.getValue()) || don;
				else
					fields.remove(f);
			}
		}
		return don;
	}

	private boolean equals(Statement statement, String tableName, int rowId, String columnName, String newValue) throws SQLException {
		String query = "SELECT 'equals' FROM "+tableName+" WHERE `id`="+rowId+" AND "+columnName+"='"+newValue+"'";
//irt.work.Error.setErrorMessage(query);
		return statement.executeQuery(query).next();
	}

	public void insert(Statement statement, int userId, String tableName, String componentId) throws SQLException {
		insertHistory(statement, userId, tableName, componentId, INSERT);
	}

	private ResultSet insertHistory(Statement statement, int userId, String tableName, int rowId, String columnName, String newValue, int operation) throws SQLException {
		String query = "INSERT INTO`irt`.`history`" +
								"(`date_time`,`id_users`, `table_name`,`row_id`,`operation`)" +
							"SELECT " +
									"now(),"+userId+",'"+tableName+"',"+rowId+","+operation+
							" FROM "+tableName+
							" WHERE `id`="+rowId+" AND "+(newValue!=null ? "("+columnName+"!='"+newValue+"'OR "+columnName+" IS NULL)" : columnName+" IS NOT NULL");
//irt.work.Error.setErrorMessage(query);
		 statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
		return resultSet;
	}

	private void insertHistory(Statement statement, int userId, String tableName, String componentId, int operation) throws SQLException {
		String query = "INSERT INTO`irt`.`history`" +
				"(`date_time`,`id_users`, `table_name`,`row_id`,`operation`)" +
			"SELECT " +
					"now(),"+userId+",'"+tableName+"','"+componentId+"',"+operation;
//irt.work.Error.setErrorMessage(query);
		statement.executeUpdate(query);
	}

	public Table getHistoryTable(int limit){
		String query = "SELECT date_format(`date_time`,'%M %D %Y')AS`Date`," +
								"`u`.`firstName`AS`By`," +
								"`a`.`description`AS`did`," +
								"`hd`.`column_name`AS``," +
								"`hd`.`old_value`AS`old`," +
								"if(`h`.`table_name`='`irt`.`components`',`hd`.`new_value`," +
									"if(`h`.`table_name`='`irt`.`components_alternative`',(SELECT`alt_mfr_part_number`FROM`irt`.`components_alternative`WHERE`id`=`h`.`row_id`)," +
										"if(`h`.`table_name`='`irt`.`manufacture`',(SELECT`name`FROM`irt`.`manufacture`WHERE`id`=`h`.`row_id`),`h`.`row_id`)))AS`new`," +
								"if(`c`.`part_number`IS NULL,NULL,concat('<a href=\"/irt/part-numbers?pn=',`part_number`,'\">',`irt`.part_number(`part_number`),'</a>'))AS`PartNumber`," +
								"if(`c`.`id`IS NULL,`h`.`table_name`,`c`.`Description`)AS`Description`" +
							"FROM`irt`.`history`AS`h`" +
						"LEFT JOIN`irt`.`history_details`AS`hd`ON`hd`.`id_history`=`h`.`id`" +
						"JOIN`irt`.`users`AS`u`ON`u`.`id`=`h`.`id_users`" +
						"JOIN`irt`.`arrays`AS`a`ON`a`.`name`='history_oper'AND`a`.`id`=`h`.`operation`" +
						"LEFT JOIN`irt`.`components`AS`c`ON`c`.`id`=`h`.`row_id`AND`h`.`table_name`='`irt`.`components`'" +
						"ORDER BY `h`.`id`DESC  " +
						"LIMIT "+limit;
		return getTable(query, null);
	}
}
 