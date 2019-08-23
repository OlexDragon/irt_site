
package irt.entities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import irt.entities.LinkEntity;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {

	@Query("SELECT max(l.id)+1 AS шв FROM LinkEntity AS l")
	long findNewId();
}
