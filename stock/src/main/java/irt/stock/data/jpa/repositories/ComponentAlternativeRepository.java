package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.ComponentAlternative;

public interface ComponentAlternativeRepository extends CrudRepository<ComponentAlternative, Long> {

	List<ComponentAlternative> findByIdComponents(Long componentId);
}
