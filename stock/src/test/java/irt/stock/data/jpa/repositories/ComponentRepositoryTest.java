package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import irt.stock.data.jpa.beans.CompanyQty;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Manufacture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentRepositoryTest {

	private final static Logger logger = LogManager.getLogger();

	@Autowired private CompanyRepository companyRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private CompanyQtyRepository companyQtyRepository;
	@Autowired private CostRepository costRepository;

	private Company company;
	private Component component;
	private Manufacture manufacture;

	private ComponentAlternative alternative;

	private CompanyQty companyQty;

	private Cost cost;

	@Before
	public void before() {
		company = companyRepository.save(new Company("Company Name", CompanyType.VENDOR));
		manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));

		component = componentRepository.save(new Component("partNumber", manufacture));

		alternative = new ComponentAlternative(null, component.getId(), null, manufacture, null);
		List<ComponentAlternative> list = new ArrayList<>();
		list.add(alternative);
		component.setAlternatives(list);

		component = componentRepository.save(component);
		alternative = component.getAlternatives().get(0);

		companyQty = new CompanyQty(company.getId(), component.getId(), 195);
	
		companyQtyRepository.save(companyQty);
		cost = costRepository.save(new Cost(component, alternative, company, 100L, new BigDecimal("456"), Currency.CAD, null, null));
	}

	@Test
	public void test() {

		final Optional<Company> company = companyRepository.findById(this.company.getId());
		assertNotNull(company);

		final Optional<Component> optional = componentRepository.findById(this.component.getId());

		assertTrue(optional.isPresent());

		final Component component = optional.get();

		logger.info("Result: {}", component);

		final Long id = component.getId();

		assertNotNull(id);
		assertEquals(this.component.getId(), id);

		final Manufacture m = component.getManufacture();
		assertNotNull(m);
		assertEquals("name", m.getName());
		assertEquals("link", m.getLink());

		final List<ComponentAlternative> alts = component.getAlternatives();
		assertNotNull(alts);
		assertFalse(alts.isEmpty());
		final ComponentAlternative actual = alts.get(0);
		assertEquals(alternative, actual);
		assertEquals(manufacture, actual.getManufacture());

		final List<CompanyQty> companyQties = component.getCompanyQties();
		assertNotNull(companyQties);
		assertFalse(companyQties.isEmpty());
		assertEquals(companyQty, companyQties.get(0));

		final List<Cost> costs = component.getCosts();
		assertFalse(costs.isEmpty());
		assertEquals(cost, costs.get(0));
	}


	@Test
	public void finfByPartNumberTest() {
		final Optional<Component> oComponent = componentRepository.findByPartNumber(component.getPartNumber());
		assertTrue(oComponent.isPresent());
		final Component get = oComponent.get();
		assertEquals(component, get);
	}

}
