package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.ComponentMovementDetail;
import irt.stock.data.jpa.beans.ComponentMovementDetailId;

public interface ComponentMovementDetailRepository extends CrudRepository<ComponentMovementDetail, ComponentMovementDetailId> {

	List<ComponentMovementDetail> findByIdComponentIdOrderByIdComponentMovementDateTimeDesc(long componentId);
}
