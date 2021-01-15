package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.ComponentImage;
import irt.stock.data.jpa.beans.ImageLink;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentImageRepositoryTest {

	@Autowired private ImageLinkRepository linkRepository;
	@Autowired private ComponentImageRepository repository;

	private ComponentImage ref;

	@BeforeTestClass
	public void before() {
		ImageLink save = linkRepository.save(new ImageLink("test"));
		ref = repository.save(new ComponentImage(10L, save));
	}

	@Test
	public void test() {
		assertNotNull(ref);
	}

}
