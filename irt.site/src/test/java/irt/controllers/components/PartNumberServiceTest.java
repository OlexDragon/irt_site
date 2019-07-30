
package irt.controllers.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.entities.IrtComponentEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class PartNumberServiceTest {

	private Logger logger = LogManager.getLogger();

	@Autowired private PartNumberService partNumberService;

	@Test
	public void entityBuilder1CB_SetNullTest(){
		logger.traceEntry();
		IrtComponentEntity entity = partNumberService.buildEntity(null);
		assertNull(entity);
	}

	@Test
	public void entityBuilder1CB_FirstTest(){
		logger.traceEntry();
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);
		pnf.setSecond("CB");

		IrtComponentEntity entity = partNumberService.buildEntity(pnf);
		assertNotNull(entity);
		assertEquals("0CB_____________", entity.getPartNumber());
	}

	@Test
	public void entityBuilder1CB_ComponentTypeTest(){
		logger.traceEntry();
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);
		pnf.setSecond("CB");

		IrtComponentEntity entity = partNumberService.buildEntity(pnf);
		assertNotNull(entity);
		assertEquals("0CB_____________", entity.getPartNumber());
	}

	@Test
	public void entityBuilder1CB_buildEntityTest(){
		logger.traceEntry();
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);
		pnf.setSecond("CB");
		String[] fields = new String[8];
//		fields[0] = "BM";
		fields[1] = "19";
		fields[2] = "8W";
		fields[3] =	"22E4";
		fields[4] =	"111";
		fields[5] =	"Description field";
		fields[6] =	"AB";
		fields[7] =	"Mfr PN";
		pnf.setFields(fields);

		IrtComponentEntity entity = partNumberService.buildEntity(pnf);
		assertNotNull(entity);
		assertEquals("0CB__198W22E4111", entity.getPartNumber());
		assertEquals(fields[5], entity.getDescription());
		assertEquals(fields[6], entity.getManufacture().getId());
		assertEquals(fields[7], entity.getManufPartNumber());
	}

	@Test
	public void entityBuilder1CB_buildEntityTest2(){
		logger.traceEntry();
		PartNumberForm pnf = new PartNumberForm();
		pnf.setFirst(1);
		pnf.setSecond("CB");
		pnf.setPartNumber("0CB-BFSAFL-28E1-___");
		String[] fields = new String[8];
//		fields[0] = "BM";
		fields[1] = "19";
		fields[2] = "8W";
		fields[3] =	"22E4";
		fields[4] =	"___";
		fields[5] =	"Description field";
		fields[6] =	"AB";
		fields[7] =	"RF316-04BJ3-01SP1-0330";
		pnf.setFields(fields);

		IrtComponentEntity entity = partNumberService.buildEntity(pnf);

		assertNotNull(entity);
		assertEquals("0CBBFSAFL33E1013", entity.getPartNumber());
		assertEquals("BNC TO SMA 33CM", entity.getDescription());
		assertEquals("SA", entity.getManufacture().getId());
		assertEquals("RF316-04BJ3-01SP1-0330", entity.getManufPartNumber());
	}
}
