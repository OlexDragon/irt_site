package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Component;

public interface ComponentRepository extends CrudRepository<Component, Long> {

//	final static String SQL = "SELECT id, part_number(part_number) as part_number, description, manuf_part_number, qty FROM components WHERE ";

//	@Query(value=SQL + "part_number LIKE concat('%', :#{#desiredPN}, '%') ORDER BY part_number", nativeQuery=true)
	List<Component> findByPartNumberContaining(String desiredPN);

//	@Query(value=SQL + "id = ?1", nativeQuery=true)
//	@Override
//	Optional<Component> findById(Long id);
}
