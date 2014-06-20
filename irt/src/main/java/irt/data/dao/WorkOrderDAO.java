package irt.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import irt.work.TextWorker;

public class WorkOrderDAO extends DataAccessObject {

	public static String getNewWorkOrderName(String prefix){

		String yearMonth = TextWorker.getYearMonth();
		String query ="SELECT count(*)FROM`work_orders`WHERE`work_order_name`LIKE ?";

		int count;
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)){

			statement.setString(1, "__"+yearMonth+'%');

			try(ResultSet resultSet = statement.executeQuery()){
				if(resultSet.next())
					count = resultSet.getInt(1);
				else
					count = 0;

				count++;
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "WorkOrderDAO.getNewWorkOrderName");
			throw new RuntimeException(e);
		}

		return prefix+yearMonth+TextWorker.addZeroInFront(""+count, 3);
	}
}
