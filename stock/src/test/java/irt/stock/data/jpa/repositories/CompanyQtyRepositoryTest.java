package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.CompanyQty;
import irt.stock.data.jpa.beans.CompanyQtyId;
import irt.stock.data.jpa.beans.Component;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyQtyRepositoryTest {

	private static final Logger logger = LogManager.getLogger();

	@Autowired private CompanyRepository companyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private CompanyQtyRepository repository;

	private Company company;
	private Component component;

	@BeforeTestClass
	public void before() {

		company = companyRepository.save(new Company("Test Company", CompanyType.VENDOR));
		component = componentRepository.save(new Component("partNumber", null, null));

		repository.save( new CompanyQty(company.getId(), component.getId(), 120));

	}

	@Test
	public void test() {
		logger.info("Start test");

		final List<CompanyQty> list = repository.findByIdIdComponents(component.getId());

		logger.info(list);

		assertFalse(list.isEmpty());

		final CompanyQty entity = list.get(0);
		final CompanyQtyId id = entity.getId();

		assertNotNull(id);
		assertEquals(component.getId(), id.getIdComponents());

		final Company c = entity.getCompany();
		assertNotNull(c);
		assertEquals(company.getId(), c.getId());


logger.info("Result: {}", entity);
//
//		assertNotNull(partNumbers);
	}
}
