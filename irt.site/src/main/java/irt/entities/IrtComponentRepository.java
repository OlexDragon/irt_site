
package irt.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IrtComponentRepository extends JpaRepository<IrtComponentEntity, Long> {

	IrtComponentEntity findOneByManufPartNumber(String manufPartNumber);
	IrtComponentEntity findOneByPartNumber(String partNumber);
	IrtComponentEntity findFirstByManufPartNumberAndManufPartNumberNotNullOrPartNumberOrderByManufPartNumberDesc(String manufPartNumber, String partNumber);

	@Query(value="SELECT part_number(:partNamber);", nativeQuery=true)
	String partNumberWithDashes(@Param("partNamber") String partNamber);

	List<IrtComponentEntity> findByPartNumberStartingWith(String partNamber);
}
