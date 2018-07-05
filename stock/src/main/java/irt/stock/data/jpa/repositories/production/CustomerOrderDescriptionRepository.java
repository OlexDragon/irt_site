package irt.stock.data.jpa.repositories.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.production.CustomerOrderDescription;

public interface CustomerOrderDescriptionRepository extends CrudRepository<CustomerOrderDescription, Long> {

	Optional<CustomerOrderDescription> findByDescription(String description);
}
