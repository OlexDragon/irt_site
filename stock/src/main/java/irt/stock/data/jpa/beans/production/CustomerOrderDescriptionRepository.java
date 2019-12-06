package irt.stock.data.jpa.beans.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CustomerOrderDescriptionRepository extends CrudRepository<CustomerOrderDescription, Long> {

	Optional<CustomerOrderDescription> findByDescription(String description);
}
