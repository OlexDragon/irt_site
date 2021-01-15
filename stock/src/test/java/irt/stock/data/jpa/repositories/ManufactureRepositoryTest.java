package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.Manufacture;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ManufactureRepositoryTest {

	@Autowired private ManufactureRepository manufactureRepository;

	@BeforeTestClass
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
