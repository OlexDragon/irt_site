package irt.data.dao;

import irt.data.database.DatabaseTableData;
import irt.data.database.DatabaseTableDescription;
import irt.data.database.InformationSchema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;


public class DatabaseDAO extends DataAccessObject {

	public void writeToFile(File file, String databaseName) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {

			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			LinkedList<DatabaseTableDescription>tablesDescriptions = new LinkedList<>();

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

			InformationSchema is = new InformationSchema(statement, databaseName);
//Create the tables
			for(String tableName:is.getTablesNames()){
				DatabaseTableDescription tableDescription = getTableDescription(statement, databaseName, tableName, is.getAutoIncrement(tableName));
				tablesDescriptions.add(tableDescription);
				bufferWriter.write(tableDescription.toString());
				bufferWriter.newLine();
			}

			for(DatabaseTableDescription tableDescription:tablesDescriptions){
				writeTable(statement, tableDescription, bufferWriter);
			}

			bufferWriter.close();

		} catch (IOException | SQLException e) {
			new ErrorDAO().saveError(e, "DatabaseDAO.writeToFile");
			throw new RuntimeException(e);
		}finally {
			close(resultSet, statement, conecsion);
		}
	}

	private void writeTable(Statement statement, DatabaseTableDescription tableDescription, BufferedWriter bufferWriter) throws SQLException, IOException {

		String query = "SELECT*FROM`"+tableDescription.getDatabaseName()+"`.`"+tableDescription.getName()+'`'; 

			ResultSet resultSet = statement.executeQuery(query);

			if(resultSet.next()){

				DatabaseTableData databaseTableData = new DatabaseTableData( resultSet, tableDescription);

				while(resultSet.next())
					databaseTableData.addValues(resultSet, tableDescription);

				bufferWriter.write(databaseTableData.toString());
				bufferWriter.newLine();
			}

	}

	private DatabaseTableDescription getTableDescription(Statement statement, String databaseName, String tableName, BigInteger bigInteger) throws SQLException {

		DatabaseTableDescription tableDescription = null;
		String query = "DESC`"+databaseName+"`.`"+tableName+"`";

		ResultSet resultSet = statement.executeQuery(query);

			tableDescription = new DatabaseTableDescription(databaseName, tableName, resultSet, bigInteger);

			query = "SELECT`column_name`,`column_comment`"+
						"FROM`information_schema`.`columns`"+
						"WHERE`table_name`='"+tableName+"'";

			resultSet = statement.executeQuery(query);

			tableDescription.addComments(resultSet);

		return tableDescription;
	}

	public LinkedList<String> getTablesNames(Statement statement, String databaseName) throws SQLException {

		LinkedList<String> tables = new LinkedList<>();
		String query = "SHOW TABLES FROM`"+databaseName+"`";

		ResultSet resultSet = statement.executeQuery(query);

		while (resultSet.next()) {
			tables.add(resultSet.getString(1));
		}


		return tables;
	}

}
