package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Manufacture;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CostRepositoryTest {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentAlternativeRepository alternativeRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private CostRepository costRepository;
	private Manufacture manufacture;
	private Component component;
	private ComponentAlternative alternative;
	private Company company;
	private Cost[] cost = new Cost[2];

	@Before
	public void befor() {
		manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));
		component = componentRepository.save(new Component("partNumber", manufacture, null));
		alternative = alternativeRepository.save(new ComponentAlternative(null, component.getId(), "altMfrPartNumber", manufacture, null));
		company = companyRepository.save(new Company("companyName", CompanyType.VENDOR));
		cost[0] = costRepository.save(new Cost(component, alternative, company, 10L, new BigDecimal("1234.4321"), Currency.CAD, null, null));
		cost[1] = costRepository.save(new Cost(component, null, company, 10L, new BigDecimal("8367.7347378"), Currency.CAD, null, null));
		logger.info("{}",(Object[])cost);
	}

	@Test
	public void test() {
		logger.info("\n\n\t ***** Start Test ***** \n\n");
		final List<Cost> list = costRepository.findByIdIdComponents(component.getId());
		logger.info(list);

		assertFalse(list.isEmpty());
		assertEquals(cost.length, list.size());

		for(int i=0; i<cost.length; i++) {
			final int index = list.indexOf(cost[i]);
			final Cost c = list.get(index);
			logger.info("\n\t{}\n\t{}", c, cost[i]);
			assertThat(cost[i].getCost(),  Matchers.comparesEqualTo(c.getCost()));
			assertEquals(company, c.getCompany());
			
			final ComponentAlternative alt = c.getAlternative();
			if(alt!=null)
				assertEquals(alternative, alt);
			else
				assertNull(alt);

			assertNotNull(c.getChangeDate());
		}
		logger.info("\n\n\t ^^^^^^^ Test ends ^^^^^^^ \n\n");
	}

}
