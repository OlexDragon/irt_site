package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.CostId;

public interface CostRepository extends CrudRepository<Cost, CostId> {

	List<Cost> findByIdComponentId(Long componentId);
}
