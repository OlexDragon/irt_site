
package irt.entities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import irt.entities.IrtComponentEntity;

public interface IrtComponentRepository extends JpaRepository<IrtComponentEntity, Long> {

	IrtComponentEntity findOneByManufPartNumber(String manufPartNumber);
	IrtComponentEntity findOneByPartNumber(String partNumber);
	IrtComponentEntity findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc(String manufPartNumber, String partNumber);

	@Query(value="SELECT part_number(:partNamber);", nativeQuery=true)
	String partNumberWithDashes(@Param("partNamber") String partNamber);

	@Query(value="SELECT DISTINCT SUBSTRING(`part_number`, 4, 5) FROM irt.components where part_number like 'A%'", nativeQuery=true)
	List<String> getAssembliesSequences();

	List<IrtComponentEntity> findByPartNumberStartingWith(String partNamber);
}
