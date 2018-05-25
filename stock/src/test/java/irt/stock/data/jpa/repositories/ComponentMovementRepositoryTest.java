package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.User.Status;
import irt.stock.data.jpa.beans.ComponentMovement;
import irt.stock.data.jpa.beans.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentMovementRepositoryTest {

	private static final Logger logger = LogManager.getLogger();

	@Autowired private ComponentMovementRepository componentMovementRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private CompanyRepository companyRepository;

	private ComponentMovement componentMovement;
	private User user;
	private Company fromCompany;
	private Company toCompany;

	@Before
	public void before() {
		user = userRepository.save(new User("username", "password", "firstName", "lastName", 16777200L, "extension", "email", Status.ACTIVE));
		fromCompany = companyRepository.save(new Company("companyName", CompanyType.VENDOR));
		toCompany = companyRepository.save(new Company("companyName", CompanyType.VENDOR));
		final ComponentMovement entity = new ComponentMovement(user, fromCompany, toCompany, "String description", new Date());
		logger.debug(entity);
		componentMovement = componentMovementRepository.save(entity);
	}

	@Test
	public void test() {

		logger.info(componentMovement);

		final Optional<ComponentMovement> oComponentMovement = componentMovementRepository.findById(componentMovement.getId());
		assertTrue(oComponentMovement.isPresent());
		final ComponentMovement cm = oComponentMovement.get();

		logger.info(cm);

		assertEquals(user, cm.getUser());
		assertEquals(fromCompany, cm.getFromCompany());
		assertEquals(toCompany, cm.getToCompany());
	}

}
