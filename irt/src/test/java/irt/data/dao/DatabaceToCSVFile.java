package irt.data.dao;

import static org.junit.Assert.*;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class DatabaceToCSVFile {

    private Logger logger = LogManager.getLogger();
	private ComponentDAO componentDAO;

	public DatabaceToCSVFile() {
    	logger.trace("setUp");
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("irt");
        dataSource.setPassword("vsat");
        dataSource.setUrl("jdbc:mysql://localhost:3306");
        dataSource.setMaxActive(10);
        dataSource.setMaxIdle(5);
        dataSource.setInitialSize(5);
        DataAccessObject.setDataSource(dataSource);

        componentDAO = new ComponentDAO();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
