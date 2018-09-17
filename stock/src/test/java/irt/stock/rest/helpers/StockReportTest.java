package irt.stock.rest.helpers;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.rest.helpers.StockReport.SumCollector;

public class StockReportTest {

	@Test
	public void sumCollectorTest() {
		StockReport.SumCollector sumCollector = new StockReport.SumCollector();
		sumCollector.add(null, new BigDecimal("12.66777"));
		assertEquals("$12.67", sumCollector.toString());

		sumCollector.add(null, new BigDecimal("12"));
		assertEquals("$24.67", sumCollector.toString());

		sumCollector.add(Currency.CAD, new BigDecimal("55.555"));
		assertEquals("CAD$55.56; $24.67", sumCollector.toString());

		sumCollector.add(Currency.CAD, new BigDecimal("55.555"));
		assertEquals("CAD$111.11; $24.67", sumCollector.toString());

		sumCollector.add(Currency.USD, new BigDecimal("432"));
		assertEquals("USD$432.00; CAD$111.11; $24.67", sumCollector.toString());

		sumCollector.add(Currency.USD, new BigDecimal("1.555"));
		assertEquals("USD$433.56; CAD$111.11; $24.67", sumCollector.toString());
	}

	@Test
	public void costCollectorTest() {

		StockReport.CostCollector costCollector = new StockReport.CostCollector();

		costCollector.add(Currency.CAD, new BigDecimal("12.66777"), 5L);
		assertEquals("5xCAD$12.66777; ", costCollector.toString());

		costCollector.add(Currency.USD, new BigDecimal("2.661"), 7L);
		assertEquals("7xUSD$2.661; 5xCAD$12.66777; ", costCollector.toString());

		costCollector.add(Currency.CAD, new BigDecimal("12.66777"), 2L);
		assertEquals("7xUSD$2.661; 7xCAD$12.66777; ", costCollector.toString());

		costCollector.add(null, new BigDecimal("12"), 2L);
		assertEquals("7xUSD$2.661; 7xCAD$12.66777; 2x$12; ", costCollector.toString());

		costCollector.add(Currency.USD, new BigDecimal("2.661"), 17L);
		assertEquals("24xUSD$2.661; 7xCAD$12.66777; 2x$12; ", costCollector.toString());

		costCollector.add(null, new BigDecimal("12"), 2L);
		assertEquals("24xUSD$2.661; 7xCAD$12.66777; 4x$12; ", costCollector.toString());

		costCollector.add(null, new BigDecimal("11"), 2L);
		assertEquals("24xUSD$2.661; 7xCAD$12.66777; 2x$11, 4x$12; ", costCollector.toString());

		costCollector.add(null, new BigDecimal("1"), 2L);
		assertEquals("24xUSD$2.661; 7xCAD$12.66777; 2x$1, 2x$11, 4x$12; ", costCollector.toString());

		costCollector.add(Currency.USD, new BigDecimal("12"), 1L);
		assertEquals("24xUSD$2.661, 1xUSD$12; 7xCAD$12.66777; 2x$1, 2x$11, 4x$12; ", costCollector.toString());

		SumCollector sum = costCollector.getSum();
		assertNotNull(sum);
		String string = sum.toString();
		System.out.println(sum);
		assertEquals("USD$75.86; CAD$88.67; $72.00", string);
	}
}
