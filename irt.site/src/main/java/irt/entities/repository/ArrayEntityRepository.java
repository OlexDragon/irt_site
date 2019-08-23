package irt.entities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import irt.entities.ArrayEntity;
import irt.entities.ArrayEntityPK;

public interface ArrayEntityRepository extends JpaRepository<ArrayEntity, ArrayEntityPK>{

	List<ArrayEntity> findByKeyNameOrderByDescriptionAsc(String name);
	ArrayEntity findOneBySequenceAndKeyName(Short sequence, String name);
}
