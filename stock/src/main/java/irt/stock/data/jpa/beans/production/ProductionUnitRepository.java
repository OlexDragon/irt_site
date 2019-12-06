package irt.stock.data.jpa.beans.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProductionUnitRepository extends CrudRepository<ProductionUnit, Long> {

	Optional<ProductionUnit> findBySerialNumber(String serialNumber);
}
