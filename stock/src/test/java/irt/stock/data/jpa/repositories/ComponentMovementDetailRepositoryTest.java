package irt.stock.data.jpa.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentMovement;
import irt.stock.data.jpa.beans.ComponentMovementDetail;
import irt.stock.data.jpa.beans.ComponentMovementDetailId;
import irt.stock.data.jpa.beans.Manufacture;
import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.User.Status;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ComponentMovementDetailRepositoryTest {

	private static final Logger logger = LogManager.getLogger();

	@Autowired private UserRepository userRepository;
	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentMovementRepository componentMovementRepository;
	@Autowired private ComponentMovementDetailRepository componentMovementDetailRepository;
	@Autowired private CompanyRepository companyRepository;

	private ComponentMovementDetail componentMovementDetail;
	private Component component;
	private ComponentMovement movement;

	@BeforeTestClass
	public void before() {
		User user = userRepository.save(new User("username", "password", "firstName", "lastName", 16777200L, "extension", "email", Status.ACTIVE));
		Company company = companyRepository.save(new Company("companyName", CompanyType.VENDOR));
		final Manufacture manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));
		component = componentRepository.save(new Component("partNumber", manufacture, null));
		movement = componentMovementRepository.save(new ComponentMovement(user, company, company, "description", new Date()));

		componentMovementDetail = new ComponentMovementDetail(movement, component, 25L, null);
		logger.error(componentMovementDetail);
		componentMovementDetail = componentMovementDetailRepository.save(componentMovementDetail);
	}


	@Test
	public void test() {
		logger.info("Start test");

		final List<ComponentMovementDetail> list = componentMovementDetailRepository.findByIdComponentIdOrderByIdComponentMovementDateTimeDesc(component.getId());

		logger.info(list);

		assertFalse(list.isEmpty());

		final ComponentMovementDetail entity = list.get(0);
		final ComponentMovementDetailId id = entity.getId();

		assertNotNull(id);
		assertNotNull(id.getComponentMovement());
		assertNotNull(id.getComponent());
		assertEquals(movement, id.getComponentMovement());
		assertEquals(component, id.getComponent());
		assertEquals(componentMovementDetail, entity);


		logger.info("Result: {}", entity);
	}
}
