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

	public FirstDigit get(char id) {
		return get(""+id);
	}

	public FirstDigit get(String id) {
		//		logger.debug("get(String description); description = " + description);

		String query = "SELECT*FROM`irt`.`first_digit`WHERE`id`='"+id+"'";
		try(Connection conecsion = getDataSource().getConnection();
			PreparedStatement statement = conecsion.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();) {

			if (!resultSet.next()) {
				return null;
			}
			return new FirstDigit(resultSet);
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "FirstDigitDAO.get");
			throw new RuntimeException(e);
		}
	}

	public List<FirstDigit> getAll() {

		LinkedList<FirstDigit> firstDigitList = new LinkedList<FirstDigit>();

		String query = "SELECT*FROM`IRT`.`first_digit`ORDER BY`description`";
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while (resultSet.next()) {
				firstDigitList.add(new FirstDigit(resultSet));
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "FirstDigitDAO.getAll");
			throw new RuntimeException(e);
		}

		return firstDigitList;
	}
	
}
