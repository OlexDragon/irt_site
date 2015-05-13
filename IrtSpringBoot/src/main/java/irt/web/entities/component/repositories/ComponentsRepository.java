package irt.web.entities.component.repositories;

import irt.web.entities.component.ComponentEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentsRepository  extends JpaRepository<ComponentEntity, Long>{
    public ComponentEntity findOneByPartNumber(String partNumber);

	public Page<ComponentEntity> findFirst10ByPartNumberContaining(String partNumber, Pageable pageable);
}
