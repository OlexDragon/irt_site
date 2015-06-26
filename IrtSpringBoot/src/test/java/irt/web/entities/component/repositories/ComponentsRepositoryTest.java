package irt.web.entities.component.repositories;

import static org.junit.Assert.*;
import irt.web.IrtWebApp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IrtWebApp.class)
public class ComponentsRepositoryTest {

	Logger logger = LogManager.getLogger();
	@Autowired
	ComponentsRepository componentsRepository;

	@Test
	public void test() {
		String partNamber = "0ICST0512SM008";
		String partNumberWithDashes = componentsRepository.partNumberWithDashes(partNamber);
		logger.trace("\n\tinput: {}\n\toutput: {}", partNamber, partNumberWithDashes);
		assertNotNull(partNumberWithDashes);
		assertTrue(partNumberWithDashes.contains("-"));
		assertTrue(partNumberWithDashes.length()>partNamber.length());
	}

}
