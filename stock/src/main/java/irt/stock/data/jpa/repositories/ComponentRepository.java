package irt.stock.data.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Component;

public interface ComponentRepository extends CrudRepository<Component, Long> {

	Optional<Component> findByPartNumber(String partNumber);

	//	PN
	List<Component> findByPartNumberContainingOrderByPartNumber(String desired);
	//	MFR_PN
	List<Component> findDistinctByManufPartNumberContainingOrAlternativeComponentsAltMfrPartNumberContainingOrderByPartNumber(String mfrPN, String aMfrPN);
	//	MFR
	List<Component> findByManufactureNameContainingOrderByPartNumber(String desired);
	//	DESCRIPTION
	List<Component> findByDescriptionContainingOrderByPartNumber(String desired);
	//	VAL
	List<Component> findByValueContainingOrderByPartNumber(String desired);

	//	PN + MFR_PN
	List<Component> findByPartNumberContainingAndManufPartNumberContainingOrderByPartNumber(String pn, String mfrPN);
	//	PN + MFR
	List<Component> findByPartNumberContainingAndManufactureNameContainingOrderByPartNumber(String pn, String mfr);
	//	PN + VAL
	List<Component> findByPartNumberContainingAndValueContainingOrderByPartNumber(String pn, String value);
	//	PN + DESCRIPTION
	List<Component> findByPartNumberContainingAndDescriptionContainingOrderByPartNumber(String pn, String description);
	//	PN + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String pn, String description, String valu);

	//	PN + MFR_PN + MFR
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingOrderByPartNumber(String pn, String mfrPN, String mfrName);
	//	PN + MFR_PN + MFR + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingOrderByPartNumber(String pn, String mfrPN, String mfrName, String description);
	//	PN + MFR + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndDescriptionContainingOrderByPartNumber(String pn, String mfrName, String description);
	//	PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndValueContainingOrderByPartNumber(String pn, String mfrName, String value);
	//	PN + MFR + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String pn, String mfrName, String description, String value);
	//	PN + MFR_PN + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContainingOrderByPartNumber(String pn, String mfrPN, String description);
	//	PN + MFR_PN + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndValueContainingOrderByPartNumber(String pn, String mfrPN, String value);
	//	PN + MFR_PN + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String pn, String mfrPN, String description, String value);

	//	MFR_PN + MFR
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingOrderByPartNumber(String mfrPN, String mfrName);
	//	MFR + DESCRIPTION
	List<Component> findByManufactureNameContainingAndDescriptionContainingOrderByPartNumber(String mfrName, String description);
	//	MFR + VAL
	List<Component> findByManufactureNameContainingAndValueContainingOrderByPartNumber(String mfrName, String value);
	//	MFR + DESCRIPTION + VAL
	List<Component> findByManufactureNameContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String mfrName, String description, String value);
	//	MFR_PN + DESCRIPTION
	List<Component> findByManufPartNumberContainingAndDescriptionContainingOrderByPartNumber(String pn, String description);
	//	MFR_PN + VAL
	List<Component> findByManufPartNumberContainingAndValueContainingOrderByPartNumber(String pn, String value);
	//	MFR_PN + DESCRIPTION + VAL
	List<Component> findByManufPartNumberContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String pn, String value, String description);

	//	PN + MFR_PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndValueContainingOrderByPartNumber(String pn, String mfrPN, String mfrName, String value);

	//	PN + MFR_PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String pn, String mfrPN, String mfrName, String description, String value);

	//	PN + MFR_PN + MFR + DESCRIPTION
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingOrderByPartNumber(String mfrPN, String mfrName, String description);
	//	PN + MFR_PN + MFR + VAL
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndValueContainingOrderByPartNumber(String mfrPN, String mfrName, String value);
	//	PN + MFR_PN + MFR + DESCRIPTION + VAL
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContainingOrderByPartNumber(String mfrPN, String mfrName, String descrption, String value);

	//	DESCRIPTION + VAL
	List<Component> findByDescriptionContainingAndValueContainingOrderByPartNumber(String description, String value);

	Iterable<Component> findAllByOrderByPartNumberAsc();

	List<Component> findDistinctByPartNumberContainingOrManufPartNumberContainingOrAlternativeComponentsAltMfrPartNumberContainingOrderByPartNumber(String desiredPN, String mfrPn, String altMfrPn);

	Optional<Component> findByPartNumberOrManufPartNumber(String partNumber, String mfrPartNumber);
}
