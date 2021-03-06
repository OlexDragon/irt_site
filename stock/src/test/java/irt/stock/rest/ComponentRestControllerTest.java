package irt.stock.rest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.Cost.OrderType;
import irt.stock.data.jpa.beans.Manufacture;
import irt.stock.data.jpa.repositories.CompanyRepository;
import irt.stock.data.jpa.repositories.ComponentAlternativeRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.CostRepository;
import irt.stock.data.jpa.repositories.ManufactureRepository;
import irt.stock.rest.helpers.StockReport;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentRestControllerTest {
	private final static Logger logger = LogManager.getLogger();

	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentAlternativeRepository alternativeRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private CostRepository costRepository;

	private Component component;

	private Company company;

	@BeforeTestClass
	public void befor() {

		final Manufacture manufacture = manufactureRepository.save(new Manufacture("CA", "name", "link"));
		final Component entity = new Component("partNumber", manufacture, null);
		entity.setQty(1234L);
		component = componentRepository.save(entity);

		company = companyRepository.save(new Company("companyName", CompanyType.CO_MANUFACTURER));
		Cost cost = new Cost(component, null, company, 1000L, new BigDecimal("32"), Currency.CAD, OrderType.INV, "orderNumber");
		costRepository.save(cost);
		cost = new Cost(component, null, company, 200L, new BigDecimal("31.6"), null, null, null);
		costRepository.save(cost);
		cost = new Cost(component, null, company, 200L, BigDecimal.ZERO, null, null, null);
		costRepository.save(cost);

		alternativeRepository.save(new ComponentAlternative(null, component.getId(), "altMfrPartNumber", manufacture, true));
	}

	@Test
	public void test() throws InterruptedException {
		logger.info("\n\t****** Start Test ******\n");

		List<Date> dates = new ArrayList<>();

		Date d1 = new Date();
		dates.add(d1);

		synchronized (this) {
			wait(1);
		}

		Date d2 = new Date();
		dates.add(d2);
		logger.info("{}", dates);

		dates.sort((a, b)->b.compareTo(a));
		logger.info("{} : {} : {} : {}", d1.compareTo(d2), d1.getTime(), d2.getTime(), dates);

		assertEquals(d2, dates.get(0));
	}

	@Test
	public void componentStockReportTest(){
		logger.info("\n\t****** Start Test ******\n");

		final Component c = componentRepository.findById(component.getId()).get();
		logger.info(c);
		logger.info("qty:{}, {}", c.getQty(), c.getCosts());
		final String[] componentStockReport = StockReport.componentStockReport(c);
		logger.info("{}", Arrays.stream(componentStockReport).collect(Collectors.joining(",")));
	}


	@Test
	public void roundReportTest(){
		logger.info("\n\t****** Start Test ******\n");

		List<Cost> list = new ArrayList<>();
		final Cost cost = new Cost(component, null, company, 1L, new BigDecimal(900), null, null, null);
		list.add(cost);
		logger.info(cost);
		final Optional<BigDecimal> round = StockReport.round(list);
		logger.info("round: {}; {}", round,  NumberFormat.getCurrencyInstance().format(round.get().multiply(new BigDecimal(2))));
	}
}
