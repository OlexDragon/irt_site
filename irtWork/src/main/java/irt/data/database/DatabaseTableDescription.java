package irt.data.database;


import irt.data.dao.ErrorDAO;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class DatabaseTableDescription {

	private String tableName;
	private String primaryKeys;
	private String uniqueIndex;
	private LinkedList<DatabaseFieldDescription>fieldsDescriptions = new LinkedList<>();
	private String databaseName;
	private BigInteger autoIncrement;

	public DatabaseTableDescription(String databaseName, String tableName, ResultSet resultSet, BigInteger bigInteger) {
		setName(tableName);
		this.databaseName =databaseName;
		this.autoIncrement = bigInteger;

		try {
			while (resultSet.next()) {
				fieldsDescriptions.add( new DatabaseFieldDescription(resultSet));
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DatabaseTableDescription.DatabaseTableDescription");
			throw new RuntimeException(e);
		}
		
		setPrimaryKeys();
		setUniqueKeys();
	}

	public String getName() {
		return tableName;
	}

	public void setName(String name) {
		tableName = name;
	}

	public LinkedList<DatabaseFieldDescription> getFieldsDescriptions() {
		return fieldsDescriptions;
	}

	public void setFieldsDescriptions( LinkedList<DatabaseFieldDescription> fieldsDescriptions) {
		this.fieldsDescriptions = fieldsDescriptions;
	}

	public String getPrimaryKeys() {
		return primaryKeys;
	}

	public void setUniqueKeys(){
		uniqueIndex = "";
		
		if(getFieldsDescriptions()!=null)
			for(DatabaseFieldDescription fieldDescription:getFieldsDescriptions()){
				if(fieldDescription.getKey().equalsIgnoreCase("UNI")){
					if (!uniqueIndex.isEmpty())
						uniqueIndex += ',';
					uniqueIndex += "UNIQUE INDEX `"+fieldDescription.getFieldName()+"_UNIQUE` (`"+fieldDescription.getFieldName()+"` ASC)";
				}
			}
	}

	public void setPrimaryKeys() {
		primaryKeys = "";

		if(getFieldsDescriptions()!=null)
			for(DatabaseFieldDescription fieldDescription:getFieldsDescriptions()){
				if(fieldDescription.getKey().equalsIgnoreCase("PRI")){
					if (!primaryKeys.isEmpty())
						primaryKeys += ',';
					primaryKeys += "`"+fieldDescription.getFieldName()+"`";
				}
			}
	}

	private String getFields() {
		String query = "";

		for (DatabaseFieldDescription fieldDescription : getFieldsDescriptions()) {
			if (!query.isEmpty())
				query += ',';
			query += fieldDescription;
		}

		if (!getPrimaryKeys().isEmpty())
			query += ",PRIMARY KEY (" + getPrimaryKeys() + ")";

		if(!getUniqueIndex().isEmpty())
			query += ","+getUniqueIndex();

		return query;
	}

	public String getUniqueIndex() {
		return uniqueIndex;
	}

	public void addComments(ResultSet resultSet) throws SQLException {
		String comment;

		while(resultSet.next()){
			comment = resultSet.getString("column_comment");
			if(comment!=null && !comment.isEmpty())
				setComment(resultSet.getString("column_name"), comment);
		}

	}

	private void setComment(String name, String comment) {
		DatabaseFieldDescription field = getFieldDescription(name);

		if(field!=null)
			field.setComment(comment);
	}

	private DatabaseFieldDescription getFieldDescription(String name) {
		DatabaseFieldDescription field = null;

		for(int i=0; i<fieldsDescriptions.size(); i++)
			if(fieldsDescriptions.get(i).getFieldName().equals(name)){
				field = fieldsDescriptions.get(i);
				break;
			}

		return field;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public boolean containsAutoIncrement(){

		boolean contains = false;

		for(DatabaseFieldDescription fd:fieldsDescriptions){
			String extra = fd.getExtra();
			contains =  extra!=null && extra.equals("auto_increment");
		}

		return contains;
	}

	@Override
	public String toString() {
		return "DROP TABLE IF EXISTS`"+databaseName+"`.`"+tableName+"`;"+
				"CREATE TABLE`"+databaseName+"`.`"+ tableName+"`("+ getFields() + ")" +
				(autoIncrement!=null ? "AUTO_INCREMENT="+autoIncrement : "")+
				";";
	}

	public BigInteger getAutoIncrement() {
		return autoIncrement;
	}
}