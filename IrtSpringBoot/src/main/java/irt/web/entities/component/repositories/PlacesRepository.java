package irt.web.entities.component.repositories;

import irt.web.entities.component.PlaceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacesRepository  extends JpaRepository<PlaceEntity, Long>{
}
