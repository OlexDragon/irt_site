
package irt.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class ManufactureRepositoryTest {

	@Autowired ManufactureRepository repository;

	@Test
	public void test() {
		List<ManufactureEntity> entities = repository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>100);
	}

}
