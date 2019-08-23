
package irt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.entities.repository.FirstDigitsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
@Transactional
public class FirstDigitsEntityRepozitoryTest {

	@Autowired private FirstDigitsRepository firstDigitsRepository;
	@Test
	public void test() {
		List<FirstDigitsEntity> entities = firstDigitsRepository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>0);
	}

	@Test
	public void findOneTest() {
		Optional<FirstDigitsEntity> optional = firstDigitsRepository.findById(1);
		optional.ifPresent(entity->{
			
			assertNotNull(entity);
			assertNotNull(entity.getFirstDigitsHasArrayNames());
			assertTrue(entity.getFirstDigitsHasArrayNames().getArrayNameEntity().getArrayEntities().size()>0);
		});
	}

	@Test
	public void findOneByPartNumbetFirstCharTest() {
		FirstDigitsEntity entity = firstDigitsRepository.findOneByPartNumbetFirstChar('0');
		assertNotNull(entity);
		assertNotNull(entity.getFirstDigitsHasArrayNames());
		assertTrue(entity.getFirstDigitsHasArrayNames().getArrayNameEntity().getArrayEntities().size()>0);
		assertEquals(new Integer(1), entity.getId());
	}
}
