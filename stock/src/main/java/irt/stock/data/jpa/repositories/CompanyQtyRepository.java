package irt.stock.data.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.CompanyQty;

public interface CompanyQtyRepository extends CrudRepository<CompanyQty, Long> {
	List<CompanyQty> findByIdIdComponents(Long idComponents);
	Optional<CompanyQty> findByIdIdCompaniesAndIdIdComponents(Long idCompanies, Long idComponents);
}
