package irt.data.dao;

import irt.data.FirstDigit;
import irt.data.Menu;
import irt.data.SecondAndThirdDigits;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SecondAndThirdDigitsDAO extends DataAccessObject {

//	private Logger logger = Logger.getLogger(this.getClass());

	public SecondAndThirdDigits get(String id, char firstDigitId) {

		String query = "SELECT*FROM`IRT`.`first_digit`WHERE id=" + id
					+ " and id_first=" + firstDigitId;
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			if (!resultSet.next())
				return null;

			return new SecondAndThirdDigits(resultSet);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.SecondAndThirdDigits");
			throw new RuntimeException(e);
		}
	}

	public List<SecondAndThirdDigits> getAll() {

		LinkedList<SecondAndThirdDigits> newsItems = new LinkedList<SecondAndThirdDigits>();

		String query = "SELECT*FROM`IRT`.`first_digit`ORDER BY`description`";
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while (resultSet.next())
				newsItems.add(new SecondAndThirdDigits(resultSet));

			return newsItems;
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getAll");
			throw new RuntimeException(e);
		}
	}

	public List<SecondAndThirdDigits> getRequired(char selectedStr) {

		LinkedList<SecondAndThirdDigits> newsItems = null;

		FirstDigit firstDigit = new FirstDigitDAO().get(selectedStr);
		if (firstDigit != null) {
			newsItems = new LinkedList<SecondAndThirdDigits>();

			String query = "SELECT*FROM`IRT`.`second_and_third_digit`WHERE`id_first`=\'"
					+ firstDigit.getId() + "\' order by description";
			// irt.work.Error.setErrorMessage(query);
			try (	Connection conecsion = getDataSource().getConnection();
					PreparedStatement statement = conecsion.prepareStatement(query);
					ResultSet resultSet = statement.executeQuery();) {

				while (resultSet.next())
					newsItems.add(new SecondAndThirdDigits(resultSet));

			} catch (SQLException e) {
				// new ErrorDAO().saveError(e,
				// "SecondAndThirdDigitsDAO.getRequired");
				throw new RuntimeException(e);
			}
		}
		return newsItems;
	}

	public String getDescription(String componentId){

		String query = "SELECT`description`FROM`irt`.`second_and_third_digit`WHERE`id`='"
				+ componentId + "'";
		return (String) getSQLObject(query);
	}

	public String getId(String descriptionSecond) {

		String query = "SELECT`id`FROM`irt`.`second_and_third_digit`WHERE`description`='"
				+ descriptionSecond + "'";
		return (String) getSQLObject(query);
	}

	public String getClassID(int classid) {

		String query = "SELECT concat(`id_first`,`id`)FROM`irt`.`second_and_third_digit`WHERE`class_id`="+classid;
// irt.work.Error.setErrorMessage(query);
		return (String) getSQLObject(query);
	}

	public int getClassID(String classId) {

		Object sqlObject = null;
		if (classId != null && classId.length() == 3){
				String query = "SELECT`class_id`FROM`irt`.`second_and_third_digit`WHERE `id_first`='"+ classId.charAt(0)+"'AND`id`='"+classId.substring(1)+"'";
				sqlObject = getSQLObject(query);
		}
//		irt.work.Error.setErrorMessage("getClassID: "+sqlObject);
		return (int) (sqlObject!=null ? (Long) sqlObject :-1);
	}

	public String[] getClassIDs(int countID) {

		String query = "SELECT concat(`s`.`id_first`,`s`.`id`)" +
						"FROM`irt`.`counts`AS`c`" +
						"JOIN`irt`.`second_and_third_digit`AS`s`ON`s`.`class_id`=`c`.`class_id`" +
						"WHERE`c`.`id`="+ countID;
		return getStringArray(query);
	}

	public Menu getClassIDsMenue() {

		Menu menu = new Menu();

		String query = "SELECT*FROM`irt`.`second_and_third_digit`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				menu.add(resultSet.getInt("class_id"), resultSet.getString("id_first")+resultSet.getString("id"));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getClassIDsMenue");
			throw new RuntimeException(e);
		}

		return menu;
}

	public String getClassDescription(int classId) {

		String query = "SELECT`description`FROM`irt`.`second_and_third_digit`WHERE`class_id`="+classId;
		return (String) getSQLObject(query);
	}

	public Map<Integer, String> getMapIdClass() {

		Map<Integer, String> m = new HashMap<>();
		String query = "SELECT`class_id`,concat(id_first,id)FROM`irt`.`second_and_third_digit`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				m.put(resultSet.getInt(1), resultSet.getString(2));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getClassIDsMenue");
			throw new RuntimeException(e);
		}

		return m;
	}

	public Map<String, Integer> getMapClassId() {

		Map<String, Integer> m = new HashMap<>();
		String query = "SELECT`class_id`,concat(id_first,id)FROM`irt`.`second_and_third_digit`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				m.put(resultSet.getString(2), resultSet.getInt(1));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getClassIDsMenue");
			throw new RuntimeException(e);
		}

		return m;
	}
}
