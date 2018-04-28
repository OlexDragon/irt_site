package irt.stock.data.jpa.repositories;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.Manufacture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManufactureRepositoryTest {

	@Autowired private ManufactureRepository manufactureRepository;

	@Before
	public void before() {
		manufactureRepository.save(new Manufacture("ID", "Manufacture Name", "Manufacture Link"));
	}

	@Test
	public void test() {

		final Optional<Manufacture> findById = manufactureRepository.findById("ID");
		assertTrue(findById.isPresent());

		final Manufacture manufacture = findById.get();
		assertEquals("Manufacture Name", manufacture.getName());
		assertEquals("Manufacture Link", manufacture.getLink());
	}
}
