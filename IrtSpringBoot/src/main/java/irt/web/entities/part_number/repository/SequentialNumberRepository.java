package irt.web.entities.part_number.repository;

import irt.web.entities.part_number.SequentialNumberEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SequentialNumberRepository  extends JpaRepository<SequentialNumberEntity, Long>{
}
