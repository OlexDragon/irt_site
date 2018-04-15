package irt.stock.data.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.ComponentMovement;

public interface ComponentMovementRepository extends CrudRepository<ComponentMovement, Long> {
}
