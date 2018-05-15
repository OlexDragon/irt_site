
package irt.stock.data.jpa.repositories;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.BomReference;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BomReferenceRepositoryTest {

	@Autowired private BomReferenceRepository bomReferenceRepository;
	private BomReference ref;

	@Before
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
