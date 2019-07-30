
package irt.entities.builders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class EntityBuilder00CTest {

	@Autowired EntityBuilder entityBuilder00C;

	@Test
	public void getSizeOrSequence() {
		EntityBuilder00C eb = (EntityBuilder00C) entityBuilder00C;

		String seq = eb.getSizeOrSequence("00C10E8ES050", "OT");
		assertEquals("03", seq);

		 seq = eb.getSizeOrSequence("00C47E7ET063", "04");
		assertEquals("04", seq);

		 seq = eb.getSizeOrSequence("00C47E7ET063", "OT");
		assertEquals("01", seq);
	}

}
