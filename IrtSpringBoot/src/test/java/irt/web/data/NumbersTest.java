package irt.web.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumbersTest {

	@Test
	public void stringFormatTest() {

		assertEquals("00E0", Numbers.stringFormat("0", 4, 0));
		assertEquals("10E0", Numbers.stringFormat("1", 4, 0));
		assertEquals("14E0", Numbers.stringFormat("14", 4, 0));

		assertEquals("000E0", Numbers.stringFormat("0", 5, 0));
		assertEquals("100E0", Numbers.stringFormat("1", 5, 0));
		assertEquals("140E0", Numbers.stringFormat("14",5, 0));

		assertEquals("155E0", Numbers.stringFormat("155", 5, 0));
		assertEquals("146E0", Numbers.stringFormat("146", 5, 0));
	}

	@Test
	public void errorTest() {

		String n = Numbers.numberToExponential(null, 4);
		assertEquals("ERR ", n);
		n = Numbers.numberToExponential(null, 5);
		assertEquals("ERROR", n);
		n = Numbers.numberToExponential("", 5);
		assertEquals("ERROR", n);
		n = Numbers.numberToExponential("A", 5);
		assertEquals("ERROR", n);
		n = Numbers.numberToExponential("     ", 5);
		assertEquals("ERROR", n);
	}

	@Test
	public void integerToExponentialTest1() {

		String n = Numbers.numberToExponential("0", 4);
		assertEquals("00E0", n);
		n = Numbers.numberToExponential("00000000000000000", 4);
		assertEquals("00E0", n);
		n = Numbers.numberToExponential("1", 4);
		assertEquals("10E0", n);
		n = Numbers.numberToExponential("000000000000000001", 4);
		assertEquals("10E0", n);
		n = Numbers.numberToExponential("16", 4);
		assertEquals("16E1", n);
		n = Numbers.numberToExponential("0000000000000000018", 4);
		assertEquals("18E1", n);

		n = Numbers.numberToExponential("0", 5);
		assertEquals("000E0", n);
		n = Numbers.numberToExponential("00000000000000000", 5);
		assertEquals("000E0", n);
		n = Numbers.numberToExponential("1", 5);
		assertEquals("100E0", n);
		n = Numbers.numberToExponential("000000000000000001", 5);
		assertEquals("100E0", n);
		n = Numbers.numberToExponential("16", 5);
		assertEquals("160E1", n);
		n = Numbers.numberToExponential("0000000000000000018", 5);
		assertEquals("180E1", n);
	}

	@Test
	public void integerToExponentialTest2() {

		String n = Numbers.numberToExponential("123", 4);
		assertEquals("12E2", n);
		n = Numbers.numberToExponential("125", 4);
		assertEquals("13E2", n);

		n = Numbers.numberToExponential("12347", 5);
		assertEquals("123E4", n);
		n = Numbers.numberToExponential("11111", 5);
		assertEquals("111E4", n);
	}
}
