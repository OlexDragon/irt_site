package irt.data.dao;

import static org.junit.Assert.*;

import java.util.Arrays;

import irt.data.components.Component;
import irt.data.workorder.WorkOrder;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ComponentDAOTest {

    private Logger logger = LogManager.getLogger();
	private ComponentDAO componentDAO;

	public ComponentDAOTest() {
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

		WorkOrder workOrder = new WorkOrder();
		workOrder.add(new Integer[]{ (int) 1, (int) 2, (int) 3, (int) 4, (int) 5, (int) 6, (int) 7, (int) 8, (int) 9});

		Component[] components = componentDAO.getComponents(workOrder.getIdsArray(), null);

		logger.error(Arrays.toString(components));
		assertNotNull(components);
	}

}
