package irt.web.entities.all.repository;

import irt.web.entities.all.ArrayEntity;
import irt.web.entities.all.ArrayEntityPK;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArrayEntityRepository  extends JpaRepository<ArrayEntity, ArrayEntityPK>{

	ArrayEntity findOneBySequenceAndKeyName(Short sequence, String name);
}
