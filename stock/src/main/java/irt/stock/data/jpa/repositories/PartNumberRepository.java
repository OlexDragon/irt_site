package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import irt.stock.web.PartNumberImpl;

public interface PartNumberRepository extends CrudRepository<PartNumberImpl, Long> {

//		@Query(value="SELECT id, part_number(part_number) as part_number FROM components WHERE part_number LIKE concat('%', :#{#desiredPN}, '%') ORDER BY part_number", nativeQuery=true)
	List<PartNumberImpl> findByPartNumberContaining(@Param("desiredPN") String desiredPN);
}
