/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.naming.NamingException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 *
 * @author Oleksandr
 */
public class DataAccessObjectTest {

    private Logger logger = LogManager.getLogger();

    public DataAccessObjectTest() throws NamingException {
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

    /**
     * Test of getStringArray method, of class DataAccessObject.
     */
    @Test
    public void testGetStringArray() {
    	logger.trace("getStringArray");
        String query = "SELECT part_number FROM irt.components";
        logger.trace(query);
        DataAccessObject instance = new DataAccessObject();
        String[] result = instance.getStringArray(query);
        logger.trace("{}", (Object)result);
        assertNotNull(result);
    }

    /**
     * Test of getSQLObject method, of class DataAccessObject.
     */
    @Test
    public void testGetSQLObject() {
    	logger.trace("getSQLObject");
        String query = "SELECT part_number FROM irt.components";
        logger.trace(query);
       DataAccessObject instance = new DataAccessObject();
        Object result = instance.getSQLObject(query);
        logger.trace("{}", result);
        assertNotNull(result);
    }

    /**
     * Test of isResult method, of class DataAccessObject.
     */
    @Test
    public void testIsResult() {
    	logger.trace("isResult");
        String query = "SELECT part_number FROM irt.components";
        logger.trace(query);
        DataAccessObject instance = new DataAccessObject();
        boolean expResult = true;
        boolean result = instance.isResult(query);
        logger.trace("{}", result);
        assertEquals(expResult, result);
    }

    /**Description	Resource	Path	Location	Type
Target runtime Apache Tomcat v7.0 is not defined.	irtWork		Unknown	Faceted Project Problem

     * Test of getRowCount method, of class DataAccessObject.
     */
    @Test
    public void testGetRowCount() {
    	logger.trace("getRowCount");
        DataAccessObject instance = new DataAccessObject();
        Integer result = instance.getRowCount("irt", "components");
        logger.trace("{}", result);
        assertNotNull(result);
    }
}