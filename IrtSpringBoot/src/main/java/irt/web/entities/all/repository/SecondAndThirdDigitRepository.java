package irt.web.entities.all.repository;

import irt.web.entities.all.SecondAndThirdDigitEntity;
import irt.web.entities.all.SecondAndThirdDigitEntityPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SecondAndThirdDigitRepository  extends JpaRepository<SecondAndThirdDigitEntity, SecondAndThirdDigitEntityPK>{

	@Query("SELECT sfd FROM SecondAndThirdDigitEntity sfd WHERE sfd.secondAndThirdDigitEntityPK.idFirstDigits = :firstLetter")
	public SecondAndThirdDigitEntity findByFirstDigit(int firstLetter);
}
