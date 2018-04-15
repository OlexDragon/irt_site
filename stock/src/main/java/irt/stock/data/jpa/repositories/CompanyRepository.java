package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;

public interface CompanyRepository extends CrudRepository<Company, Long> {

	List<Company> findByType(CompanyType companyType);
}
