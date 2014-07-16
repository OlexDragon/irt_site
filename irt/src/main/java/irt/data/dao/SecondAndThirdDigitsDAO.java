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
//
//	public SecondAndThirdDigits get(int firstDigitId) {
//
//		String query = "SELECT*FROM`irt`.`first_digits`WHERE id=?";
//		try(	Connection conecsion = getDataSource().getConnection();
//				PreparedStatement statement = conecsion.prepareStatement(query);) {
//
//			statement.setInt(1, firstDigitId);
//
//			try(ResultSet resultSet = statement.executeQuery()){
//
//				if (resultSet.next())
//					return new SecondAndThirdDigits(resultSet);
//				else
//					return null;
//			}
//
//		} catch (SQLException e) {
//			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.SecondAndThirdDigits");
//			throw new RuntimeException(e);
//		}
//	}
//
//	public List<SecondAndThirdDigits> getAll() {
//
//		LinkedList<SecondAndThirdDigits> newsItems = new LinkedList<SecondAndThirdDigits>();
//
//		String query = "SELECT*FROM`IRT`.`first_digits`ORDER BY`description`";
//		try(	Connection conecsion = getDataSource().getConnection();
//				PreparedStatement statement = conecsion.prepareStatement(query);
//				ResultSet resultSet = statement.executeQuery();) {
//
//			while (resultSet.next())
//				newsItems.add(new SecondAndThirdDigits(resultSet));
//
//			return newsItems;
//		} catch (SQLException e) {
//			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getAll");
//			throw new RuntimeException(e);
//		}
//	}

	public List<SecondAndThirdDigits> getRequired(char selectedFirstDigit){
		logger.entry(selectedFirstDigit);

		LinkedList<SecondAndThirdDigits> newsItems = new LinkedList<>();

		String query = "SELECT"
							+ "`f`.*,"
							+ "`s`.`id`,"
							+ "`s`.`description`AS`s_description`,"
							+ "`s`.`class_id`"
						+ "FROM`irt`.`first_digits`AS`f`"
					+ "JOIN `irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`"
					+ "WHERE`part_numbet_first_char`=? ORDER BY `s_description`";

		logger.trace("Query={}, '?'={}", query, selectedFirstDigit);

		try (	Connection conecsion = getDataSource().getConnection();
					PreparedStatement statement = conecsion.prepareStatement(query);) {

				statement.setString(1, ""+selectedFirstDigit);

				try(ResultSet resultSet = statement.executeQuery()){
					while (resultSet.next()){
						FirstDigit firstDigit = new FirstDigit(resultSet.getInt("id_first_digits"), resultSet.getString("part_numbet_first_char").charAt(0), resultSet.getString("description"));
						newsItems.add(new SecondAndThirdDigits(resultSet.getString("id"), firstDigit, resultSet.getString("s_description"), resultSet.getInt("class_id")));
					}
				}

			} catch (SQLException e) {
				// new ErrorDAO().saveError(e,
				// "SecondAndThirdDigitsDAO.getRequired");
				throw new RuntimeException(e);
			}
		logger.trace("EXIT WITH: {}", newsItems);
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

		String query = "SELECT	concat(`part_numbet_first_char`,`id`)"
							+ "FROM`irt`.`first_digits`AS`f`"
						+ "JOIN `irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`"
						+ "WHERE`class_id`="+classid;
// irt.work.Error.setErrorMessage(query);
		return (String) getSQLObject(query);
	}

	public int getClassID(String classId) {

		Object sqlObject = null;
		if (classId != null && classId.length() == 3){
				String query = "SELECT`class_id`FROM`irt`.`second_and_third_digit`WHERE `id_first_digits`='"+ classId.charAt(0)+"'AND`id`='"+classId.substring(1)+"'";
				sqlObject = getSQLObject(query);
		}
//		irt.work.Error.setErrorMessage("getClassID: "+sqlObject);
		return (int) (sqlObject!=null ? (Long) sqlObject :-1);
	}

	public String[] getClassIDs(int countID) {

		String query = "SELECT concat(`s`.`id_first_digits`,`s`.`id`)" +
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
				menu.add(resultSet.getInt("class_id"), resultSet.getString("id_first_digits")+resultSet.getString("id"));

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
		String query = "SELECT	`class_id`,"
							+ "concat(`part_numbet_first_char`,`id`)"
						+ "FROM`irt`.`first_digits`AS`f`"
					+ "JOIN `irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				m.put(resultSet.getInt(1), resultSet.getString(2));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getMapIdClass");
			throw new RuntimeException(e);
		}

		return m;
	}

	public Map<String, Integer> getMapClassId() {

		Map<String, Integer> m = new HashMap<>();
		String query = "SELECT	`class_id`,"
							+ "concat(`part_numbet_first_char`,`id`)"
						+ "FROM`irt`.`first_digits`AS`f`"
					+ "JOIN `irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				m.put(resultSet.getString(2), resultSet.getInt(1));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "SecondAndThirdDigitsDAO.getMapClassId");
			throw new RuntimeException(e);
		}

		return m;
	}
}
