package irt.web.entities.component.repositories;

import irt.web.entities.component.MovementDetailsEntity;
import irt.web.entities.component.MovementDetailsEntityPK;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovementDetailRepository  extends JpaRepository<MovementDetailsEntity, MovementDetailsEntityPK>{

	@Query("SELECT md FROM MovementDetailsEntity md WHERE md.movementDetailsEntityPK.idComponents=:componentId")
	List<MovementDetailsEntity> findByIdComponents(@Param("componentId") Long componentId);
}
