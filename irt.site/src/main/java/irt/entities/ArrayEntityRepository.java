package irt.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrayEntityRepository extends JpaRepository<ArrayEntity, ArrayEntityPK>{

	List<ArrayEntity> findByKeyNameOrderByDescriptionAsc(String name);
	ArrayEntity findOneBySequenceAndKeyName(Short sequence, String name);
}
