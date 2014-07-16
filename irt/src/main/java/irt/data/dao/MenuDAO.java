package irt.data.dao;

import irt.data.Menu;
import irt.data.ValueText;
import irt.data.companies.Place;
import irt.work.ComboBoxField;
import irt.work.TextWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MenuDAO extends DataAccessObject {

	public enum OrderBy{
		DESCRIPTION	("`description`"),
		SEQUENCE	("`sequence`"	);

		private String orderBy;

		private OrderBy(String orderBy){
			this.orderBy = orderBy;
		}

		@Override
		public String toString(){
			return orderBy;
		}
	}

	public Menu getMenu(String[] menuNames, OrderBy orderBy) {
		String query = null;

		if(menuNames!=null && menuNames.length!=0){
			String whereClause = "`name`='"+menuNames[0]+"'";

			for(int i=1; i<menuNames.length; i++)
				whereClause += "OR`name`='"+menuNames[i]+"'";

			query = "SELECT`id`,`description`FROM`irt`.`arrays`WHERE"+whereClause+(orderBy!=null ? "ORDER BY"+orderBy : "");
		}
		return getMenu(query);
	}

	public Menu getMenu(String menuName, OrderBy orderBy) {
		logger.entry(menuName, orderBy);
		String query = null;

		if(menuName!=null && !menuName.isEmpty())
			query = "SELECT`id`,`description`FROM`irt`.`arrays`WHERE`name`='"+ menuName+"'";
		if(orderBy!=null)
			query += "ORDER BY"+orderBy;

		logger.trace("\n\tQuery:\t{}", query);

		return logger.exit(getMenu(query));
	}

	private Menu getMenu(String query) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Menu menu = null;

		if (query != null)
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.prepareStatement(query);
				resultSet = statement.executeQuery();

				menu = new Menu(resultSet);

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "MenuDAO.getMenu");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}

		return menu;
	}

	public boolean isBomValid(String partNumber) {
		boolean isValid = false;

		if (partNumber != null && partNumber.length()>3){
				String query = "select * from `irt`.`arrays` where name='bom' and id='"
						+ partNumber.substring(0,3) + "'";
		isValid = isResult(query);
		}
		return isValid;
	}

	public List<Place> getPlaces() {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Place> places = null;

		try {
			String query = "SELECT`id`,`description`FROM`irt`.`arrays`WHERE`name`='from_to'ORDER BY`description`";
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			places = new ArrayList<>();

			while(resultSet.next()){
				String str = resultSet.getString("id").replaceAll("[^\\d]","");
				if(!str.isEmpty())
					places.add(new Place(Integer.parseInt(str),resultSet.getString("description")));
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "MenuDAO.getPlaces");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return places;
	}

	public Place getPlace(int id) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Place place = null;

		try {
			String query = "select`description`from`irt`.`arrays`where`name`='from_to'and`id`="+id+" order by`description`";
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if(resultSet.next()){
				place = new Place(id,resultSet.getString(1));
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "MenuDAO.getPlaces 2");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return place;
	}

	public String getDescription(String name, String id) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "select`description`from`irt`.`arrays`where`name`='"+name+"'and`id`='"+id+"'";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if(resultSet.next()){
				query = resultSet.getString(1);
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "MenuDAO.getDescription");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return query;
	}

	public boolean shawExel(String classId) {

		String query = "select*from`irt`.`arrays`where`name`='shaw_exel'and`id`='"+classId+"'";
		return isResult(query);
	}

	public Menu getTopComponentsMenu(String arraysTableName) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT lpad(`c`.`id`,5,'0')AS`id`," +
								"`irt`.part_number(`c`.`part_number`)AS`description`" +
							"FROM`irt`.`components`AS`c`" +
						"JOIN`irt`.`arrays`AS`a`ON`c`.`part_number`LIKE concat(`a`.`id`,'%')" +
						"WHERE`a`.`name`='"+arraysTableName+"'" +
						"ORDER BY`id`";
		Menu menu;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			menu = new Menu(resultSet);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return menu;
	}

	public Menu getAssembliedOptions(String partNumberStr) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		partNumberStr = TextWorker.pnValidation(partNumberStr);
		if(partNumberStr.length()>8)
			partNumberStr = partNumberStr.substring(0,8)+'%';

		String query = "SELECT DISTINCT substr(`part_number`,9,1)AS`id`," +
										"substr(`part_number`,9,1)AS`description`" +
								"FROM`irt`.`components`" +
						"WHERE`part_number`LIKE'"+partNumberStr+"'" +
						"ORDER BY`id`";

		Menu menu;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			menu = new Menu(resultSet);
			int index = menu.getKeys().length;
			String ch;
			if(index<9)
				ch = ""+(++index);
			else
				ch = ""+(char)(56+index);//(char)(56+9) = 'A'

			menu.add(ch,ch);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "MenuDAO.getAssembliedOptions");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return menu;
	}

	public ComboBoxField[] getComboBoxFields(String name, OrderBy orderBy){
		logger.entry(name, orderBy);

		ComboBoxField[] cbFields = null;

		String query = "SELECT`id`,`description`FROM`irt`.`arrays`WHERE`name`=?";

		if(orderBy!=null)
			query += " ORDER BY"+orderBy;

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);) {

			statement.setString(1, name);

			try(ResultSet resultSet = statement.executeQuery()){

				if (resultSet.last()){
					int index = resultSet.getRow();
					logger.debug("result Length: {}, query: {}, ? = {}", index, query, name);
					cbFields = new ValueText[index];
					cbFields[--index] = new ValueText(resultSet.getString("id"), resultSet.getString("description"));
					while(resultSet.previous())
						cbFields[--index] = new ValueText(resultSet.getString("id"), resultSet.getString("description"));					
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "MenuDAO.getComboBoxFields");
			throw new RuntimeException(e);
		}
		
	return logger.exit(cbFields);
	}
}
