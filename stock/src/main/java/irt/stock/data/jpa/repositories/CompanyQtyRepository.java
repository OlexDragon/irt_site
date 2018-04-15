package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.CompanyQty;

public interface CompanyQtyRepository extends CrudRepository<CompanyQty, Long> {
	List<CompanyQty> findByIdIdComponents(Long idComponents);
}
