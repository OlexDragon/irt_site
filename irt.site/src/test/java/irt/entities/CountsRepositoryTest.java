
package irt.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import irt.IrtSiteApp;
import irt.entities.repository.CountsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class CountsRepositoryTest {

	@Autowired private CountsRepository repository;
	@Test
	public void test() {
		List<String> partNubersToCountByClassId = repository.getPartNubersToCountByClassId(17);
		assertTrue(partNubersToCountByClassId.size()>530);

		List<String> partNubersToCount = repository.getPartNubersToCount(1, "00");
		assertTrue(partNubersToCount.size()>530);

		assertEquals(partNubersToCount.size(), partNubersToCountByClassId.size());
	}

}
