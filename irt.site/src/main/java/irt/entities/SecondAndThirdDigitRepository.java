package irt.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecondAndThirdDigitRepository  extends JpaRepository<SecondAndThirdDigitEntity, SecondAndThirdDigitPK>{

	public List<SecondAndThirdDigitEntity> findByKeyIdFirstDigitOrderByDescriptionAsc( Integer firstLetter);
}
