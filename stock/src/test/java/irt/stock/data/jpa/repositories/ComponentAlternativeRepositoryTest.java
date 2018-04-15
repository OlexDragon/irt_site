package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentAlternative;
import irt.stock.data.jpa.beans.Manufacture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentAlternativeRepositoryTest {

	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private ComponentRepository componentRepository;
	@Autowired private ComponentAlternativeRepository alternativeRepository;

	private ComponentAlternative alternative;

	private Component component;
	private Manufacture manufacture;

	@Before
	public void before() {
		manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));
		component = componentRepository.save(new Component(null, "partNumber", null));
		alternative = alternativeRepository.save(new ComponentAlternative(null, component.getId(), "Mfr PN", manufacture, null));
	}

	@Test
	public void test() {
		List<ComponentAlternative> list = alternativeRepository.findByIdComponents(alternative.getIdComponents());
		assertFalse(list.isEmpty());
		final ComponentAlternative actual = list.get(0);
		assertEquals(alternative, actual);
		assertEquals(manufacture, actual.getManufacture());
	}
}
