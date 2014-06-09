package irt.data.dao;

import irt.data.Link;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LinkDAO extends DataAccessObject {

	public Link getLink(int id) {
		if(id==-1)
			return null;

		Link link = null;

		String query = "select * from `irt`.`links` where `id`="+id;
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {
			

			if (resultSet.next())
				link = new Link(resultSet);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "LinkDAO.getLink");
			throw new RuntimeException(e);
		} 
		
	return link;
	}

	public int getId(String filePath) {

		int id = -1;

		String query = "select * from `irt`.`links` where `link`like'%"+filePath+"'";
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			if (resultSet.next())
				id = new Link(resultSet).getId();

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "LinkDAO.getId");
			throw new RuntimeException(e);
		}

		return id;
	}
	
	public int add(String filePath) {
		logger.entry(filePath);

		int id = -1;

		if (isExists(filePath))
			id = getId(filePath);
		else {
			String query = "insert into`irt`.`links`(`id`,`link`)VALUES(?,?)";

			try (	Connection conecsion = getDataSource().getConnection();
					PreparedStatement statement = conecsion.prepareStatement(query);) {

				id = getNewId();

				logger.trace("Query:\n{}; {}, {}", query, id, filePath);

				statement.setLong	(1, id		);
				statement.setString	(2, filePath);
				statement.executeUpdate();

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "LinkDAO.add");
				throw new RuntimeException(e);
			}
		}
		return id;
	}

	public int add(Link link) {
		return add(link.getLink());
	}

	private int getNewId() {

		int newId = 0;

		String query = "select count(*) from `links`";
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			if (resultSet.next()) {
				newId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "LinkDAO.getNewId");
			throw new RuntimeException(e);
		}
		return newId;
	}

	public boolean isExists(String filePath) {

		String query = "SELECT`id`FROM`irt`.`links`WHERE`link`='" + filePath + "'";
		query = "select count(*) from (" + query + ") as t";
		Object obj = getSQLObject(query);

		return obj!=null && (long)obj>0 ;
	}
}
