
package irt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.entities.repository.IrtComponentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class ComponentRepositoryTest {

	@Autowired IrtComponentRepository repository;

	@Test
	public void findAllTest() {
		List<IrtComponentEntity> entities = repository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>1000);
	}

	@Test
	public void findOneByManufPartNumberTest() {
		IrtComponentEntity entities = repository.findOneByManufPartNumber("TCD-20-4+");
		assertNotNull(entities);
		assertEquals(new Long(19), entities.getId());
	}

	@Test
	public void notFindOneByManufPartNumberTest() {
		IrtComponentEntity entities = repository.findOneByManufPartNumber("nonexistent");
		assertNull(entities);
	}

	@Test
	public void findOneByPartNumberTest() {
		IrtComponentEntity entities = repository.findOneByPartNumber("000MC0027SM006");
		assertNotNull(entities);
		assertEquals(new Long(19), entities.getId());
	}

	@Test
	public void notFindOneByPartNumberTest() {
		IrtComponentEntity entities = repository.findOneByPartNumber("nonexistent");
		assertNull(entities);
	}
}
