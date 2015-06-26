package irt.web.entities.part_number.repository;

import irt.web.entities.part_number.FirstDigitsEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FirstDigitsRepository  extends JpaRepository<FirstDigitsEntity, Integer>{

	FirstDigitsEntity findOneByPartNumbetFirstChar(Character partNumbetFirstChar);
}
