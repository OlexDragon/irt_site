package irt.data.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTableData {

	String query;
	private DatabaseTableDescription tableDescription;

	public DatabaseTableData(ResultSet resultSet,	DatabaseTableDescription tableDescription) throws SQLException{

		this.tableDescription = tableDescription;
		String tableName = tableDescription.getName();
		query = "INSERT INTO`"+tableDescription.getDatabaseName()+"`.`"+tableName+"`";

		String fieldsValues = "";

		for(DatabaseFieldDescription fieldDescription:tableDescription.getFieldsDescriptions()){

			if(fieldsValues.isEmpty()){
				query +='(';
				fieldsValues += '(';
			}else{
				fieldsValues += ',';
				query += ',';
			}

			query += "`"+fieldDescription.getFieldName()+"`";

				String value = resultSet.getString(fieldDescription.getFieldName());
				fieldsValues += (fieldDescription.getType().substring(0,3).equalsIgnoreCase("int") || value==null)
									? value
									: '\''+value.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"")+'\'';
		}

		query += ")VALUES"+fieldsValues+")";
	}

	public void addValues(ResultSet resultSet, DatabaseTableDescription tableDescription) throws SQLException {

		String fieldsValues = "";

		for(DatabaseFieldDescription fieldDescription:tableDescription.getFieldsDescriptions()){
			if(fieldsValues.isEmpty()){
				fieldsValues += ",(";
			}else{
				fieldsValues += ',';
			}
			String value = resultSet.getString(fieldDescription.getFieldName());
			fieldsValues += (fieldDescription.getType().substring(0,3).equalsIgnoreCase("int") || value==null)
					? value
					: '\''+value.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"")+'\'';
		}
		query += fieldsValues+")";
	}


	@Override
	public String toString() {

		String databaseName = tableDescription.getDatabaseName();
		String tableName = tableDescription.getName();
		String primaryKeys = tableDescription.getPrimaryKeys();

		return "LOCK TABLES`"+databaseName+"`.`"+tableName+"`WRITE;" +
				(primaryKeys!=null ? "/*!40000 ALTER TABLE`"+databaseName+"`.`"+tableName+"`DISABLE KEYS */;" : "") +
				query+";" +
				(primaryKeys!=null ? "/*!40000 ALTER TABLE`"+databaseName+"`.`"+tableName+"`ENABLE KEYS */;" : "") +
				"UNLOCK TABLES;";
	}
}