package irt.web.entities.all.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import irt.web.IrtWebApp;
import irt.web.entities.all.SecondAndThirdDigitEntity;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IrtWebApp.class)
public class SecondAndThirdDigitRepositoryTest {

	@Autowired
	SecondAndThirdDigitRepository secondAndThirdDigitRepository;

	@Test
	public void test() {
		List<SecondAndThirdDigitEntity> secondAndThirdDigitEntities = secondAndThirdDigitRepository.findAll();
		assertNotNull(secondAndThirdDigitEntities);
		assertTrue(secondAndThirdDigitEntities.size()>0);


		secondAndThirdDigitEntities = secondAndThirdDigitRepository.findByFirstDigitOrderByDescription(1, new Sort("description"));
		assertNotNull(secondAndThirdDigitEntities);
		assertTrue(secondAndThirdDigitEntities.size()>0);
	}

}
