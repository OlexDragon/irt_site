package irt.stock.data.jpa.repositories.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.production.ProductionUnit;

public interface ProductionUnitRepository extends CrudRepository<ProductionUnit, Long> {

	Optional<ProductionUnit> findBySerialNumber(String serialNumber);
}
