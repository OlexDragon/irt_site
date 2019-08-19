
package irt.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class ClassIdHasArrayEntityRepositoryTest {

	@Autowired ClassIdHasArrayEntityRepository classIdHasArrayEntityRepository;

	@Test
	public void test() {
		Optional<ClassIdHasArrayEntity> optional = classIdHasArrayEntityRepository.findById(49);
		optional.ifPresent(arrayEntity->{
			
			assertNotNull(arrayEntity);
			assertNotNull(arrayEntity.getArrayNameEntity());
			assertNotNull(arrayEntity.getArrayNameEntity().map(ArrayNameEntity::getArrayEntities).orElseGet(()->new ArrayList<>()));
			assertTrue(arrayEntity.getArrayNameEntity().map(ArrayNameEntity::getArrayEntities).orElseGet(()->new ArrayList<>()).size()>0);
		});
	}

}
