package irt.data.dao;

import irt.data.FirstDigit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FirstDigitDAO extends DataAccessObject {

//	private Logger logger = Logger.getLogger(this.getClass());

	public FirstDigit get(int id) {
		//		logger.debug("get(String description); description = " + description);
		return getFirstDigit("SELECT*FROM`irt`.`first_digits`WHERE`id_first_digits`="+id);
	}

	public FirstDigit get(char partNumberFirstChar) {
		return getFirstDigit("SELECT*FROM`irt`.`first_digits`WHERE`part_numbet_first_char`='"+partNumberFirstChar+"'");
	}

	private FirstDigit getFirstDigit(String query) {
		try(Connection conecsion = getDataSource().getConnection();
			PreparedStatement statement = conecsion.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();) {

			if (!resultSet.next()) {
				return null;
			}
			return new FirstDigit(resultSet.getInt("id_first_digits"), resultSet.getString("part_numbet_first_char").charAt(0), resultSet.getString("description"));
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "FirstDigitDAO.getFirstDigit");
			throw new RuntimeException(e);
		}
	}

	public List<FirstDigit> getAll() {

		LinkedList<FirstDigit> firstDigitList = new LinkedList<FirstDigit>();

		String query = "SELECT*FROM`IRT`.`first_digits`ORDER BY`description`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while (resultSet.next()) {
				firstDigitList.add(new FirstDigit(resultSet.getInt("id_first_digits"), resultSet.getString("part_numbet_first_char").charAt(0), resultSet.getString("description")));
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "FirstDigitDAO.getAll");
			throw new RuntimeException(e);
		}

		return firstDigitList;
	}
	
}
