package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.BomComponent;
import irt.stock.data.jpa.beans.BomComponentId;
import irt.stock.data.jpa.beans.PartNumber;

public interface BomRepository extends CrudRepository<BomComponent, BomComponentId> {

	boolean existsByIdComponentId(Long componentId);
	List<BomComponent> findByIdComponentId(Long componentId);

	boolean existsByIdTopComponentId(Long componentId);
	List<BomComponent> findByIdTopComponentId(Long componentId);

	@Query("SELECT DISTINCT bom.topPartNumber AS topPartNumber FROM BomComponent AS bom WHERE bom.id.componentId = ?1")
	List<PartNumber> findTopPartNumberByIdComponentId(Long componentId);
}
