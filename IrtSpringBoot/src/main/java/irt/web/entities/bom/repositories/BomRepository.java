package irt.web.entities.bom.repositories;

import irt.web.entities.bom.BomEntity;
import irt.web.entities.bom.BomEntityPK;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BomRepository  extends JpaRepository<BomEntity, BomEntityPK>{

	@Query("SELECT b FROM BomEntity b WHERE b.bomEntityPK.idTopComp = :idTopComp")
	List<BomEntity> findBomComponentsEntities(@Param("idTopComp") long idTopComp);
}
