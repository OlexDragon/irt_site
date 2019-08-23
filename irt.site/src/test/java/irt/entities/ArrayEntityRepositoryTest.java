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
import irt.entities.repository.ArrayEntityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IrtSiteApp.class)
public class ArrayEntityRepositoryTest {

	@Autowired private ArrayEntityRepository arrayEntityRepository;

	@Test
	public void test() {
		List<ArrayEntity> entities = arrayEntityRepository.findAll();
		assertNotNull(entities);
		assertTrue(entities.size()>0);
	}

	@Test
	public void findByKeyNameTest() {
		List<ArrayEntity> entities = arrayEntityRepository.findByKeyNameOrderByDescriptionAsc("band");
		assertNotNull(entities);
		assertTrue(entities.size()>0);
	}

	@Test
	public void findOneBySequenceAndKeyNameTest() {
		ArrayEntity entity = arrayEntityRepository.findOneBySequenceAndKeyName((short) 4, "board_title");
		assertNotNull(entity);
	}

}
