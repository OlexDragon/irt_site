package irt.web.entities.part_number.repository;

import irt.web.entities.part_number.SecondAndThirdDigitEntity;
import irt.web.entities.part_number.SecondAndThirdDigitPK;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SecondAndThirdDigitRepository  extends JpaRepository<SecondAndThirdDigitEntity, SecondAndThirdDigitPK>{

	public List<SecondAndThirdDigitEntity> findByKeyIdFirstDigitOrderByDescriptionAsc(@Param("firstLetter") Integer firstLetter);

	public SecondAndThirdDigitEntity findOneByKeyIdAndFirstDigitsEntityPartNumbetFirstChar(String id, char partNumbetFirstChar);
}
