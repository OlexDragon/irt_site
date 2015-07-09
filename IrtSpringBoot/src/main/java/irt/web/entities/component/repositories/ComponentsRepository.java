package irt.web.entities.component.repositories;

import java.util.List;

import irt.web.entities.component.ComponentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComponentsRepository  extends JpaRepository<ComponentEntity, Long>{
    public ComponentEntity findOneByPartNumber(String partNumber);

	public Page<ComponentEntity> findFirst10ByPartNumberContaining(String partNumber, Pageable pageable);

	@Query(value="SELECT part_number(:partNamber);", nativeQuery=true)
	public String partNumberWithDashes(@Param("partNamber") String partNamber);

	public List<ComponentEntity> findByPartNumberStartingWith(String firstLetters);
	public ComponentEntity findFirstByPartNumberStartingWith(String startWith);
	public ComponentEntity findFirstByManufPartNumber(String mfrPN);
}
