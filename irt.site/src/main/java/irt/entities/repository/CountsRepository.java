
package irt.entities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import irt.entities.CountsEntity;

public interface CountsRepository extends JpaRepository<CountsEntity, Long> {

	static final String byClassId = "SELECT`part_number`FROM`irt`.`components`AS`c`"
								+ "JOIN`irt`.`first_digits`AS`f`ON`f`.`part_numbet_first_char`=substring(`part_number`, 1, 1)"
								+ "JOIN`irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`AND`s`.`id`=substring(`part_number`, 2, 2)"
								+ "JOIN`irt`.`counts`AS`c_class`ON`c_class`.`class_id`=`s`.`class_id`"
								+ "JOIN`irt`.`counts`AS`c_id`ON`c_id`.`id`=`c_class`.`id`"
								+ "WHERE`c_id`.`class_id`=:classId ";

	@Query(value=byClassId, nativeQuery=true)
	List<String> getPartNubersToCountByClassId(@Param("classId")int classId);

	static final String query = "SELECT`part_number`FROM`irt`.`components`AS`c`"
								+ "JOIN`irt`.`first_digits`AS`f`ON`f`.`part_numbet_first_char`=substring(`part_number`, 1, 1)"
								+ "JOIN`irt`.`second_and_third_digit`AS`s`ON`s`.`id_first_digits`=`f`.`id_first_digits`AND`s`.`id`=substring(`part_number`, 2, 2)"
								+ "JOIN`irt`.`counts`AS`c_class`ON`c_class`.`class_id`=`s`.`class_id`"
								+ "JOIN`irt`.`counts`AS`c_id`ON`c_id`.`id`=`c_class`.`id`"
								+ "JOIN`irt`.`second_and_third_digit`AS`sd`ON`c_id`.`class_id`=`sd`.`class_id`"
								+ "WHERE`sd`.`id_first_digits`=:firstId AND`sd`.`id`=:secondId";

	@Query(value=query, nativeQuery=true)
	List<String> getPartNubersToCount(@Param("firstId")int firstId, @Param("secondId")String secondId);
}
