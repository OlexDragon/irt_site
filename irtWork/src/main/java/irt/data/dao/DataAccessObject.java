package irt.data.dao;

import irt.data.Error;
import irt.table.Row;
import irt.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class DataAccessObject {

    protected final Logger logger = (Logger) LogManager.getLogger();

	private static DataSource dataSource;
	private Error error = new Error();

	public Error getError() {
		return error;
	}

	protected static void close(ResultSet resultSet, Statement statement, Connection connection) {
		try {
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.close");
			throw new RuntimeException(e);
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		DataAccessObject.dataSource = dataSource;
	}

	public String[] getColumnNames(String tableName) {

		String query = "SELECT`COLUMN_NAME`from`INFORMATION_SCHEMA`.`COLUMNS`WHERE`TABLE_NAME`='"+tableName+"'";	
		logger.trace(query);
		return getStringArray(query);
	}

	protected Table getTable(String query, String href) {
		logger.entry(query, href);
		Table table = null;

		try(Connection connecsion = getDataSource().getConnection();
				Statement statement = connecsion.createStatement();
				ResultSet resultSet = statement.executeQuery(query)){

			table = getTable(resultSet, href);
			logger.trace(table);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.close");
			throw new RuntimeException(e);
		}
		return logger.exit(table);
	}

	protected Table getTable(ResultSet resultSet, String href) throws SQLException {
		logger.entry(resultSet, href);
		Table table = null;

			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] columnNames = new String[metaData.getColumnCount()];

			if(columnNames.length!=0){
				for (int i = 0; i < columnNames.length; i++){
					String title = metaData.getColumnLabel(i+1);
					if(href!=null)
						columnNames[i] = "<a href=\""+href+"?ob="+title+"\">"+title+"</a>";
					else
						columnNames[i] = title;
				}

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
				else{
					table.setClassName("border");
				}
				logger.trace(table);
			}

		return logger.exit(table);
	}

	public String[] getStringArray(String query) {
		logger.entry(query);
		String[] results = null;
//		irt.work.Error.setErrorMessage(query);

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if (resultSet.last()){
				int index = resultSet.getRow();
				if(index>0){
					results = new String[index];
					do
						results[--index] = resultSet.getString(1);
					while(resultSet.previous());
				}
			}

			logger.trace("{}", (Object)results);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.getStringArray");
			throw new RuntimeException(e);
		}
		return logger.exit(results);
	}

	public int executeUpdate(String query) {
		logger.entry(query);
		int isExecuted = 0;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement()) {

			isExecuted = statement.executeUpdate(query);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.executeUpdate");
			throw new RuntimeException(e);
		}

		return logger.exit(isExecuted);
	}

	public int insert(String query) {
		logger.entry(query);
		int id = 0;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement()) {
			if(statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS)>0){

				try(ResultSet resultSet = statement.getGeneratedKeys()){
					if(resultSet.next())
						id = resultSet.getInt(1);
				}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.executeUpdate");
			throw new RuntimeException(e);
		}

		return logger.exit(id);
	}

	public Object getSQLObject(String query) {
		logger.entry(query);

		Object object = null;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if (resultSet.next()) {
				object = resultSet.getObject(1);
				logger.trace("'{}'", object);
			}else
				logger.trace("statement.executeQuery({}) = null", query);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.getSQLObject");
			throw new RuntimeException(e);
		}

		return logger.exit(object);
	}

	public boolean isResult(String query) {
		logger.entry(query);

		boolean isExists = false;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			isExists = resultSet.next();

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.isResult");
			throw new RuntimeException(e);
		}

		return logger.exit(isExists);
	}

	public Integer getRowCount(String dataBase, String tableName) {
		logger.entry(dataBase, tableName);

		Integer rowCount = null;
		String query = "SELECT COUNT(*)FROM "+dataBase+"."+tableName;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			if(resultSet.next())
				rowCount = resultSet.getInt(1);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.getRowCount");
			throw new RuntimeException(e);
		}

		return logger.exit(rowCount);
	}
}
