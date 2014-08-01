package irt.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorDAO extends DataAccessObject {

    private Logger logger = LogManager.getLogger();

    public boolean saveError(Exception e, String errorNumber) {
    	logger.entry(e, errorNumber);

        boolean isAdded = false;
//        String stackTrace = getError().getStackTrace(e);
//            if (stackTrace.length() > 3072) {
//                stackTrace = stackTrace.substring(0, 3069) + "...";
//            }
//        String query = "INSERT INTO `irt`.`errors` (`date`, `errorCod`, `error`) VALUES (NOW(),'" + errorNumber + "', \"" + stackTrace + "\")";
//
//        try (Connection connection = getDataSource().getConnection();
//                Statement statement = connection.createStatement()) {
//
//            isAdded = statement.executeUpdate(query) != 0;
//
//        } catch (SQLException ex) {
//            logger.catching(ex);
//            throw new RuntimeException(ex);
//        }

        return logger.exit(isAdded);
    }

    public String getLastError() {

    	String error = null;
        String sql = "SELECT*FROM irt.errors ORDER BY date DESC LIMIT 1;";
        logger.trace(sql);

        try(    Connection connection = getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()){

            if(resultSet.next())
                error = resultSet.getString("date")+" : "+resultSet.getString("error");
            
        } catch (SQLException ex) {
            logger.catching(ex);
        }
        return logger.exit(error);
    }
}
