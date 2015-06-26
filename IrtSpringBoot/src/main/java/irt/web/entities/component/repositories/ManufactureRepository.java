package irt.web.entities.component.repositories;

import irt.web.entities.all.ManufactureEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufactureRepository  extends JpaRepository<ManufactureEntity, Long>{
}
