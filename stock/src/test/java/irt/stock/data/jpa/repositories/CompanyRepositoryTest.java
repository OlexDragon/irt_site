package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyRepositoryTest {

	@Autowired
	private CompanyRepository companyRepository;
	private Company company;

	@BeforeTestClass
	public void before() {
		company = companyRepository.save(new Company( "name", CompanyType.VENDOR));
		companyRepository.save(new Company("CO MANUFACTURER", CompanyType.CO_MANUFACTURER));
	}

	@Test
	public void test() {

		final Optional<Company> findById = companyRepository.findById(company.getId());
		assertTrue(findById.isPresent());

		final Company c = findById.get();
		assertEquals("name", c.getCompanyName());
		assertTrue(c.getId()>0);

		final List<Company> findByType = companyRepository.findByType(CompanyType.CO_MANUFACTURER);
		assertFalse(findByType.isEmpty());
		assertEquals(1, findByType.size());
		assertNotEquals(company, findByType.get(0));
	}
}
