package irt.web.view.component.workers;

import static org.junit.Assert.assertEquals;
import irt.web.view.workers.component.PartNumbers;

import org.junit.Test;

public class PartNumberTest {

	@Test
	public void test() {
		String format = PartNumbers.format("000CR0101SM004");
		assertEquals("000-CR0101-SM004", format);
		format = PartNumbers.dbFormat(" 00 0-CR01   01-SM004 ");
		assertEquals("000CR0101SM004", format);
	}

}
