package irt.stock.data.jpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.BomReference;

public interface BomReferenceRepository extends CrudRepository<BomReference, Long> {

	Optional<BomReference> findByReferences(String references);
}
