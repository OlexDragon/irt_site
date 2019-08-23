package irt.entities.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import irt.entities.FirstDigitsEntity;

public interface FirstDigitsRepository  extends JpaRepository<FirstDigitsEntity, Integer>{

	FirstDigitsEntity findOneByPartNumbetFirstChar(char partNumbetFirstChar);
}
