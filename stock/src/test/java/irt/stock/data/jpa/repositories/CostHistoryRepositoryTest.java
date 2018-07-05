package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Cost.OrderType;
import irt.stock.data.jpa.beans.CostHistory;
import irt.stock.data.jpa.beans.Manufacture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CostHistoryRepositoryTest {

	private final static Logger logger = LogManager.getLogger();

	@Autowired private CostRepository costRepository;
	@Autowired private CostHistoryRepository costHistoryRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private ManufactureRepository manufactureRepository;

	private Component component;
	private List<CostHistory> costHistoryList = new ArrayList<>();

	@Before
	public void before() {
		Manufacture manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));
		component = componentRepository.save(new Component("partNumber", manufacture, null));
		Company company = companyRepository.save(new Company("Company Name", CompanyType.VENDOR));

		Cost cost = costRepository.save(new Cost(component, null, company, 10L, new BigDecimal("123.321"), Currency.CAD, OrderType.INV, "1387897"));
		costHistoryList.add(costHistoryRepository.save(new CostHistory(cost)));

		cost = costRepository.save(new Cost(component, null, company, 10L, new BigDecimal("123.321"), Currency.CAD, OrderType.INV, "1387897"));
		costHistoryList.add(costHistoryRepository.save(new CostHistory(cost)));
	}

	@Test
	public void test() {
		final Iterable<CostHistory> iterable = costHistoryRepository.findAll();
		logger.info("All histories:\n\t{}\n\t{}", component, iterable);

		final List<CostHistory> list = costHistoryRepository.findByIdComponentsOrderByChangeDateDesc(component.getId());

		logger.info("\n\t{}", list);
		assertNotNull(list);
		assertEquals(2, list.size());
//		assertTrue(list.get(0).getDate().after(list.get(1).getDate()));
	}

}
