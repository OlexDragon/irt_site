
package irt.stock.data.jpa.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;

public class BomReferenceTest {
//	private final static Logger logger = LogManager.getLogger();

	private BomReference bomReference;

	@BeforeTestClass
	public void befor(){
		bomReference = new BomReference("1-10 16 17 25-26 31-33"); // {1 2 3 4 5 6 7 8 9 10 16 17 25 26 31 32 33} - 17
	}

	@Test
	public void qtyTest() {
		assertEquals(17, bomReference.getQty());
	}

	@Test
	public void referencesSetTest() {

		Set<Integer> forTest = new HashSet<>();
		forTest.addAll(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 25, 26, 31, 32, 33}));
		assertEquals(forTest, bomReference.referencesToSet());
	}
}
