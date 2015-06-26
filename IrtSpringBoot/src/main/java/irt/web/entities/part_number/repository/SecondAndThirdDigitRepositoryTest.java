package irt.web.entities.part_number.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import irt.web.IrtWebApp;
import irt.web.entities.part_number.SecondAndThirdDigitEntity;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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


		secondAndThirdDigitEntities = secondAndThirdDigitRepository.findByKeyIdFirstDigitOrderByDescriptionAsc(1);
		assertNotNull(secondAndThirdDigitEntities);
		assertTrue(secondAndThirdDigitEntities.size()>0);
	}

}
