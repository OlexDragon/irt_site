
package irt.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class SecondAndThirdDigitRepositoryTest {

	@Autowired private SecondAndThirdDigitRepository repository;

	@Test
	public void test() {
		List<SecondAndThirdDigitEntity> entities = repository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>0);
	}

	@Test
	@Transactional
	public void findOneTest() {
		Optional<SecondAndThirdDigitEntity> optional = repository.findById(new SecondAndThirdDigitPK("CB", 1));
		optional.ifPresent(entity->{
			
			assertNotNull(entity);
			assertNotNull(entity.getHasArrayEntity());
			assertTrue(entity.getHasArrayEntity().flatMap(ClassIdHasArrayEntity::getArrayNameEntity).map(ArrayNameEntity::getArrayEntities).orElseGet(()->new ArrayList<>()).size()>0);
		});
	}
}
