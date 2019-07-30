
package irt.controllers.components;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.entities.SecondAndThirdDigitEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class PartNumberSecondDigitsServiceTest {

	@Autowired PartNumberSecondDigitsService partNumberSecondDigitsService;

	@Test
	public void getSecondDiditsListTest() {

		PartNumberComponent partNumberComponent = new PartNumberComponent();
		partNumberSecondDigitsService.setSecondDigitsEntities(6, partNumberComponent);

		List<SecondAndThirdDigitEntity> secondDiditsList_2 = partNumberComponent.getSecondDiditsList();
		assertNotNull(secondDiditsList_2);
		assertTrue(secondDiditsList_2.size()>0);
	}
}
