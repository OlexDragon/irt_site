package irt.data;

import org.junit.Test;

public class ToDoClassTest {

	@Test
	public void test() {
		ToDoClass.parseToDoClass("ToDoClass [command=SEARCH, value={Capacitor [partNumber=00C10E1CS05004, id=36, manufPartNumber=CC0402JRNPO9BN100, description=10PF, bdValue=10pF, dbVoltage=50 V, manufId=YA, quantityStr=9720, link=irt.data.Link@772f2cd1, footprint=CAPC1005X60N, schematicLetter=C, schematicPart=CAP]}])");
	}
}
