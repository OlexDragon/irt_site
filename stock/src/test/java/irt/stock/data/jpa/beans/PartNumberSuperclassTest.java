package irt.stock.data.jpa.beans;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PartNumberSuperclassTest {

	@Test
	public void test() {
		assertEquals("00A-AV0047-SM003", PartNumberSuperclass.addDashes("00AAV0047SM003"));
		assertEquals("0CB-19NEPC-20E2-021", PartNumberSuperclass.addDashes("0CB19NEPC20E2021"));
		assertEquals("00C-01E0PS-02502", PartNumberSuperclass.addDashes("00C01E0PS02502"));
		assertEquals("0CO-001FCT-0099", PartNumberSuperclass.addDashes("0CO001FCT0099"));
		assertEquals("00D-0054SW-SM", PartNumberSuperclass.addDashes("00D0054SWSM"));
		assertEquals("00F-006040-0405", PartNumberSuperclass.addDashes("00F0060400405"));
		assertEquals("00G-FG0000-010", PartNumberSuperclass.addDashes("00GFG0000010"));
		assertEquals("0RQ-AD0002-QF024", PartNumberSuperclass.addDashes("0RQAD0002QF024"));
		assertEquals("0IC-AD0065-SM008", PartNumberSuperclass.addDashes("0ICAD0065SM008"));
		assertEquals("00I-10E1A08-90E2", PartNumberSuperclass.addDashes("00I10E1A0890E2"));
		assertEquals("0IS-CB110-19CI", PartNumberSuperclass.addDashes("0ISCB11019CI"));
		assertEquals("0MC-LC0169-SM100", PartNumberSuperclass.addDashes("0MCLC0169SM100"));
		assertEquals("000-A10633-FT002", PartNumberSuperclass.addDashes("000A10633FT002"));
		assertEquals("0PP-BT0005-00", PartNumberSuperclass.addDashes("0PPBT000500"));
		assertEquals("0RF-MBKXB-10GN76", PartNumberSuperclass.addDashes("0RFMBKXB10GN76"));
		assertEquals("0PS-ADNCAI-BM02", PartNumberSuperclass.addDashes("0PSADNCAIBM02"));
		assertEquals("00R-000E01-0402", PartNumberSuperclass.addDashes("00R000E010402"));
		assertEquals("00T-0057PN-SM", PartNumberSuperclass.addDashes("00T0057PNSM"));
		assertEquals("0VV-AD0074-SM008", PartNumberSuperclass.addDashes("0VVAD0074SM008"));
		assertEquals("0WH-0351E0-0000", PartNumberSuperclass.addDashes("0WH0351E00000"));
		assertEquals("MBR-0059IC-01R01", PartNumberSuperclass.addDashes("MBR0059IC01R01"));
		assertEquals("MCA-0017IC-01R01", PartNumberSuperclass.addDashes("MCA0017IC01R01"));
		assertEquals("MCV-0001IC-01R01", PartNumberSuperclass.addDashes("MCV0001IC01R01"));
		assertEquals("MDB-0003IC-01R01", PartNumberSuperclass.addDashes("MDB0003IC01R01"));
		assertEquals("PCB-00008H-CONV-R01", PartNumberSuperclass.addDashes("PCB00008HCONVR01"));
		assertEquals("0PP-BT0005-00", PartNumberSuperclass.addDashes("0PPBT000500"));
		assertEquals("SNT-002035", PartNumberSuperclass.addDashes("SNT002035"));
	}

}
