package irt.stock.data.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findById(Long id);
	Optional<User> findByUsername(String username);
	Iterable<User> findAllByOrderByStatusDescFirstnameAsc();

	@Query(value="SELECT * FROM users u WHERE (u.permission & :permission) > 0 ORDER BY u.firstname ASC", nativeQuery = true)
	Iterable<User> findByPermission(Long permission);
}
