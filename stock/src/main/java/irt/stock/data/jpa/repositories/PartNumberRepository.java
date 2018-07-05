package irt.stock.data.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.PartNumber;

public interface PartNumberRepository extends CrudRepository<PartNumber, Long> {

	List<PartNumber> findByPartNumberContainingOrManufPartNumberContainingOrderByPartNumber(String desiredPN, String mfrPn);
	List<PartNumber> findByPartNumberLikeOrderByPartNumber(String desiredPN);
	Optional<PartNumber> findByPartNumber(String partNamber);
}
