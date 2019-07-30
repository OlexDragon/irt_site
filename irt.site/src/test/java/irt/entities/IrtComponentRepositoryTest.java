
package irt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class IrtComponentRepositoryTest {

	@Autowired IrtComponentRepository repository;

	@Test
	public void findOne() {
		Optional<IrtComponentEntity> optional = repository.findById(939L);
		optional.ifPresent(e->{
			
			assertNotNull(e);
			assertEquals("BNC TO SMA 33CM", e.getDescription());
			assertNull(e.getFootprint());
			assertEquals((Long)939L, e.getId());
			assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
			assertNull(e.getLocation());
			assertEquals("Samtec", e.getManufacture().getName());
			assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
			assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
			assertNull(e.getPartType());
//			assertEquals((Integer)24, e.getQty());
			assertNull(e.getSchematicLetter());
			assertNull(e.getSchematicPart());
		});
	}

	@Test
	public void findOneByManufPartNumber() {
		IrtComponentEntity e = repository.findOneByManufPartNumber("RF316-04BJ3-01SP1-0330");

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByPartNumber() {
		IrtComponentEntity e = repository.findOneByPartNumber("0CBBFSAFL33E1013");

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByManufPartNumberOrPartNumber1() {
		IrtComponentEntity e = repository.findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc("RF316-04BJ3-01SP1-0330", "0CBBFSAFL33E1013");

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByManufPartNumberOrPartNumber2() {
		IrtComponentEntity e = repository.findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc(null, "0CBBFSAFL33E1013");

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByManufPartNumberOrPartNumber3() {
		IrtComponentEntity e = repository.findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc("RF316-04BJ3-01SP1-0330", null);

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByManufPartNumberOrPartNumber4() { //Part Number is from different component
		IrtComponentEntity e = repository.findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc("RF316-04BJ3-01SP1-0330", "0CBSASPSR36E0000");

		assertNotNull(e);
		assertEquals("BNC TO SMA 33CM", e.getDescription());
		assertNull(e.getFootprint());
		assertEquals((Long)939L, e.getId());
		assertEquals("/irt/lib/0CB/RF316.pdf", e.getLink().getLink());
		assertNull(e.getLocation());
		assertEquals("Samtec", e.getManufacture().getName());
		assertEquals("RF316-04BJ3-01SP1-0330", e.getManufPartNumber());
		assertEquals("0CBBFSAFL33E1013", e.getPartNumber());
		assertNull(e.getPartType());
//		assertEquals((Integer)24, e.getQty());
		assertNull(e.getSchematicLetter());
		assertNull(e.getSchematicPart());
	}

	@Test
	public void findOneByManufPartNumberOrPartNumber5() { //Part Number is from different component
		IrtComponentEntity e = repository.findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc(null, null);

		assertNull(e);
	}
}
