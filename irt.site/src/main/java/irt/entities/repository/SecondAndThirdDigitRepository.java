package irt.entities.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import irt.entities.SecondAndThirdDigitEntity;
import irt.entities.SecondAndThirdDigitPK;

public interface SecondAndThirdDigitRepository  extends JpaRepository<SecondAndThirdDigitEntity, SecondAndThirdDigitPK>{

	public List<SecondAndThirdDigitEntity> findByKeyIdFirstDigitOrderByDescriptionAsc( Integer firstLetter);
}
