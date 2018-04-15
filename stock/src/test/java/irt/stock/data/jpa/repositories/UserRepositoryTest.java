package irt.stock.data.jpa.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import irt.stock.data.jpa.beans.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	private final static Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository repository;

	@Before
	public void before() {
		repository.save(new User( 1L, "username", "password", "firstName", "lastName", 7777777L, "extension", "email"));
	}

	@Test
	public void test() {

		final Optional<User> oUser = repository.findById(1L);

		logger.info(oUser);

		assertTrue(oUser.isPresent());

		final User user = oUser.get();
		final Long id = user.getId();

		assertNotNull(id);
		assertEquals(new Long(1), id);
	}
}
