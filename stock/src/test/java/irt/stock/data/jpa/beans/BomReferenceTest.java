
package irt.stock.data.jpa.beans;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BomReferenceTest {

	private BomReference bomReference;

	@Before
	public void befor(){
		bomReference = new BomReference("1-10 16 17 25-26 31-33"); // {1 2 3 4 5 6 7 8 9 10 16 17 25 26 31 32 33} - 17
	}

	@Test
	public void test() {
		assertEquals(17, bomReference.getQty());
	}

}
