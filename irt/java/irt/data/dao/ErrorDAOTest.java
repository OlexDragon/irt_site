/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package irt.data.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 *
 * @author Oleksandr
 */
public class ErrorDAOTest {

    private Logger logger = LogManager.getLogger();

    public ErrorDAOTest() {
    	logger.trace("setUp");
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("oleksandr");
        dataSource.setPassword("dragon");
        dataSource.setUrl("jdbc:mysql://irttechnologies:3306");
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(5);
        dataSource.setInitialSize(5);
        DataAccessObject.setDataSource(dataSource);
    }

    @Test
    public void testLastError() {
    	logger.trace("getLastError");
        final ErrorDAO errorDAO = new ErrorDAO();
        logger.trace(errorDAO.getLastError());
    }
    
}
