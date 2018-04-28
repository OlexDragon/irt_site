package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.PartNumber;

public interface PartNumberRepository extends CrudRepository<PartNumber, Long> {

	List<PartNumber> findByPartNumberContainingOrManufPartNumberContainingOrderByPartNumber(String desiredPN, String mfrPn);
}
