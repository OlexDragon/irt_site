
package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.BomReference;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class BomReferenceRepositoryTest {

	@Autowired private BomReferenceRepository bomReferenceRepository;
	private BomReference ref;

	@BeforeTestClass
	public void before() {
		ref = bomReferenceRepository.save(new BomReference("refferences"));
	}

	@Test
	public void test() {
		Optional<BomReference> oRef = bomReferenceRepository.findByReferences(ref.getReferences());
		assertTrue(oRef.isPresent());
		assertEquals(ref, oRef.get());
	}

}
