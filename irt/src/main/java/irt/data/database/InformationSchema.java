package irt.data.database;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InformationSchema {

	Map<String,BigInteger> tablesMap = new HashMap<>();

	public InformationSchema(Statement statement, String databaseName) throws SQLException {
		String query = "SELECT`TABLE_NAME`,`AUTO_INCREMENT`FROM`INFORMATION_SCHEMA`.`TABLES`WHERE `table_schema` ='"+databaseName+"'";
		ResultSet resultSet = statement.executeQuery(query);

		while(resultSet.next()){
			tablesMap.put(resultSet.getString("TABLE_NAME"), (BigInteger) resultSet.getObject("AUTO_INCREMENT"));
		}
	}

	public Set<String> getTablesNames() {
		return tablesMap.keySet();
	}

	public BigInteger getAutoIncrement(String tableName) {
		return tablesMap.get(tableName);
	}

}
