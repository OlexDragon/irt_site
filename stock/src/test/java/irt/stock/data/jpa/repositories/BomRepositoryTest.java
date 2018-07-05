package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.BomReference;
import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.Manufacture;
import irt.stock.data.jpa.beans.PartNumber;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BomRepositoryTest {

	@Autowired private ComponentRepository componentRepository;
	@Autowired private ManufactureRepository manufactureRepository;
	@Autowired private BomReferenceRepository bomReferenceRepository;
	@Autowired private BomRepository bomRepository;

	private Component top;
	private ArrayList<Component> components;
	private BomReference ref1;
	private BomReference ref2;

	@Before
	public void before() {
		Manufacture manufacture = manufactureRepository.save(new Manufacture("ID", "name", "link"));
		top = componentRepository.save(new Component("top component", manufacture, null));
		components = new ArrayList<>();
		components.add(new Component("component #1", manufacture, null));
		components.add(new Component("component #2", manufacture, null));
		componentRepository.saveAll(components);
		ref1 = bomReferenceRepository.save(new BomReference("references1"));
		ref2 = bomReferenceRepository.save(new BomReference("references2"));
		bomRepository.save(new BomComponent(top.getId(), components.get(0), ref1));
		bomRepository.save(new BomComponent(top.getId(), components.get(1), ref2));
	}

	@Test
	public void test() {
		Component component = components.get(0);
		assertTrue(bomRepository.existsByIdComponentId(component.getId()));
		List<BomComponent> bomComponents = bomRepository.findByIdComponentId(component.getId());
		assertEquals(1, bomComponents.size());
		assertEquals(components.get(0), bomComponents.get(0).getComponent());
		assertEquals(ref1, bomComponents.get(0).getReference());

		component = components.get(1);
		assertTrue(bomRepository.existsByIdComponentId(component.getId()));
		bomComponents = bomRepository.findByIdComponentId(component.getId());
		assertEquals(1, bomComponents.size());
		assertEquals(components.get(1), bomComponents.get(0).getComponent());
		assertEquals(ref2, bomComponents.get(0).getReference());

		
		assertTrue(bomRepository.existsByIdTopComponentId(top.getId()));
		bomComponents = bomRepository.findByIdTopComponentId(top.getId());
		assertEquals(2, bomComponents.size());
		assertEquals(top, bomComponents.get(0).getTopPartNumber());
		assertEquals(top, bomComponents.get(1).getTopPartNumber());

	}

	@Test
	public void findTopComponentTest() {
		Component component = components.get(0);
		final List<PartNumber> topComponents = bomRepository.findTopPartNumberByIdComponentId(component.getId());
		assertFalse(topComponents.isEmpty());
		assertEquals(top, topComponents.get(0));		
	}

	@After
	public void after(){
		bomRepository.deleteAll();
		bomReferenceRepository.deleteAll();
	}
}
