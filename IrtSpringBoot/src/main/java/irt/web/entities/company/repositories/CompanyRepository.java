package irt.web.entities.company.repositories;

import java.util.List;

import irt.web.entities.company.CompanyEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{

	public List<CompanyEntity> findByType(short type);
}
