package irt.stock.data.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Component;

public interface ComponentRepository extends CrudRepository<Component, Long> {

	Optional<Component> findByPartNumber(String partNumber);

	//	PN
	List<Component> findByPartNumberContaining(String desired);
	//	MFR_PN
	List<Component> findByManufPartNumberContaining(String desired);
	//	MFR
	List<Component> findByManufactureNameContaining(String desired);
	//	DESCRIPTION
	List<Component> findByDescriptionContaining(String desired);
	//	VAL
	List<Component> findByValueContaining(String desired);

	//	PN + MFR_PN
	List<Component> findByPartNumberContainingAndManufPartNumberContaining(String pn, String mfrPN);
	//	PN + MFR
	List<Component> findByPartNumberContainingAndManufactureNameContaining(String pn, String mfr);
	//	PN + VAL
	List<Component> findByPartNumberContainingAndValueContaining(String pn, String value);
	//	PN + DESCRIPTION
	List<Component> findByPartNumberContainingAndDescriptionContaining(String pn, String description);
	//	PN + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndDescriptionContainingAndValueContaining(String pn, String description, String valu);

	//	PN + MFR_PN + MFR
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContaining(String pn, String mfrPN, String mfrName);
	//	PN + MFR_PN + MFR + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(String pn, String mfrPN, String mfrName, String description);
	//	PN + MFR + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(String pn, String mfrName, String description);
	//	PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndValueContaining(String pn, String mfrName, String value);
	//	PN + MFR + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(String pn, String mfrName, String description, String value);
	//	PN + MFR_PN + DESCRIPTION
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContaining(String pn, String mfrPN, String description);
	//	PN + MFR_PN + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndValueContaining(String pn, String mfrPN, String value);
	//	PN + MFR_PN + DESCRIPTION + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndDescriptionContainingAndValueContaining(String pn, String mfrPN, String description, String value);

	//	MFR_PN + MFR
	List<Component> findByManufPartNumberContainingAndManufactureNameContaining(String mfrPN, String mfrName);
	//	MFR + DESCRIPTION
	List<Component> findByManufactureNameContainingAndDescriptionContaining(String mfrName, String description);
	//	MFR + VAL
	List<Component> findByManufactureNameContainingAndValueContaining(String mfrName, String value);
	//	MFR + DESCRIPTION + VAL
	List<Component> findByManufactureNameContainingAndDescriptionContainingAndValueContaining(String mfrName, String description, String value);
	//	MFR_PN + DESCRIPTION
	List<Component> findByManufPartNumberContainingAndDescriptionContaining(String pn, String description);
	//	MFR_PN + VAL
	List<Component> findByManufPartNumberContainingAndValueContaining(String pn, String value);
	//	MFR_PN + DESCRIPTION + VAL
	List<Component> findByManufPartNumberContainingAndDescriptionContainingAndValueContaining(String pn, String value, String description);

	//	PN + MFR_PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndValueContaining(String pn, String mfrPN, String mfrName, String value);

	//	PN + MFR_PN + MFR + VAL
	List<Component> findByPartNumberContainingAndManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(String pn, String mfrPN, String mfrName, String description, String value);

	//	PN + MFR_PN + MFR + DESCRIPTION
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContaining(String mfrPN, String mfrName, String description);
	//	PN + MFR_PN + MFR + VAL
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndValueContaining(String mfrPN, String mfrName, String value);
	//	PN + MFR_PN + MFR + DESCRIPTION + VAL
	List<Component> findByManufPartNumberContainingAndManufactureNameContainingAndDescriptionContainingAndValueContaining(String mfrPN, String mfrName, String descrption, String value);

	//	DESCRIPTION + VAL
	List<Component> findByDescriptionContainingAndValueContaining(String description, String value);
}
