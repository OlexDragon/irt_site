package irt.stock.data.jpa.repositories;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.User.Status;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	private final static Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository repository;

	private User user;

	@BeforeTestClass
	public void before() {
		user = repository.save(new User( "username", "password", "firstName", "lastName", 7777777L, "extension", "email", Status.ACTIVE));
	}

	@Test
	public void test() {

		final Optional<User> oUser = repository.findById(user.getId());

		logger.info(oUser);

		assertTrue(oUser.isPresent());

		final User user = oUser.get();
		final Long id = user.getId();

		assertNotNull(id);
		assertEquals(this.user.getId(), id);
	}
}
