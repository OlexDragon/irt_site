
package irt.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class FirstDigitsHasArrayNamesEntityRepositoryTest {

	@Autowired FirstDigitsHasArrayNamesEntityRepository firstDigitsHasArrayNamesEntityRepository;
	
	@Test
	public void test() {
		List<FirstDigitsHasArrayNamesEntity> entities = firstDigitsHasArrayNamesEntityRepository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>0);
	}
	
	@Test
	public void findOneTest() {
		Optional<FirstDigitsHasArrayNamesEntity> optional = firstDigitsHasArrayNamesEntityRepository.findById(1);
		optional.ifPresent(entities->{
			
			assertNotNull(entities);
			assertNotNull(entities.getArrayNameEntity());
			assertTrue(entities.getArrayNameEntity().getArrayEntities().size()>0);
		});
	}
}
