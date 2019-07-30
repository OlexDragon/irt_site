package irt.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FirstDigitsRepository  extends JpaRepository<FirstDigitsEntity, Integer>{

	FirstDigitsEntity findOneByPartNumbetFirstChar(char partNumbetFirstChar);
}
