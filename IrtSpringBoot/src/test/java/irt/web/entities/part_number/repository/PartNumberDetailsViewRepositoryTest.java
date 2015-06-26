package irt.web.entities.part_number.repository;

import static org.junit.Assert.*;
import irt.web.IrtWebApp;
import irt.web.entities.part_number.PartNumberDetailsView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IrtWebApp.class)
public class PartNumberDetailsViewRepositoryTest {

	Logger logger = LogManager.getLogger();

	@Autowired
	PartNumberDetailsViewRepository partNumberDetailsViewRepository;

	@Test
	public void test() {
		logger.entry();
		PartNumberDetailsView partNumberDetailsView = partNumberDetailsViewRepository.findFirstByKeyFirstThreeCharsAndKeySequence("0CB", 4);
		logger.trace(partNumberDetailsView);
		assertNotNull(partNumberDetailsView);
	}

}
